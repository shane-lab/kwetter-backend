package nl.shanelab.kwetter.dal.dao.impl;

import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.dal.dao.HashTagDao;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.ejb.DummyData;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

@InMemoryDao
@ApplicationScoped
@NoArgsConstructor
public class HashTagCollectionDaoImpl extends BaseCollectionDao implements HashTagDao {

    @Inject
    public HashTagCollectionDaoImpl(DummyData data) {
        this.data = data;
    }

    public int count() {
        return data.getHashTags().size();
    }

    public HashTag create(HashTag hashTag) {
        if (hashTag.getName() == null) {
            return null;
        }
        if (this.getByName(hashTag.getName()) != null) {
            return null;
        }

        long nextId = (long) this.count();

        hashTag.setId(nextId);

        data.getHashTags().put(nextId, hashTag);

        return hashTag;
    }

    public HashTag edit(HashTag hashTag) {
        if (this.find(hashTag.getId()) == null) {
            return create(hashTag);
        }

        // persisted hashtag names are immutable

        return null;
    }

    public HashTag find(Long id) {
        return data.getHashTags().get(id);
    }

    public Collection<HashTag> findAll() {
        return data.getHashTags().values();
    }

    public void remove(HashTag hashTag) {
        data.getHashTags().remove(hashTag);
    }

    public HashTag getByName(String name) {
        AtomicReference<HashTag> hashTagRef = new AtomicReference<>();

        this.findAll().forEach(hashTag -> {
            if (hashTag.getName().equals(name)) {
                hashTagRef.set(hashTag);
                return;
            }
        });

        return hashTagRef.get();
    }
}
