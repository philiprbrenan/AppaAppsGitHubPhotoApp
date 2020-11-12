#!/usr/bin/perl
#-------------------------------------------------------------------------------
# Quadrate apps by genre and popularity
# Philip R Brenan at gmail dot com, Appa Apps Ltd Inc., 2018
#-------------------------------------------------------------------------------

require v5.16;
use warnings FATAL => qw(all);
use strict;
use Carp;
use Data::Dump qw(dump);
use Data::Table::Text qw(:all);
use utf8;
use Test::More qw(no_plan);

sub App(@)                                                                      # App definition
 {package App;
  my $q = bless {promoted=>0, @_};
  return $q;
  BEGIN
   {::genLValueArrayMethods (qw(genres));                                       # Array of genres that the item is in
    ::genLValueHashMethods  (qw(genre));                                        # Hash of the genres for each item
    ::genLValueScalarMethods(qw(app));                                          # The name of the app
    ::genLValueScalarMethods(qw(icon));                                         # Icon file
    ::genLValueScalarMethods(qw(popularity));                                   # Popularity of the app  in terms of approximate number of downloads
    ::genLValueScalarMethods(qw(promoted));                                     # App has been promoted
   }
 }

sub Quadrature(@)                                                               # Choices plus apps == 4
 {package Quadrature;
  bless {@_};
  BEGIN
   {::genLValueArrayMethods(qw(apps));                                          # The apps already in the quadrature
    ::genLValueArrayMethods(qw(promoted));                                      # The apps promoted into the quadrature
    ::genLValueHashMethods (qw(choices));                                       # The choices in the quadrature
   }
 }

sub divide($$@);
sub divide($$@)                                                                 # Divide apps by the most common genres
 {my ($N, $tree, @q) = @_;                                                      # Number of elements in a quadrature, quadrature tree, list of apps to be divided
  while(@q)                                                                     # Try to quadrate the supplied apps
   {my $m = sub                                                                 # One of the most frequently occuring genres
     {my %g;
      for my $q(@q)                                                             # Each remaining app
       {$g{$_}++ for sort keys %{$q->genre};                                    # Genres this app occurs in
       }
      (sort {$b->[1] <=> $a->[1]} map {[$_=> $g{$_}]} keys %g)[0][0];           # Return a most frequently occurring remaining genre
     }->();

    my @m =  grep { $_->genre->{$m}} @q;                                        # Set of remaining items that are in the chosen genre
       @q =  grep {!$_->genre->{$m}} @q;                                        # Set of remaining items that are not in the chosen genre

    delete $_->genre->{$m} for @m;                                              # Remove the chosen genre from apps that are in the chosen genre

    if (keys %$tree >= $N)                                                      # Complain that the quadrature is not possible
     {my $s  = "Divide these apps amongst these genres:\n";
         $s .= formatTable([sort keys %$tree],      [qw(Genres)]);
         $s .= formatTable([sort map {$_->app} @q], [qw(Apps)]);
      confess $s
     }

    if (@m > $N)                                                                # Quadrate a choice that is still too big
     {divide($N, ($tree->{$m} = Quadrature)->choices, @m);
     }
    else                                                                        # Accept a quadrature
     {$tree->{$m} = Quadrature(apps=>[@m]);                                     # New quadrature
     }
   }
  $tree
 }

sub Quadrature::appsNotPromoted($)                                              # Apps found in and below the specified quadrature that have not been promoted
 {my ($quad) = @_;                                                              # Quadrature
  my $choices = $quad->choices;                                                 # Choices in this quadrature
  my $apps    = $quad->apps;                                                    # Apps in this quadrature
  my @apps;                                                                     # Apps in and under this quadrature
  for my $a(@{$quad->apps})                                                     # Apps in this quadrature
   {push @apps, $a unless $a->promoted;                                         # Apps in this quadrature that have not been promoted
   }
  for my $c(sort keys %$choices)                                                # Choices in this quadratrue
   {my $choice = $choices->{$c};                                                # Choice
    push @apps, $choice->appsNotPromoted;                                       # Apps in this choice that have not been promoted
   }
  sort {$b->popularity <=> $a->popularity} @apps;                               # Sort by popularity
 }

sub Quadrature::promote($$)                                                     # Promote apps to fill empty slots
 {my ($quad, $N) = @_;                                                          # Quadrature, number of elements in a quadrature, quadrature tree, array of items
  my $choices = $quad->choices;                                                 # Choices in this quadrature
  for my $a($quad->appsNotPromoted)                                             # Fill slots in this quadrature with apps that have not been promoted
   {next if $a->promoted;
    last if keys(%$choices) + scalar @{$quad->apps} +                           # Finished if the quad is full
                              scalar @{$quad->promoted} >= $N;
    push @{$quad->promoted}, $a;                                                # Promote an app
    $a->promoted++;
   }
  for my $c(sort keys %$choices)                                                # Fill slots below this quad
   {$choices->{$c}->promote($N);
   }
 }

sub Quadrature::cutZeroes($)                                                    # Remove choices with no remaining elements
 {my ($quad) = @_;                                                              # Quadrature
  my $choices = $quad->choices;                                                 # Choices in this quadrature
  for my $c(sort keys %$choices)
   {my $choice = $choices->{$c};                                                # Each choice
    my @a = $choice->appsNotPromoted;
    if (@a == 0 and !@{$choice->promoted})                                      # No apps in or below this choice
     {delete $choices->{$c};                                                    # Delete choice
     }
    elsif (@a)                                                                  # Try contained quads
     {$choice->cutZeroes;
     }
   }
 }

sub Quadrature::unwrapSingles($$)                                               # Unwrap choices that contain just one app
 {my ($quad, $N) = @_;                                                          # Quadrature, number of elements in a quadrature, quadrature tree, array of items
  my $choices = $quad->choices;                                                 # Choices in this quadrature
  for my $c(sort keys %$choices)
   {my $choice = $choices->{$c};                                                # Each choice in this quad
    my @a = ($choice->appsNotPromoted, @{$choice->promoted});                   # Apps in this quad
    if (@a == 1)                                                                # Just one app in this quad
     {for my $a(@a)
       {push @{$quad->promoted}, $a;                                            # Promote app
        $a->promoted++;
       }
      delete $choices->{$c};                                                    # Remove choice as it is now empty
     }
    else                                                                        # Unwrap single choices in contained  quads.
     {$choices->{$c}->unwrapSingles($N);
     }
   }
 }

sub quadrate($@)                                                                # Divide a list of apps by the most frequently occurring genres
 {my ($N, @q) = @_;                                                             # Number of elements in a quadrature, quadrature tree, list of apps
  for my $q(@q)
   {$q->genre = {map {$_=>1} @{$q->genres}};                                    # Genres left over - modified during divide()
   }
  my $tree = Quadrature(choices=>divide($N, {}, @q));                           # Divide the apps
  $tree->promote($N);                                                           # Promote most popular apps into empty slots woith the most popular going to the highest position
  $tree->cutZeroes;                                                             # Remove empty choices
  $tree->unwrapSingles($N);                                                     # Unwrap choices with just one app in them

  $tree
 }

sub Quadrature::dump($;$)                                                       # Dump the quadrature
 {my ($quad, $depth) = @_;                                                      # Quadrature, depth on tree
  $depth //= 0;                                                                 # Depth in tree
  my $choices = $quad->choices;                                                 # Choices in this quadrature
  my @out;                                                                      # Output
  for my $c(sort keys %$choices)
   {my $choice = $choices->{$c};                                                # Each choice in the quad
    my @a = $choice->appsNotPromoted;                                           # Apps not promoted
    push @out, "  " x $depth, "choice:   ", $c, " [", scalar(@a), "] ",         # Choice details
               join(" ", sort map {$_->app} @a), "\n";
    push @out, $choice->dump($depth+1);                                         # Choices in contained quads
   }
  for my $a(@{$quad->apps})                                                     # Apps in quad
   {push @out, "  " x $depth, "app:      ", $a->app, " ", $a->promoted,
               " ", $a->popularity, "\n";
   }
  for my $a(@{$quad->promoted})                                                 # Apps promoted into the quad
   {push @out, "  " x $depth, "promoted: ", $a->app, " ", $a->promoted,
               " ", $a->popularity, "\n";
   }
  @out
 }

sub Quadrature::print($;$)                                                      # Print the quadrature as the user will see it
 {my ($quad, $depth) = @_;                                                      # Quadrature, depth on tree
  $depth //= 0;                                                                 # Depth in tree
  my $choices = $quad->choices;                                                 # Choices in this quadrature
  my @out;                                                                      # Output
  for my $c(sort keys %$choices)
   {my $choice = $choices->{$c};                                                # Each choice in the quad
    my @a = $choice->appsNotPromoted;                                           # Apps not promoted
    push @out, "  " x $depth, "choice: ", $c, "\n";                             # Choice
    push @out, $choice->print($depth+1);                                        # Choices in contained quads
   }
  for my $a(@{$quad->apps}, @{$quad->promoted})                                 # Apps in quad
   {push @out, "  " x $depth, "app:    ", $a->app, "\n";
   }
  @out
 }

if (1)
 {my @q =
   (App(app=>q(1a), genres=>[qw(1 A a)], popularity=>100),
    App(app=>q(1b), genres=>[qw(1 A b)], popularity=>110),
    App(app=>q(1c), genres=>[qw(1 A c)], popularity=>121),
    App(app=>q(1d), genres=>[qw(1 B d)], popularity=>110),
    App(app=>q(1e), genres=>[qw(1 B e)], popularity=>120),
    App(app=>q(1f), genres=>[qw(1 B f)], popularity=>102),

    App(app=>q(2a), genres=>[qw(2 A a)], popularity=>121),
    App(app=>q(2b), genres=>[qw(2 A b)], popularity=>111),
    App(app=>q(2c), genres=>[qw(2 A c)], popularity=>102),
    App(app=>q(2d), genres=>[qw(2 B d)], popularity=>112),
    App(app=>q(2e), genres=>[qw(2 B e)], popularity=>110),
   );

  my $q = quadrate(4, @q);
  my $s = join '', $q->dump;
  my $t = <<END;
choice:   A [0]
  promoted: 2b 1 111
  promoted: 1b 1 110
  promoted: 1a 2 100
  promoted: 2c 2 102
choice:   B [1] 1f
  choice:   1 [1] 1f
    app:      1d 1 110
    app:      1e 1 120
    app:      1f 0 102
    promoted: 1d 1 110
  promoted: 1e 1 120
  promoted: 2d 1 112
  promoted: 2e 2 110
promoted: 1c 1 121
promoted: 2a 1 121
END

  say STDERR $q->print;
 }
