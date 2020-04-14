package com.huawei.moderation;

import com.alibaba.fastjson.JSONObject;
import com.huawei.moderation.utils.AccessService;

import okhttp3.Call;
import okhttp3.Callback;

public class ModerationImageBatchContent {
    private AccessService service;

    public ModerationImageBatchContent(AccessService service) {
        this.service = service;
    }

    public void imageContentBatchCheck(String[] urls, Callback callback) throws Exception {
        //
        // 构建访问图像内容批量检测服务需要的参数
        //
        String uri = "/v1.0/moderation/image/batch";

        JSONObject json = new JSONObject();

        // api请求参数说明可参考：https://support.huaweicloud.com/api-moderation/moderation_03_0036.html
        json.put("urls", urls);
        json.put("categories", new String[]{"politics", "terrorism", "porn", "ad"}); //检测内容
        json.put("threshold", 0);

        // 传入图像内容批量检测服务对应的uri参数, 传入图像内容批量检测服务需要的参数，
        Call call = service.post(uri, json.toJSONString());
        call.enqueue(callback);
    }
}
