package com.uberall.uberdoc.metadata

class RequestAndResponseObjects {

    Set requestAndResponseClasses = []

    void extractFromResource(def annotation){

        if(!annotation){
            return
        }

        if(annotation.requestObject() && !(annotation.requestObject() in Closure)){
            requestAndResponseClasses << annotation.requestObject()
        }

        if(annotation.responseObject() && !(annotation.responseObject() in Closure)){
            requestAndResponseClasses << annotation.responseObject()
        }

        if(annotation.responseCollectionOf() && !(annotation.responseCollectionOf() in Closure)){
            requestAndResponseClasses << annotation.responseCollectionOf()
        }

        if(annotation.object() && !(annotation.object() in Closure)){
            requestAndResponseClasses << annotation.object()
        }
    }

    Map fetch(){
        return convertToMap(requestAndResponseClasses)
    }

    private Map convertToMap(Set set) {
        // should extract information from our annotations on each class and spit it out as a map
        null
    }
}
