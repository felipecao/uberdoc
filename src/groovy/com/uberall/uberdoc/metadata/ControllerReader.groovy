package com.uberall.uberdoc.metadata

import com.uberall.uberdoc.annotation.UberDocErrors
import com.uberall.uberdoc.annotation.UberDocHeaders
import org.codehaus.groovy.grails.commons.GrailsClass

class ControllerReader {

    GrailsClass controller
    MetadataReader metadataReader

    ControllerReader(GrailsClass gClass) {
        controller = gClass
        metadataReader = new MetadataReader()
    }

    List<Map> getErrors(){
        def errors = metadataReader.getAnnotation(UberDocErrors).inController(controller)
        def ret = []

        if(!errors){
            return []
        }

        errors.value().each { err ->
            ret << [errorCode: err.errorCode(), httpCode: err.httpCode(), description: err.description()]
        }

        return ret
    }

    List<Map> getHeaders(){
        def headers = metadataReader.getAnnotation(UberDocHeaders).inController(controller)
        def ret = []

        if(!headers){
            return []
        }

        headers.value().each { hdr ->
            ret << [name: hdr.name(), description: hdr.description(), required: hdr.required(), sampleValue: hdr.sampleValue()]
        }

        return ret
    }
}