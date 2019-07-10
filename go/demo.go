package main

import (
	"moderation_sdk/src/sdk"
	"fmt"
)
func main() {
	  
	/*
	 * Services currently support North China-Beijing 1 (cn-north-1),
	 * North China-Beijing 4 (cn-north-4), Asia Pacific-Hong Kong (ap-southeast-1)
	 */
	sdk.InitRegion("cn-north-1")
	
	ak := "*******" // your AppKey
	sk := "*******" // your AppSecret

	// The sample for image clarity detect service
	// Test_ClarityDetectAkskDemo(ak, sk)

	// The sample for image distortion correct service
	// Test_DistortionCorrectAkskDemo(ak, sk)

	// The sample for image content detect service
	// Test_ImageModerationAkskDemo(ak, sk)

	// The sample for image content batch detect service
	// Test_ImageModerationBatchAkskDemo(ak, sk)

	// The sample for moderation text
	// Test_ModerationTextAkskDemo(ak, sk)

	// The sample for moderation video
	// Test_ModerationVideoAkskDemo(ak, sk)
	
	// The sample for image content batch job detect service
	Test_ImageModerationBatchJobAkskDemo(ak, sk)
	
}

func Test_ClarityDetectAkskDemo(ak string, sk string) {
	var threshold float32 = 0.8

	// The OBS link should match the region, and the OBS resources of different regions are not shared
	url := "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg"
	result := sdk.ClarityDetectAksk(ak, sk, "", url, threshold)
	fmt.Println(result)

	// post data by native file
	filepath := "data/moderation-clarity-detect.jpg"
	image := sdk.ChangeFileToBase64(filepath)
	result = sdk.ClarityDetectAksk(ak, sk, image, "", threshold)
	fmt.Println(result)
}

func Test_DistortionCorrectAkskDemo(ak string, sk string) {
	var correction bool = true

	// The OBS link should match the region, and the OBS resources of different regions are not shared
	url := "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg"
	result := sdk.DistortionCorrectAksk(ak, sk, "", url, correction)
	fmt.Println(result)

	// post data by native file
	filepath := "data/modeation-distortion.jpg"
	image := sdk.ChangeFileToBase64(filepath)
	result = sdk.DistortionCorrectAksk(ak, sk, image, "", correction)
	fmt.Println(result)
}

func Test_ImageModerationAkskDemo(ak string, sk string) {
	var categories = []string{"politics", "terrorism", "porn"}

	// The OBS link should match the region, and the OBS resources of different regions are not shared
	url := "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg"
	result := sdk.ImageModerationAksk(ak, sk, "", url, categories)
	fmt.Println(result)

	// post data by native file
	filepath := "data/moderation-terrorism.jpg"
	image := sdk.ChangeFileToBase64(filepath)
	result = sdk.ImageModerationAksk(ak, sk, image, "", categories)
	fmt.Println(result)
}

func Test_ImageModerationBatchAkskDemo(ak string, sk string) {
	var categories = []string{"politics", "terrorism", "porn"}
	
	// The OBS link should match the region, and the OBS resources of different regions are not shared
	url1 := "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg"
	url2 := "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg"

	var urls = []string{url1, url2}
	result := sdk.ImageModerationBatchAksk(ak, sk, urls, categories)
	fmt.Println(result)
}

func Test_ModerationTextAkskDemo(ak string, sk string) {

	var categories = []string{"ad", "politics", "flood", "politics", "contraband", "contraband"}

	var text string = "666聊请+110亚砷酸钾六位qq，fuck666666666666666sssssssssss"
	var types string = "content"

	result := sdk.ModerationTextAksk(ak, sk, categories, text, types)
	fmt.Println(result)
}

func Test_ModerationVideoAkskDemo(ak string, sk string) {

	var frameInterval int = 1
	var categories = []string{"politics", "terrorism", "porn"}
	// The OBS link should match the region, and the OBS resources of different regions are not shared
	url := "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition"
	result := sdk.ModerationVideoAksk(ak, sk, url, frameInterval, categories)
	fmt.Println(result)
}

func Test_ImageModerationBatchJobAkskDemo(ak string, sk string) {

	// The OBS link should match the region, and the OBS resources of different regions are not shared
	var url1 string = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg"
	var url2 string = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg"

	var urls = []string{url1, url2}
	var categories = []string{"politics", "terrorism", "porn"}
	result := sdk.ImageModerationBatchJobsAksk(ak, sk, urls, categories)
	fmt.Println(result)
}
