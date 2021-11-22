package com.mindata.superheroes.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerInterceptor {

    private final ThreadLocal<Long> startTime;

    public ControllerInterceptor() {
        this.startTime = new ThreadLocal<>();
    }

    @Around("execution(* com.mindata.superheroes.*..*(..))")
    public Object doAround(final ProceedingJoinPoint joinPoint) throws Throwable {
        final long start = System.currentTimeMillis();
        try {
            final Object result = joinPoint.proceed();
            if (result == null) {
                // Empty method without return type.
                return null;
            }

            final long end = System.currentTimeMillis();

            log.info("-------------------");
            log.info ("Class / Interface: {} ({}) - Method: {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getDeclaringType().toString().split(" ")[0], joinPoint.getSignature().getName());
            log.info("Full method: {}", joinPoint.getSignature().toLongString());
            log.info("Time consumed: {} ms.", end - start);

            return result;
        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            log.info("-------------------");
            log.info("Time consumed: {} ms with exception: {}", end - start, e.getMessage());
            throw e;
        }
    }

    @Before(value = "execution(* com.mindata.superheroes.controllers.*.*(..))")
    public void beforeMethod(final JoinPoint jp) {
        log.info("===================");
        startTime.set(System.currentTimeMillis());
    }

    @AfterReturning(value = "execution(* com.mindata.superheroes.controllers.*.*(..))")
    public void afterMethod(final JoinPoint jp) {
        log.info("-------------------");
        log.info ("Method {} - Total execution time: {} ms.", jp.getSignature().getName(), System.currentTimeMillis() - startTime.get());
    }

    @AfterThrowing(value = "execution(* com.mindata.superheroes.controllers.*.*(..))")
    public void afterMethodWithError(final JoinPoint jp) {
        log.info("-------------------");
        log.info ("Method {} - Total execution time: {} ms with exception.", jp.getSignature().getName(),
            System.currentTimeMillis() - startTime.get());
    }

}
