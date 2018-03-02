package pe.assetec.edificia.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;

import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import me.leolin.shortcutbadger.ShortcutBadger;
import pe.assetec.edificia.MainActivity;
import pe.assetec.edificia.R;
import pe.assetec.edificia.notification.MyNotificationManager;
import pe.assetec.edificia.util.Constant;
import pe.assetec.edificia.util.ManageSession;

/**
 * Created by frank on 15/02/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    ManageSession session;
    DatabaseReference databaseRoot,databaseCompany,databasePending,databaseUsers;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("Myfirebaseservice","FROM:" +  remoteMessage.getFrom());

        session = new ManageSession(getApplication());

        String collapse_key = remoteMessage.getCollapseKey();

        String titleData = "";
        String bodyData = "";
        String CompanyData = "";

        if (remoteMessage.getData().size() > 0) {
            titleData =remoteMessage.getData().get("title");
            bodyData =remoteMessage.getData().get("body");
            CompanyData =remoteMessage.getData().get("company");

        }

        if (session.isUserLogedIn() == true) {
            sendNotificationTwo(titleData, bodyData,collapse_key);
            int badgeCount = 1;
            int total = session.getCountBadge();
            session.StoreCountBadge(total+=badgeCount);
            ShortcutBadger.applyCount(getApplicationContext(), total); //for 1.1.4+

        }else{
            sendNotificationTwo(titleData, bodyData,collapse_key);
//            databasePending = databaseRoot.child("pendings");
//            databaseCompany = databasePending.child(company_name);
//            databaseUsers = databaseCompany.child("users");


        }
    }

    private void sendNotificationTwo(String title, String messageBody,String collapseKey) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("collapse", collapseKey);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title )
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constant.CHANNEL_ID,
                    "Configurar",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}
