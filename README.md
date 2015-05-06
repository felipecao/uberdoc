# uberdoc
A very simple Grails plugin to generate RESTful API documentation.

This plug-in is meant to provide a very simple way to extract RESTful information from Grails apps.

What it does is going through all controllers annotated with @UberDocController and extract information from request/response objects, along with information from UrlMappings
and structure it within a Map, that then can be used whatever way the app using it feels like is more appropriate.

So, the plugin just provides:
- a set of annotations
- a Service with a public method that extracts information from classes and return it as a Map

This was done on purpose, so that your app can decide on the best way to display or cache this information.