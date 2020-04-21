# -*- coding:utf-8 -*-
from moderation_sdk.utils import encode_to_base64
from moderation_sdk.utils import decode_to_wave_file
from moderation_sdk.distortion_correct import distortion_correct_aksk
from moderation_sdk.utils import init_global_env
import json

if __name__ == '__main__':
    # Services currently support North China-Beijing(cn-north-4)
    init_global_env('cn-north-4')

    #
    # access moderation distortion correct post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url = 'https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/modeation-distortion.jpg'

    #call interface use the url correction is true means do not correction
    result = distortion_correct_aksk(app_key, app_secret, '', demo_data_url, True)
    result_obj = json.loads(result)

    # If the image needs to be corrected, the corrected image will be generated in the data directory,
    # otherwise the processing result will be printed.
    if result_obj['result']['data'] != '':
        decode_to_wave_file(result_obj['result']['data'], 'data/modeation-distortion-aksk-1.png')
    else:
        print(result)

    # call interface use the file
    result = distortion_correct_aksk(app_key, app_secret, encode_to_base64('data/modeation-distortion.jpg'), '', True)
    result_obj = json.loads(result)
    # If the image needs to be corrected, the corrected image will be generated in the data directory,
    # otherwise the processing result will be printed.
    if result_obj['result']['data'] != '':
        decode_to_wave_file(result_obj['result']['data'], 'data/modeation-distortion-aksk-2.png')
    else:
        print(result)