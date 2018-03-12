package com.ziffytech.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.Preferences;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import com.ziffytech.R;


public class LoginActivity extends CommonActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    EditText editEmail, editPassword;
    SignInButton signInButton;
    Button fb, gp;
    LoginButton loginButton;
    GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private SharedPreferences sharedPreferences;
    private String social_name,
            social_email,
            social_id,
            social_image,
            social_phone,
            social_type,
            login_by;
    RelativeLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);
        layout=(RelativeLayout)findViewById(R.id.activity_login);
        hideKeyboard(layout,LoginActivity.this);
        setHeaderTitle(getString(R.string.login_now));
        editEmail = (EditText) findViewById(R.id.txtEmail);
        editPassword = (EditText) findViewById(R.id.txtPassword);
        Button button = (Button) findViewById(R.id.button);
        Button forgot=(Button)findViewById(R.id.button4);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(intent);
            }
        });


        sharedPreferences = getSharedPreferences(Preferences.MyPREFERENCES, Context.MODE_PRIVATE);


        callbackManager = CallbackManager.Factory.create();


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        setUpViews();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    private void setUpViews() {

        loginButton = (LoginButton) findViewById(R.id.login_button);

        signInButton = (SignInButton) findViewById(R.id.g_login);
        signInButton.setOnClickListener(this);
        GoogleSignInOptions signinOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signinOption).build();


        loginButton.setReadPermissions("user_friends");
        //  loginButton.setFrag
        //loginButton.registerCallback(callbackManager, callback);

        fb = (Button) findViewById(R.id.fb);
        gp = (Button) findViewById(R.id.gp);

        fb.setOnClickListener(this);
        gp.setOnClickListener(this);
        facebook();


    }

    private void facebook() {

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse response) {

                                try{



                                    social_id=jsonObject.getString("id");
                                    social_email =jsonObject.getString("email");
                                    social_name = jsonObject.getString("name");
                                    social_type = "2";
                                    login_by = "Facebook";
                                    LoginManager.getInstance().logOut();

                                    SocialLogin();

                                }catch (Exception e){

                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code

            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "error to Login Facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("AT", "onActivityResult");


        if (requestCode == REQ_CODE) {

            Log.e("AT", "google");


            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);

        } else {

            callbackManager.onActivityResult(requestCode, resultCode, data);

        }


    }


    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.g_login:
               // signIn();
                break;

         /*   case R.id.logout:
                SignOut();
                break;*/

            case R.id.fb:

                loginButton.performClick();

                break;

            case R.id.gp:

                signIn();

                break;
        }
    }

    private void signIn() {


        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void SignOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
               // UpdateUI(false);
            }
        });


    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount account = result.getSignInAccount();


       /*     String emaill = account.getEmail();
            String namee = account.getDisplayName();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Preferences.Email, emaill);
            editor.putString(Preferences.Name, namee);
            editor.commit();*/
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                }
            });

            if (result!=null){
                social_id=account.getId();
                social_email = account.getEmail();
                social_name = account.getDisplayName();
                social_type = "1";
                login_by = "Gmail";
                SocialLogin();

            }else {
                Toast.makeText(this, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
          //  UpdateUI(true);
        } else {
           // UpdateUI(false);
            Toast.makeText(this, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
        }
    }

/*
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            loginButton.setReadPermissions(Arrays.asList("email,user_birthday,phone,name"));

            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            //displayMessage(profile);

            Log.e("AT", "onSuccess");

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code

                            JSONObject c = response.getJSONObject();

                            Log.e("AT", c.toString());


                            try {

                                social_id = c.getString("id");
                                social_name = c.getString("name");
                                social_email = c.getString("email");
                                social_type = "2";
                                social_phone = c.getString("phone");

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                            login_by = "Facebook";


                             SocialLogin();


                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender");
            request.setParameters(parameters);
            request.executeAsync();


        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };*/

    public void login() {

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
           // common.setToastMessage(getString(R.string.valid_required_email));
            editEmail.setError(getString(R.string.valid_required_email));
            editEmail.setFocusable(true);

            return;

           // cancel = true;
        }
        if (!isValidEmail(email)) {
            editEmail.setError(getString(R.string.valid_email));
            editEmail.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editPassword.setError(getString(R.string.valid_required_password));
            editPassword.setFocusable(true);
            return;
        }

        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }

            showPrgressBar();
            HashMap<String, String> params = new HashMap<>();
            params.put("user_email", email);
            params.put("user_password", password);

            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.LOGIN_URL, params,
                    new VJsonRequest.VJsonResponce() {
                        @Override
                        public void VResponce(String responce) {

                            hideProgressBar();

                            JSONObject userdata = null;
                            try {
                                userdata = new JSONObject(responce);


                                if(userdata.getInt("responce")==1){

                                    JSONObject data=userdata.getJSONArray("data").getJSONObject(0);

                                    common.setSession(ApiParams.COMMON_KEY, data.getString("user_id"));
                                    common.setSession(ApiParams.USER_EMAIL, data.getString("user_email"));
                                    common.setSession(ApiParams.USER_FULLNAME, data.getString("user_fullname"));
                                    common.setSession(ApiParams.USER_PHONE, data.getString("user_phone"));
                                    common.setSession(ApiParams.USER_JSON_DATA, data.toString());

                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);


                                }else{
                                    MyUtility.showAlertMessage(LoginActivity.this,"Invalid Credentials");
                                }

                            } catch (JSONException e) {
                                hideProgressBar();
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void VError(String responce) {

                            hideProgressBar();
                            common.setToastMessage(responce);
                        }
                    });

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void registerClick(View v) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);

    }


    private void SocialLogin() {


        Log.e("Data",social_name+"/"+social_email+"/"+social_id+"/"+social_type);


        showPrgressBar();

        HashMap<String, String> params = new HashMap<>();

        params.put("user_fullname", social_name);
        params.put("user_email", social_email);
        params.put("social_id", social_id);
        params.put("social_type", social_type);
        params.put("user_phone", "");

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.SOCIAL_REGISTER1, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);


                            if(userdata.getInt("success")==1){

                                JSONObject data=userdata.getJSONObject("data");
                                common.setSession(ApiParams.COMMON_KEY, data.getString("user_id"));
                                common.setSession(ApiParams.USER_EMAIL, data.getString("user_email"));
                                common.setSession(ApiParams.USER_FULLNAME, data.getString("user_fullname"));
                                common.setSession(ApiParams.USER_PHONE, data.getString("user_phone"));
                                common.setSession(ApiParams.USER_JSON_DATA, data.toString());

                                //1 means new 0 means old
                                if(userdata.getInt("is_new")==1){

                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, PersonalDetailsActivity.class);
                                    intent.putExtra("new","new");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);


                                }else if (userdata.getInt("is_new")==0){


                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }



                            }else{
                                MyUtility.showAlertMessage(LoginActivity.this,"Failed to sign-in.");
                            }

                        } catch (JSONException e) {
                            hideProgressBar();
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void VError(String responce) {

                        hideProgressBar();
                        common.setToastMessage(responce);
                    }
                });

    }


    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
