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

    private String validate_code = "";

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
            jsonObject.put("user", userForBase);

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
            if (cook.getName().equalsIgnoreCase("token")) { //?????????
                tokenStr = cook.getValue().toString();    //?????????
            }
        }
        session.invalidate();
        Cookie token = new Cookie("token", tokenStr);
        token.setMaxAge(0);
        token.setPath("/");
        response.addCookie(token);
        return RpcResponse.success("success");
    }

    // ???????????????
    @GetMapping("/getVertifyCodeImage")
    public RpcResponse<String> getImageCode(HttpServletRequest request) throws IOException {
        // 1. ??????????????????????????????
        String code = VertifyCodeUtils.generateVerifyCode(4);
        validate_code = code; // ????????????????????????
        // 2. ??????????????????ServletContext?????????
        request.getServletContext().setAttribute("code", code);
        // 3. ??????????????????base64??????
        // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //????????????????????????????????????????????????????????????????????????????????????????????????
        VertifyCodeUtils.outputImage(220,60,byteArrayOutputStream,code);
        //??????spring???????????????????????????????????????????????????????????????????????????Base64?????????
        //?????????????????????
        return RpcResponse.success("data:image/png;base64," + Base64Utils.encodeToString(byteArrayOutputStream.toByteArray()));
    }

    @PostMapping("/registUser")
    public RpcResponse<String> registUser(String email, String password, String code) throws NoSuchAlgorithmException {
        if (email == null || "".equals(email)) {
            return RpcResponse.error(EMAIL_IS_EMPTY);
        }
        if (password == null || "".equals(password)) {
            return RpcResponse.error(PASSWORD_IS_EMPTY);
        }
        if (code == null || "".equals(code)) {
            return RpcResponse.error(CODE_IS_EMPTY);
        }
        if (userService.getUserByEmail(email) != null) {
            return RpcResponse.error(EMAIL_IS_EXIST);
        }
        if (code.equals(validate_code)) {
            // ???????????????????????????
//            if (!emailCode.equals(redisService.getCode(email))) {
//                return RpcResponse.error(CODE_ERROR);
//            }
            User user = new User();
            user.setEmail(email);
            user.setPassword(Md5.getMd5(password));
            return RpcResponse.success(userService.registUser(user));
        } else { // ????????????
            return RpcResponse.error(CODE_ERROR);
        }
    }

    @PostMapping("/sendMail")
    public RpcResponse<EmailVO> sendMail(String email, int type) {
        UserVO userVO = userService.getUserByEmail(email);
        if (userVO == null) {
            return RpcResponse.error(USER_NOT_EXIST);
        }
        if (type == 1) {
            email = "regist" + email;
        } else {
            email = "resetPassword" + email;
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
     * ????????????????????????token????????????
     * @return String ????????????
     */
    @UserLoginToken
    @GetMapping("/getMessage")
    public RpcResponse<String> getMessage() {
        // ??????token???????????????id ????????????
        System.out.println(TokenUtil.getTokenUserId());
        return RpcResponse.success(TokenUtil.getTokenUserId());
    }
}
