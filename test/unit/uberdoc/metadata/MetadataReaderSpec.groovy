package uberdoc.metadata

import uberdoc.annotation.UberDocController
import uberdoc.annotation.UberDocErrors
import org.codehaus.groovy.grails.commons.GrailsClass
import sample.OtherController
import sample.PodController
import spock.lang.Specification
import uberdoc.annotation.UberDocError

class MetadataReaderSpec extends Specification {

    void "getAnnotation for PodController and annotation Error returns no instances"() {
        given:
        MetadataReader reader = new MetadataReader()

        when:
        def annotation = reader.getAnnotation(UberDocError).inClass(PodController.asType(GrailsClass))

        then:
        !annotation
    }

    void "getAnnotation for PodController and annotation Errors returns an instance"() {
        given:
        MetadataReader reader = new MetadataReader()

        when:
        def annotation = reader.getAnnotation(UberDocErrors).inClass(PodController.asType(GrailsClass))

        then:
        annotation
        3 == annotation.value().size()
    }

    void "getAnnotation for PodController and annotation UberDocController returns an instance"() {
        given:
        MetadataReader reader = new MetadataReader()

        when:
        def annotation = reader.getAnnotation(UberDocController).inClass(PodController.asType(GrailsClass))

        then:
        annotation
    }

    void "getAnnotation for OtherController and annotation UberDocController returns null"() {
        given:
        MetadataReader reader = new MetadataReader()

        when:
        def annotation = reader.getAnnotation(UberDocController).inClass(OtherController.asType(GrailsClass))

        then:
        !annotation
    }

}
