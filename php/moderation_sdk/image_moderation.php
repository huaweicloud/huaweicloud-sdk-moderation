<?php
require "signer.php";
require "ais.php";

/**
 * token 方式
 */
function image_content($token, $data, $url, $categories, $threshold, $moderation_rule)
{

    $endPoint = get_endpoint(MODERATION);
    // 构建请求信息
    $_url = "https://" . $endPoint . IMAGE_CONTENT_DETECT;

    $data = array(
        "image" => $data,                      // 与url二选一 图片文件Base64编码字符串
        "url" => $url,                         // 与image二选一 图片的URL路径
        "threshold" => $threshold,             // 非必选 结果过滤门限
        "categories" => $categories,           // 非必选 检测场景 array politics：是否涉及政治人物的检测。terrorism：是否包含暴恐元素的检测。porn：是否包含涉黄内容元素的检测
        "moderation_rule" => $moderation_rule  // 非必选 配置审核规则请参考：https://support.huaweicloud.com/api-moderation/moderation_03_0063.html
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
    curl_setopt($curl, CURLOPT_TIMEOUT, DEFAULT_TIMEOUT);

    // 执行请求信息
    $response = curl_exec($curl);
    $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
    if ($status == 0) {
        echo curl_error($curl);
    } else {

        // 验证服务调用返回的状态是否成功，如果为2xx, 为成功, 否则失败。
        if (status_success($status)) {
            return $response;
        } else {
            echo "Http status is: " . $status . "\n";
            echo $response;
        }

    }
    curl_close($curl);


}

/**
 * ak,sk 方式
 */
function image_content_aksk($_ak, $_sk, $data, $url, $categories, $threshold, $moderation_rule)
{
    // 构建ak，sk对象
    $signer = new Signer();
    $signer->AppKey = $_ak;             // 构建ak
    $signer->AppSecret = $_sk;          // 构建sk

    $endPoint = get_endpoint(MODERATION);

    // 构建请求对象
    $req = new Request();
    $req->method = "POST";
    $req->scheme = "https";
    $req->host = $endPoint;
    $req->uri = IMAGE_CONTENT_DETECT;

    $data = array(
        "image" => $data,                      // 与url二选一 图片文件Base64编码字符串
        "url" => $url,                         // 与image二选一 图片的URL路径
        "threshold" => $threshold,             // 非必选 结果过滤门限 过滤门限0-1 之间，检测结果与算法有关，与其他无关
        "categories" => $categories,           // 非必选 检测场景 array politics：是否涉及政治人物的检测。terrorism：是否包含暴恐元素的检测。porn：是否包含涉黄内容元素的检测
        "moderation_rule" => $moderation_rule  // 非必选 配置审核规则请参考：https://support.huaweicloud.com/api-moderation/moderation_03_0063.html
    );

    $headers = array(
        "Content-Type" => "application/json",
    );

    $req->headers = $headers;
    $req->body = json_encode($data);

    // 获取ak，sk方式的请求对象，执行请求
    $curl = $signer->Sign($req);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($curl, CURLOPT_TIMEOUT, DEFAULT_TIMEOUT);
    $response = curl_exec($curl);
    $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
    if ($status == 0) {
        echo curl_error($curl);
    } else {

        // 验证服务调用返回的状态是否成功，如果为2xx, 为成功, 否则失败。
        if (status_success($status)) {
            return $response;
        } else {

            echo "Http status is: " . $status . "\n";
            echo $response;
        }

    }
    curl_close($curl);
}
