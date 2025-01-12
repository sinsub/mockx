package org.example.mockx.core;

public class Stubbing {

    private final Invocation invocation;
    private final MockXCore mockXCore;

    public Stubbing(Invocation invocation, MockXCore mockXCore) {
        this.invocation = invocation;
        this.mockXCore = mockXCore;
    }

    public void doReturn(Object returnValue) {
        mockXCore.addDoReturn(invocation, returnValue);
    }
}
