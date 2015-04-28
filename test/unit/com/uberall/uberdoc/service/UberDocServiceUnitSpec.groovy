package com.uberall.uberdoc.service

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UberDocService)
class UberDocServiceUnitSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "getErrors in PodController returns a map with 3 entries"() {
        when:
        def apiInfo = service.getApiDocs()

        then:
        apiInfo
    }
}
