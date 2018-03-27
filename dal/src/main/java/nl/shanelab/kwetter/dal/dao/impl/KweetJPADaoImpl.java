package nl.shanelab.kwetter.dal.dao.impl;

import nl.shanelab.kwetter.dal.dao.KweetDao;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.JPADao;
import nl.shanelab.kwetter.util.Patterns;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JPADao
@Stateless
public class KweetJPADaoImpl extends BaseJPADao<Kweet, Long> implements KweetDao {

    @Override
    public Kweet create(Kweet kweet) {
//        if (!manager.contains(kweet.getAuthor()) || manager.find(User.class, kweet.getAuthor().getId()) != null) {
//            return null;
//        }

        super.create(kweet);

        handleMentions(kweet);
        handleHashTags(kweet);

        kweet.getAuthor().getKweets().add(kweet);

        manager.merge(kweet.getAuthor());

        return kweet;
    }

    public Collection<Kweet> getByUserName(String name) {
        Query query = manager.createNamedQuery("Kweet.findByUserName", Kweet.class);
        query.setParameter("username", name);

        return query.getResultList();
    }

    public Collection<Kweet> getByUserId(Long id) {
        Query query = manager.createNamedQuery("Kweet.findByUserId", Kweet.class);
        query.setParameter("id", id);

        return query.getResultList();
    }

    public Collection<Kweet> getByHashTagName(String name) {
        Query query = manager.createNamedQuery("Kweet.findByHashTagName", Kweet.class);
        query.setParameter("name", name);

        return query.getResultList();
    }

    public Collection<Kweet> getByHashTagId(Long id) {
        Query query = manager.createNamedQuery("Kweet.findByHashTagId", Kweet.class);
        query.setParameter("id", id);

        return query.getResultList();
    }

    public Collection<Kweet> getByMention(String name) {
        Query query = manager.createNamedQuery("Kweet.findByMentioned", Kweet.class);
        query.setParameter("username", name);

        return query.getResultList();
    }

    public Collection<Kweet> getByFavoritedBy(String name) {
        Query query = manager.createNamedQuery("Kweet.findByMentioned", Kweet.class);
        query.setParameter("username", name);

        return query.getResultList();
    }

    public boolean isFavoritedBy(Kweet kweet, User user) {
        Query query = manager.createNamedQuery("Kweet.isFavoritedBy", Kweet.class);
        query.setParameter("id", kweet.getId());
        query.setParameter("username", user.getUsername());

        return (Long) JPAResult.getSingleResultOrNull(query) > 0;
    }

    public boolean isMentionedIn(Kweet kweet, User user) {
        Query query = manager.createNamedQuery("Kweet.isMentionedIn", Kweet.class);
        query.setParameter("id", kweet.getId());
        query.setParameter("username", user.getUsername());

        return (Long) JPAResult.getSingleResultOrNull(query) > 0;
    }

    public void favourite(Kweet kweet, User user) {
        if (isFavoritedBy(kweet, user)) {
            return;
        }
        if (kweet.getAuthor().equals(user)) {
            return;
        }

        kweet.getFavoritedBy().add(user);
        user.getFavoriteKweets().add(kweet);

        manager.merge(kweet);
        manager.merge(user);
    }

    public void unFavourite(Kweet kweet, User user) {
        if (!isFavoritedBy(kweet, user)) {
            return;
        }

        if (kweet.getFavoritedBy().contains(user)) {
            kweet.getFavoritedBy().remove(user);
        }
        if (user.getFavoriteKweets().contains(kweet)) {
            user.getFavoriteKweets().remove(kweet);
        }

        manager.merge(kweet);
        manager.merge(user);
    }

    private void handleHashTags(Kweet kweet) {
        Pattern pattern = Pattern.compile(Patterns.HASHTAG_PATTERN);
        Matcher matcher = pattern.matcher(kweet.getMessage());

        Set<String> hashTagNames = new HashSet<>();

        while (matcher.find()) {
            hashTagNames.add(matcher.group(0).substring(1));
        }

        Set<HashTag> hashTags = new HashSet<>();

        for (String hashTagName : hashTagNames) {
            try {
                HashTag hashTag = findHashTag(hashTagName);
                if (hashTag == null) {
                    hashTag = new HashTag(hashTagName);
                }

                hashTags.add(hashTag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            if (hashTags.size() > 0) {
                for (HashTag hashTag : hashTags) {
                    kweet.getHashTags().add(hashTag);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMentions(Kweet kweet) {
        Pattern pattern = Pattern.compile(Patterns.MENTION_PATTERN);
        Matcher matcher = pattern.matcher(kweet.getMessage());

        Set<String> mentions = new HashSet<>();

        while (matcher.find()) {
            mentions.add(matcher.group(0).substring(1));
        }

        Set<User> users = new HashSet<>();

        for (String mention : mentions) {
            try {
                User mentionedUser = findUser(mention);
                if (mentionedUser != null) {
                    users.add(mentionedUser);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            if (users.size() > 0) {
                for (User mentioned : users) {
                    kweet.getMentions().add(mentioned);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashTag findHashTag(String name) {
        Query query = manager.createNamedQuery("HashTag.findByName", User.class);
        query.setParameter("name", name);

        return JPAResult.getSingleResultOrNull(query);
    }

    private User findUser(String name) {
        Query query = manager.createNamedQuery("User.findByName", User.class);
        query.setParameter("username", name);

        return JPAResult.getSingleResultOrNull(query);
    }
}
