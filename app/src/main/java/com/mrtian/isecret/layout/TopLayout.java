package com.mrtian.isecret.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrtian.isecret.R;

/**
 * Created by tianxiying on 16/12/7.
 */
public class TopLayout extends RelativeLayout {
    RelativeLayout back_rl;
    TextView title_tv;

    public TopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_top, this);
        back_rl = (RelativeLayout) findViewById(R.id.back_rl);
        title_tv = (TextView) findViewById(R.id.title_tv);
        back_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
    }

    public void setTitle(String title) {
        title_tv.setText(title);
    }
}
