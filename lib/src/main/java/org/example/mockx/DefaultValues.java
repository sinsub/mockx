package org.example.mockx;

public class DefaultValues {
    public static final Boolean DEFAULT_BOOLEAN = false;
    public static final Character DEFAULT_CHAR = 0;
    public static final Byte DEFAULT_BYTE = 0;
    public static final Short DEFAULT_SHORT = 0;
    public static final Integer DEFAULT_INT = 0;
    public static final Long DEFAULT_LONG = 0L;
    public static final Float DEFAULT_FLOAT = 0.0F;
    public static final Double DEFAULT_DOUBLE = 0.0;
    public static final Object DEFAULT_OBJECT = null;

    private DefaultValues() {}

    public static Object get(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) return DEFAULT_BOOLEAN;
            if (type == char.class) return DEFAULT_CHAR;
            if (type == byte.class) return DEFAULT_BYTE;
            if (type == short.class) return DEFAULT_SHORT;
            if (type == int.class) return DEFAULT_INT;
            if (type == long.class) return DEFAULT_LONG;
            if (type == float.class) return DEFAULT_FLOAT;
            if (type == double.class) return DEFAULT_DOUBLE;
        }
        return DEFAULT_OBJECT;
    }
}
