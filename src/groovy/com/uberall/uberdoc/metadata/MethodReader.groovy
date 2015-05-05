package com.uberall.uberdoc.metadata

import com.uberall.uberdoc.annotation.UberDocError
import com.uberall.uberdoc.annotation.UberDocErrors
import com.uberall.uberdoc.annotation.UberDocHeader
import com.uberall.uberdoc.annotation.UberDocHeaders
import com.uberall.uberdoc.annotation.UberDocQueryParam
import com.uberall.uberdoc.annotation.UberDocQueryParams
import com.uberall.uberdoc.annotation.UberDocResource
import com.uberall.uberdoc.annotation.UberDocUriParam
import com.uberall.uberdoc.annotation.UberDocUriParams

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

    Class getResponseCollection(){
        def uberDocResource = reader.getAnnotation(UberDocResource).inMethod(method)
        return (uberDocResource?.responseCollectionOf() in Closure ) ? null : uberDocResource?.responseCollectionOf()
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

    List<Map> getHeaders(){
        def ret = []
        def methodHeaders = reader.getAnnotation(UberDocHeaders).inMethod(method)
        def singleHeader = reader.getAnnotation(UberDocHeader).inMethod(method)

        if(singleHeader){
            ret << parseHeader(singleHeader)
        }

        if(methodHeaders){
            methodHeaders.value().each {
                ret << parseHeader(it)
            }
        }

        if(genericHeaders){
            ret.addAll(genericHeaders)
        }

        return ret
    }

    private Map parseHeader(def hdr){
        if(!hdr){
            return [:]
        }
        return [name: hdr.name(), description: hdr.description(), required: hdr.required(), sampleValue: hdr.sampleValue()]
    }

    List<Map> getUriParams(){
        def ret = []
        def methodUriParams = reader.getAnnotation(UberDocUriParams).inMethod(method)
        def singleUriParam = reader.getAnnotation(UberDocUriParam).inMethod(method)

        if(singleUriParam){
            ret << parseUriParam(singleUriParam)
        }

        if(methodUriParams){
            methodUriParams.value().each {
                ret << parseUriParam(it)
            }
        }

        return ret
    }

    private Map parseUriParam(def hdr){
        if(!hdr){
            return [:]
        }
        return [name: hdr.name(), description: hdr.description(), sampleValue: hdr.sampleValue()]
    }

    List<Map> getQueryParams(){
        def ret = []
        def methodQueryParams = reader.getAnnotation(UberDocQueryParams).inMethod(method)
        def singleQueryParam = reader.getAnnotation(UberDocQueryParam).inMethod(method)

        if(singleQueryParam){
            ret << parseQueryParam(singleQueryParam)
        }

        if(methodQueryParams){
            methodQueryParams.value().each {
                ret << parseQueryParam(it)
            }
        }

        return ret
    }

    private Map parseQueryParam(def hdr){
        if(!hdr){
            return [:]
        }
        return [name: hdr.name(), description: hdr.description(), sampleValue: hdr.sampleValue(), required: hdr.required(), isCollection: hdr.isCollection()]
    }

}
