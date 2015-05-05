package com.uberall.uberdoc.annotation

@interface UberDocUriParam {

    String name()

    String description() default ""

    String sampleValue() default ""

}
