//------------------------------------------------------------------------------
// Context
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------
package android.content;
import java.io.InputStream;

public class Context
 {public class FileList
   {public String [] list(String folder) {return new String[0];}
    public InputStream open(String folder) {return null;}
   }
  public FileList getAssets() {return new FileList();}

  public static void main(String[] args)
   {System.err.println("Hello World");
   }
 }
