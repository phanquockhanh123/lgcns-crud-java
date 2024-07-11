package org.example.socialmediaspring.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.mapping.Join;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class Logging {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Logging.class);
    // setup logger
    private Logger logger = Logger.getLogger(getClass().getName());

    @Pointcut("execution(* org.example.socialmediaspring.controller.*.*(..))")
    private void forControllerPackage(){}

    @Pointcut("execution(* org.example.socialmediaspring.service.*.*(..))")
    private void forServicePackage(){}


    @Pointcut("forControllerPackage() || forServicePackage()")
    private void forAppFlow() {}

    // add @Before Device
    @Before("forAppFlow()")
    public void before(JoinPoint joinPoint) {
        // display method we are calling
        String theMethod = joinPoint.getSignature().toShortString();
        logger.info("=========> in @Before: calling method: " + theMethod);

        // display the arguments to the methods

        // get the arguments

        Object[] args = joinPoint.getArgs();

        // loop thru and display argument
        for(Object temArgs : args) {
            logger.info("================> Argument:  " + temArgs);
        }
    }

    /// add @AfterReturning
    @AfterReturning(pointcut = "forAppFlow()",
    returning = "theResult")
    public void afterReturning(JoinPoint joinPoint,Object theResult ) {
        // display method we are returning from
        String theMethod = joinPoint.getSignature().toShortString();
        logger.info("=========> in @AfterReturning: from method: " + theMethod);

        // display data returned
        logger.info("==========> result: " + theResult);
    }

}
