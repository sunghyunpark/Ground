package view.boardPager;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yssh.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.adapter.BoardAreaAdapter;

public class RecruitFragment extends Fragment {

    @BindView(R.id.area_recyclerView) RecyclerView areaRecyclerView;

    public static RecruitFragment newInstance(){
        Bundle args = new Bundle();

        RecruitFragment fragment = new RecruitFragment();
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
        View v = inflater.inflate(R.layout.fragment_recruit, container, false);
        ButterKnife.bind(this, v);

        init();
        return v;
    }

    /**
     * init
     */
    private void init(){
        Resources res = getResources();
        String[] matching_board_list= res.getStringArray(R.array.recruit_board_list);

        LinearLayoutManager soul_lL = new LinearLayoutManager(getContext());
        BoardAreaAdapter areaOfAdapter = new BoardAreaAdapter(getContext(), matching_board_list, 4);
        areaRecyclerView.setLayoutManager(soul_lL);
        areaRecyclerView.setAdapter(areaOfAdapter);
    }

}
