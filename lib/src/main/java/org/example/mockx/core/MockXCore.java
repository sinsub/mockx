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

import java.lang.reflect.Method;
import java.util.*;

public class MockXCore {
    private int nextMockId = 0;
    private final ArrayList<Invocation> invocations = new ArrayList<>();
    private final HashMap<Integer, HashMap<Method, List<Map.Entry<Object[], Behavior>>>> drb = new HashMap<>();

    public synchronized <T> T createMock(Class<T> typeToMock) {
        int mockId = nextMockId++;
        drb.put(mockId, new HashMap<>());
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

    public synchronized <T> Stubbing<T> when(T methodCall) {
        if (invocations.isEmpty()) {
            throw new IllegalStateException("Stub-able method invocation not found on any mock!");
        }
        Invocation invocation = invocations.get(invocations.size() - 1);
        return new Stubbing<>(
            invocation,
            this
        );
    }

    synchronized void doReturn(Invocation invocation, Object returnValue) {
        if (!drb.get(invocation.mockId).containsKey(invocation.method)) {
            drb.get(invocation.mockId).put(invocation.method, new ArrayList<>());
        }
        List<Map.Entry<Object[], Behavior>> list = drb.get(invocation.mockId).get(invocation.method);
        list.add(new AbstractMap.SimpleEntry<>(invocation.args, new ReturnBehavior(returnValue)));
    }

    synchronized void doThrow(Invocation invocation, Throwable throwable) {
        if (!drb.get(invocation.mockId).containsKey(invocation.method)) {
            drb.get(invocation.mockId).put(invocation.method, new ArrayList<>());
        }
        List<Map.Entry<Object[], Behavior>> list = drb.get(invocation.mockId).get(invocation.method);
        list.add(new AbstractMap.SimpleEntry<>(invocation.args, new ThrowBehavior(throwable)));
    }

    // Invocation Handling
    public synchronized Behavior handleInvocation(Invocation invocation) {
        Behavior stubbedBehavior = resolveBehavior(invocation);
        invocations.add(invocation);
        return stubbedBehavior;
    }

    private synchronized Behavior resolveBehavior(Invocation invocation) {
        List<Map.Entry<Object[], Behavior>> list = drb.get(invocation.mockId).getOrDefault(invocation.method, Collections.emptyList());
        // Match the most recent stubbed behavior
        for (int i = list.size() - 1; i >= 0; i--) {
            if (argsMatch(invocation.args, list.get(i).getKey())) {
                return list.get(i).getValue();
            }
        }
        // fallback to default
        return new ReturnBehavior(DefaultValues.get(invocation.method.getReturnType()));
    }

    // Note:
    // - args[] is null if the method was invoked without any argument
    private boolean argsMatch(Object[] args1, Object[] args2) {
        if (args1 == args2) return true;                        // returns when both null
        if (args1 == null || args2 == null) return false;       // returns when either null
        for (int i = 0; i < args1.length; i++) {
            if (!Objects.equals(args1[i], args2[i])) return false;
        }
        return true;
    }
}
