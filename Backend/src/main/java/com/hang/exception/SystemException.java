package com.hang.exception;

import com.hang.emuns.AppHttpCodeEnum;

/**
 * @ClassName SystemException
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/15 21:16
 * @Version 1.0
 */

public class SystemException extends RuntimeException{

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

}
