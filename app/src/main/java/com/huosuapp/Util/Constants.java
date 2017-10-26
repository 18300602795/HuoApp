package com.huosuapp.Util;


import com.huosuapp.text.BuildConfig;

/**
 * author choose
 * 2016年8月19日下午16:08:07
 */
public class Constants {
    /**
     * 加密解密的 key
     */
	public static String appkey="";
    //公共参数
    public static final String APP_ID="appid";
    public static final String CLIENT_ID="clientid";
    public static final String FROM="from";
    public static final String AGENT="agent";
    
    public static String appid="";
	public static String agent="";
	public static String clientid="";
	public static String from="3";

    //public static final String URL_BASE = "http://sdk.huosdk.com/api/public/v1";

   // public static final String  URL_BASE="http://sdk.1tsdk.com/api/public/v1";
    public static final String BASE = BuildConfig.BASE_URL;
    public static final String URL_BASE_FOR_KEFU = BASE;
    public static final String  URL_BASE=BASE+"/api/public/index.php/v1";
	/**
	 * 游戏列表
	 */
	public static final String URL_GAME_LIST=URL_BASE+"/game/list";
	
	/**
	 * 游戏详情
	 */
	public static final String URL_GAME_DETAIL=URL_BASE+"/game/detail";

	/**
	 * 获取游戏评论列表
	 */
    public static final String URL_COMMENT_LIST=URL_BASE+"/game/comment_list";
    
    /**
     * 添加游戏评论
     */
    public static final String URL_COMMENT_ADD=URL_BASE+"/game/comment_add";
    
    /**
     * 获取游戏类型列表
     */
    public static final String TYPE_LIST=URL_BASE+"/game/type_list";

    /**
     * 获取礼包列表
     */
    public static final String GIFT_LIST=URL_BASE+"/gift/list";
    /**
     * 激活码列表
     */
 public static final String CDKEY_LIST=URL_BASE+"/cdkey/list";

    /**
     * 用户注册
     * */
    public static final String USER_ADD=URL_BASE+"/user/add";

    /**
     * 用户登录
     */
    public static final String USER_LOGIN=URL_BASE+"/user/login";

    /**
     * 获取礼包用户列表
     * */
    public static final String GIFY_LIST=URL_BASE+"/user/gift/list";
    /**
     * 获取激活码用户列表
     * */
    public static final String USER_CDKEY_LIST=URL_BASE+"/user/cdkey/list";


    /**
     * 领取礼包
     */
    public static final String GIFT_ADD=URL_BASE+"/user/gift/add";
    /**
     * 领取激活码
     */
    public static final String CDKEY_ADD=URL_BASE+"/user/cdkey/add";

    /**
     * 获取发送手机验证码
     */
    public static final String SMSCODE_SEND=URL_BASE+"/smscode/send";
    
    /**
     * 验证短信验证码
     */
//    public static final String SMSCODE_CHECK=URL_BASE+"/smscode/check";


    /**
     * 获取客服信息
     */
    public static final String GET_HELP_INFO=URL_BASE+"/system/get_help_info";
    
    /**
     * 获取服务器时间
     */
    public static final String GET_SERVER_TIME=URL_BASE+"/system/get_server_time";

    /**
     * 获取开机闪屏图
     */
//    public static final String URL_SEARCH_FIND=URL_BASE+"/system/get_splash";
    
    /**
     *打开APP
     */
//    public static final String URL_OPENAPP=URL_BASE+"/system/openapp";
    
    /**
     * 获取版本信息
     */
    public static final String URL_GET_VERSION_INFO=URL_BASE+"/game/get_version_info";
 
    /**
     * 获取是否有新版本
     */
    public static final String URL_HAS_NEW_VERSION=URL_BASE+"/system/has_new_version";


    /**
     * 搜索列表
     */
    public static final String URL_SEARCH_LIST=URL_BASE+"/search/list";

    /**
     * 获取推荐搜索
     */
    public static final String RECOMMEND_LIST=URL_BASE+"/search/recommend_list";

    /**
     * 获取搜索热词
     */
    public static final String HOTWORD_LIST=URL_BASE+"/search/hotword_list";

    /**
     * 获取资讯列表
     */
    public static final String NEWS_LIST=URL_BASE+"/news/list";

    /**
     * 获取资讯详情
     */
    public static final String NES_GETDETAIL=URL_BASE+"news/getdetail";

    /**
     * 资讯详情页
     */
    public static final String NEWS_WEBDETAIL_ID=URL_BASE+"/news/webdetail/";

    /**
     * 游戏下载
     */
    public static final String GAME_DOWN=URL_BASE+"/game/down";

    /**
     * 获取轮播图
     */
    public static final String SLIDE_LIST=URL_BASE+"/slide/list";

    /**
     * 开服表
     */
    public static final String TABLE_URL = BASE+"/sdk/open.php";


    /**
     * 获取用户钱包金额
     */
    public static final String URL_USER_WALLET=URL_BASE+"/user/wallet/get_balance";

    /**
     * 权限常量
     */
    public static final int WRITE_READ_EXTERNAL_CODE=0x01;

}
