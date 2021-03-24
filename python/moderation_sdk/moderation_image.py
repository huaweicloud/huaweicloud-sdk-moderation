# -*- coding:utf-8 -*-

import sys
import json
import moderation_sdk.ais as ais
import moderation_sdk.utils as utils
import moderation_sdk.signer as signer

#
# access moderation image,post data by token
#
def moderation_image(token, image, url, categories=None, threshold=None, moderation_rule="default"):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    _url = 'https://%s/v1.0/moderation/image' % endpoint

    if sys.version_info.major >= 3:
        if image:
            image = image.decode("utf-8")

    _data = {
        "image": image,
        "url": url,
        "categories": categories,
        "threshold": threshold,
        "moderation_rule": moderation_rule
    }

    status_code, resp = utils.request_token(_url, _data, token)
    if sys.version_info.major < 3:
        return resp
    else:
        return resp.decode('utf-8')


#
# access moderation image,post data by token
#
def moderation_image_aksk(_ak, _sk, image, url, categories=None, threshold=None, moderation_rule="default"):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    _url = 'https://%s/v1.0/moderation/image' % endpoint

    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    if sys.version_info.major >= 3:
        if image:
            image = image.decode("utf-8")

    _data = {
        "image": image,
        "url": url,
        "categories": categories,
        "threshold": threshold,
        "moderation_rule": moderation_rule
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endpoint
    kreq.uri = "/v1.0/moderation/image"
    kreq.method = "POST"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.body = json.dumps(_data)

    status_code, resp = utils.request_aksk(sig, kreq, _url)
    if sys.version_info.major < 3:
        return resp
    else:
        return resp.decode('utf-8')