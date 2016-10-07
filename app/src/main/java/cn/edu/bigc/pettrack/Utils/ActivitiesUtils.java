package cn.edu.bigc.pettrack.Utils;

import android.net.Uri;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import cn.edu.bigc.pettrack.Event.RefreshEvent;
import cn.edu.bigc.pettrack.PtApplication;

/**
 * Created by L T on 2016/10/6.
 */

public class ActivitiesUtils {
    public static void publish(AVUser user, String location, String time, String date, String info, String msg, Uri uri) {
        AVObject o=new AVObject("Activity");
        o.put("user",user);
        o.put("location",location);
        o.put("time",time);
        o.put("date",date);
        o.put("info",info);
        o.put("msg",msg);
        if(uri!=null){
            try {
                AVFile file=new AVFile(uri.getLastPathSegment()+"",UserUtils.readBytes(uri, PtApplication.getContext().getContentResolver()));
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        o.put("imgURL",file.getUrl());
                        o.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                EventBus.getDefault().post(new RefreshEvent(true));
                            }
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            o.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    EventBus.getDefault().post(new RefreshEvent(true));
                }
            });
        }
    }
    public static void queryAllActivities(FindCallback callback){
        AVQuery<AVObject> q=new AVQuery<>("Activity");
        q.include("user");
        q.orderByDescending("createdAt");
        q.findInBackground(callback);
    }
    public static void queryPeople(AVObject act, FindCallback callback){
        AVQuery<AVObject> q=new AVQuery<>("UserActivity");
        q.whereEqualTo("activity",act);
        q.include("user");
        q.orderByDescending("createdAt");
        q.findInBackground(callback);
    }
    public static void queryActivities(AVUser user, FindCallback callback){
        AVQuery<AVObject> q=new AVQuery<>("UserActivity");
        q.whereEqualTo("user",user);
        q.include("activity.user");
        q.orderByDescending("createdAt");
        q.findInBackground(callback);
    }
    public static void subscribe(AVObject act){
        int peopleNum=act.getInt("people");
        act.put("people",++peopleNum);
        AVObject o=new AVObject("UserActivity");
        o.put("activity",act);
        o.put("user",AVUser.getCurrentUser());
        o.saveInBackground();
    }
}
