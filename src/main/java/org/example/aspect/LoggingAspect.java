package org.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Around("within(org.example.controller..*)")
    public Object logController(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String method = sig.getDeclaringType().getSimpleName() + "." + sig.getName();
        Object[] args = pjp.getArgs();
        log.info("Enter {} args={}", method, args);
        long start = System.currentTimeMillis();
        try {
            Object result = pjp.proceed();
            long took = System.currentTimeMillis() - start;
            log.info("Exit {} resultType={} took={}ms", method, result != null ? result.getClass().getSimpleName() : "null", took);
            return result;
        } catch (Throwable t) {
            log.error("Exception in {}: {}", method, t.toString());
            throw t;
        }
    }
}
