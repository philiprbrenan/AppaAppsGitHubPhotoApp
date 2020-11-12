#!/usr/bin/perl
#-------------------------------------------------------------------------------
# Document java
# Philip R Brenan at gmail dot com, Appa Apps Ltd Inc., 2017
#-------------------------------------------------------------------------------

require v5.16;
use warnings FATAL => qw(all);
use strict;
use Data::Dump qw(dump);
use Data::Table::Text qw(:all);

xxx("makeWithPerl --doc *.java");
