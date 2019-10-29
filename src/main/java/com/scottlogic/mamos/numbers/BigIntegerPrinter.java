package com.scottlogic.mamos.numbers;

import java.math.BigInteger;

public class BigIntegerPrinter {
  public static void main(String[] args) {
    BigInteger i = BigInteger.valueOf(Long.MAX_VALUE);
    printNumberAndNumBytes(i);
    printNumberAndNumBytes(i.multiply(BigInteger.valueOf(4)));
  }
  private static void printNumberAndNumBytes(BigInteger i){
    System.out.println(
        "Size in bytes = "+i.toByteArray().length+", i in bits = "+i.toString(2)
    );
  }
}
