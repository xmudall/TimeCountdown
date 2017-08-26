package com.xmudall.timecountdown.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.Iterator;
import java.util.List;

/**
 * Created by udall on 2017/8/26.
 */

public class TargetDaoSPImpl implements TargetDao {

    private static final String SP_TARGET = "target";
    private static final String KEY_TARGET = "target";
    private static final String TAG = TargetDaoSPImpl.class.getName();
    private Context context;
    private SharedPreferences sp;

    public TargetDaoSPImpl(Context context) {
        this.context = context;
        this.sp = context.getSharedPreferences(SP_TARGET, Context.MODE_PRIVATE);
    }

    @Override
    public synchronized Target insert(Target target) {
        String json = sp.getString(KEY_TARGET, "[]");
        List<Target> targets = JSON.parseArray(json, Target.class);
        long maxId = targets.size() > 0 ? targets.get(targets.size() - 1).getId() : 0;
        target.setId(maxId + 1);
        targets.add(target);
        Log.i(TAG, "inserting target id: " + target.getId());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_TARGET, JSON.toJSONString(targets));
        editor.commit();
        return target;
    }

    @Override
    public synchronized boolean delete(long id) {
        Log.i(TAG, "deleting target id: " + id);
        String json = sp.getString(KEY_TARGET, "[]");
        List<Target> targets = JSON.parseArray(json, Target.class);
        for (Iterator<Target> it = targets.iterator(); it.hasNext(); ) {
            Target old = it.next();
            Log.d(TAG, "old id: " + old.getId());
            if (old.getId() == id) {
                it.remove();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(KEY_TARGET, JSON.toJSONString(targets));
                editor.commit();
                return true;
            }
        }
        Log.i(TAG, "id not found: " + id);
        return false;
    }

    @Override
    public synchronized boolean update(long id, Target target) {
        String json = sp.getString(KEY_TARGET, "[]");
        List<Target> targets = JSON.parseArray(json, Target.class);
        for (Iterator<Target> it = targets.iterator(); it.hasNext(); ) {
            Target old = it.next();
            if (old.getId() == id) {
                old.setContent(target.getContent());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(KEY_TARGET, JSON.toJSONString(targets));
                editor.commit();
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized List<Target> getAll() {
        String json = sp.getString(KEY_TARGET, "[]");
        List<Target> targets = JSON.parseArray(json, Target.class);
        return targets;
    }
}
