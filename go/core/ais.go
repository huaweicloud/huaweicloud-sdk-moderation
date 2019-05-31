package core

const (
	// domain name for get token
	IAM_ENPOINT string = "iam.myhuaweicloud.com"

	// the uri for get token
	IAM_TOKEN string = "/v3/auth/tokens"

	// the uri for Service clarity detect
	IMAGE_CLARITY_DETECT string = "/v1.0/moderation/image/clarity-detect"

	// the uri for Service distortion correction
	DISTORTION_CORRECTION string = "/v1.0/moderation/image/distortion-correct"

	// the uri for Service image anti porn
	IMAGE_ANTI_PORN string = "/v1.0/moderation/image/anti-porn"

	// the uri for Service image content
	IMAGE_MODERATION string = "/v1.0/moderation/image"

	// the uri for Service moderation text
	MODERATION_TEXT string = "/v1.0/moderation/text"

	// the uri for service of moderation video
	MODERATION_VIDEO string = "/v1.0/moderation/video"

	// the uri for service of image content batch result
	IMAGE_MODERATION_BATCH_RESULT string = "/v1.0/moderation/image/batch"

	// the uri for service of image content batch job id
	IMAGE_MODERATION_BATCH_JOBS string = "/v1.0/moderation/image/batch/jobs"

	// the uri for service of image content batch result
	IMAGE_MODERATION_BATCH string = "/v1.0/moderation/image/batch"
	
	// moderation service type 
	MODERATION string = "moderation"
	
	// the max retry times
	RETRY_MAX_TIMES int = 3
)
