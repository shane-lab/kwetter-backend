package nl.shanelab.kwetter.web.jsfbean.view;

import lombok.Data;
import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.services.KweetingService;
import nl.shanelab.kwetter.web.jsfbean.RoutesServlet;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
@Data
public class KweetViewServlet extends PaginatedViewServlet {

    private Pagination<Kweet> pagination;

    @Inject
    private KweetingService kweetingService;

    @PostConstruct
    private void onPostConstruct() {
        pagination = kweetingService.getAllKweets(page, perPage);
    }

    public void onDelete() {
        this.redirectRequest(this.appRouteUri(RoutesServlet.Routes.KWEETS.getPath(), true));
    }
}
