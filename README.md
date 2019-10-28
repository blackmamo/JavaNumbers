# Numeric types - JVM edition

I am constantly amazed at how unfamiliar otherwise competent developers can be with numeric types. Most know that there are "inaccuracies", or "rounding errors" involved with floating point numbers, but few know why or can explain them. Many assume floats are decimal, even when GCSE students know computers work with binary bits. Without understanding the representation of numbers used by computers, developers cannot anticipate the issues that may arise and avoid them. 

This problem seems more common in developers who work with the JVM, than those who do C/C++. Many think that BigDecimal is the answer to all problems. C/C++ developers more often work in numerical applications, e.g. physics simulation, so this difference is understandable. Just to make things a little more awkward working on the JVM brings a few other issues of its own.

In this blog post I will cover the JVM's numeric types from the ground up, and as a bonus cover a few of those JVM specific quirks too.

## The common confusions

If you want the TL;DR; version of this article:

Common confusions:
  * Integers don't have a sign bit, they use two's complement
  * Floats & doubles are not decimal types
  * Floating point arithmetic can be error free
  * BigDecimal doesn't solve all your problems
  
Java Quirks
  * Java has two shift operators
  * Auto widening of smaller numeric types
  * Surprising behaviour of equality for autoboxed numeric types
  * Comparing +0 and -0 is inconsistent
  * Java's floating point implementation is fundamentally broken
  * In early versions Java would hang and get incorrect results when parsing de-normalised numbers
  
Bonus Links
  * Rational numbers
  * Surreal numbers

# Integer types

These days binary arithmetic is covered in [GCSE computing](https://www.aqa.org.uk/subjects/computer-science-and-it/gcse/computer-science-8520/subject-content/fundamentals-of-data-representation). I have had the pleasure of working with GCSE students at a school on the Isle of Dogs, and most students easily understand this concept.

### Number bases

Our conception of numbers has varied throughout time and between cultures. Many of us have heard of [Roman Numerals](https://en.wikipedia.org/wiki/Roman_numerals). We are indirectly familiar with [sexagesimal - base 60](https://en.wikipedia.org/wiki/Sexagesimal) numbers, because of the legacy they have left on our measurement of angles and time. Our main education has used the [Arabic-Hindu - base 10](https://en.wikipedia.org/wiki/Hindu%E2%80%93Arabic_numeral_system) number system. That base 10 system has thrived because it includes the concept of [zero](https://en.wikipedia.org/wiki/0), and because most of us have 10 fingers.

Computers are digital electronic systems, based around the bit. A bit can be a 0 or a 1. Computers use a [binary](https://en.wikipedia.org/wiki/Binary_number) number system, i.e. one with a base of 2.

When you are taught to do arithmetic using the base 10 system, you are taught to think in terms of columns of units, tens, hundreds, thousands &c. Units can be any number from 0 to 9. Tens are worth 10 times the value of the column. Hundreds 10 x 10 times the value. Thousands 10 x 10 x 19 times the value. Each column is worth 10 times the previous one. 

| Thousands | Hundreds | Tens | Units | Total in Decimal                                 |
|:---------:|:--------:|:----:|:-----:|:------------------------------------------------:|
|          1|         2|     0|      1|(1 x 1000) + (2 x 100) + (0 x 10) + (1 x 1) = **1201**|

Adding, for example, can be performed by first adding the units, adding the tens along with any carry over from the units, then the hundreds with the carry over from the tens, &c. I am not going to try to teach you all basic arithmetic, or sucking eggs here. My point is to show how binary numbers are no different from decimals, other than that the base we use is 2, not ten i.e. the value in each column can range from 0 to 1, and each column is worth 2 times the previous one.

| Eights | Fours | Twos | Ones | Total in Decimal                             |
|:------:|:-----:|:----:|:----:|:--------------------------------------------:|
|       1|      0|     1|     1|(1 x 8) + (0 x 4) + (1 x 2) + (1 x 1) = **11**|

### Confusion One - Integers don't have a sign bit

The system described so far doesn't include any way to specify a negative number. In other languages, e.g. C/C++, there are separate types for signed and unsigned integers. In Java et. al. we only have signed types.

How do you represent a signed number?

The obvious way to do so is to use 1 bit to represent the sign e.g.:

| Minus | Fours | Twos | Ones | Total in Decimal                                 |
|:------:|:-----:|:----:|:----:|:------------------------------------------------|
|       1|      0|     1|     1| (1 x -1) X ((0 x 4) + (1 x 2) + (1 x 1)) = **-3**|

Despite this being a common belief, this is not the case. Java, like most languages and the hardware we use, use a system called [two's complement](https://en.wikipedia.org/wiki/Two%27s_complement). I won't explain it here, the wikipedia article does a good enough job. If you find this interesting, you might also want to investigate [Gray code](https://en.wikipedia.org/wiki/Gray_code), another way to represent binary numbers that has uses in hardware and error correction codes.

Why do we use two's complement? It turns out that the algorithms needed to do arithmetic, like addition, subtraction and multiplication are identical for signed numbers in this format and for unsigned numbers. This meant processor manufacturers didn't need to implement two lots of circuitry for the two numeric types.

This also means that in effect that Java does have an unsigned type. It is the same type. Since Java 8 methods have been added to the standard library to make this more obvious. This code prints the signed and unsigned version of the integers as they roll over the 0 boundary, and the Integer.MAX_VALUE boundaries to highlight the difference:

```java
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
```

```
binary representation = 01111111111111111111111111111101, signed int value = 2147483645, unsigned int value = 2147483645
binary representation = 01111111111111111111111111111110, signed int value = 2147483646, unsigned int value = 2147483646
binary representation = 01111111111111111111111111111111, signed int value = 2147483647, unsigned int value = 2147483647
binary representation = 10000000000000000000000000000000, signed int value = -2147483648, unsigned int value = 2147483648
binary representation = 10000000000000000000000000000001, signed int value = -2147483647, unsigned int value = 2147483649
...
binary representation = 11111111111111111111111111111110, signed int value = -2, unsigned int value = 4294967294
binary representation = 11111111111111111111111111111111, signed int value = -1, unsigned int value = 4294967295
binary representation = 00000000000000000000000000000000, signed int value = 0, unsigned int value = 0
binary representation = 00000000000000000000000000000001, signed int value = 1, unsigned int value = 1
binary representation = 00000000000000000000000000000010, signed int value = 2, unsigned int value = 2
```

#JVM specific

Problems specific to java - problems from that java float is broken paper

instruction sets small for shorter types (auto widen)

auto box unbox pooling

compare +0 -0

shifting mask for numbers