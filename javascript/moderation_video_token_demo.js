/**
 * 视频审核服务token方式请求的使用示例
 */
var video = require("./moderation_sdk/moderation_video");
var token = require("./moderation_sdk/gettoken");
var utils = require("./moderation_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京(cn-north-4)、亚太-香港(ap-southeast-1)等区域信息
utils.initRegion("cn-north-4");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码

// obs链接需要和region区域一致，不同的region的obs资源不共享
demo_data_url = "https://obs-image-bj4.obs.cn-north-4.myhuaweicloud.com/bgm_recognition";

token.getToken(username, domain_name, password, function (token) {

    video.video(token, demo_data_url, 1, ["terrorism", "porn", "politics"], function (result) {
        console.log(result);
    })

});


