package com.example.alexandr.news;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.Hashtable;
import java.util.Map;

class ServiceHelper {
    private int mIdCounter = 1;
    private boolean turnOn;
    private final Map<Integer, NewsResultReceiver> mResultReceivers = new Hashtable<>();

    private static ServiceHelper instance;

    private ServiceHelper(){}

    synchronized static ServiceHelper getInstance(){
        if (instance == null){
            instance = new ServiceHelper();
        }
        return instance;
    }

    int makeLikeNew(final Context context, final boolean turnON,final NewsResultListener listener){
        final NewsResultReceiver receiver = new NewsResultReceiver(mIdCounter, new Handler());
        receiver.setListener(listener);
        mResultReceivers.put(mIdCounter,receiver);
        this.turnOn = turnON;

        Intent intent = new Intent(context, NewsIntentService.class);
        intent.setAction(NewsIntentService.ACTION_NEWS);
        intent.putExtra(NewsIntentService.EXTRA_NEWS_RESULT_RECEIVER,receiver);
        intent.putExtra(NewsIntentService.EXTRA_FLAG,turnON);
        context.startService(intent);

        return mIdCounter++;
    }

    void removeListener(final int id) {
        if (!turnOn) {
            NewsResultReceiver receiver = mResultReceivers.remove(id);
            if (receiver != null) {
                receiver.setListener(null);
            }
        }
    }

    void removeListenerKill(final int id){
        NewsResultReceiver receiver = mResultReceivers.remove(id);
        if (receiver != null){
            receiver.setListener(null);
        }
    }

    interface NewsResultListener {
        void onNewsResult(final boolean success);
    }

}
