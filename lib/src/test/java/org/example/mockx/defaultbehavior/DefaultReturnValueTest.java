package org.example.mockx.defaultbehavior;

import org.example.mockx.DefaultValues;
import org.example.mockx.MockX;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultReturnValueTest {

    public boolean booleanMethod() { return !DefaultValues.DEFAULT_BOOLEAN; }

    @Test
    @DisplayName("Test default behavior when return type is Boolean")
    void testBoolean() {
        DefaultReturnValueTest mock = MockX.create(DefaultReturnValueTest.class);
        assertEquals(DefaultValues.DEFAULT_BOOLEAN, mock.booleanMethod());
    }

    public char charMethod() { return DefaultValues.DEFAULT_CHAR + 1; }

    @Test
    @DisplayName("Test default behavior when return type is Character")
    void testCharacter() {
        DefaultReturnValueTest mock = MockX.create(DefaultReturnValueTest.class);
        assertEquals(DefaultValues.DEFAULT_CHAR, mock.charMethod());
    }

    public byte byteMethod() { return DefaultValues.DEFAULT_BYTE + 1; }

    @Test
    @DisplayName("Test default behavior when return type is Byte")
    void testByte() {
        DefaultReturnValueTest mock = MockX.create(DefaultReturnValueTest.class);
        assertEquals(DefaultValues.DEFAULT_BYTE, mock.byteMethod());
    }

    public short shortMethod() { return DefaultValues.DEFAULT_SHORT + 1; }

    @Test
    @DisplayName("Test default behavior when return type is Short")
    void testShort() {
        DefaultReturnValueTest mock = MockX.create(DefaultReturnValueTest.class);
        assertEquals(DefaultValues.DEFAULT_SHORT, mock.shortMethod());
    }

    public int intMethod() { return DefaultValues.DEFAULT_INT + 1; }

    @Test
    @DisplayName("Test default behavior when return type is Int")
    void testInt() {
        DefaultReturnValueTest mock = MockX.create(DefaultReturnValueTest.class);
        assertEquals(DefaultValues.DEFAULT_INT, mock.intMethod());
    }

    public long longMethod() { return DefaultValues.DEFAULT_LONG + 1; }

    @Test
    @DisplayName("Test default behavior when return type is Long")
    void testLong() {
        DefaultReturnValueTest mock = MockX.create(DefaultReturnValueTest.class);
        assertEquals(DefaultValues.DEFAULT_LONG, mock.longMethod());
    }

    public float floatMethod() { return DefaultValues.DEFAULT_FLOAT + 1; }

    @Test
    @DisplayName("Test default behavior when return type is Float")
    void testFloat() {
        DefaultReturnValueTest mock = MockX.create(DefaultReturnValueTest.class);
        assertEquals(DefaultValues.DEFAULT_FLOAT, mock.floatMethod());
    }

    public double doubleMethod() { return DefaultValues.DEFAULT_DOUBLE + 1; }

    @Test
    @DisplayName("Test default behavior when return type is Double")
    void testDouble() {
        DefaultReturnValueTest mock = MockX.create(DefaultReturnValueTest.class);
        assertEquals(DefaultValues.DEFAULT_DOUBLE, mock.doubleMethod());
    }

    public Object objectMethod() { return new Object(); }

    @Test
    @DisplayName("Test default behavior when return type is Object")
    void testObject() {
        DefaultReturnValueTest mock = MockX.create(DefaultReturnValueTest.class);
        assertEquals(DefaultValues.DEFAULT_OBJECT, mock.objectMethod());
    }

}
