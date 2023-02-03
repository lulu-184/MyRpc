package com.jxj.netty.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Description
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Remote {
    String value() default "";
}
