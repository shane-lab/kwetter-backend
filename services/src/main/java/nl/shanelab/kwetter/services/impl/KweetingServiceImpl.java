package nl.shanelab.kwetter.services.impl;

import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.dal.dao.HashTagDao;
import nl.shanelab.kwetter.dal.dao.KweetDao;
import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.JPADao;
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

    @JPADao
    @Inject
    UserDao userDao;

    @JPADao
    @Inject
    KweetDao kweetDao;

    @JPADao
    @Inject
    HashTagDao hashTagDao;

    public int getAmountOfKweets() {
        return kweetDao.count();
    }

    public int getAmountOfKweets(User user) {
        return kweetDao.getAmountOfKweets(user);
    }

    public int getAmountOfHashtags() {
        return hashTagDao.count();
    }

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

    public Pagination<Kweet> getAllKweets(int page) {
        return kweetDao.findAll(page);
    }

    public Pagination<Kweet> getAllKweets(int page, int size) {
        return kweetDao.findAll(page, size);
    }

    public Collection<Kweet> getNthLatestKweetsByUser(int nth, User user) throws UserException {
        validateUser(user);

        return userDao.getNthLatestKweets(nth, user);
    }

    public Pagination<Kweet> getKweetsByUser(User user, int page) throws UserException {
        validateUser(user);

        return kweetDao.getByUserId(user.getId(), page);
    }

    public Pagination<Kweet> getKweetsByUser(User user, int page, int size) throws UserException {
        validateUser(user);

        return kweetDao.getByUserId(user.getId(), page, size);
    }

    public Pagination<Kweet> getKweetsByUserName(String name, int page) throws UserException {
        validateUser(userDao.getByUsername(name));

        return kweetDao.getByUserName(name, page);
    }

    public Pagination<Kweet> getKweetsByUserName(String name, int page, int size) throws UserException {
        validateUser(userDao.getByUsername(name));

        return kweetDao.getByUserName(name, page, size);
    }

    public Pagination<Kweet> getKweetsByUserId(long id, int page) throws UserException {
        validateUser(userDao.find(id));

        return kweetDao.getByUserId(id, page);
    }

    public Pagination<Kweet> getKweetsByUserId(long id, int page, int size) throws UserException {
        validateUser(userDao.find(id));

        return kweetDao.getByUserId(id, page, size);
    }

    public Pagination<Kweet> getKweetsWithHashTag(HashTag hashTag, int page) {
        return hashTag == null ? null : this.getKweetsWithHashTagName(hashTag.getName(), page);
    }

    public Pagination<Kweet> getKweetsWithHashTag(HashTag hashTag, int page, int size) {
        return hashTag == null ? null : this.getKweetsWithHashTagName(hashTag.getName(), page, size);
    }

    public Pagination<Kweet> getKweetsWithHashTagName(String name, int page) {
        return kweetDao.getByHashTagName(name, page);
    }

    public Pagination<Kweet> getKweetsWithHashTagName(String name, int page, int size) {
        return kweetDao.getByHashTagName(name, page, size);
    }

    public Pagination<Kweet> getKweetsWithHashTagId(long id, int page) {
        return kweetDao.getByHashTagId(id, page);
    }

    public Pagination<Kweet> getKweetsWithHashTagId(long id, int page, int size) {
        return kweetDao.getByHashTagId(id, page, size);
    }

    public Pagination<Kweet> getKweetsWithMention(User user, int page) throws UserException {
        validateUser(user);

        return kweetDao.getByMention(user.getUsername(), page);
    }

    public Pagination<Kweet> getKweetsWithMention(User user, int page, int size) throws UserException {
        validateUser(user);

        return kweetDao.getByMention(user.getUsername(), page, size);
    }

    public Pagination<Kweet> getKweetsWithMentionByUserName(String name, int page) throws UserException {
        validateUser(userDao.getByUsername(name));

        return kweetDao.getByMention(name, page);
    }

    public Pagination<Kweet> getKweetsWithMentionByUserName(String name, int page, int size) throws UserException {
        validateUser(userDao.getByUsername(name));

        return kweetDao.getByMention(name, page, size);
    }

    public Pagination<Kweet> getKweetsWithMentionByUserId(long id, int page) throws UserException {
        User user = userDao.find(id);

        return this.getKweetsWithMention(user, page);
    }

    public Pagination<Kweet> getKweetsWithMentionByUserId(long id, int page, int size) throws UserException {
        User user = userDao.find(id);

        return this.getKweetsWithMention(user, page, size);
    }

    public Pagination<Kweet> getFavouritedKweets(User user, int page) throws UserException {
        return this.getFavouritedKweetsByUserName(user.getUsername(), page);
    }

    public Pagination<Kweet> getFavouritedKweets(User user, int page, int size) throws UserException {
        return this.getFavouritedKweetsByUserName(user.getUsername(), page, size);
    }

    public Pagination<Kweet> getFavouritedKweetsByUserName(String name, int page) throws UserException {
        validateUser(userDao.getByUsername(name));

        return kweetDao.getByFavoritedBy(name, page);
    }

    public Pagination<Kweet> getFavouritedKweetsByUserName(String name, int page, int size) throws UserException {
        validateUser(userDao.getByUsername(name));

        return kweetDao.getByFavoritedBy(name, page, size);
    }

    public Pagination<Kweet> getFavouritedKweetsByUserId(long id, int page) throws UserException {
        User user = userDao.find(id);

        return this.getFavouritedKweets(user, page);
    }

    public Pagination<Kweet> getFavouritedKweetsByUserId(long id, int page, int size) throws UserException {
        User user = userDao.find(id);

        return this.getFavouritedKweets(user, page, size);
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

    public Kweet getMostFavouritedKweet() {
        return kweetDao.getMostFavourited();
    }

    public HashTag getHashTagById(long id) {
        return hashTagDao.find(id);
    }

    public HashTag getHashTagByName(String name) {
        return hashTagDao.getByName(name);
    }

    public Pagination<HashTag> getAllHashTags(int page) {
        return hashTagDao.findAll(page);
    }

    public Pagination<HashTag> getAllHashTags(int page, int size) {
        return hashTagDao.findAll(page, size);
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
