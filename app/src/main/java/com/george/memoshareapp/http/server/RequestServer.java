package com.george.memoshareapp.http.server;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.BodyType;

/**
 * @projectName: EasyHttp
 * @package: com.george.easyhttp.server
 * @className: RequestServer
 * @author: George
 * @description: TODO
 * @date: 2023/6/18 15:49
 * @version: 1.0
 */
public class RequestServer implements IRequestServer {

    @NonNull
    @Override
    public String getHost() {
//        return "http://82.156.138.114:6027/";
        return "http://192.168.1.7:6028/";
    }

    @NonNull
    @Override
    public BodyType getBodyType() {
        // 参数以 Json 格式提交（默认是表单）
        return BodyType.JSON;
    }
}