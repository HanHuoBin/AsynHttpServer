package com.hb.server.api;

import android.text.TextUtils;
import android.util.Log;

import com.koushikdutta.async.http.Multimap;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;


/**
 * Created by hanbin on 2018/2/5.
 * <p/>
 * App local server
 * App本地服务器
 */
public class LauncherHttpServer {
    private static final String TAG = "AsynHttpServer";

    private static LauncherHttpServer mInstance;

    public static int PORT_LISTEN_DEFAULT = 5000;

    AsyncHttpServer server = new AsyncHttpServer();

    public static LauncherHttpServer getInstance() {
        if (mInstance == null) {
            // 增加类锁,保证只初始化一次
            synchronized (LauncherHttpServer.class) {
                if (mInstance == null) {
                    mInstance = new LauncherHttpServer();
                }
            }
        }
        return mInstance;
    }

    /**
     * start Http server
     */
    public void startServer(HttpServerReqCallBack reqCallBack) {
        Log.i(TAG,"startServer");
        server.get("[\\d\\D]*", reqCallBack);
        server.post("[\\d\\D]*", reqCallBack);
        server.listen(PORT_LISTEN_DEFAULT);
    }

    /**
     * Http request's callback
     */
    public abstract static class HttpServerReqCallBack implements HttpServerRequestCallback {
        public abstract void onError(String uri, Multimap params, AsyncHttpServerResponse response);

        public abstract void onSuccess(String uri, Multimap params,
                                       AsyncHttpServerResponse response);

        public abstract void onFile(MultipartFormDataBody body,
                                    AsyncHttpServerResponse response);

        @Override
        public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
            Log.i(TAG,"onRequest");
            String uri = request.getPath();
            //获取header
            Multimap headers = request.getHeaders().getMultiMap();
            if (!TextUtils.isEmpty(uri)) {//针对的是接口的处理
                //获取post请求的参数的地方
                if (headers != null) {
                    Log.d(TAG, headers.toString());
                }
                if (uri.equals(HttpRequestUri.UPLOAD)) {
                    MultipartFormDataBody body = (MultipartFormDataBody) request.getBody();
                    onFile(body, response);
                } else {
                    Multimap params = (Multimap) request.getBody().get();
                    onSuccess(uri, params, response);
                }
            } else {
                response.send("Invalid request url.");
            }
        }
    }

}
