## The TL;DR; summary

If you want the TL;DR; version of this article:

Common confusions:
  * Integers don't have a sign bit, they use two's complement
  * Floats & doubles are not decimal types
  * Floats & doubles do have a sign bit
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



Problems specific to java - problems from that java float is broken paper

instruction sets small for shorter types (auto widen)

auto box unbox pooling

compare +0 -0

shifting mask for numbers