package com.midterm.proj.warehousemanagement.database;

public interface QueryResponse<T> {
    void onSuccess(T data);
    void onFailure(String message);
}
