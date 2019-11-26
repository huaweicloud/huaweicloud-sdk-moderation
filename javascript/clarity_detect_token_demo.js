/**
 * 图像清晰度检测服务token 方式请求的使用示例
 */
var clarity = require("./moderation_sdk/clarity_detect");
var token = require("./moderation_sdk/gettoken");
var utils = require("./moderation_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京(cn-north-4)区域信息
utils.initRegion("cn-north-4");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码

var filepath = "./data/moderation-clarity-detect.jpg";
var data = utils.changeFileToBase64(filepath);

// obs链接需要和region区域一致，不同的region的obs资源不共享
demo_data_url = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/vat-invoice.jpg";

token.getToken(username, domain_name, password, function (token) {

    clarity.clarity_detect(token, data, "", 0.8, function (result) {
        console.log(result);
    });

    clarity.clarity_detect(token, "", demo_data_url, 0.8, function (result) {
        console.log(result);
    });
});
