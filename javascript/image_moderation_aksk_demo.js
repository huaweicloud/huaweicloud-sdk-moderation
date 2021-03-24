/**
 * 图像内容检测服务ak,sk请求方式的使用示例
 */
var content = require("./moderation_sdk/image_moderation");
var utils = require("./moderation_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京(cn-north-4)、华东上海一(cn-east-3)、亚太-香港(ap-southeast-1)、亚太-新加坡(ap-southeast-3)等区域信息
utils.initRegion("cn-north-4");

var app_key = "*************";
var app_secret = "*************";

var filepath = "./data/moderation-terrorism.jpg";
var data = utils.changeFileToBase64(filepath);

// obs链接需要和region区域一致，不同的region的obs资源不共享
demo_data_url = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/terrorism.jpg";

content.image_content_aksk(app_key, app_secret, data, "", ["politics"], "", "default", function (result) {
    console.log(result);
});

content.image_content_aksk(app_key, app_secret, "", demo_data_url, ["politics"], "", "default", function (result) {
    console.log(result);
});