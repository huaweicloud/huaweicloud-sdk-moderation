#!/usr/bin/python
# -*- coding: utf-8 -*-

class AisEndpoint:
    IAM_ENPOINT = 'iam.myhuaweicloud.com'

class ModerationURI:
    IMAGE_CLARITY_DETECT = '/v1.0/moderation/image/clarity-detect'
    DISTORTION_CORRECT = '/v1.0/moderation/image/distortion-correct'
    IMAGE_ANTI_PORN = '/v1.0/moderation/image/anti-porn'
    DARK_ENHANCE = '/v1.0/vision/dark-enhance'
    DEFOG = '/v1.0/vision/defog'
    SURPER_RESOLUTION = '/v1.0/vision/super-resolution'

class AisService:
    MODERATION_SERVICE = 'moderation'
    REGION_MSG = 'region_name'