package com.uberall.uberdoc.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Resource {

    String description()

    Class requestObject()

    Class responseObject()

}