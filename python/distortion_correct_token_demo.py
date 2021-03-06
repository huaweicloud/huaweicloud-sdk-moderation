# -*- coding:utf-8 -*-
from moderation_sdk.gettoken import get_token
from moderation_sdk.utils import encode_to_base64
from moderation_sdk.utils import decode_to_wave_file
from moderation_sdk.distortion_correct import distortion_correct
from moderation_sdk.utils import init_global_env
import json

if __name__ == '__main__':
    # Services currently support North China-Beijing(cn-north-4)
    init_global_env('cn-north-4')

    #
    # access moderation distortion correct post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url = 'https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/modeation-distortion.jpg'
    token = get_token(user_name, password, account_name)

    #call interface use the url correction is true means do not correction
    result = distortion_correct(token, '', demo_data_url, True)
    result_obj = json.loads(result)

    # If the image needs to be corrected, the corrected image will be generated in the data directory,
    # otherwise the processing result will be printed.
    if result_obj['result']['data'] !='':
        decode_to_wave_file(result_obj['result']['data'], 'data/modeation-distortion-token-1.png')
    else:
        print(result)

    # call interface use the file
    result = distortion_correct(token, encode_to_base64('data/modeation-distortion.jpg'), '', True)
    result_obj = json.loads(result)

    # If the image needs to be corrected, the corrected image will be generated in the data directory,
    # otherwise the processing result will be printed.
    if result_obj['result']['data'] != '':
        decode_to_wave_file(result_obj['result']['data'], 'data/modeation-distortion-token-2.png')
    else:
        print(result)
