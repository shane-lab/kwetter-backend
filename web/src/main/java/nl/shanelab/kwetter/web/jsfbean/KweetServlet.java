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

    public int getFavouriteCount(long id) {
        Kweet kweet = kweetingService.getKweetById(id);

        if (kweet == null) {
            return 0;
        }

        int size = 0;
        try {
            size = kweetingService.getAmountOfFavourites(kweet);
        } catch (KweetException e) { }

        return size;
    }

    public int getMentionCount(long id) {
        Kweet kweet = kweetingService.getKweetById(id);

        if (kweet == null) {
            return 0;
        }

        int size = 0;
        try {
            size = kweetingService.getAmountOfMentions(kweet);
        } catch (KweetException e) { }

        return size;
    }

    public int getHashTagCount(long id) {
        Kweet kweet = kweetingService.getKweetById(id);

        if (kweet == null) {
            return 0;
        }

        int size = 0;
        try {
            size = kweetingService.getAmountOfHashtags(kweet);
        } catch (KweetException e) { }

        return size;
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
