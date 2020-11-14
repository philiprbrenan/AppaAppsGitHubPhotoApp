#!/usr/bin/perl -I/home/phil/perl/cpan/AndroidBuild/lib/
#-------------------------------------------------------------------------------
# Generate an an Appa Apps Educational Android App on GitHub
# Philip R Brenan at gmail dot com, Appa Apps Ltd Inc., 2020
#-------------------------------------------------------------------------------
use warnings FATAL => qw(all);
use strict;
use Carp;
use Data::Dump qw(dump);
use Data::Table::Text qw(:all !mmm);
use Android::Build;
use feature qw(say current_sub);

lll "GenApp 3";

sub home    {$ENV{HOME}}                                                        # Home folder
sub develop {home =~ m(\A/home/phil\Z) ? 1 : 0}                                 # Developing
sub homeDir {develop ? fpd(home, q(AppaAppsGitHubPhotoApp)) : &gitHubHome}      # Working folder
sub homeJava             {fpd(homeDir, q(java))}                                # Java files
sub appActivity          {qq(Activity)}                                         # Name of Activity = $activity.java file containing onCreate() for this app
sub appDebuggable        {0}                                                    # Add debugabble to app manifest if true
sub androidSdk           {fpd(develop ? home : homeDir, q(android), q(sdk))}    # Android sdk folder
sub appBuildDir          {fpd(homeDir,     q(build))}                           # This folder is where Android::Build builds the Android code.
sub assetsDir            {fpd(appBuildDir, q(assets))}                          # Assets folder
sub buildVersion         {q(25.0.3)}                                            # Build tools version
sub buildTools           {fpd(androidSdk,  q(build-tools), buildVersion)}       # Folder containing build tools - often found in the Android sdk
sub platform             {fpd(androidSdk,  qw(platforms android-25))}           # Android platform - the folder that contains android.jar
sub platformTools        {fpd(androidSdk,  qw(platform-tools))}                 # Android platform tools - the folder that contains 𝗮𝗱𝗯
sub sdkLevels            {[15,25]}                                              # minSdkLevel, targetSdkLevel
sub aapt                 {fpf(buildTools,  q(aapt))}                            # Location of aapt so we can list an apk
sub keyAlias             {q(key)}                                               # Alias of key to be used to sign these apps
sub keyStorePwd          {q(121212)}                                            # Password for keystore
sub keyStoreDir          {fpd(homeDir,     q(keys))}                            # Key store folder
sub keyStoreFile         {fpf(keyStoreDir, q(key.keystore))}                    # Key store file
sub domainReversed       {qq(com.appaapps)}                                     # Domain name prefix in reverse order for these apps
sub audioCacheDir        {fpd(homeDir,     q(audioCache))}                      # Audio files cache to avoid regenerating fiels on Polly
sub audioAssetsFolder    {q(audio)}                                             # The audio folder in assets
sub audioAssetsDir       {fpd(assetsDir,   audioAssetsFolder)}                  # Audio files in assets
sub appPackage           {domainReversed.  q(.photoapp)}                        # App package name
sub jpxTileSize          {256}                                                  # Size of jpx tiles
sub jpxTilesMax          {4}                                                    # Maximum number of jpx tiles in either direction. Larger images are scaled down first to meet this requirement.  This prevents the creation of huge apps that crash when played.
sub maxImageSize         {1024}                                                 # Maximum size of normal images - otherwise Android runs out of memory
sub maxImageSizeGH       {1024*1024}                                            # Maximum size of an image on GitHub via GitHub::Crud.pm
sub minimumImageFileSize {1e3}                                                  # Minimum size of an image file - suspect that something has gone wrong if it is smaller then this number of bytes
sub minimumSoundFileSize {1e3}                                                  # Minimum size of a sound file - suspect that something has gone wrong if it is smaller then this number of bytes
sub imagesFolder         {q(images)}                                            # Images folder in assets
sub imagesApp            {fpd(assetsDir,   imagesFolder)}                       # Images in build
sub midiSourceDir        {fpd(homeDir,     q(midi))}                            # Music
sub midiAssetsDir        {fpd(assetsDir,   q(midi))}                            # Music in assets
sub imagesLocal          {fpd(homeDir,     q(images))}                          # Images supplied by user
sub sourceJava           {fpd(homeDir,     q(java))}                            # Source java folder
sub sourceActivity       {fpe(sourceJava,  qw(Activity java))}                  # Source java - Activity source file
sub sourceJavaFiles      {map{fpe(sourceJava, @$_, q(java))}                    # Source java files
  [qw(LoadAppDescription                       )],
  [qw(appDescription       AppDescription      )],
  [qw(assets               Assets              )],
  [qw(appState             AppState            )],
  [qw(choices              Choices             )],
  [qw(coloursTransformed   ColoursTransformed  )],
  [qw(congratulations      Congratulations     )],
  [qw(email                Email               )],
  [qw(filter               Filter              )],
  [qw(fourier              Fourier             )],
  [qw(log                  Log                 )],
  [qw(maths                Maths               )],
  [qw(midi                 MidiTracks          )],
  [qw(orderedStack         OrderedStack        )],
  [qw(photoBytes           PhotoBytes          )],
  [qw(photoBytes           PhotoBytesJP        )],
  [qw(photoBytes           PhotoBytesJpx       )],
# [qw(prompts              Prompts             )],
  [qw(randomChoice         RandomChoice        )],
  [qw(rightWrongTracker    RightWrongTracker   )],
  [qw(save                 Save                )],
  [qw(say                  Say                 )],
  [qw(sha256               Sha256              )],
  [qw(sinkSort             SinkSort            )],
  [qw(sound                Midi                )],
  [qw(sound                Speech              )],
  [qw(svg                  Svg                 )],
  [qw(themes               Themes              )],
  [qw(time                 Time                )],
 }

sub appPermissions {qw(INTERNET ACCESS_NETWORK_STATE WRITE_EXTERNAL_STORAGE)}   # App permissions - WES only required for earlier androids

sub icon                                                                        # Icon
 {my $i = fpf(imagesLocal, qw(icon));
  my $j = fpe($i, q(jpg)); return $j if -e $j;
  my $p = fpe($i, q(png)); return $p if -e $p;
  undef
 }

sub githubRepo                                                                  # Icon
 {return "AppaAppsGitHubPhotoApp" if develop;
  my $s = $ENV{GITHUB_REPOSITORY};
  (split m(/), $s)[-1];
 }

sub gitHubHome                                                                  # Github home
 {my $d = githubRepo;
  fpd(q(/home/runner/work), $d, $d)
  }

sub githubUserDotRepo                                                           # Icon
 {return "AppaAppsGitHub.PhotoApp" if develop;
  $ENV{GITHUB_REPOSITORY} =~ s(/) (.)gsr
 }

sub imageFiles(@) {grep {!m(/build/)} grep {m(images/.*\.(jpg|png)\Z)} @_}      # Image files

sub squeezeFileName($)                                                          # Remove weird characters from a file name
 {my ($file) = @_;                                                              # File name
  confess unless $file;
  $file =~ s(\s) ()gsr =~ s([^a-zA-Z0-9/._-]) ()gsr;
 }

my @messages;                                                                   # Messages
sub mmm(@)                                                                      # Log messages with a time stamp and originating file and line number.
 {my (@m) = @_;                                                                 # Text
  my $m = join "\n", @_;
  say STDERR $m;
  push @m, $m;
 }

sub eee(@)                                                                      # Log messages with a time stamp and originating file and line number.
 {my (@e) = @_;                                                                 # Text
  my $e = join "\n", @_;
  confess $e;   # Raise an issue here as well
 }

sub convertImages(@)                                                            # Convert images to jpx
 {lll "Convert images";
  for my $source(@_)
   {next  if $source =~ m(/icon\.);                                             # Icon does not get converted to jpx
    next if -e fpe(imagesApp, fn($source), qw(jpx data));
    my $target = swapFilePrefix $source, imagesLocal, imagesApp;
    my $t      = setFileExtension $target;
    convertImageToJpx($source, $t, jpxTileSize, jpxTilesMax);                   # Convert image to jpx
   }
 }

sub copyMusic()                                                                 # Copy midi to assets
 {lll "Copy music";
  makePath(midiAssetsDir);
  copyFolder(midiSourceDir, midiAssetsDir);
 }

sub createSpeechFile($)                                                         # Create a speech file and return its name
 {my ($text) = @_;                                                              # Text to speak
  my $a = fpe(audioCacheDir, $text, q(mp3));                                    # The file in the cache

  my $say = $text =~ s(\W)  ( )gr;                                              # Replace punctuation with space in speech
     $say =~ s(\s+) ( )gs;                                                      # Replace spaces with space
     $say = convertUnicodeToXml $say;                                           # Replace unicode points that are not ascii with an xml representation of such characters so that they can be understood by Polly

  makePath($a);
  my $c = <<END;                                                                # Speech request - with emphasis
set AWS_ACCESS_KEY_ID=\$AWSPolly_ACCESS_KEY_ID;
set AWS_SECRET_ACCESS_KEY=\$AWSPolly_SECRET_ACCESS_KEY;
/usr/local/bin/aws polly synthesize-speech --text-type ssml
  --text
    "<speak>
        <prosody>$say</prosody>
     </speak>"
  --output-format mp3
  --voice-id Amy "$a"
  --region eu-west-1
END
  $c =~ s/\n/ /gs;                                                              # Put Polly command all on one line
  return $a if -e $a;                                                           # Create audio file in cache if necessary

  lll "Polly: $text to $a";
  my $r = qx($c 2>&1);                                                          # Execute Polly with credentials
  my $R = [$?, $@, $!, $$];
  if (!$r)                                                                      # No response
   {eee "No response from AWS Polly\n".dump($R);
   }
  elsif ($r =~ /You must specify a region/)                                     # Complain about the region
   {eee "Tell the developer to specify a region for AWS Polly\n$r\n";
   }
  elsif (!-e $a)                                                                # Confirm speech file generated
   {eee "Failed to generate audio file\n$a\n$r\nusing command:\n$c\n";
   }
  $a
 }

sub copySpeech()                                                                # Copy speech to assets
 {lll "Copy speech";
  copyFolder(audioCacheDir, audioAssetsDir);
 }

sub imageFile($)                                                                # Test whether a string could be an image file name
 {my ($string) = @_;                                                            # Test whether a string could be an image file
  $string =~ m(jpg|png)i
 }

sub getImageSize($)                                                             # Cache image sizes to speed up compile
 {my ($image) = @_;                                                             # Image file name
  my $j = fpe(imagesApp, fn($image), qw(jpx data));
  if (-e $j)
   {my $s = readFile($j);
    if ($s =~ m(width\s*(\d+).*height\s*(\d+))s)
     {return ($1, $2)
     }
   }
  imageSize($image)
 }

sub  genAppDescription(@)                                                       # Generate java describing the app from the text and photos
 {my (@files) = @_;                                                             # Files - with images and text amongst them
  lll "Generate App Description";
  my @t = grep {fe($_) eq q(txt)} @files;                                       # Text files
  my @i = grep {imageFile $_}     @files;                                       # Image files
  my @j;                                                                        # Generated java

  my ($t) = @t;                                                                 # Text file containing app description
  if (!$t)                                                                      # No text file
   {eee("Need a .txt file containing a description of the app");
   }
  if (@t  > 1)                                                                  # Too many text files
   {my (undef, @f) = @t;
    my $f = join "\n  ", @f;
     mmm(<<END)
Several .txt files found which might contain a description of the app.

Choosing the first .txt file:
  $t

Ignoring these .txt files:
  $f
END
   }
  if (@i < 2)                                                                   # Not enough images
   {eee(<<END)
Need at least two .png or .jpg files to build an app.

Please add some images to your repository.
END
   }

  my %i;                                                                        # Images by squeezed base name
  for my $i(@i)
   {my $b = fn $i;
    $i{$b} = $i;
   }

  my @source = readFile($t);                                                    # Parse text file into images and facts

  my sub lineType($$)                                                           # Return the current line if it matches the type of line requested
   {my ($line, $types) = @_;                                                    # Line number, types
    my $s = $source[$line];                                                     # Input line
       $s = $s =~ s(#\.*\Z) ()gr;                                               # Remove comments

    my $b = $s !~ m(\S);                                                        # Blank lines
    my $i = $s =~ m(\.(png|jpg)\s*\Z);                                          # Image
    my $t = !$b && !$i;                                                         # Non blank text which is not an image

    if ($i)                                                                     # Image name check
     {$s = nws($s);
      if ($s =~ m([^a-zA-Z0-9 .+-_/]))
       {eee(<<END)
The following image file contains characters that are not in a-zA-Z0-0.+_- or spaces.
  $s
Please remove the extraneous characters from the file name
END
       }
     }
    if ($t)                                                                     # Text check
     {$s = nws($s);
      if ($s =~ m([^a-zA-Z ]))
       {eee(<<END)
The following fact contains text that are not in a-z or spaces.
  $s
Please remove the extraneous characters from the fact
END
       }
     }

    return $s if $types =~ m(blank) and $b;
    return $s if $types =~ m(image) and $i;
    return $s if $types =~ m(text)  and $t;
    undef;
   }

  for my $line(keys @source)                                                    # Each source line
   {if (my $s = lineType($line, q(image)))                                      # Look for the next image
     {my $b = fn $s;
      my $i = $i{$b};                                                           # Image file
      my $L = $line + 1;

      if (!$i)
       {eee(<<END);
On line $L of file:

 $t

you mention an image file:

 $s

But I cannot find this file anywhere in your repository.
END
       }

      my $j = $L;
      for my $n($j..@source-1)                                                  # Skip to next block of text
       {$j = $n;
        last if lineType($j, q(text));
       }

      my @f;                                                                    # Facts associated with this image
      for my $n($j..@source-1)
       {$j = $n;
        my $s = lineType($j, q(image));
        last if $s;
        my $t = lineType($j, q(text));
        push @f, $t if $t;
       }

      if (1)
       {my $b = fn $i;
        my $t = $f[0] // '';
        my $a = imagesFolder;
        my ($w, $h) = getImageSize($i);
        push @j, <<END;                                                         # Photo java
 {AppDescription.Photo p = d.new Photo();
  p.name  = "$a/$b"; p.width = $w; p.height = $h;
  p.title = "$t";
END
       }
      for my $f(@f)                                                             # Create speech for facts
       {my $F = fne createSpeechFile($f);
        my $a = audioAssetsFolder;
        push @j, <<END;
   {AppDescription.Fact f = d.new Fact();
    f.name = "$a/$F"; f.title = "$f";
    d.new PhotoFact(p, f);
   }
END
       }
      push @j, <<END;
 }
END
     }
   }

  my $repo = githubRepo;
  unshift @j, <<END;                                                            # Program header
package com.appaapps;
public class LoadAppDescription                                                 // Create app description
 {public AppDescription load()
   {AppDescription d = new AppDescription();
    AppDescription.App a = d.new App();
    a.name  = "$repo";
    a.title = "$repo";
END
  push @j, <<END;                                                               # Program trailer
    return d;
   }
  public static void main(String[] args)
   {System.out.println(new LoadAppDescription().load());
    System.out.println("Hello World\\n");
   }
 }
END

  owf(fpe(homeJava, qw(LoadAppDescription java)), join "\n", @j);
 }

sub createKey                                                                   # Create a key for the app
 {my $k = keyStoreFile;
  return if -e $k;
  makePath($k);
  my $c = qq(keytool -genkey -v -keystore $k -keyalg RSA -keysize 2048 -validity 10000 -storepass 121212 -alias key -dname 'CN=a, OU=a, O=a, L=a, ST=a, C=a');
  lll qx($c);
 }

sub compileApp                                                                  # Compile the app
 {lll "Compile App";
  my $a = &Android::Build::new();                                               # Android build details
  $a->activity       = appActivity;                                             # Name of Activity = $activity.java file containing onCreate() for this app
  $a->buildFolder    = appBuildDir;                                             # This folder is removed after the Android build so we cannot use the same build area as AppaAppsPhotoApp

  $a->buildTools     = buildTools;                                              # Build tools folder
  $a->debug          = appDebuggable;                                           # Whether the app is debuggable or not
# $a->device         = device;                                                  # Device to install on
  $a->keyAlias       = keyAlias;                                                # Alias of key to be used to sign this app
  $a->keyStoreFile   = keyStoreFile;                                            # Keystore location
  $a->keyStorePwd    = keyStorePwd;                                             # Password for keystore
  $a->icon           = icon;                                                    # Image that will be scaled to make an icon using Imagemagick
# $a->libs           = $appLibs;                                                # Library files to be copied into app
  $a->package        = githubUserDotRepo;                                       # Package name for activity to be started
  $a->platform       = platform;                                                # Android platform - the folder that contains android.jar
  $a->platformTools  = platformTools;                                           # Android platform tools - the folder that contains adb
 #$a->parameters     = $params;                                                 # Parameters: user app name, download url for app content
  $a->sdkLevels      = sdkLevels;                                               # Min sdk, target sdk for manifest
  $a->src            = [sourceJavaFiles];                                       # Source files to be copied into app as additional classes
  $a->title          = githubRepo;                                              # Title of the app as seen under the icon
# $a->version        = (versionCode =~ s(-.+\Z) ()r);                           # Version of the app with possible adjustment
  $a->permissions    = [appPermissions];                                        # Add permissions and remove storage

# $a->assets = {q(guid.data)=>(q(1)x32)} if $develop;                           # Add a guid so we can test linkage

  if (1)                                                                        # Edit package name of activity
   {my $android = $a;                                                           # Builder
    my $a = $android->activity;                                                 # Activity class name
    my $s = readFile(sourceActivity);                                           # Java Activity source file as string
    my $p = $android->package;                                                  # Package name
    $s =~ s(package\s+(\w|\.)+\s*;) (package $p;)gs;                            # Update package name
    my $P = javaPackageAsFileName($s);                                          # Target file name for activity
    my $t = fpe($android->getGenFolder, $P, $a, qw(java));                      # Target file
    unlink $t;
    writeFile($t, $s);                                                          # Write activity source with the correct package name edited into place into the gen folder where it will be picked up automatically by Android::Build
  }

  my $apk = $a->apk;                                                            # Apk file produced by compile
  unlink $apk;                                                                  # Remove apk so that we can check that something got built
  develop ? $a->run : $a->compile;                                              # Compile or run

  if (-e $apk)                                                                  # Apk file produced by build
   {lll "Success";
   }
  else
   {my $b = $a->buildFolder;
    confess join '', "Unable to create apk file $apk in build folder:\n$b\n",
      @{$a->log}, "\n";
   }
 } # compileApp

sub removeBadFilesFromAssets                                                    # Remove known bad files that creep into the works
 {my @files = searchDirectoryTreesForMatchingFiles(assetsDir);
  for my $f(@files)
   {if ($f =~ m(/\.directory\Z))
     {#lll "Deleting bad file: $f";
      unlink $f;
     }
   }
 }

sub buildApp                                                                    # Build an app
 {makePath(appBuildDir); #clearFolder(appBuildDir, 999);                  # Clear build folder

  my @files = searchDirectoryTreesForMatchingFiles(homeDir);                    # Files available

  genAppDescription(@files);

  convertImages(imageFiles @files);
  createKey;
  copyMusic;
  copySpeech;
  removeBadFilesFromAssets;
  compileApp;
 }

buildApp;
