package com.uberall.uberdoc.metadata

import org.codehaus.groovy.grails.commons.GrailsClass
import org.codehaus.groovy.grails.web.mapping.UrlMappings

class GrailsReader {

    def grailsApplication
    UrlMappings grailsUrlMappingsHolder

    GrailsReader(grailsApplication, UrlMappings grailsUrlMappingsHolder) {
        this.grailsApplication = grailsApplication
        this.grailsUrlMappingsHolder = grailsUrlMappingsHolder
    }

    GrailsClass[] getControllers(){
        return grailsApplication.controllerClasses
    }

    List getMethodsFrom(GrailsClass controller){
        return controller.clazz.methods
    }

    List extractUrlMappingsFor(GrailsClass controller){
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
}
