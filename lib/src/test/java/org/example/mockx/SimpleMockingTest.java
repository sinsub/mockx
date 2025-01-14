package org.example.mockx;

import org.example.mockx.ClassForMocking.FirstException;
import org.example.mockx.ClassForMocking.SecondException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class SimpleMockingTest {
    @Test
    @DisplayName("Test when().thenReturn()")
    void testWhenThenReturn() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);

        // default behavior
        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));

        // first stubbing
        int firstStubbingArgument = 1, firstStubbingReturn = -1;
        MockX.when(mock.singleArgumentMethod(firstStubbingArgument)).thenReturn(firstStubbingReturn);

        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));
        assertEquals(firstStubbingReturn, mock.singleArgumentMethod(firstStubbingArgument));

        // second stubbing
        int secondStubbingArgument = 2, secondStubbingReturn = -2;
        MockX.when(mock.singleArgumentMethod(secondStubbingArgument)).thenReturn(secondStubbingReturn);

        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));
        assertEquals(firstStubbingReturn, mock.singleArgumentMethod(firstStubbingArgument));
        assertEquals(secondStubbingReturn, mock.singleArgumentMethod(secondStubbingArgument));

        // override first stubbing
        int thirdStubbingReturn = -3;
        MockX.when(mock.singleArgumentMethod(firstStubbingArgument)).thenReturn(thirdStubbingReturn);

        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));
        assertEquals(thirdStubbingReturn, mock.singleArgumentMethod(firstStubbingArgument));
        assertEquals(secondStubbingReturn, mock.singleArgumentMethod(secondStubbingArgument));

    }

    @Test
    @DisplayName("Test doReturn().when()")
    void testDoReturnWhen() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);

        // default behavior
        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));

        // first stubbing
        int firstStubbingArgument = 1, firstStubbingReturn = -1;
        MockX.doReturn(firstStubbingReturn).when(mock).singleArgumentMethod(firstStubbingArgument);

        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));
        assertEquals(firstStubbingReturn, mock.singleArgumentMethod(firstStubbingArgument));

        // second stubbing
        int secondStubbingArgument = 2, secondStubbingReturn = -2;
        MockX.doReturn(secondStubbingReturn).when(mock).singleArgumentMethod(secondStubbingArgument);

        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));
        assertEquals(firstStubbingReturn, mock.singleArgumentMethod(firstStubbingArgument));
        assertEquals(secondStubbingReturn, mock.singleArgumentMethod(secondStubbingArgument));

        // override first stubbing
        int thirdStubbingReturn = -3;
        MockX.doReturn(thirdStubbingReturn).when(mock).singleArgumentMethod(firstStubbingArgument);

        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));
        assertEquals(thirdStubbingReturn, mock.singleArgumentMethod(firstStubbingArgument));
        assertEquals(secondStubbingReturn, mock.singleArgumentMethod(secondStubbingArgument));

    }

    @Test
    @DisplayName("Test when().thenThrow()")
    void testWhenThenThrow() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);

        // default behavior
        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));

        // first stubbing
        int firstStubbingArgument = 1;
        FirstException firstStubbingThrow = new FirstException();
        MockX.when(mock.singleArgumentMethod(firstStubbingArgument)).thenThrow(firstStubbingThrow);

        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));
        assertThrowsExactly(FirstException.class, () -> mock.singleArgumentMethod(firstStubbingArgument));

        // second stubbing
        int secondStubbingArgument = 2;
        SecondException secondStubbingThrow = new SecondException();
        MockX.when(mock.singleArgumentMethod(secondStubbingArgument)).thenThrow(secondStubbingThrow);

        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));
        assertThrowsExactly(FirstException.class, () -> mock.singleArgumentMethod(firstStubbingArgument));
        assertThrowsExactly(SecondException.class, () -> mock.singleArgumentMethod(secondStubbingArgument));

        // override first stubbing not possible directly since invocation causes exception
    }

    @Test
    @DisplayName("Test doThrow().when()")
    void testDoThrowWhen() {
        ClassForMocking mock = MockX.create(ClassForMocking.class);

        // default behavior
        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));

        // first stubbing
        int firstStubbingArgument = 1;
        FirstException firstStubbingThrow = new FirstException();
        MockX.doThrow(firstStubbingThrow).when(mock).singleArgumentMethod(firstStubbingArgument);

        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));
        assertThrowsExactly(FirstException.class, () -> mock.singleArgumentMethod(firstStubbingArgument));

        // second stubbing
        int secondStubbingArgument = 2;
        SecondException secondStubbingThrow = new SecondException();
        MockX.doThrow(secondStubbingThrow).when(mock).singleArgumentMethod(secondStubbingArgument);

        assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));
        assertThrowsExactly(FirstException.class, () -> mock.singleArgumentMethod(firstStubbingArgument));
        assertThrowsExactly(SecondException.class, () -> mock.singleArgumentMethod(secondStubbingArgument));

        // override first stubbing not possible directly since invocation causes exception
    }

}
