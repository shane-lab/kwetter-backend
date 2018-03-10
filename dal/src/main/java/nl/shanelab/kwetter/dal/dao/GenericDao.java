package nl.shanelab.kwetter.dal.dao;

import java.io.Serializable;
import java.util.Collection;

/**
 * Generic data assess object methods
 *
 * @param <T> targeted class type
 * @param <Id> serializable entity identifier type
 */
public interface GenericDao<T, Id extends Serializable> {

    /**
     * Count the amount of entities
     *
     * @return int The amount of stored entities
     */
    int count();

    /**
     * Creates a new entity
     *
     * @param arg entity instance of target class type The entity to persist
     * @return T Returns a new instance of the targeted class type
     */
    T create(T arg);

    /**
     * Modifies an existing entity
     *
     * @param arg entity instance of target class type The entity to persist
     * @return T Returns the modified entity of the targeted class type
     */
    T edit(T arg);

    /**
     * Find an entity by an identifier
     *
     * @param id the serializable entity identifier of the targeted class type
     * @return T Returns a persisted entity of the targeted class type
     */
    T find(Id id);


    /**
     * Find all existing entities
     *
     * @return Collection Returns a collection of persisted entities of the targeted class type
     */
    Collection<T> findAll();

    /**
     * Remove an existing entity
     *
     * @param arg entity instance of target class type The entity to remove
     */
    void remove(T arg);
}
