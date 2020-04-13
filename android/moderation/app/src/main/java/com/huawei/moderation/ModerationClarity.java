package com.huawei.moderation;

import android.graphics.Bitmap;

import com.alibaba.fastjson.JSONObject;
import com.huawei.moderation.utils.AccessService;
import com.huawei.moderation.utils.HttpClientUtils;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ModerationClarity {
    private AccessService service;

    public ModerationClarity(AccessService service) {
        this.service = service;
    }

    public void clarityDetectDemo(Bitmap bit, Callback callback) throws Exception {

        // 构建访问需要的参数
        String uri = "/v1.0/moderation/image/clarity-detect";
        String fileBase64Str = HttpClientUtils.BitmapStrByBase64(bit);

        // api请求参数说明可参考：https://support.huaweicloud.com/api-moderation/moderation_03_0024.html
        JSONObject json = new JSONObject();
        json.put("image", fileBase64Str); //检测图片
        json.put("threshold", 0.8); //判断是否清晰的阈值

        // 传入清晰度检测服务对应的uri参数, 传入清晰度检测服务需要的参数，
        Call call = service.post(uri, json.toJSONString());
        call.enqueue(callback);
    }

    public void clarityDetectDemo(String url, Callback callback) throws Exception {
        // 2.构建访问图像内容检测服务需要的参数
        String uri = "/v1.0/moderation/image/clarity-detect";
        // api请求参数说明可参考：https://support.huaweicloud.com/api-moderation/moderation_03_0024.html
        JSONObject json = new JSONObject();
        json.put("url", url); //检测图片
        json.put("threshold", 0.8); //判断是否清晰的阈值

        // 3.传入清晰度检测服务对应的uri参数, 传入清晰度检测服务需要的参数，
        // 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
        Call call =  service.post(uri, json.toJSONString());
        call.enqueue(callback);
    }
}
