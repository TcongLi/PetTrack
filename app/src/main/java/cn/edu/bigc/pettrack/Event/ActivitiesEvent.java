package cn.edu.bigc.pettrack.Event;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * Created by L T on 2016/10/7.
 */

public class ActivitiesEvent {
    AVObject activites;
    AVUser user;

    public ActivitiesEvent(AVObject activites, AVUser user) {
        this.activites = activites;
        this.user = user;
    }

    public AVObject getActivites() {
        return activites;
    }

    public void setActivites(AVObject activites) {
        this.activites = activites;
    }

    public AVUser getUser() {
        return user;
    }

    public void setUser(AVUser user) {
        this.user = user;
    }
}
