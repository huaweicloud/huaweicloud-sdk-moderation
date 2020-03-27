<?php
/**
 * 图片内容检测服务token 方式请求的示例
 */
require "./moderation_sdk/gettoken.php";
require "./moderation_sdk/image_moderation.php";
require "./moderation_sdk/utils.php";

// region目前支持华北-北京(cn-north-4)、华东上海一(cn-east-3)、亚太-香港(ap-southeast-1)
init_region($region = 'cn-north-4');

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$filepath = "./data/moderation-terrorism.jpg";
$data = file_to_base64($filepath);

$data_url = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/terrorism.jpg";

$token = get_token($username, $password, $domainName);

// 图片base64方式请求接口
$result = image_content($token, $data, "", array("politics"), 0);
echo $result;
echo "\n";

// 图片的obs 的url方式请求接口
$result = image_content($token, "", $data_url, array("politics"), 0);
echo $result;