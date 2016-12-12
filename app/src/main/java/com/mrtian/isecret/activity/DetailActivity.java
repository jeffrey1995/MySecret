package com.mrtian.isecret.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mrtian.isecret.R;
import com.mrtian.isecret.db.MyDBManager;
import com.mrtian.isecret.util.AesEncDec;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText title_edt;
    private EditText text_edt;
    private TextView change_tv;
    private String title;
    private String text;
    private String _id;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title_edt = (EditText) findViewById(R.id.title_edt);
        text_edt = (EditText) findViewById(R.id.text_edt);
        change_tv = (TextView) findViewById(R.id.change_tv);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        text = intent.getStringExtra("text");
        _id = intent.getStringExtra("_id");
        key = intent.getStringExtra("key");
        title_edt.setText(title);
        text_edt.setText(text);

        findViewById(R.id.back_rl).setOnClickListener(this);
        change_tv.setOnClickListener(this);
        title_edt.setFocusableInTouchMode(false);
        text_edt.setFocusableInTouchMode(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.change_tv:
                changeText();
                break;
        }
    }

    private void changeText() {
        if ("编辑".equals(change_tv.getText().toString())) {
            change_tv.setText("保存");
            title_edt.setFocusableInTouchMode(true);
            text_edt.setFocusableInTouchMode(true);
        } else {
            String title = title_edt.getText().toString();
            String text = text_edt.getText().toString();
            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("_id",_id+"");
                map.put("title",title);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
                String date = df.format(new Date());    //获取系统时间
                map.put("date",date);
                String encrypted = null;
                try {
                    encrypted = AesEncDec.encrypt(key, text);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,"修改失败!",Toast.LENGTH_SHORT).show();
                    return;
                }
                map.put("text",encrypted);
                int result = -1;
                try {
                    MyDBManager manager = new MyDBManager(this);
                    result = manager.update(map);
                    manager.closeDB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (result == 0) {
                    Toast.makeText(this,"修改成功!",Toast.LENGTH_SHORT).show();
                    change_tv.setText("编辑");
                    title_edt.setFocusableInTouchMode(false);
                    text_edt.setFocusableInTouchMode(false);
                    setResult(MainActivity.RESULT_CODE_CHANGED);
                    finish();
                } else {
                    Toast.makeText(this,"修改失败!",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this,"标题和内容不能为空!",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
