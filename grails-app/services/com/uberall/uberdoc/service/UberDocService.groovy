package com.uberall.uberdoc.service

import com.uberall.uberdoc.annotation.Errors
import com.uberall.uberdoc.metadata.MetadataReader
import org.codehaus.groovy.grails.commons.GrailsClass
import org.codehaus.groovy.grails.web.mapping.UrlMappings

import java.lang.reflect.AnnotatedElement

class UberDocService {

    def grailsApplication
    UrlMappings grailsUrlMappingsHolder

    Map getApiDocs() {
        Map apiInfo = [:]

        for(GrailsClass controller: controllers){
            Map genericErrors = getErrors(controller)
            Map genericHeaders = [:]

            getActionsForController(controller).each { action ->
                // TODO implement this!
            }
        }

        return apiInfo
    }

    private GrailsClass[] getControllers(){
        return grailsApplication.controllerClasses
    }

    private List getActionsForController(GrailsClass controller){
        // TODO implement this!
    }

    private List<Map> getErrors(GrailsClass gClass){
        def errors = new MetadataReader().getAnnotation(Errors).inClass(gClass)
        def ret = []

        if(!errors){
            return []
        }

        errors.value().each { err ->
            ret << [errorCode: err.errorCode(), httpCode: err.httpCode(), description: err.description()]
        }

        return ret
    }

    @Deprecated // use MetadataReader
    private <T> List<T> getAnnotationsOfTypeInClass(Class<T> annotation, AnnotatedElement object){
        return object.annotations.findAll { it.annotationType() == annotation } as T
    }

}
