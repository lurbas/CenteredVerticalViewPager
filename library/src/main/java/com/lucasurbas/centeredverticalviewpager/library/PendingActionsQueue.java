package com.lucasurbas.centeredverticalviewpager.library;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Lucas on 1/14/15.
 */
public class PendingActionsQueue<T> extends ArrayBlockingQueue<T> {

    private boolean isExecuting;

    public PendingActionsQueue() {
        super(1000);
    }

    public boolean isExecuting() {
        return isExecuting;
    }

    public void setExecuting(boolean isExecuting) {
        this.isExecuting = isExecuting;
    }
}
