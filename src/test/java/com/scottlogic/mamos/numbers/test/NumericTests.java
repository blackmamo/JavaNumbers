package com.scottlogic.mamos.numbers.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static java.lang.System.out;

import java.util.function.BiConsumer;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class NumericTests {




  @ParameterizedTest
  @MethodSource("comparisons")
  public void testZeroCompare(BiConsumer<Double, Double> comparisonAssertion){
    comparisonAssertion.accept(-0.0, 0.0);
  }

  private static Stream<BiConsumer<Double, Double>> comparisons() {
    return Stream.of(
        (Double a, Double b) ->
            assertTrue( (double) a == (double) b , "Failed zeros equal check"),
        (Double a, Double b) ->
            assertFalse( (double) a < (double) b , "Failed neg zero lt check"),
        (Double a, Double b) ->
            assertTrue(Double.compare(a, b) == 0, "Failed zeros compare 0 check")
    );
  }

  @Test
  public void printShift(){
    out.println(toBits(-32));
    out.println(toBits(-32 >> 1));
    out.println(toBits(32));
    out.println(toBits(32 >> 1));
    out.println((-32 >>> 1));
    out.println(toBits(-32 >>> 1));
  }

  @Test
  public void printNaNAsInt(){
    out.println(toBits(Double.NEGATIVE_INFINITY));
    out.println(toBits(Double.NEGATIVE_INFINITY + 1));
    out.println(toBits(Double.NaN));
    out.println(toBits(Double.NaN+Double.NEGATIVE_INFINITY));
    out.println(toBits(Math.sqrt(-3)));
    out.println(toBits(Math.log(-2)));
    out.println(toBits(0*Double.POSITIVE_INFINITY));
    out.println(toBits(0*Double.NEGATIVE_INFINITY));
    out.println(Long.toBinaryString(-1));
    out.println(toBits(Double.NaN + Double.longBitsToDouble(0b0111111111111000000000000000000000000000001100000000000000000000L)));
  }

  private static String toBits(double d) {
    return String.format("%64s", Long.toBinaryString(Double.doubleToRawLongBits(d))).replace(' ','0');
  }

  private static String toBits(int i) {
    return String.format("%32s", Integer.toBinaryString(i)).replace(' ','0');
  }
}
