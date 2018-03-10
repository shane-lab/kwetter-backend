package nl.shanelab.kwetter.dal.ejb;

import lombok.Getter;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton
@Getter
/**
 * An injectable wrapper class (singleton instance) for collections of initial test data, used to mock and test the expected behaviours of the to-be-validated entities
 */
public class DummyData {

    /**
     * A collection of User entities, mapped by their serializable identifier
     */
    private final Map<Long, User> users = new HashMap<>();

    /**
     * A collection of Kweet entities, mapped by their serializable identifier
     */
    private final Map<Long, Kweet> kweets = new HashMap<>();

    /**
     * A collection of HashTag entities, mapped by their serializable identifier
     */
    private final Map<Long, HashTag> hashTags = new HashMap<>();

    @PostConstruct
    /**
     * Initializes the local collections when invoked/injected
     */
    private void onPostConstruct()
    {
        // initialize users
        long usersIndex = -1;

        User userShane = new User("Shane", "shane", Role.ADMINISTRATOR);
        User userAlice = new User("Alice", "alice", Role.MODERATOR);
        User userBob = new User("Bob", "bob", Role.USER);

        users.put(++usersIndex, userShane);
        users.put(++usersIndex, userAlice);
        users.put(++usersIndex, userBob);

        // initialize HashTags
        long hashTagsIndex = -1;

        HashTag hashTagFhict = new HashTag("FHICT");
        HashTag hashTagKwetter = new HashTag("kwetter");

        hashTags.put(++hashTagsIndex, hashTagFhict);
        hashTags.put(++hashTagsIndex, hashTagKwetter);

        // initialize Kweets
        long kweetsIndex = -1;

        // kweets for userShane
        Kweet kweetShane = new Kweet("Message by Shane", userShane);
        Kweet kweetShane1 = new Kweet("Message with hashtags by Shane #FHICT #kwetter", userShane);

        // kweets for userAlice
        Kweet kweetAlice = new Kweet("Message by Alice", userAlice);

        // kweets for userBob
        Kweet kweetBob = new Kweet("Message by Bob", userBob);
        Kweet kweetBob1 = new Kweet("Message by Bob, in reply to @Shane", userBob);

        kweets.put(++kweetsIndex, kweetShane);
        kweets.put(++kweetsIndex, kweetShane1);
        kweets.put(++kweetsIndex, kweetAlice);
        kweets.put(++kweetsIndex, kweetBob);
        kweets.put(++kweetsIndex, kweetBob1);

        // set hashtags
        setKweetHashTag(kweetShane1, hashTagFhict);
        setKweetHashTag(kweetShane1, hashTagKwetter);

        // set mentions
        setKweetMention(kweetBob1, userShane);

        // set favorited
        setKweetFavouritedBy(kweetShane, userAlice);
        setKweetFavouritedBy(kweetShane1, userAlice);
        setKweetFavouritedBy(kweetShane1, userBob);

        setKweetFavouritedBy(kweetAlice, userShane);

        // set following
        createFollow(userShane, userAlice);
        createFollow(userShane, userBob);

        createFollow(userAlice, userShane);
        createFollow(userAlice, userBob);

        createFollow(userBob, userShane);
        createFollow(userBob, userAlice);
    }

    /**
     * Create a follow between two users
     *
     * @param a The user to follow
     * @param b The user who is following
     */
    private void createFollow(User a, User b) {
        if (a.equals(b)) {
            return;
        }

        Set<User> followers = a.getFollowers();
        if (followers == null) {
            followers = new HashSet<>();
        }
        Set<User> following = b.getFollowing();
        if (following == null) {
            following = new HashSet<>();
        }

        followers.add(b);
        following.add(a);

        a.setFollowers(followers);
        b.setFollowing(following);

        users.put(a.getId(), a);
        users.put(b.getId(), b);
    }

    /**
     * Appends a hashtag to a Kweet
     *
     * @param kweet The Kweet to append the hashtag to
     * @param hashTag The hashtag to append
     */
    private void setKweetHashTag(Kweet kweet, HashTag hashTag) {
        Set<HashTag> hashTags = kweet.getHashTags();
        if (hashTags == null) {
            hashTags = new HashSet<>();
        }

        hashTags.add(hashTag);

        kweet.setHashTags(hashTags);

        kweets.put(kweet.getId(), kweet);
    }

    /**
     * Appends a mention to a Kweet
     *
     * @param kweet The Kweet to append the mention to
     * @param user The mention to append
     */
    private void setKweetMention(Kweet kweet, User user) {
        Set<User> mentions = kweet.getMentions();
        if (mentions == null) {
            mentions = new HashSet<>();
        }

        mentions.add(user);

        kweet.setMentions(mentions);

        kweets.put(kweet.getId(), kweet);
    }

    /**
     * Sets a Kweet as favourited by a user
     *
     * @param kweet The Kweet to favorite
     * @param user The user who favourited the Kweet
     */
    private void setKweetFavouritedBy(Kweet kweet, User user) {
        Set<User> favouritedBy = kweet.getFavoritedBy();
        if (favouritedBy == null) {
            favouritedBy = new HashSet<>();
        }
        Set<Kweet> favourited = user.getFavoriteKweets();
        if (favourited == null) {
            favourited = new HashSet<>();
        }

        favouritedBy.add(user);
        favourited.add(kweet);

        kweet.setFavoritedBy(favouritedBy);
        user.setFavoriteKweets(favourited);

        kweets.put(kweet.getId(), kweet);
        users.put(user.getId(), user);
    }
}