package com.uberall.uberdoc.service

import com.uberall.uberdoc.annotation.UberDocErrors
import com.uberall.uberdoc.annotation.UberDocError
import com.uberall.uberdoc.annotation.UberDocHeader
import com.uberall.uberdoc.annotation.UberDocHeaders
import com.uberall.uberdoc.annotation.UberDocQueryParam
import com.uberall.uberdoc.annotation.UberDocResource
import com.uberall.uberdoc.metadata.MetadataReader
import com.uberall.uberdoc.metadata.RequestAndResponseObjects
import org.codehaus.groovy.grails.commons.GrailsClass
import org.codehaus.groovy.grails.web.mapping.UrlMappings

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Method

class UberDocService {

    def grailsApplication
    UrlMappings grailsUrlMappingsHolder

    Map getApiDocs() {
        Map apiInfo = [:]

        List<Map> genericErrors
        List<Map> genericHeaders
        List methods
        List resources
        RequestAndResponseObjects objects = new RequestAndResponseObjects() // this info will be put in the root of the returned object

        apiInfo.objects = [:]
        apiInfo.resources = [:]

        for(GrailsClass controller: controllers){
            genericErrors = getErrors(controller)
            genericHeaders = getHeaders(controller)
            methods = getMethodsForController(controller)
            resources = getResourcesForController(controller)

            resources.each { res ->
                def method = methods.find { it.name == res.name }

                def resource = getAnnotationOfTypeInMethod(UberDocResource, method)
                objects.extractFromResource(resource)

                def errors = getAnnotationOfTypeInMethod(UberDocErrors, method)
                def singleError = getAnnotationOfTypeInMethod(UberDocError, method)

                def headers = getAnnotationOfTypeInMethod(UberDocHeaders, method)
                def singleHeader = getAnnotationOfTypeInMethod(UberDocHeader, method)

                def singleQueryParam = getAnnotationOfTypeInMethod(UberDocQueryParam, method)

                def allAnnotations = getAnnotationsOfSupportedTypesInMethod(method)

                println "allAnnotations = $allAnnotations"
            }
        }
        apiInfo.objects = objects.fetch()

        return apiInfo
    }

    private GrailsClass[] getControllers(){
        return grailsApplication.controllerClasses
    }

    private List getResourcesForController(GrailsClass controller){
        def mappedActions = grailsUrlMappingsHolder.urlMappings.findAll {
            it.controllerName == controller.name.toLowerCase()
        }

        if(!mappedActions){
            return []
        }

        def resources = []

        mappedActions.each { action ->
            if(action.actionName in Map){
                for(Map.Entry entry: action.actionName.entrySet()){
                    resources << [name: entry.value, uri: action.toString(), method: entry.key]
                }
            }
        }

        return resources
    }

    private List getMethodsForController(GrailsClass controller){
        return controller.clazz.methods
    }

    private <T> T getAnnotationOfTypeInMethod(Class annotation, Method method){
        return method.annotations.find { it.annotationType() == annotation } as T
    }

    private <T> List<T> getAnnotationsOfSupportedTypesInMethod(Method method){
        return method.annotations.findAll { it.annotationType() in [UberDocResource, UberDocErrors, UberDocError, UberDocHeaders, UberDocHeader, UberDocQueryParam] } as List<T>
    }

    private List<Map> getErrors(GrailsClass gClass){
        def errors = new MetadataReader().getAnnotation(UberDocErrors).inController(gClass)
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
        def headers = new MetadataReader().getAnnotation(UberDocHeaders).inController(gClass)
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
