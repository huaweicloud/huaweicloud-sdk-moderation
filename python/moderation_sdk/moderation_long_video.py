# -*- coding:utf-8 -*-
import sys
import time
import json
import moderation_sdk.ais as ais
import moderation_sdk.utils as utils
import moderation_sdk.signer as signer

#
# moderation long video，post data by token
#
_RETRY_TIMES = 3

def moderation_long_video(token, project_id, requestbody):
    status, r = _moderation_long_video(project_id, token, requestbody)

    if status < 200 or status > 300:
        return r
    job_result_list = json.loads(r)
    for job_result in job_result_list:
        task_id = job_result["id"]
        time.sleep(1.0)

        retry_times = 0
        while True:
            status, r = _get_result(project_id, token, task_id)
            if status < 200 or status > 300:
                if retry_times < _RETRY_TIMES:
                    retry_times += 1
                    time.sleep(2.0)
                    continue
                else:
                    return r

            rec_result = json.loads(r)

            process_status = rec_result["state"]
            if process_status == 'PENDING' or process_status == "SCHEDULING" or process_status == "STARTING" or process_status =="RUNNING":
                time.sleep(15)
                continue
            elif process_status == 'CREATE_FAIL' or process_status == "FAILED" or process_status == "ABNORMAL":
                print("job failed")
                return rec_result
            else:
                hosting_result = rec_result['hosting_result']
                hosting_status = hosting_result['status']
                if hosting_status == "NOT_GENERATED":
                    time.sleep(15)
                    continue
                else:
                    return rec_result


#
# moderation long video, post the data
#
def _moderation_long_video(project_id, token, requestbody):
    endpoint = utils.get_endpoint(ais.AisService.IVA_SERVICE)
    _url = 'https://%s/v2/%s/services/video-moderation/tasks' % (endpoint, project_id)

    status, resp = utils.request_token(_url, requestbody, token)
    return status, resp


#
# access moderation long video task, get the result
#
def _get_result(project_id, token, job_id):
    endpoint = utils.get_endpoint(ais.AisService.IVA_SERVICE)
    _url_tmpl = 'https://%s/v2/%s/services/video-moderation/tasks/%s'
    _url = _url_tmpl % (endpoint, project_id, job_id)
    return utils.request_job_result_token(_url, token)



#
# access moderation long video，post data by ak,sk
#
def moderation_long_video_aksk(_ak, _sk, project_id, requestbody):
    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    status, r = _moderation_long_video_aksk(sig, project_id, requestbody)

    if status < 200 or status > 300:
        return r

    job_result_list = json.loads(r)
    for job_result in job_result_list:
        task_id = job_result["id"]
        # print "Process job id is :", job_id
        time.sleep(1.0)
        retry_times = 0
        while True:
            status, r = _get_result_aksk(sig, project_id, task_id)
            if status < 200 or status > 300:
                if retry_times < _RETRY_TIMES:
                    retry_times += 1
                    time.sleep(2.0)
                    continue
                else:
                    return r

            rec_result = json.loads(r)

            process_status = rec_result["state"]
            if process_status == 'PENDING' or process_status == "SCHEDULING" or process_status == "STARTING" or process_status == "RUNNING":
                time.sleep(15)
                continue
            elif process_status == 'CREATE_FAIL' or process_status == "FAILED" or process_status == "ABNORMAL":
                print("job failed")
                return rec_result
            else:
                hosting_result = rec_result['hosting_result']
                hosting_status = hosting_result['status']
                if hosting_status == "NOT_GENERATED":
                    time.sleep(15)
                    continue
                else:
                    return rec_result


#
# moderation long video, post the data
#
def _moderation_long_video_aksk(sig, project_id, requestbody):
    endpoint = utils.get_endpoint(ais.AisService.IVA_SERVICE)
    _url = 'https://%s/v2/%s/services/video-moderation/tasks' % (endpoint, project_id)

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endpoint
    kreq.uri = "/v2/%s/services/video-moderation/tasks" % project_id
    kreq.method = "POST"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.body = json.dumps(requestbody)

    status, resp = utils.request_aksk(sig, kreq, _url)
    if sys.version_info.major < 3:
        return status, resp
    else:
        return status, resp.decode('utf-8')


#
# access moderation long video, get the result
#
def _get_result_aksk(sig, project_id, task_id):
    endpoint = utils.get_endpoint(ais.AisService.IVA_SERVICE)
    _url_tmpl = 'https://%s/v2/%s/services/video-moderation/tasks/%s'
    _url = _url_tmpl % (endpoint, project_id, task_id)

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endpoint
    kreq.uri = "/v2/%s/services/video-moderation/tasks/%s" % (project_id, task_id)
    kreq.method = "GET"
    kreq.headers = {"Content-Type": "application/json"}

    return utils.request_job_result_aksk(sig, kreq, _url)

#
# get input param of moderation long video
#
def get_moderation_input(input_type, input_bucket, input_path, url):
    input_data = None

    if input_type == "obs":
        input_data = {
                        "type": input_type,
                        "data": [{
                            "bucket": input_bucket,
                            "path": input_path,
                            "index": 0
                        }]
                    }

    elif input_type == "url":
        input_data = {
                        "type": input_type,
                        "data": [{
                            "url": url
                     }]
}
    else:
        print("input parameter error")

    return input_data

#
# get output param of moderation long video
#
def get_moderation_output(bucket, path):
    output_data = {
                    "obs": {
                        "bucket": bucket,
                        "path": path
                    },
                    "hosting": {}
                }

    return output_data

#
# get requestbody param of moderation long video
#
def get_moderation_requestbody(taskname, description ,input, output, categories, text_categories):
    requestBody = {
                    "name": taskname,
                    "description": description,
                    "input": input,
                    "output": output,
                    "service_config": {
                        "common": {
                            "frame_interval": 5,
                            "categories": categories,
                            "text_categories": text_categories,
                            "use_sis": "false",
                            "use_ocr": "false",
                            "upload": "false"
                        }
                    },
                    "service_version": "1.2"
                }
    return requestBody