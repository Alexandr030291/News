package com.example.alexandr.news;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

@SuppressLint("ParcelCreator")
class NewsResultReceiver extends ResultReceiver{
    private final int requestId;
    private ServiceHelper.NewsResultListener mListener;

    public NewsResultReceiver (int requestId, final Handler handler){
        super(handler);
        this.requestId =requestId;
    }

    void setListener(final ServiceHelper.NewsResultListener listener){ mListener = listener;}

    @Override
    protected void onReceiveResult(final int resultCode, final Bundle resultData){
        if (mListener != null) {
            final boolean success = (resultCode == NewsIntentService.RESULT_SUCCESS);
            mListener.onNewsResult(success);
        }
        ServiceHelper.getInstance().removeListener(requestId);
    }
}
