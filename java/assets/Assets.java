//------------------------------------------------------------------------------
// List of all files in assets
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2020
//------------------------------------------------------------------------------
package com.appaapps;

import android.content.Context;

import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.Stack;

public class Assets                                                             //C Assets
 {public final static Stack<String> files = new Stack<String>();                // Files in assets
  public       static Context context;                                          // Context

  public static void load                                                       //M Load asset file names
   (Context context)                                                            //P Context
   {Assets.context      = context;                                              // Context
    Stack<String> stack = new Stack<String>();                                  // Folders awaiting expansion

    try
     {for(String s: context.getAssets().list("")) stack.push(s);                // Get top most folder contents
      while(stack.size() > 0)
       {final String s = stack.pop();     files.push(s);
        for(final String r: context.getAssets().list(s)) stack.push(s+'/'+r);   // Expand each sub folder
       }
     }
    catch (Exception e) {}
   }

  public static String copyAssetsFileToRealFile                                 //M Copy an asset sound file to a proper file that can be used by the media player.
   (String assetFile)                                                           //P An asset file name
   {String outFile = assetFile.replaceAll("/", "_");
    InputStream      i = null;
    FileOutputStream o = null;
    final int N = 1024;
    try
     {i = context.getAssets().open(assetFile);
     }
    catch (Exception e)
     {say("Failed to open assets file: \""+assetFile+"\"");
      e.printStackTrace();
     }
    if (i != null) try
     {o = context.openFileOutput(outFile, Context.MODE_PRIVATE);
     }
    catch (Exception e)
     {say("Failed to open real file: \""+outFile+"\"");
      e.printStackTrace();
     }
    if (i != null && o != null) try
     {byte [] b = new byte[N*N];
      for(int j = 0; j < N; ++j)
       {final int r = i.read(b);
        if      (r == -1) break;
        else if (r >   0) o.write(b, 0, r);
       }
     }
    catch (Exception e)
     {say("Failed to copy asset: "+assetFile);
      e.printStackTrace();
     }

    if (i != null) try
     {i.close();
     }
    catch (Exception e)
     {say("Failed to close asset: "+assetFile);
      e.printStackTrace();
     }

    if (o != null) try
     {o.close();
     }
    catch (Exception e)
     {say("Failed to close real file: "+outFile);
      e.printStackTrace();
     }

    return outFile;
   }

  public static void print()                                                    //M Print asset file names
   {for(String s: files) say("Asset:", s);
   }

  public static void main(String[] args)                                        //m Test
   {System.out.println("Hello World\n");
   }

  private static void say(Object...o)                                           // Log a message
   {com.appaapps.Log.say(o);
   }
 } //C Assets
