//------------------------------------------------------------------------------
// Sink Sort - Partially sort a stack
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2018
//------------------------------------------------------------------------------
package com.appaapps;
import java.util.Stack;

abstract public class SinkSort<E>                                               //C Ordered Stack which allows duplicates in the lexicographical  order
 {public int compare() {return 0;}                                              //M Return -1 if element a is less than element b, 0 if they are equal, else +1

  E a, b;                                                                       // Elements to be compared
  public int compare                                                            //M Return -1 if element a is less than element b, 0 if they are equal, else +1
   (E a,                                                                        //P Element a
    E b)                                                                        //P Element b
   {this.a = a; this.b = b; return compare();
   }

  private void swap                                                             //M Swap two elements of a stack
   (final Stack<E> stack,                                                       //P The stack  containing th elements
    final int a,                                                                //P Index of the first element to swap
    final int b)                                                                //P Index of the second element to swap
   {final E A = stack.elementAt(a), B = stack.elementAt(b);
    stack.setElementAt(A, b);
    stack.setElementAt(B, a);
   }

  SinkSort                                                                      //c Fully sort the specified stack
   (final Stack<E> stack)                                                       //P The stack to sort
   {final int N = stack.size();
    for(int i = 1; i < N; ++i)
     {for(int j = N; j > i; --j)
       {final int
          a = j - 2,
          b = j - 1,
          c = compare(stack.elementAt(a), stack.elementAt(b));
        if (c > 0) swap(stack, a, b);                                           // Swap if the second element is smaller
       }
     }
   }

  SinkSort                                                                      //c Partially sort the specified stack
   (final Stack<E> stack,                                                       //P The stack to sort
    final int passes,                                                           //P Number of passes to perform - numerator
    final int scale)                                                            //P Number of passes to perform - denominator
   {final int N = stack.size();
    final float p = Math.max(0, Math.min(1, passes * 1f / scale));              // Probability between 0 and 1 of performing a swap when it is needed based on how close passes is to scale
    for(int i = 1; i < N; ++i)
     {for(int j = N; j > i; --j)
       {final int
          a = j - 2,
          b = j - 1,
          c = compare(stack.elementAt(a), stack.elementAt(b));
        if (c > 0 && Math.random() < p) swap(stack, a, b);                      // Swap into order more frequently as probability rises
       }
     }
   }

  SinkSort                                                                      //c Partially disrupt the specified stack
   (final Stack<E> stack,                                                       //P The stack to disrupt
    final int passes,                                                           //P Number of passes to perform - numerator
    final int scale,                                                            //P Number of passes to perform - denominator
    final String disrupt)                                                       //P "Disrupt"
   {final int N = stack.size();
    final float p = Math.max(0, Math.min(1, passes * 1f / scale));              // Probability between 0 and 1 of performing a swap
    for  (int i = 1; i < N; ++i)
     {for(int j = 1; j < N; ++j)
       {if (Math.random() < p / 2f) swap(stack, i-1, j-1);                      // Swap more erratically as probability rises
       }
     }
   }

  SinkSort                                                                      //c Disrupt the specified stack
   (final Stack<E> stack,                                                       //P The stack to disrupt
    final String disrupt)                                                       //P "Disrupt"
   {final int N = stack.size();
    for  (int i = 1; i < N; ++i)
     {for(int j = 1; j < N; ++j)
       {if (Math.random() < 1 / 2f) swap(stack, i-1, j-1);                      // Swap more erratically as probability rises
       }
     }
   }

  final static int N = 16;

  public static void main(String[] args)                                        // Tests
   {assert compareInt(1,2) == -1;
    assert compareInt(2,1) == +1;
    fullyOrdered();
    ordered(2, 8);
    ordered(4, 8);
    ordered(8, 8);
    disrupt(2, 8);
    disrupt(4, 8);
    disrupt(8, 8);
    fullyDisrupted();
   }

  public static void fullyOrdered()
   {final Stack<Integer> ints = new Stack<Integer>();
    for(int i = N; i > 0; --i) ints.push(i*i*i % 16);
    new SinkSort<Integer>(ints)
     {public int compare() {return compareInt(a, b);}
     };
    say("Fully ordered", ints);
   }

  public static void fullyDisrupted()
   {final Stack<Integer> ints = new Stack<Integer>();
    for(int i = 1; i <= N; ++i) ints.push(i);
    new SinkSort<Integer>(ints, "disrupted") {};
    say("Fully disrupted", ints);
   }

  public static void ordered(final int a, final int b)
   {final Stack<Integer> ints = new Stack<Integer>();
    for(int i = N; i > 0; --i) ints.push(i);
    new SinkSort<Integer>(ints, a, b)
     {public int compare() {return compareInt(a, b);}
     };
    say("Ordered ", a, "/", b, ints);
   }

  public static void disrupt(final int a, final int b)
   {final Stack<Integer> ints = new Stack<Integer>();
    for(int i = N; i > 0; --i) ints.push(i);
    new SinkSort<Integer>(ints, a, b, "disrupt") {};
    say("Disrupted ", a, "/", b, ints);
   }

  public static int compareInt(final int a, final int b)
   {if (a < b) return -1;
    if (a > b) return +1;
    return 0;
   }

  private static void say(Object...O) {Say.say(O);}
 }
