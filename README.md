# mockx

A toy mocking library for Java.

Currently lacks a lot of features, including:
- ordered invocation verification.
- stubbing of method with void return type.
- Argument matchers.

**Example code**:

```java
// create a mock
ClassForMocking mock = MockX.create(ClassForMocking.class);

// verify 0 invocations
MockX.verify(0, mock).singleArgumentMethod(0);
assertThrowsExactly(VerificationFailedException.class, () -> MockX.verify(1, mock).singleArgumentMethod(0));
assertThrowsExactly(IllegalArgumentException.class, () -> MockX.verify(-1, mock).singleArgumentMethod(0));

// default behavior is to return default value
assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));

// verify 1 invocation when asserting default behavior
MockX.verify(1, mock).singleArgumentMethod(0);
// 0 invocation for different argument
MockX.verify(0, mock).singleArgumentMethod(1);

// first stubbing
int firstStubbingArgument = 1, firstStubbingReturn = -1;
MockX.when(mock.singleArgumentMethod(firstStubbingArgument)).thenReturn(firstStubbingReturn);

// no invocations with this argument
MockX.verify(0, mock).singleArgumentMethod(firstStubbingArgument);

assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));
assertEquals(firstStubbingReturn, mock.singleArgumentMethod(firstStubbingArgument));

// verify invocations
MockX.verify(2, mock).singleArgumentMethod(0);
MockX.verify(1, mock).singleArgumentMethod(firstStubbingArgument);

// second stubbing
int secondStubbingArgument = 2, secondStubbingReturn = -2;
MockX.when(mock.singleArgumentMethod(secondStubbingArgument)).thenReturn(secondStubbingReturn);

assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));
assertEquals(firstStubbingReturn, mock.singleArgumentMethod(firstStubbingArgument));
assertEquals(secondStubbingReturn, mock.singleArgumentMethod(secondStubbingArgument));

// verify invocations
MockX.verify(3, mock).singleArgumentMethod(0);
MockX.verify(2, mock).singleArgumentMethod(firstStubbingArgument);
MockX.verify(1, mock).singleArgumentMethod(secondStubbingArgument);


// override first stubbing
int thirdStubbingReturn = -3;
MockX.when(mock.singleArgumentMethod(firstStubbingArgument)).thenReturn(thirdStubbingReturn);

assertEquals(DefaultValues.DEFAULT_INT, mock.singleArgumentMethod(0));
assertEquals(thirdStubbingReturn, mock.singleArgumentMethod(firstStubbingArgument));
assertEquals(secondStubbingReturn, mock.singleArgumentMethod(secondStubbingArgument));

// verify invocations
MockX.verify(4, mock).singleArgumentMethod(0);
MockX.verify(3, mock).singleArgumentMethod(firstStubbingArgument);
MockX.verify(2, mock).singleArgumentMethod(secondStubbingArgument);
```

For more examples just look at the Unit Tests.

Will debuggers ever stop calling `toString()` on my mocks? :_)
