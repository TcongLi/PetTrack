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
 * Created by L T on 2016/10/3.
 */

public class RecordsUtils {
    public static void createRecord(String msg,String imgURL){
        AVObject o = new AVObject("PetRecord");
        o.put("user", AVUser.getCurrentUser());
        o.put("msg",msg);
        if(!(imgURL==null||imgURL.isEmpty())){
            o.put("imgURL",imgURL);
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
    public static void queryRecord(AVUser user, FindCallback callback){
        AVQuery<AVObject> q=new AVQuery<>("PetRecord");
        q.whereEqualTo("user",user);
        q.orderByDescending("createdAt");
        q.include("user");
        q.findInBackground(callback);
    }
}
