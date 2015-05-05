package com.uberall.uberdoc.metadata

import com.uberall.uberdoc.annotation.UberDocError
import com.uberall.uberdoc.annotation.UberDocErrors
import com.uberall.uberdoc.annotation.UberDocResource

class MethodReader {

    def method
    MetadataReader reader
    List<Map> genericErrors
    List<Map> genericHeaders

    MethodReader(method) {
        this.method = method
        reader = new MetadataReader()
    }

    MethodReader useGenericErrors(def g){
        if(g){
            genericErrors = g
        }
        return this
    }

    MethodReader useGenericHeaders(def g){
        if(g){
            genericHeaders = g
        }
        return this
    }

    String getDescription(){
        def uberDocResource = reader.getAnnotation(UberDocResource).inMethod(method)
        return uberDocResource?.description() ?: null
    }

    Class getRequestObject(){
        def uberDocResource = reader.getAnnotation(UberDocResource).inMethod(method)
        def requestObject = (uberDocResource?.requestObject() in Closure ) ? null : uberDocResource?.requestObject()
        def object = (uberDocResource?.object() in Closure ) ? null : uberDocResource?.object()
        return requestObject ?: object
    }

    Class getResponseObject(){
        def uberDocResource = reader.getAnnotation(UberDocResource).inMethod(method)
        def responseObject = (uberDocResource?.responseObject() in Closure ) ? null : uberDocResource?.responseObject()
        def object = (uberDocResource?.object() in Closure ) ? null : uberDocResource?.object()
        return responseObject ?: object
    }

    List<Map> getErrors(){
        def ret = []
        def methodErrors = reader.getAnnotation(UberDocErrors).inMethod(method)
        def singleError = reader.getAnnotation(UberDocError).inMethod(method)

        if(singleError){
            ret << parseError(singleError)
        }

        if(methodErrors){
            methodErrors.value().each {
                ret << parseError(it)
            }
        }

        if(genericErrors){
            ret.addAll(genericErrors)
        }

        return ret
    }

    private Map parseError(def err){
        if(!err){
            return [:]
        }
        return [errorCode: err.errorCode(), httpCode: err.httpCode(), description: err.description()]
    }

}
