package com.huawei.ais.demo.moderation;

import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.demo.ResponseProcessUtils;
import com.huawei.ais.demo.ServiceAccessBuilder;
import com.huawei.ais.sdk.AisAccess;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *  语音内容检测服务的使用示例类
 */
public class ModerationVoiceContentDemo {

	//
	// 1. 在ClientContextUtils类中, 配置好访问语音内容检测服务的基本信息,
	// 然后，在此处生成对应的一个客户端连接对象
	// 设置三个超时参数限制连接超时，分别如下
	private int connectionTimeout = 5000; //连接目标url超时限制
	private int connectionRequestTimeout = 1000;//连接池获取可用连接超时限制
	private int socketTimeout = 5000;//获取服务器响应数据超时限制

	private AisAccess service;

	public ModerationVoiceContentDemo() {
		// 1. 配置好访问语音内容检测服务的基本信息,生成对应的一个客户端连接对象
		service = ServiceAccessBuilder.builder()
				.ak("######")                       // your ak
				.sk("######")                       // your sk
				.region("cn-north-1")               // 内容审核服务目前支持华北-北京一(cn-north-1)以及亚太-香港(ap-southeast-1)
				.connectionTimeout(5000)            // 连接目标url超时限制
				.connectionRequestTimeout(1000)     // 连接池获取可用连接超时限制
				.socketTimeout(20000)               // 获取服务器响应数据超时限制
				.build();

	}

	private void voiceContentCheck(byte[] imagebytes) throws IOException {
		try {
			//
			// 2.构建访问语音内容检测服务需要的参数
			//
			String uri = "/v1.0/moderation/voice";
			
			String fileBase64Str = Base64.encodeBase64String(imagebytes);
						
			JSONObject json = new JSONObject();
			
			json.put("data", fileBase64Str);
			json.put("categories", new String[] {"politics","porn","ad","abuse"}); //检测内容
			
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 3.传入语音内容检测服务对应的uri参数, 传入语音内容检测服务需要的参数，
			// 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
			HttpResponse response = service.post(uri, stringEntity);

			// 4.验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
			ResponseProcessUtils.processResponseStatus(response);

			// 5.处理服务返回的字符流，输出识别结果。
			ResponseProcessUtils.processResponse(response);
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
		ModerationVoiceContentDemo tool = new ModerationVoiceContentDemo();

		byte[] imageBytes = FileUtils.readFileToByteArray(new File("data/moderation-voice.wav"));
		tool.voiceContentCheck(imageBytes);
	}
}
