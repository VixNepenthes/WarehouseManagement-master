package com.midterm.proj.warehousemanagement.features.observer;

public interface Callback {
    public void attach(Observer o);
    public void detach(Observer o);
    public void notify(Message msg);
}
