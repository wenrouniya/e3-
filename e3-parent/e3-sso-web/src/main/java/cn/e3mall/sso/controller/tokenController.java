package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class tokenController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping("/token/{token}")
    @ResponseBody
    public Object finUserByToken(String token,String callback){
     E3Result result =tokenService.getUserByToken(token);
     //响应结果之前 判断是否为空串
        if(StringUtils.isNotBlank(callback)){
            //将结果封装成一个json串
            MappingJacksonValue mappingJacksonValue=new MappingJacksonValue(result);
            return mappingJacksonValue;
        }
     return result;
    }

}
