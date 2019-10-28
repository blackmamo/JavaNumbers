package com.scottlogic.mamos.numbers;

public class IntegerRepresentations {

  public static void main(String[] args) {
    for (int i = -2; i <= 2; i++) {
      int j = Integer.MAX_VALUE + i;
      System.out.println("binary representation = "+toBits(j)+", signed int value = "+j+", unsigned int value = "+Integer.toUnsignedString(j));
    }
    System.out.println("...");
    for (int i = -2; i <= 2; i++) {
      System.out.println("binary representation = "+toBits(i)+", signed int value = "+i+", unsigned int value = "+Integer.toUnsignedString(i));
    }
  }

  private static String toBits(int i) {
    return String.format("%32s", Integer.toBinaryString(i)).replace(' ', '0');
  }
}
