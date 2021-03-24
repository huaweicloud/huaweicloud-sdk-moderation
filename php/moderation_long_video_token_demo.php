<?php
/**
 * 长视频审核服务token 方式请求的使用示例
 */
require "./moderation_sdk/gettoken.php";
require "./moderation_sdk/moderation_long_video.php";
require "./moderation_sdk/utils.php";

// region目前支持华北-北京一(cn-north-4)
init_region($region = 'cn-north-4');

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "********";   // 配置用户名
// projectId 与华为云账号关联，每个账号在对应region下只有一个
$projectId = "********";

$token = get_token($username, $password, $domainName);

/**
 * 使用url配置方式视频审核
 */
$inputType = "url";
$url = "https://moderation-sdk-video.obs.cn-north-4.myhuaweicloud.com/input/demo.mp4";
$inputParam = get_moderation_input($inputType, $inputBucket=null, $inputPath=null, $url);

// obs输出文件所在桶的名称
$outputBucket = "moderation-sdk-video";
// obs输出文件在桶中的路径
$outputPath = "output/";
$outputParam = get_moderation_output($outputBucket, $outputPath);

// 任务名称
$taskName = "task-demo";
// 任务描述
$description = "description";
$requestBody = get_moderation_requestbody($taskName, $description ,$inputParam, $outputParam, "porn,terrorism,politics",
    "ad,politics,porn,abuse,contraband,flood");

$result = moderation_long_video($projectId, $token, $requestBody);
echo json_encode($result);
echo "\n";

/**
 * 使用obs配置方式视频审核
 */
$inputType = "obs";
$inputBucket = "moderation-sdk-video";
$inputPath = "input/demo.mp4";
$inputParam = get_moderation_input($inputType, $inputBucket, $inputPath, $url=null);

$requestBody = get_moderation_requestbody($taskName, $description ,$inputParam, $outputParam, "porn,terrorism,politics",
    "ad,politics,porn,abuse,contraband,flood");

$result = moderation_long_video($projectId, $token, $requestBody);
echo json_encode($result);
