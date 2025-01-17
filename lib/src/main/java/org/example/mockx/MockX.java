package org.example.mockx;

import org.example.mockx.core.BehaviorStubbing;
import org.example.mockx.core.InvocationStubbing;
import org.example.mockx.core.MockXCore;

public class MockX {
    private static final ThreadLocal<MockXCore> mockXCoreTL = ThreadLocal.withInitial(MockXCore::new);

    public static <T> T create(Class<T> typeToMock) {
        return mockXCoreTL.get().createMock(typeToMock);
    }

    public static <T> InvocationStubbing<T> when(T methodCall) {
        return new InvocationStubbing<>(mockXCoreTL.get());
    }

    public static BehaviorStubbing doReturn(Object value) {
        mockXCoreTL.get().doReturn(value);
        return new BehaviorStubbing();
    }

    public static BehaviorStubbing doThrow(Throwable throwable) {
        mockXCoreTL.get().doThrow(throwable);
        return new BehaviorStubbing();
    }

    public static <T> T verify(T mock) {
        return mockXCoreTL.get().verify(mock);
    }

    public static <T> T verify(int count, T mock) {
        return mockXCoreTL.get().verify(count, mock);
    }
}
