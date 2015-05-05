package com.uberall.uberdoc.service

import com.uberall.uberdoc.annotation.UberDocErrors
import com.uberall.uberdoc.annotation.UberDocError
import com.uberall.uberdoc.annotation.UberDocHeader
import com.uberall.uberdoc.annotation.UberDocHeaders
import com.uberall.uberdoc.annotation.UberDocQueryParam
import com.uberall.uberdoc.annotation.UberDocResource
import com.uberall.uberdoc.metadata.MetadataReader
import com.uberall.uberdoc.metadata.MethodReader
import com.uberall.uberdoc.metadata.RequestAndResponseObjects
import org.codehaus.groovy.grails.commons.GrailsClass
import org.codehaus.groovy.grails.web.mapping.UrlMappings

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Method

class UberDocService {

    def grailsApplication
    UrlMappings grailsUrlMappingsHolder
    MetadataReader metadataReader = new MetadataReader()

    Map getApiDocs() {
        Map apiInfo = [:]

        List<Map> genericErrors
        List<Map> genericHeaders
        List controllerMethods
        List resources
        Map restfulResource
        MethodReader methodReader
        RequestAndResponseObjects objects = new RequestAndResponseObjects() // this info will be put in the root of the returned object

        apiInfo.objects = [:]
        apiInfo.resources = []

        for(GrailsClass controller: controllers){
            genericErrors = getErrors(controller)
            genericHeaders = getHeaders(controller)
            controllerMethods = getControllerMethods(controller)
            resources = extractResourcesFromController(controller)

            resources.each { restResource ->
                def controllerMethod = controllerMethods.find { it.name == restResource.name }

                methodReader = new MethodReader(controllerMethod)
                        .useGenericErrors(genericErrors)
                        .useGenericHeaders(genericHeaders)

                objects.extractFromResource(metadataReader.getAnnotation(UberDocResource).inMethod(controllerMethod))

                def headers = metadataReader.getAnnotation(UberDocHeaders).inMethod(controllerMethod)
                def singleHeader = metadataReader.getAnnotation(UberDocHeader).inMethod(controllerMethod)

                def singleQueryParam = metadataReader.getAnnotation(UberDocQueryParam).inMethod(controllerMethod)


                def allAnnotations = getAnnotationsOfSupportedTypesInMethod(controllerMethod)

                println "allAnnotations = $allAnnotations"

                restfulResource = [:]
                restfulResource.description = methodReader.description
                restfulResource.uri = restResource.uri?.replaceAll("\\(\\*\\)", "{id}")
                restfulResource.method = restResource.method
                restfulResource.requestObject = methodReader.requestObject
                restfulResource.responseObject = methodReader.responseObject
                restfulResource.responseIsCollection = null // TODO
                restfulResource.uriParams = [[:]] // TODO
                restfulResource.queryParams = [[:]] // TODO
                restfulResource.headers = [[:]] // TODO
                restfulResource.errors = methodReader.errors

                apiInfo.resources << restfulResource
            }
        }

        apiInfo.objects = objects.fetch()

        return apiInfo
    }

    private GrailsClass[] getControllers(){
        return grailsApplication.controllerClasses
    }

    private List extractResourcesFromController(GrailsClass controller){
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

    private List getControllerMethods(GrailsClass controller){
        return controller.clazz.methods
    }

    @Deprecated
    private <T> List<T> getAnnotationsOfSupportedTypesInMethod(Method method){
        return method.annotations.findAll { it.annotationType() in [UberDocResource, UberDocErrors, UberDocError, UberDocHeaders, UberDocHeader, UberDocQueryParam] } as List<T>
    }

    private List<Map> getErrors(GrailsClass gClass){
        def errors = metadataReader.getAnnotation(UberDocErrors).inController(gClass)
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
        def headers = metadataReader.getAnnotation(UberDocHeaders).inController(gClass)
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
