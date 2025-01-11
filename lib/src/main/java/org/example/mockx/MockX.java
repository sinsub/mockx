package org.example.mockx;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;


public class MockX {

    public static Objenesis objenesis = new ObjenesisStd();

    public static <T> T create(Class<T> typeToMock) {
        Class<? extends T> dynamicSubclass = new ByteBuddy()
                .subclass(typeToMock, ConstructorStrategy.Default.NO_CONSTRUCTORS)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(MockX.class.getClassLoader())
                .getLoaded();

        ObjectInstantiator<? extends T> objectInstantiator = objenesis.getInstantiatorOf(dynamicSubclass);
        return objectInstantiator.newInstance();
    }

}
