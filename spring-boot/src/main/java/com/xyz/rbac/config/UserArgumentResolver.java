package com.xyz.rbac.config;

import com.xyz.rbac.data.domain.User;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        System.out.println("UserArgumentResolver "+clazz);
        return clazz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        User user = (User) nativeWebRequest.getAttribute(User.REQUEST_CONTEXT_USER, RequestAttributes.SCOPE_REQUEST);
        System.out.println("resolveArgument "+user);
        if (user != null) {
            return user;
        }
        return null;
    }
}
