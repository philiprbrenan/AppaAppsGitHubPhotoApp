#!/usr/bin/perl -I/home/phil/z/java -I/home/phil/z/perl/cpan/AndroidBuild/lib
#-------------------------------------------------------------------------------
# Compile and run an Android app in an emulator
# Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
#-------------------------------------------------------------------------------
use MakeWithPerlLocally;
use Data::Table::Text qw(:all);

my $home = filePathDir(qw(/home phil z java));                                  # Home

MakeWithPerlLocally::build
 {$_->icon  = filePathExt($home, qw(svg icon svg));                             # Image that will be scaled to make an icon using Imagemagick

  $_->src   =                                                                   # Source files
   [fpe($home, qw(svg                Activity           java)),
    fpe($home, qw(svg                Svg                java)),
#   fpe($home, qw(svg                SvgPosition        java)),
    fpe($home, qw(coloursTransformed ColoursTransformed java)),
    fpe($home, qw(layoutText         LayoutText         java)),
    fpe($home, qw(gradients          Gradients          java)),
    fpe($home, qw(fourier            Fourier            java)),
    fpe($home, qw(log                Log                java)),
   ];

  $_->title = qq(Svg test);                                                     # Title of the app as seen under the icon
 };
