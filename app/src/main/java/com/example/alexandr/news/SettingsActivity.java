package com.example.alexandr.news;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;

import static ru.mail.weather.lib.Topics.*;

public class SettingsActivity extends AppCompatActivity {
    private Storage storage;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;

    private final View.OnClickListener onSelect1 = (view) -> {
        storage.saveCurrentTopic(AUTO);
        draw();
    };

    private final View.OnClickListener onSelect2 = (view) -> {
        storage.saveCurrentTopic(HEALTH);
        draw();
    };

    private final View.OnClickListener onSelect3 = (view) -> {
        storage.saveCurrentTopic(IT);
        draw();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        radioButton1 = (RadioButton) findViewById(R.id.cancel_action_1);
        radioButton2 = (RadioButton) findViewById(R.id.cancel_action_2);
        radioButton3 = (RadioButton) findViewById(R.id.cancel_action_3);

        radioButton1.setOnClickListener(onSelect1);
        radioButton2.setOnClickListener(onSelect2);
        radioButton3.setOnClickListener(onSelect3);

        radioButton1.setText(R.string.Category1);
        radioButton2.setText(R.string.Category2);
        radioButton3.setText(R.string.Category3);

        storage = Storage.getInstance(this);
        draw();
    }

    @Override
    protected void onStop(){

        super.onStop();
    }

    private void draw(){
        radioButton1.setChecked(false);
        radioButton2.setChecked(false);
        radioButton3.setChecked(false);
        final String category = storage.loadCurrentTopic();
        if (category.equals(AUTO))
            radioButton1.setChecked(true);
        else if (category.equals(HEALTH))
            radioButton2.setChecked(true);
        else if (category.equals(IT))
            radioButton3.setChecked(true);
    }

}
