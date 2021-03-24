<?php
/**
 * 内容审核服务请求url常量及配置信息
 * Created by PhpStorm.
 * User: Administrator
 * Date: 2018/11/16
 * Time: 10:44
 */

// iva 请求域名
define("IVA_ENDPOINT", "iva.cn-north-4.myhuaweicloud.com");

// token请求域名
define("IAM_ENPOINT", "iam.myhuaweicloud.com");

// token请求uri
define("AIS_TOKEN", "/v3/auth/tokens");

// 图像清晰度的检测服务uri
define("IMAGE_CLARITY_DETECT", "/v1.0/moderation/image/clarity-detect");

// 扭曲校正服务的uri
define("DISTORTION_CORRECT", "/v1.0/moderation/image/distortion-correct");

// 图片内容审核服务的uri
define("IMAGE_CONTENT_DETECT", "/v1.0/moderation/image");

// 文本内容检测服务的uri
define("MODERATION_TEXT", "/v1.0/moderation/text");

// 长视频审核的uri
define("MODERATION_LONG_VIDEO", "/v2/%s/services/video-moderation/tasks");

// 长视频审核结果查询的uri
define("MODERATION_LONG_VIDEO_RESULT", "/v2/%s/services/video-moderation/tasks/%s");

// 图像内容审核批量异步uri
define("IMAGE_CONTENT_BATCH_JOBS", "/v1.0/moderation/image/batch/jobs");

// 图像内容审核批量异步结果uri
define("IMAGE_CONTENT_BATCH_RESULT", "/v1.0/moderation/image/batch");

// 图片内容审核服务批量的uri
define("IMAGE_CONTENT_BATCH", "/v1.0/moderation/image/batch");

//内容审核服务类别
define("MODERATION", "moderation");

// 异步查询任务失败最大重试次数
define("RETRY_MAX_TIMES", 3);