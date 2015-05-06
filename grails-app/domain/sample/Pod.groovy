package sample

import com.uberall.uberdoc.annotation.UberDocModel
import com.uberall.uberdoc.annotation.UberDocProperty

@UberDocModel(description = "This class does something...")
class Pod {

    @UberDocProperty(description = "license is used for ...", sampleValue = "DBNG3r")
    String license

    static hasOne = [jedi: Person]

    static constraints = {
        license blank: true, nullable: false
    }
}
