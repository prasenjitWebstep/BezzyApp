package com.bezzy.Ui.View.gcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bezzy.Ui.View.activity.Fragments.ChatFragment;
import com.bezzy.Ui.View.activity.LoginActivity;
import com.bezzy.Ui.View.activity.Massage;
import com.bezzy.Ui.View.activity.NotificationActivity;
import com.bezzy.Ui.View.activity.PostImageVideoViewActivity;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class MyFcmMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    Bitmap bitmap;
    String title,message,type;
    PendingIntent pendingIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        title=message=type="";

        /*Log.e(TAG, "From: " + remoteMessage.getFrom());*/


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            /*Log.e(TAG, "Message data payload: " + remoteMessage.getData());*/
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            /*Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());*/
        }

        title = remoteMessage.getData().get("title");

        /*Log.e("TITLE",title);*/

        //message will contain the Push Message
        message= remoteMessage.getData().get("body");

        /*Log.e("MESSAGE",message);*/

        type = remoteMessage.getData().get("type");

        /*Log.e("TYPE",type);*/

        if(type.equals("chat_box_msg")){
            /*Log.e("Chat","true");*/
            String username = remoteMessage.getData().get("from_usernam");
            String userimage = remoteMessage.getData().get("from_userimage");
            String userid = remoteMessage.getData().get("from_userid");

            sendNotification(message, title,type,username,userimage,userid);

        }else if(type.equals("post")){
            String postId = remoteMessage.getData().get("respostID");
            sendNotification(message, title,type,postId);
        }else{
            sendNotification(message, title);
        }




        /*//imageUri will contain URL of the image to be displayed with Notification
        imageUri = remoteMessage.getData().get("image");


        //To get a Bitmap image from the URL received
        if(imageUri == null || imageUri.equals("")){

            sendNotification(message,title);

        }else{

            Log.e("IMAGE",imageUri);

            bitmap = getBitmapfromUrl(imageUri);

            sendNotification(message, bitmap,title,imageUri);

        }*/


    }

    private void sendNotification(String messageBody, String title) {

        int notificationId = new Random().nextInt(60000);

        /*Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,PendingIntent.FLAG_ONE_SHOT);*/

        if(Utility.getLogin(this).equals("1")){
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0  , intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }



        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "BEZZY", importance);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotification(String messageBody, String title, String type, String postId) {

        int notificationId = new Random().nextInt(60000);

        /*Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,PendingIntent.FLAG_ONE_SHOT);*/

        if(Utility.getLogin(this).equals("1") && type.equals("post")){
            Intent intent = new Intent(this, PostImageVideoViewActivity.class);
            intent.putExtra("postId",postId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0  , intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }



        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "BEZZY", importance);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }


    private void sendNotification(String messageBody, String title, String type, String username, String userimage, String userid) {

        int notificationId = new Random().nextInt(60000);

        if(!type.equals("chat_box_msg")){
            Log.e("called","1");
            Utility.setNotificationStatus(this,"1");
        }

        if(Utility.getLogin(this).equals("1") && type.equals("chat_box_msg")){
            Log.e("called","2");
            Intent intent = new Intent(this, Massage.class);
            intent.putExtra("FrndId",userid);
            intent.putExtra("userName",username);
            intent.putExtra("userImage",userimage);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }else if(Utility.getLogin(this).equals("1")){
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                /*.setLargeIcon(image)*//*Notification icon image*/
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(messageBody)
                /*.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image))*//*Notification with Image*/
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "BEZZY", importance);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }


    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher_round;
    }
}
