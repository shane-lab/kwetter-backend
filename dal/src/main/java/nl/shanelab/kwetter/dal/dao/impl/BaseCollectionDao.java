package nl.shanelab.kwetter.dal.dao.impl;

import nl.shanelab.kwetter.dal.dao.GenericDao;
import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.ejb.DummyData;

import javax.ejb.EJB;
import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class BaseCollectionDao<T, Id extends Serializable> implements GenericDao<T, Id> {

    @EJB
    protected DummyData data;

    public Pagination<T> findAll(int page) {
        return findAll(page, defaultResults);
    }

    public Pagination<T> findAll(int page, int size) {
        if (size <= 0) {
            size = defaultResults;
        }
        if (size > maxResults) {
            size = maxResults;
        }
        Collection collection = this.findAll().stream()
                .skip(page <= 0 ? 0 : page * size)
                .limit(size)
                .collect(Collectors.toList());

        //noinspection unchecked
        return Pagination.builder()
                .collection(collection)
                .count(this.count())
                .page(page)
                .requestedSize(size)
                .build();
    }
}
