package com.scottlogic.mamos.numbers.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class IntegerAutoBoxingTests {
  @ParameterizedTest
  @ValueSource(ints = {12345, 128, 127, 0})
  public void testBoxCompare(int i){
    assertTrue(box(i) == box(i), () -> "Failed equality check for "+i);
  }
  public static Integer box(int i){
    return i;
  }
}
