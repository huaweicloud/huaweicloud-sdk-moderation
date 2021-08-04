# -*- coding:utf-8 -*-
from moderation_sdk.moderation_long_video import moderation_long_video_aksk,get_moderation_input,get_moderation_output,get_moderation_requestbody
from moderation_sdk.utils import init_global_env
if __name__ == '__main__':
    init_global_env("cn-north-4")

    #
    # access long video moderation,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '*************'

    project_id = '*************'

    input_type = "url"
    url = "https://moderation-sdk-video.obs.cn-north-4.myhuaweicloud.com/input/demo.mp4"
    input_param = get_moderation_input(input_type, None, None, url)

    output_bucket = "moderation-sdk-video"
    output_path = "output/"
    output_param = get_moderation_output(output_bucket, output_path)

    task_name = "task-demo"
    description = "description"
    request_body = get_moderation_requestbody(task_name, description, input_param, output_param,
                                              "porn,terrorism,politics",
                                              "ad,politics,porn,abuse,contraband,flood")
    # call interface use the url
    result = moderation_long_video_aksk(app_key, app_secret, project_id, request_body)
    print(result)

    # call interface use the obs
    input_type = "obs"
    input_bucket = "moderation-sdk-video"
    input_path = "input/demo.mp4"
    input_param = get_moderation_input(input_type, input_bucket, input_path, None)

    request_body = get_moderation_requestbody(task_name, description, input_param, output_param,
                                             "porn,terrorism,politics",
                                             "ad,politics,porn,abuse,contraband,flood")

    result = moderation_long_video_aksk(app_key, app_secret, project_id, request_body)
    print(result)

