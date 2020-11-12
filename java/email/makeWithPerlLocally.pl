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
   [filePathExt($home, qw(email Activity  java)),
    filePathExt($home, qw(email Email     java)),
   ];

  $_->title = qq(SendEmail);                                                    # Title of the app as seen under the icon

  push @{$_->permissions}, "READ_CONTACTS";                                     # Additional permissions
 };
