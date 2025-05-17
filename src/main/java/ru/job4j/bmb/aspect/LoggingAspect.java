package ru.job4j.bmb.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

		@Pointcut("execution(* ru.job4j.bmb.services.*.*(..))")
		private void serviceLayer() {
		}

		@Before("serviceLayer()")
		public void logBefore(JoinPoint joinPoint) {
				System.out.println(
						"Вызов метода: " + joinPoint.getTarget().getClass().getSimpleName()
								+ "." + joinPoint.getSignature().getName()
								+ "(" + (joinPoint.getArgs().length > 0 ? Arrays.toString(joinPoint.getArgs()) : "") + ")");
		}
}
