package com.uberall.uberdoc.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Resource {

    String description() default ""

    Class requestObject() default null

    Class responseObject() default null

    Class responseCollectionOf() default null

    Class object() default null // if requestObject and responseObject are the same, just use this attribute as a shortcut

}