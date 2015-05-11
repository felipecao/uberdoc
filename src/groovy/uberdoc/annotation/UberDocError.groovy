package uberdoc.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Provides information about errors to be expected when interacting with a given resource / controller method. When
 * used at controller level, defines information about errors to be expected when interacting with every method
 * within that controller.
 */
@Target([ElementType.METHOD, ElementType.TYPE])
@Retention(RetentionPolicy.RUNTIME)
@interface UberDocError {

    /**
     * API internal error code, used to identify error situations within an API documentation. E.g.: UB-ERR-123
     */
    String errorCode()

    /**
     * HTTP error code associated with this error. E.g.: 404 (Not Found)
     */
    int httpCode()

    /**
     * Description of the error. May include information such as expected pre-conditions, how to recover from it, etc.
     */
    String description() default ""

}