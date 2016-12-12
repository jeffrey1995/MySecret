package com.mrtian.isecret.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mrtian.isecret.R;
import com.mrtian.isecret.db.MyDBManager;
import com.mrtian.isecret.entity.Secret;
import com.mrtian.isecret.layout.TopLayout;
import com.mrtian.isecret.util.AesEncDec;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddSecretActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText title_edt;
    private EditText text_edt;
    private EditText key_edt;
    private EditText key_again_edt;
    private String title;
    private String text;
    private String key;
    private String key_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_secret);

        title_edt = (EditText) findViewById(R.id.title_edt);
        text_edt = (EditText) findViewById(R.id.text_edt);
        key_edt = (EditText) findViewById(R.id.key_edt);
        key_again_edt = (EditText) findViewById(R.id.key_again_edt);
        ((TopLayout)findViewById(R.id.top_rl)).setTitle("添加");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = title_edt.getText().toString();
                text = text_edt.getText().toString();
                key = key_edt.getText().toString();
                key_again = key_again_edt.getText().toString();
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)) {
                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(key_again)) {
                        if (key.equals(key_again)) {
                            saveText();
                        } else {
                            Toast.makeText(getApplication(), "两次输入的口令不一致!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplication(), "请输入口令!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplication(), "标题和内容不能为空哦!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveText() {
        try {
            String encrypted = AesEncDec.encrypt(key, text);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
            String date = df.format(new Date());    //获取系统时间
            Secret secret = new Secret(title, R.drawable.apple, encrypted, date);
            MyDBManager myDBManager = new MyDBManager(this);
            myDBManager.add(secret);
            myDBManager.closeDB();
            Toast.makeText(getApplication(), "保存成功!", Toast.LENGTH_SHORT).show();
            setResult(MainActivity.RESULT_CODE_CHANGED);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "操作失败,请重试!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
