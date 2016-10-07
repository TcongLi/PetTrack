package cn.edu.bigc.pettrack.Event;

import com.avos.avoscloud.AVUser;

/**
 * Created by L T on 2016/9/27.
 */

public class UserEvent {
    AVUser user;

    public UserEvent(AVUser user) {
        this.user = user;
    }

    public AVUser getUser() {
        return user;
    }

    public void setUser(AVUser user) {
        this.user = user;
    }
}
