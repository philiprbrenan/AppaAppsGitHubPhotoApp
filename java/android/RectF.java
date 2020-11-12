//------------------------------------------------------------------------------
// Rectangle with floating point coordinates
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------
package android.graphics;

public class RectF
 {public float bottom;
  public float left;
  public float right;
  public float top;
  public RectF() {}
  public RectF(float left, float top, float right, float bottom)
   {this.left = left; this.top = top; this.right = right; this.bottom = bottom;
   }
  public RectF(RectF r) {}

  public float centerX() {return 0f;}
  public float centerY() {return 0f;}
  public boolean   contains(float left, float top, float right, float bottom) {return true;}

public boolean   contains(RectF r) {return true;}
public boolean   contains(float x, float y) {return true;}
public int       describeContents()    {return 0;}
public boolean   equals(Object o)  {return true;}
public int       hashCode()            {return 0;}
public float     height()            {return bottom -top;}
public void      inset(float dx, float dy)  {}
public boolean   intersect(RectF r)       {return true;}
public boolean   intersect(float left, float top, float right, float bottom){return true;}
public boolean   intersects(float left, float top, float right, float bottom) {return true;}
public static boolean  intersects(RectF a, RectF b)  {return true;}
public boolean   isEmpty()    {return true;}
public void      offset(float dx, float dy) {}
public void      offsetTo(float newLeft, float newTop) {}
// public void  readFromParcel(Parcel in)  {}
// public void  round(Rect dst)    {}
// public void  roundOut(Rect dst){}
public void  set(RectF src)
 {this.left   = src.left;
  this.right  = src.right;
  this.top    = src.top;
  this.bottom = src.bottom;
 }
// public void  set(Rect src) {}
public void  set(float left, float top, float right, float bottom){}
public void  setEmpty()  {}
public boolean   setIntersect(RectF a, RectF b)  {return true;}
public void  sort() {}
public String  toShortString() {return "";}
public String  toString() {return "("+left+","+top+","+right+","+bottom+")";}
public void  union(RectF r) {}
public void  union(float left, float top, float right, float bottom){}
public void  union(float x, float y) {}
public float   width() {return right-left;}
// public void  writeToParcel(Parcel out, int flags)

   public static void main(String[] args)
   {System.err.println("Hello World");
   }
 }
