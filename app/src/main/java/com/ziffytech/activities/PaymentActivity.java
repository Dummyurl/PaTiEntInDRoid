package com.ziffytech.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.util.NotifyService;
import com.ziffytech.R;


public class PaymentActivity extends CommonActivity {
    /* URL saved to be loaded after fb login */
    private static final String target_url_prefix="192.168.1.101";
    private Context mContext;
    private WebView mWebview;
    private WebView mWebviewPop;
    private FrameLayout mContainer;
    private long mLastBackPressTime = 0;
    private Toast mToast;
    JSONObject appointmentData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        allowBack();
        setHeaderTitle(getString(R.string.payment_process));
        if (getIntent().hasExtra("order_details")){


            try {
                appointmentData = new JSONObject(getIntent().getExtras().getString("order_details"));


                String messageType = getString(R.string.appoitnment_confirm_message_part1)+appointmentData.getString("id")+ " "+getString(R.string.appoitnment_confirm_message_part1);
                //common.setToastMessage(getString(R.string.appoitnment_confirm_message_part1)+appointmentData.getString("id")+ " "+getString(R.string.appoitnment_confirm_message_part1));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss", Locale.UK);

                    Date testDate = formatter.parse(appointmentData.getString("appointment_date"));
                    Date testTime = formatter2.parse(ActiveModels.SELECTED_SLOT.getSlot());
                    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);


                    Calendar myAlarmDate = Calendar.getInstance();
                    myAlarmDate.setTimeInMillis(System.currentTimeMillis());
                    myAlarmDate.set(testDate.getYear(), testDate.getMonth(), testDate.getDate(), testTime.getHours(), testTime.getMinutes(), 0);

                    Intent _myIntent = new Intent(PaymentActivity.this, NotifyService.class);
                    _myIntent.putExtra("MyMessage",messageType);
                    PendingIntent _myPendingIntent = PendingIntent.getBroadcast(PaymentActivity.this, 123, _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(),_myPendingIntent);

                // final View controlsView =
                // findViewById(R.id.fullscreen_content_controls);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                mWebview = (WebView) findViewById(R.id.webview);
                //mWebviewPop = (WebView) findViewById(R.id.webviewPop);
                mContainer = (FrameLayout) findViewById(R.id.webview_frame);
                WebSettings webSettings = mWebview.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAppCacheEnabled(true);
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                webSettings.setSupportMultipleWindows(true);
                mWebview.setWebViewClient(new UriWebViewClient());
                mWebview.setWebChromeClient(new UriChromeClient());
                mWebview.loadUrl(ApiParams.PAYMENT_URL+"/"+appointmentData.get("id"));

                mContext=this.getApplicationContext();




            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }



        }
    }
    public void success_callback(String url){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        final HashMap<String,String> map = new HashMap<>();
        StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String responseString)
                    {

                        try {
                            JSONObject response = new JSONObject(responseString);
                            if (!response.getString("id").equalsIgnoreCase("") && !response.getString("id").equalsIgnoreCase("null") && response.getString("state").equalsIgnoreCase("approved")){
                                Intent intent = null;

                                intent = new Intent(PaymentActivity.this, ThanksActivity.class);
                                try {
                                    intent.putExtra("date",appointmentData.getString("appointment_date"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                intent.putExtra("timeslot",ActiveModels.SELECTED_SLOT.getSlot());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),response.getString("Error"),Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Some technical issue in payment.",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        if (pDialog!= null) {
                            pDialog.dismiss();

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d("Error",error.getMessage());
                        if (pDialog!= null) {
                            pDialog.dismiss();

                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                return map;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PaymentActivity.this);
        requestQueue.add(strRequest);

    }
    private class UriWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            String host = Uri.parse(url).getHost();
            //Log.d("shouldOverrideUrlLoading", url);
            Pattern p = Pattern.compile(".*paypalsuccess.*");
            Matcher m = p.matcher(url);
            if(m.matches()){
                if(mWebviewPop!=null)
                {
                    mWebviewPop.setVisibility(View.GONE);
                    mContainer.removeView(mWebviewPop);
                    mWebviewPop=null;
                }
                if(mWebview != null){
                    mWebview.setVisibility(View.GONE);
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            success_callback(url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, 3000);

                return false;
            }
            Pattern p2 = Pattern.compile(".*paypalcancel.*");
            Matcher m2 = p2.matcher(url);
            if(m2.matches()){
                Toast.makeText(getApplicationContext(),"Order is canceled",Toast.LENGTH_SHORT).show();
                finish();
                return false;
            }
            if (host.equals(target_url_prefix))
            {
                // This is my web site, so do not override; let my WebView load
                // the page
                if(mWebviewPop!=null)
                {
                    mWebviewPop.setVisibility(View.GONE);
                    mContainer.removeView(mWebviewPop);
                    mWebviewPop=null;
                }
                return false;
            }

            if(host.equals("www.sandbox.paypal.com"))
            {
                return false;
            }else if(host.equals("sandbox.paypal.com"))
            {
                return false;
            }else if(host.equals("www.paypal.com"))
            {
                return false;
            }else if(host.equals("paypal.com"))
            {
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch
            // another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            Log.d("onReceivedSslError", "onReceivedSslError");
            //super.onReceivedSslError(view, handler, error);
        }
    }

    class UriChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            mWebviewPop = new WebView(mContext);
            mWebviewPop.setVerticalScrollBarEnabled(false);
            mWebviewPop.setHorizontalScrollBarEnabled(false);
            mWebviewPop.setWebViewClient(new UriWebViewClient());
            mWebviewPop.getSettings().setJavaScriptEnabled(true);
            mWebviewPop.getSettings().setSavePassword(false);
            mWebviewPop.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mContainer.addView(mWebviewPop);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPop);
            resultMsg.sendToTarget();

            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
            Log.d("onCloseWindow", "called");
        }

    }

}
