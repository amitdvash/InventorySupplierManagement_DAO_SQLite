package dev.Inventory.Interface;

import java.util.List;

public interface IDTO<T> {
    boolean create(T entity); // Create a new entity in the database
    void update(T entity); // Update an existing entity
    void delete(T entity); // Delete an entity b
    List<T> readAll(); // Read all entities

}
