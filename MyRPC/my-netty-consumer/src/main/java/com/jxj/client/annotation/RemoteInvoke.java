package com.jxj.client.annotation;

import java.lang.annotation.*;

/**
 * @Description
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RemoteInvoke {

}
