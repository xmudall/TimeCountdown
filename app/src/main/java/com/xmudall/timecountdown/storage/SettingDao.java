package com.xmudall.timecountdown.storage;

import java.util.Map;

/**
 * Created by udall on 2017/8/26.
 */

public interface SettingDao {
    void save(Map<String, String> setting);
    Map<String, String> load();
}
