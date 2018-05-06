package nl.shanelab.kwetter.web.jsfbean.view;

import lombok.Data;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.KweetingService;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.web.jsfbean.JsfBeanServlet;
import nl.shanelab.kwetter.web.jsfbean.RoutesServlet;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.Collections;

@Named
@ViewScoped
@Data
public class UpdateUserViewServlet implements JsfBeanServlet {

    private User user;

    private Collection<Kweet> kweets;

    private int currentRoleId;
    private int pendingRoleId;

    @Inject
    private UserService userService;

    @Inject
    private KweetingService kweetingService;

    @PostConstruct
    private void onPostConstruct() {
        String sid = this.getRequest().getParameter("id");
        if (sid == null) {
            this.redirectRequest(this.appRouteUri(RoutesServlet.Routes.PROFILES.getPath(), false));
            return;
        }

        Long l = null;
        try {
            l = Long.parseLong(sid);
        } catch (Exception e) {
            this.redirectRequest(this.appRouteUri(RoutesServlet.Routes.PROFILES.getPath(), false));
            return;
        }

        user = userService.getById(l);
        pendingRoleId = currentRoleId = user.getRole().getId();

        try {
            kweets = kweetingService.getNthLatestKweetsByUser(10, user);
        } catch (UserException e) {
            kweets = Collections.EMPTY_SET;
        }
    }

    public void handleRoleChange(AjaxBehaviorEvent event) {
        Object value = ((UIOutput) event.getSource()).getValue();
        if (value instanceof Integer) {
            int roleId = (Integer) value;
            if (roleId == -1) {
                return;
            }
            pendingRoleId = roleId;
        }
    }

    @RolesAllowed({"Administrator"})
    public void handleRoleConfirm(AjaxBehaviorEvent event) {
        if (pendingRoleId == currentRoleId) {
            return;
        }

        try {
            Role role = Role.findById(pendingRoleId);

            if (userService.getById(user.getId()) == null) {
                this.redirectRequest(this.appRouteUri(RoutesServlet.Routes.PROFILES.getPath()));
                return;
            }

            userService.setRole(role, user);

            currentRoleId = role.getId();
        } catch (Exception e) {
            pendingRoleId = currentRoleId;
        }
    }

    public void handleRoleDeny(AjaxBehaviorEvent event) {
        pendingRoleId = currentRoleId;
    }

    public boolean hasBiography() {
        return user != null && user.getBio() != null && user.getBio().length() > 0;
    }
}
