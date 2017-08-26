package com.xmudall.timecountdown.storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by udall on 2017/8/26.
 */

public class SettingDaoSpImpl implements SettingDao {

    private static final String SP_SETTING = "setting";
    private static final String KEY_TARGET = "setting";
    private static final String TAG = SettingDaoSpImpl.class.getName();
    private SharedPreferences sp;

    public SettingDaoSpImpl(Context context) {
        this.sp = context.getSharedPreferences(SP_SETTING, Context.MODE_PRIVATE);
    }

    @Override
    public synchronized void save(Map<String, String> setting) {
        SharedPreferences.Editor editor = sp.edit();
        for (String s : setting.keySet()) {
            editor.putString(s, setting.get(s));
        }
        editor.commit();
    }

    @Override
    public synchronized Map<String, String> load() {
        return (Map<String, String>) sp.getAll();
    }
}
