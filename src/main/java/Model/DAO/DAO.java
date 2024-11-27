package Model.DAO;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO (Data Access Object) interface provides a generic set of methods for
 * performing CRUD (Create, Read, Update, Delete) operations on a database.
 * It extends the Closeable interface to ensure that resources can be properly
 * closed when they are no longer needed.
 *
 * @param <T> the type of entity this DAO handles
 * @param <K> the type of the primary key of the entity
 */
public interface DAO<T, K> extends Closeable {
    /**
     * Saves the given entity to the database. If the entity is new, it will be inserted;
     * otherwise, the existing entity will be updated.
     *
     * @param entity the entity to save
     * @return the saved entity with any database-generated fields populated (e.g., ID)
     */
    T save(T entity) throws SQLException;

    /**
     * Deletes the given entity from the database.
     *
     * @param entity the entity to delete
     * @return the deleted entity
     * @throws SQLException if a database access error occurs
     */
    T delete(T entity) throws SQLException;

    /**
     * Finds an entity by its primary key.
     *
     * @param key the primary key of the entity to find
     * @return the found entity, or null if no entity was found with the given key
     */
    T findById(K key) throws SQLException;

    /**
     * Finds all entities of the given type in the database.
     *
     * @return a list of all entities
     */
    List<T> findAll();
}