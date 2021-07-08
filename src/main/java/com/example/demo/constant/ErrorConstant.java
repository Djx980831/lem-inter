package com.example.demo.constant;

import com.example.demo.util.ErrorInfo;

/**
 * @version v1.0
 * @ProjectName: lem-inter
 * @Author: jingxiong.dong
 * @Date: 2021/7/7 13:57
 */
public class ErrorConstant {

    private ErrorConstant(){};

    public static final Integer SUCCESS_CODE = 0;

    /*
    user错误代码
    */
    public static final ErrorInfo CODE_ERROR = new ErrorInfo(100, "验证码错误，请重新输入");

    public static final ErrorInfo PARAM_ERROR = new ErrorInfo(101, "邮箱格式错误，请重新输入");

    public static final ErrorInfo EMAIL_IS_EXIST = new ErrorInfo(102, "邮箱已存在，请直接登陆");

    public static final ErrorInfo EMAIL_IS_EMPTY = new ErrorInfo(103, "邮箱为空，请输入邮箱");

    public static final ErrorInfo PASSWORD_IS_EMPTY = new ErrorInfo(104, "密码为空，请输入密码");

    public static final ErrorInfo LOGIN_IS_ERROR = new ErrorInfo(105, "邮箱或密码错误，请重新输入");

    public static final ErrorInfo USER_NOT_EXIST = new ErrorInfo(106, "邮箱未注册，请前往注册");

    public static final ErrorInfo CODE_IS_EXIST = new ErrorInfo(107, "验证码十分钟内请勿重复发送");

    public static final ErrorInfo CODE_IS_EMPTY = new ErrorInfo(107, "验证码为空，请输入验证码");
}
