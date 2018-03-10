package nl.shanelab.kwetter.dal.dao.impl;

import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.dal.dao.KweetDao;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.ejb.DummyData;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

@InMemoryDao
@Stateless
@NoArgsConstructor
public class KweetCollectionDaoImpl extends BaseCollectionDao implements KweetDao {

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

        long nextId = (long) this.count();

        kweet.setId(nextId);

        data.getKweets().put(nextId, kweet);

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

    public void remove(Kweet kweet) {
        data.getKweets().remove(kweet.getId());
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

    public boolean isFavoritedBy(Kweet kweet, String name) {
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
            if (favoritedBy.getUsername().equals(name)) {
                flagRef.set(true);
                return;
            }
        });

        return flagRef.get();
    }

    public boolean isMentionedIn(Kweet kweet, String name) {
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
            if (mentioned.getUsername().equals(name)) {
                flagRef.set(true);
                return;
            }
        });

        return flagRef.get();
    }
}
