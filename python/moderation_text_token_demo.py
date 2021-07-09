# -*- coding:utf-8 -*-
from moderation_sdk.gettoken import get_token
from moderation_sdk.moderation_text import moderation_text
from moderation_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing(cn-north-4),China East-Shanghai1(cn-east-3), CN-Hong Kong(ap-southeast-1),AP-Singapore(ap-southeast-3)
    init_global_env('cn-north-4')

    #
    # access moderation text enhance,posy data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    token = get_token(user_name, password, account_name)

    # call interface use the text
    result = moderation_text(token, '666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666', 'content',
                             ['ad', 'politics', 'porn', 'abuse', 'contraband', 'flood'])
    print(result)
