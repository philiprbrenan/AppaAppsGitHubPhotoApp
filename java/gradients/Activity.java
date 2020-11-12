/*------------------------------------------------------------------------------
Test Paint gradients
Philip R Brenan at gmail dot com, © Appa Apps Ltd Inc 2016.06.24 21:39:25
------------------------------------------------------------------------------*/
package com.appaapps;

import android.graphics.*;
import android.graphics.Bitmap.*;
import android.view.*;

public class Activity extends android.app.Activity
 {Gradients.Tartan g = new Gradients().tartan();

  public void onCreate(android.os.Bundle save)
   {super.onCreate(save);
    setContentView(new Display());
   }

  class Display extends android.view.View
   {Display()                                                                   // Create display
     {super(Activity.this);
     }

    public void onDraw(Canvas c)                                                // Draw
     {final float w = c.getWidth(), h = c.getHeight();
      final Paint p = new Paint(),  q = new Paint();
      final int delta = 100;
      g.set(c, p, q, w, h);

      postInvalidateDelayed(50);                                                // Keep the redraw cycle going
      c.drawColor(0x00000000);                                                  // Clear background

      c.drawRect(0, 0, w, h, p);
      c.drawRect(0, 0, w, h, q);
     }
   }

  static void say(Object...O) {Say.say(O);}
  private static double t() {return System.currentTimeMillis() / 10d;}
 }
