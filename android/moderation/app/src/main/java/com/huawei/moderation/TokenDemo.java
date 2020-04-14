package com.huawei.moderation;

import android.graphics.Bitmap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.moderation.utils.HttpClientUtils;
import com.huawei.moderation.utils.ServiceAccessBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;

/**
 * 使用Token认证方式访问服务
 */
public class TokenDemo {
    public static String projectName = "cn-north-4"; // 此处，请输入服务的区域信息，目前支持华北-北京(cn-north-4)、华东上海一(cn-east-3)以及亚太-香港(ap-southeast-1)
    private static final long POLLING_INTERVAL = 2000L;
    private static final Integer RETRY_MAX_TIMES = 3; // 查询任务失败的最大重试次数
    public static int connectionTimeout = 5000; //连接目标url超时限制参数
    public static int connectionRequestTimeout = 1000;//连接池获取可用连接超时限制参数
    public static int socketTimeout = 5000;//获取服务器响应数据超时限制参数


    /**
     * 构造使用Token方式访问服务的请求Token对象
     *
     * @param username    用户名
     * @param passwd      密码
     * @param domainName  域名
     * @param projectName 项目名称
     * @return 构造访问的JSON对象
     */
    private static JSONObject requestBody(String username, String passwd, String domainName, String projectName) {
        JSONObject auth = new JSONObject();

        JSONObject identity = new JSONObject();

        JSONArray methods = new JSONArray();
        methods.add("password");
        identity.put("methods", methods);

        JSONObject password = new JSONObject();

        JSONObject user = new JSONObject();
        user.put("name", domainName);
        user.put("password", passwd);

        JSONObject domain = new JSONObject();
        domain.put("name", domainName);
        user.put("domain", domain);

        password.put("user", user);

        identity.put("password", password);

        JSONObject scope = new JSONObject();

        JSONObject scopeProject = new JSONObject();
        scopeProject.put("name", projectName);

        scope.put("project", scopeProject);

        auth.put("identity", identity);
        auth.put("scope", scope);

        JSONObject params = new JSONObject();
        params.put("auth", auth);
        return params;
    }

    /**
     * 获取Token参数， 注意，此函数的目的，主要为了从HTTP请求返回体中的Header中提取出Token
     * 参数名为: X-Subject-Token
     *
     * @param username    用户名
     * @param password    密码
     * @param projectName 区域名，可以参考http://developer.huaweicloud.com/dev/endpoint
     * @throws Exception
     */
    public static Call getToken(String username, String password, String projectName)
            throws Exception {
        JSONObject requestBody = requestBody(username, password, username, projectName);
        String url = "https://iam.myhuaweicloud.com/v3/auth/tokens";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", MediaType.parse("application/json; charset=utf-8").toString());

        return HttpClientUtils.post(url, headers, requestBody);
    }

    /**
     * 清晰度检测，使用Token认证方式访问服务
     *
     * @param token  token认证串
     * @param bitmap 图像
     * @throws IOException
     */
    public static void moderationClarity(String token, Bitmap bitmap, Callback callback) throws Exception {
        String url = ServiceAccessBuilder.getCurrentEndpoint(projectName) + "/v1.0/moderation/image/clarity-detect";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth-Token", token);

        String fileBase64Str = HttpClientUtils.BitmapStrByBase64(bitmap);
        JSONObject json = new JSONObject();
        json.put("image", fileBase64Str);
        json.put("threshhold", 0.8);
        Call call = HttpClientUtils.post(url, headers, json);
        call.enqueue(callback);
    }

    /**
     * 扭曲矫正，使用Token认证方式访问服务
     *
     * @param token  token认证串
     * @param bitmap 图像
     * @throws IOException
     */
    public static void moderationDistortionCorrect(String token, Bitmap bitmap, Callback callback) throws Exception {
        String url = ServiceAccessBuilder.getCurrentEndpoint(projectName) + "/v1.0/moderation/image/distortion-correct";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth-Token", token);

        String fileBase64Str = HttpClientUtils.BitmapStrByBase64(bitmap);
        JSONObject json = new JSONObject();
        json.put("image", fileBase64Str);
        json.put("correction", true);

        Call call = HttpClientUtils.post(url, headers, json);
        call.enqueue(callback);
    }

    /**
     * 文本内容检测，使用Base64编码后的文件方式，使用Token认证方式访问服务
     *
     * @param token          token认证串
     * @param textModeration 文本内容
     * @throws IOException
     */
    public static void moderationTextContent(String token, String textModeration, Callback callback) throws Exception {
        String url = ServiceAccessBuilder.getCurrentEndpoint(projectName) + "/v1.0/moderation/text";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth-Token", token);

        JSONObject json = new JSONObject();
        json.put("categories", new String[]{"porn", "politics", "ad", "abuse", "contraband", "flood"});

        JSONObject text = new JSONObject();
        text.put("text", textModeration);
        text.put("type", "content");

        JSONArray items = new JSONArray();
        items.add(text);

        json.put("items", items);

        Call call = HttpClientUtils.post(url, headers, json);
        call.enqueue(callback);
    }

    /**
     * 图像内容检测，使用Base64编码后的文件方式，使用Token认证方式访问服务
     *
     * @param token  token认证串
     * @param bitmap 图像
     * @throws IOException
     */
    public static void moderationImageContent(String token, Bitmap bitmap, Callback callback) throws Exception {
        String url = ServiceAccessBuilder.getCurrentEndpoint(projectName) + "/v1.0/moderation/image";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth-Token", token);

        String fileBase64Str = HttpClientUtils.BitmapStrByBase64(bitmap);
        JSONObject json = new JSONObject();
        json.put("image", fileBase64Str);
        json.put("categories", new String[]{"politics", "terrorism", "porn", "ad"}); //检测内容
        json.put("threshold", 0);

        Call call = HttpClientUtils.post(url, headers, json);
        call.enqueue(callback);
    }

    /**
     * 图像内容批量检测，使用obs对象方式，使用Token认证方式访问服务
     *
     * @param token token认证串
     * @param urls  obs 对象数组
     * @throws IOException
     */
    public static void moderationImageContentBatch(String token, String[] urls, Callback callback) throws Exception {
        String url = ServiceAccessBuilder.getCurrentEndpoint(projectName) + "/v1.0/moderation/image/batch";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth-Token", token);

        JSONObject json = new JSONObject();
        json.put("urls", urls);
        json.put("categories", new String[]{"politics", "terrorism", "porn", "ad"}); //检测内容
        json.put("threshold", 0);

        Call call = HttpClientUtils.post(url, headers, json);
        call.enqueue(callback);
    }

}

