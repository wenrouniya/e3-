package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Value("${COOKIE_KEY}")
    private  String COOKIE_KEY;

    @RequestMapping("/page/login")
    public String registerShow(){
        return "login";
    }

    @RequestMapping(value="/user/login",method=RequestMethod.POST)
    @ResponseBody
    public E3Result login(String username, String password,
                          HttpServletRequest request, HttpServletResponse response){
    E3Result e3Result=loginService.login(username,password);
    //判断是否登陆成功
        if(e3Result.getStatus()==200){
            String token=e3Result.getData().toString();
            //如果登陆成功 将token写入cookie
            CookieUtils.setCookie(request,response,COOKIE_KEY,token);
        }
        return  e3Result;
    }
}
