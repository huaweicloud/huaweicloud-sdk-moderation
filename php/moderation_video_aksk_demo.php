<?php
/**
 * 视频审核服务ak,sk 方式请求的使用示例
 */
require "./moderation_sdk/moderation_video.php";
require "./moderation_sdk/utils.php";

// region目前支持华北-北京(cn-north-4)、亚太-香港(ap-southeast-1)
init_region($region = 'cn-north-4');

$app_key = "*************";
$app_secret = "*************";

$demo_data_url = "https://obs-image-bj4.obs.cn-north-4.myhuaweicloud.com/video_moderation.mp4";

// obs的url方式请求
$result = moderation_video_aksk($app_key, $app_secret, $demo_data_url, 1, array("terrorism", "porn", "politics"));
echo json_encode($result);