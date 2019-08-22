package cn.e3mall.sso.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class TokenServiceImpl implements TokenService
{

    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRT}")
    private  Integer SESSION_EXPIRT;

    @Override
    public E3Result getUserByToken(String token) {
        //根据token取redis中用户的信息
        String json=jedisClient.get("SESSION:"+token);
        //取不到用户信息 登陆已经过期 返回登陆过期
        if(StringUtils.isBlank(json)){
            return E3Result.build(201,"用户登陆已过期");
        }
        //取到用户信息  更新token的过期时间
        jedisClient.expire("SESSION:"+token,SESSION_EXPIRT);
        //返回结果 其中包含user
        TbUser user=JsonUtils.jsonToPojo(json,TbUser.class);
        return  E3Result.ok(user);

    }
}
