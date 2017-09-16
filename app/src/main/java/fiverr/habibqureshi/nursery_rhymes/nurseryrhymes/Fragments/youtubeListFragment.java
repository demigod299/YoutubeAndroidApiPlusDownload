package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.Fragments;
import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.android.volley.VolleyError;

import java.io.File;
import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeJsoneResponse.Item;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.dataBases.DataSource;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.activities.MainActivity;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.R;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.Interfaces.*;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.extras.Config;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.extras.touchEffect;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.adapter.youtubePlayListView;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeJsoneResponse.youtubeJsonReply;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeJsonRequest.*;

public class youtubeListFragment extends Fragment implements DownloadButtonDisplayHelp,ytJsonResponseCommunicator {
    youtubePlayListView.youtubeData CurrentYouTubeData;
    View fragment_view;
    MainActivity activity;
    ListView list;
    Button retry;
    DataSource dataSource;
    String PlayListID="";
    String channelName="";
    String search=null;
    public String AppName=null;
    public ImageView download;
    public youtubePlayListView adapter;
    static int TotalVideos;
    public String CurrentPlayingVideoID=null;
    public String CurrentPlayingVideoTitte=null;
    public  String [] YouTubeVideosTittles;
    public  static  String [] YouTubeVideosTumbnil;
    public  String [] YouTubeVideosID;
    public int CurrentVideoPosition=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragment_view = inflater.inflate(R.layout.youtube_list_fragment,
                container, false);
        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }
        this.FindViews();
        this.init();
        this.initListner();
        permission_check();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if(search!=null)
        {
            this.CurrentYouTubeData=dataSource.search(search);
            if(CurrentYouTubeData!=null)
                displayList();
            else
                activity.displayMessage("No match found");
        }
        else
            ShowAllPlayListVideos();
        return fragment_view;
    }
    public void FindViews() {
        download=(ImageView)fragment_view.findViewById(R.id.downloadVideo);
        list=(ListView)fragment_view.findViewById(R.id.listView);
        retry=(Button)fragment_view.findViewById(R.id.retryButton);

    }
    public void init() {
        dataSource  = new DataSource(activity);
        this.PlayListID=getArguments().getString("PlayListID");
        this.search=getArguments().getString("search");
        this.AppName=activity.getResources().getString(R.string.app_name);
        this.CurrentYouTubeData= new youtubePlayListView.youtubeData(activity.getApplicationContext());


    }
    public void initListner() {
        activity.initInterface(this);
        download.setOnClickListener(YouTubeApimpClick);
        download.setOnTouchListener(new touchEffect());
        retry.setOnClickListener(YouTubeApimpClick);

    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(check_network()) {
                CurrentPlayingVideoID = CurrentYouTubeData.VideoID[position];
                CurrentPlayingVideoTitte=CurrentYouTubeData.Tittle[position];
                CurrentVideoPosition=position;
                activity.playVideo(CurrentPlayingVideoID);

            }
            else
                activity.displayMessage("No internet");


        }
    };
    View.OnClickListener YouTubeApimpClick= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(R.id.downloadVideo==v.getId())
            {

                if(permission_check())
                    if(check_network())
                        DownloadingVideos();
                    else
                        activity.displayMessage("No internet");
            }
            else
            if(v.getId()==R.id.retryButton)
            {
                if(check_network())
                {
                    retry.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);
                    ShowAllPlayListVideos();
                }
                else
                    activity.displayMessage("No internet");
            }

        }
    };
    private void ShowAllPlayListVideos() {
        this.CurrentYouTubeData=dataSource.getAllData(this.PlayListID);
        if(!displayList())
        {
            if(check_network()) {
               activity.showProgresMessage("Featching your videos");
                JsonRequest request=new JsonRequest(activity.getApplicationContext(),this);
                request.request(Config.YOUTUBE_API_Base+Config.YOUTUBE_API_MaxResult+Config.YOUTUBE_API_PlayListID+PlayListID+Config.YOUTUBE_API_key+Config.YOUTUBE_API_KEY);
            }
            else
            {
                activity.displayMessage("No internet");
                this.HideEveryThing();
                this.retry.setVisibility(View.VISIBLE);
            }
        }
    }
    private Boolean displayList() {
        if(this.CurrentYouTubeData!=null)
        {
            Log.e("currentData","NotNull");
            adapter  = new youtubePlayListView(activity.getApplicationContext(),this.CurrentYouTubeData);
            list.setAdapter(adapter);
            list.setVisibility(View.VISIBLE);
            list.setOnItemClickListener(itemClickListener);
            return true;
        }
        return  false;
    }
    private boolean permission_check() {
        if(ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return false;
            }
        }


        return true;
    }
    public boolean check_network() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)activity. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
    public void HideEveryThing() {
       // youTubeView.setVisibility(View.GONE);
        list.setVisibility(View.GONE);
        /*if(player!=null)
            if(player.isPlaying())
                player.pause();*/

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
        }else {
            permission_check();
        }
    }
    public void DownloadingVideos() {
        activity.StopPlayingVideo();
        activity.showProgresMessage("Preparing");
        new YouTubeExtractor(activity.getApplicationContext()) {
            @Override
            public void onExtractionComplete(final SparseArray<YtFile> ytFiles, VideoMeta vMeta) {

                if (ytFiles != null){
                    if(activity.isProgressShowing()){
                        activity.cancleProgressMessage();
                        final Dialog d = new Dialog(activity);
                        d.setContentView(R.layout.download_youtube_video_selection);
                        final Button downloadVideo;
                        final RadioButton r1, r2, r3;
                        ImageView downloadAbleThumbnil;
                        TextView downloadAbleTittle;
                        downloadAbleThumbnil = (ImageView) d.findViewById(R.id.downloadableVideoThumbnil);
                        downloadAbleTittle = (TextView) d.findViewById(R.id.DownloadAbleVideoTittle);
                        String imgPath = dataSource.getImgPath(CurrentVideoPosition, CurrentYouTubeData.PlayListID);
                        if (!imgPath.equals("empty")){
                            File imgFile = new File(imgPath);
                            if (imgFile.exists()) {
                                Log.e("getView  ", " exist");
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                downloadAbleThumbnil.setImageBitmap(myBitmap);}}
                        downloadAbleTittle.setText(CurrentPlayingVideoTitte.substring(0, 20) + "...");
                        downloadVideo = (Button) d.findViewById(R.id.downloadVideoNow);
                        r1 = (RadioButton) d.findViewById(R.id.low);
                        r2 = (RadioButton) d.findViewById(R.id.med);
                        r3 = (RadioButton) d.findViewById(R.id.hig);
                        if (ytFiles.get(36) != null)
                            r1.setVisibility(View.VISIBLE);
                        if (ytFiles.get(18) != null)
                            r2.setVisibility(View.VISIBLE);
                        if (ytFiles.get(22) != null)
                            r3.setVisibility(View.VISIBLE);
                        View.OnClickListener clickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.downloadVideoNow:
                                        if (r1.isChecked() || r2.isChecked() || r3.isChecked()) {
                                            int ditag = 0;
                                            if (r1.isChecked())
                                                ditag = 36;
                                            else if (r2.isChecked())
                                                ditag = 18;
                                            else
                                                ditag = 22;
                                            DownloadVideoNow(ytFiles.get(ditag).getUrl());
                                            d.cancel();
                                        } else activity.displayMessage("Select option");

                                        break;
                                    case R.id.low:
                                        downloadVideo.setClickable(true);
                                        break;
                                    case R.id.med:
                                        downloadVideo.setClickable(true);
                                        break;
                                    case R.id.hig:
                                        downloadVideo.setClickable(true);
                                        break;
                                }

                            }
                        };
                        r1.setOnClickListener(clickListener);
                        r2.setOnClickListener(clickListener);
                        r3.setOnClickListener(clickListener);
                        downloadVideo.setOnClickListener(clickListener);
                        d.show();
                    }

                }
                else{
                    activity.displayMessage("Something went wrong try again");
                }
            }
        }.extract(Config.youtubeVideoLink+CurrentPlayingVideoID, true, true);

    }
    @Override
    public void displayButton() {
        download=(ImageView)fragment_view.findViewById(R.id.downloadVideo);
        download.setVisibility(View.VISIBLE);

    }
    @Override
    public void dontDisplayButton() {
        download.setVisibility(View.GONE);

    }
    public void initCurrentYoutubeData(youtubeJsonReply reply) {
        Log.e("where","initCurrentYoutubeData");
        TotalVideos=reply.getPageInfo().getTotalResults();
        Log.e("TotalVideo","total="+TotalVideos);
        YouTubeVideosTittles = new String[TotalVideos];
        YouTubeVideosID = new String[TotalVideos];
        YouTubeVideosTumbnil = new String[TotalVideos];
        List<Item> items=reply.getItems();
        channelName=items.get(0).getSnippet().getChannelTitle();
        CurrentYouTubeData=new youtubePlayListView.youtubeData(activity.getApplicationContext());
        CurrentYouTubeData.ThumbNillPath = new String[TotalVideos];
        CurrentYouTubeData.ChannelName = channelName;
    }
    @Override
    public void successResponse(youtubeJsonReply reply, int which) {
        List<Item> items=reply.getItems();
        boolean saveInDb=false;
        Log.e("where","which="+which);
        switch (which)
        {
            case 1:
                initCurrentYoutubeData(reply);
                youtubePlayListView.youtubeData.size=0;
                break;
            case 2:
                youtubePlayListView.youtubeData.size+=50;
                break;
            case 3:
                youtubePlayListView.youtubeData.size+=50;
                saveInDb=true;
                break;
            case 4:
                initCurrentYoutubeData(reply);
                youtubePlayListView.youtubeData.size=0;
                break;
            default:
                break;
        }

        int j=youtubePlayListView.youtubeData.size;
        int i = 0;
        for (; i < items.size(); i++) {
            Log.e("index","index="+(i+j));
            YouTubeVideosTumbnil[i+j] =items.get(i).getSnippet().getThumbnails().getMedium().getUrl();
            YouTubeVideosID[i+j] = items.get(i).getContentDetails().getVideoId();
            YouTubeVideosTittles[i+j] =items.get(i).getSnippet().getTitle();
            CurrentYouTubeData.ThumbNillPath[i+j] = "empty";
        }
        activity.updateProgressMessage("Featching your videos "+(i+j)+" out of "+TotalVideos);
        switch (which)
        {
            case 1:
            case 2:
                JsonRequest request=new JsonRequest(activity.getApplicationContext(),this);
                request.request(Config.YOUTUBE_API_Base+Config.YOUTUBE_API_NextPageToken+reply.getNextPageToken()+"&"+Config.YOUTUBE_API_MaxResult+Config.YOUTUBE_API_PlayListID+PlayListID+Config.YOUTUBE_API_key+Config.YOUTUBE_API_KEY);
                break;
            case 3:
            case 4:
                saveInDb=true;
                break;
            default:
                break;
        }
        if (saveInDb)
        {
            CurrentYouTubeData.ThumbNillLink = YouTubeVideosTumbnil;
            CurrentYouTubeData.Tittle = YouTubeVideosTittles;
            CurrentYouTubeData.PlayListID = PlayListID;
            CurrentYouTubeData.VideoID = YouTubeVideosID;
            CurrentYouTubeData.size = TotalVideos;
            CurrentYouTubeData.ChannelName = channelName;
            if (dataSource.Add(CurrentYouTubeData) < 0)
                activity.displayMessage("Cant Store in DB");
            activity.cancleProgressMessage();
            displayList();

        }

    }
    @Override
    public void failureResponse(VolleyError error) {
        activity.displayMessage("Error while feathing");
        activity.cancleProgressMessage();

    }
    public void DownloadVideoNow(String url) {
        String fileName = CurrentPlayingVideoTitte + ".mp4";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Downloading");
        request.setDescription(CurrentPlayingVideoTitte);
        request.setVisibleInDownloadsUi(false);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        DownloadManager downloadManager = (DownloadManager)activity.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

    }
}
