package org.example.mockx.core;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.example.mockx.DefaultValues;
import org.example.mockx.ObjenesisProvider;
import org.example.mockx.core.behavior.Behavior;
import org.example.mockx.core.behavior.ReturnBehavior;
import org.example.mockx.core.behavior.ThrowBehavior;

import java.util.*;

public class MockXCore {
    private final List<Mocking> mockingList = new ArrayList<>();
    private Invocation lastInvocation = null;

    public synchronized <T> T createMock(Class<T> typeToMock) {
        int mockId = mockingList.size();
        Mocking mocking = new Mocking(mockId, this);
        mockingList.add(mocking);
        Class<? extends T> subClass = new ByteBuddy()
            .subclass(typeToMock, ConstructorStrategy.Default.NO_CONSTRUCTORS)
            .method(ElementMatchers.any())
            .intercept(InvocationHandlerAdapter.of(mocking))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded();
        return ObjenesisProvider.getObjenesisStd().newInstance(subClass);
    }

    public synchronized <T> Stubbing<T> when(T methodCall) {
        if (lastInvocation == null) {
            throw new IllegalStateException("Method Invocation Missing");
        }
        Stubbing<T> stubbing = new Stubbing<>(
            lastInvocation,
            this
        );
        lastInvocation = null;
        return stubbing;
    }

    synchronized void doReturn(Invocation invocation, Object returnValue) {
        // TODO check if type of return values matches the method
        mockingList.get(invocation.mockId).addBehavior(invocation, new ReturnBehavior(returnValue));
    }

    synchronized void doThrow(Invocation invocation, Throwable throwable) {
        mockingList.get(invocation.mockId).addBehavior(invocation, new ThrowBehavior(throwable));
    }

    public synchronized Behavior handleInvocation(Invocation invocation) {
        Behavior stubbedBehavior = resolveBehavior(invocation);
        lastInvocation = invocation;
        return stubbedBehavior;
    }

    private synchronized Behavior resolveBehavior(Invocation invocation) {
        Behavior resolvedBehavior = mockingList.get(invocation.mockId).findMatchingBehavior(invocation);
        if (resolvedBehavior == null) {
            resolvedBehavior = new ReturnBehavior(DefaultValues.get(invocation.method.getReturnType()));
        }
        return resolvedBehavior;
    }

    // Note:
    // - args[] is null if the method was invoked without any argument
    static boolean argsMatch(Object[] args1, Object[] args2) {
        if (args1 == args2) return true;                        // returns when both null
        if (args1 == null || args2 == null) return false;       // returns when either null
        for (int i = 0; i < args1.length; i++) {
            if (!Objects.equals(args1[i], args2[i])) return false;
        }
        return true;
    }
}
