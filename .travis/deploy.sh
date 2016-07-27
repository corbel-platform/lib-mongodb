if [ ! -z "$TRAVIS_TAG" ]
then
    echo "On a tag -> set pom.xml <version> to $TRAVIS_TAG"
    mvn --settings .travis/settings.xml org.codehaus.mojo:versions-maven-plugin:2.1:set -DnewVersion=$TRAVIS_TAG 1>/dev/null 2>/dev/null
else
    echo "Not on a tag -> Keep snapshot version in pom.xml"
fi

mvn clean deploy --settings .travis/settings.xml -B -U