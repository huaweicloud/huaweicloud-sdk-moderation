package com.huawei.ais.demo.vcm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cloud.sdk.util.StringUtils;
import com.huawei.ais.common.AuthInfo;
import com.huawei.ais.demo.HttpJsonDataUtils;
import com.huawei.ais.sdk.AisAccess;
import com.huawei.ais.sdk.util.HttpClientUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 * 长视频内容审核API AK/SK方式使用示例
 */
public class ModerationLongVideoAkskDemo {
    private static final String SUBMIT_TASK_URI = "/v2/%s/services/video-moderation/tasks";
    private static final String GET_TASK_RESULT_URI_TEMPLATE = "/v2/%s/services/video-moderation/tasks/%s";
    public static int connectionTimeout = 5000; //连接目标url超时限制参数
    public static int connectionRequestTimeout = 1000;//连接池获取可用连接超时限制参数
    public static int socketTimeout =  5000;//获取服务器响应数据超时限制参数
    public static int maxRetryTimes =  3; //请求异常的重试次数
    public static boolean sslVerification = true;
    private static final long QUERY_JOB_RESULT_INTERVAL = 10000L;
    private static final Integer RETRY_MAX_TIMES = 3; // 查询任务失败的最大重试次数

    public static void main(String [] args){

        // 1. 配置用户project_id
        String projectId = "######";

        // 2. 访问长视频审核服务的ak,sk信息,生成对应的一个客户端连接对象
        AuthInfo HEC_AUTH = new AuthInfo(
                "https://iva.cn-north-4.myhuaweicloud.com", //域名信息和区域要保持一致
                "cn-north-4",
                "######",    /* 请输入你的AK信息 */
                "######"     /* 对应AK的的SK信息 */
        );

       AisAccess aisAccess = new AisAccess(HEC_AUTH, sslVerification, connectionTimeout, connectionRequestTimeout, socketTimeout, maxRetryTimes);
       try {

           // 3. 配置视频审核的输入参数(可配置为obs，或url方式)
           String type = "obs";
           String inputBucket = "moderation-sdk-video";
           String inputPath = "input/demo.mp4";
           JSONObject inputJson = getInputJsonByObs(inputBucket, inputPath, type);

           // 配置视频审核输入为可访问的视频url方式（与步骤3作用一致，区别为输入方式不同）
           // String type = "url";
           // String url = "https://moderation-sdk-video.obs.cn-north-4.myhuaweicloud.com/input/demo.mp4";
           // JSONObject inputJson = getInputJsonByUrl(url, type);

           // 4. 访问长视频审核服务
           callModerationLongVideoService(projectId, inputJson, aisAccess);

       }catch (Exception e){
           e.printStackTrace();
       }finally {
           //
           // 3.使用完毕，关闭服务的客户端连接
           //
           aisAccess.close();
       }

    }

    /**
     * 配置视频审核的场景和相关参数，例如视频截帧，检测场景，是否启用语音审核等
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
            commonJson.put("use_sis", "true");
            // 是否使用文字审核服务
            commonJson.put("use_ocr", "true");
            // 是否使用问题场景图片上传服务
            commonJson.put("upload", "true");

            serviceConfigJson.put("common", commonJson);
            return serviceConfigJson;
    }

    /**
     * 调用长视频审核服务
     * @param projectId 用户项目id
     * @param inputJson 视频输入json对象
     * @param aisAccess 客户端连接
     * @throws IOException
     * @throws InterruptedException
     */
    private static void callModerationLongVideoService(String projectId, JSONObject inputJson, AisAccess aisAccess)
            throws IOException, InterruptedException {
        String taskUrl = String.format(SUBMIT_TASK_URI, projectId);

        JSONObject requestJson = new JSONObject();

        // 新建任务名称
        requestJson.put("name", "task-demo");
        // 新建任务描述
        requestJson.put("description", "description");
        requestJson.put("input", inputJson);

        // 审核视频的结果所在OBS桶名称，可自定义
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

        HttpResponse response = aisAccess.post(taskUrl, stringEntity);

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

            String url = String.format(GET_TASK_RESULT_URI_TEMPLATE, projectId, taskId);

            // 初始化查询jobId失败次数
            Integer retryTimes = 0;
            getResult(aisAccess, url, retryTimes);

        }
        }
    }

    /**
     * 获取审核结果
     * @param aisAccess 客户端连接对象
     * @param url 获取任务结果的url
     * @param retryTimes 重试次数
     * @throws IOException
     * @throws InterruptedException
     */
    private static void getResult(AisAccess aisAccess, String url, Integer retryTimes) throws IOException, InterruptedException {
        // 构建进行查询的请求链接，并进行轮询查询，由于是异步任务，必须多次进行轮询
        // 直到结果状态为任务已处理结束
        while (true){
            HttpResponse getResponse = aisAccess.get(url);
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
                    aisAccess.delete(url);
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
                aisAccess.delete(url);
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
                    aisAccess.delete(url);
                    break;
                }

            }
        }
    }

    /**
     * 视频obs路径方式获取输入json对象
     * @param inputBucket 视频所在桶
     * @param inputPath 视频所在路径
     * @param inputType 视频输入类型
     * @return
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
     * 视频url路径方式获取输入json对象
     * @param url 可访问的视频url
     * @param inputType 视频输入类型
     * @return
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
     * 配置获取结果json文件输出的obs位置
     * @param outputBucket 输出桶位置
     * @param outputPath 输出文件路径
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

}
