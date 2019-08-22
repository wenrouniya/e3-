package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

public interface TokenService {
    //在redis中取token
    E3Result getUserByToken(String token);
}
