
package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeJsoneResponse;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ContentDetails {

    @SerializedName("videoId")
    private String mVideoId;
    @SerializedName("videoPublishedAt")
    private String mVideoPublishedAt;

    public String getVideoId() {
        return mVideoId;
    }

    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }

    public String getVideoPublishedAt() {
        return mVideoPublishedAt;
    }

    public void setVideoPublishedAt(String videoPublishedAt) {
        mVideoPublishedAt = videoPublishedAt;
    }

}
