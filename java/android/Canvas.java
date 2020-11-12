package android.graphics;
public class Canvas
 {public boolean isRecordingFor(Object o) { return false; }
  private Bitmap mBitmap;
  private int mSurfaceFormat;
  public static final int DIRECTION_LTR = 0;
  public static final int DIRECTION_RTL = 1;
  private static final int MAXMIMUM_BITMAP_SIZE = 32766;
  public Canvas() {}
  public Canvas(Bitmap bitmap) {}
  public Canvas(long nativeCanvas) {}
  public boolean isHardwareAccelerated() {return false;}
  public void setBitmap(Bitmap bitmap) {mBitmap = bitmap;}
  private void setNativeBitmap(long bitmapHandle) {}
  public void setViewport(int width, int height) {}
  public void setHighContrastText(boolean highContrastText) {}
  public void insertReorderBarrier() {}
  public void insertInorderBarrier() {}
  public boolean isOpaque() {return  false;}
  public int getWidth() {return 100;}
  public int getHeight() {return 100;}
  public int getDensity() {return 1;}
  public void setDensity(int density) {}
  public void setScreenDensity(int density) {}
  public int getMaximumBitmapWidth() { return 1;}
  public void drawARGB(int a, int r, int g, int b) {}
  public void drawColor(int color) {}
  public void drawPaint(Paint paint) {}
  public void drawPoints(float[] pts, int offset, int count, Paint paint) {}
  public void drawPoints(float[] pts, Paint paint) {}
  public void drawPoint(float x, float y, Paint paint) {}
  public void drawLine(float startX, float startY, float stopX, float stopY, Paint paint) {}
  public void drawLines(float[] pts, int offset, int count, Paint paint) {}
  public void drawLines(float[] pts, Paint paint) {}
  public void drawRect(RectF rect, Paint paint) {}
  public void drawRect(float left, float top, float right, float bottom, Paint paint) {}
  public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {}
  public void drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint) {}
  public void drawText(String a,int b ,int c,Paint d) {}
  public void drawText(String a,float b , float c,Paint d) {}
  public void rotate(float a) {}
  public void drawPath(Path a,Paint b) {}
  public void save() {}
  public void restore() {}
  public void clipRect(RectF a) {}
  public void translate(float a,float b) {}
  public void scale(float a,float b)  {}
  public void scale(float a,float b, float c, float d)  {}
  public void drawCircle(float a,float b,float c,Paint p)  {}

  public static void main(String[] args)
   {System.err.println("Hello World");
   }

 }
