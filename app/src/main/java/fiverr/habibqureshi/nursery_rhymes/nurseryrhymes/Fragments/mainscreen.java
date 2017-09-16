package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.activities.MainActivity;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.R;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.extras.touchEffect;
/**
 * Created by HabibQureshi on 8/4/2017.
 */

public class mainscreen extends Fragment {
    View fragment_view;
    MainActivity activity;
    LinearLayout l,l1,l2,l3,l4;
    ImageView search;
    EditText searchbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragment_view = inflater.inflate(R.layout.main_layout_fragment,
                container, false);
        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        l=(LinearLayout)fragment_view.findViewById(R.id.l);
        l1=(LinearLayout)fragment_view.findViewById(R.id.l2);
        l2=(LinearLayout)fragment_view.findViewById(R.id.l3);
        l3=(LinearLayout)fragment_view.findViewById(R.id.l4);
        l4=(LinearLayout)fragment_view.findViewById(R.id.l5);
        this.search=(ImageView)fragment_view.findViewById(R.id.search);
        this.searchbar=(EditText)fragment_view.findViewById(R.id.searchbar);
        this.search.setOnClickListener(click);
        this.search.setOnTouchListener(new touchEffect());
        l.setOnClickListener(click);
        l1.setOnClickListener(click);
        l2.setOnClickListener(click);
        l3.setOnClickListener(click);
        l4.setOnClickListener(click);






        return fragment_view;
    }
    View.OnClickListener click= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment fragment=new youtubeListFragment();
            Bundle bundle=new Bundle();
            if(v.getId()==R.id.search)
            {
                bundle.putString("search",searchbar.getText().toString());
            }
            else
            {
                LinearLayout l=(LinearLayout)v;
                bundle.putString("PlayListID",l.getTag().toString());

            }
            fragment.setArguments(bundle);
            activity.changeSupportFragment(fragment,true);


        }
    };
}
