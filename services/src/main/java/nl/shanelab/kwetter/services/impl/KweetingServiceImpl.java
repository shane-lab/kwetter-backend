package nl.shanelab.kwetter.services.impl;

import nl.shanelab.kwetter.dal.dao.HashTagDao;
import nl.shanelab.kwetter.dal.dao.KweetDao;
import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;
import nl.shanelab.kwetter.services.KweetingService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;

@Stateless
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

    public boolean isUserFollowedBy(User a, User b) {
        return userDao.isFollowedBy(a, b);
    }

    public boolean isUserFollowing(User a, User b) {
        return userDao.isFollowing(a, b);
    }

    public void followUser(User a, User b) {
        userDao.createFollow(a, b);
    }

    public void unFollowUser(User a, User b) {
        userDao.unFollow(a, b);
    }

    public Kweet createKweet(String message, User user) {
        return kweetDao.create(new Kweet(message, user));
    }

    public Kweet editKweet(Kweet kweet) {
        return kweetDao.edit(kweet);
    }

    public void removeKweet(Kweet kweet) {
        kweetDao.remove(kweet);
    }

    public void removeKweet(long id) {
        Kweet kweet = kweetDao.find(id);
        this.removeKweet(kweet);
    }

    public Kweet getKweetById(long id) {
        return null;
    }

    public Collection<Kweet> getAllKweets() {
        return kweetDao.findAll();
    }

    public Collection<Kweet> getNthLatestKweetsByUser(int nth, User user) {
        return userDao.getNthLatestKweets(nth, user);
    }

    public Collection<Kweet> getKweetsByUser(User user) {
        return kweetDao.getByUserId(user.getId());
    }

    public Collection<Kweet> getKweetsByUserName(String name) {
        return kweetDao.getByUserName(name);
    }

    public Collection<Kweet> getKweetsByUserId(long id) {
        User user = userDao.find(id);
        return this.getKweetsByUser(user);
    }

    public Collection<Kweet> getKweetsWithHashTag(HashTag hashTag) {
        return this.getKweetsWithHashTagName(hashTag.getName());
    }

    public Collection<Kweet> getKweetsWithHashTagName(String name) {
        return kweetDao.getByHashTagName(name);
    }

    public Collection<Kweet> getKweetsWithHashTagId(long id) {
        HashTag hashTag = hashTagDao.find(id);
        return this.getKweetsWithHashTag(hashTag);
    }

    public Collection<Kweet> getKweetsWithMention(User user) {
        return this.getFavouritedKweetsByUserName(user.getUsername());
    }

    public Collection<Kweet> getKweetsWithMentionByUserName(String name) {
        return kweetDao.getByMention(name);
    }

    public Collection<Kweet> getKweetsWithMentionByUserId(long id) {
        User user = userDao.find(id);
        return this.getKweetsWithMention(user);
    }

    public Collection<Kweet> getFavouritedKweets(User user) {
        return this.getFavouritedKweetsByUserName(user.getUsername());
    }

    public Collection<Kweet> getFavouritedKweetsByUserName(String name) {
        return kweetDao.getByFavoritedBy(name);
    }

    public Collection<Kweet> getFavouritedKweetsByUserId(long id) {
        User user = userDao.find(id);
        return this.getFavouritedKweets(user);
    }

    public boolean isUserMentionedInKweet(Kweet kweet, User user) {
        return kweetDao.isMentionedIn(kweet, user);
    }

    public boolean isKweetFavoritedByUser(Kweet kweet, User user) {
        return kweetDao.isFavoritedBy(kweet, user);
    }

    public void favouriteKweet(Kweet kweet, User user) {
        kweetDao.favourite(kweet, user);
    }

    public void unFavouriteKweet(Kweet kweet, User user) {
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
}
