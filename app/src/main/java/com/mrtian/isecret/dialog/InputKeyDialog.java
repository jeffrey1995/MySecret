package com.mrtian.isecret.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mrtian.isecret.R;


/**
 * Created by tianxiying on 16/7/15.
 */
public class InputKeyDialog extends Dialog implements View.OnClickListener {
    private Context mContext;

    private EditText key_edt;
    private Button ok_btn;

    public final static int OK = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_btn:
                inputKeyDialogEventListener.customDialogEvent(OK,""+key_edt.getText().toString());
                hide();
                break;
        }
    }

    // 利用interface来构造一个回调函数
    public interface InputKeyDialogEventListener {
        public void customDialogEvent(int valueYouWantToSendBackToTheActivity, String key);
    }

    private InputKeyDialogEventListener inputKeyDialogEventListener;

    public InputKeyDialog(Context context, InputKeyDialogEventListener inputKeyDialogEventListener) {
        super(context);
        mContext = context;
        this.inputKeyDialogEventListener = inputKeyDialogEventListener;
    }

    public InputKeyDialog(Context context, int themeResId, InputKeyDialogEventListener inputKeyDialogEventListener) {
        super(context, themeResId);
        this.mContext = context;
        this.inputKeyDialogEventListener = inputKeyDialogEventListener;
    }

    private void findView() {
        key_edt = (EditText) findViewById(R.id.key_edt);
        ok_btn = (Button) findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_inputkey);
        findView();
    }
}
