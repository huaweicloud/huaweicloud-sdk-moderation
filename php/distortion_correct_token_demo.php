<?php
/**
 * 扭曲矫正服务token 方式请求的服务示例
 */
require "./moderation_sdk/gettoken.php";
require "./moderation_sdk/distortion_correct.php";
require "./moderation_sdk/utils.php";

// region目前支持华北-北京(cn-north-4)
init_region($region = 'cn-north-4');

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$filepath = "./data/modeation-distortion.jpg";
$image = file_to_base64($filepath);

$demo_data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg";

$token = get_token($username, $password, $domainName);

// token方式 图片base64请求接口
$result = distortion_correct($token, $image, "", true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]['data'];
if ($basestr != "") {
    base64_to_file("data/moderation_distortion-token-1.jpg", $basestr);
}

// token 方式图片的url请求接口
$result = distortion_correct($token, "", $demo_data_url, true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]['data'];
if ($basestr != "") {
    base64_to_file("data/moderation_distortion-token-2.jpg", $basestr);
}
