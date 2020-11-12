#!/usr/bin/perl -I/home/phil/z/java -I/home/phil/z/perl/cpan/AndroidBuild/lib
#-------------------------------------------------------------------------------
# Compile and run an Android app in an emulator
# Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
#-------------------------------------------------------------------------------
use MakeWithPerlLocally;
use Data::Table::Text qw(:all);

my $home = filePathDir(qw(/home phil z java));                                  # Home

MakeWithPerlLocally::build
 {$_->icon  = filePathExt($home, qw(gradients icon svg));                             # Image that will be scaled to make an icon using Imagemagick

  $_->src   =                                                                   # Source files
   [filePathExt($home, qw(gradients Activity java)),
    filePathExt($home, qw(coloursTransformed ColoursTransformed java)),
    filePathExt($home, qw(fourier Fourier java)),
    filePathExt($home, qw(gradients Gradients java)),
   ];

  $_->title = qq(Gradients test);                                               # Title of the app as seen under the icon
 };
