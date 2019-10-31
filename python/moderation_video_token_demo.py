# -*- coding:utf-8 -*-
from moderation_sdk.gettoken import get_token
from moderation_sdk.moderation_video import moderation_video
from moderation_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing(cn-north-4), Asia Pacific-Hong Kong(ap-southeast-1)
    init_global_env('cn-north-4')

    #
    # access asr, long_sentence，post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url = 'https://obs-image-bj4.obs.cn-north-4.myhuaweicloud.com/video_moderation.mp4'
    token = get_token(user_name, password, account_name)

    # call interface use the url
    result = moderation_video(token, demo_data_url, 1, ['porn', 'politics', 'terrorism'])
    print(result)