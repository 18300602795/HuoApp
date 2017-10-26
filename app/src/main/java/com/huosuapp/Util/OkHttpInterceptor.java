package com.huosuapp.Util;



import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/6/28.
 */

public class OkHttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response=chain.proceed(chain.request());
        return response.newBuilder().removeHeader("pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control","max-age="+ 3600 * 24 * 30).build();
    }
}
