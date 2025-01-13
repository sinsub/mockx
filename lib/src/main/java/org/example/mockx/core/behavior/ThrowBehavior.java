package org.example.mockx.core.behavior;

public class ThrowBehavior extends Behavior {

    public ThrowBehavior(Object value) {
        super(value);
        if (!(value instanceof Throwable)) {
            throw new IllegalArgumentException("Argument to ThrowBehavior is not Throwable");
        }
    }

    @Override
    public Object run() throws Throwable {
        throw (Throwable) value;
    }
}
