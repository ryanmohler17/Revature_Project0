package com.ryan.data;

import java.util.List;

public interface DataAccess<T, Y> {

    default void init() {

    }
    Y saveItem(T item);
    T getItem(Y item);
    List<T> getAllItems();

}
