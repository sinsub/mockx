package org.example.mockx.core;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.example.mockx.DefaultValues;
import org.example.mockx.ObjenesisProvider;

import java.util.ArrayList;

/**
 * Keeps track of all the mocks.
 */
public class MockXCore {
    private int nextMockId = 0;
    private final ArrayList<Invocation> invocations = new ArrayList<>();

    public synchronized Object handleInvocation(Invocation invocation) {
        invocations.add(invocation);
        return resolveReturnValue(invocation);
    }

    public synchronized <T> T createMock(Class<T> typeToMock) {
        int mockId = nextMockId++;
        MockInvocationHandler invocationHandler = new MockInvocationHandler(mockId, this);
        Class<? extends T> subClass = new ByteBuddy()
            .subclass(typeToMock, ConstructorStrategy.Default.NO_CONSTRUCTORS)
            .method(ElementMatchers.any())
            .intercept(InvocationHandlerAdapter.of(invocationHandler))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded();
        return ObjenesisProvider.getObjenesisStd().newInstance(subClass);
    }

    private Object resolveReturnValue(Invocation invocation) {
        return DefaultValues.get(invocation.method.getReturnType());
    }
}
