package com.uberall.uberdoc.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Resource {

    String description() default ""

    Class requestObject() default {}

    Class responseObject() default {}

    Class responseCollectionOf() default {}

    Class object() default {} // if requestObject and responseObject are the same, just use this attribute as a shortcut

}