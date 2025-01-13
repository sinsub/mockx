package org.example.mockx.core;

import org.example.mockx.core.behavior.Behavior;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MockInvocationHandler implements InvocationHandler {
    private final int mockId;
    private final MockXCore mockXCore;

    public MockInvocationHandler(int mockId, MockXCore mockXCore) {
        this.mockId = mockId;
        this.mockXCore = mockXCore;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Behavior stubbedBehavior = mockXCore.handleInvocation(new Invocation(
            this.mockId,
            method,
            args
        ));
        return stubbedBehavior.run();
    }
}
