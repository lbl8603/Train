package com.wherewego.train.exception;

/**
 * 内部异常
 * @Author:lubeilin
 * @Date:Created in 19:21 2020/2/7
 * @Modified By:
 */
public class WhereWeGoException extends RuntimeException {
    private int code;
    public WhereWeGoException(int code,String msg){
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
