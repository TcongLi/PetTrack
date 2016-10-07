package cn.edu.bigc.pettrack.Event;

import com.avos.avoscloud.AVObject;

/**
 * Created by L T on 2016/10/6.
 */

public class RecordEvent {
    AVObject record;

    public RecordEvent(AVObject record) {
        this.record = record;
    }

    public AVObject getRecord() {
        return record;
    }

    public void setRecord(AVObject record) {
        this.record = record;
    }
}
