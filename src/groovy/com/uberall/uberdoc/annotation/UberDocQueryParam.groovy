package com.uberall.uberdoc.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface UberDocQueryParam {

    String name()

    String description() default ""

    boolean required() default true

    String sampleValue() default ""

    boolean isCollection() default false

}
