package org.example.mockx.core.behavior;

public abstract class Behavior {

    protected final Object value;

    public Behavior(Object value) {
        this.value = value;
    }

    public abstract Object run() throws Throwable;
}
