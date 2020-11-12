//------------------------------------------------------------------------------
// Time
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------
package com.appaapps;

public class Time
 {public static void main(String[] args)
   {say("Secs  (3): ",  sinceSecs (milli() - 3*      1000-1));
    say("Mins  (2): ",  sinceMins (milli() - 2*   60*1000-1));
    say("Hours (1): ",  sinceHours(milli() - 1*60*60*1000-1));

    for(int  i = 0; i < 10; ++i)
     {say("Seconds: (", i, ") ", secs());
      try
       {Thread.sleep(1000);
       }
      catch(Exception e)
       {}
     }
   }
  public static double milli()                                                  // Time in milliseconds
   {return System.currentTimeMillis();
   }
  public static double secs()                                                   // Time in seconds
   {return milli() / 1000d;
   }
  public static double mins()                                                   // Time in minutes
   {return secs()  / 60d;
   }
  public static double hours()                                                  // Time in hours
   {return mins() / 60d;
   }
  public static double sinceMilli                                               // Since specified time in milliseconds
   (double time)                                                                // Reference time in milliseconds
   {return System.currentTimeMillis() - time;
   }
  public static double sinceSecs                                                // Time in seconds
   (double time)                                                                // Reference time in milliseconds
   {return sinceMilli(time) / 1000d;
   }
  public static double sinceMins                                                // Time in minutes
   (double time)                                                                // Reference time in milliseconds
   {return sinceSecs(time)  / 60d;
   }
  public static double sinceHours                                               // Time in hours
   (double time)                                                                // Reference time in milliseconds
   {return sinceMins(time) / 60d;
   }
  private static void say(Object...O) {Say.say(O);}
 }
