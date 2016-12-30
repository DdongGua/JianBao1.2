package com.bgs.jianbao12;

/**
 * Created by Administrator on 2016/12/26.
 */

public class Constant {
    public static final int IO_BUFFER_SIZE = 10 * 1024;
    //公共接口
    public static final String URL = "http://223.72.255.100/Goods";
    //下载图片前缀
    public static final String IMGURL = URL+"/uploads/";
    //用户注册网址
    public static final String REGISTERURL = URL + "/app/common/register.json";

    //用户登录网址
    public static final String LOGINURL = URL + "/app/common/login.json";

    //版本更新网址
    public static final String VERSINURL = URL + "/app/common/version.json";

    //导航页网址
    public static final String NAVIURL = URL + "/app/common/navi.json";

    //个人已发布商品网址
    public static final String ISSUELISTURL = URL + "/app/user/issue_list.json";

    //已关注商品网址
    public static final String FOLLOWLISTURL = URL + "/app/user/follow_list.json";

    //个人信息查询网址
    public static final String INFOURL = URL + "/app/user/info.json";

    //上传头像网址
    public static final String UPLOADURL = URL + "/app/user/upload.json";

    //验证邀请码网址
    public static final String INVITEURL = URL + "/app/user/invite.json";

    //发布商品网址
    public static final String ISSUEURL = URL + "/app/item/issue.json";

    //商品列表查询网址
    public static final String LISTURL = URL + "/app/item/list.json";

    //商品详细查询网址
    public static final String DETAILURL = URL + "/app/item/detail.json";

    //商品状态变更网址
    public static final String MODIFYURL = URL + "/app/item/modify.json";

    //是否关注商品网址
    public static final String FOLLOWURL = URL + "/app/item/follow.json";


}
