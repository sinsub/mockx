package org.example.mockx;

import org.example.mockx.testclasses.ClassWithoutPublicConstructor;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class MockXTest {

    @Test
    void create() {
        File proxy = MockX.create(File.class);
        assertNull(proxy.toString());
        assertEquals(0, proxy.hashCode());
    }

    @Test
    void mockClassWithoutDefaultConstructor() {
        ClassWithoutPublicConstructor proxy = MockX.create(ClassWithoutPublicConstructor.class);
        assertNull(proxy.toString());
        assertEquals(0, proxy.hashCode());
    }
}