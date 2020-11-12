/*------------------------------------------------------------------------------
Test SendEmail
Philip R Brenan at gmail dot com, © Appa Apps Ltd Inc 2016.06.24 21:39:25
------------------------------------------------------------------------------*/
package com.appaapps;

import android.graphics.*;
import android.view.*;

public class Activity extends android.app.Activity
 {final static String
    appName      = "horses",                                                    // The name of the app  which is also the last part of the repository name on GitHub
    packageName  = "com.appaapps.photoapp",                                     // The package name of the app
    userid       = "philiprbrenan";                                             // The userid component of the github name of the repository

  public void onCreate(android.os.Bundle save)
   {super.onCreate(save);
    final Email email = new Email(this, appName, userid);
    Email.create();
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
      c.drawColor(0xffffaaaa);                                                  // Clear background
     }
   }

  static void say(Object...O) {Say.say(O);}
 }
