package com.uberall.uberdoc.service

import com.uberall.uberdoc.annotation.Errors
import com.uberall.uberdoc.annotation.HeaderParam
import com.uberall.uberdoc.annotation.HeaderParams
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
            List<Map> genericErrors = getErrors(controller)
            List<Map> genericHeaders = getHeaders(controller)

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
        def mappedActions = grailsUrlMappingsHolder.urlMappings.findAll {
            it.controllerName == controller.name.toLowerCase()
        }

        if(!mappedActions){
            return []
        }

        // url, method, annotations from action
        def resources = []

        mappedActions.each { action ->
            if(!action.actionName in String){
                for(Map.Entry entry: action.actionName){
                    resources << [name: entry.value, uri: action.toString(), method: entry.key]
                }
            }
//            resources << [actionName: "", name: "", uri: "", method: ""]
        }

        return resources
    }

    private List<Map> getErrors(GrailsClass gClass){
        def errors = new MetadataReader().getAnnotation(Errors).inController(gClass)
        def ret = []

        if(!errors){
            return []
        }

        errors.value().each { err ->
            ret << [errorCode: err.errorCode(), httpCode: err.httpCode(), description: err.description()]
        }

        return ret
    }

    private List<Map> getHeaders(GrailsClass gClass){
        def headers = new MetadataReader().getAnnotation(HeaderParams).inController(gClass)
        def ret = []

        if(!headers){
            return []
        }

        headers.value().each { hdr ->
            ret << [name: hdr.name(), description: hdr.description(), required: hdr.required(), sampleValue: hdr.sampleValue()]
        }

        return ret
    }

    @Deprecated // use MetadataReader
    private <T> List<T> getAnnotationsOfTypeInClass(Class<T> annotation, AnnotatedElement object){
        return object.annotations.findAll { it.annotationType() == annotation } as T
    }

}
