package dev.Suppliers.Interfaces;

import java.util.List;

public interface IDTO<T> {
    int create(T entity); // Create a new entity in the database
    List<T> readAll(); // Read all entities
    void update(T entity); // Update an existing entity
    void delete(int id); // Delete an entity by its ID
}
