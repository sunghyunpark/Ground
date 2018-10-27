package com.groundmobile.ground;

public class Constants {
    // EXTRA KEY
    public static final String EXTRA_USER_ID = "uid";
    public static final String EXTRA_BOARD_TYPE = "boardType";
    public static final String EXTRA_MATCH_BOARD_TYPE = "matchBoardType";    // match 게시판(match/hire/recruit)에만 사용된다. 분기처리용.
    public static final String EXTRA_COMMUNITY_BOARD_TYPE = "communityBoardType";    // community 게시판(free) 에만 사용된다. 분기처리용.
    public static final String EXTRA_AREA_NAME = "area";
    public static final String EXTRA_AREA_NO = "areaNo";
    public static final String EXTRA_ARTICLE_NO = "articleNo";
    public static final String EXTRA_EXIST_ARTICLE_MODEL = "hasArticleModel";
    public static final String EXTRA_ARTICLE_MODEL = "articleModel";
    public static final String EXTRA_MY_TYPE = "myType";
    public static final String EXTRA_BOARD_PHOTO_URL = "photoUrl";
    public static final String EXTRA_YOUTUBE_VIDEO_ID = "videoId";
    public static final String EXTRA_YOUTUBE_TITLE = "title";

    //MY
    public static final String MY_ARTICLE_TYPE = "myArticle";
    public static final String MY_COMMENT_TYPE = "myComment";
    public static final String MY_FAVORITE_TYPE = "myFavorite";

    //ARTICLE TYPE 댓글 관련하여 match(match/hire/recruit) / free 를 구분할 때 사용
    public static final String BOARD_TYPE_MATCH = "match";    // match(match / hire / recruit)
    public static final String BOARD_TYPE_COMMUNITY = "community";

    // 아래 3개의 변수는 match 게시판 내에서 분기처리가 필요한 경우 사용된다.
    public static final String MATCH_OF_BOARD_TYPE_MATCH = "match";
    public static final String HIRE_OF_BOARD_TYPE_MATCH = "hire";
    public static final String RECRUIT_OF_BOARD_TYPE_MATCH = "recruit";

    // 아래 1개의 변수는 community 게시판 내에서 분기처리가 필요한 경우 사용된다.
    public static final String FREE_OF_BOARD_TYPE_COMMUNITY = "free";

    // 디폴트 시간
    public static final String DEFAULT_TIME_FORMAT = "0000-00-00";

    //로컬 저장소
    public static final String STORAGE_DIRECTORY_NAME = "GROUND";
    public static final String IMG_NAME = "_Ground.png";
}
