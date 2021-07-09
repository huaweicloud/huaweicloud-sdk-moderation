<?php
/**
 * 文本检测服务的aksk请求方式的示例
 */
require "./moderation_sdk/moderation_text.php";
require "./moderation_sdk/utils.php";

// region目前支持华北-北京(cn-north-4)、华东上海一(cn-east-3)、中国-香港(ap-southeast-1)、亚太-新加坡(ap-southeast-3)
init_region($region = 'cn-north-4');

$app_key = "*********";
$app_secret = "*********";

$categories = array(
    array(
        "text" => "666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666",
        "type" => "content"
    )
);

$items = array("ad", "abuse", "politics", "porn", "contraband");
$result = moderation_text_aksk($app_key, $app_secret, $categories, $items);
echo $result;

