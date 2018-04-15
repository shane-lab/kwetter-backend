package nl.shanelab.kwetter.web.jsfbean;

import lombok.Data;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.services.KweetingService;
import nl.shanelab.kwetter.services.exceptions.KweetException;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
@Data
public class KweetServlet implements JsfBeanServlet {

    @Inject
    private KweetingService kweetingService;

    public int count() {
        return kweetingService.getAmountOfKweets();
    }

    @RolesAllowed({"Administrator"})
    public boolean delete(long id) {
        Kweet kweet = kweetingService.getKweetById(id);

        if (kweet == null) {
            return false;
        }

        try {
            kweetingService.removeKweet(kweet);
        } catch (KweetException e) {
            return false;
        }

        return true;
    }
}
