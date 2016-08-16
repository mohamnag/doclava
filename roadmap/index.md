---
layout: page
title: Roadmap
shortDescription: a modern javadoc alternative
---

# Phase 1
Implementing a new look and feel with existing limitations.
    - [x] switch to handlebars
    - [x] dont force output structure where it is not necessary
    - [] rewrite templates to give the first impression of what output may potentially be
    - [] make template input directory definable by params
    - [] make output directory definable by params
    - [] release 2.0 
    

# Phase 2
Fixing lots of data structures used in templates. Remove/reduce lazy data
and heavy calculations in all DocInfo subclasses. Fixing not handled Java 
8 tags.

Enable JavaScript external helpers for max flexibility and freedom of 
template writers.

# Phase 3
Add some alternate outputs templates like XML, JSON and so on.
Add support for plugging in other templating engine implementations.