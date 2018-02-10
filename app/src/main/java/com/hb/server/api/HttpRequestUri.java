package com.hb.server.api;

/**
 * Created by hanbin on 2018/2/5.
 * <p/>
 * Api请求URI
 */
public interface HttpRequestUri {

    String VERSION       = "/app/api/v1";

    /**
     * 用户添加
     */
    String MEMBER_ADD    = VERSION + "/member/add";

    /**
     * 删除用户
     */
    String DELETE        = VERSION + "/member/del";

    /**
     * 升级接口
     */
    String UPDATE        = VERSION + "/public/update";

    /**
     * 广告
     */
    String AD            = VERSION + "/public/ad";

    /**
     * 上传图片
     */
    String UPLOAD        = VERSION + "/member/upload";

}
