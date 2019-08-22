package cn.e3mall.sso.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper userMapper;
    @Override
    public E3Result checkData(String param, int type) {
    //根据不同type生成不同的查询条件
        TbUserExample example =new TbUserExample();
      Criteria criteria =example.createCriteria();

      //1.用户名2. 手机号3.邮箱

        if(type==1){
            criteria.andUsernameEqualTo(param);
        }else if(type ==2){
            criteria.andPhoneEqualTo(param);
        }else if(type ==3){
            criteria.andEmailEqualTo(param);

        }else{
            return E3Result.build(400,"数据类型出错");
        }

        //执行查询
        List<TbUser> list=userMapper.selectByExample(example);
        //如果有数据说明数据库中已经存在  注册失败
        if(list!=null&&list.size()>0) {
            return E3Result.ok(false);
        }
        //如果没有返回成功
        return  E3Result.ok(true);

    }

    @Override
    public E3Result regiter(TbUser user) {
        //对数据进行校验
        if("".equals(user.getUsername())||"".equals(user.getPassword())||"".equals(user.getPhone())){
            return E3Result.build(400,"用户数据不完整注册失败");
        }
        //1.用户名 2.手机号 3.邮箱
        E3Result result=checkData(user.getUsername(),1);
        if(!(boolean)result.getData()){
            return E3Result.build(400,"此用户名已经被占用");
        }
//        result=checkData(user.getEmail(),3);
////        if(!(boolean)result.getData()){
////            return E3Result.build(400,"此邮箱已经被占用");
////        }
         result=checkData(user.getPhone(),2);
        if(!(boolean)result.getData()){
            return E3Result.build(400,"此手机号已经被占用");
        }
        //补全数据
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //对密码进行md5加密
        String md5Pass=DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //将数据插入到数据库中
        userMapper.insert(user);
        //返回添加成功
        return E3Result.ok();

    }
}
