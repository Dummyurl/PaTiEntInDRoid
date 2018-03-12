package com.ziffytech.activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.chat.ChatUserModel;
import com.ziffytech.chat.DataProvider;
import com.ziffytech.chat.ServerUtilities;
import com.ziffytech.chat.UserChatActivity;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.Utils;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mahesh on 08/01/18.
 *
 */

public class ChatActivity extends CommonActivity implements  LoaderManager.LoaderCallbacks<Cursor> {

    private TextView empty;
    private ProgressBar load;
    private SimpleCursorAdapter adapter;

    private TextView tvNotFound;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        allowBack();
        setHeaderTitle("Chat");
        setUpViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setUpViews() {

        empty=(TextView)findViewById(R.id.empty);
        empty.setVisibility(View.GONE);
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ChatActivity.this, MyDoctorsActivity.class);
                startActivity(i);
            }
        });

        load=(ProgressBar)findViewById(R.id.load);
        load.setVisibility(View.GONE);

        FloatingActionButton btnAddChat=(FloatingActionButton)findViewById(R.id.btnAddChat);
        btnAddChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ChatActivity.this,MyDoctorsActivity.class));
            }
        });


      /*  if(MyUtility.isConnected(this)){

           // getChatUsers();
        }
        */

        setUpAdapter();
    }

    private void setUpAdapter() {

        adapter = new SimpleCursorAdapter(this,
                R.layout.chat_module_chat_list_item,
                null,
                new String[]{DataProvider.COL_NAME, DataProvider.COL_COUNT,DataProvider.COL_LAST_MSG,
                        DataProvider.COL_IMAGE,DataProvider.COL_IS_ONLINE,
                        DataProvider.COL_LAST_SEEN,DataProvider.COL_IS_MINE,DataProvider.COL_USER_ID},
                new int[]{R.id.text1,
                        R.id.text2,
                        R.id.textlastmsg,
                        R.id.avatar,
                        R.id.textonline,
                        R.id.textlastseen,
                        R.id.msg_view,
                        R.id.textid},
                0){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {

                final View row = super.getView(position, convertView, parent);

                if (position % 2 == 0) {

                    row.setBackgroundResource(R.drawable.list_background_alternate);

                } else {

                    row.setBackgroundResource(R.drawable.list_background_alternate);

                }
                return row;
            }
        };
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, final Cursor cursor, int columnIndex) {
                switch (view.getId()) {

                    case R.id.text1:

                        String name = cursor.getString(cursor.getColumnIndex(DataProvider.COL_NAME));
                        ((TextView) view).setText(name);

                        empty.setVisibility(View.GONE);

                      /*  String is_group_e = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_GROUP));
                        if (is_group_e.equalsIgnoreCase("no")) {

                            ((EmojiconTextView) view).setText(name);

                        } else {

                            try{
                                byte[] data = Base64.decode(name, Base64.DEFAULT);
                                String name1 = new String(data, "UTF-8");
                                ((EmojiconTextView) view).setText(name1);

                            }catch (Exception e){

                            }

                        }
                        */
                        return true;


                    case R.id.textid:

                        String is_group = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_GROUP));
                        if (is_group.equalsIgnoreCase("no")) {

                            String id = cursor.getString(cursor.getColumnIndex(DataProvider.COL_USER_ID));
                            ((TextView) view).setText(id + "#no");

                        } else {

                            String id = cursor.getString(cursor.getColumnIndex(DataProvider.COL_GROUP_ID));
                            ((TextView) view).setText(id + "#yes");

                        }


                        return true;

                    case R.id.msg_view:


                        String is_mine = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_MINE));

                        if (is_mine.equalsIgnoreCase("no")) {

                            view.setVisibility(View.GONE);

                        } else {

                            view.setVisibility(View.VISIBLE);


                            String is_send = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_SENT));
                            String is_view = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_VIEW));

                            String is_typing = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_TYPING));

                            if (is_typing.equalsIgnoreCase("yes")) {

                                view.setVisibility(View.GONE);

                            } else {

                                if (is_view.equalsIgnoreCase("yes")) {

                                    //green
                                    view.setVisibility(View.VISIBLE);
                                    Picasso.with(ChatActivity.this).load(R.drawable.ic_eye_view_green)
                                            .into((ImageView) view);


                                } else if (is_send.equalsIgnoreCase("yes")) {

                                    //gray image
                                    view.setVisibility(View.VISIBLE);
                                    Picasso.with(ChatActivity.this).load(R.drawable.view_gray)
                                            .into((ImageView) view);

                                } else if(is_view.equalsIgnoreCase("no")) {

                                    view.setVisibility(View.GONE);
                                    Picasso.with(ChatActivity.this).load(R.drawable.ic_eye_view_white)
                                            .into((ImageView) view);

                                }
                            }

                        }

                        return true;

                    case R.id.textlastseen:
                        //String user_id=cursor.getString(cursor.getColumnIndex(DataProvider.COL_USER_ID));
                        //	((TextView)view).setText(user_id);


                     /*   String is_online_text = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_ONLINE));

                        if (is_online_text.equalsIgnoreCase("no")) {

                            String last_seen = cursor.getString(cursor.getColumnIndex(DataProvider.COL_LAST_SEEN));

                            ((TextView) view).setText(Utils.getDate(last_seen));

                            view.setVisibility(View.VISIBLE);

                        } else {

                            view.setVisibility(View.GONE);
                        }
                        */


                        return true;

                    case R.id.textonline:

                        if (MyUtility.isConnected(ChatActivity.this)) {

                        //    String is_online = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_ONLINE));

                         /*   if (is_online.equalsIgnoreCase("no")) {

                                view.setVisibility(View.GONE);


                            } else {

                                view.setVisibility(View.VISIBLE);

                            }
                            */

                        } else {

                            view.setVisibility(View.GONE);

                        }


                        return true;

                    case R.id.avatar:

                        final String image = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IMAGE));
                        final String user_name = cursor.getString(cursor.getColumnIndex(DataProvider.COL_NAME));
                        final String user_id = cursor.getString(cursor.getColumnIndex(DataProvider.COL_USER_ID));
                        final String u_is_group = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_GROUP));
                        final String GROUP_id = cursor.getString(cursor.getColumnIndex(DataProvider.COL_GROUP_ID));


                        Picasso.with(ChatActivity.this).load(ConstValue.BASE_URL + "/uploads/profile/" + image).into((ImageView) view);




                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                             /*   if (u_is_group.equalsIgnoreCase("no")) {

                                    ProfileDialog dialog = new ProfileDialog(getActivity(),
                                            social_type, social_id, image, user_id, user_name, u_is_group);
                                    dialog.show();


                                } else {


                                    ProfileDialog dialog = new ProfileDialog(getActivity(),
                                            social_type, social_id, image, GROUP_id, user_name, u_is_group);
                                    dialog.show();


                                }
                                */

                            }
                        });


                        return true;

                    case R.id.textlastmsg:


                        String last_msg = cursor.getString(columnIndex);



                        String is_typing = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_TYPING));

                        if (is_typing.equalsIgnoreCase("yes")) {

                            // ((TextView)view).setTextColor(getResources().getColor(R.color.green4));
                            ((TextView) view).setText("typing...");
                            ((TextView) view).setTextColor(getResources().getColor(R.color.typing));

                        } else {

                            String is_image = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_IMAGE));
                            String is_stream = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_STREAM_PROFILE));

                            if (is_image.equalsIgnoreCase("yes")) {

                                if(last_msg.contains("explicate") || last_msg.contains("storage")){


                                    ((TextView) view).setText("Image");
                                    ((TextView) view).setTextColor(getResources().getColor(R.color.no_typing));


                                }else{

                                    ((TextView) view).setText(last_msg);
                                    ((TextView) view).setTextColor(getResources().getColor(R.color.no_typing));

                                }


                            } else if (is_stream.equalsIgnoreCase("yes")) {



                                if(last_msg.contains(".mp4")){


                                    ((TextView) view).setText("Stream");
                                    ((TextView) view).setTextColor(getResources().getColor(R.color.no_typing));

                                }else{

                                    ((TextView) view).setText(last_msg);
                                    ((TextView) view).setTextColor(getResources().getColor(R.color.no_typing));

                                }

                            } else {

                                ((TextView) view).setText(last_msg);
                                ((TextView) view).setTextColor(getResources().getColor(R.color.no_typing));


                            }

                        }


                        return true;

                    case R.id.text2:
                        int count = cursor.getInt(columnIndex);
                        if (count > 0) {
                            view.setVisibility(View.VISIBLE);
                            ((TextView) view).setText(String.valueOf(count));
                        } else {

                            view.setVisibility(View.GONE);
                        }
                        return true;
                }
                return false;
            }
        });


       // setListAdapter(adapter);

        ListView listView=(ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {

                    TextView tv=(TextView)v.findViewById((R.id.textid));
                   String[] t= tv.getText().toString().split("#");


                    Intent intent = new Intent(ChatActivity.this, UserChatActivity.class);
                    intent.putExtra(ServerUtilities.AppClass.PROFILE_ID, t[0]);
                    intent.putExtra("is_group","");
                    startActivity(intent);


            }
        });



        getLoaderManager().initLoader(0, null, this);

    }

    private void getChatUsers1() {

        showPrgressBar();

        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",common.get_user_id());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_CHAT_USER_LIST,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);

                            if(userdata!=null){

                                JSONArray jsonArray=userdata.getJSONArray("data");

                                ArrayList<ChatUserModel> data =new ArrayList<>();

                                for(int i=0;i<jsonArray.length();i++){

                                    JSONObject obj=jsonArray.getJSONObject(i);

                                    ChatUserModel model=new ChatUserModel();
                                    model.setId(obj.getString("doct_id"));
                                    model.setName(obj.getString("doct_name"));
                                    model.setEmail(obj.getString("doct_email"));
                                    model.setImage(obj.getString("doct_photo"));
                                   //  model.setIs_online(obj.getString("is_online"));
                                   // model.setLast_seen(obj.getString("modified"));
                                    model.setIs_group("no");
                                    model.setGroup_id("0");

                                    data.add(model);

                                }

                                if(!data.isEmpty()){


                                    addChatUserOrModify(data);
                                }
                            }

                            Log.e("Data",userdata.toString());

                        } catch (JSONException e) {
                            hideProgressBar();
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void VError(String responce) {
                        hideProgressBar();
                    }
                });

    }

    private void addChatUserOrModify(ArrayList<ChatUserModel> data) {

        for (ChatUserModel model : data) {

            if (model.getIs_group().equalsIgnoreCase("no")) {

                Log.e("AT", "Friend");

                Cursor c = getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, model.getId()), null, null, null, null);

                if (c != null && c.getCount() > 0) {

                    Log.e("AT", "Friend:Update");

                    ContentValues values = new ContentValues(8);
                    values.put(DataProvider.COL_NAME, model.getName());
                    values.put(DataProvider.COL_EMAIL, model.getEmail());
                    values.put(DataProvider.COL_IMAGE, model.getImage());
                    values.put("social_id", model.getSocial_id());
                    values.put("social_type", model.getSocial_type());
                    values.put("is_online", model.getIs_online());
                    values.put("is_group", "no");
                    values.put(DataProvider.COL_IS_TYPING, "no");
                    values.put(DataProvider.COL_LAST_SEEN, model.getLast_seen());
                    getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, model.getId()), values, null, null);

                } else {

                    Log.e("AT", "Friend:New");

                    ContentValues values = new ContentValues(11);
                    values.put(DataProvider.COL_NAME, model.getName());
                    values.put(DataProvider.COL_USER_ID, model.getId());
                    values.put(DataProvider.COL_EMAIL, model.getEmail());
                    values.put(DataProvider.COL_IMAGE, model.getImage());
                    values.put(DataProvider.COL_SOCIAL_ID, model.getSocial_id());
                    values.put(DataProvider.COL_SOCIAL_TYPE, model.getSocial_type());
                    values.put("is_view", "no");
                    values.put("is_online", model.getIs_online());
                    values.put("last_seen_time", "");
                    values.put(DataProvider.COL_LAST_MSG, "");
                    values.put(DataProvider.COL_IS_TYPING, "no");
                    values.put("is_group", "no");
                    values.put(DataProvider.COL_LAST_SEEN, model.getLast_seen());
                    getContentResolver().insert(DataProvider.CONTENT_URI_PROFILE, values);

                }

                if (!c.isClosed()) {

                    c.close();
                }


            }
        }

    }


    /**** Cursor Adapter ***/

    //----------------------------------------------------------------------------

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader( this,
                DataProvider.CONTENT_URI_PROFILE,
                new String[]{DataProvider.COL_ID,
                        DataProvider.COL_USER_ID,
                        DataProvider.COL_NAME,
                        DataProvider.COL_COUNT,
                        DataProvider.COL_LAST_MSG,
                        DataProvider.COL_IS_ONLINE,
                        DataProvider.COL_IMAGE,
                        DataProvider.COL_LAST_SEEN,
                        DataProvider.COL_SOCIAL_ID,
                        DataProvider.COL_SOCIAL_TYPE,
                        DataProvider.COL_IS_MINE,
                        DataProvider.COL_IS_SENT,
                        DataProvider.COL_IS_VIEW,
                        DataProvider.COL_IS_TYPING,
                        DataProvider.COL_IS_GROUP,
                        DataProvider.COL_GROUP_ID,
                        DataProvider.COL_IS_IMAGE,
                        DataProvider.COL_IS_STREAM_PROFILE,
                        DataProvider.COL_LAST_MSG_DATE
                },
                DataProvider.COL_GROUP_ID + " = ? ",
                new String[]{common.get_user_id()},
                DataProvider.COL_LAST_MSG_DATE + " DESC");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        if(adapter.getCount()==0){
            empty.setVisibility(View.VISIBLE);
        }else{
            load.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
