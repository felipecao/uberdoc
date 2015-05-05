package com.uberall.uberdoc.metadata

class RequestAndResponseObjects {

    Set requestAndResponseClasses = []

    void extractFromResource(def uberDocResource){

        if(!uberDocResource){
            return
        }

        if(uberDocResource.requestObject() && !(uberDocResource.requestObject() in Closure)){
            requestAndResponseClasses << uberDocResource.requestObject()
        }

        if(uberDocResource.responseObject() && !(uberDocResource.responseObject() in Closure)){
            requestAndResponseClasses << uberDocResource.responseObject()
        }

        if(uberDocResource.responseCollectionOf() && !(uberDocResource.responseCollectionOf() in Closure)){
            requestAndResponseClasses << uberDocResource.responseCollectionOf()
        }

        if(uberDocResource.object() && !(uberDocResource.object() in Closure)){
            requestAndResponseClasses << uberDocResource.object()
        }
    }

    Map fetch(){
        return convertToMap(requestAndResponseClasses)
    }

    private Map convertToMap(Set<Class> set) {
        // should extract information from our annotations on each class and spit it out as a map
        null
    }
}
