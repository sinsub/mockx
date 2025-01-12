package org.example.mockx.core;

import java.lang.reflect.Method;

/**
 * A simple struct to store an invocation.
 */
public class Invocation {
    public final int mockId;     // id of the mock object.
    public final Method method;
    public final Object[] args;

    public Invocation(int mockId, Method method, Object[] args) {
        this.mockId = mockId;
        this.method = method;
        this.args = args;
    }
}
