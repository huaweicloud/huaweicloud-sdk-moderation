<?php
require "signer.php";
require "ais.php";

/**
 * token 方式
 * @return stri
 */
function moderation_long_video($projectId, $token, $requestbody)
{
    $taskUri = sprintf(MODERATION_LONG_VIDEO, $projectId);
    // 获取任务信息
    $jobResult = _moderation_long_video($token, $taskUri, $requestbody);

    // 验证服务调用返回的状态是否成功，如果为2xx, 为成功, 否则失败。
    if (!status_success($jobResult['status'])) {
        echo "The http status code for get taskId request failure: " . $jobResult['status'] . "\n";
        return $jobResult;
    }

    $jsonCount = count($jobResult);
    for ($i =0; $i < $jsonCount; $i++){
        $taskId = $jobResult[$i]['id'];
        echo "Process task id is :" . $taskId . "\n";

        $resultUri = sprintf(MODERATION_LONG_VIDEO_RESULT, $projectId, $taskId);

        $retryTimes = 0;
        while (true) {

            // 获取任务解析的结果
            $resultObj = get_result($token, $resultUri);
            if (!status_success($resultObj['status'])) {
                // 如果查询次数小于最大次数，进行重试
                if($retryTimes < RETRY_MAX_TIMES){
                    $retryTimes++;
                    sleep(3);
                    continue;
                }else{
                    return $resultObj;
                }
            }

            $state = $resultObj['state'];
            // 任务处理未完成，轮询继续请求接口
            if ($state == "PENDING" || $state == "SCHEDULING" || $state == "STARTING" || $state =="RUNNING") {
                sleep(10);
                continue;
            } else {
                $hostingResult = $resultObj['hosting_result'];
                $hostingStatus = $hostingResult['status'];
                if ($hostingStatus == "NOT_GENERATED"){
                    sleep(10);
                    continue;
                }else {
                    return $resultObj;
                }

            }

        }
    }

}

function _moderation_long_video($token, $uri, $requestbody)
{
    // 构建请求信息
    $_url = "https://" . IVA_ENDPOINT . $uri;

    $curl = curl_init();
    $headers = array(
        "Content-Type:application/json",
        "X-Auth-Token:" . $token);

    // 请求信息封装
    curl_setopt($curl, CURLOPT_URL, $_url);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
    curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($requestbody));
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, TRUE);
    curl_setopt($curl, CURLOPT_NOBODY, FALSE);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($curl, CURLOPT_TIMEOUT, DEFAULT_TIMEOUT);

    // 执行请求信息
    try{
        $response = curl_exec($curl);
        $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
        if ($status == 0) {
            echo curl_error($curl);
        } else {
            $response = json_decode($response, true);
            $response['status'] = $status;
            return $response;
        }
    }finally{
        curl_close($curl);
    }
    return $response;
}

/**
 * token 方式获取结果
 * @param $token string token信息
 * @param $resultUri string 获取结果的uri
 * @return mixed
 */
function get_result($token, $resultUri)
{
    $url = "https://" . IVA_ENDPOINT . $resultUri;

    $headers = array(
        "Content-Type:application/json",
        "X-Auth-Token:" . $token);

    $curl = curl_init(); // 启动一个CURL会话
    curl_setopt($curl, CURLOPT_URL, $url);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($curl, CURLOPT_HEADER, 0);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($curl, CURLOPT_TIMEOUT, DEFAULT_TIMEOUT);

    try{
        // 执行请求信息
        $response = curl_exec($curl);
        $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);

        if ($status == 0) {
            echo curl_error($curl);
        } else {
            $response = json_decode($response, true);
            $response['status'] = $status;
            return $response;
        }
    }finally{
        curl_close($curl);
    }
   return $response;
}

/**
 *
 */
/**
 * ak,sk 方式 长视频审核
 * @param $_ak string ak信息
 * @param $_sk string sk信息
 * @param $projectId string 账户projectId
 * @param $requestbody array 请求体
 * @return mixed
 */
function moderation_long_video_aksk($_ak, $_sk, $projectId, $requestbody)
{
    $taskUri = sprintf(MODERATION_LONG_VIDEO, $projectId);

    // 构建ak，sk对象
    $signer = new Signer();
    $signer->AppKey = $_ak;             // 构建ak
    $signer->AppSecret = $_sk;          // 构建sk

    $jobResult = _moderation_long_video_aksk($signer, $taskUri, $requestbody);

    // 验证服务调用返回的状态是否成功，如果为2xx, 为成功, 否则失败。
    if (!status_success($jobResult['status'])) {
        echo "The http status code for get jobId request failure:" . $jobResult['status'] . "\n";
        return $jobResult;
    }

    $jsonCount = count($jobResult);
    for ($i =0; $i < $jsonCount; $i++){
        $taskId = $jobResult[$i]['id'];
        echo "Process task id is :" . $taskId . "\n";

        $resultUri = sprintf(MODERATION_LONG_VIDEO_RESULT, $projectId, $taskId);

        $retryTimes = 0;
        while (true) {

            // 获取任务的执行结果
            $resultObj = get_result_aksk($signer, $resultUri);

            if (!status_success($resultObj['status'])) {
                // 如果查询次数小于最大次数，进行重试
                if ($retryTimes < RETRY_MAX_TIMES) {
                    $retryTimes++;
                    sleep(3);
                    continue;
                } else {
                    return $resultObj;
                }

            }

            $state = $resultObj['state'];
            // 任务处理未完成，轮询继续请求接口
            if ($state == "PENDING" || $state == "SCHEDULING" || $state == "STARTING" || $state =="RUNNING") {
                sleep(10);
                continue;
            } else {
                $hostingResult = $resultObj['hosting_result'];
                $hostingStatus = $hostingResult['status'];
                if ($hostingStatus == "NOT_GENERATED"){
                    sleep(10);
                    continue;
                }else {
                    return $resultObj;
                }

            }

        }
    }
}

/**
 * 获取长视频审核的任务集合
 * @param $signer object 签名对象
 * @param $taskUri string 请求的uri
 * @param $requestbody array 请求体
 * @return mixed 任务集合
 */
function _moderation_long_video_aksk($signer, $taskUri, $requestbody)
{
    $req = new Request();
    $req->method = 'POST';
    $req->scheme = 'https';
    $req->host = IVA_ENDPOINT;
    $req->uri = $taskUri;
    $req->body = json_encode($requestbody);
    $req->headers = array(
        'Content-Type' => 'application/json'
    );
    $curl = $signer->Sign($req);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($curl, CURLOPT_TIMEOUT, DEFAULT_TIMEOUT);

    try{
        // 执行请求信息
        $response = curl_exec($curl);
        $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
        if ($status == 0) {
            echo curl_error($curl);
        } else {

            $response = json_decode($response, true);
            $response['status'] = $status;
            return $response;
        }
    }
    finally{
        curl_close($curl);
    }
    return $response;
}


/**
 * @param $signer object 签名对象
 * @param $resultUri string 获取结果的uri
 * @return mixed 返回审核结果
 */
function get_result_aksk($signer, $resultUri)
{

    $req = new Request();
    $req->method = 'GET';
    $req->scheme = 'https';
    $req->host = IVA_ENDPOINT;
    $req->uri = $resultUri;
    $req->headers = array(
        'Content-Type' => 'application/json'
    );
    $curl = $signer->Sign($req);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($curl, CURLOPT_TIMEOUT, DEFAULT_TIMEOUT);

    try{
        // 执行请求信息
        $response = curl_exec($curl);
        $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);

        if ($status == 0) {
            echo curl_error($curl);
        } else {

            $response = json_decode($response, true);
            $response['status'] = $status;
            return $response;
        }
    }finally{
        curl_close($curl);
    }

    return $response;
}

/**
 * 配置审核视频输入参数
 * @param $inputType string 视频数据的输入类型
 * @param $inputBucket string OBS桶名称
 * @param $inputPath string OBS桶内的路径
 * @param $url string 输入数据的URL
 */
function get_moderation_input($inputType, $inputBucket, $inputPath, $url){
    $inputData = null;

    if($inputType == "obs"){
        $inputData = array(
            "data" => $data = array(
                array(
                    "bucket" => $inputBucket,
                    "path" => $inputPath,
                    "index" => 0
                )
            ),
            "type" => $inputType
        );
    }elseif ($inputType == "url"){
        $inputData = array(
            "data" => array(
                array(
                    "url" => $url
                )
            ),
            "type" => $inputType
        );
    }else{
        echo "input parameter error";
    }

    return $inputData;
}

/**
 * 获取请求的body内容
 * @param $taskName string 审核任务
 * @param $description string 审核秒数
 * @param $input array 审核输入配置
 * @param $output array 审核输出配置
 * @param $categories string 视频检测场景
 * @param $text_categories string 语音或文字审核服务的检测场景
 */
function get_moderation_requestbody($taskName, $description ,$input, $output, $categories, $text_categories){
    $requestBody = array(
        "name" => $taskName,
        "description" => $description,
        "input" => $input,
        "output" => $output,
        "service_version" => "1.2",
        "service_config" =>
            array(
                "common" =>             array(
                    "categories" => $categories,
                    "text_categories" => $text_categories,
                    "use_sis" => "true",                // 是否使用语音审核服务
                    "use_ocr" => "true",                // 是否使用文字审核服务
                    "upload" => "true",                 // 是否使用问题场景图片上传服务
                    "frame_interval" => 1,              // 视频截帧时间间隔
            )
        )
    );
    return $requestBody;
}


/**
 * 配置审核视频输出参数
 * @param $bucket string OBS桶名称
 * @param $path  string obs输出路径
 */
function get_moderation_output($bucket, $path){
    $outPutData = array(
        "obs" => array(
            "bucket" => $bucket,
            "path" => $path
        ),
        "hosting" => (object)null               // 结果将托管到服务侧的OBS,单个作业接口获取该路径
    );

    return $outPutData;
}
