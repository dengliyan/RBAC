package com.xyz.rbac.exception;

import com.alibaba.fastjson.JSON;
import com.xyz.rbac.config.LoginRequired;
import com.xyz.rbac.result.JSONResult;
import com.xyz.rbac.result.Result;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {


    private static boolean isResponseBodyAnnotation(HandlerMethod handler) {
        Method method = handler.getMethod();
        return method.getAnnotation(ResponseBody.class) != null
                || method.getDeclaringClass().getAnnotation(ResponseBody.class) != null;
    }

    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(HttpServletRequest request, HttpServletResponse response,Model model,  Exception exception,
                                   HandlerMethod handler )  throws Exception {

        System.out.println("exceptionHandler---------------------------------------");
        exception.printStackTrace();
        response.setCharacterEncoding("UTF-8");
        JSONResult result = JSONResult.error(Result.SYSTEM_ERROR);
        if (exception instanceof BusinessException) {
            BusinessException ex = (BusinessException) exception;
            result = JSONResult.error(ex.getResult());
        } else if (exception instanceof BindException) {
            BindException ex = (BindException) exception;
            List<ObjectError> errors = ex.getAllErrors();
            List<FieldError> fieldErrors = ex.getFieldErrors();
            //FieldError error = fieldErrors.get(0);
            ObjectError error = errors.get(0);
            result = JSONResult.error(Result.BIND_EXCEPTION.format(error.getDefaultMessage()));
        } else if (exception instanceof ValidationException) {
            ValidationException ex = (ValidationException) exception;
            result = JSONResult.error(Result.VALIDATION_EXCEPTION);
        }

        if (!(

                        request.getHeader("accept").contains("application/json") ||
                        (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").contains("XMLHttpRequest"))||
                        isResponseBodyAnnotation(handler)
        )) {
            //如果是没有登录，则跳回至登录页面
            if (result.getRet() == Result.USER_NOT_LOGIN.getRet()
                    || result.getRet() == Result.USER_TOKEN_ILLEGAL.getRet()) {
                return "redirect:/login?returnUrl=" + URLEncoder.encode(request.getRequestURI(), "utf-8");
            }
            response.setStatus(500);
            model.addAttribute("error", result.getMsg());
            return "error";
        }

        System.out.println("json---------------------------------------");
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        OutputStream out = response.getOutputStream();
        String val = JSON.toJSONString(result);
        out.write(val.getBytes(Charset.forName("utf-8")));
        out.flush();
        return null;
    }
}
