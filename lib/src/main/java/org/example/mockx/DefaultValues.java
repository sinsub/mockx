package org.example.mockx;

public class DefaultValues {

    private DefaultValues() {}

    public static Object get(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) return false;
            if (type == char.class) return '\0';
            if (type == int.class || type == short.class || type == byte.class) return 0;
            if (type == long.class) return 0L;
            if (type == float.class) return 0.0F;
            if (type == double.class) return 0.0;
        }
        return null;
    }
}
