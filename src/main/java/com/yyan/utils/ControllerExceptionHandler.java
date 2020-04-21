package com.yyan.utils;//package com.yyan.utils;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 控制器错误处理器，从控制器抛出的异常被它拦截。
// * 可以在此处封装错误信息，以友好的方式返回给前端
// */
//
//@ControllerAdvice
//public class ControllerExceptionHandler {
//
//    /**
//     * 处理ServiceException
//     * @return
//     */
//    @ExceptionHandler(ServiceException.class)
//    @ResponseBody
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Map<String,Object> HandlerServiceException(ServiceException e){
//        Map<String,Object> errorMessage=new HashMap<>();
//        errorMessage.put("code",e.getCode());
//        errorMessage.put("desc",e.getDesc());
//        return errorMessage;
//    }
//
//}