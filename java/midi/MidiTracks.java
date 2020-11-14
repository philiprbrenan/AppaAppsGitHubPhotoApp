//------------------------------------------------------------------------------
// Midi tracks
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------
package com.appaapps;
import android.content.Context;
import java.io.File;
import java.util.Stack;
import java.util.TreeMap;

public class MidiTracks                                                         //C Midi tracks available
 {final static String
    dir   = "midi",                                                             // Midi folder
    music = dir+'/'+"music",                                                    // Midi music used in races
    right = dir+'/'+"right";                                                    // Midi played after right first time

  final public static Stack<String>
      midiMusic = new Stack<String> (),                                         // Midi music tracks
      midiRight = new Stack<String> ();                                         // Midi right tracks

  final public static RandomChoice<String>choose = new                          // A chooser to select a midi track at random
                      RandomChoice<String>();

  public static void download                                                   //M Download a zip file of midi
   (Context context,                                                            //P Android context if called from Android - should be set in Activity - else null
    final Stack<String> stack,                                                  //P The stack of midi to load
    final String folder)                                                        //P Folder in assets containing the midi tracks
   {try
     {if (context != null)                                                      // On Android
       {for(String k: Assets.files)
         {if (k.startsWith(folder)) stack.push(k);
         }
       }
      else                                                                      // On Ubuntu
       {File p = new File("/home/phil/AppaAppsGitHubPhotoApp/"+folder);
        String [] files = p.list();
        for(String f: files) stack.push(f);
       }
     }
    catch(Exception e)
     {
     }
   }

  public static void load                                                       //M Load details of midi tracks
   (Context context)                                                            //P Android context if called from Android - should be set in Activity - else null
   {download(context, midiMusic, music);
    download(context, midiRight, right);
   }

  public static String chooseMusic()                                            //M Choose a music track at random
   {return choose.chooseFromStack(midiMusic);                                   // Choose midi music
   }
  public static String chooseRight()                                            //M Choose a right track at random
   {return choose.chooseFromStack(midiRight);                                   // Choose midi right
   }

  public static void printMusic                                                 //M Print details of a music stack
   (final String title,                                                         //P Title of the stack
    final Stack<String> stack)                                                  //P Stack of music
   {final int N = stack.size();
    say(title, " has ", N, " entries");
   }

  public static void main                                                       //m Fetch a prompts file and choose a prompt from it at random
   (String[] args)                                                              //P Arguments
   {load(null);
    printMusic("Music", midiMusic);
    printMusic("Right", midiRight);
    say("Choose music: ", chooseMusic());
    say("Choose Right: ", chooseRight());
    say("Hello World\n");
   }

  static void say(Object...O) {Say.say(O);}
 }
