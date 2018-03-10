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
        userShane.setId(usersIndex);
        users.put(++usersIndex, userAlice);
        userAlice.setId(usersIndex);
        users.put(++usersIndex, userBob);
        userBob.setId(usersIndex);

        assert userShane.getId() == 0;
        assert userAlice.getId() == 1;
        assert userBob.getId() == 2;

        // initialize HashTags
        long hashTagsIndex = -1;

        HashTag hashTagFhict = new HashTag("FHICT");
        HashTag hashTagKwetter = new HashTag("kwetter");

        hashTags.put(++hashTagsIndex, hashTagFhict);
        hashTagFhict.setId(hashTagsIndex);
        hashTags.put(++hashTagsIndex, hashTagKwetter);
        hashTagKwetter.setId(hashTagsIndex);

        assert hashTagFhict.getId() == 0;
        assert hashTagKwetter.getId() == 1;

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
        kweetShane.setId(kweetsIndex);
        kweets.put(++kweetsIndex, kweetShane1);
        kweetShane1.setId(kweetsIndex);
        kweets.put(++kweetsIndex, kweetAlice);
        kweetAlice.setId(kweetsIndex);
        kweets.put(++kweetsIndex, kweetBob);
        kweetBob.setId(kweetsIndex);
        kweets.put(++kweetsIndex, kweetBob1);
        kweetBob1.setId(kweetsIndex);

        assert kweetShane.getId() == 0;
        assert kweetShane1.getId() == 1;
        assert kweetAlice.getId() == 2;
        assert kweetBob.getId() == 3;
        assert kweetBob1.getId() == 4;

        // set kweets
        setUserKweet(userShane, kweetShane);
        setUserKweet(userShane, kweetShane1);

        setUserKweet(userAlice, kweetAlice);

        setUserKweet(userBob, kweetBob);
        setUserKweet(userBob, kweetBob1);

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
//        createFollow(userBob, userAlice); // disabled for demo purposes
    }

    /**
     * Create a follow between two users
     *
     * @param a The user to follow
     * @param b The user who is following
     */
    public void createFollow(User a, User b) {
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

        // omit circular reference recursion (lazy LAZY-implementation)
        User newUserA = new User(a.getUsername(), a.getPassword(), a.getRole());
        newUserA.setId(a.getId());
        newUserA.setBio(a.getBio());
        newUserA.setKweets(a.getKweets());

        assert newUserA.equals(a);

        User newUserB = new User(b.getUsername(), b.getPassword(), b.getRole());
        newUserB.setId(b.getId());
        newUserB.setBio(b.getBio());
        newUserB.setKweets(b.getKweets());

        assert newUserB.equals(b);

        followers.add(newUserB);
        following.add(newUserA);

        a.setFollowers(followers);
        b.setFollowing(following);

        users.replace(a.getId(), a);
        users.replace(b.getId(), b);
    }

    /**
     * Appends a Kweet to a user
     *
     * @param user The user to append the Kweet to
     * @param kweet The Kweet to append
     */
    public void setUserKweet(User user, Kweet kweet) {
        Set<Kweet> kweets = user.getKweets();
        if (kweets == null) {
            kweets = new HashSet<>();
        }

        // omit circular reference recursion (lazy LAZY-implementation)
        User newUser = new User(user.getUsername(), user.getPassword(), user.getRole());
        newUser.setId(user.getId());
        newUser.setBio(user.getBio());
        newUser.setFollowing(user.getFollowing());
        newUser.setFollowers(user.getFollowers());

        assert newUser.equals(user);

        Kweet newKweet = new Kweet(kweet.getMessage(), newUser);
        newKweet.setId(kweet.getId());
        newKweet.setCreatedAt(kweet.getCreatedAt());
        newKweet.setMentions(kweet.getMentions());
        newKweet.setHashTags(kweet.getHashTags());

        assert newKweet.equals(kweet);

        kweets.add(newKweet);

        user.setKweets(kweets);

        users.replace(user.getId(), user);
    }

    /**
     * Appends a hashtag to a Kweet
     *
     * @param kweet The Kweet to append the hashtag to
     * @param hashTag The hashtag to append
     */
    public void setKweetHashTag(Kweet kweet, HashTag hashTag) {
        Set<HashTag> hashTags = kweet.getHashTags();
        if (hashTags == null) {
            hashTags = new HashSet<>();
        }

        hashTags.add(hashTag);

        kweet.setHashTags(hashTags);

        kweets.replace(kweet.getId(), kweet);
    }

    /**
     * Appends a mention to a Kweet
     *
     * @param kweet The Kweet to append the mention to
     * @param user The mention to append
     */
    public void setKweetMention(Kweet kweet, User user) {
        Set<User> mentions = kweet.getMentions();
        if (mentions == null) {
            mentions = new HashSet<>();
        }

        mentions.add(user);

        kweet.setMentions(mentions);

        kweets.replace(kweet.getId(), kweet);
    }

    /**
     * Sets a Kweet as favourited by a user
     *
     * @param kweet The Kweet to favorite
     * @param user The user who favourited the Kweet
     */
    public void setKweetFavouritedBy(Kweet kweet, User user) {
        Set<User> favouritedBy = kweet.getFavoritedBy();
        if (favouritedBy == null) {
            favouritedBy = new HashSet<>();
        }
        Set<Kweet> favourited = user.getFavoriteKweets();
        if (favourited == null) {
            favourited = new HashSet<>();
        }

        // omit circular reference recursion (lazy LAZY-implementation)
        Kweet newKweet = new Kweet(kweet.getMessage(), kweet.getAuthor());
        newKweet.setId(kweet.getId());
        newKweet.setCreatedAt(kweet.getCreatedAt());
        newKweet.setHashTags(kweet.getHashTags());
        newKweet.setMentions(kweet.getMentions());

        assert newKweet.equals(kweet);

        User newUser = new User(user.getUsername(), user.getPassword(), user.getRole());
        newUser.setId(user.getId());
        newUser.setBio(user.getBio());
        newUser.setKweets(user.getKweets());
        newUser.setFollowing(user.getFollowing());
        newUser.setFollowers(user.getFollowers());

        assert newUser.equals(user);

        favouritedBy.add(newUser);
        favourited.add(newKweet);

        kweet.setFavoritedBy(favouritedBy);
        user.setFavoriteKweets(favourited);

        kweets.replace(kweet.getId(), kweet);
        users.replace(user.getId(), user);
    }
}