package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeJsonRequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.Interfaces.ytJsonResponseCommunicator;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeJsoneResponse.youtubeJsonReply;

/**
 * Created by HabibQureshi on 9/13/2017.
 */

public class JsonRequest implements Response.Listener<String>,Response.ErrorListener{
    RequestQueue queue;
    ytJsonResponseCommunicator communicator;


    public JsonRequest(Context context,ytJsonResponseCommunicator communicator) {
        this.queue = Volley.newRequestQueue(context);
        this.communicator=communicator;
    }
    public void request(String url)
    {
        Log.e("URL",url);
        StringRequest stringReque=new StringRequest(Request.Method.GET,url,this,this);
        this.queue.add(stringReque);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        communicator.failureResponse(error);

    }

    @Override
    public void onResponse(String response) {
        Gson gson=new Gson();
        youtubeJsonReply reply=gson.fromJson(response,youtubeJsonReply.class);
        if(reply.getmPrevPageToken()==null&&reply.getNextPageToken()==null)
            this.communicator.successResponse(reply,4);
        else
        if(reply.getmPrevPageToken()==null&&reply.getNextPageToken()!=null)
            this.communicator.successResponse(reply,1);
        else
            if(reply.getmPrevPageToken()!=null&&reply.getNextPageToken()!=null)
                this.communicator.successResponse(reply,2);
        else if(reply.getNextPageToken()==null)
                this.communicator.successResponse(reply,3);
        else
                this.communicator.successResponse(reply,0);

    }
}
