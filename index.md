---
layout: page
title: Doclava
shortDescription: a modern javadoc alternative
---

## Alternative Javadoc

Javadoc is (or at least may be) the most important asset that any Java developer may deliver beside the code and the product. However the current state of Javadoc has some drawbacks:

 - it has a drastically outdated look and feel
 - it is not easy to search for information as it is not properly indexable
 - deep linking is a big problem and simply does not work
 
We believe even technical people has deserved a modern look and feel in their daily work. Therefore this project aims to provide a modern alternative for standard Javadoc.

> You can view a sample documentation output [here](sample/). Bear in mind that the code is still in beta status.

![package details page](images/package-details-sc.png) ![packages page](images/packages-sc.png)

## How to
Before trying to use this tool for generation of your own documentation,
it is again important to insist that the code is on a beta stage. 

After obtaining the latest JAR (from [here](https://github.com/mohamnag/doclava/releases), 
you can issue following command to generate your Javadocs:
 
```bash
$ javadoc -encoding UTF-8 \
    -sourcepath path/to/your/project/src/main/java/ \
    -d output/path/ \
    -subpackages your.package.prefix \
    -docletpath ~/Projects/doclava-1.0.6.jar \
    -doclet com.google.doclava.Doclava 
```

You can even download the templates of the same release and modify them
as you like, then use the modified templates by issuing following command:
 
```bash
$ javadoc -encoding UTF-8 \
    -sourcepath path/to/your/project/src/main/java/ \
    -d output/path/ \
    -subpackages your.package.prefix \
    -docletpath ~/Projects/doclava-1.0.6.jar \
    -doclet com.google.doclava.Doclava \
    -templatedir path/to/your/modified/templates/
```
