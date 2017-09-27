package com.xmudall.timecountdown;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.alibaba.fastjson.JSON;
import com.xmudall.timecountdown.storage.Target;

import java.util.List;

/**
 * Created by udall on 2017/9/27.
 */

public class MyListViewService extends RemoteViewsService {

    private static final String TAG = MyListViewService.class.getName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }

    class MyRemoteViewFactory implements RemoteViewsFactory {

        private Context context;
        private int appWidgetId;
        private List<Target> targets;

        public MyRemoteViewFactory(Context context, Intent intent) {
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            String jsonStr = intent.getStringExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS);
            targets = JSON.parseArray(jsonStr, Target.class);
        }

        @Override
        public void onCreate() {
            Log.i(TAG, "onCreate");
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews convertView = new RemoteViews(context.getPackageName(), R.layout.list_item);
            // fill data
            Target target = targets.get(position);
            convertView.setTextViewText(R.id.checkBox, target.getContent());
            convertView.setBoolean(R.id.checkBox, "setChecked", target.isFinished());

            return convertView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
