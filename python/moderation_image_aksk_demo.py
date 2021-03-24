# -*- coding:utf-8 -*-
from moderation_sdk.utils import encode_to_base64
from moderation_sdk.moderation_image import moderation_image_aksk
from moderation_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing(cn-north-4),China East-Shanghai1(cn-east-3) Asia Pacific-Hong Kong(ap-southeast-1)
    init_global_env('cn-north-4')

    #
    # access moderation image,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url = 'https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/terrorism.jpg'

    # call interface use the local file
    result = moderation_image_aksk(app_key, app_secret, encode_to_base64('data/moderation-terrorism.jpg'), url=None,
                                   categories=['porn', 'politics', 'terrorism', 'ad'],
                                   threshold=None, moderation_rule='default')
    print(result)

    # call interface use the url
    result = moderation_image_aksk(app_key, app_secret, image=None, url=demo_data_url,
                                   categories=['porn', 'politics', 'terrorism', 'ad'],
                                   threshold=None, moderation_rule='default')
    print(result)


