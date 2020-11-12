//------------------------------------------------------------------------------
// Bitmap
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------
package android.graphics;

public class Bitmap
 {public int width, height;
  public Bitmap() {}
  public Bitmap(int width, int height)
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
  public void setPixel(int a,int b,int c) {}
  public enum Config
   {ARGB_8888;
   }

  public void eraseColor(int a) {};

  public static void main(String[] args)
   {System.err.println("Hello World");
   }
 }
