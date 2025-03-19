package auth;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RoleCheckAspect {

	@Around("@annotation(auth.CheckRole)")
	public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable {
		// 현재 요청에서 HttpServletRequest 가져오기
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			throw new RuntimeException("No request found");
		}
		HttpServletRequest request = attributes.getRequest();

		// HTTP 헤더에서 역할 가져오기 ("X-User-Role")
		String userRole = request.getHeader("X-User-Role");
		if (userRole == null) {
			throw new RuntimeException("No user role found in request headers");
		}

		// 어노테이션에서 필요한 역할 가져오기
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		CheckRole checkRole = method.getAnnotation(CheckRole.class);
		Role[] allowedRoles = checkRole.value();

		// 요청한 역할이 허용된 역할 중 하나인지 검사
		boolean hasAccess = Arrays.stream(allowedRoles)
			.anyMatch(role -> role.name().equals(userRole));

		if (!hasAccess) {
			throw new RuntimeException("Permission denied: Allowed roles are " + Arrays.toString(allowedRoles));
		}

		// 권한이 맞으면 원래 메서드 실행
		return joinPoint.proceed();
	}
}
