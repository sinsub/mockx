package org.example.mockx;

import org.example.mockx.core.excpetion.ReturnTypeMismatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReturnTypeValidationTest {

    @Test
    @DisplayName("Test for primitive return type validation")
    void testPrimitiveReturnTypeValidation() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);

        assertThrows(ReturnTypeMismatchException.class, () -> MockX.doReturn(1).when(mock).doubleMethod());
        assertThrows(ReturnTypeMismatchException.class, () -> MockX.doReturn(null).when(mock).doubleMethod());
        assertThrows(ReturnTypeMismatchException.class, () -> MockX.doReturn(new Object()).when(mock).doubleMethod());
    }

    @Test
    @DisplayName("Test for reference return type validation")
    void testReferenceReturnTypeValidation() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);

        assertThrows(ReturnTypeMismatchException.class, () -> MockX.doReturn(1).when(mock).listMethod());
        assertThrows(ReturnTypeMismatchException.class, () -> MockX.doReturn(new Object()).when(mock).listMethod());
        // this is allowed due to Type Erasure of generics during runtime
        MockX.doReturn(new ArrayList<String>()).when(mock).listMethod();
    }
}
