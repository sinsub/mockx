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
import org.example.mockx.core.excpetion.ReturnTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MockXCore {
    private final List<Mocking> mockingList = new ArrayList<>();
    private final List<Invocation> invocationList = new ArrayList<>();
    private MockXCoreState state = MockXCoreState.IDLE;
    private Invocation lastInvocation = null;
    private Behavior lastBehavior = null;

    enum MockXCoreState {
        IDLE,
        INVOKED,
        BEHAVIOR_ADDED
    }

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

    synchronized void thenReturn(Object returnValue) {
        thenBehavior(new ReturnBehavior(returnValue));
    }

    synchronized void thenThrow(Throwable throwable) {
        thenBehavior(new ThrowBehavior(throwable));
    }

    private void thenBehavior(Behavior behavior) {
        assertState();
        switch (this.state) {
            case IDLE:
            case BEHAVIOR_ADDED: {
                resetStubbingProgress();
                throw new IllegalStateException("Method Invocation Missing");
            }
            case INVOKED: {
                addBehavior(lastInvocation, behavior);
                resetStubbingProgress();
            }
            break;
        }
        assertState();
    }

    public synchronized void doReturn(Object returnValue) {
        doBehavior(new ReturnBehavior(returnValue));
    }

    public synchronized void doThrow(Throwable throwable) {
        doBehavior(new ThrowBehavior(throwable));
    }

    private void doBehavior(Behavior behavior) {
        assertState();
        switch (this.state) {
            case IDLE: {
                lastBehavior = behavior;
                this.state = MockXCoreState.BEHAVIOR_ADDED;
            }
            break;
            case INVOKED: {
                commitLastInvocation();
                lastBehavior = behavior;
                this.state = MockXCoreState.BEHAVIOR_ADDED;
            }
            break;
            case BEHAVIOR_ADDED: {
                resetStubbingProgress();
                throw new IllegalStateException("Stubbing already in progress, cannot start new stubbing");
            }
        }
        assertState();
    }

    synchronized Behavior handleInvocation(Invocation invocation) {
        assertState();
        Behavior stubbedBehavior = resolveBehavior(invocation);
        switch (state) {
            case IDLE: {
                lastInvocation = invocation;
                state = MockXCoreState.INVOKED;
            }
            break;
            case INVOKED: {
                commitLastInvocation();
                lastInvocation = invocation;
            }
            break;
            case BEHAVIOR_ADDED: {
                addBehavior(invocation, lastBehavior);
                resetStubbingProgress();
            }
            break;
        }
        assertState();
        return stubbedBehavior;
    }

    private synchronized void addBehavior(Invocation invocation, Behavior behavior) {
        if (behavior instanceof ReturnBehavior) {
            if (!isAssignableConsideringBoxing(invocation.method.getReturnType(), behavior.getValue())) {
                resetStubbingProgress();
                throw new ReturnTypeMismatchException("Cannot return "
                    + behavior.getValue()
                    + " from a method with return type: "
                    + invocation.method.getReturnType()
                );
            }
        }
        mockingList.get(invocation.mockId).addBehavior(invocation, behavior);
    }

    private synchronized Behavior resolveBehavior(Invocation invocation) {
        Behavior resolvedBehavior = mockingList.get(invocation.mockId).findMatchingBehavior(invocation);
        if (resolvedBehavior == null) {
            resolvedBehavior = new ReturnBehavior(DefaultValues.get(invocation.method.getReturnType()));
        }
        return resolvedBehavior;
    }

    private synchronized void commitLastInvocation() {
        if (lastInvocation == null) return;
        int invocationId = invocationList.size();
        invocationList.add(lastInvocation);
        mockingList.get(lastInvocation.mockId).addInvocation(invocationId);
        lastInvocation = null;
    }

    private synchronized void resetStubbingProgress() {
        lastInvocation = null;
        lastBehavior = null;
        state = MockXCoreState.IDLE;
    }

    private void assertState() {
        switch (this.state) {
            case IDLE:
                assert lastInvocation == null;
                assert lastBehavior == null;
                break;
            case INVOKED:
                assert lastInvocation != null;
                assert lastBehavior == null;
                break;
            case BEHAVIOR_ADDED:
                assert lastInvocation == null;
                assert lastBehavior != null;
                break;
        }
    }


    // Note:
    // - args[] is null if the method was invoked without any argument
    public static boolean argsMatch(Object[] args1, Object[] args2) {
        if (args1 == args2) return true;                        // returns when both null
        if (args1 == null || args2 == null) return false;       // returns when either null
        for (int i = 0; i < args1.length; i++) {
            if (!Objects.equals(args1[i], args2[i])) return false;
        }
        return true;
    }

    public static boolean isAssignableConsideringBoxing(Class<?> target, Object value) {
        if (target == null) return false;
        if (target == void.class) return false;
        if (target.isPrimitive()) {
            if (value == null) return false;
            Class<?> valueClass = value.getClass();
            if (target == valueClass) return true;
            // unboxing
            if (target == boolean.class && valueClass == Boolean.class) return true;
            if (target == char.class && valueClass == Character.class) return true;
            if (target == byte.class && valueClass == Byte.class) return true;
            if (target == short.class && valueClass == Short.class) return true;
            if (target == int.class && valueClass == Integer.class) return true;
            if (target == long.class && valueClass == Long.class) return true;
            if (target == float.class && valueClass == Float.class) return true;
            return target == double.class && valueClass == Double.class;
        } else {
            if (value == null) return true;
            Class<?> valueClass = value.getClass();
            return target.isAssignableFrom(valueClass);
        }
    }

}
