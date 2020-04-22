<?php
/**
 * 图片清晰度检测的服务ak,sk 方式请求的使用示例
 */
require "./moderation_sdk/clarity_detect.php";
require "./moderation_sdk/utils.php";

// region目前支持华北-北京(cn-north-4)
init_region($region = 'cn-north-4');

$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/moderation-clarity-detect.jpg";
$data = file_to_base64($filepath);

$data_url = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/moderation-clarity-detect.jpg";

// base64 方式请求
$result = clarity_detect_aksk($app_key, $app_secret, $data, "", 0.8);
echo $result;


// obs的url方式请求
$result = clarity_detect_aksk($app_key, $app_secret, "", $data_url, 0.8);
echo $result;
