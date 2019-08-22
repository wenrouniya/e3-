package cn.e3mall.search.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GlobalErrorHandler implements HandlerExceptionResolver {

    private static final Logger logger=LoggerFactory.getLogger(GlobalErrorHandler.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        //打印控制台
        ex.printStackTrace();
        //写日志
        logger.debug("测试日志输出的异常");
        logger.info("系统发生异常....");
        logger.error("系统异常",ex);
        //发邮件发短信
        //返回错误友好页面
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("error/exception");

        return modelAndView;


    }
}
