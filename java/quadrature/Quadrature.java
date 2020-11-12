//------------------------------------------------------------------------------
// The quadrature tree of commands that the user can select down to step through
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------
package com.appaapps;
import android.content.Context;
import java.io.File;
import java.util.TreeMap;

public class Quadrature                                                         //C Download and reconstruct the quadrature tree
 {public  static DownloadAndUnzip quadratureZipFile = null;                     // Download the zip file
  final private static TreeMap<String,TreeMap<String,String>> translations =    // Translations by language
                   new TreeMap<String,TreeMap<String,String>>();
  final private static String
    fileOnServer = "quadrature.zip",                                            // File to download from
    fileLocally  = "quadrature.zip";                                            // File to download to
  private static boolean downloadComplete = false;                              // Set to true when the download is complete

  public boolean downloadComplete()                                             // Read only access to the download complete field
   {return downloadComplete;
   }

  public static void download                                                   //M Download the translations
   (final Context context,                                                      //P Context from Android
    final String domain)                                                        //P Domain to download from
   {quadratureZipFile = new DownloadAndUnzip                                    // Download the zip file
     (context, domain, fileOnServer, fileLocally)
     {protected void finished()                                                 // At finish of unzip
       {//...
        downloadComplete = true;                                                // Show that the translations are ready for use
       }
     };
   }

  public static void downloadAndWait                                            //M start the download then qait on a spin lock for the download to complete
   (final Context context,                                                      //P Context from Android
    final String domain)                                                        //P Domain to download from
   {download(context, domain);
    for(int i = 0; i < 1e4; ++i)                                                // Wait for the download to finish
     {if (downloadComplete) break;
      try{Thread.sleep(100);} catch(Exception e) {}
     }
   }

  public static void main(String[] args)                                        // Test
   {downloadAndWait(null, "www.appaapps.com");
    say(translate("it", "Three Letter Words In French"), "==Parole di tre lettere in francese");
    say(translate("it", "Ant"), "==Formica");
    say(translate("it", "ant"), "==Formica");
    assert TextTranslations.translate("it", "Three Letter Words In French")
                               .equals("Parole di tre lettere in francese");
    say("Success");
   }

  static void say(Object...O) {Say.say(O);}
 }
