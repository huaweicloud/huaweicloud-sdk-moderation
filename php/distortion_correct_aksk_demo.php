<?php
/**
 * 扭曲矫正服务ak.sk 方式请求的示例
 */

require "./moderation_sdk/distortion_correct.php";
require "./moderation_sdk/utils.php";

// region目前支持华北-北京(cn-north-4)
init_region($region = 'cn-north-4');

$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/moderation-distortion.jpg";
$image = file_to_base64($filepath);

$demo_data_url = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/moderation-distortion.jpg";

// ak,sk 方式图片的base64请求接口
$result = distortion_correct_aksk($app_key, $app_secret, $image, "", true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]['data'];
if ($basestr != "") {
    base64_to_file("data/moderation_distortion-aksk-1.jpg", $basestr);
}

// ak,sk 方式图片的url方式请求接口
$result = distortion_correct_aksk($app_key, $app_secret, "", $demo_data_url, true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]['data'];
if ($basestr != "") {
    base64_to_file("data/moderation_distortion-aksk-2.jpg", $basestr);
}

