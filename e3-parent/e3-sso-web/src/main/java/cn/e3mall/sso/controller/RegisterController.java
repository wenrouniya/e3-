package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {


    @Autowired
    private RegisterService registerService;


    @RequestMapping("/page/register")
    public  String registerShow(){
        return "register";
    }

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkDate(@PathVariable String param,@PathVariable Integer type){
       E3Result e3Result= registerService.checkData(param,type);
        return e3Result;
    }

    @RequestMapping(value="/user/register",method =RequestMethod.POST)
    @ResponseBody
    public  E3Result register(TbUser user){
       E3Result result= registerService.regiter(user);
       return  result;
    }

}
