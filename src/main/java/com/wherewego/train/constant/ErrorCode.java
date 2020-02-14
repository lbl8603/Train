package com.wherewego.train.constant;

/**
 * 错误代码枚举类
 * @Author:lubeilin
 * @Date:Created in 19:52 2020/2/7
 * @Modified By:
 */
public enum ErrorCode {
    DownLoadError(411,"文件下载失败"),
    OcrFailed(412,"ocr识别失败"),
    UnKnowError(500,"未知异常"),
    ClientError(400,"客户端请求错误"),
    TooLargeFileError(414,"文件过大"),
    CreateQRCodeError(415,"生成二维码失败"),
    TokenError(416,"token错误"),
    CodeError(417,"code错误"),
    ParamError(418,"参数错误");
    private final int code;
    private final String msg;
    ErrorCode(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    public int getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }

}
