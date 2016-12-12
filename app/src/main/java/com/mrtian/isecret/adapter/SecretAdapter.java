package com.mrtian.isecret.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrtian.isecret.R;
import com.mrtian.isecret.entity.Secret;

import java.util.List;

/**
 * Created by tianxiying on 16/10/26.
 */
public class SecretAdapter extends ArrayAdapter<Secret> {
    private int resourceId;

    public SecretAdapter(Context context, int resource, List<Secret> list) {
        super(context, resource, list);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Secret secret = getItem(position);

        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.title_iv = (ImageView) view.findViewById(R.id.title_iv);
            viewHolder.title_tv = (TextView) view.findViewById(R.id.title_tv);
            viewHolder.date_tv = (TextView) view.findViewById(R.id.date_tv);
            view.setTag(viewHolder); //将viewholder存储在viwe中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title_iv.setImageResource(secret.getImageId());
        viewHolder.title_tv.setText(secret.getTitle());
        viewHolder.date_tv.setText(secret.getDate());

        return view;
    }

    class ViewHolder {
        ImageView title_iv;
        TextView title_tv;
        TextView date_tv;
    }
}
