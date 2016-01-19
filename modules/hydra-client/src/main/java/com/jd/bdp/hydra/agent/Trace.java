package com.jd.bdp.hydra.agent;

import java.lang.annotation.*;

/**
 * @author:杨果
 * @date:16/1/15 下午4:38
 * <p/>
 * Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Trace {
}
