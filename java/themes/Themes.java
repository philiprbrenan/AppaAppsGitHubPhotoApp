//------------------------------------------------------------------------------
// Create colour themes for objects drawn by Svg
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------

package com.appaapps;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;

public class Themes                                                             //C Various colour themes for objects drawn by Svg
 {final private ColoursTransformed ct = new ColoursTransformed();               // Colour transformer
  final Shader.TileMode mirror = Shader.TileMode.MIRROR;                        // Mirror mode for linear gradients
  final Matrix matrix = new Matrix();                                           // Matrix for rotations
  final Bitmap chessBitmap = chessBitmap();                                     // Bitmap used to draw the chess theme

  public Theme fromName                                                         //M Make a theme from its name - this should only be used when receiving a theme name as it is inefficient
   (String  name)                                                               //P Name of the theme to make
   {if (name.equalsIgnoreCase("chess"))     return chess();
    if (name.equalsIgnoreCase("waves"))     return waves();
    if (name.equalsIgnoreCase("tartan"))    return tartan();
    if (name.equalsIgnoreCase("white"))     return white();
    if (name.equalsIgnoreCase("black"))     return black();
    if (name.equalsIgnoreCase("redBlue"))   return redBlue();
    if (name.equalsIgnoreCase("blueGreen")) return blueGreen();
    if (name.equalsIgnoreCase("blueRed"))   return blueRed();
    return tartan();                                                            // Return the default theme
   };

  public Chess chess()                                                          //M A new chess board theme
   {return new Chess(1f);
   }

  public Waves waves()                                                          //M A new waves theme with default speed
   {return new Waves(1f);
   }

  public Waves waves                                                            //M A new wave theme with the specified speed
   (final float speed)                                                          //P Speed relative to 1
   {return new Waves(speed);
   }

  public Tartan tartan()                                                        //M A new tartan theme
   {return new Tartan();
   }

  public White white()                                                          //M A new white on black theme
   {return new White();
   }

  public Black black()                                                          //M A new black on white theme
   {return new Black();
   }

  public BlueGreen blueGreen()                                                  //M A new blue on green theme
   {return new BlueGreen();
   }

  public BlueRed blueRed()                                                      //M A new blue on red theme
   {return new BlueRed();
   }

  public RedBlue redBlue()                                                      //M A new red on blue theme
   {return new RedBlue();
   }

  abstract public class Theme                                                   //C Each theme should be derived from this class
   {final public float speed;                                                   // The speed of this theme relative to 1.
    final public Paint                                                          // A palette of paints
      p = new Paint(), P = new Paint(),                                         // First paint to be set  background - text
      q = new Paint(), Q = new Paint(),                                         // Second paint to be set background - text
      r = new Paint(), R = new Paint();                                         // Third paint to be set  background - text

    abstract public void set                                                    //m Set the paints needed to draw a theme
     (Canvas canvas,                                                            //P Canvas so that we can get the size of the drawing area
      final float w,                                                            //P Approximate width of thing to be drawn
      final float h);                                                           //P Approximate height of thing to be drawn

    abstract public Theme make();                                               //M Override to make a new instance of this theme

    public Theme()                                                              //C Theme with default speed
     {this.speed = 1;
     }

    public Theme                                                                //C Theme with specified speed: The speed of this theme relative to 1, 2 is twice as fast, 1/2 is twice as slow etc.
     (final float speed)                                                        //P Speed of the theme
     {this.speed = speed;
     }

    abstract public String name();                                              //M Name of theme as a string
   } //C Pattern

  public class Waves                                                            //C Create a waves paint gradient
    extends Theme
   {private final Fourier                                                       // Corners, orientation
      x, y,  X, Y, a;
    private final int[]                                                         // Use all metal or all vivid at a reasonable opacity
      colours = ct.metalOrVivid(2);
    private final int                                                           // Colours
      c1 = colours[0],                                                          // Colour with opacity so we can overlay more than one paint
      c2 = colours[1],                                                          // Ditto
      C1 = ColoursTransformed.opposite(c1),                                     // Opposing colour
      C2 = ColoursTransformed.opposite(c2);                                     // Ditto

    private Waves                                                               //C Create a waves paint gradient
     (final float speed)                                                        //P Speed relative to 1
     {super(speed);                                                             // Save the speed
      x = new Fourier(speed);                                                   // Corners
      y = new Fourier(speed);
      X = new Fourier(speed);
      Y = new Fourier(speed);
      a = new Fourier(speed);                                                   // Orientation
     }

    public Waves make()                                                         //O=com.appappps.Gradients.Pattern.make Make a new instance of this theme thus providing variation within a common theme
     {return new Waves(speed);
     }

    public void set                                                             //O=com.appaapps.Gradients.set Update the latest state of the theme to allow time evolution
     (Canvas canvas,                                                            //P Canvas so that we can get the size of the drawing area
      final float w,                                                            //P Approximate width in pixels of the thing to be drawn
      final float h)                                                            //P Approximate height in pixels of the thing to be drawn
     {final LinearGradient
        g1 = new LinearGradient(x.get()*w, 0, X.get()*w, 0, c1, C1, mirror),
        g2 = new LinearGradient(0, y.get()*h, 0, Y.get()*h, c2, C2, mirror);    // We could add a small offset in x at one corner to get a spiral effect at full width or zero width - I originally did this by accident and it took ahes to find woiut why the patern was rotating without any rotation supplied
      final Matrix matrix = new Matrix();
      matrix.reset();
      final float A = a.get(), angle = A * 360;
      matrix.postRotate(angle);
      g1.setLocalMatrix(matrix);
      g2.setLocalMatrix(matrix);
      p.setShader(g1);
      p.setAntiAlias(true);
      p.setDither(true);
      q.setShader(g2);
      q.setAntiAlias(true);
      q.setDither(true);
      P.set(p); Q.set(q); R.set(r);
     }
    public String name()                                                        //O=com.appappps.Gradients.Pattern.name Name of the theme
     {return "waves";
     }
   } //C Waves

  public class Tartan                                                           //C Create a tartan paint gradient as a static wave
    extends Waves
   {public Tartan()
     {super(0f);
     }
    public String name()                                                        //O=com.appappps.Gradients.Pattern.name Name of the theme
     {return "tartan";
     }
   } //C Tartan

  public class Chess                                                            //C Create a chess board theme in black and white
    extends Theme
   {private Chess                                                               //C Create a chess board paint gradient
     (final float speed)                                                        //P Speed relative to 1
     {super(speed);                                                             // Save the speed
     }

    public Chess make()                                                         //O=com.appappps.Gradients.Pattern.make Make a new instance of this theme thus providing variation within a common theme
     {return new Chess(speed);
     }

    public void set                                                             //O=com.appaapps.Gradients.set Set the two paints needed to draw the tartan gradient
     (Canvas canvas,                                                            //P Canvas so that we can get the size of the drawing area
      final float w,                                                            //P Approximate width in pixels of the thing to be drawn
      final float h)                                                            //P Approximate height in pixels of the thing to be drawn
     {final BitmapShader g = new BitmapShader(chessBitmap, mirror, mirror);     // Create the gradient
      p.setShader(g);
      p.setAntiAlias(true);
      p.setDither(true);
      q.setColor(0);
      P.set(p); Q.set(q); R.set(r);
     }
    public String name()                                                        //O=com.appappps.Gradients.Pattern.name Name of the theme
     {return "chess";
     }
   } //C Chess

  public abstract class Contrast                                                //C Create a theme of two contrasting colours
    extends Theme
   {final int
      fore,                                                                     // Foreground colour
      back;                                                                     // Background colout
     public void set                                                            //O=com.appaapps.Gradients.set Set the two paints needed to draw the theme
     (Canvas canvas,                                                            //P Canvas so that we can get the size of the drawing area
      final float w,                                                            //P Approximate width in pixels of the thing to be drawn
      final float h)                                                            //P Approximate height in pixels of the thing to be drawn
     {final float width = Math.max(Math.min(16, (w+h)/16), 2);                  // Text outline

      p.setAntiAlias(true);
      p.setDither(true);
      p.setColor(back);
      p.setStrokeWidth(width);
      p.setStyle(Paint.Style.FILL);

      q.setAntiAlias(true);
      q.setDither(true);
      q.setColor(fore);
      q.setStyle(Paint.Style.FILL);

      P.set(p); Q.set(q); R.set(r);
      P.setStyle(Paint.Style.FILL_AND_STROKE);
      p.setAlpha(0x7f);
      q.setAlpha(0x7f);
     }
    public Contrast                                                             //C Create a theme of two contrasting colours
     (final int back,                                                           // Background colout
      final int fore)                                                           // Foreground colour
     {this.fore = fore;
      this.back = back;
     }
    public String name()                                                        //O=com.appappps.Gradients.Pattern.name Name of the theme
     {return "Contrast";
     }
   } //C Contrast

  public class White                                                            //C Plain white on black
    extends Contrast
   {public White()                                                              //c Plain white on black
     {super(0xff000000, 0xffffffff);
     }
    public White make()                                                         //O=com.appappps.Gradients.Pattern.make Make a new instance of this theme
     {return this;
     }
    public String name()                                                        //O=com.appappps.Gradients.Pattern.name Name of the theme
     {return "White";
     }
   } //C White

  public class Black                                                            //C Plain black on white
    extends Contrast
   {public Black()                                                              //c Plain black on white
     {super(0xffffffff, 0xff000000);
     }
    public Black make()                                                         //O=com.appappps.Gradients.Pattern.make Make a new instance of this theme
     {return this;
     }
    public String name()                                                        //O=com.appappps.Gradients.Pattern.name Name of the theme
     {return "Black";
     }
   } //C Black

  public class BlueRed                                                          //C Blue on Red
    extends Contrast
   {public BlueRed()                                                            //c Blue on Red
     {super(0xffff0000, 0xff0000ff);
     }
    public BlueRed make()                                                       //O=com.appappps.Gradients.Pattern.make Make a new instance of this theme
     {return this;
     }
    public String name()                                                        //O=com.appappps.Gradients.Pattern.name Name of the theme
     {return "BlueRed";
     }
   } //C BlueRed

  public class BlueGreen                                                        //C Blue on green
    extends Contrast
   {public BlueGreen()                                                          //c Blue on green
     {super(0xff00ff00, 0xff0000ff);
     }
    public BlueGreen make()                                                     //O=com.appappps.Gradients.Pattern.make Make a new instance of this theme
     {return this;
     }
    public String name()                                                        //O=com.appappps.Gradients.Pattern.name Name of the theme
     {return "BlueGreen";
     }
   } //C BlueGreen

  public class RedBlue                                                          //C Red on blue
    extends Contrast
   {public RedBlue()                                                            //c Red on blue
     {super(0xff0000ff, 0xffff0000);
     }
    public RedBlue make()                                                       //O=com.appappps.Gradients.Pattern.make Make a new instance of this theme
     {return this;
     }
    public String name()                                                        //O=com.appappps.Gradients.Pattern.name Name of the theme
     {return "RedBlue";
     }
   } //C RedBlue

  private Bitmap chessBitmap()                                                  //M Create the chess board  bitmap used to draw the chess board theme
   {final int                                                                   // Colours
      black = ColoursTransformed.black,                                         // Colour with opacity so we can overlay more than one paint
      white = ColoursTransformed.white,                                         // Ditto
      w = 2;                                                                    // Dimensions of black and white areas in bit map
    final Bitmap B = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888);        // Create  bitmap
    B.eraseColor(white);                                                        // Draw the white edge of the bitmap
    for(int i = 0; i < w; ++i) B.setPixel(i, i, black);
    return B;                                                                   // Return the constructed chess board bitmap
   }

  public static void main(String[] args)
   {System.err.println("Hello World");
   }

  static void say(Object...O) {Say.say(O);}
 } //C Gradients
