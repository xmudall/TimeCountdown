package com.xmudall.timecountdown;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.alibaba.fastjson.JSON;
import com.xmudall.timecountdown.storage.SettingDaoSpImpl;
import com.xmudall.timecountdown.storage.Target;
import com.xmudall.timecountdown.storage.TargetDao;
import com.xmudall.timecountdown.storage.TargetDaoSPImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Implementation of App Widget functionality.
 */
public class MyAppWidget extends AppWidgetProvider {

    private static final String TAG = MyAppWidget.class.getName();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // set countdown hint
        Map<String, String> setting = new SettingDaoSpImpl(context).load();

        String hint = "";
        if (setting.containsKey("age") && setting.containsKey("birth")) {
            // generate countdown hint
            try {
                int age = Integer.valueOf(setting.get("age"));
                String birth = setting.get("birth");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(simpleDateFormat.parse(birth));
                calendar.add(Calendar.YEAR, age);
                long end = calendar.getTimeInMillis();
                int diff = (int) ((end - System.currentTimeMillis()) / 1000 / 3600 / 24);
                hint = String.format(
                        context.getResources().getString(R.string.label_countdown_hint),
                        String.valueOf(age),
                        String.valueOf(diff)
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

//        // set up intent
        TargetDao targetDao = new TargetDaoSPImpl(context);
        List<Target> targets = targetDao.getAll();
        StringBuilder sb = new StringBuilder();
        for (Target target : targets) {
            sb.append(target.isFinished() ? "\u25A3 " : "\u25A1 ");
            sb.append(target.getContent()).append('\n');
        }
//        Intent intent = new Intent(context, MyListViewService.class);
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//        intent.putExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, JSON.toJSONString(targets));
//        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
//        views.setRemoteAdapter(appWidgetId, intent);
        views.setTextViewText(R.id.label_countdown_hint, hint);
        views.setTextViewText(R.id.targets, sb.toString());

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

