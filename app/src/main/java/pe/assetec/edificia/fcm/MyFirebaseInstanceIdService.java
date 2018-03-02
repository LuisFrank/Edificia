package pe.assetec.edificia.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import pe.assetec.edificia.util.ManageSession;

/**
 * Created by frank on 15/02/18.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    ManageSession session;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e( "MyfirebaseToken", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        session = new ManageSession(getApplication());
        session.StoreFirebaseToken(refreshedToken);

    }


}
