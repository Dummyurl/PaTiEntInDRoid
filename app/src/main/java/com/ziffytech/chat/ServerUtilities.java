/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ziffytech.chat;


import android.util.Log;


import com.ziffytech.Config.ConstValue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


/**
 * Helper class used to communicate with the demo server.
 */


public final class ServerUtilities {


    public static class AppClass{

        public static final String PROFILE_ID = "profile_id";
        public static final String ACTION_REGISTER = "com.ziffytech.REGISTER";
        public static final String FROM = "email";
        public static final String REG_ID = "regId";
        public static final String MSG = "msg";
        public static final String TO = "email2";

        public static String getServerUrl() {
            return ConstValue.BASE_URL+"Chat/send_gcm";
        }

    }
	
	private static final String TAG = "ServerUtilities";

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();


    /**
     * Register this account/device pair within the server.
     */
    public static void register(final String email, final String regId) {
        //Log.i(TAG, "registering device (regId = " + regId + ")");
        String serverUrl = AppClass.getServerUrl() + "/register";
        Map<String, String> params = new HashMap<String, String>();
        params.put(AppClass.FROM, email);
        params.put(AppClass.REG_ID, regId);
        // Once GCM returns a registration id, we need to register it in the
        // demo server. As the server might be down, we will retry it a couple
        // times.
        try {
        	post(serverUrl, params, MAX_ATTEMPTS);
        } catch (IOException e) {
        }
    }

    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(final String email) {
        //Log.i(TAG, "unregistering device (email = " + email + ")");
        String serverUrl = AppClass.getServerUrl() + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put(AppClass.FROM, email);
        try {
            post(serverUrl, params, MAX_ATTEMPTS);
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
        }
    }
    
    /**
     * Send a message.
     */
    public static void send(String msg, String to,String from,String msg_id) throws IOException {
        Log.e(TAG, "to:"+to +" from: "+from);

        String serverUrl = AppClass.getServerUrl() + "/send_gcm";
        Map<String, String> params = new HashMap<String, String>();
        params.put("msg", msg);
        params.put("user_id", from);
        params.put("friend_id", to);
        params.put("msg_id", msg_id);
        post(serverUrl, params, MAX_ATTEMPTS);
    }

    public static void sendStream(String msg, String to,String from,String msg_id,String stream_id,String stream_link) throws IOException {
        //Log.i(TAG, "sending message (msg = " + msg + ")");
        String serverUrl = AppClass.getServerUrl() + "/send_gcm_with_stream";
        Map<String, String> params = new HashMap<String, String>();
        params.put("msg", msg);
        params.put("user_id", from);
        params.put("friend_id", to);
        params.put("msg_id", msg_id);
        params.put("stream_id", stream_id);
        params.put("stream_link", stream_link);
        post(serverUrl, params, MAX_ATTEMPTS);

    }


    public static void sendFriendTyping(String from, String to) throws IOException {
        //Log.i(TAG, "sending message (msg = " + msg + ")");
        String serverUrl = AppClass.getServerUrl() + "/is_typing";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", from);
        params.put("friend_id", to);
        post(serverUrl, params, MAX_ATTEMPTS);
    }

    public static void sendFriendTypingNo(String from, String to) throws IOException {
        //Log.i(TAG, "sending message (msg = " + msg + ")");
        String serverUrl = AppClass.getServerUrl() + "/is_typing_no";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", from);
        params.put("friend_id", to);
        post(serverUrl, params, MAX_ATTEMPTS);
    }

    public static void sendGroupTyping(String msg, String to,String from) throws IOException {
        //Log.i(TAG, "sending message (msg = " + msg + ")");
        String serverUrl = AppClass.getServerUrl() + "/send_group_typing_gcm";
        Map<String, String> params = new HashMap<String, String>();
        params.put("msg", msg);
        params.put("group_id", to);
        params.put("user_id", from);
        post(serverUrl, params, MAX_ATTEMPTS);
    }


    public static void sendGroup(String msg, String to,String from,String msg_id) throws IOException {
        Log.i(TAG, "sending message (msg = " + msg + ")");
        String serverUrl = AppClass.getServerUrl() + "/send_group_gcm";
        Map<String, String> params = new HashMap<String, String>();
        params.put("msg", msg);
        params.put("user_id", from);
        params.put("group_id", to);
        params.put("msg_id", msg_id);

        post(serverUrl, params, MAX_ATTEMPTS);
    }


    public static void sendStreamGroup(String msg, String to,String from,String msg_id,String stream_id,String stream_link) throws IOException {
        //Log.i(TAG, "sending message (msg = " + msg + ")");
        String serverUrl = AppClass.getServerUrl() + "/send_group_gcm_with_stream";
        Map<String, String> params = new HashMap<String, String>();
        params.put("msg", msg);
        params.put("user_id", from);
        params.put("group_id", to);
        params.put("msg_id", msg_id);
        params.put("stream_id", stream_id);
        params.put("stream_link", stream_link);
        post(serverUrl, params, MAX_ATTEMPTS);

    }

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */
    private static void post(String endpoint, Map<String, String> params) throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        //Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
              throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }
    
    /** Issue a POST with exponential backoff */
    private static void post(String endpoint, Map<String, String> params, int maxAttempts) throws IOException {
    	long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
    	for (int i = 1; i <= maxAttempts; i++) {
    		//Log.d(TAG, "Attempt #" + i);
    		try {
    			post(endpoint, params);
    			return;
    		} catch (IOException e) {
    			//Log.e(TAG, "Failed on attempt " + i + ":" + e);
    			if (i == maxAttempts) {
    				throw e;
                }
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                    return;
                }
                backoff *= 2;    			
    		} catch (IllegalArgumentException e) {
    			throw new IOException(e.getMessage(), e);
    		}
    	}
    }
}