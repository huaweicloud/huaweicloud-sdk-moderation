/**
 * 内容审核服务类请求url常量配置信息
 * @type
 */
var ais = {

    // token请求域名
    IAM_ENPOINT: 'iam.myhuaweicloud.com',

    // token请求uri
    AIS_TOKEN: '/v3/auth/tokens',

    // 图像清晰度的检测服务uri
    IMAGE_CLARITY_DETECT: '/v1.0/moderation/image/clarity-detect',

    // 扭曲校正服务的uri
    DISTORTION_CORRECT: '/v1.0/moderation/image/distortion-correct',

    // 图片内容审核服务的uri
    IMAGE_CONTENT_DETECT: '/v1.0/moderation/image',

    // 文本内容检测服务的uri
    MODERATION_TEXT: '/v1.0/moderation/text',

    // 视频审核服务的uri
    MODERATION_VIDEO: '/v1.0/moderation/video',

    // 图像内容审核批量异步uri
    IMAGE_CONTENT_BATCH_JOBS: '/v1.0/moderation/image/batch/jobs',

    // 图像内容审核批量异步结果uri
    IMAGE_CONTENT_BATCH_RESULT: '/v1.0/moderation/image/batch',

    // 图片内容审核服务的批量
    IMAGE_CONTENT_BATCH: '/v1.0/moderation/image/batch',

    // moderation 服务的服务类别
    MODERATION_SERVICE: 'moderation',

    // 最大重试次数
    RETRY_TIMES_MAX: 3

};
module.exports = ais;