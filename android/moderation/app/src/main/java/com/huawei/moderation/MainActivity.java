package com.huawei.moderation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cloud.sdk.util.StringUtils;
import com.huawei.moderation.utils.AccessService;
import com.huawei.moderation.utils.HttpClientUtils;
import com.huawei.moderation.utils.ServiceAccessBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //
    // 1.ak、sk方式需先初始化AccessService对象
    //
    private AccessService service = ServiceAccessBuilder.builder()
            .ak("######")
            .sk("######")
            .region("cn-north-4")
            .connectionTimeout(1000)
            .writeTimeout(5000)
            .readTimeout(5000)
            .build();
    private String result;
    private Bitmap bitmap;
    private String token = "";
    TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化图像内容审核数据
        ImageView imageView = findViewById(R.id.imageView);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.terrorism);
        imageView.setImageBitmap(bitmap);

        Button confirmBtn = (Button) findViewById(R.id.confirmBtn);
        resultView = findViewById(R.id.resultText);

        confirmBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 图像内容审核
                moderationImage(bitmap);

                // 文本审核
                // String moderationText = "luo聊请+我，微信110";
                // moderationText(moderationText);

                // 清晰度检测
                // moderationClarity(bitmap);

                // 图像内容审核批量
                // String[] urls = new String[]{"https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/terrorism.jpg"};
                // moderationImageBatch(urls);

                // 扭曲矫正
                // distortionCorrect(bitmap);

                // token方式示例
                String username = "######";
                String password = "######";
                String region = "cn-north-4";
                TokenDemo.projectName = region;
                tokenSample(username, password, region);
            }

        });
    }

    private void tokenSample(String username, String password, String region) {
        getToken(username, password, region, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result = e.getMessage();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                token = response.header("X-Subject-Token");
                // 图像内容审核
                // moderationImageToken(bitmap);

                // 文本审核
                // String moderationText = "luo聊请+我，微信110";
                // moderationTextToken(moderationText);

                // 清晰度检测
                // moderationClarity(bitmap);

                // 图像内容审核批量
                // String[] urls = new String[]{"https://sdk-obs-source-save.obs.cn-north-4.myhuaweicloud.com/terrorism.jpg"};
                // moderationImageBatchToken(urls);

                // 扭曲矫正
                // distortionCorrectToken(bitmap);
            }
        });
    }

    private void moderationImage(Bitmap bitmap) {
        ModerationImage initInstance = new ModerationImage(service);
        try {
            initInstance.moderationImageContent(bitmap, getCallback());
        } catch (Exception e) {
            e.printStackTrace();
            responseErrorMsg(e.getMessage());
        }

    }

    private void moderationImageToken(Bitmap bitmap) {
        try {
            if (StringUtils.isNullOrEmpty(token)) {
                responseErrorMsg("Token is null");
                return;
            }
            TokenDemo.moderationImageContent(token, bitmap, getCallback());
        } catch (Exception e) {
            e.printStackTrace();
            responseErrorMsg(e.getMessage());
        }

    }

    private void responseErrorMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void moderationClarity(Bitmap bitmap) {
        ModerationClarity initInstance = new ModerationClarity(service);
        try {
            initInstance.clarityDetectDemo(bitmap, getCallback());
        } catch (Exception e) {
            e.printStackTrace();
            responseErrorMsg(e.getMessage());
        }

    }

    private void moderationClarityToken(Bitmap bitmap) {
        try {
            if (StringUtils.isNullOrEmpty(token)) {
                responseErrorMsg("Token is null");
                return;
            }
            TokenDemo.moderationClarity(token, bitmap, getCallback());
        } catch (Exception e) {
            e.printStackTrace();
            responseErrorMsg(e.getMessage());
        }

    }

    private void moderationImageBatch(String[] urls) {
        ModerationImageBatchContent initInstance = new ModerationImageBatchContent(service);
        try {
            initInstance.imageContentBatchCheck(urls, getCallback());
        } catch (Exception e) {
            e.printStackTrace();
            responseErrorMsg(e.getMessage());
        }

    }

    private void moderationImageBatchToken(String[] urls) {
        try {
            if (StringUtils.isNullOrEmpty(token)) {
                responseErrorMsg("Token is null");
                return;
            }
            TokenDemo.moderationImageContentBatch(token, urls, getCallback());
        } catch (Exception e) {
            e.printStackTrace();
            responseErrorMsg(e.getMessage());
        }

    }

    private void moderationText(String moderationText) {
        ModerationTextContent initInstance = new ModerationTextContent(service);
        try {
            initInstance.moderationText(moderationText, getCallback());
        } catch (Exception e) {
            e.printStackTrace();
            responseErrorMsg(e.getMessage());
        }

    }

    private void moderationTextToken(String moderationText) {
        try {
            if (StringUtils.isNullOrEmpty(token)) {
                responseErrorMsg("Token is null");
                return;
            }
            TokenDemo.moderationTextContent(token, moderationText, getCallback());
        } catch (Exception e) {
            e.printStackTrace();
            responseErrorMsg(e.getMessage());
        }

    }

    private void distortionCorrect(Bitmap bitmap) {
        ModerationDistortionCorrect initInstance = new ModerationDistortionCorrect(service);
        try {
            initInstance.moderationDistortionCorrectDemo(bitmap, getCallback());
        } catch (Exception e) {
            e.printStackTrace();
            responseErrorMsg(e.getMessage());
        }

    }

    private void distortionCorrectToken(Bitmap bitmap) {
        try {
            if (StringUtils.isNullOrEmpty(token)) {
                responseErrorMsg("Token is null");
                return;
            }
            TokenDemo.moderationDistortionCorrect(token, bitmap, getCallback());
        } catch (Exception e) {
            e.printStackTrace();
            responseErrorMsg(e.getMessage());
        }
    }

    private void getToken(String username, String password, String region, Callback callback) {
        try {
            Call call = TokenDemo.getToken(username, password, region);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
            responseErrorMsg(e.getMessage());
        }
    }


    private Callback getCallback() {
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                result = e.toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultView.setText(result);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultStr = response.body().string();
                JSON json = JSON.parseObject(resultStr);
                result = HttpClientUtils.formatString(json.toJSONString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultView.setText(result);
                    }
                });

            }
        };
    }
}
