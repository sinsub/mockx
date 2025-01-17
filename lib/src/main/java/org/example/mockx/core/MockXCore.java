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
import org.example.mockx.core.excpetion.VerificationFailedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MockXCore {
    private final List<Mocking> mockingList = new ArrayList<>();
    private final List<Invocation> invocationList = new ArrayList<>();
    private MockXCoreState state = MockXCoreState.IDLE;
    private Invocation previousInvocation = null;
    private Behavior addedBehavior = null;
    private Integer verificationCount = null;

    enum MockXCoreState {
        IDLE,
        INVOKED,
        BEHAVIOR_ADDED,
        VERIFICATION_REQUESTED
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

    public Invocation getInvocation(int invocationId) {
        assert invocationId >= 0 && invocationId < invocationList.size();
        return invocationList.get(invocationId);
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
            case BEHAVIOR_ADDED:
            case VERIFICATION_REQUESTED: {
                resetStubbingProgress();
                throw new IllegalStateException("Method Invocation Missing");
            }
            case INVOKED: {
                addBehavior(previousInvocation, behavior);
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
                addedBehavior = behavior;
                this.state = MockXCoreState.BEHAVIOR_ADDED;
            }
            break;
            case INVOKED: {
                commitLastInvocation();
                addedBehavior = behavior;
                this.state = MockXCoreState.BEHAVIOR_ADDED;
            }
            break;
            case BEHAVIOR_ADDED: {
                resetStubbingProgress();
                throw new IllegalStateException("Stubbing was in progress, could not start new stubbing");
            }
            case VERIFICATION_REQUESTED: {
                resetStubbingProgress();
                throw new IllegalStateException("Verification was in progress, could not start stubbing");
            }
        }
        assertState();
    }

    public synchronized <T> T verify(T mock) {
        handleVerificationRequest(1);
        return mock;
    }

    public synchronized <T> T verify(int count, T mock) {
        if (count < 0) throw new IllegalArgumentException("Count cannot be less than 0");
        handleVerificationRequest(count);
        return mock;
    }

    private void handleVerificationRequest(int count) {
        assertState();
        switch (state) {
            case IDLE: {
                verificationCount = count;
                this.state = MockXCoreState.VERIFICATION_REQUESTED;
            } break;
            case INVOKED: {
                commitLastInvocation();
                verificationCount = count;
                this.state = MockXCoreState.VERIFICATION_REQUESTED;
            } break;
            case BEHAVIOR_ADDED: {
                resetStubbingProgress();
                throw new IllegalStateException("Stubbing was in progress, could not start verification");
            }
            case VERIFICATION_REQUESTED: {
                resetStubbingProgress();
                throw new IllegalStateException("Verification was in progress, could not start new verification");
            }
        }
        assertState();
    }

    synchronized Behavior handleInvocation(Invocation invocation) {
        assertState();
        Behavior stubbedBehavior = resolveBehavior(invocation);
        switch (state) {
            case IDLE: {
                previousInvocation = invocation;
                state = MockXCoreState.INVOKED;
            }
            break;
            case INVOKED: {
                commitLastInvocation();
                previousInvocation = invocation;
            }
            break;
            case BEHAVIOR_ADDED: {
                addBehavior(invocation, addedBehavior);
                resetStubbingProgress();
            }
            break;
            case VERIFICATION_REQUESTED: {
                int count = mockingList.get(invocation.mockId).getMatchingInvocationCount(invocation);
                if (verificationCount != count) {
                    resetStubbingProgress();
                    throw new VerificationFailedException("Expected " + verificationCount + " invocations, but got " + count);
                }
                resetStubbingProgress();
            } break;
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
        if (previousInvocation == null) return;
        int invocationId = invocationList.size();
        invocationList.add(previousInvocation);
        mockingList.get(previousInvocation.mockId).addInvocation(invocationId);
        previousInvocation = null;
    }

    private synchronized void resetStubbingProgress() {
        previousInvocation = null;
        addedBehavior = null;
        verificationCount = null;
        state = MockXCoreState.IDLE;
    }

    private void assertState() {
        switch (this.state) {
            case IDLE:
                assert previousInvocation == null;
                assert addedBehavior == null;
                assert verificationCount == null;
                break;
            case INVOKED:
                assert previousInvocation != null;
                assert addedBehavior == null;
                assert verificationCount == null;
                break;
            case BEHAVIOR_ADDED:
                assert previousInvocation == null;
                assert addedBehavior != null;
                assert verificationCount == null;
                break;
            case VERIFICATION_REQUESTED:
                assert previousInvocation == null;
                assert addedBehavior == null;
                assert verificationCount != null;
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
