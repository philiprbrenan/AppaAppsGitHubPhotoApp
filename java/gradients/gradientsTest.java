//------------------------------------------------------------------------------
// Create gradient paints
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------
package com.appaapps;
import android.graphics.*;
import android.util.*;

public class Gradients                                                          //C Graduated paints in various patterns
 {public Tartan tartan()                                                        //M A new tartan paint
   {return new Tartan();           }
  void setPaintL(Canvas c, Paint paint)
   {final LinearGradient g = new LinearGradient
     (0, 0, 100, 50, 0xffff0000, 0xff00ff00, Shader.TileMode.MIRROR);
    final Matrix matrix = new Matrix();
    final float a = (float)((t()*20)%360);
    matrix.reset();
    matrix.postRotate(a);
    matrix.postSkew(2, 1);
    g.setLocalMatrix(matrix);
    paint.setShader(g);
    paint.setAntiAlias(true);
    paint.setDither(true);
   }
  void setPaintR(Canvas c, Paint paint)
   {final RadialGradient g = new RadialGradient
     (0, 0, 100, 0xffff0000, 0xff00ff00, Shader.TileMode.MIRROR);
    final Matrix matrix = new Matrix();
    final float a = (float)((t()*20)%360);
    matrix.reset();
    matrix.postRotate(a);
    matrix.postSkew(2, 1);
    g.setLocalMatrix(matrix);
    paint.setShader(g);
    paint.setAntiAlias(true);
    paint.setDither(true);
   }
  void setPaintS(Canvas c, Paint paint, boolean reverse)
   {final float qx = c.getWidth() / 4f, qy = c.getHeight() / 4f;
    final float cx = (reverse ? 1 : 3) * qx, cy = 2  * qy;
    final int[]c1 = {0xffff0000, 0xff00ff00, 0xffff0000, 0xff00ff00, 0xffff0000, 0xff00ff00, 0xffff0000, 0xff00ff00, 0xffff0000};
    final int[]c2 = {0xff00ff00, 0xffff0000, 0xff00ff00, 0xffff0000, 0xff00ff00, 0xffff0000, 0xff00ff00, 0xffff0000, 0xff00ff00};
    final int[]colours = reverse ? c1 : c2;
    final SweepGradient g = new SweepGradient(cx, cy, colours, null);
    final Matrix matrix = new Matrix();
    final float a = (float)((t()*20)%360);
    matrix.reset();
    matrix.postRotate(a, cx, cy);
    g.setLocalMatrix(matrix);
    paint.setShader(g);
    paint.setAntiAlias(true);
    paint.setDither(true);
   }
  void setPaintS2(Canvas c, Paint paint)
   {final float cx1 = 3f/4f * c.getWidth(), cy1 = c.getHeight() / 2f;
    final float cx2 = 1f/4f * c.getWidth(), cy2 = c.getHeight() / 2f;
    final int[]colours1 = {0x44440000, 0x44004400, 0x44440000};
    final SweepGradient g1 = new SweepGradient(cx1, cy1, colours1, null);
    final int[]colours2 = {0x44004444, 0x44440044, 0x44004444};
    final SweepGradient g2 = new SweepGradient(cx2, cy2, colours2, null);
//  final Matrix m1 = new Matrix(), m2 = new Matrix();
//  final float a = (float)((t()*20)%360);
//  m1.reset(); m1.postRotate(a, cx1, cy1); g1.setLocalMatrix(m1);
//  m2.reset(); m2.postRotate(a, cx2, cy2); g2.setLocalMatrix(m2);

    final ComposeShader g = new ComposeShader(g1, g2, PorterDuff.Mode.SCREEN);

    paint.setShader(g);
    paint.setAntiAlias(true);
    paint.setDither(true);
   }

  static void say(Object...O) {Say.say(O);}
  private static double t() {return System.currentTimeMillis() / 1000d;}
 } //C Svg
