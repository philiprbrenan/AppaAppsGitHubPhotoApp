## Create Immersive Educational Android Apps with AppaApps

![Test](https://github.com/philiprbrenan/AppaAppsGitHubPhotoApp/workflows/Test/badge.svg)

Create an [immersive Educational Android Photo
App](https://github.com/philiprbrenan/AppaAppsGitHubPhotoApp/raw/main/AppaAppsGitHubPhotoApp.apk)
without doing any programming by forking this repository and changing the just
the photos and the facts - everything else will be done for you.

Every time you change a file in the forked copy of this repository the app will
rebuild itself a new so that it is ready for you to download and play on your
phone.

## Detailed Instructions

Fork this repository by clicking on the **Fork** button in the top right
corner.

Place some photos in your forked copy of this repository and describe them with
some facts saved in a **.txt** file saved in your repository. The app will be
generated each time you save a new copy of this file. GitHub will send you an
issue by email to tell you the results of each change that you make. Or you can
click on the **Actions** tab of your repository and watch GitHub actions create
your app for you.


### Adding Photos

Add some photos of the items you wish to discuss with your students to your
forked copy of this repository.  The names of the files should end in either
.jpg or .png. You can place these photo files anywhere in your repository, under
any folder name.  I tend to use the __images/__ folder but this is not
required. Feel free to delete any existing photos you do not want.

### Adding Facts

The facts about each photo are held in the **.txt** file with the shortest name
in the repository.  At the moment there is only one such file
__images/seasons.txt__ which you could either reuse or delete and start over
again with a different name.

The structure of the **.txt** file is: photo name followed by the title of the
photo followed by facts about the photo one fact or title per line.  Blank
lines are allowed.  Comments, which are ignored, begin with **#** and extend to
the end of the line.  Here is a sample **.txt**file layout with some
explanatory comments:

~~~~
  images/Summer1.jpg    # A photo file in this repository illustrating "Summer"
  images/Summer2.jpg    # Optionally: more photos of "Summer"

  Summer                # The title of the preceding photos
  Warm and sunny        # A fact about Summer illustrated by the preceding photos
  Not often cold        # Another fact about Summer - ditto


  Winter.png            # Another photo file name

  Winter                # The title of the preceding photo
  Cold and dark         # A fact illustrated by the preceding photo
~~~~

### Synthesizing Speech

A userid and secret from:
[AWS Polly](https://docs.aws.amazon.com/polly/latest/dg/security-iam.html#security_iam_authentication)
is needed to enable [AWS Polly](https://aws.amazon.com/polly/) to synthesize
the speech for your app.

The access key and secret access key you obtained from AWS should be installed
as two secrets in the settings for your forked copy of this repository as:

~~~~
  Key_______________________   Value____________________________________
  AWSPolly_ACCESS_KEY_ID       20 characters in upper case A-Z 0-9
  AWSPolly_SECRET_ACCESS_KEY   40 characters in mixed case a-z A-Z 0-9 /
~~~~

as described in:
[Secrets](https://docs.github.com/en/free-pro-team@latest/actions/reference/encrypted-secrets#creating-encrypted-secrets-for-a-repository)

Please raise an issue against this repository if you have any difficulty with
this step and I will be pleased to help you.  Or add me as a
[collaborator](https://docs.github.com/en/enterprise/2.14/user/articles/inviting-collaborators-to-a-personal-repository)
and I will be able to fix your repository for you.

### Notification

GitHub will send you an email when your app is ready or if any errors were
detected while generating your app.  Correct the errors and the app will
rebuild itself. Iterate until you are happy with the results.

## Playing your educational app

Look at the photos displayed on the screen, listen to the spoken fact and then
**tap** on the photo that corresponds to the spoken fact. **Tap**ing is much
faster and easier than swiping which is why we use **taps** rather than
**swipes** to control the app.

The app teaches by asking the student the questions whose answers they know
least well while reteaching them better known material, complementing them on
their progress, playing them music as rewards for correct answers and
remonstrating with them when they get things wrong - all with the infinite
patience and precision of a machine so that the material becomes known by rote.

And if you think that knowing things by rote is not important please send me an
email in fluent Klingon, __not English__, explaining why.

## Problems

Please create an issue against this repository if you need help or add me as a
[collaborator](https://docs.github.com/en/enterprise/2.14/user/articles/inviting-collaborators-to-a-personal-repository)
and I will be able to fix your repository for you.

I will be pleased to help you because I take pride in my work: I want my work
to work as well for you as it does for me - all day, every day.

## Acknowledgments

The following Perl modules are being used to create educational android apps
for you:

  [Android::Build](https://metacpan.org/pod/Android::Build)

  [Data::Table::Text](https://metacpan.org/pod/Data::Table::Text)

  [GitHub::Crud](https://metacpan.org/pod/GitHub::Crud)
