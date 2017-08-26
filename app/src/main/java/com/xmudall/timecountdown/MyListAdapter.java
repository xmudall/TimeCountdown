package com.xmudall.timecountdown;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.xmudall.timecountdown.storage.Target;

import java.util.List;

/**
 * Created by udall on 2017/8/26.
 */

public class MyListAdapter extends BaseAdapter {

    private static final String TAG = MyListAdapter.class.getName();

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private List<Target> data;
    private Context context;
    private OnItemClickListener listener;

    static class ViewHolder {
        public CheckBox checkBox;
    }

    public MyListAdapter(List<Target> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // reuse views
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) convertView.getTag();
        String content = data.get(position).getContent();
        holder.checkBox.setText(content);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });

        return convertView;
    }

    public List<Target> getData() {
        return data;
    }

    public void setData(List<Target> data) {
        this.data = data;
    }
}
