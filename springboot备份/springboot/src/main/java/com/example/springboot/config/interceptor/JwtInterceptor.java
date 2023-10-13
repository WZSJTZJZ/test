package com.example.springboot.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import com.example.springboot.common.Constants;
import com.example.springboot.config.AuthAccess;
import com.example.springboot.entity.Admin;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private IAdminService adminService;
//    @Autowired
//    private IUsersService usersService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");



        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){

            return true;
        }else{
            HandlerMethod h = (HandlerMethod) handler;
            AuthAccess authAccess = h.getMethodAnnotation(AuthAccess.class);
            if (authAccess != null){
                return true ;
            }
        }
        // 执行认证
        if (StrUtil.isBlank(token)) {
            throw new ServiceException(Constants.CODE_401, "无token，请重新登录");
        }
        // 获取 token 中的 user id
        String adminId;
        try {
            adminId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
//        String usersId;
//
//        try {
//            usersId = JWT.decode(token).getAudience().get(0);
//        } catch (JWTDecodeException j) {
//            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
//        }
        // 根据token中的userid查询数据库
        Admin admin = adminService.getById(adminId);
        if (admin == null) {
            throw new ServiceException(Constants.CODE_401, "用户不存在，请重新登录");
        }

//        Users users = usersService.getById(usersId);
//        if (users == null) {
//            throw new ServiceException(Constants.CODE_401, "用户不存在，请重新登录");
//        }
        // 用户密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(admin.getPassword())).build();
        try {
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
        return true;


    }
}
