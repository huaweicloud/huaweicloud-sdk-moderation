# -*- coding:utf-8 -*-
from moderation_sdk.moderation_text import moderation_text_aksk
from moderation_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing(cn-north-4), Asia Pacific-Hong Kong(ap-southeast-1)
    init_global_env('cn-north-4')

    #
    # access moderation text enhance,posy data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    # call interface use the text
    result = moderation_text_aksk(app_key, app_secret, '666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666', 'content')
    print(result)