package com.mrtian.isecret.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mrtian.isecret.R;
import com.mrtian.isecret.layout.TopLayout;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ((TopLayout)findViewById(R.id.top_rl)).setTitle(this.getResources().getString(R.string.settings_about));
        //显示版本号
        try {
            PackageInfo pi = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            ((TextView) findViewById(R.id.version_name_tv)).setText("v" + pi.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
