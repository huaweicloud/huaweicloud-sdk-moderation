<?php
/**
 * 图片内容检测批量异步服务token 方式请求的示例
 */
require "./moderation_sdk/gettoken.php";
require "./moderation_sdk/image_moderation_batch_jobs.php";
require "./moderation_sdk/utils.php";

// region目前支持华北-北京(cn-north-4)、亚太-香港(ap-southeast-1)
init_region($region = 'cn-north-4');

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$data_url1 = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/terrorism.jpg";
$data_url2 = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/antiporn.jpg";

$token = get_token($username, $password, $domainName);

$result = batch_jobs($token, array($data_url1,$data_url2), array("politics", "terrorism", "porn"));
echo json_encode($result);