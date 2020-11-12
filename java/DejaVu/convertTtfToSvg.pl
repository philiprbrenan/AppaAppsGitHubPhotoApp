#!/usr/bin/perl
#-------------------------------------------------------------------------------
# Convert DejaVu ttf to Android jar - copied from vocabulary/../genApp.pm
# Philip R Brenan at gmail dot com, Appa Apps Ltd, 2017
#-------------------------------------------------------------------------------

require v5.16;
use warnings FATAL => qw(all);
use strict;
use Carp;
use Data::Dump qw(dump);
use Data::Table::Text qw(:all);
use File::Copy;

=pod

Download latest definition of DejaVu from: http://sourceforge.net/projects/dejavu/files/dejavu/
sudo apt-get install libbatik-java                                              # Install ttf to svg

The code below uses Mono Bold which is not kerned via HKERN

=cut

# Settings
my $font       = "DejaVuSansMonoBold";                                          # Font name
my $home       = filePathDir(qw(/home phil z java DejaVu));                     # Home
my $fontTtf    = filePathExt($home, $font, qq(ttf));                            # Ttf file name
my $fontSvg    = filePathExt($home, $font, qq(svg));                            # File containing font in svg format
my $fontJava   = filePathExt($home, $font, qq(java));                           # Output file - java
my $classes    = filePathDir(qw(/home phil z java Classes));                    # Java classes
my $androidJar = filePathExt                                                    # Android jar
 (qw(/home phil Android sdk platforms android-25 android jar));
my $jar        = filePathExt($home, qw(libs), $font, qw(jar));                  # Java Jar file
my $package    = qq(com.appaapps);                                              # Package

# Methods
sub translateUnicodeName($)                                                     # Translate a unicode name in format: &#xa4; to java
 {my ($name) = @_;

  my $translate =                                                               # Transliterations
   {'&lt;'  =>'<', '&gt;'=>'>',
    '&quot;'=>'"',
    '\\'    =>'\\\\',
    '&amp;' =>'&',
    '&apos;'=>'\\\'',
   };
  my $q = '\'';
  my $t = $translate->{$name}; return "$q$t$q" if $t;                           # Explicit translation

  if ($name =~ /\A\&\#x([[:xdigit:]]+)/i)                                       # Match unicode hexadecimal point name
   {return dump(hex($1));                                                       # Java format
   }

  "$q$name$q"                                                                   # Use as is - the character is the same as the name
 }

sub translateGlyphName($)                                                       # Translate a unicode name in format: &#xa4; to java
 {my ($name) = @_;
  my $translate =                                                               # Explicit translation
   {"integral\'"  =>"integral",
    ""            =>"emptyString"
   };

  $translate->{$name} // $name =~ s/\.//gr;
 }

sub fontAdvance($)                                                              # Translate a unicode name in format: &#xa4; to java
 {my ($fontDef) = @_;                                                           # Svg source defining the font
  if ($fontDef =~ /<font[^>]+horiz-adv-x="(\d+)"/) {return $1}                  # Global font advance
  0                                                                             # No global font advance - presumably because it is not a mono font
 }

# Convert
xxx "ttf2svg $fontTtf  -l 0 -h 999999 >$fontSvg";                               # Convert ttf to svg


my $fontDef = readFile($fontSvg) =~ s/\R/ /rg;                                  # Read font definition from svg file, convert to a single line and parse

my $fontAdvance = fontAdvance($fontDef);                                        # Get font advance if present

my @Kern = $fontDef =~ m/(<hkern.+?\/>)/gi;                                     # Separate into kerns
my @Glyphs = grep {!/arabic-form=/} $fontDef =~ m/(<glyph.+?\/>)/gi;            # Separate into glyphs
say STDERR scalar(@Glyphs), " $font symbol definitions found";

my %Symbol;                                                                     # Symbol name to character code translation
my @NumberToGlyph;                                                              # Number to glyph
my %NumberToGlyph;                                                              # Glyph character to number

if (1)
 {my $N = 0;
  for(@Glyphs)                                                                  # Gather symbol to character code translation
   {my @p = my ($name, $symbol) =                                               # Keywords for each glyph
      /<glyph\s*unicode=   \s*\"(.+?)\"
             \s*glyph-name=\s*\"(.*?)\"
      /xgi;
    if (grep{!defined} @p)                                                      # All keywords found check
     {carp "Not all keywords present for DejaVu glyph ".
           "$_ during gather symbol names";
      next;
     }
    my $s = $Symbol{$symbol} = translateUnicodeName($name);                     # Translate name
    $NumberToGlyph{$s} = @NumberToGlyph;                                        # Assign a consecutive number to each glyph
    push @NumberToGlyph, $s;                                                    # Number to glyph
   }
 }

my %Kern;                                                                       # Advance for each kern pair
for(@Kern)                                                                      # Kern pairs
 {my @p = my ($n1, $n2, $k) =                                                   # Keywords for hkern command
    /<hkern\s+g1="(.+?)"\s+g2="(.*?)"\s+k="([+-]?\d+?)"\s*\/>/gi;
  if (grep{!defined} @p)                                                        # All keywords found check
   {carp "Not all keywords present for DejaVu hkern $_";
    next;
   }
  my $N1 = $Symbol{$n1};                                                        # Character code for first symbol
  my $N2 = $Symbol{$n2};                                                        # Character code for second symbol
  if (! defined $N1)                                                            # Check code exists for name
   {carp "No character code for $n1";
    next;
   }
  if (! defined $N2)                                                            # Check code exists for name
   {carp "No character code for $n2";
    next;
   }
  $Kern{$N1}{$N2} = $k;
 }

my @SymbolDef;                                                                  # Symbol definitions
my @Advance;                                                                    # Symbol advances
my @GetGlyphNumber;                                                             # Code to assign glyph number
my @DrawSymbol;                                                                 # Symbol draw methods
my @AdvanceSymbol;                                                              # Symbol advance methods
for(@Glyphs)                                                                    # Convert each glyph
 {my @p = my ($name, $symbol, $advance, $svg) =                                 # Keywords for each glyph. There is a glyp with the name ""
    /<glyph
           \s*unicode=       \"(.+?)\"
           \s*glyph-name=    \"(.*?)\"
           \s*(?:horiz-adv-x=\"(\d+?)\")?
           \s*(?:d=          \"([^"]+?)\")?
           .+?\/>
    /xgi;
  $advance //= $fontAdvance;                                                    # For characters have no advance assume that the advance is the global font advance
  $svg     //= '';                                                              # Some characters like spaces do not need drawing
  if (grep{!defined} ($name, $symbol))                                          # Keywords found check
   {carp "Not all keywords present for DejaVu glyph $_";
    next;
   }
  my $n = translateUnicodeName($name);                                          # Translate name

  if (1)                                                                        # Commands to draw glyp
   {my @c = $svg =~ /([[:alpha:]][^[:alpha:]]*)/g;                              # Commands to draw glyp
    my $P = translateGlyphName($symbol);
    my $D = '  private int draw'   .$P.'() {';                                  # Procedure to draw symbol
    my $A = '  private int advance'.$P.'() {';                                  # Procedure to advance symbol
    for(@c)                                                                     # Each command
     {my ($c, $p) = /\A(.)(.*)\Z/;                                              # Command plus zero or more parameters
      $D .= "$c(";                                                              # Java command
      $D .= $_."f," for split /\s+/, $p;                                        # Make each number floating point
      chop($D) if $D =~ /,\Z/;                                                  # Remove trailing comma
      $D .= ");\n";                                                             # End of command
     }

    my $a = '';                                                                 # End symbol/advance definition
    if (my $kern = $Kern{$n})                                                   # If kerning info is present for this character code
     {$a .= "  switch(C) {\n";
      for(sort keys %$kern)
       {$a .= sprintf("    case %s: return %6d + %6d;\n",
                      $_, $advance, $kern->{$_});
       }
      $a .= "   }\n";
     }
    $a .= sprintf("  return %6d;", $advance);                                   # End symbol/advance definition with no kern available
    $a .= " }";

    push @DrawSymbol,    $D.$a;                                                 # Save symbol draw method
    push @AdvanceSymbol, $A.$a;                                                 # Save symbol advance method
   }
  my $P = translateGlyphName($symbol);
  push @GetGlyphNumber, sprintf("      case %s: return %s;",                    # Decode glyph number
                        $n, $NumberToGlyph{$n});
  my $t   = sprintf('      case %s: return ',        $NumberToGlyph{$n});       # Draw by glyph number
  push @SymbolDef, "$t draw$P();";                                              # Call draw method
  push @Advance, "$t advance$P();";                                             # Call advance method
 }

# Generate
my $space = <<'END' =~ s/\R\Z//r;                                               # Generate code without Java complaining about the size of the code
END

  my $t1 = <<"END" =~ s/\R\Z//r;
//------------------------------------------------------------------------------
// Paths for each letter in DejaVuSans - generated by $home
//------------------------------------------------------------------------------
package $package;

import android.graphics.*;

public class $font extends Path
 {float SX = 0, SY = 0;                                                         // Start position for the next letter
  float  X = 0,  Y = 0;                                                         // Current SVG pen position
  float cX = 0, cY = 0;                                                         // Last control point
  boolean QQ = false;                                                           // Last control point has been set or not
  char c = 0;                                                                   // Current character
  char C = 0;                                                                   // Previous character
  int  n = 0;                                                                   // Number of character being drawn

  private void M(float x, float y)
   {X = x;
    Y = y;
    moveTo(SX+X, SY-Y);                                                         // -Y because DejaVu has +y above whilest Android has +y below the x axis?
    QQ = false;
   }
  private void h(float x) {l(x, 0);}
  private void H(float x) {L(x, Y);}
  private void v(float y) {l(0, y);}
  private void V(float y) {L(X, y);}
  private void l(float x, float y) {L(X + x, Y + y);}
  private void L(float x, float y)
   {X = x;
    Y = y;
    lineTo(SX+X, SY-Y);
    QQ = false;
   }
  private void q(float x1, float y1, float x2, float y2)
   {Q(X+x1, Y+y1, X+x2, Y+y2);
   }
  private void Q(float x1, float y1, float x2, float y2)
   {quadTo(SX+x1, SY-y1, SX+x2, SY-y2);
    QQ = true;
    cX = x1; cY = y1;
     X = x2;  Y = y2;
   }
  private void t(float x2, float y2)
   {T(X+x2, Y+y2);
   }
  private void T(float x2, float y2)
   {float x1, y1;
    if (QQ)
     {x1 = 2*X-cX; y1 = 2*Y-cY;
     }
    else
     {x1 = X; y1 = Y;
     }
    Q(x1, y1, x2, y2);
   }
  private void z() {close();}
  private void Z() {close();}
//------------------------------------------------------------------------------
// Path for a string
//------------------------------------------------------------------------------
  public $font(String s)                                                        // Create the path for the specified string drawn in font $font
   {SX = 0; SY = 0;                                                             // SX is the last X pos, SY the last Y pos
    C = 0;                                                                      // The last character drawn from which we kern
    for(int i = 0; i < s.length(); ++i)                                         // Each letter
     {X = 0; Y = 0;                                                             // Path pen position relative to start of character
      c = s.charAt(i);                                                          // Character to draw
      SX += d(c, C);                                                            // Draw path for character
      C = c;                                                                    // Kern character
     }
   }
//------------------------------------------------------------------------------
// Get character number
//------------------------------------------------------------------------------
  protected int n()                                                             // Get number of glyph associated with current char so that the switchs in d() and a() work without Java complaining they are too big
   {switch(c) {
END

my $t2 = <<'END' =~ s/\R\Z//r;
     }
    return -1;
   }
//------------------------------------------------------------------------------
// Draw character
//------------------------------------------------------------------------------
  protected int d(char pc, char pC)                                             // Draw character kerned against the prior character (or 0 if no prior character), returns advance or -1 if character not found
   {c = pc; C = pC; n = n();
    switch(n) {
END

my $t3 = <<'END' =~ s/\R\Z//r;
     }
    return -1;
   }
//------------------------------------------------------------------------------
// Advance for a character
//------------------------------------------------------------------------------
  protected int a(char pc, char pC)                                             // Width of a character kerned against the prior character or -1 if the character was not found
   {c = pc; C = pC; n = n();
    switch(n) {
END
my $t4 = <<'END' =~ s/\R\Z//r;
     }
    return -1;
   }
END
my $t5 = <<'END' =~ s/\R\Z//r;
 } // $font
END

writeFile($fontJava, join "\n",
  $t1, @GetGlyphNumber,
  $t2, @SymbolDef,
  $t3, @Advance,
  $t4, @DrawSymbol, @AdvanceSymbol,
  $t5);

if (1)
 {my $r = xxx("javac -nowarn -source 7 -target 7 ".                             # Compile
              "-g -d $classes -cp $androidJar $fontJava");
  !$r or confess "Java failed\n$r";
 }

if (1)
 {makePath($jar);
  my $class = ("$package" =~ s(\.)(/)gsr)."/$font.class";                       # Class file name
  my $r = xxx("jar cf $jar -C $classes $class");                                # Execute jar command
  !$r or confess "Jar failed\n$r";
 }
xxx("jar tf $jar");                                                             # List jar command

say STDERR "\nNormal Finish: $font written to $fontJava";
