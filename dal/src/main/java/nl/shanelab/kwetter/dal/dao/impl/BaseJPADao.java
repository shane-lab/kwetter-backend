package nl.shanelab.kwetter.dal.dao.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.dal.dao.GenericDao;

import javax.enterprise.inject.Produces;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public abstract class BaseJPADao<T, Id extends Serializable> implements GenericDao<T, Id> {

    @Produces
    @PersistenceContext(name = "kwetter_pu")
    protected EntityManager manager;

    // using reflection to fetch the class based on the given template parameter
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

    public void remove(T entity) {
        manager.remove(entity);
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
