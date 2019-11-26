/**
 * 扭曲校正服务token 方式请求的使用示例
 */
var discor = require("./moderation_sdk/distortion_correct");
var token = require("./moderation_sdk/gettoken");
var utils = require("./moderation_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京(cn-north-4)区域信息
utils.initRegion("cn-north-4");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码

var filepath = "./data/moderation-distortion.jpg";
var data = utils.changeFileToBase64(filepath);

// obs链接需要和region区域一致，不同的region的obs资源不共享
demo_data_url = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/vat-invoice.jpg";

token.getToken(username, domain_name, password, function (token) {

    discor.distortion_correct(token, data, "", true, function (result) {
        var resultObj = JSON.parse(result);

        if (resultObj.result.data !== "") {
            utils.getFileByBase64Str("./data/modeation_distortion-token-1.jpg", resultObj.result.data);
        }else{
            console.log(result);
        }
    });

    discor.distortion_correct(token, "", demo_data_url, true, function (result) {
        var resultObj = JSON.parse(result);

        if (resultObj.result.data !== "") {
            utils.getFileByBase64Str("./data/modeation_distortion-token-2.jpg", resultObj.result.data);
        }else{
            console.log(result);
        }
    });
});