package nl.shanelab.kwetter.web.jsfbean.view;

import lombok.Data;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.services.KweetingService;
import nl.shanelab.kwetter.web.jsfbean.JsfBeanServlet;
import nl.shanelab.kwetter.web.jsfbean.RoutesServlet;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
@Data
public class UpdateKweetViewServlet implements JsfBeanServlet {

    private Kweet kweet;

    @Inject
    private KweetingService kweetingService;

    @PostConstruct
    private void onPostConstruct() {
        String sid = this.getRequest().getParameter("id");
        if (sid == null) {
            this.redirectRequest(this.appRouteUri(RoutesServlet.Routes.KWEETS.getPath(), false));
            return;
        }

        Long l = null;
        try {
            l = Long.parseLong(sid);
        } catch (Exception e) {
            this.redirectRequest(this.appRouteUri(RoutesServlet.Routes.KWEETS.getPath(), false));
            return;
        }

        kweet = kweetingService.getKweetById(l);
    }
}
