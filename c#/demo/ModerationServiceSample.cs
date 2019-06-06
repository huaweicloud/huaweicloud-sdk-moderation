using Moderation.Models;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;

namespace ModerationDemo
{
    class ModerationServiceSample
    {
        static void Main(string[] args)
        {
            // Services currently support North China-Beijing (cn-north-1,cn-north-4), Asia Pacific-Hong Kong (ap-southeast-1)
            String regionName = "*******";
            String username = "*******";
            String password = "*******";
            String domainName = "*******";

            // domain names for moderation service
            String MODERATION_ENDPOINT = ModerationService.getEndponit(regionName);

            // get token domain name 
            String IAM_ENPOINT = "iam.myhuaweicloud.com";

            String token = Authentication.GetToken(username, domainName, password, regionName, IAM_ENPOINT);

            // distortion correction service example
            DistortionCorrect(token, MODERATION_ENDPOINT);

            // clarity detect service example
            ClarityDetect(token, MODERATION_ENDPOINT);

            // image anti porn service example
            AntiPorn(token, MODERATION_ENDPOINT);

            // image content detect service example
            ImageContent(token, MODERATION_ENDPOINT);

            // moderation text detect service example
            ModerationText(token, MODERATION_ENDPOINT);

            // moderation video service example
            ModerationVideo(token, MODERATION_ENDPOINT);

            // image content batch jobs service example
            ImageContentBatchJobs(token, MODERATION_ENDPOINT);

            // image content batch service example
            ImageContentBatch(token, MODERATION_ENDPOINT);

        }

        private static void ClarityDetect(String token, String endpoint)
        {
            // The obs url of file
            String dataUrl = "";
            // The clarity confidence interval,default 0.8f
            float threshold = 0.8f;   

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/moderation-clarity-detect.jpg");
            String reslut = ModerationService.ClarityDetectToken(token, data, dataUrl, threshold, endpoint);
            Console.WriteLine(reslut);

            // The OBS link must match the region, and the OBS resources of different regions are not shared
            dataUrl = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg";

            // post data by obs url
            reslut = ModerationService.ClarityDetectToken(token, "", dataUrl, threshold, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();

        }

        private static void DistortionCorrect(String token, String endpoint)
        {
            // The obs url of file
            String dataUrl = "";
            // Whether to correct distortion or not
            bool correction = false;   

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/modeation-distortion.jpg");
            String reslut = ModerationService.DistortionCorrectToken(token, data, dataUrl, correction, endpoint);
            JObject joResult = (JObject)JsonConvert.DeserializeObject(reslut);
            if (joResult["result"]["data"].ToString() != "")
            {
                String resultPath = @"../../data/modeation-distortion-token-1.bmp";
                resultPath = utils.Base64ToFileAndSave(joResult["result"]["data"].ToString(), resultPath);
                Console.WriteLine(resultPath);
            }
            else
            {
                Console.WriteLine(reslut);
            }

            // The OBS link must match the region, and the OBS resources of different regions are not shared
            dataUrl = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg";

            // post data by obs url
            reslut = ModerationService.DistortionCorrectToken(token, "", dataUrl, correction, endpoint);
            joResult = (JObject)JsonConvert.DeserializeObject(reslut);
            if (joResult["result"]["data"].ToString() != "")
            {
                String resultPath = @"../../data/modeation-distortion-token-2.bmp";
                resultPath = utils.Base64ToFileAndSave(joResult["result"]["data"].ToString(), resultPath);
                Console.WriteLine(resultPath);
            }
            else
            {
                Console.WriteLine(reslut);
            }
            Console.ReadKey();

        }

        private static void AntiPorn(String token, String endpoint)
        {
            // The obs url of file
            String dataUrl = "";    

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/moderation-antiporn.jpg");
            String reslut = ModerationService.AntiPornToken(token, data, dataUrl, endpoint);
            Console.WriteLine(reslut);

            // The OBS link must match the region, and the OBS resources of different regions are not shared
            dataUrl = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";

            // post data by obs url
            reslut = ModerationService.AntiPornToken(token, "", dataUrl, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();
        }

        private static void ImageContent(String token, String endpoint)
        {
            // The obs url of file 
            String dataUrl = "";
            // The image content confidence interval,"politics" default 0.48f,"terrorism":0
            float threshold = 0.6f; 

            JArray categories = new JArray();                                           

            categories.Add("politics");
            categories.Add("terrorism");
            categories.Add("porn");

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/moderation-terrorism.jpg");
            String reslut = ModerationService.ImageContentToken(token, data, dataUrl, threshold, categories, endpoint);
            Console.WriteLine(reslut);

            // The OBS link must match the region, and the OBS resources of different regions are not shared
            dataUrl = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";

            // post data by obs url
            reslut = ModerationService.ImageContentToken(token, "", dataUrl, threshold, categories, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();
        }

        private static void ImageContentBatch(String token, String endpoint)
        {
            // The OBS link must match the region, and the OBS resources of different regions are not shared
            String dataUrl1 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";
            String dataUrl2 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";

            JArray urls = new JArray();
            urls.Add(dataUrl1);
            urls.Add(dataUrl2);

            float threshold = 0.6f;

            JArray categories = new JArray();
            categories.Add("politics");
            categories.Add("terrorism");
            categories.Add("porn");

            String reslut = ModerationService.ImageContentBatchToken(token, urls, threshold, categories, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();
        }

        private static void ImageContentBatchJobs(String token, String endpoint)
        {

            // The OBS link must match the region, and the OBS resources of different regions are not shared
            String dataUrl1 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";
            String dataUrl2 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";

            JArray urls = new JArray();
            urls.Add(dataUrl1);
            urls.Add(dataUrl2);

            JArray categories = new JArray();
            categories.Add("politics");
            categories.Add("terrorism");
            categories.Add("porn");

            String reslut = ModerationService.ImageContentBatchJobsToken(token, urls, categories, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();
        }

        private static void ModerationText(String token, String endpoint)
        {
            JArray categories = new JArray();
            categories.Add("politics");
            categories.Add("porn");
            categories.Add("contraband");
            categories.Add("ad");

            JArray items = new JArray();
            JObject content = new JObject();
            content.Add("text", "666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666");
            content.Add("type", "content");
            items.Add(content);

            String reslut = ModerationService.ModerationTextToken(token, categories, items, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();
        }

        private static void ModerationVideo(String token, String endpoint)
        {

            JArray categories = new JArray();
            categories.Add("terrorism");
            categories.Add("porn");
            categories.Add("politics");

            // The OBS link must match the region, and the OBS resources of different regions are not shared
            String url = "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition";
            // Frame time interval
            int frame_interval = 1;             

            String reslut = ModerationService.VideoToken(token, url, frame_interval, categories, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();
        }
    }
}
