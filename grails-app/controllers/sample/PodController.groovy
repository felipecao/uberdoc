package sample

import com.uberall.uberdoc.annotation.Error
import com.uberall.uberdoc.annotation.HeaderParam
import com.uberall.uberdoc.annotation.Resource

@Error(errorCode = "XYZ123", httpCode = 412, description = "this is a general 412 error, to be applied to all actions/resources in this file")
@Error(errorCode = "ABC456", httpCode = 400, description = "this is a general 409 error, to be applied to all actions/resources in this file")
@Error(errorCode = "CONF409", httpCode = 409, description = "all actions in this file may throw a 409 to indicate conflict")
@HeaderParam(name = "publicToken", sampleValue = "all methods in this file should send a header param", description = "this param should be sent within the headers")
@HeaderParam(name = "other token", sampleValue = "just some other token that every method should send", description = "this param should also be sent within the headers")
class PodController {

    @Resource(requestObject = Pod, responseCollectionOf = Pod, description = "this resource allows all Pods to be retrieved from DB")
    @Error(errorCode = "NF404", httpCode = 404, description = "returned if the resource does not exist")
    def get() { }

    @Resource(responseObject = Pod, description = "this resource allows all Pods to be retrieved from DB")
    def list() { }

    def create() { }

    def update() { }

    def delete() { }
}
