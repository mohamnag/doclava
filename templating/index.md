---
layout: page
title: Templating guide
---

Following gives you the idea of how the templating works. This is necessary
if you want to modify or replace the templates.

## Template engines
Job of applying extracted data to templates and generating final 
documentation is dedicated to template engines. This gives the flexibility
of generating a wide range of output types.

The template engine interface defined by this package is dictated by 
`com.mohamnag.doclavax.TemplateEngine` interface and has absolutely 
minimalistic implications. This means anybody can potentially plug his 
own implementation into the process (this plugging is not yet in place 
see [roadmap](../roadmap)).

Each template engine is also passed some parameters like template input 
and output directories (not yet in place, see [roadmap](../roadmap)).  


### Handlebars
Currently there only exists one engine implementation which is based on
Handlebars templates using the [handlebars.java](https://github.com/jknack/handlebars.java)
engine. 

You can see the guides on how to modify [handlebars templates](handlebars).


## Templating concepts
There are three categories of pages that should be handled by templating 
engines:

 - Class pages for each root class (in contrast to inner classes)
 - Package pages for each package
 - Meta pages, can contain any kind of additional data
 
The templating engine will handle the first two types specially, each 
class template will be rendered multiple times, each time with the context 
of a dedicated class. Similarly a package template will be rendered multiple 
times each time with context of a single package. While compiling each 
class or package page, additional info is also provided in the context
which contains at least the full package and root class list. However each 
template engine may add more data into these meta information.

All additional templates fall into the category of meta pages and are 
rendered ONLY once with context of full list of packages and root classes.