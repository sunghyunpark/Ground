package view.myFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yssh.ground.GroundApplication;
import com.yssh.ground.R;

import butterknife.ButterKnife;

public class RecruitFragment extends Fragment {
    private View v;
    private String type;

    // TODO: Rename and change types and number of parameters
    public static RecruitFragment newInstance(String type) {
        RecruitFragment fragment = new RecruitFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_recruit, container, false);
        ButterKnife.bind(this, v);

        init(type);

        return v;
    }

    private void init(String type){
        if(type.equals(GroundApplication.MY_ARTICLE_TYPE)){

        }else if(type.equals(GroundApplication.MY_COMMENT_TYPE)){

        }else if(type.equals(GroundApplication.MY_FAVORITE_TYPE)){

        }
    }
}