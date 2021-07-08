package com.example.demo.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.annotation.UserLoginToken;
import com.example.demo.entity.User;
import com.example.demo.service.MailService;
import com.example.demo.service.RedisService;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserService;
import com.example.demo.util.*;
import com.example.demo.verification.VertifyCodeUtils;
import com.example.demo.vo.EmailVO;
import com.example.demo.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.example.demo.constant.ErrorConstant.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisService redisService;

    private String validate_code = null;

    @PostMapping("/login")
    public RpcResponse<Object> login(String email, String password, HttpServletResponse response) throws NoSuchAlgorithmException {
        if (email == null || "".equals(email)) {
            return RpcResponse.error(EMAIL_IS_EMPTY);
        }
        if (password == null || "".equals(password)) {
            return RpcResponse.error(PASSWORD_IS_EMPTY);
        }
        JSONObject jsonObject = new JSONObject();
        User user = new User();
        user.setEmail(email);
        user.setPassword(Md5.getMd5(password));
        UserVO userForBase = userService.login(user);
        if (userForBase == null) {
            return RpcResponse.error(LOGIN_IS_ERROR);
        } else {
            String token = tokenService.getToken(userForBase);
            jsonObject.put("token", token);

            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            response.addCookie(cookie);

            return RpcResponse.success(jsonObject);
        }
    }

    @PostMapping("/logout")
    public RpcResponse<String> logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String tokenStr = "";
        for (int i = 0; i < cookies.length; i++) {
            Cookie cook = cookies[i];
            if (cook.getName().equalsIgnoreCase("token")) { //获取键
                tokenStr = cook.getValue().toString();    //获取值
            }
        }
        session.invalidate();
        Cookie token = new Cookie("token", tokenStr);
        token.setMaxAge(0);
        token.setPath("/");
        response.addCookie(token);
        return RpcResponse.success("success");
    }

    // 生成验证码
    @GetMapping("/getVertifyCodeImage")
    public RpcResponse<String> getImageCode(HttpServletRequest request) throws IOException {
        // 1. 使用工具类生成验证码
        String code = VertifyCodeUtils.generateVerifyCode(4);
        validate_code = code; // 存放生成的验证码
        // 2. 将验证码放入ServletContext作用域
        request.getServletContext().setAttribute("code", code);
        // 3. 将图片转换成base64格式
        // 字节数组输出流在内存中创建一个字节数组缓冲区，所有发送到输出流的数据保存在该字节数组缓冲区中
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //将得到的验证码，使用工具类生成验证码图片，并放入到字节数组缓存区
        VertifyCodeUtils.outputImage(220,60,byteArrayOutputStream,code);
        //使用spring提供的工具类，将字节缓存数组中的验证码图片流转换成Base64的形式
        //并返回给浏览器
        return RpcResponse.success("data:image/png;base64," + Base64Utils.encodeToString(byteArrayOutputStream.toByteArray()));
    }

    @RequestMapping("/registUser")
    public RpcResponse<String> registUser(@RequestBody JSONObject data) throws JSONException, NoSuchAlgorithmException {

        //System.out.println(data.toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject = data;

        String email = jsonObject.getString("email");
        if (userService.getUserByEmail(email) != null) {
            return RpcResponse.error(EMAIL_IS_EXIST);
        }
        String password = jsonObject.getString("password");
        String validate_code_input = jsonObject.getString("vertify_code");

        System.out.println("validate_code: " + validate_code);
        System.out.println("validate_code_input: " + validate_code_input);

        if (validate_code_input.equals(validate_code)) {
            // 验证成功，注册用户
            String code = redisService.getCode(email);
            if (!code.equals(redisService.getCode(email))) {
                return RpcResponse.error(CODE_ERROR);
            }
            User user = new User();
            user.setEmail(email);
            user.setPassword(Md5.getMd5(password));
            userService.registUser(user);
            return RpcResponse.success(email);
        } else { // 验证失败
            return RpcResponse.error(CODE_ERROR);
        }
    }

    @PostMapping("/sendMail")
    public RpcResponse<EmailVO> sendMail(String email) {
        UserVO userVO = userService.getUserByEmail(email);
        if (userVO == null) {
            return RpcResponse.error(USER_NOT_EXIST);
        }
        String code = RandomCode.getSomeString();
        if (redisService.hasKsy(email)) {
            return RpcResponse.error(CODE_IS_EXIST);
        }
        redisService.addCode(email, code);
        mailService.sandSimpleMail(email, code);
        EmailVO vo = new EmailVO();
        vo.setEmail(email);
        vo.setCode(code);
        return RpcResponse.success(vo);
    }

    @PostMapping("/reSetPassword")
    public RpcResponse<String> reSetPassword(String email, String password, String code) throws NoSuchAlgorithmException {
        if (email == null || "".equals(email)) {
            return RpcResponse.error(EMAIL_IS_EMPTY);
        }
        if (userService.getUserByEmail(email) == null) {
            return RpcResponse.error(USER_NOT_EXIST);
        }
        if (password == null || "".equals(password)) {
            return RpcResponse.error(PASSWORD_IS_EMPTY);
        }
        if (code == null || "".equals(code)) {
            return RpcResponse.error(CODE_IS_EMPTY);
        }
        if (!code.equals(redisService.getCode(email))) {
            return RpcResponse.error(CODE_ERROR);
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(Md5.getMd5(password));
        userService.updatePasswordByEmail(user);

        return RpcResponse.success(email);
    }

    /***
     * 这个请求需要验证token才能访问
     * @return String 返回类型
     */
    @UserLoginToken
    @GetMapping("/getMessage")
    public RpcResponse<String> getMessage() {
        // 取出token中带的用户id 进行操作
        System.out.println(TokenUtil.getTokenUserId());
        return RpcResponse.success(TokenUtil.getTokenUserId());
    }
}
