package android.util;

public class Pair<First, Second>
 {public final First  first;
  public final Second second;

  public Pair(First first, Second second)
   {this.first = first;
    this.second = second;
   }

  public static <First, Second> Pair<First, Second>
    create(First first, Second second)
   {return new Pair<First, Second>(first, second);
   }

  public static void main(String[] args)
   {say("Pair");
    final Object o = Pair.create(new Integer(1), new Integer(1));
    final Object p = new Pair<Integer,Integer>(1, 1);
   }
  static void say(Object...O) {final StringBuilder b = new StringBuilder(); for(Object o: O) b.append(o.toString()); System.err.print(b.toString()+"\n");}    /* Utilities: say(...) */
 }
