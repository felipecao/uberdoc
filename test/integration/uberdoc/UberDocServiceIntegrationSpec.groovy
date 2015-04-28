package uberdoc

import com.uberall.uberdoc.service.UberDocService
import grails.test.spock.IntegrationSpec

class UberDocServiceIntegrationSpec extends IntegrationSpec {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        given:
        UberDocService service = new UberDocService()

        when:
        def m = service.apiDocs

        then:
        m
    }
}
