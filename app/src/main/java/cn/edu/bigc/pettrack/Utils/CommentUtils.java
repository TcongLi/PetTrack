package cn.edu.bigc.pettrack.Utils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import org.greenrobot.eventbus.EventBus;

import cn.edu.bigc.pettrack.Event.RefreshEvent;

/**
 * Created by L T on 2016/10/5.
 */

public class CommentUtils {
    public static void sendComment(AVObject status, AVUser user,String msg){
        AVObject o=new AVObject("StatusComment");
        o.put("user",user);
        o.put("msg",msg);
        o.put("status",status);
        o.saveInBackground();
        int commentNum=status.getInt("commentNum");
        status.put("commentNum",++commentNum);
        status.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                EventBus.getDefault().post(new RefreshEvent(false));

            }
        });
    }
    public static void queryComment(AVObject status, FindCallback callback){
        AVQuery<AVObject> q=new AVQuery<>("StatusComment");
        q.whereEqualTo("status",status);
        q.include("user");
        q.orderByDescending("createdAt");
        q.findInBackground(callback);
    }
}
