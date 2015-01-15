Doclava
=======

Doclava is a new javadoc doclet which is written by Google and used for Android Developer Docs. Clear and efficient(support search).

This project is forked from https://code.google.com/p/doclava/, and with a few style modification.

## How to build
The [official site](https://code.google.com/p/doclava/) has binary downloads offered(or you can download it from [here](releases)).

We use [Gradle](http://www.gradle.org/) to build this project.

    gradle build

This will build a jar in build/libs which is the artifact.

Once you have the artifact, run `javadoc` command with the `-doclet` and `-docletpath`(if not in classpath) options to generate javadocs, for example:


    javadoc -encoding UTF-8 -sourcepath src -d docs -subpackages project.package -doclet com.google.doclava.Doclava -docletpath doclava.jar -generatesources -project.name MyProject


More details please refer to [official site](https://code.google.com/p/doclava/).

Here is a screenshot

![Doclava](assets/screenshot.png)
