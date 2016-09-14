package com.scdc.csiapp.gcmservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiGCMRequest;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.main.WelcomeActivity;

import java.io.IOException;

/**
 * Created by Pantearz07 on 22/5/2559.
 */
public class GcmRegisterService extends IntentService {
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    private PreferenceData mManager;
    String officialID = "";

    public GcmRegisterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    /**
     * Persist registration to third-party servers.
     * <p>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        mManager = new PreferenceData(this);
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        Log.e(TAG, "Token : " + token + " officialID : " + officialID);
        String username = mManager.getPreferenceData(mManager.KEY_USERNAME);
        String password = mManager.getPreferenceData(mManager.KEY_PASSWORD);
        ApiGCMRequest gcmRequest = new ApiGCMRequest();
        gcmRequest.setUsername(username);
        gcmRequest.setPassword(password);
        gcmRequest.setRegisOfficialID(officialID);
        gcmRequest.setRegistration_id(token);
        //gcmRequest.setApiLogin(WelcomeActivity.login);
        Connect connect = new Connect();
        connect.execute(gcmRequest);
    }

    class Connect extends AsyncTask<ApiGCMRequest, Void, ApiStatus> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ApiStatus doInBackground(ApiGCMRequest... params) {
            return WelcomeActivity.api.saveGCM(params[0]);
        }

        @Override
        protected void onPostExecute(ApiStatus apiStatus) {
            super.onPostExecute(apiStatus);
            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
                Log.d(TAG, apiStatus.getData().getReason());
              //  Toast.makeText(getApplication(), apiStatus.getData().getReason(), Toast.LENGTH_LONG).show();
            } else {
                 //       Toast.makeText(getApplication(), apiStatus.getData().getReason(), Toast.LENGTH_LONG).show();
            }
        }
    }
//    protected void switchPageToSettingIP() {
//        Intent gotoIPSettingActivity = new Intent(getBaseContext(), IPSettingActivity.class);
//
//        getApplication().startActivity(gotoIPSettingActivity);
//
//    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}