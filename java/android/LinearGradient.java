//------------------------------------------------------------------------------
// Bitmap
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------
package android.graphics;

public class LinearGradient
 {public int width, height;
  public LinearGradient() {}
  public LinearGradient(int width, int height)
   {this.width  = width;
    this.height = height;
   }
  public LinearGradient(float a, float b, float c, float d, int width, int height, Shader.TileMode z)
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

  public void setLocalMatrix(Matrix m) {}

  public static void main(String[] args)
   {System.err.println("Hello World");
   }
 }
