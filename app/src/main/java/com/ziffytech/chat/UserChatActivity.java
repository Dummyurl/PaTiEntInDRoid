package com.ziffytech.chat;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.RoundedImageView;
import com.ziffytech.util.Utils;

import java.io.IOException;

import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

/**
 * Created by Mahesh on 08/01/18.
 */

public class UserChatActivity extends CommonActivity  implements
        MessageList.OnFragmentInteractionListener, View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>
{

    private EmojiconEditText msgEdit;
    private ImageButton sendBtn,smile_btn;
    private String profileName,is_group;
    private String profileId,profilePhoto,profileSocialId,profileSocialType,image_path;
    boolean hidden = true;
    private static final int REQUEST_CAMERA = 100;
    private static final int SELECT_FILE = 200;
    private ProgressBar bar;
    private int theme_postion;
    private EmojiconsPopup popup;
    private RelativeLayout parent;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.chat);

        allowBack();

        parent=(RelativeLayout)findViewById(R.id.parent);
        parent.setOnClickListener(this);


        profileId = getIntent().getStringExtra(ServerUtilities.AppClass.PROFILE_ID);



        ContentValues cv = new ContentValues(1);
        cv.put(DataProvider.COL_COUNT, 0);
        getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, profileId), cv, null, null);


        Utils.cancelNotification(this,007);

        msgEdit = (EmojiconEditText) findViewById(R.id.msg_edit);
        sendBtn = (ImageButton) findViewById(R.id.send_btn);
        smile_btn=(ImageButton)findViewById(R.id.smile_btn);
        smile_btn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);


        registerReceiver(registrationStatusReceiver, new IntentFilter(ServerUtilities.AppClass.ACTION_REGISTER));

        Cursor c= getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, profileId), null, null, null, null);


        if (c.moveToFirst()) {

            profileName = c.getString(c.getColumnIndex(DataProvider.COL_NAME));
            profileId = c.getString(c.getColumnIndex(DataProvider.COL_USER_ID));
            profilePhoto = c.getString(c.getColumnIndex(DataProvider.COL_IMAGE));
            profileSocialId = c.getString(c.getColumnIndex(DataProvider.COL_SOCIAL_ID));
            profileSocialType = c.getString(c.getColumnIndex(DataProvider.COL_SOCIAL_TYPE));


            setHeaderTitle(Utils.makeUppperLetter(profileName));



            SharedPreferences sharedpreferences=getSharedPreferences("my_pref",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(ApiParams.CHAT_WITH, profileId);
            editor.commit();


        }else{

            finish();
        }

        if(!c.isClosed()){

            c.close();
        }

        /*

        msgEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("Change", "Before");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (MyUtility.isConnected(UserChatActivity.this)) {

                    if (count == 0) {

                        sendTypingNo(sharedpreferences.getString(userId, ""), profileId);

                    } else {

                        if (count == 3 || count==7 ) {

                            sendTyping(sharedpreferences.getString(userId, ""), profileId);

                        }
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.e("Change", "After");

            }
        });

        */


        getSupportLoaderManager().initLoader(0,null,this);





        View view= parent;

        // Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
        popup = new EmojiconsPopup(view, this);
        popup.setSizeForSoftKeyboard();
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                changeEmojiKeyboardIcon(smile_btn, R.drawable.my_chat_s);
            }
        });

        //If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if (popup.isShowing())
                    popup.dismiss();
            }
        });

        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (msgEdit == null || emojicon == null) {
                    return;
                }

                int start = msgEdit.getSelectionStart();
                int end = msgEdit.getSelectionEnd();
                if (start < 0) {
                    msgEdit.append(emojicon.getEmoji());
                } else {
                    msgEdit.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });

        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {

                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                msgEdit.dispatchKeyEvent(event);

            }
        });


        smile_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //If popup is not showing => emoji keyboard is not visible, we need to show it
                if (!popup.isShowing()) {

                    //If keyboard is visible, simply show the emoji popup
                    if (popup.isKeyBoardOpen()) {
                        popup.showAtBottom();
                        changeEmojiKeyboardIcon(smile_btn, R.drawable.keyboard);
                    }

                    //else, open the text keyboard first and immediately after that show the emoji popup
                    else {
                        msgEdit.setFocusableInTouchMode(true);
                        msgEdit.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(msgEdit, InputMethodManager.SHOW_IMPLICIT);
                        changeEmojiKeyboardIcon(smile_btn, R.drawable.keyboard);
                    }
                }

                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else {
                    popup.dismiss();
                }
            }
        });



        msgEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                openKeyboard();

            }
        });

        msgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                popup.dismiss();
            }
        });


    }
    public void openKeyboard() {

        msgEdit.setFocusableInTouchMode(true);
        msgEdit.requestFocus();
    }
    public void onClick(View v) {
        switch(v.getId()) {




            case R.id.send_btn:


                //	if(CheckConnection.isConnected(UserChatActivity.this)) {




                if(!msgEdit.getText().toString().equals("")){

                    //	Utils.hideKeyboard(v,UserChatActivity.this);

                    int msg_id=(int)(Math.random()*9000)+1000;
                     String msg_final_id=common.get_user_id()+profileId+String.valueOf(msg_id);
                    Log.e("Msg_id", msg_final_id);

                    ContentValues values = new ContentValues(6);
                    values.put(DataProvider.COL_MSG, msgEdit.getText().toString());
                    values.put(DataProvider.COL_MSG_ID,msg_final_id);
                    values.put(DataProvider.COL_IS_IMAGE,"no");
                    values.put(DataProvider.COL_IS_UPLOAD,"yes");
                    values.put(DataProvider.COL_TO, profileId);
                    values.put(DataProvider.COL_AT, Utils.getCurrentDate());

                    getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);

                    send(msgEdit.getText().toString(), msg_final_id);

                    ContentValues values1 = new ContentValues(6);
                    values1.put(DataProvider.COL_LAST_MSG, msgEdit.getText().toString());
                    values1.put(DataProvider.COL_IS_MINE,"yes");
                    values1.put(DataProvider.COL_IS_TYPING,"no");
                    values1.put(DataProvider.COL_IS_VIEW,"no");
                    values1.put(DataProvider.COL_IS_SENT,"no");
                    values1.put(DataProvider.COL_LAST_MSG_DATE,Utils.getCurrentDate());
                    getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, profileId), values1, null, null);
                    msgEdit.setText(null);

                    MyUtility.hideKeyboard(msgEdit,this);

                }


                //	}else{


                //		Utils.showToast(Utils.INTERNET_ERROR,UserChatActivity.this);
                //	}




            break;




        }
    }

    @Override
    public String getProfileEmail() {
        return profileId;
    }
    private void sendTyping(final String user_id, final String friend_id) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";

                try {

                    ServerUtilities.sendFriendTyping(user_id, friend_id);

                } catch (IOException ex) {
                    msg = "Message could not be sent";
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }
    private void sendTypingNo(final String user_id, final String friend_id) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";

                try {

                    ServerUtilities.sendFriendTypingNo(user_id, friend_id);

                } catch (IOException ex) {
                    msg = "Message could not be sent";
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }
    private void send(final String txt, final String msg_id) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";

                try {

                    ServerUtilities.send(txt, profileId, common.get_user_id(), msg_id);

                } catch (IOException ex) {
                    msg = "Message could not be sent";
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }
    private void sendStream(final String txt, final String msg_id, final String stream_id, final String stream_link) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";

                try {

                    ServerUtilities.sendStream(txt,
                            profileId,
                            common.get_user_id(),
                            msg_id, stream_id, stream_link
                    );

                } catch (IOException ex) {
                    msg = "Message could not be sent";
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(registrationStatusReceiver);
        super.onDestroy();
    }

    //--------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {


            onBackPressed();

        }

        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this,
                DataProvider.CONTENT_URI_PROFILE,
                new String[]{
                        DataProvider.COL_USER_ID,
                        DataProvider.COL_IS_TYPING,
                },
                DataProvider.COL_USER_ID  + " = ? " ,
                new String[]{profileId},
                DataProvider.COL_LAST_MSG_DATE + " DESC");


        return loader;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        if (data.moveToFirst()) {


          /*  String is_typing = data.getString(data.getColumnIndex(DataProvider.COL_IS_TYPING));

            if(is_typing.equalsIgnoreCase("yes")){

                title.setText("Typing...");
                title.setBackground(getResources().getDrawable(R.drawable.bg_date));
                title.setTextColor(getResources().getColor(R.color.typing));

            }else{

                title.setBackground(null);
                title.setText(Utils.makeUppperLetter(profileName));
                title.setTextColor(getResources().getColor(R.color.black_dark));
            }

            */

        }



    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    

    /****BROADCAST *****************/

    private BroadcastReceiver registrationStatusReceiver = new  BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {


            try{
                DataProvider data=new DataProvider();
                data.deleteNotificationFriend(UserChatActivity.this, profileId);

            }catch (Exception e){

            }

            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String id=intent.getStringExtra("id");

            if(id!=null){

                ContentValues cv = new ContentValues(1);
                cv.put(DataProvider.COL_COUNT, 0);
                getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, profileId), cv, null, null);
                Utils.cancelNotification(UserChatActivity.this, 007);
            }
        }

    };


    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
        iconToBeChanged.setImageResource(drawableResourceId);
    }

}
