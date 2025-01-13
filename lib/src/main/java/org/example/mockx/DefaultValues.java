package org.example.mockx;

public class DefaultValues {

    public static final boolean DEFAULT_BOOLEAN = false;
    public static final char DEFAULT_CHAR = 0;
    public static final byte DEFAULT_BYTE = 0;
    public static final short DEFAULT_SHORT = 0;
    public static final int DEFAULT_INT = 0;
    public static final long DEFAULT_LONG = 0;
    public static final float DEFAULT_FLOAT = 0;
    public static final double DEFAULT_DOUBLE = 0;
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
