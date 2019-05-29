<?php
/**
 * 视频审核服务token 方式请求的使用示例
 */
require "./moderation_sdk/gettoken.php";
require "./moderation_sdk/moderation_video.php";
require "./moderation_sdk/utils.php";

// region目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)
init_region($region = 'cn-north-1');

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$demo_data_url = "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition";

$token = get_token($username, $password, $domainName);

$result = moderation_video($token, $demo_data_url, 5, array("terrorism", "porn", "politics"));
echo json_encode($result);

