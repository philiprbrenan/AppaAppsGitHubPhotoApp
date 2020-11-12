//------------------------------------------------------------------------------
// Upload a stream to a web server encoding it in Base64 as we go
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------
// This version uses the Java version of Base64 which differs from the one supplied by Android
// base64 -d test.data | hexdump | head
package com.appaapps;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Base64.*;

public class UploadStreamTest extends Thread                                    //C A thread used to upload a stream to a url
 {public final URL  url;                                                        // Url to upload to
  public UploadStreamTest                                                       //c Create a new uploader
   (String  url)                                                                //P Url to upload to
   {URL u = null;
    try
     {u = new URL(url);
     }
    catch(Exception e)
     {say(e);
     }
    this.url = u;
   }

  public void run()                                                             //M Perform the upload when the thread is started or we call run()
   {final StringBuilder b = new StringBuilder();                                // Results from server
    HttpURLConnection u = null;                                                 // Connection
    Integer rc = null;                                                          // Response code from server
    try
     {u = (HttpURLConnection)url.openConnection();                              // Create (but do not open) the connection
      u.setRequestProperty("Accept-Encoding", "identity");                      // Stop compression
      u.setDoOutput(true);                                                      // Request upload processing
//    u.setChunkedStreamingMode(0);

      final Base64.Encoder encoder = Base64.getEncoder();
      final OutputStream
        os = u.getOutputStream(),                                               // Upload stream from connection
        bs = encoder.wrap(os);                                                  // Upload stream wrpaeed with a base64 encoder
      upload(bs);                                                               // Let the caller supply their data to the stream
      bs.close();                                                               // Close output stream
      rc = u.getResponseCode();                                                 // Save response code - has the side effect of opening the connection to the server

      final InputStream in = new BufferedInputStream(u.getInputStream());       // Read response from server
      for(int i = in.read(); i > -1; i = in.read())                             // Buffered so this should not be a problem
       {b.append((char)i);
       }
      in.close();
      finished(rc, b.toString());                                               // Report finished if we get this far
     }
    catch(Exception e) {failed(e);}                                             // Call failed() override if an exception is thrown
    finally            {u.disconnect();}                                        // Disconnect in every case
   }

  protected void upload                                                         //M Override to write the data to be uploaded into the specified stream
   (OutputStream os)                                                            //P Write data to this stream
   {}

// Override these methods to observe progress
  protected void finished                                                       //M Called when the upload has completed
   (Integer code,                                                               //P status code from server or  null if no status code received
    String result)                                                              //P String received from web server
   {}                                                                           // Override

  protected void failed                                                         //M Override called on failure
   (Exception e)                                                                //P Exception reporting failure
   {say(e);
    e.printStackTrace();
   }

  public static void main(String[] args)                                        // Upload a stream to a web server
   {final UploadStreamTest u = new UploadStreamTest
     ("http://www.appaapps.com/cgi-bin/gitAppaSaveFile.pl?userid=>q(philiprbrenan),repo=>q(screenShots-0),file=>q(test.data)")
     {protected void upload(OutputStream s)
       {try
         {for(int i = 0; i < 1e3; ++i) s.write('b');
         }
        catch(Exception e) {failed(e);}
       }
      protected void finished(Integer code, String result)
       {say(result);
       }
     };
                                                                                // Either of the following will work
    u.run();                                                                    // Start a file upload and wait for it to finish
//  u.start(); try{u.join();} catch(Exception e) {say(e);}                      // Start a file upload and wait for it to finish
   }

  static void say(Object...O) {Say.say(O);}
 }
