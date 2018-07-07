package view.recentPager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yssh.ground.R;

import butterknife.ButterKnife;

public class RecentHireBoardFragment extends Fragment {

    private View v;
    // TODO: Rename and change types and number of parameters
    public static RecentHireBoardFragment newInstance() {
        Bundle args = new Bundle();

        RecentHireBoardFragment fragment = new RecentHireBoardFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_recent_hire_board, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    private void init(){

    }


}
