package util;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.graphics.Color;

import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnDismissedListener;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;
import java.util.List;

public class PowerMenuUtil {

    /**
     * 평균 연령 선택 팝업
     * @param context
     * @param lifecycleOwner
     * @param onMenuItemClickListener
     * @param onDismissedListener
     * @return
     */
    public static PowerMenu getAgePowerMenu(Context context, LifecycleOwner lifecycleOwner, OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener, OnDismissedListener onDismissedListener) {
        return new PowerMenu.Builder(context)
                .addItem(new PowerMenuItem("10대", false))
                .addItem(new PowerMenuItem("20대", false))
                .addItem(new PowerMenuItem("30대", false))
                .addItem(new PowerMenuItem("40대", false))
                .addItem(new PowerMenuItem("50대 이상", false))
                .setAnimation(MenuAnimation.SHOW_UP_CENTER) // Animation start point (TOP | LEFT)
                .setLifecycleOwner(lifecycleOwner)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setWidth(GroundApplication.DISPLAY_WIDTH/2)
                .setTextColor(context.getResources().getColor(R.color.colorBlack))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(context.getResources().getColor(R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setOnDismissListener(onDismissedListener)
                .build();
    }

    /**
     * 경기 방식 선택 팝업
     * @param context
     * @param lifecycleOwner
     * @param onMenuItemClickListener
     * @param onDismissedListener
     * @return
     */
    public static PowerMenu getPlayRulePowerMenu(Context context, LifecycleOwner lifecycleOwner, OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener, OnDismissedListener onDismissedListener){
        return new PowerMenu.Builder(context)
                .addItem(new PowerMenuItem("3vs3", false))
                .addItem(new PowerMenuItem("5vs5", false))
                .addItem(new PowerMenuItem("6vs6", false))
                .addItem(new PowerMenuItem("11vs11", false))
                .addItem(new PowerMenuItem("기타", false))
                .setAnimation(MenuAnimation.SHOW_UP_CENTER) // Animation start point (TOP | LEFT)
                .setLifecycleOwner(lifecycleOwner)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setWidth(GroundApplication.DISPLAY_WIDTH/2)
                .setTextColor(context.getResources().getColor(R.color.colorBlack))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(context.getResources().getColor(R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setOnDismissListener(onDismissedListener)
                .build();
    }

    public static PowerMenu getDetailArticleMorePowerMenu(Context context, LifecycleOwner lifecycleOwner, OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener, OnDismissedListener onDismissedListener, boolean isMyContents){
        List<PowerMenuItem> itemList = new ArrayList<>();

        if(isMyContents){
            itemList.add(new PowerMenuItem("수정", false));
            itemList.add(new PowerMenuItem("삭제", false));
        }else{
            itemList.add(new PowerMenuItem("신고", false));
        }
        return new PowerMenu.Builder(context)
                .addItemList(itemList)
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT)
                .setLifecycleOwner(lifecycleOwner)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setWidth(GroundApplication.DISPLAY_WIDTH/2)
                .setTextColor(context.getResources().getColor(R.color.colorBlack))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(context.getResources().getColor(R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setOnDismissListener(onDismissedListener)
                .build();
    }
}
