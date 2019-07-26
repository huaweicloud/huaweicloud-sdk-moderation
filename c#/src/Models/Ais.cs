using System;


namespace Moderation.Models
{
    public class Ais
    {
        // the uri for get token 
        public static String IAM_TOKEN = "/v3/auth/tokens";

        // the uri for Service clarity detect
        public static String IMAGE_CLARITY_DETECT = "/v1.0/moderation/image/clarity-detect";

        // the uri for Service distortion correction
        public static String DISTORTION_CORRECTION = "/v1.0/moderation/image/distortion-correct";

        // the uri for Service image content
        public static String IMAGE_CONTENT = "/v1.0/moderation/image";

        // the uri for Service moderation text
        public static String MODERATION_TEXT = "/v1.0/moderation/text";

        // the uri for service of moderation video
        public static String MODERATION_VIDEO = "/v1.0/moderation/video";

        // the uri for service of image content batch result
        public static String IMAGE_CONTENT_BATCH_RESULT = "/v1.0/moderation/image/batch";

        // the uri for service of image content batch job id
        public static String IMAGE_CONTENT_BATCH_JOBS = "/v1.0/moderation/image/batch/jobs";

        // the uri for service of image content batch result
        public static String IMAGE_CONTENT_BATCH = "/v1.0/moderation/image/batch";

        // retry max times
        public static int RETRY_MAX_TIMES = 3;
    }
}
