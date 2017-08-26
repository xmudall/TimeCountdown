package com.xmudall.timecountdown.storage;

import java.util.List;

/**
 * Created by udall on 2017/8/26.
 */

public interface TargetDao {

    Target insert(Target target);
    boolean delete(long id);
    boolean update(long id, Target target);
    List<Target> getAll();
}
