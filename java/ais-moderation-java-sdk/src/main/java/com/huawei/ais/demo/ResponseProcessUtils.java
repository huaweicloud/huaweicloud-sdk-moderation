package com.huawei.ais.demo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.cloud.sdk.util.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import com.huawei.ais.sdk.util.HttpClientUtils;

/**
 * 访问服务返回结果信息验证的工具类
 */
public class ResponseProcessUtils {
	private static final Logger logger = LoggerFactory.getLogger(ResponseProcessUtils.class);
	/**
	 * 打印出服务访问完成的HTTP状态码 
	 * 
	 * @param response 响应对象
	 */
	public static void processResponseStatus(HttpResponse response) {
		System.out.println(response.getStatusLine().getStatusCode());
	}
	
	/**
	 * 打印响应的状态码，并检测是否为200
	 * 
	 * @param response 
	 * 			响应对象
	 * @return 如果状态码为200则返回true，否则返回false
	 * 
	 */
	public static boolean isRespondedOK(HttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println(statusCode);
		return HttpStatus.SC_OK == statusCode;
	}
	
	/**
	 * 打印出服务访问完成后，转化为文本的字符流，主要用于JSON数据的展示
	 * 
	 * @param response 响应对象
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public static void processResponse(HttpResponse response) throws UnsupportedOperationException, IOException {
		System.out.println(HttpClientUtils.convertStreamToString(response.getEntity().getContent()));
	}
	
	/**
	 * 处理返回Base64编码的图像文件的生成
	 * 
	 * @param result
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public static void processResponseWithImage(String result, String fileName) throws UnsupportedOperationException, IOException {
		JSONObject resp = JSON.parseObject(result);
		JSONObject responseRlt = (JSONObject) resp.get("result");
		String imageString = (String)responseRlt.get("data");

		if (StringUtils.isNullOrEmpty(imageString)){
			logger.info("The result without file string of base64, response {} ", resp);
		}else {
			byte[] fileBytes = new BASE64Decoder().decodeBuffer(imageString);
			writeBytesToFile(fileName, fileBytes);
		}
	}
	
	/**
	 *  将字节数组写入到文件, 用于支持二进制文件(如图片)的生成
	 * @param fileName 文件名
	 * @param data 数据
	 * @throws IOException
	 */
	public static void writeBytesToFile(String fileName, byte[] data) throws IOException{
        
		FileChannel fc = null;
		try {
			ByteBuffer bb = ByteBuffer.wrap(data);
			fc = new FileOutputStream(fileName).getChannel();
			fc.write(bb);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Failed to generate file is faild, cause {}", e);
		}
		finally {
			fc.close();
		}
    }
}


