# mockx

A simple (toy) mocking library for Java. Similar to that other library that
also start with "Mock", but nowhere as production ready as that one.

Currently lacks a lot of features that I am not even aware of, including:
- invocation verification.
- ordered invocation verification.
- Spy? Why? What?

**Example code**:

```java
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
```

For more examples just look at the Unit Tests.

**Motivation for building this**: The first time I looked at a mock,
I just could not understand how it was working. `when()` just does not
make sense. The return value of method call that has not been stubbed yet
is being passed to it? What? What does that even mean? What does it even do?

**Learning**: `when` is not really necessary, the following works:

```java
List mockedList = mock(List.class);
var ignored = mockedList.size();
when(1).thenReturn(100);
assertEquals(100, mockedList.size());
```

The last invocation on any mock is stubbed by the `thenReturn` call.
It is a bit weird, but it gets the job done.

Will debuggers ever stop calling `toString()` on my mocks? :_)
