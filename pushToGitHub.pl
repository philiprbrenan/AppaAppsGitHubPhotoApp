#!/usr/bin/perl -Ilib/ -I/home/phil/perl/cpan/DataTableText/lib/ -I/home/phil/perl/cpan/GitHubCrud/lib/
#-------------------------------------------------------------------------------
# Push Photo App to GitHub/AppaAppsGitHubPhotoApp
# Philip R Brenan at gmail dot com, Appa Apps Ltd Inc., 2020
#-------------------------------------------------------------------------------
use warnings FATAL => qw(all);
use strict;
use Carp;
use Data::Dump qw(dump);
use Data::Table::Text qw(:all);
use GitHub::Crud qw(:all);
use YAML::Loader;
use feature qw(say current_sub);

my $home     = q(/home/phil/);                                                  # Local files
my $cpan     = q(/home/phil/perl/cpan);                                         # Cpan files
my $user     = q(philiprbrenan);                                                # User
my $repo     = q(AppaAppsGitHubPhotoApp);                                       # Repo
my $lf       = fpf($home, $repo);                                               # The local folder
my $wf       = q(.github/workflows/main.yml);                                   # Work flow
my $licence  = fpe($home, $repo, qw(LICENSE md));                               # License
my $dir      = fpd($home, $repo);                                               # Directories to upload

# Files

if (1)                                                                          # Upload files
 {deleteFileUsingSavedToken($user, $repo, $wf);                                 # Delete this file to prevent each upload triggering an action - it will be added at the end.

  my @files = grep {!m(/(backup|build|z)/)}                                     # Select files ignoring backups, builds and tests
    searchDirectoryTreesForMatchingFiles($dir,
      qw(.java .jpg .md .mid .mp3 .pl .pm .png .txt));

  my %files = map {$_=>1} grep {m(genApp.pm|readme)i}                           # Filter files
    @files;

  for my $f(sort keys %files)                                                   # Upload each selected file
   {my $t = swapFilePrefix($f, $dir);
    my $s = $f =~ m((jpg|mid|mp3|png)\Z)i ? readBinaryFile($f) : readFile($f);
    lll "$f to $t ", writeFileUsingSavedToken($user, $repo, $t, $s);
   }
 }

if (0)                                                                          # CPAN modules to lib
 {my @files = qw(
/home/phil/perl/cpan/AndroidBuild/lib/Android/Build.pm
/home/phil/perl/cpan/DataTableText/lib/Data/Table/Text.pm
/home/phil/perl/cpan/GitHubCrud/lib/GitHub/Crud.pm
);

  for my $s(@files)                                                             # Upload each selected file
   {my $t = $s =~ s(\A.*/(lib/)) ($1)r;
    lll "$s to $t ", writeFileFromFileUsingSavedToken($user, $repo, $t, $s);
   }
 }

if (1)                                                                          # Create app
 {my $y = <<'END';                                                              # Workflow for C
name: Test

on:
  push

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout\@v2

    - name: Install Sdk
      run: |
       mkdir -p android/sdk
        (cd  android/sdk; wget -q https://dl.google.com/android/repository/sdk-tools-linux-3859397.zip; unzip -q sdk-tools-linux-3859397.zip)
         echo 'y' | android/sdk/tools/bin/sdkmanager 'platforms;android-25' 'platform-tools' 'build-tools;25.0.3' emulator 'system-images;android-25;google_apis;x86_64'

    - name: Install Java
      run: |
       sudo apt update
       sudo apt install imagemagick zip openjdk-8-jdk tree curl zip

    - name: Create keystore
      run: |
       mkdir keys
       keytool -genkey -v -keystore keys/key.keystore -keyalg RSA -keysize 2048 -validity 10000 -storepass 121212 -alias key -dname 'CN=a, OU=a, O=a, L=a, ST=a, C=a'

    - name: Install Perl Data::Table::Text
      run: |
        sudo cpan -T install Data::Table::Text

    - name: Install Perl Android::Build
      run: |
        sudo cpan -T install Android::Build

    - name: Install Perl GitHub::Crud
      run: |
        sudo cpan -T install GitHub::Crud

    - name: Check Perl
      run: |
        perl -v

    - name: Tree
      run: |
        tree -L 2

    - name: GenApp
      run: |
        export GITHUB_TOKEN=${{ secrets.GITHUB_TOKEN }}
        export AWSPolly_ACCESS_KEY_ID=${{ secrets.AWSPolly_ACCESS_KEY_ID }}
        export AWSPolly_SECRET_ACCESS_KEY=${{ secrets.AWSPolly_SECRET_ACCESS_KEY }}
        perl genApp.pm
END

  lll "Work flow to $repo ", writeFileUsingSavedToken($user, $repo, $wf, $y);
 }

__DATA__
    - name: Artifact
      uses: actions/upload-artifact\@v2
      with:
        name: apk
        path: build/bin/*.apk

