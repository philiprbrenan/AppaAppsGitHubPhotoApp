//------------------------------------------------------------------------------
// List of all files in assets
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2020
//------------------------------------------------------------------------------
package com.appaapps;

import android.content.Context;
import java.util.Stack;

public class Assets                                                             //C Assets
 {public static Stack<String> files = new Stack<String>();

  public static void load                                                       //M Asset file names
   (Context context)                                                            //P Context
   {Stack<String> stack = new Stack<String>();
    try
     {for(String s: context.getAssets().list(""))
       {say("Asset:", s);
        stack.push(s);
       }
      while(stack.size() > 0)
       {String s = stack.pop();
        files.push(s);
        for(String r: context.getAssets().list(s))
         {String q = s+'/'+r;
          stack.push(q);
          say("Asset:", q);
         }
       }
     }
    catch (Exception e) {}
   }

  public static void print()                                                    //M Print asset file names
   {for(String s: files)
     {say("Asset:", s);
     }
   }

  public static void main(String arg[])
   {
   }

  private static void say(Object...o)                                           // Log a message
   {com.appaapps.Log.say(o);
   }
 } //C Assets

