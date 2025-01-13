package org.example.mockx;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class MockXTest {

    @Test
    void create() {
        File proxy = MockX.create(File.class);
        assertNull(proxy.toString());

        // specify behavior for integer return type
        assertEquals(0, proxy.hashCode());
        MockX.when(proxy.hashCode()).doReturn(1);
        assertEquals(1, proxy.hashCode());

        // by default false is returned for all any argument
        assertFalse(proxy.setExecutable(true));
        assertFalse(proxy.setExecutable(false));

        // specify behavior when argument is false
        MockX.when(proxy.setExecutable(false)).doReturn(true);
        assertTrue(proxy.setExecutable(false));

        // behavior for true remains default
        assertFalse(proxy.setExecutable(true));

        // specify behavior when argument is true
        MockX.when(proxy.setExecutable(true)).doReturn(true);
        assertTrue(proxy.setExecutable(true));

        // behavior defined for both argument now
        assertTrue(proxy.setExecutable(true));
        assertTrue(proxy.setExecutable(false));

    }
}