package com.mrtian.isecret.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mrtian.isecret.R;
import com.mrtian.isecret.layout.TopLayout;

public class ExplainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
        ((TopLayout)findViewById(R.id.top_rl)).setTitle(this.getResources().getString(R.string.settings_explain));
    }
}
