package android.graphics;

public class Matrix
 {public void set(Matrix src) {}
  public void reset() {}
  public void setTranslate(float dx, float dy) {}
  public void setScale(float sx, float sy, float px, float py) {}
  public void setScale(float sx, float sy) {}
  public void setRotate(float degrees, float px, float py) {}
  public void setRotate(float degrees) {}
  public void setSinCos(float sinValue, float cosValue, float px, float py) {}
  public void setSinCos(float sinValue, float cosValue) {}
  public void setSkew(float kx, float ky, float px, float py) {}
  public void setSkew(float kx, float ky) {}
  public boolean setConcat(Matrix a, Matrix b) {return false;}
  public boolean preTranslate(float dx, float dy) {return false;}
  public boolean preScale(float sx, float sy, float px, float py) {return false;}
  public boolean preScale(float sx, float sy) {return false;}
  public boolean preRotate(float degrees, float px, float py) {return false;}
  public boolean preRotate(float degrees) {return false;}
  public boolean preSkew(float kx, float ky, float px, float py) {return false;}
  public boolean preSkew(float kx, float ky) {return false;}
  public boolean preConcat(Matrix other) {return false;}
  public boolean postTranslate(float dx, float dy) {return false;}
  public boolean postScale(float sx, float sy, float px, float py) {return false;}
  public boolean postScale(float sx, float sy) {return false;}
  public boolean postRotate(float degrees, float px, float py) {return false;}
  public boolean postRotate(float degrees) {return false;}
  public boolean postSkew(float kx, float ky, float px, float py) {return false;}
  public boolean postSkew(float kx, float ky) {return false;}
  public boolean postConcat(Matrix other) {return false;}
// public boolean setRectToRect(RectF src, RectF dst, ScaleToFit stf) {return false;}
  public boolean setPolyToPoly(float[] src, int srcIndex, float[] dst, int dstIndex,
          int pointCount) {return false;}

  public void setValues(float[] values) {}
 }
