package com.hb.server.logic;


import com.alibaba.fastjson.JSONObject;
import com.hb.demo.App;
import com.hb.server.utils.PackageUtils;
import com.koushikdutta.async.http.Multimap;

/**
 * Created by hanbin on 2018/2/5.
 */
public class PublicLogic extends BaseLogic {

    private static final String TAG               = "PublicLogic";
    private static PublicLogic  publicLogic       = null;

    public PublicLogic() {
    }

    public static PublicLogic getInstance() {
        if (publicLogic == null) {
            synchronized (PublicLogic.class) {
                if (publicLogic == null) {
                    publicLogic = new PublicLogic();
                }
            }
        }
        return publicLogic;
    }

    /**
     * 升级接口
     *
     * @param params
     * @return
     */
    public JSONObject update(Multimap params) {

        JSONObject result = new JSONObject();
        result.put("code", PackageUtils.getCurrentVersionCode(App.getContext()));
        result.put("ver", PackageUtils.getCurrentVersion(App.getContext()));
        return onSuccess(result, "请求成功");
    }

}
