package org.example.mockx;

import java.util.List;

public class ClassForMocking {
    public boolean booleanMethod() {return !DefaultValues.DEFAULT_BOOLEAN;}

    public Boolean boxedBooleanMethod() {return !DefaultValues.DEFAULT_BOOLEAN;}

    public char charMethod() {return (char) (DefaultValues.DEFAULT_CHAR + 1);}

    public Character boxedCharMethod() {return (char) (DefaultValues.DEFAULT_CHAR + 1);}

    public byte byteMethod() {return (byte) (DefaultValues.DEFAULT_BYTE + 1);}

    public Byte boxedByteMethod() {return (byte) (DefaultValues.DEFAULT_BYTE + 1);}

    public int intMethod() {return DefaultValues.DEFAULT_INT + 1;}

    public Integer boxedIntMethod() {return DefaultValues.DEFAULT_INT + 1;}

    public short shortMethod() {return (short) (DefaultValues.DEFAULT_SHORT + 1);}

    public Short boxedShortMethod() {return (short) (DefaultValues.DEFAULT_SHORT + 1);}

    public long longMethod() {return DefaultValues.DEFAULT_LONG + 1;}

    public Long boxedLongMethod() {return DefaultValues.DEFAULT_LONG + 1;}

    public float floatMethod() {return DefaultValues.DEFAULT_FLOAT + 1;}

    public Float boxedFloatMethod() {return DefaultValues.DEFAULT_FLOAT + 1;}

    public double doubleMethod() {return DefaultValues.DEFAULT_DOUBLE + 1;}

    public Double boxedDoubleMethod() {return DefaultValues.DEFAULT_DOUBLE + 1;}

    public Object objectMethod() {return new Object();}

    public void voidMethod() {}

    public int singleArgumentMethod(int arg) {return arg + 1;}

    public List<Integer> listMethod() {return null;}

    public static class FirstException extends Throwable {}

    public static class SecondException extends Throwable {}

    public static class ThirdException extends Throwable {}
}
