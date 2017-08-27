package com.xmudall.timecountdown.storage;

/**
 * Created by udall on 2017/8/26.
 */

public class Target {

    private long id;
    private String content;
    private boolean finished;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setValue(Target target) {
        if (target != null) {
            setContent(target.getContent());
            setFinished(target.isFinished());
        }
    }
}
