//------------------------------------------------------------------------------
// Bitmap
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------
package android.graphics;

public class BitmapShader
 {public int width, height;
  public BitmapShader() {}
  public BitmapShader(int width, int height)
   {this.width  = width;
    this.height = height;
   }
  public final int getWidth () {return width;}
  public final int getHeight() {return height;}
  public static Bitmap createBitmap(int width, int height, Object config)
   {final Bitmap b = new Bitmap();
    b.width  = width;
    b.height = height;
    return b;
   }

  public BitmapShader(Bitmap c, Shader.TileMode a, Shader.TileMode b) {}

  public static void main(String[] args)
   {System.err.println("Hello World");
   }
 }
