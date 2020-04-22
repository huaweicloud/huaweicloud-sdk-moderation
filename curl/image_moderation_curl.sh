#!/bin/sh
>data.json cat <<EOF
{
  "image":"",
  "url":"https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/terrorism.jpg",
  "categories":["politics", "terrorism", "porn"],
  "threshold":0
}
EOF
#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''
curl -X POST https://moderation.cn-north-4.myhuaweicloud.com/v1.0/moderation/image \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" \
  -d "@data.json"
rm -f headers data.json