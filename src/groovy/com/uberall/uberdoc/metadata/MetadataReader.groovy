package com.uberall.uberdoc.metadata

import java.lang.reflect.AnnotatedElement

class MetadataReader {

    private def type

    MetadataReader getAnnotation(def t){
        type = t
        return this
    }

    def inClass(AnnotatedElement object){
        return object.annotations.find { it.annotationType() == type }
    }

}
