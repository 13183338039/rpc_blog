package com.markerhub.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {
    String operateModule();    // 模块名
    String operateType();      // 操作类型，如 GET、POST、DELETE、PUT
    String operateDesc();      // 操作描述
}
