package it.geosolutions.savemybike.auth;
 import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.customtabs.CustomTabsIntent;
import android.text.TextUtils;

 import net.openid.appauth.internal.Logger;

 import static net.openid.appauth.Preconditions.checkNotNull;
 public class LogoutService {
     @VisibleForTesting
    Context mContext;
     @NonNull
     private boolean mDisposed = false;

     @VisibleForTesting
    LogoutService(@NonNull Context context) {
        mContext = checkNotNull(context);
    }


     public void performLogoutRequest(
            @NonNull LogoutRequest request,
            @NonNull PendingIntent resultHandlerIntent) {
        checkNotDisposed();
        Uri requestUri = request.toUri();
        PendingLogoutIntentStore.getInstance().addPendingIntent(request, resultHandlerIntent);

        mContext.startActivity(intent);
    }

     private void checkNotDisposed() {
        if (mDisposed) {
            throw new IllegalStateException("Service has been disposed and rendered inoperable");
        }
    }
}