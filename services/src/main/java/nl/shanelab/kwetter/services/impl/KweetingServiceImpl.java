package nl.shanelab.kwetter.services.impl;

import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.dal.dao.HashTagDao;
import nl.shanelab.kwetter.dal.dao.KweetDao;
import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;
import nl.shanelab.kwetter.services.KweetingService;
import nl.shanelab.kwetter.services.exceptions.KweetException;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.exceptions.kweet.KweetFavouriteException;
import nl.shanelab.kwetter.services.exceptions.kweet.KweetNotFoundException;
import nl.shanelab.kwetter.services.exceptions.user.UserNotFoundException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;

@Stateless
@NoArgsConstructor
public class KweetingServiceImpl implements KweetingService {

    @InMemoryDao
    @Inject
    UserDao userDao;

    @InMemoryDao
    @Inject
    KweetDao kweetDao;

    @InMemoryDao
    @Inject
    HashTagDao hashTagDao;

    public Kweet createKweet(String message, User user) throws UserException {
        validateUser(user);

        return kweetDao.create(new Kweet(message, user));
    }

    public Kweet editKweet(Kweet kweet) throws KweetException {
        Kweet storedKweet = validateKweet(kweet);

        storedKweet.setMessage(kweet.getMessage());

        return kweetDao.edit(storedKweet);
    }

    public void removeKweet(Kweet kweet) throws KweetException {
        validateKweet(kweet);

        kweetDao.remove(kweet);
    }

    public void removeKweet(long id) throws KweetException {
        Kweet kweet = kweetDao.find(id);
        this.removeKweet(kweet);
    }

    public Kweet getKweetById(long id) {
        return kweetDao.find(id);
    }

    public Collection<Kweet> getAllKweets() {
        return kweetDao.findAll();
    }

    public Collection<Kweet> getNthLatestKweetsByUser(int nth, User user) throws UserException {
        validateUser(user);

        return userDao.getNthLatestKweets(nth, user);
    }

    public Collection<Kweet> getKweetsByUser(User user) throws UserException {
        validateUser(user);

        return kweetDao.getByUserId(user.getId());
    }

    public Collection<Kweet> getKweetsByUserName(String name) throws UserException {
        User user = userDao.getByUsername(name);

        return this.getKweetsByUser(user);
    }

    public Collection<Kweet> getKweetsByUserId(long id) throws UserException {
        User user = userDao.find(id);
        return this.getKweetsByUser(user);
    }

    public Collection<Kweet> getKweetsWithHashTag(HashTag hashTag) {
        return hashTag == null ? null : this.getKweetsWithHashTagName(hashTag.getName());
    }

    public Collection<Kweet> getKweetsWithHashTagName(String name) {
        return kweetDao.getByHashTagName(name);
    }

    public Collection<Kweet> getKweetsWithHashTagId(long id) {
        HashTag hashTag = hashTagDao.find(id);
        return this.getKweetsWithHashTag(hashTag);
    }

    public Collection<Kweet> getKweetsWithMention(User user) throws UserException {
        validateUser(user);

        return kweetDao.getByMention(user.getUsername());
    }

    public Collection<Kweet> getKweetsWithMentionByUserName(String name) throws UserException {
        validateUser(userDao.getByUsername(name));

        return kweetDao.getByMention(name);
    }

    public Collection<Kweet> getKweetsWithMentionByUserId(long id) throws UserException {
        User user = userDao.find(id);
        return this.getKweetsWithMention(user);
    }

    public Collection<Kweet> getFavouritedKweets(User user) throws UserException {
        return this.getFavouritedKweetsByUserName(user.getUsername());
    }

    public Collection<Kweet> getFavouritedKweetsByUserName(String name) throws UserException {
        validateUser(userDao.getByUsername(name));

        return kweetDao.getByFavoritedBy(name);
    }

    public Collection<Kweet> getFavouritedKweetsByUserId(long id) throws UserException {
        User user = userDao.find(id);
        return this.getFavouritedKweets(user);
    }

    public boolean isUserMentionedInKweet(Kweet kweet, User user) throws KweetException, UserException {
        validateKweet(kweet);

        validateUser(user);

        return kweetDao.isMentionedIn(kweet, user);
    }

    public boolean isKweetFavoritedByUser(Kweet kweet, User user) throws KweetException, UserException {
        validateKweet(kweet);

        validateUser(user);

        return kweetDao.isFavoritedBy(kweet, user);
    }

    public boolean isKweetOwnedByUser(Kweet kweet, User user) throws KweetException, UserException {
        Kweet foundKweet = validateKweet(kweet);

        User foundUser = validateUser(user);

        return  kweet.equals(foundKweet) &&
                user.equals(foundUser) &&
                kweet.getAuthor().equals(foundUser);
    }

    public void favouriteKweet(Kweet kweet, User user) throws KweetException, UserException {
        if (this.isKweetOwnedByUser(kweet, user)) {
            throw new KweetFavouriteException(KweetFavouriteException.FavouriteViolationType.SELF_FAVOURIITNG);
        }
        if (this.isKweetFavoritedByUser(kweet, user)) {
            throw new KweetFavouriteException(KweetFavouriteException.FavouriteViolationType.ALREADY_FAVOURITED);
        }

        kweetDao.favourite(kweet, user);
    }

    public void unFavouriteKweet(Kweet kweet, User user) throws KweetException, UserException {
        if (!this.isKweetFavoritedByUser(kweet, user)) {
            throw new KweetFavouriteException(KweetFavouriteException.FavouriteViolationType.NOT_FAVOURITED);
        }

        kweetDao.unFavourite(kweet, user);
    }

    public HashTag getHashTagById(long id) {
        return hashTagDao.find(id);
    }

    public HashTag getHashTagByName(String name) {
        return hashTagDao.getByName(name);
    }

    public Collection<HashTag> getAllHashTags() {
        return hashTagDao.findAll();
    }

    public Collection<HashTag> getTrendingHashTags(Date date) {
        return hashTagDao.getTrending(date);
    }

    /**
     * Checks if a Kweet exists
     *
     * @param kweet The Kweet to check
     * @throws KweetNotFoundException Throws a KweetNotFoundException if the Kweet was not found
     */
    private Kweet validateKweet(Kweet kweet) throws KweetNotFoundException {
        if (kweet == null) {
            throw new IllegalArgumentException();
        }
        Kweet foundKweet = kweetDao.find(kweet.getId());
        if (foundKweet == null || !kweet.equals(foundKweet)) {
            throw new KweetNotFoundException();
        }

        return foundKweet;
    }

    /**
     * Checks if a user exists
     *
     * @param user The user to check
     * @throws UserNotFoundException Throws an UserNotFoundException if the user was not found
     */
    private User validateUser(User user) throws UserNotFoundException {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        User foundUser = userDao.find(user.getId());
        if (foundUser == null || !user.equals(foundUser)) {
            throw new UserNotFoundException();
        }

        return foundUser;
    }

}
