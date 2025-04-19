package com.quantum.logs.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.quantum.logs.config.QuantumLogAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * When added to the Spring Boot Class It prints Logs for an api request which
 * include logs for Response and Request Headers and the Request Body.
 * If you want a certain method to be exempt from this log you can simply
 * add `@ExcludeFromLog` to that method
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(QuantumLogAutoConfiguration.class)
public @interface EnableQuantumLog {
    /**
     * When Enabled The Logs are printed for each and every write except for the
     * ones with ExcludeFromLog.
     * When disabled Logs are only printed once for an endpoint showing its details
     *
     */
    boolean printAll() default false;

    /**
     * When Enabled The Quantum Logs are printed for the environment
     */
    boolean enabled() default true;

    /**
     * When Enabled cURL for the endPoints are printed
     */
    boolean enableCurls() default false;
}
