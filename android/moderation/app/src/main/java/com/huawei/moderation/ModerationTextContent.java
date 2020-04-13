package com.huawei.moderation;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.moderation.utils.AccessService;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ModerationTextContent {
    private AccessService service;

    public ModerationTextContent(AccessService service) {
        this.service = service;
    }

    public void moderationText(String textStr, Callback callback) throws Exception{
        //
        // 2.构建访问文本内容检测服务需要的参数
        //
        String uri = "/v1.0/moderation/text";

        // api请求参数说明可参考: https://support.huaweicloud.com/api-moderation/moderation_03_0018.html
        JSONObject json = new JSONObject();
        // 注：检测场景支持默认场景和自定义词库场景
        // 自定义词库配置使用可参考:https://support.huaweicloud.com/api-moderation/moderation_03_0027.html
        json.put("categories", new String[]{"porn", "politics", "ad", "abuse", "contraband", "flood"});

        JSONObject text = new JSONObject();
        text.put("text", textStr);
        text.put("type", "content");

        JSONArray items = new JSONArray();
        items.add(text);

        json.put("items", items);

        // 3.传入文本内容检测服务对应的uri参数, 传入文本内容检测服务需要的参数，
        // 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
        Call call = service.post(uri, json.toJSONString());
        call.enqueue(callback);

    }
}
