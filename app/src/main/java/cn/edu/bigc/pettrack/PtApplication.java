package cn.edu.bigc.pettrack;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by L T on 2016/9/22.
 */

public class PtApplication extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();

        AVOSCloud.initialize(this, "aVPQjjfxt1qwOOLDzmJtG8xj-gzGzoHsz", "g0zjGM7A86nByL14tDG67N6P");
    }
    public static Context getContext(){
        return  context;
    }
}
