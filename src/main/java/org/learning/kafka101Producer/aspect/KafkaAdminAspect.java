package org.learning.kafka101Producer.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class KafkaAdminAspect {
    @Around(value = "execution(* org.learning.kafka101Producer.configuration.KafkaAdminConfiguration.*(..))" +
            "|| execution(* org.learning.kafka101Producer.producer.LibraryEventProducer.*(..))")
    public Object logAroundAdminConfiguration(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = pjp.getSignature().getName();
        log.info("Execution begin for method {} and payload {}", methodName, pjp.getArgs());
        Object reference = pjp.proceed();
        log.info("Execution end for method {} and time taken in ms {}", methodName, System.currentTimeMillis() - startTime);
        return reference;
    }
}
