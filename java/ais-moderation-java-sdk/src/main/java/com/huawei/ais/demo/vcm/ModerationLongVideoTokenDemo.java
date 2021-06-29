package com.huawei.ais.demo.vcm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cloud.sdk.util.StringUtils;
import com.huawei.ais.demo.HttpJsonDataUtils;
import com.huawei.ais.sdk.util.HttpClientUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;

/**
 * 长视频内容审核API TOKEN方式使用示例
 */
public class ModerationLongVideoTokenDemo {
    private static final String SUBMIT_TASK_URI = "/v2/%s/services/video-moderation/tasks";
    private static final String ENDPOINT = "https://iva.cn-north-4.myhuaweicloud.com";
    private static final String IAM_ENDPOINT = "https://iam.myhuaweicloud.com";
    private static final String TOKEN_URL = IAM_ENDPOINT + "/v3/auth/tokens";
    private static final String GET_TASK_RESULT_URI_TEMPLATE = "/v2/%s/services/video-moderation/tasks/%s";

    public static int connectionTimeout = 5000; //连接目标url超时限制参数
    public static int connectionRequestTimeout = 1000;//连接池获取可用连接超时限制参数
    public static int socketTimeout =  5000;//获取服务器响应数据超时限制参数
    private static final long QUERY_JOB_RESULT_INTERVAL = 10000L;
    private static final Integer RETRY_MAX_TIMES = 3; // 查询任务失败的最大重试次数
    private static boolean sslVerification = true;


    public static void main(String [] args){

        // 1. 配置好访问视频审核服务的基本信息, 获取Token
        String username = "zhangshan";    // 用户名
        String domainName = "*******";    // 账户名，参考地址：https://console.huaweicloud.com/iam/#/myCredential
        String password = "*******";      // 对应用户名的密码
        String regionName = "cn-north-4"; // 服务的区域信息修改此项，需要同步修改ENDPOINT参数，参考地址: http://developer.huaweicloud.com/dev/endpoint

        try {
            // projectId 与华为云账号关联，不同区域的同一个账号projectId不同
            String projectId = "*******";

            String token = getToken(username, domainName, password, regionName);

            // 2. 配置视频审核的输入参数(可配置为obs，或url方式)
            String type = "obs";
            String inputBucket = "moderation-sdk-video";
            String inputPath = "input/demo.mp4";
            JSONObject inputJson = getInputJsonByObs(inputBucket, inputPath, type);

            // 配置视频审核输入为可访问的视频url方式（与步骤2作用一致，区别为输入方式不同）
            // String type = "url";
            // String url = "https://moderation-sdk-video.obs.cn-north-4.myhuaweicloud.com/input/demo.mp4";
            // JSONObject inputJson = getInputJsonByUrl(url, type);


            // 3. 访问长视频审核服务
            callModerationLongVideoService(projectId, inputJson, token);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 配置视频审核的对应规则参数
     * @return
     */
    private static JSONObject  getServiceConfigJson(){
        JSONObject  serviceConfigJson  = new JSONObject();

        JSONObject commonJson = new JSONObject();

        // 视频截帧时间间隔
        commonJson.put("frame_interval", 1);
        // 视频检测场景
        commonJson.put("categories", "porn,terrorism,politics");
        // 语音或文字审核服务的检测场景
        commonJson.put("text_categories", "ad,politics,porn,abuse,contraband,flood");
        // 是否使用语音审核服务
        commonJson.put("use_sis", "false");
        // 是否使用文字审核服务
        commonJson.put("use_ocr", "false");
        // 是否使用问题场景图片上传服务
        commonJson.put("upload", "false");

        serviceConfigJson.put("common", commonJson);
        return serviceConfigJson;
    }

    /**
     * 调用长视频审核服务
     * @param projectId 项目id
     * @param inputJson 输入json对象
     * @param token 请求token
     * @throws IOException
     * @throws InterruptedException
     */
    private static void callModerationLongVideoService(String projectId, JSONObject inputJson, String token) throws IOException, InterruptedException {
        String taskUrl = ENDPOINT + String.format(SUBMIT_TASK_URI, projectId);
        Header[] headers = new Header[]{
                new BasicHeader("X-Auth-Token", token),
                new BasicHeader("Content-Type", "application/json")};

        JSONObject requestJson = new JSONObject();

        // 新建任务名称
        requestJson.put("name", "video-task-demo");
        // 新建任务描述
        requestJson.put("description", "description");
        requestJson.put("input", inputJson);

        // 审核视频的结果所在OBS桶名称
        String outputBucket = "moderation-sdk-video";
        // 审核视频的结果所在桶内的路径
        String outputPath = "output/";
        JSONObject outputJson = getOutputJson(outputBucket, outputPath);
        requestJson.put("output", outputJson);

        // 视频审核场景以及相关参数设置
        JSONObject servicesConfigJson = getServiceConfigJson();
        requestJson.put("service_config", servicesConfigJson);
        // 功能版本为 1.2
        requestJson.put("service_version", "1.2");

        StringEntity stringEntity = new StringEntity(requestJson.toJSONString(), "utf-8");

        HttpResponse response = HttpClientUtils.post(taskUrl, headers, stringEntity,sslVerification, connectionTimeout, connectionRequestTimeout, socketTimeout);

        if (!HttpJsonDataUtils.isOKResponded(response)) {
            System.out.println("Submit the task failed!");
            System.out.println("Request body:" + requestJson.toJSONString());
            System.out.println(HttpJsonDataUtils.responseToString(response));
            return;
        }

        String taskResult = HttpClientUtils.convertStreamToString(response.getEntity().getContent());
        if(!StringUtils.isNullOrEmpty(taskResult)){
            JSONArray taskResultArray = (JSONArray)JSONArray.parse(taskResult);
            for (Object task:
                    taskResultArray) {
                // 获取结果的任务id
                String taskId = ((JSONObject)task).getString("id");
                System.out.println(taskId);

                String url = ENDPOINT + String.format(GET_TASK_RESULT_URI_TEMPLATE, projectId, taskId);

                // 初始化查询jobId失败次数
                Integer retryTimes = 0;
                getResult(headers, url, retryTimes);
            }
        }
    }

    /**
     * 获取请求结果
     * @param headers 请求头
     * @param url 请求链接
     * @param retryTimes 重试次数
     * @throws IOException
     * @throws InterruptedException
     */
    private static void getResult(Header[] headers, String url, Integer retryTimes) throws IOException, InterruptedException {
        // 构建进行查询的请求链接，并进行轮询查询，由于是异步任务，必须多次进行轮询
        // 直到结果状态为任务已处理结束
        while (true){
            HttpResponse getResponse = HttpClientUtils.get(url, headers, sslVerification, connectionTimeout, connectionRequestTimeout, socketTimeout);
            if (getResponse == null || !HttpJsonDataUtils.isOKResponded(getResponse)) {
                System.out.println("Get " + url);
                if (getResponse != null){
                    System.out.println(HttpClientUtils.convertStreamToString(getResponse.getEntity().getContent()));
                }

                if(retryTimes < RETRY_MAX_TIMES){
                    retryTimes++;
                    System.out.println(String.format("Jobs process result failed! The number of retries is %s!", retryTimes));
                    Thread.sleep(QUERY_JOB_RESULT_INTERVAL);
                    continue;
                }else{
                    HttpClientUtils.delete(url, headers, sslVerification, connectionTimeout, connectionRequestTimeout, socketTimeout);
                    break;
                }
            }

            String result = HttpClientUtils.convertStreamToString(getResponse.getEntity().getContent());
            JSONObject resp = JSON.parseObject(result);
            String status = resp.getString("state");

            // 如果作业处于非结束状态，则继续执行
            if (status.equals("PENDING") || status.equals("SCHEDULING")
                    || status.equals("STARTING") || status.equals("RUNNING")){
                Thread.sleep(QUERY_JOB_RESULT_INTERVAL);
                continue;
                // 如过作业失败，则直接退出
            }else if(status.equals("CREATE_FAIL") || status.equals("FAILED")
                    || status.equals("ABNORMAL")){
                System.out.println("Job failed!");
                System.out.println(JSON.toJSONString(resp, SerializerFeature.PrettyFormat));
                HttpClientUtils.delete(url, headers, sslVerification, connectionTimeout, connectionRequestTimeout, socketTimeout);
                break;
            } else{
                JSONObject hostingResult = resp.getJSONObject("hosting_result");
                String hostingStatus = hostingResult.getString("status");
                if (hostingStatus.equals("NOT_GENERATED")){
                    Thread.sleep(QUERY_JOB_RESULT_INTERVAL);
                    continue;
                }else {
                    // 任务处理结束，打印结果
                    System.out.println("Job finished!");
                    System.out.println(JSON.toJSONString(resp, SerializerFeature.PrettyFormat));
                    HttpClientUtils.delete(url, headers, sslVerification, connectionTimeout, connectionRequestTimeout, socketTimeout);
                    break;
                }

            }
        }
    }

    /**
     * 获取obs路径方式的视频输入json
     * @param inputBucket 视频所在桶信息
     * @param inputPath 视频所在路径
     * @param inputType 输入类型
     * @return 待审核视频的json对象
     */
    private static JSONObject getInputJsonByObs(String inputBucket, String inputPath, String inputType){
        JSONObject inputJson = new JSONObject();

        JSONObject dataJson = new JSONObject();
        dataJson.put("bucket", inputBucket);
        dataJson.put("path", inputPath);
        dataJson.put("index", 0);

        JSONArray dataJsonArray = new JSONArray();
        dataJsonArray.add(dataJson);

        inputJson.put("type", inputType);
        inputJson.put("data", dataJsonArray);
        return inputJson;
    }

    /**
     * 获取url路径方式的视频输入json
     * @param url 可访问的视频地址
     * @param inputType 输入类型
     * @return 待审核视频的json对象
     */
    private static JSONObject getInputJsonByUrl(String url, String inputType){
        JSONObject inputJson = new JSONObject();

        JSONObject dataJson = new JSONObject();
        dataJson.put("url", url);

        JSONArray dataJsonArray = new JSONArray();
        dataJsonArray.add(dataJson);

        inputJson.put("type", inputType);
        inputJson.put("data", dataJsonArray);
        return inputJson;
    }

    /**
     * 获取Token参数， 注意，此函数的目的，主要为了从HTTP请求返回体中的Header中提取出Token
     * 参数名为: X-Subject-Token
     *
     * @param username   用户名
     * @param domainName 账户名
     * @param password   密码
     * @param regionName 区域名
     * @return 包含Token串的返回体，
     * @throws UnsupportedOperationException
     * @throws IOException
     */
    private static String getToken(String username, String domainName, String password, String regionName)
            throws UnsupportedOperationException, IOException {
        String requestBody = getTokenReqBody(username, password, domainName, regionName);

        Header[] headers = new Header[]{new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
        StringEntity stringEntity = new StringEntity(requestBody, "utf-8");

        HttpResponse response = HttpClientUtils.post(TOKEN_URL, headers, stringEntity, sslVerification, connectionTimeout,
                connectionRequestTimeout, socketTimeout);
        if (!HttpJsonDataUtils.isOKResponded(response)) {
            System.out.println("Request body:" + HttpJsonDataUtils.prettify(requestBody));
            System.out.println(HttpJsonDataUtils.responseToString(response));
            return null;
        }
        Header[] xst = response.getHeaders("X-Subject-Token");
        return xst[0].getValue();

    }

    /**
     * 获取输出结果的json对象
     * @param outputBucket 输出桶位置
     * @param outputPath 输出路径
     * @return
     */
    private static JSONObject getOutputJson(String outputBucket, String outputPath){
        JSONObject outputJson = new JSONObject();

        JSONObject obsJson = new JSONObject();
        obsJson.put("bucket", outputBucket);
        obsJson.put("path", outputPath);

        JSONObject hostingJson = new JSONObject();

        outputJson.put("obs", obsJson);
        // 结果将托管到服务侧的OBS,单个作业接口获取该路径
        outputJson.put("hosting", hostingJson);
        return outputJson;
    }

    private static String getTokenReqBody(String username, String password, String domainName, String regionName) {
        return "{" +
                "    \"auth\":{" +
                "        \"identity\":{" +
                "            \"password\":{" +
                "                \"user\":{" +
                "                    \"name\":\"" + username + "\"," +
                "                    \"password\":\"" + password + "\"," +
                "                    \"domain\":{" +
                "                        \"name\":\"" + domainName + "\"" +
                "                    }" +
                "                }" +
                "            }," +
                "            \"methods\":[\"password\"]" +
                "        }," +
                "        \"scope\":{" +
                "            \"project\":{" +
                "                 \"name\":\"" + regionName + "\"" +
                "            }" +
                "        }" +
                "    }" +
                "}";
    }


}
