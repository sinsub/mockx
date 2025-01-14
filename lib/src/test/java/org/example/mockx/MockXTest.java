package org.example.mockx;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class MockXTest {

    @Test
    void create() {
        File mockFile = MockX.create(File.class);
        assertNull(mockFile.toString());

        // specify behavior for integer return type
        assertEquals(0, mockFile.hashCode());
        MockX.when(mockFile.hashCode()).thenReturn(1);
        assertEquals(1, mockFile.hashCode());

        // by default false is returned for all any argument
        assertFalse(mockFile.setExecutable(true));
        assertFalse(mockFile.setExecutable(false));

        // specify behavior when argument is false
        MockX.when(mockFile.setExecutable(false)).thenReturn(true);
        assertTrue(mockFile.setExecutable(false));

        // behavior for true remains default
        assertFalse(mockFile.setExecutable(true));

        // specify behavior when argument is true
        MockX.when(mockFile.setExecutable(true)).thenReturn(true);
        assertTrue(mockFile.setExecutable(true));

        // behavior defined for both argument now
        assertTrue(mockFile.setExecutable(true));
        assertTrue(mockFile.setExecutable(false));

        // doThrow
        Object equalsArg = new Object();
        MockX.when(mockFile.equals(equalsArg)).thenThrow(new IllegalArgumentException("My exception"));
        assertThrows(IllegalArgumentException.class, () -> mockFile.equals(equalsArg));
        assertEquals(DefaultValues.DEFAULT_BOOLEAN, mockFile.equals(new Object()));


        // mock void method
        MockX.doThrow(new StackOverflowError()).when(mockFile).deleteOnExit();
        assertThrows(StackOverflowError.class, mockFile::deleteOnExit);

        assertThrows(IllegalStateException.class, () -> MockX.doReturn(1).when(mockFile).deleteOnExit());
    }
}