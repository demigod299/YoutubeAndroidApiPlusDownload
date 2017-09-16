package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.activities;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.extras.Config;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.Fragments.splash;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.Fragments.youtubeListFragment;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.Interfaces.DownloadButtonDisplayHelp;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.R;

public class MainActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    FragmentManager fm;
    FragmentTransaction ft;
    private ProgressDialog progress;
    YouTubePlayer player;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    DownloadButtonDisplayHelp displayHelp;
    int fullscreen=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       if(fullscreen==2)
           changeSupportFragment(new youtubeListFragment(),true);
        if(fullscreen==1)
        {}
        else {
            init();
            changeSupportFragment(new splash(),false);
        }



    }

    public void init() {
        this.fm = getSupportFragmentManager();
        ft=fm.beginTransaction();
        youTubePlayerFragment =(YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.player);
        youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, this);
        ft.hide(this.youTubePlayerFragment);
        ft.commit();
    }

    public void showProgresMessage(String message) {
        progress = new ProgressDialog(this);
        progress.setMessage(message);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }
    public Boolean isProgressShowing()
    {
        if(this.progress!=null)
            if(this.progress.isShowing())
                return true;
        return false;
    }
    public void updateProgressMessage(final String s)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.setMessage(s);
            }
        });
    }
    public void cancleProgressMessage() {
        if (progress != null)
            if (progress.isShowing())
                this.progress.cancel();
    }
    public void changeSupportFragment(android.support.v4.app.Fragment frag,
                                      Boolean addToStack) {
        ft = fm.beginTransaction();
        ft.replace(R.id.all_fram_layout, frag);
        if (addToStack) {
            ft.addToBackStack(null);

        }
        ft.commitAllowingStateLoss();
    }

    public void displayMessage(String mesg) {
        Toast.makeText(this, mesg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.player = youTubePlayer;
        this.player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                Log.e("onFullscreen",""+b);
                if(b)
                    fullscreen=1;
                else fullscreen=2;
            }
        });
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    public void playVideo(String videoID) {
        if(this.youTubePlayerFragment.isHidden())
        {
            ft=fm.beginTransaction();
            ft.show(this.youTubePlayerFragment);
            ft.commit();
        }
        if(this.player!=null)
        {
            player.cueVideo(videoID);
            player.play();
            displayHelp.displayButton();


        }


    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("where","onRestoreInstanceState");
        this.fullscreen=savedInstanceState.getInt("fullscreen");
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Log.e("where","onSaveInstanceState");
        bundle.putInt("fullscreen",this.fullscreen);
    }

    @Override
    public void onBackPressed() {
        if(fullscreen==1)
        {
            if(this.player!=null)
                player.setFullscreen(false);
        }
        else
        if(this.youTubePlayerFragment.isVisible())
        {
            displayHelp.dontDisplayButton();
            if(this.player!=null)
                if(this.player.isPlaying())
                    this.player.pause();
            ft=fm.beginTransaction();
            ft.hide(this.youTubePlayerFragment);
            ft.commit();

        }
        else
            super.onBackPressed();
    }
    public void initInterface(DownloadButtonDisplayHelp displayHelp) {
        this.displayHelp=displayHelp;
    }
    public void StopPlayingVideo() {
        if(this.player!=null)
            if(this.player.isPlaying())
                this.player.pause();
    }

}
