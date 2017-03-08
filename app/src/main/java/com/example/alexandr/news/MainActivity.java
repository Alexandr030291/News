package com.example.alexandr.news;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.Storage;

public class MainActivity extends AppCompatActivity implements ServiceHelper.NewsResultListener{
    private int mRequestId;
    private boolean turnOn;
    private Button buttonAuto;
    private Button buttonUpdate;
    private TextView textTextTitle;
    private TextView textTextNews;
    private TextView textTextDate;

    private final View.OnClickListener onSettings = (view) ->
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));

    private final View.OnClickListener onUpdate = (view) ->
            newRequestID();

    private final View.OnClickListener onAuto = (view) ->{
        NewsIntentService.setAutoUpdate(!turnOn);
        turnOn = NewsIntentService.isAutoUpdate();
        drawAutoButton();
        if (turnOn) newRequestID();
    };

    private void drawAutoButton(){
        if (turnOn)
            buttonAuto.setText(R.string.turn_on);
        else
            buttonAuto.setText(R.string.turn_off);
    }

    private void newRequestID(){
        mRequestId = (mRequestId == 0)?
                ServiceHelper.getInstance().makeLikeNew(this, turnOn,this):mRequestId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);
        turnOn = NewsIntentService.isAutoUpdate();

        textTextTitle = (TextView) findViewById(R.id.title);
        textTextNews = (TextView) findViewById(R.id.text);
        textTextDate = (TextView) findViewById(R.id.time);

        findViewById(R.id.btn_settings).setOnClickListener(onSettings);
        findViewById(R.id.btn_update).setOnClickListener(onUpdate);

        buttonAuto = (Button) findViewById(R.id.btn_auto);
        buttonAuto.setOnClickListener(onAuto);

        buttonUpdate = (Button)findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(onUpdate);
    }

    @Override
    protected void onStop(){
        ServiceHelper.getInstance().removeListenerKill(mRequestId);
        mRequestId =0;
        super.onStop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (turnOn)
            newRequestID();
    }

    @Override
    public void onNewsResult(boolean success) {
        mRequestId = 0;
        drawAutoButton();
        News news = Storage.getInstance(MainActivity.this).getLastSavedNews();
        if (news != null) {
            textTextTitle.setText(news.getTitle());
            textTextDate.setText(DateFormat.format("MM/dd/yyyy HH:mm:ss", new Date(news.getDate())).toString());
            textTextNews.setText(news.getBody());
        }
    }
}
