//------------------------------------------------------------------------------
// Android Media MediaPlayer
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
//------------------------------------------------------------------------------
package android.media;
import java.io.*;

public class MediaPlayer
 {public void setDataSource (MediaDataSource dataSource) {}
  public void setDataSource (String file) {}
  public void setDataSource (FileDescriptor fd) {}
  public void setVolume     (float a, float b) {}
  public boolean isPlaying() {return true;}
  public void prepare() {}
  public void start()   {}
  public void stop()    {}
  public void release() {}
  public void reset()   {}
  public int  getDuration() {return 0;}
  public void setOnCompletionListener(OnCompletionListener l) {}

  public static class OnCompletionListener {}
  public static void main(String[] args)
   {System.err.println("MediaPlayer");
   }
 }
