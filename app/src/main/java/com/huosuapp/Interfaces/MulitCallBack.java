package com.huosuapp.Interfaces;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu hong liang on 2016/9/13.
 */
public  class MulitCallBack<T> extends RequestCallBack{
    private List<WeakReference<RequestCallBack>> downListenerList=new ArrayList<>();


    public void addListener(RequestCallBack requestCallBack){
        WeakReference<RequestCallBack> weakReference=new WeakReference<RequestCallBack>(requestCallBack);
        downListenerList.add(weakReference);
    }
    @Override
    public void onStart() {
        super.onStart();
        for(int i=downListenerList.size()-1;i>=0;i--){
            WeakReference<RequestCallBack> weakReference = downListenerList.get(i);
            if(weakReference.get()!=null){
                weakReference.get().onStart();
            }else{
                downListenerList.remove(i);
            }
        }
        onDownStart();
    }

    @Override
    public void onLoading(long total, long current, boolean isUploading) {
        super.onLoading(total, current, isUploading);
        for(int i=downListenerList.size()-1;i>=0;i--){
            WeakReference<RequestCallBack> weakReference = downListenerList.get(i);
            if(weakReference.get()!=null){
                weakReference.get().onLoading(total,current,isUploading);
            }else{
                downListenerList.remove(i);
            }
        }
        onDownLoading(total,current,isUploading);
    }
    @Override
    public void onCancelled() {
        super.onCancelled();
        for(int i=downListenerList.size()-1;i>=0;i--){
            WeakReference<RequestCallBack> weakReference = downListenerList.get(i);
            if(weakReference.get()!=null){
                weakReference.get().onCancelled();
            }else{
                downListenerList.remove(i);
            }
        }
        onDownCancelled();

    }

    @Override
    public void onSuccess(ResponseInfo responseInfo) {
        for(int i=downListenerList.size()-1;i>=0;i--){
            WeakReference<RequestCallBack> weakReference = downListenerList.get(i);
            if(weakReference.get()!=null){
                weakReference.get().onCancelled();
            }else{
                downListenerList.remove(i);
            }
        }
        onDownSuccess(responseInfo);
    }

    @Override
    public void onFailure(HttpException e, String s) {

    }

    public void onDownStart() {

    }

    public void onDownCancelled() {
    }

    public void onDownLoading(long total, long current, boolean isUploading) {
    }

    public void onDownSuccess(ResponseInfo<T> var1){

    };

    public void onDownFailure(HttpException var1, String var2){

    };
}
