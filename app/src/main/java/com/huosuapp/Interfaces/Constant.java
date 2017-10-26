package com.huosuapp.Interfaces;

public class Constant {
public static final String appkey="cy_app";
	
	//base url @ling
	public static final String URL_BASE = "http://www.3394.net/app";
	/**
	 * 监测版本版本信息 是否需要更新
    */
    public static final String URL_SPLASE_UPDATE=URL_BASE+"/jiekou/index.php?m=App&a=getVersions";

    /**
	 * Home推荐栏首页轮播导航的图片url地址
	 */
	public static final String URL_HOME_RECOMMAND_NAV=URL_BASE+"/jiekou/index.php?m=App&a=index";

	/**
	 * 游戏详细信息
	 */
    public static final String URL_GAMEDTAIL_MSG=URL_BASE+"/jiekou/index.php?m=App&a=gameDetail";

    /**
     * 游戏栏目列表
     */
    public static final String URL_GAME_LANMU=URL_BASE+"/jiekou/index.php?m=App&a=gameList";
    
    /**
     * 开服表加载地址
     */
    public static final String URL_MAINLOAD_TABLE=URL_BASE+"/jiekou/index.php?m=App&a=serverList";

    /**
     * 游戏礼包条目地址
     */
    public static final String URL_GAMEGIFT_ITEM=URL_BASE+"/jiekou/index.php?m=App&a=giftList";
    /**
     * 根据infoid 淘取礼包
     * */
    public static final String URL_AMOY_GAMEGIFT=URL_BASE+"/jiekou/index.php?m=App&a=amoyGift";
    /**
     * 根据infoid获取礼包码
     */
    public static final String URL_GET_GAMEGIFT=URL_BASE+"/jiekou/index.php?m=App&a=setLibao";
    /**
     * 查询用户是否领取过礼包
     * */
    public static final String URL_QUEGIFT_NAME=URL_BASE+"/jiekou/index.php?m=App&a=querygiftBYname";
    /**
     * 根据infoid获取礼包的详细信息
     */
    public static final String URL_GAMEGIFT_DETAIL=URL_BASE+"/jiekou/index.php?m=App&a=giftDetail";
    
    /**
     * 获取mygame的相关信息
     */
    public static final String URL_GET_ALLGAME=URL_BASE+"/jiekou/index.php?m=App&a=getMyGameList";
    
    /**
     * 根据用户的信息获取更新游戏列表
     */
    public static final String URL_GET_GAMEUPDATE=URL_BASE+"/jiekou/index.php?m=App&a=getUpdateInfo";

    /**
     * 根据gameid统计下载次数
     */
    public static final String URL_DOWNLOAD_COUNT=URL_BASE+"/jiekou/index.php?m=App&a=setCount";
    
    /**
     * 根据关键字 获取搜索提示
     */
    public static final String URL_SEARCH_TIP=URL_BASE+"/jiekou/index.php?m=App&a=getKeyGame";

    /**
     * 获取游戏名称搜索出的数据
     */
    public static final String URL_SEARCH_FIND=URL_BASE+"/jiekou/index.php?m=App&a=getFindGame";
    
    /**
     * 获取游戏类别列表
     */
    public static final String URL_GET_GAME_CATEGORY=URL_BASE+"/jiekou/index.php?m=App&a=typeList";
    
    /**
     * 根据游戏类表获取游戏列表【列表结果有最热和最新】
     */
    public static final String URL_GET_GAMELIST_BYCATEGORY=URL_BASE+"/jiekou/index.php?m=App&a=tgameList";
 
    /**
     * 用户注册
     */
    public static final String URL_USER_REGISTER=URL_BASE+"/jiekou/index.php?m=App&a=reg";
    
    /**
     * 用户登录功能的接口
     */
    public static final String URL_USR_LOGIN=URL_BASE+"/jiekou/index.php?m=App&a=login";
    
    /**
     * 修改密码  传递旧的用户名，旧密码，新密码
     */
    public static final String URL_USER_RPWD=URL_BASE+"/jiekou/index.php?m=App&a=updatePwd";
    
    /**
     * 分页获取奖品列表信息
     */
    public static final String URL_CONVERSONLIST=URL_BASE+"/jiekou/index.php?m=App&a=jfList";
    
    /**
     * 分页获取活动列表信息
     */
    public static final String URL_CONVERSON_TASK_LIST=URL_BASE+"/jiekou/index.php?m=App&a=rwList";
    
    /**
     * 虚拟兑换接口
     */
    public static final String URL_CONVERSION_GIFT_VIRTUAL=URL_BASE+"/jiekou/index.php?m=App&a=setjfLibao";

    /**
     * 实物兑换页面
     */
    public static final String URL_CONVERSION_GIFT_ACTUAL=URL_BASE+"/jiekou/index.php?m=App&a=setAddress";
    
    
    /**
     * 参加任务活动接口
     */
    public static final String URL_CONVERSION_TASK_VIRTUAL=URL_BASE+"/jiekou/index.php?m=App&a=setTask";
    
    
    /**
     * 用户意见反馈接口
     */
    public static final String URL_USER_ADVICE_BACK=URL_BASE+"/jiekou/index.php?m=App&a=setAsk";
    
    /**
     * 获取历史兑换积分列表集合
     */
    public static final String URL_CONVERSION_LIST=URL_BASE+"/jiekou/index.php?m=App&a=dhlog";
    

    /**
     * 获取积分总数
     */
    public static final String URL_SUMJIFEN_LIST=URL_BASE+"/jiekou/index.php?m=App&a=jfcount";
    
    /**
     * 获取积分历史记录
     */
    public static final String URL_USER_JIFEN_LIST=URL_BASE+"/jiekou/index.php?m=App&a=jflog";
    
    /**
     * 添加下载量记录
     */
    public static final String URL_USER_JIFEN_DOWNNUM=URL_BASE+"/jiekou/index.php?m=App&a=downNum";
    /**
     * 添加启动量记录
     */
    public static final String URL_USER_JIFEN_STARTLOG=URL_BASE+"/jiekou/index.php?m=App&a=startLog";

}
