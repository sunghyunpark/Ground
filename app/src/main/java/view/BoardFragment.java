package view;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yssh.ground.GroundApplication;
import com.yssh.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.adapter.BoardAreaAdapter;

public class BoardFragment extends Fragment {

    BoardAreaAdapter areaOfAdapter;
    @BindView(R.id.area_recyclerView) RecyclerView areaRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_board, container, false);
        ButterKnife.bind(this, v);

        init();

        return v;
    }

    /**
     * init
     */
    private void init(){
        Resources res = getResources();
        String[] area_seoul= res.getStringArray(R.array.seoul_area);

        LinearLayoutManager soul_lL = new LinearLayoutManager(getContext());
        areaOfAdapter = new BoardAreaAdapter(getContext(), area_seoul);
        areaRecyclerView.setLayoutManager(soul_lL);
        areaRecyclerView.setAdapter(areaOfAdapter);
    }
}
