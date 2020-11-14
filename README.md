# Create Educational Android Apps by AppaApps

![Test](https://github.com/philiprbrenan/AppaAppsGitHubPhotoApp/workflows/Test/badge.svg)

Create an AppaApps Educational Android Photo App by forking this repository
and changing the images and facts.

Every time you change the forked copy of this repository the app will rebuild
itself,

GitHub will send you a message when the app is ready to be downloaded to your
phone.

# Detailed Instructions

Place some photos in the forked repository and modify the .txt file to describe
these photos.  The app will be generated each time you save a new copy of a
file into your forked copy.

## Adding Photos

Add some photos of the items you wish to discuss with your students to the
forked repo.  The photos file names should end in either .jpg or .png. You can
place these files anywhere in the repository, under any folder name.  I tend to
use the images/ folder but this is not required. Feel free to remove any
existing photos you do not want.

## Adding Facts

The factual information about each photo is held in the only .txt file in the
repository.  At the moment there is one such file images/seasons.txt which you
could either reuse or delete and start again.

The structure of this file is photo name followed by the title of the photo
followed by facts about the photo one fact or title per line.  Blank lines are
allowed.  Comments, which I will ignore, begin with # and extend to the end of
the line.  Here is a sample file layout:

~~~~
  images/Summer1.jpg    # The photo name in repo
  images/Summer2.jpg    # Optionally more photos of the same thing

  Summer                # The title of the preceding photos
  Warm and sunny        # A fact about summer illustrated by the preceding photos
  Not often cold        # Another fact about summer - ditto


  Winter.png            # Photo name in repo

  Winter                # The title of the preceding photo
  Cold and dark         # A fact about winter illustrated by the preceding photo
~~~~

## Synthesizing Speech

I need a userid and secret from: [AWS Polly](https://docs.aws.amazon.com/polly/latest/dg/security-iam.html#security_iam_authentication)
to synthesize the speech used by the app.

The access key and secret access key you obtained from AWS should be installed
as two secrets in the settings for your forked copy of this repository as in:

~~~~
  Key_______________________   Value____________________________________
  AWSPolly_ACCESS_KEY_ID       20 characters in upper case A-Z 0-9
  AWSPolly_SECRET_ACCESS_KEY   40 characters in mixed case a-z A-z 0-9 /
~~~~

using: [Secrets](https://docs.github.com/en/free-pro-team@latest/actions/reference/encrypted-secrets#creating-encrypted-secrets-for-a-repository)

## Notification

GitHub will send you an email when the app is ready or if I cannot understand
what needs to be done to create your app.

# Problems

Create an issue against this repository if you need help.

# Acknowledgments

The following Perl modules are used to create educational android apps for you:

  [Android::Build](https://metacpan.org/pod/Android::Build)

  [Data::Table::Text](https://metacpan.org/pod/Data::Table::Text)

  [GitHub::Crud](https://metacpan.org/pod/GitHub::Crud)
