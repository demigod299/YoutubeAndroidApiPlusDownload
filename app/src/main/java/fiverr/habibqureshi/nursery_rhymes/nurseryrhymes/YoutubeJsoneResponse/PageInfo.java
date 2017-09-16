
package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeJsoneResponse;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class PageInfo {

    @SerializedName("resultsPerPage")
    private Long mResultsPerPage;
    @SerializedName("totalResults")
    private int mTotalResults;

    public Long getResultsPerPage() {
        return mResultsPerPage;
    }

    public void setResultsPerPage(Long resultsPerPage) {
        mResultsPerPage = resultsPerPage;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(int totalResults) {
        mTotalResults = totalResults;
    }

}
