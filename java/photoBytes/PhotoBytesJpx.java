//------------------------------------------------------------------------------
// Photo encoded as an array of tiles
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2018
//------------------------------------------------------------------------------
package com.appaapps;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Stack;
import java.util.TreeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class PhotoBytesJpx extends PhotoBytes                                   //C Photo encoded as jpx
 {final public byte[][][] photoBytes;                                           // Tiles comprising this image
  final public int height, width, size, X, Y;                                   // Height, width of image in pixels, size of each picture in tiles
  final public String source;                                                   // Source of the image as specified in the jpx manifest file
  final public String name;                                                     // Name of photo as specified during construction

  public PhotoBytesJpx                                                          //c Constructor
   (final Stack<String> files,                                                  //P File names in assets
    final String        Key)                                                    //P Select the entries that start with this key
   {final String key = Key + "/";                                               // Folder name
    final int N = key.length();
    int height = 0, width = 0, size = 0, X = 0, Y = 0;
    String source = null;
    byte[][][]a = null;                                                         // Tiles in image - unfinalized

    for(int pass = 0; pass < 2; ++pass)                                         // Arrange the tiles
     {for(String k: files)                                                      // Each entry in assets
       {if  (k.endsWith("/"))   continue;                                       // Directory
        if (!k.startsWith(key)) continue;                                       // Entries for the specified image
        final String s = k.substring(N);                                        // File name minus folder
        if (s.startsWith("jpx.data"))                                           // Process manifest entries describing photo
         {try
           {final String L = new String(load(k), StandardCharsets.UTF_8);
            for(final String l : L.split("\\n"))
             {final String[]w = l.split("\\s+");
              if      (w[0].equalsIgnoreCase("height")) height = s2i(w[1]);
              else if (w[0].equalsIgnoreCase("width"))  width  = s2i(w[1]);
              else if (w[0].equalsIgnoreCase("size"))   size   = s2i(w[1]);
              else if (w[0].equalsIgnoreCase("source")) source =     w[1];
             }
            continue;
           }
          catch(Exception e)
           {System.err.println(e);
            e.printStackTrace();
           }
         }
        else
         {final String[]w = s.split("_|\\.");
          try
           {final int y = s2i(w[0]), x = s2i(w[1]);                             // Coordinates of tile
            if (x > X) X = x;
            if (y > Y) Y = y;
            if (pass == 1) a[y-1][x-1] = load(k);                               // Bytes for tile
           }
          catch(Exception e)
           {System.err.println("Unable to parse: "+s+"\n"+e);
            e.printStackTrace();
           }
         }
        if (pass == 0) a = new byte[Y][X][];                                    // Allocate arrays of tiles
       }
     }
    this.photoBytes = a;                                                        // Finalize photo bytes
    this.height = height;                                                       // Finalize height of image
    this.width  = width;                                                        // Finalize width of image
    this.size   = size;                                                         // Finalize size of each tile
    this.source = source;                                                       // Finalize source of photo
    this.name   = key;                                                          // Finalize name of photo
    this.X      = X;                                                            // Number of tiles in X
    this.Y      = Y;                                                            // Number of tiles in Y
   }

  public byte[] loadAsset                                                       //M Load bytes from a file in assets
   (String file)                                                                //P File name to load
   {final int N = 1024;
    ByteArrayOutputStream B = new ByteArrayOutputStream(N*N);
    InputStream           i = null;
    try
     {i = Assets.context.getAssets().open(file);
      byte    []b = new byte[N*N];
      for(int j = 0; j < N; ++j)
       {final int r = i.read(b);
        if      (r == -1) {i.close(); break;}
        else if (r >   0) B.write(b, 0, r);
       }
     }
    catch (Exception e)
     {say("Failed to read file: "+file);
      e.printStackTrace();
     }
    return B.toByteArray();
   }

  public byte[] loadLocal                                                       //M Load bytes from a file on test computer
   (String file)                                                                //P File name to load
   {final int N = 1024;
    ByteArrayOutputStream B = new ByteArrayOutputStream(N*N);
    try
     {InputStream i = new FileInputStream(file);
      byte    []b = new byte[N*N];
      for(int j = 0; j < N; ++j)
       {final int r = i.read(b);
        if      (r == -1) {i.close(); break;}
        else if (r >   0) B.write(b, 0, r);
       }
     }
    catch (Exception e)
     {say("Failed to read file: "+file);
      e.printStackTrace();
     }
    return B.toByteArray();
   }

  public byte[] load                                                            //M Load bytes from a file
   (String file)                                                                //P File name to load
   {if (file.startsWith("/home/phil/")) return loadLocal(file);
    else                                return loadAsset(file);
   }

  public Draw prepare                                                           //O=com.appapps.PhotoBytes.prepare - prepare to draw the photo
   (final RectF picture,                                                        //P Rectangle in which to record the dimensions of bitmap
    final int   proposedBitMapScale)                                            //P Proposed scale to apply to the bitmap
   {final Draw d = new Draw(proposedBitMapScale, X, Y)                          // Decompress the bitmap
     {public void run()                                                         //O=Thread.run Prepare bitmaps to display photo
       {final int s = getActualBitMapScale();
        picture.set(0, 0, width / s, height / s);                               // Size of bitmap after any scaling

        for  (int j = 0; j < Y; ++j)                                            // Each tile
         {for(int i = 0; i < X; ++i)
           {final byte[] b = photoBytes[j][i];                                  // Prepare bitmap for tile
            final Bitmap B = bitmap[j][i] = BitmapFactory.decodeByteArray
             (b, 0, b.length, bitmapOptions);
           }
         }
       }

      public void draw                                                          //M Draw the photo
       (final Canvas canvas)                                                    //P Canvas - scaled and translated so that we can draw at coordinates (0,0) in the size of the photo
       {final int s = size / getActualBitMapScale();
        for  (int j = 0; j < Y; ++j)                                            // Draw each sub bit map
         {for(int i = 0; i < X; ++i)
           {canvas.drawBitmap(bitmap[j][i], i*s, j*s, null);                    // Place each bitmap
           }
         }
       }
     };
    return d;
   }

  private int s2i                                                               //M Convert a string to integer
   (final String s)                                                             //P String
   {try
     {return Integer.parseInt(s);
     }
    catch(Exception e)
     {System.err.println(s+"="+e);
      e.printStackTrace();
      return 0;
     }
   }

  public String toString()                                                      //M Convert to string
   {final StringBuilder s = new StringBuilder();
    s.append("{Source=>" +source);
    s.append(", Size=>"  +size);
    s.append(", Height=>"+height);
    s.append(", Width=>" +width);
    s.append(", X=>"     +X);
    s.append(", Y=>"     +Y);
    s.append("}");
    return s.toString();
   }

  public static void main(String[] args)                                        //m Test
   {Stack<String> files = fileList();
    PhotoBytesJpx photo = new PhotoBytesJpx(files, "/home/phil/AppaAppsGitHubPhotoApp/build/assets/images/Autumn L");
    assert  (""+photo).equals("{Source=>/home/phil/AppaAppsGitHubPhotoApp/images/Autumn, Size=>256, Height=>768, Width=>1024, X=>4, Y=>3}");
    say("Hello World\n");
   }

  public static Stack<String> fileList()
   {String dir = "/home/phil/AppaAppsGitHubPhotoApp/build/assets/";
    Stack<String> files = new Stack<String>();                                  // Files found
    Stack<File>   stack = new Stack<File>();                                    // Folders awaiting expansion

    try
     {for(File f: new File(dir).listFiles()) stack.push(f);                     // Get top most folder contents
      while(stack.size() > 0)
       {final File f = stack.pop();
        files.push(f.toString());
        if (f.isDirectory()) for(final File r: f.listFiles()) stack.push(r);    // Expand each sub folder
       }
     }
    catch (Exception e)
     {say("Exception "+e); e.printStackTrace();
     }
    return files;
   }

  static void say(Object...O) {Say.say(O);}
 } //C PhotoBytesJP
