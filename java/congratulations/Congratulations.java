//------------------------------------------------------------------------------
// Congratulations
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------
package com.appaapps;
import android.content.Context;
import java.io.File;
import java.util.Stack;
import java.util.TreeMap;

public class Congratulations                                                    //C Spoken congratulations in the students native language
 {final static Stack<String> stack = new                                        // Stack of congratulations in the chosen language
               Stack<String>();

  final static RandomChoice<String>choose = new                                 // A chooser to select congratulations at random
               RandomChoice<String>();

  public static class Congratulation
   {String text;
    String sound;
    Congratulation(String text, String sound)
     {this.text  = text;
      this.sound = sound;
     }
   }

  public static Congratulation choose()                                         //M Choose a congratulation at random
   {String s = choose.chooseFromStack(stack);
    String t = s.replaceAll("^.*/", "").replaceAll("\\.\\w+$", "");
    return new Congratulation(s, t);
   }

  public static void load                                                       //C Available congratulations
   (Context context)                                                            //P Android context if called from Android - should be set in Activity - else null
   {try
     {if (context != null)                                                      // On Android
       {String [] files = context.getAssets().list("congratulations");          // Files in folder
        for(String f: files) stack.push(f);
       }
      else                                                                      // On Ubuntu
       {File p = new File("/home/phil/AppaAppsGitHubPhotoApp/congratulations");
        String [] files = p.list();
        for(String f: files) stack.push(f);
       }
     }
    catch(Exception e)
     {
     }
   }

  public static void main                                                       //m Fetch a congratulations file and choose a congratulation from it at random
   (String[] args)                                                              //P Arguments
   {load(null);
    say("Size:  ", stack.size());
    say("Hello World\n");
   }

  static void say(Object...O) {Say.say(O);}
 }
