//------------------------------------------------------------------------------
// Android Graphics Point
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017, Apache Licence
//------------------------------------------------------------------------------
package android.graphics;

public class Point
 {public int x, y;
  public Point()             {}
  public Point(int x, int y) {this.x = x;     this.y = y;}
  public Point(Point src)    {this.x = src.x; this.y = src.y;}
  public int     describeContents()     {return 0;}
  public boolean equals(int x, int y)   {return this.x == x && this.y == y;}
  public boolean equals(Object o)       {return o == this;}
  public int     hashCode()             {return 0;}
  public void    negate()               {x = -x; y = -y;}
  public void    offset(int dx, int dy) {x += dx; y += dy;}
  //void         readFromParcel(Parcel in)
  public void    set(int x, int y)      {this.x = x;     this.y = y;}
  public String  toString()             {return "("+x+","+y+")";}
  //void         writeToParcel(Parcel out, int flags)
  public static void main(String[] args)
   {final Point p = new Point(1,2), q = new Point(p);
    q.offset(p.x, p.y);
    assert(2*p.x == q.x && 2*p.y == q.y);
   }
 }
