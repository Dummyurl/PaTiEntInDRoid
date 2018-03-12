package com.ziffytech.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.Utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Mahesh on 08/01/18.
 */

public class MessageList   extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {



    private SimpleCursorAdapter adapter;
    private View rootView;
    private ListView msg_list;
    private SharedPreferences sharedpreferences;
    private int rowCount = 0;
    String[] items = null;

    private OnFragmentInteractionListener mListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.msg_fragment_list, container, false);


        sharedpreferences=getActivity().getSharedPreferences("my_pref",Context.MODE_PRIVATE);

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(coonectionRegister, intentFilter);


        msg_list = (ListView) rootView.findViewById(R.id.msglistitem);
        msg_list.setAdapter(adapter);

        msg_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              //  ((ChatActivity) getActivity()).closeRevel();
              //  Utils.hideKeyboard(view, getActivity());

            }
        });

        return rootView;


    }

    private void scroll(){
        msg_list.setSelection(rowCount - 1);
        msg_list.setDivider(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.my_new_chat_message_view,
                null,
                new String[]{
                        DataProvider.COL_FROM,
                        DataProvider.COL_FROM,
                        DataProvider.COL_FROM,
                        DataProvider.COL_FROM,
                        DataProvider.COL_MSG,
                        DataProvider.COL_MSG,
                        DataProvider.COL_AT,
                        DataProvider.COL_AT,
                        DataProvider.COL_AT,

                },
                new int[]{
                        R.id.chat_layout_friend,
                        R.id.chat_layout_me,
                        R.id.user_profile_image,
                        R.id.friend_profile_image,
                        R.id.chat_msg_friend,
                        R.id.chat_msg_me,
                        R.id.chat_msg_time_friend,
                        R.id.chat_msg_time_me,
                        R.id.chat_date,

                },
                0){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {

                final View row = super.getView(position, convertView, parent);

             /*   if (position == selectedItem) {

                    row.setBackgroundResource(R.drawable.selected_transparent);

                }else{

                    row.setBackgroundResource(0);
                }
                */

                return row;
            }
        };
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(final View view, Cursor cursor, int columnIndex) {

                switch (view.getId()) {

                    case R.id.chat_layout_friend:

                        if (cursor.getString(columnIndex) != null) {
                            // view friend layout
                            view.setVisibility(View.VISIBLE);

                        } else {

                            view.setVisibility(View.GONE);
                        }
                        return true;

                    case R.id.chat_layout_me:


                        if (cursor.getString(columnIndex) == null) {

                            view.setVisibility(View.VISIBLE);

                        } else {

                            view.setVisibility(View.GONE);
                        }
                        return true;

                    case R.id.user_profile_image:


                        if (cursor.getString(columnIndex) == null) {

                            String prevDate = "";

                            // get previous item's date, for comparison
                            if (cursor.getPosition() > 0 && cursor.moveToPrevious()) {
                                prevDate = cursor.getString(columnIndex);
                                cursor.moveToNext();
                            }

                            if (prevDate == null) {


                                view.setVisibility(View.INVISIBLE);

                            } else {

                                view.setVisibility(View.VISIBLE);

                              /*  String image = sharedpreferences.getString(Image, "");

                                Picasso.with(getActivity())
                                        .load(image.toString())
                                        .placeholder(R.drawable.user_placeholder)
                                        .error(R.drawable.user_placeholder)
                                        .resize(100, 100)
                                        .centerCrop()
                                        .into((RoundedImageView) view);
                                        */


                            }


                        }

                        return true;


                    case R.id.friend_profile_image:


                        if (cursor.getString(columnIndex) != null) {
                            // view friend layout

                            String prevDate = null;

                            // get previous item's date, for comparison
                            if (cursor.getPosition() > 0 && cursor.moveToPrevious()) {
                                prevDate = cursor.getString(columnIndex);
                                cursor.moveToNext();
                            }


                            if (prevDate != null) {


                                view.setVisibility(View.INVISIBLE);

                            } else {

                                view.setVisibility(View.VISIBLE);


                                Cursor c = getActivity().getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, sharedpreferences.getString(ApiParams.CHAT_WITH, "")), null, null, null, null);

                                if (c.moveToFirst()) {

                                    String profilePhoto = c.getString(c.getColumnIndex(DataProvider.COL_IMAGE));
                                    String profileSocialId = c.getString(c.getColumnIndex(DataProvider.COL_SOCIAL_ID));
                                    String profileSocialType = c.getString(c.getColumnIndex(DataProvider.COL_SOCIAL_TYPE));


                                /*    Picasso.with(getActivity())
                                            .load(profilePhoto)
                                            .placeholder(R.drawable.user_placeholder)
                                            .error(R.drawable.user_placeholder)
                                            .resize(100, 100)
                                            .centerCrop()
                                            .into((RoundedImageView) view);

                                            */

                                }

                                if (!c.isClosed()) {
                                    c.close();
                                }

                            }


                        }

                        return true;

                    case R.id.chat_msg_me:

                        String from = cursor.getString(cursor.getColumnIndex(DataProvider.COL_FROM));

                        if (from == null) {

                            String is_image1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_IMAGE));
                            String is_stream1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_STREAM));


                            LinearLayout root = (LinearLayout) view.getParent().getParent();

                            if (is_image1.equalsIgnoreCase("yes")) {


                                String is_send1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_SENT));
                                String is_view1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_VIEW));


                                root.getChildAt(0).setVisibility(View.GONE);
                                root.getChildAt(1).setVisibility(View.VISIBLE);
                                root.getChildAt(2).setVisibility(View.GONE);

                                if (is_view1.equalsIgnoreCase("yes")) {

                                    root.setBackground(getResources().getDrawable(R.drawable.bg_msg_from));

                                } else if (is_send1.equalsIgnoreCase("yes")) {

                                    root.setBackground(getResources().getDrawable(R.drawable.bg_msg_from));

                                } else {

                                    root.setBackground(getResources().getDrawable(R.drawable.bg_msg_you));
                                }

                                final String msg1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_MSG));
                                final String msg_id1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_MSG_ID));


                                RelativeLayout lay = (RelativeLayout) root.getChildAt(1);
                                ImageView img = (ImageView) lay.getChildAt(0);

                                if (msg1.contains("http")) {

                                    Picasso.with(getActivity()).load(msg1).fit().centerCrop().into(img);


                                } else {


                                    final Uri uri = Uri.fromFile(new File(msg1));
                                    Picasso.with(getActivity()).load(uri).fit().centerCrop().into(img);

                                }

                             /*   img.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {

                                        popupdialog(msg_id1, "image", "");

                                        return true;
                                    }
                                }); */

                                img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                     /*   Intent i = new Intent(getActivity(), ImageViewerActivity.class);
                                        i.putExtra("url", msg1);
                                        i.putExtra("local", "local");
                                        startActivity(i);

                                        */

                                    }
                                });

                                final String is_upload = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_UPLOAD));
                                ProgressBar bar1 = (ProgressBar) lay.getChildAt(1);


                                if (is_upload.equalsIgnoreCase("no")) {

                                    final String friend_id = cursor.getString(cursor.getColumnIndex(DataProvider.COL_TO));

                                    bar1.setVisibility(View.VISIBLE);

                                    if (MyUtility.isConnected(getActivity())) {


                                      //  new UploadFileToServer(bar1, msg_id1, msg1, friend_id)
                                       //         .execute();

                                    }


                                } else {

                                    bar1.setVisibility(View.GONE);


                                }


                            } else if (is_stream1.equalsIgnoreCase("yes")) {

                                String is_send1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_SENT));
                                String is_view1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_VIEW));

                                root.getChildAt(0).setVisibility(View.GONE);
                                root.getChildAt(1).setVisibility(View.GONE);
                                root.getChildAt(2).setVisibility(View.VISIBLE);

                                if (is_view1.equalsIgnoreCase("yes")) {

                                    root.setBackground(getResources().getDrawable(R.drawable.bg_msg_from));

                                } else if (is_send1.equalsIgnoreCase("yes")) {

                                    root.setBackground(getResources().getDrawable(R.drawable.bg_msg_from));

                                } else {

                                    root.setBackground(getResources().getDrawable(R.drawable.bg_msg_you));
                                }

                                final String msg1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_MSG));
                                final String msg_id1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_MSG_ID));

                                final String stream_id = cursor.getString(cursor.getColumnIndex(DataProvider.COL_STREAM_ID));
                                final String stream_link = cursor.getString(cursor.getColumnIndex(DataProvider.COL_STREAM_LINK));

                                RelativeLayout lay = (RelativeLayout) root.getChildAt(2);
                                ImageView img = (ImageView) lay.getChildAt(0);

                                Picasso.with(getActivity()).load(msg1).fit().centerCrop().into(img);

                                img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                      /*  Intent i = new Intent(getActivity(), PopularStreamPlayer.class);
                                        i.putExtra("id", stream_id);
                                        i.putExtra("link", stream_link);
                                        startActivity(i);
                                        */
                                    }
                                });

                               /* img.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {

                                        popupdialog(msg_id1, "stream", "");

                                        return true;
                                    }
                                });  */


                            } else {

                                root.getChildAt(0).setVisibility(View.VISIBLE);
                                root.getChildAt(1).setVisibility(View.GONE);
                                root.getChildAt(2).setVisibility(View.GONE);

                                TextView tv1 = ((TextView) view);

                                final String msg1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_MSG));
                                String is_send1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_SENT));
                                String is_view1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_VIEW));


                                if (is_view1.equalsIgnoreCase("yes")) {

                                    root.setBackground(getResources().getDrawable(R.drawable.bg_msg_from));

                                } else if (is_send1.equalsIgnoreCase("yes")) {

                                    root.setBackground(getResources().getDrawable(R.drawable.bg_msg_from));

                                } else {

                                    root.setBackground(getResources().getDrawable(R.drawable.bg_msg_you));
                                }

                                tv1.setText(msg1);


                            }


                        }


                        return true;

                    case R.id.chat_msg_friend:

                        String from1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_FROM));

                        if (from1 != null) {

                            String is_image = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_IMAGE));
                            String is_stream = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_STREAM));


                            LinearLayout root_f = (LinearLayout) view.getParent().getParent();
                            if (is_image.equalsIgnoreCase("yes")) {

                                root_f.getChildAt(0).setVisibility(View.GONE);
                                root_f.getChildAt(1).setVisibility(View.VISIBLE);
                                root_f.getChildAt(2).setVisibility(View.GONE);

                                final String msg1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_MSG));
                                final String msg_id1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_MSG_ID));

                                RelativeLayout lay = (RelativeLayout) root_f.getChildAt(1);
                                ImageView img = (ImageView) lay.getChildAt(0);
                                final ProgressBar bar = (ProgressBar) lay.getChildAt(1);

                                Picasso.with(getActivity()).load(msg1).resize(200,200).centerCrop().into(img, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                        bar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {

                                        bar.setVisibility(View.GONE);
                                    }
                                });

                                img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                     /*   Intent i = new Intent(getActivity(), ImageViewerActivity.class);
                                        i.putExtra("url", msg1);
                                        i.putExtra("chat","chat");
                                        startActivity(i);

                                        */

                                    }
                                });

                              /*  img.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {

                                        popupdialog(msg_id1, "image", "");

                                        return true;
                                    }
                                });  */


                                String is_view1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_VIEW));
                                if (is_view1.equalsIgnoreCase("no")) {

                                    if (MyUtility.isConnected(getActivity())) {

                                       /* MsgViewSeenAck sent = new MsgViewSeenAck();
                                        sent.setResultListener(new MsgViewSeenAck.ResultListner() {
                                            @Override
                                            public void onSuccess(String value) {


                                                try {


                                                    //  ContentValues values = new ContentValues(1);
                                                    //  values.put(DataProvider.COL_IS_VIEW, "yes");
                                                    //  getActivity().getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_MESSAGES, msg_id1), values, null, null);

                                                    DataProvider db1 = new DataProvider();
                                                    db1.updateChatMsgVIEWMe(getActivity(), msg_id1);


                                                } catch (Exception e) {

                                                    e.printStackTrace();

                                                }

                                            }

                                            @Override
                                            public void onFailed() {


                                            }
                                        });
                                        sent.execute(msg_id1);

                                        */

                                    }
                                }

                            } else if (is_stream.equalsIgnoreCase("yes")) {

                                root_f.getChildAt(0).setVisibility(View.GONE);
                                root_f.getChildAt(1).setVisibility(View.GONE);
                                root_f.getChildAt(2).setVisibility(View.VISIBLE);

                                final String msg1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_MSG));
                                final String msg_id1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_MSG_ID));
                                final String stream_id = cursor.getString(cursor.getColumnIndex(DataProvider.COL_STREAM_ID));
                                final String stream_link = cursor.getString(cursor.getColumnIndex(DataProvider.COL_STREAM_LINK));

                                RelativeLayout lay = (RelativeLayout) root_f.getChildAt(2);
                                ImageView img = (ImageView) lay.getChildAt(0);

                                Picasso.with(getActivity()).load(msg1).fit().centerCrop().into(img);

                                img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                       /* Intent i = new Intent(getActivity(), PopularStreamPlayer.class);
                                        i.putExtra("id", stream_id);
                                        i.putExtra("link", stream_link);
                                        i.putExtra("msg_id", msg_id1);
                                        startActivity(i);
                                        */
                                    }
                                });

                              /*  img.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {

                                        // popupdialog(msg_id1, "stream", "");

                                        return true;
                                    }
                                });  */


                                String is_view1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_VIEW));
                                if (is_view1.equalsIgnoreCase("no")) {

                                    if (MyUtility.isConnected(getActivity())) {

                                      /*  MsgViewSeenAck sent = new MsgViewSeenAck();
                                        sent.setResultListener(new MsgViewSeenAck.ResultListner() {
                                            @Override
                                            public void onSuccess(String value) {

                                                try {


                                                    //  ContentValues values = new ContentValues(1);
                                                    //  values.put(DataProvider.COL_IS_VIEW, "yes");
                                                    //  getActivity().getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_MESSAGES, msg_id1), values, null, null);

                                                    DataProvider db1 = new DataProvider();
                                                    db1.updateChatMsgVIEWMe(getActivity(), msg_id1);


                                                } catch (Exception e) {


                                                }

                                            }

                                            @Override
                                            public void onFailed() {


                                            }
                                        });
                                        sent.execute(msg_id1);
                                        */

                                    }
                                }


                            } else {

                                root_f.getChildAt(0).setVisibility(View.VISIBLE);
                                root_f.getChildAt(1).setVisibility(View.GONE);
                                root_f.getChildAt(2).setVisibility(View.GONE);

                                TextView tv1 = ((TextView) view);

                                final String msg1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_MSG));

                                tv1.setText(msg1);

                                final String msg_id1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_MSG_ID));
                                String is_view1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_VIEW));
                                if (is_view1.equalsIgnoreCase("no")) {

                                    if (MyUtility.isConnected(getActivity())) {

                                      /*  MsgViewSeenAck sent = new MsgViewSeenAck();
                                        sent.setResultListener(new MsgViewSeenAck.ResultListner() {
                                            @Override
                                            public void onSuccess(String value) {

                                                try {


                                                    try {


                                                        //  ContentValues values = new ContentValues(1);
                                                        //  values.put(DataProvider.COL_IS_VIEW, "yes");
                                                        //  getActivity().getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_MESSAGES, msg_id1), values, null, null);

                                                        DataProvider db1 = new DataProvider();
                                                        db1.updateChatMsgVIEWMe(getActivity(), msg_id1);


                                                    } catch (Exception e) {


                                                    }

                                                } catch (Exception e) {


                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void onFailed() {


                                            }
                                        });
                                        sent.execute(msg_id1);

                                        */

                                    }
                                }

                            }

                        }


                        return true;


                    case R.id.chat_msg_time_me:

                        String from2 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_FROM));

                        if (from2 == null) {


                            String is_viewed = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IS_VIEW));


                            if (is_viewed.equalsIgnoreCase("yes")) {


                                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_eye_view_white, 0, 0, 0);

                            } else {


                                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            }


                            if (MyUtility.isConnected(getActivity())) {


                                ((TextView) view).setText(Utils.getTime(cursor.getString(columnIndex)));


                            } else {

                                ((TextView) view).setText("");
                            }


                        }


                        return true;

                    case R.id.chat_msg_time_friend:

                        ((TextView) view).setText(Utils.getTime(cursor.getString(columnIndex)));

                        return true;

                    case R.id.chat_date:


                        String thisDate = cursor.getString(columnIndex);
                        String prevDate = "";

                        // get previous item's date, for comparison
                        if (cursor.getPosition() > 0 && cursor.moveToPrevious()) {
                            prevDate = cursor.getString(columnIndex);
                            cursor.moveToNext();
                        }

                        Date prev = Utils.getDateFromString(prevDate);
                        Date curr = Utils.getDateFromString(thisDate);

                        if (prev == null || !prev.equals(curr)) {

                            view.setVisibility(View.VISIBLE);
                            ((TextView) view).setText(Utils.getChatDateHeader(thisDate));
                        } else {

                            view.setVisibility(View.GONE);
                        }

                        return true;
                }
                return false;
            }
        });


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);

        if(rowCount!=0){

            scroll();

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        String getProfileEmail();
    }
    private String getDisplayTime(String datetime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date testDate = null;
        try {
            testDate = sdf.parse(datetime);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("KK:mm aa");
        String newFormat = sdf1.format(testDate);

        return newFormat;
    }

    //----------------------------------------------------------------------------
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //	String profileEmail = args.getString(DataProvider.COL_EMAIL);
        CursorLoader loader = new CursorLoader(getActivity(),
                DataProvider.CONTENT_URI_MESSAGES,
                new String[]{DataProvider.COL_IS_SENT,
                        DataProvider.COL_IS_VIEW,
                        DataProvider.COL_MSG_ID,
                        DataProvider.COL_MSG,
                        DataProvider.COL_AT,
                        DataProvider.COL_ID,
                        DataProvider.COL_FROM,
                        DataProvider.COL_IS_UPLOAD,
                        DataProvider.COL_IS_STREAM,
                        DataProvider.COL_TO,
                        DataProvider.COL_IS_IMAGE,
                        DataProvider.COL_LAST_SEEN_TIME,
                        DataProvider.COL_STREAM_ID,
                        DataProvider.COL_STREAM_LINK,
                },
                DataProvider.COL_FROM + " = ? or " + DataProvider.COL_TO + " = ?",
                new String[]{sharedpreferences.getString(ApiParams.CHAT_WITH,""),sharedpreferences.getString(ApiParams.CHAT_WITH,"")},
                DataProvider.COL_AT + " ASC");


        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);
        rowCount=data.getCount();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private BroadcastReceiver coonectionRegister = new  BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            adapter.notifyDataSetChanged();

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(coonectionRegister);
    }


}
