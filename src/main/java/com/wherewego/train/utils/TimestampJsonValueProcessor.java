package com.wherewego.train.utils;

import net.sf.json.JSONNull;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * JSON转换处理，用于将时间格式化
 * @Author:lubeilin
 * @Date:Created in 22:52 2020/2/8
 * @Modified By:
 */
public class TimestampJsonValueProcessor implements JsonValueProcessor {
    private String format = "yyyy-MM-dd HH:mm:ss";
    public TimestampJsonValueProcessor(){

    }
    public TimestampJsonValueProcessor(String format){
        this.format = format;
    }
    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        if(value instanceof Timestamp[]){
            Timestamp[] timestamps = (Timestamp[]) value;
            String[] strs = new String[timestamps.length];

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i=0;i<strs.length;i++){
                strs[i] = timestamps[i]==null?null:simpleDateFormat.format(timestamps[i]);
            }
            return strs;
        }
        return value;
    }

    @Override
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        if(value==null){
            return JSONNull.getInstance();
        }
        if(value instanceof Timestamp){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(value);
        }
        return value;
    }
}
