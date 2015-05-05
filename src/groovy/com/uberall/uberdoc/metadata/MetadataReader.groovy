package com.uberall.uberdoc.metadata

import org.codehaus.groovy.grails.commons.GrailsClass

class MetadataReader {

    private def type

    MetadataReader getAnnotation(def t){
        type = t
        return this
    }

    def inClass(GrailsClass object){
        return object.annotations.find { it.annotationType() == type }
    }

    def inController(GrailsClass controller){
        return controller.clazz.annotations.find { it.annotationType() == type }
    }

}
