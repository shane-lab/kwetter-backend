package nl.shanelab.kwetter.web.jsfbean.view;

import lombok.Data;
import nl.shanelab.kwetter.web.jsfbean.JsfBeanServlet;

import javax.annotation.PostConstruct;

@Data
public abstract class PaginatedViewServlet implements JsfBeanServlet {

    protected int page;

    protected int perPage;

    @PostConstruct
    private void onPostConstruct() {
        page = 0;
        perPage = 0;
    }
}
