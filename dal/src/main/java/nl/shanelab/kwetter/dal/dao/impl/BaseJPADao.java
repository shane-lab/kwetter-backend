package nl.shanelab.kwetter.dal.dao.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.dal.dao.GenericDao;
import nl.shanelab.kwetter.dal.dao.Pagination;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class BaseJPADao<T, Id extends Serializable> implements GenericDao<T, Id> {

    @Produces
    @PersistenceContext(name = "kwetter_pu")
    protected EntityManager manager;

    // using reflection to fetch the class based on the given template parameter
    @SuppressWarnings("unchecked")
    private final Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    protected Class<T> getEntityClass() {
        return entityClass;
    }

    public int count() {
        CriteriaQuery<Long> criteriaQuery = manager.getCriteriaBuilder().createQuery(Long.class);
        criteriaQuery.select(manager.getCriteriaBuilder().count(criteriaQuery.from(entityClass)));

        return manager.createQuery(criteriaQuery).getSingleResult().intValue();
    }

    public T create(T entity) {
        if (entity != null) {
            manager.persist(entity);

            manager.flush();
        }

        return entity;
    }

    public T edit(T entity) {
        return manager.merge(entity);
    }

    public T find(Id id) {
        return manager.find(entityClass, id);
    }

    public Collection<T> findAll() {
        CriteriaQuery criteriaQuery = manager.getCriteriaBuilder().createQuery();
        criteriaQuery.select(criteriaQuery.from(entityClass));

        return manager.createQuery(criteriaQuery).getResultList();
    }

    public Pagination<T> findAll(int page) {
        return findAll(page, defaultResults);
    }

    public Pagination<T> findAll(int page, int size) {
        CriteriaQuery criteriaQuery = manager.getCriteriaBuilder().createQuery();
        criteriaQuery.select(criteriaQuery.from(entityClass));

        return fromQuery(page, size, this.count(), manager.createQuery(criteriaQuery));
    }

    public void remove(T entity) {
        manager.remove(entity);
    }

    protected Pagination<T> fromQuery(int page, int size, int count, Query query) {
        if (size <= 0) {
            size = defaultResults;
        }
        if (size > maxResults) {
            size = maxResults;
        }
        Collection collection = Collections.EMPTY_SET;
        if (count <= 0) {
            query.setFirstResult(page <= 0 ? 0 : page * size);
            query.setMaxResults(size);

            collection = query.getResultList();
        }

        //noinspection unchecked
        return Pagination.builder()
                .collection(collection)
                .page(page)
                .requestedSize(size)
                .count(count)
                .build();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    protected static class JPAResult {
        public static <T> T getSingleResultOrNull(Query query) {
            List results = query.getResultList();
            if (results.isEmpty()) {
                return null;
            }
            if (results.size() == 1) {
                return (T) results.get(0);
            }
            throw new NonUniqueResultException();
        }
    }
}
