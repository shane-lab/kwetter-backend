package nl.shanelab.kwetter.web.jsfbean.view;

import lombok.Data;
import nl.shanelab.kwetter.web.jsfbean.JsfBeanServlet;

import javax.annotation.PostConstruct;

@Data
public abstract class PaginatedViewServlet implements JsfBeanServlet {

    protected int page;

    protected int perPage;

    public abstract int pages();

    @PostConstruct
    private void onPostConstruct() {
        String requestedPage = this.getRequest().getParameter("page");
        if (requestedPage != null) {
            try {
                page = Integer.valueOf(requestedPage);
            } catch (Exception e) { }
        }
        String requestedPerPage = this.getRequest().getParameter("size");
        if (requestedPerPage != null) {
            try {
                perPage = Integer.valueOf(requestedPerPage);
            } catch (Exception e) { }
        }
    }
}
