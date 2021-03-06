<?php
require "signer.php";
require "ais.php";

/**
 * token 方式
 * @return stri
 */
function batch_jobs($token, $urls, $categories)
{
    $endPoint = get_endpoint(MODERATION);
    // 获取任务信息
    $jobResult = _batch_jobs($endPoint, $token, $urls, $categories);

    // 验证服务调用返回的状态是否成功，如果为2xx, 为成功, 否则失败。
    if (!status_success($jobResult['status'])) {
        echo "The http status code for get jobId request failure:" . $jobResult['status'] . "\n";
        return $jobResult;
    }

    $jobId = $jobResult['result']['job_id'];
    echo "Process job id is :" . $jobId . "\n";;

    $retryTimes = 0;
    while (true) {

        // 获取任务解析的结果
        $resultObj = get_result($endPoint, $token, $jobId);
        if (!status_success($resultObj['status'])) {
            // 如果查询次数小于最大次数，进行重试
            if($retryTimes < RETRY_MAX_TIMES){
                $retryTimes++;
                sleep(2);
                continue;
            }else{
                return $resultObj;
            }
        }

        if ($resultObj['result']['status'] == "failed" || $resultObj['result']['status'] == "finish") {
            return $resultObj;
        }
        else {
            sleep(2);
            continue;
        }

    }

}


function _batch_jobs($endPoint, $token, $urls, $categories)
{
    // 构建请求信息
    $_url = "https://" . $endPoint . IMAGE_CONTENT_BATCH_JOBS;

    $data = array(
        "urls" => $urls,                          // url：视频的URL路径
        "categories" => $categories               // categories：非必选 检测场景 array politics：是否涉及政治人物的检测。terrorism：是否包含暴恐元素的检测。porn：是否包含涉黄内容元素的检测
    );

    $curl = curl_init();
    $headers = array(
        "Content-Type:application/json",
        "X-Auth-Token:" . $token);

    // 请求信息封装
    curl_setopt($curl, CURLOPT_URL, $_url);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
    curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, TRUE);
    curl_setopt($curl, CURLOPT_NOBODY, FALSE);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($curl, CURLOPT_TIMEOUT, 5);

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
    curl_close($curl);
}

/**
 * 获取结果值
 * @param $token
 * @param $jobId
 */
function get_result($endPoint, $token, $jobId)
{
    $url = "https://" . $endPoint . IMAGE_CONTENT_BATCH_RESULT . "?job_id=" . $jobId;

    $headers = array(
        "Content-Type:application/json",
        "X-Auth-Token:" . $token);

    $curl = curl_init(); // 启动一个CURL会话
    curl_setopt($curl, CURLOPT_URL, $url);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($curl, CURLOPT_HEADER, 0);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($curl, CURLOPT_TIMEOUT, 5);

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
    curl_close($curl);
}

/**
 * ak,sk 方式
 */
function batch_jobs_aksk($_ak, $_sk, $urls, $categories)
{
    $endPoint = get_endpoint(MODERATION);

    // 构建ak，sk对象
    $signer = new Signer();
    $signer->AppKey = $_ak;             // 构建ak
    $signer->AppSecret = $_sk;          // 构建sk

    $jobResult = _batch_jobs_aksk($endPoint, $signer, $urls, $categories);

    // 验证服务调用返回的状态是否成功，如果为2xx, 为成功, 否则失败。
    if (!status_success($jobResult['status'])) {
        echo "The http status code for get jobId request failure:" . $jobResult['status'] . "\n";
        return $jobResult;
    }

    $jobId = $jobResult['result']['job_id'];
    echo "Process job id is :" . $jobId . "\n";

    $retryTimes = 0;
    while (true) {

        // 获取任务的执行结果
        $resultObj = get_result_aksk($endPoint, $signer, $jobId);

        if (!status_success($resultObj['status'])) {
            // 如果查询次数小于最大次数，进行重试
            if($retryTimes < RETRY_MAX_TIMES){
                $retryTimes++;
                sleep(2);
                continue;
            }else{
                return $resultObj;
            }
        }

        if ($resultObj['result']['status'] == "failed" || $resultObj['result']['status'] == "finish") {
            return $resultObj;
        }
        else {
            sleep(2);
            continue;
        }

    }
}

/**
 * 获取任务信息
 */
function _batch_jobs_aksk($endPoint, $signer, $urls, $categories)
{
    // 构建请求信息
    $data = array(
        "urls" => $urls,                          // urls：视频的URL路径，数组
        "categories" => $categories               // categories：非必选 检测场景 array politics：是否涉及政治人物的检测。terrorism：是否包含暴恐元素的检测。porn：是否包含涉黄内容元素的检测
    );

    $req = new Request();
    $req->method = 'POST';
    $req->scheme = 'https';
    $req->host = $endPoint;
    $req->uri = IMAGE_CONTENT_BATCH_JOBS;
    $req->body = json_encode($data);
    $req->headers = array(
        'Content-Type' => 'application/json'
    );
    $curl = $signer->Sign($req);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($curl, CURLOPT_TIMEOUT, DEFAULT_TIMEOUT);

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
    curl_close($curl);
}


/**
 * 获取结果值
 * @param $token
 * @param $jobId
 */
function get_result_aksk($endPoint, $signer, $jobId)
{

    $req = new Request();
    $req->method = 'GET';
    $req->scheme = 'https';
    $req->host = $endPoint;
    $req->uri = IMAGE_CONTENT_BATCH_RESULT;
    $req->query = array(
        'job_id' => $jobId
    );
    $req->headers = array(
        'Content-Type' => 'application/json'
    );
    $curl = $signer->Sign($req);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($curl, CURLOPT_TIMEOUT, DEFAULT_TIMEOUT);

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
    curl_close($curl);
}