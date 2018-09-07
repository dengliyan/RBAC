package com.xyz.rbac.config;

import com.xyz.rbac.cache.keys.UserKey;
import com.xyz.rbac.cache.redis.RedisService;
import com.xyz.rbac.data.domain.User;
import com.xyz.rbac.exception.BusinessException;
import com.xyz.rbac.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    RedisService redisService;

    private String getCookieValue(HttpServletRequest request,String key){
        Cookie[] cookies= request.getCookies();
        if(cookies!=null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //设置当前的目录
        String url = httpServletRequest.getRequestURI();
        System.out.println(url);
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        LoginRequired methodAnnotation = method.getAnnotation(LoginRequired.class);
        if(methodAnnotation==null) {
            methodAnnotation = method.getDeclaringClass().getAnnotation(LoginRequired.class);
        }
        if (methodAnnotation != null) {
            //进行验证
            //读取cookies或request header中的值
            String paramToken=httpServletRequest.getParameter(User.COOK_NAME_TOKEN);
            String cookieToken=getCookieValue(httpServletRequest,User.COOK_NAME_TOKEN);
            if(StringUtils.isEmpty(paramToken)&&StringUtils.isEmpty(cookieToken)) {
                throw new BusinessException(Result.USER_NOT_LOGIN);//无token，则认为失效
            }
            String token=!(StringUtils.isEmpty(paramToken))?paramToken:cookieToken;
            //读取当前用户的信息
            //根据token读取用户的缓存
            User user=redisService.get(UserKey.TOKEN,token,User.class);
            if(user==null) {
                throw new BusinessException(Result.USER_TOKEN_ILLEGAL);//无token，则认为失效
            }
            redisService.set(UserKey.TOKEN,token,user);//更新过期时间
            //设置用户
            httpServletRequest.setAttribute(User.REQUEST_CONTEXT_USER, user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
