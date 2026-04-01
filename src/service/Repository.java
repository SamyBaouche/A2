package service;

import exceptions.EntityNotFoundException;
import interfaces.Identifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Repository<T>
 *
 * Generic repository used for queries on top of the application's primary storage.
 * This class is intentionally lightweight and keeps its own internal copy of items.
 *
 * Required methods (assignment spec):
 * - add(T item)
 * - findById(String id)
 * - filter(Predicate<T> predicate)
 * - getSorted()
 */
public class Repository<T extends Identifiable & Comparable<? super T>> {

    private final List<T> items;

    public Repository() {
        this.items = new ArrayList<>();
    }

    public Repository(List<T> seedItems) {
        this.items = new ArrayList<>();
        if (seedItems != null) {
            this.items.addAll(seedItems);
        }
    }

    /**
     * Add an item to the repository (mirror copy for queries).
     */
    public void add(T item) {
        if (item == null) {
            return;
        }
        items.add(item);
    }

    /**
     * Find an item by its Identifiable id.
     *
     * @throws EntityNotFoundException if no item matches the provided id
     */
    public T findById(String id) throws EntityNotFoundException {
        if (id == null || id.isBlank()) {
            throw new EntityNotFoundException("Invalid id");
        }

        for (T item : items) {
            if (item != null && id.equalsIgnoreCase(item.getId())) {
                return item;
            }
        }

        throw new EntityNotFoundException("Entity not found (id=" + id + ")");
    }

    /**
     * Return all items that satisfy the given predicate.
     */
    public List<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate");

        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (item != null && predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Returns items sorted using their natural order (compareTo).
     * Does not mutate the repository internal list.
     */
    public List<T> getSorted() {
        List<T> copy = new ArrayList<>(items);
        Collections.sort(copy);
        return copy;
    }

    /**
     * Optional helper for debugging/testing.
     */
    public int size() {
        return items.size();
    }
}

