package nl.shanelab.kwetter.web.jsfbean.view;

import lombok.Data;
import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.web.jsfbean.RoutesServlet;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
@Data
public class UserViewServlet extends PaginatedViewServlet {

    private Pagination<User> pagination;

    @Inject
    private UserService userService;

    @PostConstruct
    private void onPostConstruct() {
        pagination = userService.getAllUsers(page, perPage);
    }

    public void onDelete() {
        this.redirectRequest(this.appRouteUri(RoutesServlet.Routes.PROFILES.getPath(), true));
    }
}
