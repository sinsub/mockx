package org.example.mockx.core;

public class Stubbing<T> {

    private final Invocation invocation;
    private final MockXCore mockXCore;

    public Stubbing(Invocation invocation, MockXCore mockXCore) {
        this.invocation = invocation;
        this.mockXCore = mockXCore;
    }

    public void doReturn(T returnValue) {
        mockXCore.addDoReturn(invocation, returnValue);
    }
}
