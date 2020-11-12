package android.graphics;
public class Paint
  {public void setAlpha(int a) {}
   public void setStyle(int a) {}
   public void setColor(int a) {}
   public void setDither(boolean a) {}
   public void setStrokeWidth(float w) {}
   public float measureText(String text) {return 0f;}
   public void setTextSize(float w) {}
   public void set(Paint p) {}
   public class Style
    {public static final int FILL = 0, FILL_AND_STROKE = 1, STROKE = 2;
    }

   public void setAntiAlias(boolean a) {}
   public void setShader(LinearGradient a) {}
   public void setShader(BitmapShader a) {}
   public Path getTextPath(String a,int b,int c,int d,int e,Path p) {return null;}
   public int getColor() {return  0;}
   public float descent() {return 0f;}
;

  public static void main(String[] args)
   {System.err.println("Hello World");
   }
 }
