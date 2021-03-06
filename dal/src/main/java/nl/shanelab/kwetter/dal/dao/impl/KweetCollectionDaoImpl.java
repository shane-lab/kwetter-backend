package nl.shanelab.kwetter.dal.dao.impl;

import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.dal.dao.KweetDao;
import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.ejb.DummyData;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;
import nl.shanelab.kwetter.util.Patterns;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@InMemoryDao
@Stateless
@NoArgsConstructor
public class KweetCollectionDaoImpl extends BaseCollectionDao<Kweet, Long> implements KweetDao {

    @Inject
    public KweetCollectionDaoImpl(DummyData data) {
        this.data = data;
    }

    public int count() {
        return data.getKweets().size();
    }

    public Kweet create(Kweet kweet) {
        if (kweet.getAuthor() == null) {
            return null;
        }
        User foundUser = data.getUsers().get(kweet.getAuthor().getId());
        if (foundUser != null && !foundUser.equals(kweet.getAuthor())) {
            return null; // user not in repository
        }

        long nextId = (long) this.count();

        kweet.setId(nextId);
        kweet.setCreatedAt(LocalDateTime.now());
        // do not permit predefined collections, parse Kweet message
        kweet.setHashTags(null);
        kweet.setFavoritedBy(null);
        kweet.setMentions(null);

        data.getKweets().put(nextId, kweet);

        data.setUserKweet(kweet.getAuthor(), kweet);

        this.handleHashTags(kweet);

        this.handleMentions(kweet);

        return kweet;
    }

    public Kweet edit(Kweet kweet) {
        if (this.find(kweet.getId()) != null) {
            data.getKweets().replace(kweet.getId(), kweet);

            return kweet;
        }

        return this.create(kweet);
    }

    public Kweet find(Long id) {
        return data.getKweets().get(id);
    }

    public Collection<Kweet> findAll() {
        return data.getKweets().values();
    }

    public void remove(Long id) {
        data.getKweets().remove(id);
    }

    public int getAmountOfKweets(User user) {
        return user != null && user.getKweets() != null ? user.getKweets().size() : 0;
    }

    public int getAmountOfFavourites(long id) {
        Kweet kweet = this.find(id);
        if (kweet == null) {
            return 0;
        }
        return kweet.getFavoritedBy() != null ? kweet.getFavoritedBy().size() : 0;
    }

    public int getAmountOfHashTags(long id) {
        Kweet kweet = this.find(id);
        if (kweet == null) {
            return 0;
        }
        return kweet.getHashTags() != null ? kweet.getHashTags().size() : 0;
    }

    public int getAmountOfMentions(long id) {
        Kweet kweet = this.find(id);
        if (kweet == null) {
            return 0;
        }
        return kweet.getMentions() != null ? kweet.getMentions().size() : 0;
    }

    public Collection<Kweet> getByUserName(String name) {
        AtomicReference<Collection<Kweet>> kweetsRef = new AtomicReference<>(new HashSet<>());

        this.findAll().forEach(kweet -> {
            if (kweet.getAuthor().getUsername().equals(name)) {
                Collection<Kweet> kweets = kweetsRef.get();

                kweets.add(kweet);

                kweetsRef.set(kweets);
            }
        });

        return kweetsRef.get();
    }

    @Override
    public Pagination<Kweet> getByUserName(String name, int page, int size) {
        return null;
    }

    public Collection<Kweet> getByUserId(Long id) {
        AtomicReference<Collection<Kweet>> kweetsRef = new AtomicReference<>(new HashSet<>());

        this.findAll().forEach(kweet -> {
            if (kweet.getAuthor().getId() == id) {
                Collection<Kweet> kweets = kweetsRef.get();

                kweets.add(kweet);

                kweetsRef.set(kweets);
            }
        });

        return kweetsRef.get();
    }

    @Override
    public Pagination<Kweet> getByUserId(Long id, int page, int size) {
        return null;
    }

    public Collection<Kweet> getByHashTagName(String name) {
        AtomicReference<Collection<Kweet>> kweetsRef = new AtomicReference<>(new HashSet<>());

        this.findAll().forEach(kweet -> {
            if (kweet.getHashTags() != null) {
                Collection<Kweet> kweets = kweetsRef.get();

                kweet.getHashTags().forEach(hashTag -> {
                    if (hashTag.getName().equals(name)) {
                        kweets.add(kweet);
                    }
                });

                kweetsRef.set(kweets);
            }
        });

        return kweetsRef.get();
    }

    @Override
    public Pagination<Kweet> getByHashTagName(String name, int page, int size) {
        return null;
    }

    public Collection<Kweet> getByHashTagId(Long id) {
        AtomicReference<Collection<Kweet>> kweetsRef = new AtomicReference<>(new HashSet<>());

        this.findAll().forEach(kweet -> {
            if (kweet.getHashTags() != null) {
                Collection<Kweet> kweets = kweetsRef.get();

                kweet.getHashTags().forEach(hashTag -> {
                    if (hashTag.getId() == id) {
                        kweets.add(kweet);
                    }
                });

                kweetsRef.set(kweets);
            }
        });

        return kweetsRef.get();
    }

    @Override
    public Pagination<Kweet> getByHashTagId(Long id, int page, int size) {
        return null;
    }

    public Collection<Kweet> getByMention(String name) {
        AtomicReference<Collection<Kweet>> kweetsRef = new AtomicReference<>(new HashSet<>());

        this.findAll().forEach(kweet -> {
            if (kweet.getMentions() != null) {
                Collection<Kweet> kweets = kweetsRef.get();

                kweet.getMentions().forEach(mention -> {
                    if (mention.getUsername().equals(name)) {
                        kweets.add(kweet);
                    }
                });

                kweetsRef.set(kweets);
            }
        });

        return kweetsRef.get();
    }

    @Override
    public Pagination<Kweet> getByMention(String name, int page, int size) {
        return null;
    }

    public Collection<Kweet> getByFavoritedBy(String name) {
        AtomicReference<Collection<Kweet>> kweetsRef = new AtomicReference<>(new HashSet<>());

        this.findAll().forEach(kweet -> {
            if (kweet.getFavoritedBy() != null) {
                Collection<Kweet> kweets = kweetsRef.get();

                kweet.getFavoritedBy().forEach(favoritedBy -> {
                    if (favoritedBy.getUsername().equals(name)) {
                        kweets.add(kweet);
                    }
                });

                kweetsRef.set(kweets);
            }
        });

        return kweetsRef.get();
    }

    @Override
    public Pagination<Kweet> getByFavoritedBy(String name, int page, int size) {
        return null;
    }

    @Override
    public Pagination<Kweet> getTimeline(String name, int page, int size) {
        return null;
    }

    public boolean isFavoritedBy(Kweet kweet, User user) {
        if (kweet == null) {
            return false;
        }
        Kweet foundKweet = data.getKweets().get(kweet.getId());
        if (!foundKweet.equals(kweet)) {
            return false; // not in repository
        }
        if (kweet.getFavoritedBy() == null) {
            return false; // not favourited by anyone
        }

        AtomicReference<Boolean> flagRef = new AtomicReference<>(false);

        kweet.getFavoritedBy().forEach(favoritedBy -> {
            if (favoritedBy.equals(user)) {
                flagRef.set(true);
                return;
            }
        });

        return flagRef.get();
    }

    public boolean isMentionedIn(Kweet kweet, User user) {
        if (kweet == null) {
            return false;
        }
        Kweet foundKweet = data.getKweets().get(kweet.getId());
        if (!foundKweet.equals(kweet)) {
            return false; // not in repository
        }
        if (kweet.getMentions() == null) {
            return false; // no mentions
        }

        AtomicReference<Boolean> flagRef = new AtomicReference<>(false);

        kweet.getMentions().forEach(mentioned -> {
            if (mentioned.equals(user)) {
                flagRef.set(true);
                return;
            }
        });

        return flagRef.get();
    }

    public void favourite(Kweet kweet, User user) {
        if (isFavoritedBy(kweet, user)) {
            return;
        }
        if (kweet.getAuthor().equals(user)) {
            return;
        }

        data.setKweetFavouritedBy(kweet, user);
    }

    public void unFavourite(Kweet kweet, User user) {
        if (!isFavoritedBy(kweet, user)) {
            return;
        }

        if (kweet.getFavoritedBy() != null) {
            kweet.getFavoritedBy().remove(user);
            edit(kweet);
        }
        if (user.getFavoriteKweets() != null) {
            user.getFavoriteKweets().remove(kweet);
            edit(kweet);
        }
    }

    public Kweet getMostFavourited() {
        return null;
    }

    private void handleMentions(Kweet kweet) {
        if (kweet == null || data.getUsers().size() <= 0) {
            return;
        }

        Pattern pattern = Pattern.compile(Patterns.MENTION_PATTERN);
        Matcher matcher = pattern.matcher(kweet.getMessage());

        Set<String> mentions = new HashSet<>();

        while (matcher.find()) {
            mentions.add(matcher.group(0).substring(1));
        }

        for (User user : data.getUsers().values()) {
            if (mentions.contains(user.getUsername())) {
                data.setKweetMention(kweet, user);
            }
        }
    }

    private void handleHashTags(Kweet kweet) {
        if (kweet == null) {
            return;
        }

        Pattern pattern = Pattern.compile(Patterns.HASHTAG_PATTERN);
        Matcher matcher = pattern.matcher(kweet.getMessage());

        Set<String> hashTagNames = new HashSet<>();

        while (matcher.find()) {
            hashTagNames.add(matcher.group(0).substring(1));
        }

        for (HashTag hashTag : data.getHashTags().values()) {
            if (hashTagNames.contains(hashTag.getName())) {
                hashTagNames.remove(hashTag.getName());

                data.setKweetHashTag(kweet, hashTag);
            }
        }

        for (String hashTagName : hashTagNames) {
            HashTag hashTag = new HashTag(hashTagName);
            hashTag.setId(data.getHashTags().size());

            data.getHashTags().put((long) data.getHashTags().size(), hashTag);

            data.setKweetHashTag(kweet, hashTag);
        }
    }
}
