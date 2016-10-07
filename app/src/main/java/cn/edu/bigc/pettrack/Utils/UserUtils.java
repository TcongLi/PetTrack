package cn.edu.bigc.pettrack.Utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by L T on 2016/9/29.
 */

public class UserUtils {
    public static final int FOLLOWER = 0;
    public static final int FOLLOWEE = 1;

    public static void getUserList(AVUser user, int type, FindCallback<AVUser> callback) {

        try {
            AVQuery<AVUser> followerQuery = null;
            if (type == FOLLOWER) {
                followerQuery = user.followerQuery(AVUser.class);
                followerQuery.include("follower");
            } else if (type == FOLLOWEE) {
                followerQuery = user.followeeQuery(AVUser.class);
                followerQuery.include("followee");
            }
            followerQuery.findInBackground(callback);

        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    public static void findUser(String username,FindCallback callback){
        AVQuery<AVUser> avuser=new AVQuery<>("_User");
        avuser.whereContains("username",username);
        avuser.findInBackground(callback);
    }

    public static void follow(AVUser user,FollowCallback callback) {
        AVUser.getCurrentUser().followInBackground(user.getObjectId(), callback);
    }

    public static void unfollow(AVUser user,FollowCallback callback){
        AVUser.getCurrentUser().unfollowInBackground(user.getObjectId(),callback);
    }

    public static byte[] readBytes(Uri uri, ContentResolver cr) throws IOException {
        if(uri==null){
            Log.e("URI", "readBytes: URI IS NULL" );
        }
        Log.e("URI", "readBytes: "+uri.getPath() );
        // this dynamically extends to take the bytes you read
        InputStream inputStream = cr.openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }
}
