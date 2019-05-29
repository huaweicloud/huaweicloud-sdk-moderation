# 内容审核服务curl使用示例

## 1.获取Token
### 1.1创建 data.json文件，文件内容如下， 其中，需要修改USERNAME, PASSWORD, project中的name目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)
```json
{
    "auth": {
        "identity": {
           
            "password": {
                "user": {
                    "name": "USERNAME", 
                    "password": "PASSWORD", 
                    "domain": {
                        "name": "USERNAME"
                    }
                }
            },
             "methods": [
                "password"
            ]
        }, 
        "scope": {
            "project": {
                "name": "cn-north-1"
            }
        }
    }
}
```
使用如下命令取得Token, 该Token在Header中：
```bash
curl -X POST https://iam.cn-north-1.myhuaweicloud.com/v3/auth/tokens --header 'content-type: application/json'  -d "@data.json"
```
## 2.申请开通服务
 
内容审核服务申请参考链接
 
https://support.huaweicloud.com/api-moderation/moderation_03_0013.html  

## 3.访问图像内容审核服务
### 3.1 图像内容审核服务

**图片的base编码方式**
1. 创建data.json文件，内容如下, 其中image的内容为图片的base64编码
```json
{
  "image":"/9j/4AAQSkZJRgABAgEASABIAAD/4RFZRXhpZgAATU0AKgAAAAgABwESAAMAAAABAAEAAAEaAAUAAAABAAAAYgEbAAUAAAABAAAAagEoAAMAAAABAAIAAAExAAIAAAAcAAAAcgEyAAIAAAAUAAAAjodpAAQAAAABAAAApAAAANAACvyAAAAnEAAK/IAAACcQQWRvYmUgUGhvdG9zaG9w......",
  "categories":["politics", "terrorism", "porn"],
  "threshold":0
}
```
使用如下命令访问
```bash
curl -X POST https://moderation.cn-north-1.myhuaweicloud.com/v1.0/moderation/image \ --header 'content-type: application/json' \ --header 'x-auth-token: xxxxxxx' -d "@data.json"
```

**OBS URL方式**
1. 创建data.json文件，内容如下, 其中url为华为公有云OBS服务上用户临时读授权的URL, 或者公开读的URL 
```json
{
      "url":"https://ais-sample-data.obs.myhuaweicloud.com/tagging-normal.jpg",
      "language": "zh",
      "limit": 5,
      "threshold": 30.0
}
```
使用如下命令访问
注：亚太-香港(ap-southeast-1) 使用链接：https://moderation.ap-southeast-1.myhuaweicloud.com/v1.0/moderation/image
```bash
curl -X POST https://moderation.cn-north-1.myhuaweicloud.com/v1.0/moderation/image \ --header 'content-type: application/json' \ --header 'x-auth-token: xxxxxxx' -d "@data.json"
```
