package com.huawei.ais.demo.moderation;

import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.demo.ResponseProcessUtils;
import com.huawei.ais.demo.ServiceAccessBuilder;
import com.huawei.ais.sdk.AisAccess;
import com.huawei.ais.sdk.AisAccessWithProxy;
import com.huawei.ais.sdk.util.HttpClientUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;

import java.io.File;
import java.io.IOException;

/**
 *  扭曲矫正服务的使用示例类
 */
public class ModerationDistortionCorrectDemo {

	private AisAccess service;

	public ModerationDistortionCorrectDemo() {

		// 1. 配置好访问图像扭曲矫正服务的基本信息,生成对应的一个客户端连接对象
		service = ServiceAccessBuilder.builder()
				.ak("######")                       // your ak
				.sk("######")                       // your sk
				.region("cn-north-4")               // 内容审核服务目前支持华北-北京(cn-north-4)
				.connectionTimeout(10000)            // 连接目标url超时限制
				.connectionRequestTimeout(5000)     // 连接池获取可用连接超时限制
				.socketTimeout(20000)               // 获取服务器响应数据超时限制
				.retryTimes(3)                      // 请求异常时的重试次数
				.build();

	}
	//
	// 扭曲矫正服务的使用示例函数
	//
	private void moderationDistortionCorrectDemo(byte[] fileData) throws IOException {
		try {
			//
			// 2.构建访问扭曲矫正服务需要的参数
			//
			String uri = "/v1.0/moderation/image/distortion-correct";
			String fileBase64Str = Base64.encodeBase64String(fileData);

			// api请求参数说明可参考：https://support.huaweicloud.com/api-moderation/moderation_03_0014.html
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str); //检测图片
			json.put("correction", true); //判断是否进行扭曲矫正
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 3.传入扭曲矫正服务对应的uri参数, 传入扭曲矫正服务需要的参数，
			// 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
			HttpResponse response = service.post(uri, stringEntity);

			// 4.验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
			if(ResponseProcessUtils.isRespondedOK(response)) {
				String result = HttpClientUtils.convertStreamToString(response.getEntity().getContent());
				System.out.println(result);
				ResponseProcessUtils.processResponseWithImage(result, "data/moderation-distortion-file.corrected.jpg");
			} else {
				// 5.处理服务返回的字符流，输出识别结果。
				ResponseProcessUtils.processResponseStatus(response);
				ResponseProcessUtils.processResponse(response);
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// 6.使用完毕，关闭服务的客户端连接			
			service.close();
		}
	}

	private void moderationDistortionCorrectDemo(String url) throws IOException {
		try {
			//
			// 2.构建访问扭曲矫正服务需要的参数
			//
			String uri = "/v1.0/moderation/image/distortion-correct";

			// api请求参数说明可参考：https://support.huaweicloud.com/api-moderation/moderation_03_0014.html
			JSONObject json = new JSONObject();
			json.put("url", url); //检测图片
			json.put("correction", true); //判断是否进行扭曲矫正
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 3.传入扭曲矫正服务对应的uri参数, 传入扭曲矫正服务需要的参数，
			// 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
			HttpResponse response = service.post(uri, stringEntity);

			// 4.验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
			if(ResponseProcessUtils.isRespondedOK(response)) {
				String result = HttpClientUtils.convertStreamToString(response.getEntity().getContent());
				System.out.println(result);
				ResponseProcessUtils.processResponseWithImage(result, "data/moderation-distortion-url.corrected.jpg");
			} else {
				// 5.处理服务返回的字符流，输出识别结果。
				ResponseProcessUtils.processResponseStatus(response);
				ResponseProcessUtils.processResponse(response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// 6.使用完毕，关闭服务的客户端连接
			service.close();
		}
	}
	//
	// 主入口函数
	//
	public static void main(String[] args) throws IOException {
		// 测试入口函数
		ModerationDistortionCorrectDemo tools = new ModerationDistortionCorrectDemo();
		byte[] fileData = FileUtils.readFileToByteArray(new File("data/modeation-distortion.jpg"));
		tools.moderationDistortionCorrectDemo(fileData);

		String url = "https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/modeation-distortion.jpg";
		tools.moderationDistortionCorrectDemo(url);
	}
}
