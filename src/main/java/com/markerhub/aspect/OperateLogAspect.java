package com.markerhub.aspect;


import com.markerhub.annotation.OperateLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class OperateLogAspect {

    @Around("@annotation(com.markerhub.annotation.OperateLog)")
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        System.out.println("sb");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperateLog operateLog = method.getAnnotation(OperateLog.class);

        String module = operateLog.operateModule();
        String type = operateLog.operateType();
        String desc = operateLog.operateDesc();

        Object[] args = joinPoint.getArgs();
        log.info("【操作日志开始】模块: {}，类型: {}，描述: {}", module, type, desc);
        log.info("请求参数: {}", Arrays.toString(args));
        Object result;
        try {
            result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("响应结果: {}", result);
            log.info("耗时: {} ms", duration);
        } catch (Exception e) {
            log.error("执行异常: {}", e.getMessage(), e);
            throw e;
        }

        log.info("【操作日志结束】\n");
        return result;
    }
}
