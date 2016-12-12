package com.mrtian.isecret.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.mrtian.isecret.MyApplication;
import com.mrtian.isecret.R;
import com.mrtian.isecret.db.MyDBManager;
import com.mrtian.isecret.entity.Secret;
import com.mrtian.isecret.layout.TopLayout;
import com.mrtian.isecret.util.AesEncDec;
import com.mrtian.isecret.util.Constant;
import com.mrtian.isecret.util.Utils;

import java.util.List;

public class DataInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText data_edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_in);
        ((TopLayout) findViewById(R.id.top_rl)).setTitle(this.getResources().getString(R.string.data_in));
        findViewById(R.id.in_btn).setOnClickListener(this);
        data_edt = (EditText) findViewById(R.id.data_edt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.in_btn:
                inData();
                break;
        }
    }

    /**
     * 将编辑框数据
     */
    private void inData() {
        final String data_aes = data_edt.getText().toString();
        if (TextUtils.isEmpty(data_aes)) {
            Toast.makeText(MyApplication.context, "请将正确的数据填入下面的编辑框,然后点击导入!", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String data = AesEncDec.decrypt(Constant.data_key, data_aes);
                    List secret_list = Utils.gson.fromJson(data, new TypeToken<List<Secret>>() {
                    }.getType());
                    MyDBManager myDB = new MyDBManager(MyApplication.context);
                    myDB.add(secret_list);
                    myDB.closeDB();
                    data_edt.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.context, "导入成功!", Toast.LENGTH_SHORT).show();
                            data_edt.setText("");
                            MainActivity.ISUPDATE = false;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    data_edt.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.context, "数据错误,请检查!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }).start();
    }
}
