package org.example.mockx.core;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.example.mockx.DefaultValues;
import org.example.mockx.ObjenesisProvider;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Keeps track of all the mocks.
 */
public class MockXCore {
    private int nextMockId = 0;
    private final ArrayList<Invocation> invocations = new ArrayList<>();
    private final HashMap<Integer, HashMap<Method, List<Map.Entry<Object[], Object>>>> drb = new HashMap<>();

    public synchronized Object handleInvocation(Invocation invocation) {
        invocations.add(invocation);
        return resolveReturnValue(invocation);
    }

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
        Invocation invocation = invocations.get(invocations.size() - 1);
        return new Stubbing<T>(
            invocation,
            this
        );
    }

    synchronized void addDoReturn(Invocation invocation, Object returnValue) {
        if (!drb.get(invocation.mockId).containsKey(invocation.method)) {
            drb.get(invocation.mockId).put(invocation.method, new ArrayList<>());
        }
        List<Map.Entry<Object[], Object>> list = drb.get(invocation.mockId).get(invocation.method);
        list.add(new AbstractMap.SimpleEntry<>(invocation.args, returnValue));
    }


    private synchronized Object resolveReturnValue(Invocation invocation) {
        List<Map.Entry<Object[], Object>> list = drb.get(invocation.mockId).getOrDefault(invocation.method, Collections.emptyList());
        for (int i = list.size() - 1; i >= 0; i--) {
            if (argsMatch(invocation.args, list.get(i).getKey())) {
                return list.get(i).getValue();
            }
        }
        return DefaultValues.get(invocation.method.getReturnType());
    }

    private boolean argsMatch(Object[] args1, Object[] args2) {
        if (args1 == args2) return true;
        for (int i = 0; i < args1.length; i++)
            if (!args1[i].equals(args2[i])) return false;
        return true;
    }
}
