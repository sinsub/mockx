package org.example.mockx;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultReturnValueTest {

    @Test
    @DisplayName("Test default behavior when return type is Boolean")
    void testBoolean() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        assertEquals(DefaultValues.DEFAULT_BOOLEAN, mock.booleanMethod());
    }

    @Test
    @DisplayName("Test default behavior when return type is Character")
    void testCharacter() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        assertEquals(DefaultValues.DEFAULT_CHAR, mock.charMethod());
    }

    @Test
    @DisplayName("Test default behavior when return type is Byte")
    void testByte() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        assertEquals(DefaultValues.DEFAULT_BYTE, mock.byteMethod());
    }

    @Test
    @DisplayName("Test default behavior when return type is Short")
    void testShort() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        assertEquals(DefaultValues.DEFAULT_SHORT, mock.shortMethod());
    }

    @Test
    @DisplayName("Test default behavior when return type is Int")
    void testInt() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        assertEquals(DefaultValues.DEFAULT_INT, mock.intMethod());
    }

    @Test
    @DisplayName("Test default behavior when return type is Long")
    void testLong() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        assertEquals(DefaultValues.DEFAULT_LONG, mock.longMethod());
    }

    @Test
    @DisplayName("Test default behavior when return type is Float")
    void testFloat() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        assertEquals(DefaultValues.DEFAULT_FLOAT, mock.floatMethod());
    }

    @Test
    @DisplayName("Test default behavior when return type is Double")
    void testDouble() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        assertEquals(DefaultValues.DEFAULT_DOUBLE, mock.doubleMethod());
    }

    @Test
    @DisplayName("Test default behavior when return type is Object")
    void testObject() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        assertEquals(DefaultValues.DEFAULT_OBJECT, mock.objectMethod());
    }

    // TODO add test case for void method when it is supported
}
