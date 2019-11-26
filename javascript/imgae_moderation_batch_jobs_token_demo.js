/**
 * 图像内容检测批量异步服务token请求方式的使用示例
 */
var content = require("./moderation_sdk/image_moderation_batch_jobs");
var token = require("./moderation_sdk/gettoken");
var utils = require("./moderation_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京(cn-north-4)、亚太-香港(ap-southeast-1)等区域信息
utils.initRegion("cn-north-4");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码

// obs链接需要和region区域一致，不同的region的obs资源不共享
url1 = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/terrorism.jpg";
url2 = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/antiporn.jpg";

token.getToken(username, domain_name, password, function (token) {

    content.batch_jobs(token, [url1, url2], ["politics", "terrorism", "porn"], function (result) {
        console.log(result);
    });

});