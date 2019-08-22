package cn.e3mall.sso.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRT}")
    private Integer SESSION_EXPIRT;

    @Override
    public E3Result login(String username,String password) {

        //判断当前用户登陆信息是否正确
        TbUserExample example=new TbUserExample();
        Criteria criteria =example.createCriteria();
        criteria.andUsernameEqualTo(username);
        //执行查询
        List<TbUser> list=userMapper.selectByExample(example);
        if(list==null&&list.size()==0){
            //返回登陆失败
            return E3Result.build(400,"用户名或密码错误");
        }
        //取用户信息
       TbUser user= list.get(0);
        //判断密码是否正确
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
            return E3Result.build(400,"用户名或密码错误");
        };
        //正确则生成token
        String token=UUID.randomUUID().toString();
        //把用户信息写入redis，key：token value：用户信息
        jedisClient.set("SESSION:"+token,JsonUtils.objectToJson(user));
        //设置session的过期时间
        jedisClient.expire("SESSION:"+token,SESSION_EXPIRT);
        //将token返回
        return  E3Result.ok(token);

    }
}
