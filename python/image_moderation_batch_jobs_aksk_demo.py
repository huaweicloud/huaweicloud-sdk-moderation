# -*- coding:utf-8 -*-
from moderation_sdk.image_moderation_batch_jobs import image_batch_jobs_aksk
from moderation_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing(cn-north-4), CN-Hong Kong(ap-southeast-1)
    init_global_env('cn-north-4')

    #
    # access moderation image of batch jobs,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url1 = 'https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/terrorism.jpg'
    demo_data_url2 = 'https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/antiporn.jpg'

    # call interface use the url
    result = image_batch_jobs_aksk(app_key, app_secret, [demo_data_url1, demo_data_url2],
                                   ['porn', 'politics', 'terrorism', 'ad'])
    print(result)


