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
        // Extract method signature information for clearer log messages
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String method = sig.getDeclaringType().getSimpleName() + "." + sig.getName();

        // Capture method arguments (may be empty)
        Object[] args = pjp.getArgs();

        // Log method entry and arguments. Arrays or complex objects will be
        // rendered by the logging framework (be cautious with sensitive data).
        log.info("Enter {} args={}", method, args);

        // Start timing the method execution
        long start = System.currentTimeMillis();
        try {
            // Proceed with the original controller method
            Object result = pjp.proceed();

            // Compute elapsed time and log exit with the result type and duration
            long took = System.currentTimeMillis() - start;
            log.info("Exit {} resultType={} took={}ms", method,
                    result != null ? result.getClass().getSimpleName() : "null",
                    took);
            return result;
        } catch (Throwable t) {
            // Log the exception with the method context, then rethrow to preserve behavior
            log.error("Exception in {}: {}", method, t.toString());
            throw t;
        }
    }
}
