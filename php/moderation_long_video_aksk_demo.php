<?php
/**
 * 长视频审核服务ak,sk 方式请求的使用示例
 */
require "./moderation_sdk/moderation_long_video.php";
require "./moderation_sdk/utils.php";

$app_key = "********";
$app_secret = "*********";
// 每个账户的对应region下projectId唯一确定的
$projectId = "*********";

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

$result = moderation_long_video_aksk($app_key, $app_secret, $projectId, $requestBody);
echo json_encode($result);
echo "\n";

/**
 * 使用obs配置方式视频审核
 */
$inputType = "obs";
$inputBucket = "moderation-sdk-video";
$inputPath = "input/demo.mp4";
$inputParam = get_moderation_input($inputType, $inputBucket, $inputPath, $url=null);

$requestBody = get_moderation_requestbody($taskName, $description ,$inputParam, $outputParam,
    "porn,terrorism,politics", "ad,politics,porn,abuse,contraband,flood");

$result = moderation_long_video_aksk($app_key, $app_secret, $projectId, $requestBody);
echo json_encode($result);
