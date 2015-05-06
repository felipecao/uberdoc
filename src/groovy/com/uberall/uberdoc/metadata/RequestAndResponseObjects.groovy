package com.uberall.uberdoc.metadata

import com.uberall.uberdoc.annotation.UberDocModel
import com.uberall.uberdoc.annotation.UberDocProperty
import groovy.util.logging.Log4j
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsDomainClass

import java.lang.reflect.Field

@Log4j
class RequestAndResponseObjects {

    private Set requestAndResponseClasses = []
    private GrailsApplication grailsApplication

    RequestAndResponseObjects(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication
    }

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

    /**
     * Extracts information from our annotations on each class and spits it out as a map
     *
     * Each request/response class:
     * - description
     * - properties
     * - name
     * - type (String, int, Boolean, etc)
     * - constraints (custom > country specifics)
     * - description
     * - sample value
     *
     * @param set the set of classes to grab information from
     * @return a map
     */
    private Map convertToMap(Set<Class> set) {
        Map<Class, Object> result = [:]

        set.each { Class clazz ->
            if (!clazz.isAnnotationPresent(UberDocModel)) {
                return
            }

            log.info("reading from $clazz")

            // collect class information
            UberDocModel modelAnnotation = clazz.getAnnotation(UberDocModel)
            Map clazzInfo = [:]
            clazzInfo << [name: clazz.name]
            clazzInfo << [description: modelAnnotation.description()]
            clazzInfo << [properties: []]

            // go over each field that is annotated and grab information from it
            clazz.declaredFields.each { Field field ->
                if (field.isAnnotationPresent(UberDocProperty)) {
                    Map fieldInformation = getProperties(field)
                    clazzInfo.properties << fieldInformation
                }
            }

            result << [(clazz): clazzInfo]
        }

        return result
    }

    private Map getProperties(Field field) {
        UberDocProperty propertyAnnotation = field.getAnnotation(UberDocProperty)

        // grab info from annotation
        Map propertyMap = [:]
        propertyMap << [name: field.name]
        propertyMap << [type: field.type.name]
        propertyMap << [description: propertyAnnotation.description()]
        propertyMap << [sampleValue: propertyAnnotation.sampleValue()]

        // read constraints
        GrailsDomainClass domainClass = grailsApplication.getDomainClass(field.getDeclaringClass().name) as GrailsDomainClass
        propertyMap << [constraints: domainClass.getConstrainedProperties()]

        return propertyMap
    }

}
