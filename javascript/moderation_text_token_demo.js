/**
 *  图像内容检测服务token请求方式的使用示例
 */
var text = require("./moderation_sdk/moderation_text");
var token = require("./moderation_sdk/gettoken");
var utils = require("./moderation_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京(cn-north-4)、华东上海一(cn-east-3)、中国-香港(ap-southeast-1)、亚太-新加坡(ap-southeast-3)等区域信息
utils.initRegion("cn-north-4");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码

token.getToken(username, domain_name, password, function (token) {

    text.moderation_text(token, [{
            "text": "666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666",
            "type": "content"}],
        ["ad", "abuse", "politics", "porn", "contraband"], function (result) {
            console.log(result);
        })

});
