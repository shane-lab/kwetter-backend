package nl.shanelab.kwetter.web.jsfbean.view;

import lombok.Data;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.KweetingService;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.web.jsfbean.JsfBeanServlet;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;

@Named
@ViewScoped
@Data
public class DashboardViewServlet implements JsfBeanServlet {

    private User mostFollowedUser;

    private Kweet mostFavouritedKweet;

    private Collection<HashTag> trendingHashtags;

    @Inject
    private UserService userService;

    @Inject
    private KweetingService kweetingService;

    @PostConstruct
    private void onPostConstruct() {
        mostFollowedUser = userService.getMostFollowed();
        mostFavouritedKweet = kweetingService.getMostFavouritedKweet();
        trendingHashtags = kweetingService.getTrendingHashTags(Date.valueOf(LocalDateTime.now().toLocalDate()));
    }
}
