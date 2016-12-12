package com.mrtian.isecret.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mrtian.isecret.R;
import com.mrtian.isecret.layout.TopLayout;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ((TopLayout) findViewById(R.id.top_rl)).setTitle("设置");
        findViewById(R.id.explain_rl).setOnClickListener(this);
        findViewById(R.id.about_rl).setOnClickListener(this);
        findViewById(R.id.data_in_rl).setOnClickListener(this);
        findViewById(R.id.data_out_rl).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.explain_rl:
                startActivity(new Intent(this, ExplainActivity.class));
                break;
            case R.id.about_rl:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.data_in_rl:
                startActivity(new Intent(this, DataInActivity.class));
                break;
            case R.id.data_out_rl:
                startActivity(new Intent(this, DataOutActivity.class));
                break;
        }
    }
}
