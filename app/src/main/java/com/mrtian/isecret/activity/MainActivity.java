package com.mrtian.isecret.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mrtian.isecret.MyApplication;
import com.mrtian.isecret.R;
import com.mrtian.isecret.adapter.SecretAdapter;
import com.mrtian.isecret.db.MyDBManager;
import com.mrtian.isecret.entity.Secret;
import com.mrtian.isecret.util.AesEncDec;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private ListView listView;

    private List<Secret> secretList = new ArrayList<Secret>();
    private SecretAdapter adapter;

    public static boolean ISUPDATE = true;  //数据是否更新的标记位

    public static final int REQUEST_CODE = 1;
    public static final int RESULT_CODE_CHANGED = 2;

    public static final int MESSAGE_UPDATE = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_UPDATE) {
                List<Secret> list = (List<Secret>) msg.obj;
                secretList.clear();
                for (Secret secret : list) {
                    secretList.add(secret);
                }
                adapter.notifyDataSetChanged();
                ISUPDATE = true;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!ISUPDATE) {
            refreshDate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new SecretAdapter(this, R.layout.list_item, secretList);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        ItemOnLongClick();
        findViewById(R.id.add_rl).setOnClickListener(this);
        findViewById(R.id.setting_rl).setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("txy", "position:" + position);
                getKey(position);
            }
        });

        refreshDate();
    }

    private void getKey(final int position) {
        final EditText editText = new EditText(this);
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        new AlertDialog.Builder(this).setTitle("请输入口令!").setIcon(android.R.drawable.ic_lock_idle_lock).setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String key = "" + editText.getText().toString();
                try {
                    Secret secret = secretList.get(position);
                    String text = AesEncDec.decrypt(key, secret.getText());
                    final Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("_id", secret.getId() + "");
                    intent.putExtra("title", secret.getTitle());
                    intent.putExtra("text", text);
                    intent.putExtra("key", key);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    }, 500);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "口令错误,请重试!", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("txy", requestCode + "," + resultCode);
        if (resultCode == RESULT_CODE_CHANGED) {
            refreshDate();
        }
    }

    /**
     * 从数据库读取数据更新listView数据
     */
    private void refreshDate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDBManager myDBManager = new MyDBManager(MyApplication.context);
                List<Secret> list = myDBManager.query();
                myDBManager.closeDB();
                Message message = new Message();
                message.what = MESSAGE_UPDATE;
                message.obj = list;
                handler.sendMessage(message);
            }
        }).start();
    }

    //注：setOnCreateContextMenuListener是与下面onContextItemSelected配套使用的
    private void ItemOnLongClick() {
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, "查看");
                menu.add(0, 1, 0, "删除");
            }
        });
    }

    // 长按菜单响应函数
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int position = info.position;
        Log.d("txy", position + "");
        switch (item.getItemId()) {
            case 0:
                // 查看操作
                getKey(position);
                break;
            case 1:
                // 删除操作
                makeSure(position);
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);

    }

    /**
     * 确认删除
     *
     * @param position
     */
    private void makeSure(final int position) {

        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("想清楚了吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cancelText(position)) {
                    Toast.makeText(getApplicationContext(), "删除成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "操作失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    private boolean cancelText(int position) {
        MyDBManager myDBManager = new MyDBManager(this);
        try {
            myDBManager.remove(secretList.get(position).getId() + "");
            myDBManager.closeDB();
            refreshDate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_rl:
                startActivityForResult(new Intent(MyApplication.context, AddSecretActivity.class), REQUEST_CODE);
                break;
            case R.id.setting_rl:
                startActivity(new Intent(MyApplication.context, SettingsActivity.class));
                break;
        }
    }
}
