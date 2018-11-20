package util;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.graphics.Color;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnDismissedListener;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;
import java.util.List;

import model.CommunityModel;
import model.MatchArticleModel;
import model.UserModel;

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

    /**
     * 상세 게시글 내에서 더보기 다이얼로그
     * @param context
     * @param lifecycleOwner
     * @param onMenuItemClickListener
     * @param onDismissedListener
     * @param matchArticleModel
     * @return
     */
    public static PowerMenu getDetailMatchArticleMorePowerMenu(Context context, LifecycleOwner lifecycleOwner, OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener,
                                                          OnDismissedListener onDismissedListener, MatchArticleModel matchArticleModel){
        List<PowerMenuItem> itemList = new ArrayList<>();

        if(matchArticleModel.getWriterId().equals(UserModel.getInstance().getUid())){
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
                .setSelectedTextColor(Color.BLACK)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(context.getResources().getColor(R.color.colorWhite))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setOnDismissListener(onDismissedListener)
                .build();
    }

    /**
     * 자유 상세게시글 내에서 더보기 다이얼로그
     * @param context
     * @param lifecycleOwner
     * @param onMenuItemClickListener
     * @param onDismissedListener
     * @param communityModel
     * @return
     */
    public static PowerMenu getDetailFreeArticleMorePowerMenu(Context context, LifecycleOwner lifecycleOwner, OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener,
                                                               OnDismissedListener onDismissedListener, CommunityModel communityModel){
        List<PowerMenuItem> itemList = new ArrayList<>();

        if(communityModel.getWriterId().equals(UserModel.getInstance().getUid())){
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
                .setSelectedTextColor(Color.BLACK)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(context.getResources().getColor(R.color.colorWhite))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setOnDismissListener(onDismissedListener)
                .build();
    }

    public static PowerMenu getAreaListMenu(Context context, LifecycleOwner lifecycleOwner, OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener,
                                                 OnDismissedListener onDismissedListener, String type){
        List<PowerMenuItem> itemList = new ArrayList<>();

        if(type.equals(Constants.MATCH_OF_BOARD_TYPE_MATCH)){
            itemList.add(new PowerMenuItem("도봉/노원/강북/중랑", false));
            itemList.add(new PowerMenuItem("성북/동대문/종로", false));
            itemList.add(new PowerMenuItem("은평/서대문/마포", false));
            itemList.add(new PowerMenuItem("용산/중구", false));
            itemList.add(new PowerMenuItem("성동/광진/강동", false));
            itemList.add(new PowerMenuItem("송파/서초/강남", false));
            itemList.add(new PowerMenuItem("양천/구로/영등포/강서", false));
            itemList.add(new PowerMenuItem("금천/관악/동작", false));
            itemList.add(new PowerMenuItem("고양", false));
            itemList.add(new PowerMenuItem("인천/부천/김포", false));
            itemList.add(new PowerMenuItem("구리/남양주/하남", false));
            itemList.add(new PowerMenuItem("시흥/안산/광명", false));
            itemList.add(new PowerMenuItem("과천/안양/군포/의왕", false));
            itemList.add(new PowerMenuItem("수원/용인/화성/오산", false));
            itemList.add(new PowerMenuItem("파주", false));
            itemList.add(new PowerMenuItem("성남/광주/이천", false));
            itemList.add(new PowerMenuItem("평택/안성", false));
            itemList.add(new PowerMenuItem("의정부/양주/그 외", false));
        }else if(type.equals(Constants.HIRE_OF_BOARD_TYPE_MATCH)){
            itemList.add(new PowerMenuItem("서울", false));
            itemList.add(new PowerMenuItem("인천/경기", false));
            itemList.add(new PowerMenuItem("대전/세종/충청", false));
            itemList.add(new PowerMenuItem("대구/경북", false));
            itemList.add(new PowerMenuItem("부산/울산/경남", false));
            itemList.add(new PowerMenuItem("광주/전라", false));
            itemList.add(new PowerMenuItem("강원", false));
            itemList.add(new PowerMenuItem("제주", false));
        }
        return new PowerMenu.Builder(context)
                .addItemList(itemList)
                .setAnimation(MenuAnimation.SHOW_UP_CENTER) // Animation start point (TOP | LEFT)
                .setLifecycleOwner(lifecycleOwner)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setWidth(GroundApplication.DISPLAY_WIDTH - GroundApplication.DISPLAY_WIDTH/5)
                .setHeight(GroundApplication.DISPLAY_HEIGHT/2)
                .setTextColor(context.getResources().getColor(R.color.colorBlack))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(context.getResources().getColor(R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setOnDismissListener(onDismissedListener)
                .build();
    }
}
