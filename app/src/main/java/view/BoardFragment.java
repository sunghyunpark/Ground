package view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yssh.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.SessionManager;
import util.Util;
import util.adapter.BoardViewPagerAdapter;
import view.dialog.MyContentsDialog;

public class BoardFragment extends Fragment {

    private MyContentsDialog myContentsDialog;
    private SessionManager sessionManager;

    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;

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

    private void init(){
        sessionManager = new SessionManager(getContext());
        myContentsDialog = new MyContentsDialog(getContext());
        BoardViewPagerAdapter pagerAdapter = new BoardViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.my_btn) void showMyContentsDialog(){
        if(sessionManager.isLoggedIn()){
            //login
            myContentsDialog.show();
        }else{
            //not login
            Util.showToast(getContext(), "로그인을 해주세요.");
        }

        /*
        myContentsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });*/
    }
}
