package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.Interfaces;

import com.android.volley.VolleyError;

import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeJsoneResponse.youtubeJsonReply;

/**
 * Created by HabibQureshi on 9/14/2017.
 */

public interface ytJsonResponseCommunicator {
     void successResponse(youtubeJsonReply response, int which);
     void failureResponse(VolleyError error);

}
