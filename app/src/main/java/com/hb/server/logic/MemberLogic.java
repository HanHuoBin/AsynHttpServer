package com.hb.server.logic;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.hb.server.api.HttpResponseCode;
import com.hb.server.utils.BitmapUtil;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.Multimap;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by hanbin on 2018/2/5.
 * <p/>
 * 用户逻辑
 */
public class MemberLogic extends BaseLogic {

    private static final String TAG = "MemberLogic";
    private static MemberLogic memberLogic = null;

    public MemberLogic() {

    }

    public static MemberLogic getInstance() {
        if (memberLogic == null) {
            synchronized (MemberLogic.class) {
                if (memberLogic == null) {
                    memberLogic = new MemberLogic();
                }
            }
        }
        return memberLogic;
    }

    /**
     * 添加用户
     *
     * @param params
     * @return
     */
    public JSONObject addMember(Multimap params) {
        String peopleName = params.getString("peopleName");
        String sex = params.getString("sex");
        if (TextUtils.isEmpty(peopleName)) {
            return onError(HttpResponseCode.Error, "请填写用户名");
        }
        if (TextUtils.isEmpty(sex)) {
            return onError(HttpResponseCode.Error, "请选择用户性别");
        }
        JSONObject result = new JSONObject();
        result.put("status", 1);
        result.put("peopleId", "1");
        return onSuccess(result, "用户添加成功");
    }

    /**
     * 删除（用户，卡，密码）
     *
     * @param params
     * @return
     */
    public JSONObject delete(Multimap params) {
        String peopleId = params.getString("peopleId");
        if (TextUtils.isEmpty(peopleId)) {
            return onError(HttpResponseCode.Error, "请选择要删除的用户");
        }
        JSONObject result = new JSONObject();
        result.put("status", 1);
        return onSuccess(result, "删除成功");
    }

    /**
     * 文件读写流
     */
    private BufferedOutputStream fileOutPutStream = null;
    private BufferedInputStream fileInputStream = null;
    private File file = null;
    private String filePath = "";

    /**
     * 上传图片
     *
     * @param body
     * @return
     */
    public void upload(final MultipartFormDataBody body, final AsyncHttpServerResponse response) {
        filePath = "/data/data/rk.device.launcher/files/temp";
        /**
         * 参数接收回调
         */
        body.setMultipartCallback(new MultipartFormDataBody.MultipartCallback() {
            @Override
            public void onPart(Part part) {
                if (part.isFile()) {
                    if (file == null || !file.exists()) {
                        String suffix = "";
                        switch (part.getContentType()) {
                            case "image/jpeg":
                                suffix = ".jpg";
                                break;
                            case "image/png":
                                suffix = ".png";
                                break;
                            default:
                                response.send(
                                        onError(HttpResponseCode.Error, "文件格式有误").toJSONString());
                                break;
                        }
                        filePath = filePath + System.currentTimeMillis() + suffix;
                        file = new File(filePath);
                    }
                    try {
                        fileOutPutStream = new BufferedOutputStream(new FileOutputStream(file));
                    } catch (FileNotFoundException e) {
                    }
                    body.setDataCallback(new DataCallback() {
                        @Override
                        public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                            writeData(bb);
                        }
                    });
                } else {
                    response.send(onError(HttpResponseCode.Error, "文件格式有误").toJSONString());
                }
            }
        });

        /**
         * 参数接收结束回调
         */
        body.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                try {
                    fileInputStream.close();
                    fileOutPutStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = BitmapUtil.decodeBitmapFromFile(filePath, 640, 480);
                JSONObject result = new JSONObject();
                result.put("faceId", "");
                response.send(onSuccess(result, "人脸同步成功").toJSONString());
            }
        });
    }

    /**
     * 写入文件
     *
     * @param bb
     */
    private void writeData(ByteBufferList bb) {
        byte[] buff = new byte[1024];
        try {
            fileInputStream = new BufferedInputStream(
                    new ByteArrayInputStream(bb.getAllByteArray()));
            int bytesRead = 0;
            while (-1 != (bytesRead = fileInputStream.read(buff, 0, buff.length))) {
                fileOutPutStream.write(buff, 0, bytesRead);
            }
            fileOutPutStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
