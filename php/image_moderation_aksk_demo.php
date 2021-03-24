<?php
/**
 * 图片内容检测服务ak,sk 方式请求的示例
 */
require "./moderation_sdk/image_moderation.php";
require "./moderation_sdk/utils.php";

// region目前支持华北-北京(cn-north-4)、华东上海一(cn-east-3)、亚太-香港(ap-southeast-1)、亚太-新加坡(ap-southeast-3)
init_region($region = 'cn-north-4');

$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/moderation-terrorism.jpg";
$data = file_to_base64($filepath);

$data_url = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/terrorism.jpg";

// 图片的base64 的方式请求接口
$result = image_content_aksk($app_key, $app_secret, $data, "", array("politics"), 0, "default");
echo $result;

// 图片的osb的url 方式请求接口
$result = image_content_aksk($app_key, $app_secret, "", $data_url, array("politics"), 0, "default");
echo $result;
