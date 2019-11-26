/**
 * 图像内容批量检测服务ak,sk请求方式的使用示例
 */
var content = require("./moderation_sdk/image_moderation_batch");
var utils = require("./moderation_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京(cn-north-4)、亚太-香港(ap-southeast-1)等区域信息
utils.initRegion("cn-north-4");

var app_key = "*************";
var app_secret = "*************";

var filepath = "./data/moderation-terrorism.jpg";
var data = utils.changeFileToBase64(filepath);

// obs链接需要和region区域一致，不同的region的obs资源不共享
url1 = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/terrorism.jpg";
url2 = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/antiporn.jpg";

content.image_content_batch_aksk(app_key, app_secret, [url1, url2], ["politics", "terrorism", "porn"], 0, function (result) {
    console.log(result);
});