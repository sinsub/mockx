package org.example.mockx.core;

public class InvocationStubbing<T> {

    private final MockXCore mockXCore;

    public InvocationStubbing(MockXCore mockXCore) {
        this.mockXCore = mockXCore;
    }

    public void thenReturn(T returnValue) {
        mockXCore.thenReturn(returnValue);
    }

    public void thenThrow(Throwable throwable) {
        mockXCore.thenThrow(throwable);
    }
}
