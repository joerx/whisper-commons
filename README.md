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

## Generating Keys

For some annoying reason that only the great wizards at the oracle of senseless complexity understand, keys must be
in PKCS#8 format. Because fuck you, we're different.

To generate keys in PKCS#8 format (based on https://stackoverflow.com/questions/11410770/load-rsa-public-key-from-file):

```bash
$ openssl genrsa -out private_key.pem 2048

$ openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key.pem -out private_key.der -nocrypt

$ openssl rsa -in private_key.pem -pubout -outform DER -out public_key.der
``` 

## Utils

### Get Current Version

```sh
mvn -Dexec.executable='echo' -Dexec.args='${project.version}' --non-recursive exec:exec -q
```
