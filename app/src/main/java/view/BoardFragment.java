package view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groundmobile.ground.R;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.adapter.BoardViewPagerAdapter;
import view.dialog.AreaSearchDialog;

public class BoardFragment extends BaseFragment {

    private BoardViewPagerAdapter pagerAdapter;

    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;

    @Override
    public void onDestroy(){
        super.onDestroy();
        pagerAdapter = null;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_board, container, false);
        ButterKnife.bind(this, v);

        initUI();
        return v;
    }

    private void init(){
        pagerAdapter = new BoardViewPagerAdapter(getChildFragmentManager());
    }

    private void initUI(){
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.search_btn) void searchBtn(){
        AreaSearchDialog areaSearchDialog = new AreaSearchDialog(getContext(), viewPager.getCurrentItem());
        areaSearchDialog.show();
    }
}
