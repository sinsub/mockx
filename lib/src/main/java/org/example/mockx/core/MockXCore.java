package org.example.mockx.core;

import com.google.common.annotations.VisibleForTesting;
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
    private MockXCoreState state = MockXCoreState.IDLE;
    private final List<Mocking> mockingList = new ArrayList<>();
    private final List<Invocation> invocationList = new ArrayList<>();
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

    // ---------- thenBehavior ----------

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
                throw new IllegalStateException("Method Invocation Missing");
            }
            case INVOKED: {
                // TODO check return type for ReturnBehavior
                addBehavior(lastInvocation, behavior);
                resetStubbingProgress();
            } break;
        }
        assertState();
    }

    // ---------- doBehavior ----------

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
            } break;
            case INVOKED: {
                commitLastInvocation();
                lastBehavior = behavior;
                this.state = MockXCoreState.BEHAVIOR_ADDED;
            } break;
            case BEHAVIOR_ADDED: {
                throw new IllegalStateException("Stubbing already in progress, cannot start new stubbing");
            }
        }
        assertState();
    }

    // ---------- invoke ----------

    synchronized Behavior handleInvocation(Invocation invocation) {
        assertState();
        Behavior stubbedBehavior = resolveBehavior(invocation);
        switch (state) {
            case IDLE: {
                lastInvocation = invocation;
                state = MockXCoreState.INVOKED;
            } break;
            case INVOKED: {
                commitLastInvocation();
                lastInvocation = invocation;
            } break;
            case BEHAVIOR_ADDED: {
                if (lastBehavior instanceof ReturnBehavior) {
                    if (invocation.method.getReturnType() == (void.class)) {
                        throw new IllegalStateException("Cannot define return behavior for a void method");
                    }
                    // TODO check return type for ReturnBehavior
                }
                addBehavior(invocation, lastBehavior);
                resetStubbingProgress();
            } break;
        }
        assertState();
        return stubbedBehavior;
    }

    private synchronized void addBehavior(Invocation invocation, Behavior behavior) {
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


    // ---------- For Internal Testing ---------

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

    @VisibleForTesting
    MockXCoreState getState() {
        return this.state;
    }
}
