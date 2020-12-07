package org.paasta.container.platform.api.common;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

/**
 * Admin Check Aspect 클래스
 * AOP - Admin check
 *
 * @author hrjin
 * @version 1.0
 * @since 2020-08-25
 **/
@Aspect
@Component
@Order(0)
public class AdminCheckAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandler.class);
    private static final String IS_ADMIN = "isAdmin";


    /**
     * API URL 호출 시 로그인한 사용자 정보로 admin/user 판별 (check that login user is admin or user)
     *
     * true/false 를 argument 안에 파라미터로 넣어줌
     * isAdmin 으로 판별해서 true 면 admin 서비스 호출
     *
     * @param joinPoint the joinPoint
     * @return the object
     * @throws Throwable
     */
    @Around("execution(* org.paasta.container.platform.api..*Controller.*(..))" + "&& !@annotation(org.paasta.container.platform.api.config.NoAuth)")
    public Object isAdminAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] parameterValues = Arrays.asList(joinPoint.getArgs()).toArray();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> list = (List<GrantedAuthority>) authentication.getAuthorities();
        LOGGER.info("YOUR AUTHORITY :: {}", list.get(0).getAuthority());
        String authority = list.get(0).getAuthority();

        boolean isAdmin = false;

        if(Constants.AUTH_CLUSTER_ADMIN.equals(authority)) {
            isAdmin = true;
        }

        CodeSignature methodSignature = (CodeSignature) joinPoint.getSignature();
        String[] sigParamNames = methodSignature.getParameterNames();

        int index = 0;
        for (String name:sigParamNames) {
            if (IS_ADMIN.equals(name)) {
                break;
            }

            index++;
        }

        parameterValues = CommonUtils.modifyValue(parameterValues, index, isAdmin);
        return joinPoint.proceed(parameterValues);
    }

}
