# -*- coding:utf-8 -*-
from moderation_sdk.gettoken import get_token
from moderation_sdk.utils import encode_to_base64
from moderation_sdk.moderation_image import moderation_image
from moderation_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing(cn-north-4),China East-Shanghai1(cn-east-3), Asia Pacific-Hong Kong(ap-southeast-1)
    init_global_env('cn-north-4')

    #
    # access moderation image,post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url = 'https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/terrorism.jpg'
    token = get_token(user_name, password, account_name)

    # call interface use the local file
    result = moderation_image(token, encode_to_base64('data/moderation-terrorism.jpg'), url='',
                              categories=['porn', 'politics', 'terrorism', 'ad'],
                              threshold=None, moderation_rule="default")
    print(result)

    # call interface use the url (token, image, url, threshold=0.95, scene=None)
    result = moderation_image(token, image='', url=demo_data_url,
                              categories=['porn', 'politics', 'terrorism', 'ad'],
                              threshold=None, moderation_rule="default")
    print(result)