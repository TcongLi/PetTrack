package cn.edu.bigc.pettrack.Event;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * Created by L T on 2016/10/3.
 */

public class StatusEvent {
    AVObject status;
    AVUser user;

    public StatusEvent(AVObject status, AVUser user) {
        this.status = status;
        this.user = user;
    }

    public AVObject getStatus() {
        return status;
    }

    public void setStatus(AVObject status) {
        this.status = status;
    }

    public AVUser getUser() {
        return user;
    }

    public void setUser(AVUser user) {
        this.user = user;
    }
}
