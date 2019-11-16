# Whisper Commons

Common components for the whisper project, e.g. JWT helper, Security Filters, HTTP client, etc.

## Local Snapshots

```sh
TAG=...-SNAPSHOT # use semver!
mvn versions:set -DnewVersion=$TAG
mvn clean install
```

## Releases

- Snapshots aren't published, for remote builds, artifacts must be released!
- Releases are currently on Bintray: https://bintray.com/beta/#/joerx/whisper/whisper-commons?tab=overview
- Release process:

```sh
TAG=... # use semver!
mvn versions:set -DnewVersion=$TAG
git tag -a $TAG -m"Release $TAG"
mvn clean deploy
git push origin $TAG
```
