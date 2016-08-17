---
layout: page
title: Handlebars templates guide
---

If you are unfamiliar with handlebars, we recommend taking a look at 
[its definition](http://handlebarsjs.com/).

Basically there are minimal implications on which templates are needed 
for a valid and successful generation of documentations:

 - at least one `class.hbs` file for compiling root class pages  
 - at least one `package.hbs` file for compiling package pages
   
Apart from these mandatory templates, any other template in root of input
directory will be compiled too. Such pages are referred to as meta pages. 

The context of templates being compiled follows the generic rules defined
in [templating overview](../), for detailed and more information we 
recommend taking a look at [default templates](https://github.com/mohamnag/doclava/tree/master/src/main/resources/templates)
provided alongside the code.

# Templates directory structure
The templates' input directory (currently fixed to default templates see 
[issue #3](https://github.com/mohamnag/doclava/issues/3)) shall follow 
these rules:

 - a `class.hbs` and `package.hbs` template is expected on the root
 
 - any file on the root with `.hbs` extension will be compiled and copied
 as `index.html` inside a directory on root of output

 - any directory that does **not** start with a `_` will be copied as it
 is to root of output directory
 
 - any directory that starts with a `_` will be ignored (can be used for
 handlebars partials)

 - any JavaScript file inside directory named `_helpers` will be registered
 as handlebars helper and should follow its [proper format](https://github.com/jknack/handlebars.java#with-plain-javascript).

Lets say this is the given template input dir:

```
    .
    ├── _helpers
    │   └── package_to_link.js
    ├── _partials
    │   ├── base.hbs
    │   ├── footer.hbs
    │   ├── header.hbs
    ├── class.hbs
    ├── images
    ├── index.hbs
    ├── package.hbs
    ├── packages.hbs
    └── styles
        ├── bootstrap-theme.css
        ├── fonts
        │   └── glyphicons-halflings-regular.ttf
        ├── js
        │   └── bootstrap.js
        └── custom.css
```

In this case the output will have following structure when applied to 
the code base of this same project for document generation (for 
abbreviation, some of the docs' HTML files have been removed):

```
    .
    ├── com
    │   ├── google
    │   │   └── doclava
    │   │       ├── AnnotationInstanceInfo
    │   │       │   └── index.html
    │   │       ├── AnnotationValueInfo
    │   │       │   └── index.html
    │   │       │...
    │   │       │
    │   │       ├── apicheck
    │   │       │   ├── AbstractMethodInfo
    │   │       │   │   └── index.html
    │   │       │   ├── ApiCheck
    │   │       │   │   └── index.html
    │   │       │   └── index.html
    │   │       │...
    │   │       │
    │   │       └── index.html
    │   └── mohamnag
    │       └── doclavax
    │           ├── Doclavax
    │           │   └── index.html
    │           ├── HandlebarsTemplateEngine
    │           │   └── index.html
    │           │...
    │           │
    │           └── index.html
    ├── images
    ├── index.html
    ├── packages
    │   └── index.html
    └── styles
        ├── bootstrap-theme.css
        ├── fonts
        │   └── glyphicons-halflings-regular.ttf
        ├── js
        │   └── bootstrap.js
        └── custom.css
```
