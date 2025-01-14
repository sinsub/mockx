package org.example.mockx.core;

import java.lang.reflect.Method;

public class Invocation {
    public final int mockId;
    public final Method method;
    public final Object[] args;

    public Invocation(int mockId, Method method, Object[] args) {
        this.mockId = mockId;
        this.method = method;
        this.args = args;
    }
}
