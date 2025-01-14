package org.example.mockx.core.behavior;

public class ReturnBehavior extends Behavior {
    public ReturnBehavior(Object value) {
        super(value);
    }

    @Override
    public Object run() {
        return value;
    }
}
