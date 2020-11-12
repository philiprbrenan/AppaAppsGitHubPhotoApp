//------------------------------------------------------------------------------
// Coordinates of point of maximum interest in a photo
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2018
//------------------------------------------------------------------------------
package com.appaapps;
import android.content.Context;
import android.graphics.Point;

import java.io.File;
import java.util.TreeMap;

public class PhotoCoordinates                                                   //C Download translations of text used in the apps
 {private static DownloadAndUnzip coordinatesFile = null;                       // Downloader and unzipper for text translations
  final private static String
    fileOnServer = "photoCoordinates/photoCoordinates.zip",                     // File to download from
    fileLocally  = "photoCoordinates.zip";                                      // File to download to
  final private static TreeMap<String,Point> coordinates =                      // Coordinates for each photo - expressed as an integer percentage
                   new TreeMap<String,Point>();
  private static boolean downloadComplete = false;                              // Set to true when the download is complete

  public boolean downloadComplete()                                             // Read only access to the download complete field
   {return downloadComplete;
   }

  public static Point getCoordinates                                           //M Get coordinates of a photo
   (final String string)                                                        //P Name of photo
   {return coordinates.get(string);                                             // Get coordinates of a photo
   }

  public static void download                                                   //M Download the coordinate files
   (final Context context,                                                      //P Context from Android
    final String domain)                                                        // Domain to download from
   {coordinatesFile = new DownloadAndUnzip
     (context, domain, fileOnServer, fileLocally)
     {protected void finished()                                                 // At finish of unzip create photo coordinates tree
       {downloadComplete = true;
         for(String entry: entries())                                           // Each file of coordinates
          {final String content = get(entry);                                   // content
           for(String line: content.split("\\n"))                               // Split content into lines
            {final String[]word = line.split("\\s+");                           // Split line into words
             coordinates.put(word[0].toLowerCase(),                             // Add coordinates of photo to tree
               new Point(atoi(word[1]), atoi(word[2])));
            }
          }
       }
     };
   }

  private static int atoi                                                       //M convert a string to an integer or zero
   (final String string)                                                        // String to be converted
   {try                                                                         // Not a command so it must be an integer
     {return Integer.parseInt(string);
     }
    catch(Exception e)
     {say("Unable to convert ", string, "to integer because: "+ e);
     }
    return 0;                                                                   // This should not happen because these kinds of errors are detected when the coordinates are created
   }

  public static void main(String[] args)                                        // Test
   {download(null, "www.appaapps.com");
    for(int i = 0; i < 10; ++i)
     {if (!downloadComplete)
       {try{Thread.sleep(1000);} catch(Exception e) {}
       }
      else
       {for(String k: coordinates.keySet())
         {say(k, " ", coordinates.get(k));
         }
        break;
       }
     }
   }

  static void say(Object...O) {Say.say(O);}
 }
