package sample

import com.uberall.uberdoc.annotation.Error
import com.uberall.uberdoc.annotation.Errors
import com.uberall.uberdoc.annotation.HeaderParam
import com.uberall.uberdoc.annotation.HeaderParams
import com.uberall.uberdoc.annotation.QueryParam
import com.uberall.uberdoc.annotation.Resource

@Errors([
        @Error(errorCode = "XYZ123", httpCode = 412, description = "this is a general 412 error, to be applied to all actions/resources in this file"),
        @Error(errorCode = "ABC456", httpCode = 400, description = "this is a general 409 error, to be applied to all actions/resources in this file"),
        @Error(errorCode = "CONF409", httpCode = 409, description = "all actions in this file may throw a 409 to indicate conflict")
])
@HeaderParams([
        @HeaderParam(name = "publicToken", sampleValue = "all methods in this file should send a header param", description = "this param should be sent within the headers"),
        @HeaderParam(name = "other token", sampleValue = "just some other token that every method should send", description = "this param should also be sent within the headers")
])
class PodController { // this example simulates a CRUD-ish controller

    @Resource(requestObject = Pod, responseCollectionOf = Pod, description = "this resource allows all Pods to be retrieved from DB")
    @Error(errorCode = "NF404", httpCode = 404, description = "returned if the resource does not exist")
    @QueryParam(name = "id", description = "the id of the Pod to be retrieved from DB", sampleValue = "4")
    def get() { }

    @Resource(responseObject = Pod, description = "this resource allows all Pods to be retrieved from DB")
    def list() { }

    @Resource(object = Pod, description = "this resource creates Pods")
    @HeaderParam(name = "some header param", sampleValue = "hdr", description = "this is just something else to be sent on creation")
    def create() { }

    @Resource(object = Pod)
    @QueryParam(name = "id", description = "the id of the Pod to be retrieved from DB", sampleValue = "4")
    def update() { }

    @QueryParam(name = "id", description = "the id of the Pod to be retrieved from DB", sampleValue = "4")
    def delete() { }
}
