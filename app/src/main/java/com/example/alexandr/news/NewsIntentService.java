package com.example.alexandr.news;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.IOException;

public class NewsIntentService extends IntentService {

    private static boolean autoUpdate;
    private final int SLEEP = 15*1000;
    public final static int RESULT_SUCCESS = 1;
    public final static int RESULT_ERROR = 2;
    public final static String ACTION_NEWS = "action.NEWS";
    public final static String EXTRA_NEWS_RESULT_RECEIVER = "extra.EXTRA_NEWS_RESULT_RECEIVER";
    public final static String EXTRA_NEWS_RESULT = "extra.EXTRA_NEWS_RESULT";
    public final static String EXTRA_FLAG = "extra.EXTRA_NEWS_FLAG";

    public NewsIntentService() {
        super("NewsIntentService");
        autoUpdate = false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NEWS.equals(action)) {
                final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_NEWS_RESULT_RECEIVER);
                autoUpdate = intent.getBooleanExtra(EXTRA_FLAG,false);
                while (autoUpdate){
                    handleActionNews(receiver);
                    try {
                        Thread.sleep(SLEEP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handleActionNews(receiver);
            }
        }
    }

    public static void setAutoUpdate(boolean autoUpdate) {
        NewsIntentService.autoUpdate = autoUpdate;
    }

    public static boolean isAutoUpdate() {
        return autoUpdate;
    }

    private void handleActionNews(final ResultReceiver receiver) {
        final Bundle data = new Bundle();
        try {
            data.putBoolean(EXTRA_NEWS_RESULT, NewsProcessor.processUpdate(this));
            receiver.send(RESULT_SUCCESS, data);
        }catch (IOException ex){
            data.putString(EXTRA_NEWS_RESULT, ex.getMessage());
            receiver.send(RESULT_ERROR,data);
        }
    }
}
