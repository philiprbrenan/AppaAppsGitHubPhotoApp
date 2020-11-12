//------------------------------------------------------------------------------
// Rectangle with integer coordinates
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------
package android.graphics;

public class Rect
 {public int bottom;
  public int left;
  public int right;
  public int top;
  public Rect() {}
  public Rect(int left, int top, int right, int bottom)
   {this.left = left; this.top = top; this.right = right; this.bottom = bottom;
   }
  public Rect(Rect r) {}

  public int centerX() {return 0;}
  public int centerY() {return 0;}
  public boolean   contains(int left, int top, int right, int bottom) {return true;}

public boolean   contains(Rect r) {return true;}
public boolean   contains(int x, int y) {return true;}
public int   describeContents()    {return 0;}
public boolean   equals(Object o)  {return true;}
public int   hashCode()            {return 0;}
public int   height()              {return 0;}
public void  inset(int dx, int dy)  {}
public boolean   intersect(Rect r)       {return true;}
public boolean   intersect(int left, int top, int right, int bottom){return true;}
public boolean   intersects(int left, int top, int right, int bottom) {return true;}
public static boolean  intersects(Rect a, Rect b)  {return true;}
public boolean   isEmpty()    {return true;}
public void  offset(int dx, int dy) {}
public void  offsetTo(int newLeft, int newTop) {}
// public void  readFromParcel(Parcel in)  {}
// public void  round(Rect dst)    {}
// public void  roundOut(Rect dst){}
public void  set(Rect src)  {}
// public void  set(Rect src) {}
public void  set(int left, int top, int right, int bottom){}
public void  setEmpty()  {}
public boolean   setIntersect(Rect a, Rect b)  {return true;}
public void  sort() {}
public String  toShortString() {return "";}
public String  toString() {return "";}
public void  union(Rect r) {}
public void  union(int left, int top, int right, int bottom){}
public void  union(int x, int y) {}
public int   width() {return 0;}
// public void  writeToParcel(Parcel out, int flags)

   public static void main(String[] args)
   {System.err.println("Hello World");
   }
 }
