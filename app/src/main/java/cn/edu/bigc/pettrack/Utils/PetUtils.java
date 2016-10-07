package cn.edu.bigc.pettrack.Utils;

import android.net.Uri;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.io.IOException;

import cn.edu.bigc.pettrack.PtApplication;

/**
 * Created by L T on 2016/9/30.
 */

public class PetUtils {
    public static final boolean MALE = true;
    public static final boolean FEMALE = false;

    public static void createPet(String petName, int age, boolean gender, String introduction, Uri uri) {
        AVUser user = AVUser.getCurrentUser();
        AVObject o = new AVObject("Pet");
        o.put("userID", AVUser.getCurrentUser().getObjectId());
        o.put("petName", petName);
        o.put("age", age);
        o.put("gender", gender);
        o.put("introduction", introduction);
        if (uri != null) {
            AVFile file = null;
            try {
                file = new AVFile(uri.getLastPathSegment() + "", UserUtils.readBytes(uri, PtApplication.getContext().getContentResolver()));
                o.put("avatar", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        o.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                user.put("pet", o);
                user.saveInBackground();
            }
        });

    }

}
