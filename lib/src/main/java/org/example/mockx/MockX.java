package org.example.mockx;

import org.example.mockx.core.MockXCore;
import org.example.mockx.core.Stubbing;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;


public class MockX {

    public static Objenesis objenesis = new ObjenesisStd();
    public static ThreadLocal<MockXCore> mockXCoreTL = ThreadLocal.withInitial(MockXCore::new);

    public static <T> T create(Class<T> typeToMock) {
        return mockXCoreTL.get().createMock(typeToMock);
    }

    public static <T> Stubbing<T> when(T methodCall) {
        return mockXCoreTL.get().when(methodCall);
    }
}
