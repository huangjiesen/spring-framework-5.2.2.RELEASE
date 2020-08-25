package com.ofwiki.test.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author HuangJS
 * @date 2020-08-25 10:11 上午
 */
@Aspect
@Component
public class ServicesAspect {
    @Pointcut("execution(* com.ofwiki.test.aspect.service..*.*(..))")
    public void proceed() { }


    @Around("com.ofwiki.test.aspect.ServicesAspect.proceed()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // start stopwatch
        Object retVal = pjp.proceed();

        System.out.println("around result:" + retVal);
        // stop stopwatch
        return retVal;
    }
}
