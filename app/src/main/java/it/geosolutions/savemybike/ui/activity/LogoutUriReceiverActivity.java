package net.openid.appauth;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.openid.appauth.internal.Logger;

import it.geosolutions.savemybike.auth.LogoutRequest;
import it.geosolutions.savemybike.auth.PendingLogoutIntentStore;

/**
 * Created by emanuel.couto on 22/04/2016.
 */
public class LogoutUriReceiverActivity extends AppCompatActivity {
    private static final String KEY_STATE = "state";
    private Clock mClock = SystemClock.INSTANCE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        LogoutRequest request = PendingLogoutIntentStore.getInstance().getOriginalRequest();
        PendingIntent target = PendingLogoutIntentStore.getInstance().getPendingIntent();

        Logger.debug("Forwarding redirect");
        try {
            target.send(this, 0, null);
        } catch (PendingIntent.CanceledException e) {
            Logger.errorWithStack(e, "Unable to send pending intent");
        }
        finish();
    }
}