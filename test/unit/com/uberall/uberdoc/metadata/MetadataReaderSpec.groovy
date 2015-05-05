package com.uberall.uberdoc.metadata

import com.uberall.uberdoc.annotation.UberDocErrors
import sample.PodController
import spock.lang.Specification
import com.uberall.uberdoc.annotation.UberDocError

class MetadataReaderSpec extends Specification {

    void "getAnnotation for PodController and annotation Error returns no instances"() {
        given:
        MetadataReader reader = new MetadataReader()

        when:
        def annotation = reader.getAnnotation(UberDocError).inClass(PodController.class)

        then:
        !annotation
    }

    void "getAnnotation for PodController and annotation Errors returns an instance"() {
        given:
        MetadataReader reader = new MetadataReader()

        when:
        def annotation = reader.getAnnotation(UberDocErrors).inClass(PodController.class)

        then:
        annotation
        3 == annotation.value().size()
    }

}
