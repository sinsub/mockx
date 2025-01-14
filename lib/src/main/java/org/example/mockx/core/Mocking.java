package org.example.mockx.core;

import org.example.mockx.core.behavior.Behavior;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

public class Mocking implements InvocationHandler {
    private final int mockId;
    private final MockXCore core;
    private final HashMap<Method, List<Map.Entry<Object[], Behavior>>> stubbedBehavior = new HashMap<>();
    private final List<Integer> invocationList = new ArrayList<>();

    public Mocking(int mockId, MockXCore core) {
        this.mockId = mockId;
        this.core = core;
    }

    public void addBehavior(Invocation invocation, Behavior behavior) {
        if (!stubbedBehavior.containsKey(invocation.method)) {
            stubbedBehavior.put(invocation.method, new ArrayList<>());
        }
        List<Map.Entry<Object[], Behavior>> list = stubbedBehavior.get(invocation.method);
        list.add(new AbstractMap.SimpleImmutableEntry<>(invocation.args, behavior));
    }

    public Behavior findMatchingBehavior(Invocation invocation) {
        // Optimistic optimization: not checking if mockId matches
        List<Map.Entry<Object[], Behavior>> list = stubbedBehavior.getOrDefault(invocation.method, Collections.emptyList());
        for (int i = list.size() - 1; i >= 0; i--) {
            if (MockXCore.argsMatch(invocation.args, list.get(i).getKey()))
                return list.get(i).getValue();
        }
        return null;
    }

    public void addInvocation(int invocationId) {
        invocationList.add(invocationId);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Behavior stubbedBehavior = core.handleInvocation(new Invocation(
            this.mockId,
            method,
            args
        ));
        return stubbedBehavior.run();
    }
}
