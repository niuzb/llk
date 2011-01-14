/* Copyright © 2006 - Fabien GIGANTE */

import java.util.*;

/**
 * Extended version of the standard Random class
 */
class GameRandom extends Random
{
  /**
   * Returns the next pseudorandom, uniformly distributed int value from this random number generator's sequence.
   * Only needed for portability to CLDC 1.0 (this implementation is native with CLDC 1.1)
   */
  /*
  public int nextInt(int n)
  {
    if (n <= 0)
      throw new IllegalArgumentException("n must be positive");

    if ((n & -n) == n)  // i.e., n is a power of 2
      return (int)((n * (long)next(31)) >> 31);

    int bits, val;
    do
    {
      bits = next(31);
      val = bits % n;
    } while (bits - val + (n - 1) < 0);
    return val;
  }
  */

  /** Returns true if the next pseudorandom between 0 and n-1 egals 0 */
  public boolean nextBoolean(int n)
  {
    return nextInt(n) == 0;
  }

  /** Returns true if the next pseudorandom between 0 and 1 egals 0 */
  public boolean nextBoolean()
  {
    return nextBoolean(2);
  }
}