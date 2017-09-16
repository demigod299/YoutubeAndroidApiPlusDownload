
package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeJsoneResponse;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class youtubeJsonReply implements java.io.Serializable{

    @SerializedName("etag")
    private String mEtag;
    @SerializedName("items")
    private List<Item> mItems;
    @SerializedName("kind")
    private String mKind;
    @SerializedName("nextPageToken")
    private String mNextPageToken;
    @SerializedName("prevPageToken")
    private String mPrevPageToken;
    @SerializedName("pageInfo")
    private PageInfo mPageInfo;

    public String getmPrevPageToken() {
        return mPrevPageToken;
    }

    public void setmPrevPageToken(String mPrevPageToken) {
        this.mPrevPageToken = mPrevPageToken;
    }


    public String getEtag() {
        return mEtag;
    }

    public void setEtag(String etag) {
        mEtag = etag;
    }

    public List<Item> getItems() {
        return mItems;
    }

    public void setItems(List<Item> items) {
        mItems = items;
    }

    public String getKind() {
        return mKind;
    }

    public void setKind(String kind) {
        mKind = kind;
    }

    public String getNextPageToken() {
        return mNextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        mNextPageToken = nextPageToken;
    }

    public PageInfo getPageInfo() {
        return mPageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        mPageInfo = pageInfo;
    }

}
