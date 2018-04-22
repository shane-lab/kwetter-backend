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
     * The amount of default paginated results
     */
    int defaultResults = 10;

    /**
     * @field The amount of maximum paginated results
     */
    int maxResults = 20;

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
     * @return Collection<T> Returns a collection of persisted entities of the targeted class type
     */
    Collection<T> findAll();

    /**
     * Find all existing entities with pagination
     *
     * @param page The page index
     * @return Collection<T> Returns a paginated collection of persisted entities of the targeted class type
     */
    Pagination<T> findAll(int page);

    /**
     * Find all existing entities with pagination
     *
     * @param page The page index
     * @param size The maximum results to return
     * @return Collection<T> Returns a paginated collection of persisted entities of the targeted class type
     */
    Pagination<T> findAll(int page, int size);

    /**
     * Remove an existing entity
     *
     * @param id The id of the entity of the targeted target class type
     */
    void remove(Id id);
}
