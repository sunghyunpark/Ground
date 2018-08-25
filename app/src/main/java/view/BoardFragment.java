package view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groundmobile.ground.R;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.SessionManager;
import util.Util;
import util.adapter.BoardViewPagerAdapter;
import view.dialog.MyContentsDialog;

public class BoardFragment extends BaseFragment {

    private BoardViewPagerAdapter pagerAdapter;
    private MyContentsDialog myContentsDialog;

    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;

    @Override
    public void onDestroy(){
        super.onDestroy();

        pagerAdapter = null;
        myContentsDialog = null;
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
        myContentsDialog = new MyContentsDialog(getContext());
        pagerAdapter = new BoardViewPagerAdapter(getChildFragmentManager());
    }

    private void initUI(){
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }


    @OnClick(R.id.my_btn) void myBtn(){
        if(isLogin()){
            //login
            myContentsDialog.show();
        }else{
            //not login
            showMessage("로그인을 해주세요.");
        }

        /*
        myContentsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });*/
    }
}
