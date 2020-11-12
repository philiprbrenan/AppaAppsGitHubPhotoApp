#!/usr/bin/perl -I/home/phil/perl/cpan/DataTableText/lib/
#-------------------------------------------------------------------------------
# Make test Android classes
# Philip R Brenan at gmail dot com, Appa Apps Ltd Inc., 2020
#-------------------------------------------------------------------------------
use warnings FATAL => qw(all);
use strict;
use Carp;
use Data::Dump qw(dump);
use Data::Table::Text qw(:all);
use feature qw(say current_sub);

sub make($)                                                                     # Make a file in isolation
 {my ($file) = @_;                                                              # File to make
  say STDERR $file;
  say STDERR qx(makeWithPerl --java --compile "$file.java");
 }

my @files = qw(
android/BitmapDrawable
android/BitmapFactory
android/Bitmap
android/Canvas
android/Context
android/Matrix
android/MediaDataSource
android/MediaPlayer
android/Paint
android/Pair
android/Path
android/Point
android/PointF
android/RectF
android/Rect
android/SystemClock
themes/Themes
midi/MidiTracks
sound/Midi
sound/Speech
say/Say
time/Time
svg/Svg
);

make($_) for @files;
__DATA__
appState/AppState
choices/Choices
coloursTransformed/ColoursTransformed
congratulations/Congratulations
downloadAndUnzip/DownloadAndUnzip
download/Download
email/Email
filter/Filter
flags/Flags
fourier/Fourier
log/Log
maths/Maths
midi/MidiTracks
orderedStack/OrderedStack
photoBytes/PhotoBytes
photoBytes/PhotoBytesJP
photoBytes/PhotoBytesJpx
prompts/Prompts
randomChoice/RandomChoice
rightWrongTracker/RightWrongTracker
save/Save
sha256/Sha256
sinkSort/SinkSort
sound/Midi
sound/Speech
translations/TextTranslations
translations/Translations
unpackappdescription/Unpackappdescription
unzip/Unzip
upload/GitHubUploadStream
upload/UploadStream
