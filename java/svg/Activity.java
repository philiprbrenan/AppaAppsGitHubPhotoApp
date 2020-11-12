/*------------------------------------------------------------------------------
Test Svg
Philip R Brenan at gmail dot com, © Appa Apps Ltd Inc 2016.06.24 21:39:25
------------------------------------------------------------------------------*/
package com.appaapps;

import android.graphics.*;
import android.graphics.Bitmap.*;
import android.view.*;

public class Activity extends android.app.Activity
 {final String text = "Hello world - what a nice day it is today to be sure";
  final Bitmap bitmap = Svg.testImage();
  Svg svg;

  public void onCreate(android.os.Bundle save)
   {super.onCreate(save);
    svg = new Svg();
//    svg.Rectangle(0.1f, 0.1f, 0.9f, 0.99f)    .setCenterToCenter(5, 5, 5,  0, 0, 1, 1);
//    svg.Image(bitmap, 0.1f, 0.1f, 0.9f,  0.2f);
//    svg.Image(bitmap, 0.1f, 0.1f, 0.9f,  0.2f).setCenterToCenter(5, 5, 5,  0.1f, 0.8f, 0.9f, 0.9f);
//    svg.Image(bitmap, 0.1f, 0.2f, 0.15f, 0.5f).setCenterToCenter(5, 5, 5,  0.7f, 0.2f, 0.9f, 0.9f);
//    svg.Text (text,   0.1f, 0.7f, 0.9f,  1.0f, +1, +1).setCenterToCenter(5, 5, 5,  0.1f, 0.1f, 0.9f, 0.4f);
    svg.AFewChars ("O", 0, 0, 1, 1, 0, 0);
    setContentView(new Display());
   }

  class Display extends android.view.View
   {Display()                                                                   // Create display
     {super(Activity.this);
     }

    public void onDraw(android.graphics.Canvas c)                               // Draw
     {final Paint p = new Paint();
      final int delta = 100;

      postInvalidateDelayed(50);                                                // Keep the redraw cycle going
      c.drawColor(0xff000000);                                                  // Clear background

      svg.draw(c);
     }
   }
  static void say(Object...O) {Say.say(O);}
 }
