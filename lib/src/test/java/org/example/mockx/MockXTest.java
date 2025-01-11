package org.example.mockx;

import org.example.mockx.testclasses.ClassWithoutPublicConstructor;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class MockXTest {

    @Test
    void create() {
        File proxy = MockX.create(File.class);
        assertEquals("Hello World!", proxy.toString());
    }

    @Test
    void mockClassWithoutDefaultConstructor() {
        ClassWithoutPublicConstructor proxy = MockX.create(ClassWithoutPublicConstructor.class);
        assertEquals("Hello World!", proxy.toString());
    }
}