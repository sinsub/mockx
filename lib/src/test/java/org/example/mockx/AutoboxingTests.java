package org.example.mockx;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AutoboxingTests {

    @Test
    @DisplayName("Test that boolean return value can be auto-boxed and auto-unboxed")
    void testAutoboxingBoolean() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        Boolean boxedReturn = Boolean.TRUE;
        boolean primitiveReturn = true;

        // primitive method, stubbed with wrapper
        MockX.when(mock.booleanMethod()).thenReturn(boxedReturn);
        assertEquals(boxedReturn, mock.booleanMethod());

        // wrapper method, stubbed with primitive
        MockX.when(mock.boxedBooleanMethod()).thenReturn(primitiveReturn);
        assertEquals(primitiveReturn, mock.boxedBooleanMethod());

        // primitive method, stubbed with wrapper
        MockX.doReturn(boxedReturn).when(mock).booleanMethod();
        assertEquals(boxedReturn, mock.booleanMethod());

        // wrapper method, stubbed with primitive
        MockX.doReturn(primitiveReturn).when(mock).boxedBooleanMethod();
        assertEquals(primitiveReturn, mock.boxedBooleanMethod());
    }

    @Test
    @DisplayName("Test that char return value can be auto-boxed and auto-unboxed")
    void testAutoboxingChar() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        Character boxedReturn = 'A';
        char primitiveReturn = 'B';

        // primitive method, stubbed with wrapper
        MockX.when(mock.charMethod()).thenReturn(boxedReturn);
        assertEquals(boxedReturn, mock.charMethod());

        // wrapper method, stubbed with primitive
        MockX.when(mock.boxedCharMethod()).thenReturn(primitiveReturn);
        assertEquals(primitiveReturn, mock.boxedCharMethod());

        // primitive method, stubbed with wrapper
        MockX.doReturn(boxedReturn).when(mock).charMethod();
        assertEquals(boxedReturn, mock.charMethod());

        // wrapper method, stubbed with primitive
        MockX.doReturn(primitiveReturn).when(mock).boxedCharMethod();
        assertEquals(primitiveReturn, mock.boxedCharMethod());
    }

    @Test
    @DisplayName("Test that byte return value can be auto-boxed and auto-unboxed")
    void testAutoboxingByte() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        Byte boxedReturn = 5;
        byte primitiveReturn = 10;

        // primitive method, stubbed with wrapper
        MockX.when(mock.byteMethod()).thenReturn(boxedReturn);
        assertEquals(boxedReturn, mock.byteMethod());

        // wrapper method, stubbed with primitive
        MockX.when(mock.boxedByteMethod()).thenReturn(primitiveReturn);
        assertEquals(primitiveReturn, mock.boxedByteMethod());

        // primitive method, stubbed with wrapper
        MockX.doReturn(boxedReturn).when(mock).byteMethod();
        assertEquals(boxedReturn, mock.byteMethod());

        // wrapper method, stubbed with primitive
        MockX.doReturn(primitiveReturn).when(mock).boxedByteMethod();
        assertEquals(primitiveReturn, mock.boxedByteMethod());
    }

    @Test
    @DisplayName("Test that short return value can be auto-boxed and auto-unboxed")
    void testAutoboxingShort() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        Short boxedReturn = 5;
        short primitiveReturn = 10;

        // primitive method, stubbed with wrapper
        MockX.when(mock.shortMethod()).thenReturn(boxedReturn);
        assertEquals(boxedReturn, mock.shortMethod());

        // wrapper method, stubbed with primitive
        MockX.when(mock.boxedShortMethod()).thenReturn(primitiveReturn);
        assertEquals(primitiveReturn, mock.boxedShortMethod());

        // primitive method, stubbed with wrapper
        MockX.doReturn(boxedReturn).when(mock).shortMethod();
        assertEquals(boxedReturn, mock.shortMethod());

        // wrapper method, stubbed with primitive
        MockX.doReturn(primitiveReturn).when(mock).boxedShortMethod();
        assertEquals(primitiveReturn, mock.boxedShortMethod());
    }

    @Test
    @DisplayName("Test that int return value can be auto-boxed and auto-unboxed")
    void testAutoboxingInt() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        Integer boxedReturn = 5;
        int primitiveReturn = 10;

        // primitive method, stubbed with wrapper
        MockX.when(mock.intMethod()).thenReturn(boxedReturn);
        assertEquals(boxedReturn, mock.intMethod());

        // wrapper method, stubbed with primitive
        MockX.when(mock.boxedIntMethod()).thenReturn(primitiveReturn);
        assertEquals(primitiveReturn, mock.boxedIntMethod());

        // primitive method, stubbed with wrapper
        MockX.doReturn(boxedReturn).when(mock).intMethod();
        assertEquals(boxedReturn, mock.intMethod());

        // wrapper method, stubbed with primitive
        MockX.doReturn(primitiveReturn).when(mock).boxedIntMethod();
        assertEquals(primitiveReturn, mock.boxedIntMethod());
    }

    @Test
    @DisplayName("Test that long return value can be auto-boxed and auto-unboxed")
    void testAutoboxingLong() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        Long boxedReturn = 5L;
        long primitiveReturn = 10;

        // primitive method, stubbed with wrapper
        MockX.when(mock.longMethod()).thenReturn(boxedReturn);
        assertEquals(boxedReturn, mock.longMethod());

        // wrapper method, stubbed with primitive
        MockX.when(mock.boxedLongMethod()).thenReturn(primitiveReturn);
        assertEquals(primitiveReturn, mock.boxedLongMethod());

        // primitive method, stubbed with wrapper
        MockX.doReturn(boxedReturn).when(mock).longMethod();
        assertEquals(boxedReturn, mock.longMethod());

        // wrapper method, stubbed with primitive
        MockX.doReturn(primitiveReturn).when(mock).boxedLongMethod();
        assertEquals(primitiveReturn, mock.boxedLongMethod());
    }

    @Test
    @DisplayName("Test that float return value can be auto-boxed and auto-unboxed")
    void testAutoboxingFloat() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        Float boxedReturn = 5.0F;
        float primitiveReturn = 10;

        // primitive method, stubbed with wrapper
        MockX.when(mock.floatMethod()).thenReturn(boxedReturn);
        assertEquals(boxedReturn, mock.floatMethod());

        // wrapper method, stubbed with primitive
        MockX.when(mock.boxedFloatMethod()).thenReturn(primitiveReturn);
        assertEquals(primitiveReturn, mock.boxedFloatMethod());

        // primitive method, stubbed with wrapper
        MockX.doReturn(boxedReturn).when(mock).floatMethod();
        assertEquals(boxedReturn, mock.floatMethod());

        // wrapper method, stubbed with primitive
        MockX.doReturn(primitiveReturn).when(mock).boxedFloatMethod();
        assertEquals(primitiveReturn, mock.boxedFloatMethod());
    }

    @Test
    @DisplayName("Test that double return value can be auto-boxed and auto-unboxed")
    void testAutoboxingDouble() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);
        Double boxedReturn = 5.0;
        double primitiveReturn = 10;

        // primitive method, stubbed with wrapper
        MockX.when(mock.doubleMethod()).thenReturn(boxedReturn);
        assertEquals(boxedReturn, mock.doubleMethod());

        // wrapper method, stubbed with primitive
        MockX.when(mock.boxedDoubleMethod()).thenReturn(primitiveReturn);
        assertEquals(primitiveReturn, mock.boxedDoubleMethod());

        // primitive method, stubbed with wrapper
        MockX.doReturn(boxedReturn).when(mock).doubleMethod();
        assertEquals(boxedReturn, mock.doubleMethod());

        // wrapper method, stubbed with primitive
        MockX.doReturn(primitiveReturn).when(mock).boxedDoubleMethod();
        assertEquals(primitiveReturn, mock.boxedDoubleMethod());
    }
}
