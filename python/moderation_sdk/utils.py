# -*- coding:utf-8 -*-
import sys
import os
import ssl
import base64
import json
import moderation_sdk.ais as ais

_context = None
if not ais.AisService.CERTIFICATE_VALIDATION:
    _context = ssl._create_unverified_context()

default_timeout = ais.AisService.DEFAULT_TIMEOUT

if sys.version_info.major < 3:
    import urllib
    import urllib2


    def request_token(_url, _data, token, timeout=default_timeout):
        kreq = urllib2.Request(url=_url)
        kreq.add_header('Content-Type', 'application/json')
        kreq.add_header('X-Auth-Token', token)
        kreq.add_data(json.dumps(_data))

        resp = None
        status_code = None
        try:
            r = urllib2.urlopen(kreq, context=_context, timeout=timeout)

        #
        # We use HTTPError and URLError，because urllib2 can't process the 4XX &
        # 500 error in the single urlopen function.
        #
        # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
        # there is no this problem.
        #
        except urllib2.HTTPError as e:
            resp = e.read()
            status_code = e.code
        except urllib2.URLError as e:
            resp = e.reason
        else:
            status_code = r.code
            resp = r.read()
        return status_code, resp

    def request_job_result_token(_url, token):
        kreq = urllib2.Request(url=_url)
        kreq.add_header('X-Auth-Token', token)
        kreq.add_header('Content-Type', 'application/json')

        resp = None
        status_code = None
        try:
            r = urllib2.urlopen(kreq, context=_context, timeout=default_timeout)

            #
            # We use HTTPError and URLError，because urllib2 can't process the 4XX &
            # 500 error in the single urlopen function.
            #
            # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
            # there is no this problem.
            #
        except urllib2.HTTPError as e:
            resp = e.read()
            status_code = e.code
        except urllib2.URLError as e:
            resp = e.reason
        else:
            status_code = r.code
            resp = r.read()
        return status_code, resp

    def request_aksk(sig, kreq, _url, timeout=default_timeout):
        resp = None
        status_code = None
        try:
            sig.Sign(kreq)
            req = urllib2.Request(url=_url, data=kreq.body, headers=kreq.headers)
            r = urllib2.urlopen(req, context=_context, timeout=timeout)
        #
        # We use HTTPError and URLError，because urllib2 can't process the 4XX &
        # 500 error in the single urlopen function.
        #
        # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
        # there is no this problem.
        #
        except urllib2.HTTPError as e:
            resp = e.read()
            status_code = e.code
        except urllib2.URLError as e:
            resp = e.reason
        else:
            status_code = r.code
            resp = r.read()
        return status_code, resp


    def request_job_result_aksk(sig, kreq, _url):
        resp = None
        status_code = None
        try:
            sig.Sign(kreq)
            req = urllib2.Request(url=_url, headers=kreq.headers)
            r = urllib2.urlopen(req, context=_context, timeout=default_timeout)

        #
        # We use HTTPError and URLError，because urllib2 can't process the 4XX &
        # 500 error in the single urlopen function.
        #
        # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
        # there is no this problem.
        #
        except urllib2.HTTPError as e:
            resp = e.read()
            status_code = e.code
        except urllib2.URLError as e:
            resp = e.reason
        else:
            status_code = r.code
            resp = r.read()
        return status_code, resp

    def download_url_base64(url):
        return base64.b64encode(urllib.urlopen(url).read())
else:
    import urllib.parse
    import socket
    import urllib.request
    from urllib.error import URLError, HTTPError

    socket.setdefaulttimeout(default_timeout)

    def request_token(_url, _data, token):
        _headers = {
            "Content-Type": "application/json",
            "X-Auth-Token": token
        }

        data = bytes(json.dumps(_data), 'utf8')
        kreq = urllib.request.Request(_url, data, _headers)

        resp = None
        status_code = None
        try:
            r = urllib.request.urlopen(kreq, context=_context)

        #
        # We use HTTPError and URLError，because urllib2 can't process the 4XX &
        # 500 error in the single urlopen function.
        #
        # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
        # there is no this problem.
        #
        except HTTPError as e:
            resp = e.read()
            status_code = e.code
        except URLError as e:
            resp = e.reason
        else:
            status_code = r.code
            resp = r.read()
        return status_code, resp

    def request_job_result_token(_url, token):
        _headers = {
            "Content-Type": "application/json",
            "X-Auth-Token": token
        }
        kreq = urllib.request.Request(_url, headers=_headers)

        resp = None
        status_code = None
        try:
            r = urllib.request.urlopen(kreq, context=_context)
        #
        # We use HTTPError and URLError，because urllib can't process the 4XX &
        # 500 error in the single urlopen function.
        #
        # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
        # there is no this problem.
        #
        except HTTPError as e:
            resp = e.read()
            status_code = e.code
        except URLError as e:
            resp = e.reason
        else:
            status_code = r.code
            resp = r.read()
        return status_code, resp.decode('utf-8')

    def request_aksk(sig, kreq, _url):
        resp = None
        status_code = None
        try:
            sig.Sign(kreq)
            req = urllib.request.Request(url=_url, data=kreq.body, headers=kreq.headers)
            r = urllib.request.urlopen(req, context=_context)
        #
        # We use HTTPError and URLError，because urllib can't process the 4XX &
        # 500 error in the single urlopen function.
        #
        # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
        # there is no this problem.
        #
        except HTTPError as e:
            resp = e.read()
            status_code = e.code
        except URLError as e:
            resp = e.reason
        else:
            status_code = r.code
            resp = r.read()
        return status_code, resp


    def request_job_result_aksk(sig, kreq, _url):
        resp = None
        status_code = None
        try:
            sig.Sign(kreq)
            req = urllib.request.Request(url=_url, headers=kreq.headers)
            r = urllib.request.urlopen(req, context=_context)
        #
        # We use HTTPError and URLError，because urllib can't process the 4XX &
        # 500 error in the single urlopen function.
        #
        # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
        # there is no this problem.
        #
        except HTTPError as e:
            resp = e.read()
            status_code = e.code
        except URLError as e:
            resp = e.reason
        else:
            status_code = r.code
            resp = r.read()
        return status_code, resp.decode('utf-8')

    def download_url_base64(url):
        return base64.b64encode(urllib.request.urlopen(url).read())

    def update_socket_time(timeout):
        socket.setdefaulttimeout(timeout)


_ENDPOINT = {
    'moderation': {
        'cn-north-1':'moderation.cn-north-1.myhuaweicloud.com',
        'cn-north-4':'moderation.cn-north-4.myhuaweicloud.com',
        'ap-southeast-1':'moderation.ap-southeast-1.myhuaweicloud.com',
        'cn-east-3':'moderation.cn-east-3.myhuaweicloud.com',
        'ap-southeast-3':'moderation.ap-southeast-3.myhuaweicloud.com'
    },
    'iva': {
        'cn-north-1':'iva.cn-north-1.myhuaweicloud.com',
        'cn-north-4':'iva.cn-north-4.myhuaweicloud.com',
        'cn-east-3':'iva.cn-east-3.myhuaweicloud.com'
    }
}


def encode_to_base64(filename):
    """
    encoding file to base64 encoded stream text
    :param filename:
    :return:
    """
    imgstr = ""
    with open(filename, 'rb') as file:
        imgstr = base64.b64encode(file.read())
    return imgstr

def decode_to_wave_file(base64_encoded_str, filename):
    '''
    decode base64 stream to wave file
    :param base64_encoded_str:
    :return:
    '''
    wave_data = base64.b64decode(base64_encoded_str)
    wf = open(filename, 'wb')
    wf.write(wave_data)
    wf.close()

def get_endpoint(type):
    region_name = os.environ.get(ais.AisService.REGION_MSG)
    return _ENDPOINT[type].get(region_name)

def get_region():
    return os.environ.get(ais.AisService.REGION_MSG)

def init_global_env(region):
    os.environ[ais.AisService.REGION_MSG] = region

def reset_global_env():
    os.environ[ais.AisService.REGION_MSG] = "cn-north-1"

