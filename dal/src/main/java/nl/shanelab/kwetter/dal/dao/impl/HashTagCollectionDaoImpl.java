package nl.shanelab.kwetter.dal.dao.impl;

import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.dal.dao.HashTagDao;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.ejb.DummyData;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@InMemoryDao
@Stateless
@NoArgsConstructor
public class HashTagCollectionDaoImpl extends BaseCollectionDao<HashTag, Long> implements HashTagDao {

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

    public void remove(Long id) {
        HashTag hashTag = this.find(id);
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

    public Collection<HashTag> getTrending(Date date) {
        AtomicReference<Map<String, Integer>> hashTagsRef = new AtomicReference<>(new TreeMap<>());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.getWeekYear();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

        this.data.getKweets().values().stream()
                .filter(kweet -> kweet.getHashTags() != null && kweet.getHashTags().size() > 0
                        && kweet.getCreatedAt().getYear() == year && kweet.getCreatedAt().get(woy) == week)
                .forEach(kweet -> {
                    Map<String, Integer> hashTags = hashTagsRef.get();

                    kweet.getHashTags().forEach(hashTag -> {
                        int occurences = 0;
                        if (hashTags.keySet().contains(hashTag.getName())) {
                            occurences = hashTags.get(hashTag.getName());
                        }
                        hashTags.put(hashTag.getName(), ++occurences);
                    });

                    hashTagsRef.set(hashTags);
                });

        return hashTagsRef.get().entrySet().stream()
                .sorted((o1, o2) -> o2.getValue() == o1.getValue() ? 0 : o2.getValue() > o1.getValue() ? 1 : -1)
                .map(stringIntegerEntry -> this.getByName(stringIntegerEntry.getKey()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
