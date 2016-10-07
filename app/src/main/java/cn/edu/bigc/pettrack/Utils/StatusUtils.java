package cn.edu.bigc.pettrack.Utils;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVStatusQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.InboxStatusFindCallback;
import com.avos.avoscloud.SaveCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.edu.bigc.pettrack.Event.RefreshEvent;

/**
 * Created by L T on 2016/9/29.
 */

public class StatusUtils {
    public static void sendStatus(String msg, String imgURL) {
        AVObject o = new AVObject("PetStatus");
        o.put("user", AVUser.getCurrentUser());
        o.put("msg", msg);
        if (!(imgURL == null || imgURL.isEmpty())) {
            o.put("imgURL", imgURL);
        }
        o.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    EventBus.getDefault().post(new RefreshEvent(true));
                }
            }
        });
    }

    public static void queryAllStatus(FindCallback<AVObject> callback) {
        AVQuery<AVObject> q = new AVQuery<>("PetStatus");
        q.orderByDescending("createdAt");
        q.include("user");
        q.findInBackground(callback);
    }

    //Query a certain user's status.
    public static void queryStatus(AVUser user, FindCallback<AVObject> callback) {
        AVQuery<AVObject> q = new AVQuery<>("PetStatus");
        q.whereEqualTo("user", user);
        q.include("user");
        q.orderByDescending("createdAt");
        q.findInBackground(callback);
    }

    public static void likeStatus(AVObject status){
        int likeNum=status.getInt("likeNum");
        status.put("likeNum",++likeNum);
        status.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                EventBus.getDefault().post(new RefreshEvent(false));
            }
        });
    }
}