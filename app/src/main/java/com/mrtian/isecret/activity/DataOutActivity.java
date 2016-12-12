package com.mrtian.isecret.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mrtian.isecret.MyApplication;
import com.mrtian.isecret.R;
import com.mrtian.isecret.db.MyDBManager;
import com.mrtian.isecret.entity.Secret;
import com.mrtian.isecret.layout.TopLayout;
import com.mrtian.isecret.util.AesEncDec;
import com.mrtian.isecret.util.Constant;
import com.mrtian.isecret.util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataOutActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView data_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_out);
        ((TopLayout) findViewById(R.id.top_rl)).setTitle(this.getResources().getString(R.string.data_out));
        findViewById(R.id.out_btn).setOnClickListener(this);
        findViewById(R.id.copy_btn).setOnClickListener(this);
        findViewById(R.id.save_btn).setOnClickListener(this);
        data_tv = (TextView) findViewById(R.id.data_tv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.out_btn:
                outData();
                break;
            case R.id.copy_btn:
                if (!TextUtils.isEmpty(data_tv.getText().toString())) {
                    Utils.copy(data_tv.getText().toString(), this);
                    Toast.makeText(this, "已复制至剪切板", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "请先导出数据,然后复制", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.save_btn:
                saveToSdcard();
                break;
        }
    }

    /**
     * 将数据保存到sdcard
     */
    private void saveToSdcard() {
        if (!TextUtils.isEmpty(data_tv.getText().toString())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(Constant.path + Constant.fileName);
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        FileWriter fw = new FileWriter(file);
                        fw.write(data_tv.getText().toString());
                        fw.flush();
                        fw.close();
                        data_tv.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyApplication.context, "保存成功,路径:" + Constant.path + Constant.fileName, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Toast.makeText(this, "请先导出数据,然后保存", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 导出数据库数据
     */
    private void outData() {
        if (TextUtils.isEmpty(data_tv.getText().toString())) {
            data_tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyDBManager myDBManager = new MyDBManager(MyApplication.context);
                    final List<Secret> list = myDBManager.query();
                    myDBManager.closeDB();
                    data_tv.post(new Runnable() {
                        @Override
                        public void run() {
                            String data_aes = "";
                            try {
                                data_aes = AesEncDec.encrypt(Constant.data_key, Utils.gson.toJson(list).toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!"".equals(data_aes)) {
                                data_tv.setText(data_aes);
                            } else {
                                data_tv.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MyApplication.context, "数据导出错误!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }).start();
        }
    }
}
