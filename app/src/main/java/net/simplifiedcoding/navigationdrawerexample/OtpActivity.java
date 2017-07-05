package net.simplifiedcoding.navigationdrawerexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyLog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import static android.Manifest.permission.READ_CONTACTS;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.app.AlertDialog;

import com.android.volley.Response;
import com.android.volley.VolleyLog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import static android.Manifest.permission.READ_CONTACTS;

import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.app.ProgressDialog;
import android.view.ViewGroup;
import android.widget.Toast;


public class OtpActivity extends AppCompatActivity {

    public static final String TAG = OtpActivity.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static OtpActivity mInstance;

    // UI references.
    private EditText mOtpNumber;
    private View mProgressView;
    private View mMobileLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        String data_otp=getIntent().getStringExtra("PASS_MOBILE");
/*
        TextView txtInput2 = (TextView)findViewById(R.id.login_label);
        txtInput2.setText("Hello, "+ data_otp);
*/
        ImageButton mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                sendLogin();

            }
        });

        Button mReSendButton = (Button) findViewById(R.id.resendOtp);
        mReSendButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                resendOtp();

            }
        });

        mMobileLoginFormView = findViewById(R.id.mobile_otp_form);
        mProgressView = findViewById(R.id.otp_progress);
    }

     /*
    volley functions
     */

    public static synchronized OtpActivity getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    /*
    volley functions end
     */


    private void sendLogin() {

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        //loader
        ProgressDialog loading = null;

        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage("Verifying OTP");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        loading.show();

        mOtpNumber = (EditText) findViewById(R.id.enter_otp);
        String otpNumber= mOtpNumber.getText().toString();

        final AlertDialog.Builder alertDialogBuilder123 = new AlertDialog.Builder(this);



        final String mobileNumber=getIntent().getStringExtra("PASS_MOBILE");
        RequestQueue queue = Volley.newRequestQueue(this);  // this = context

        final String url = "http://docref.in/api/doctors/sign_on.php?mobile_number=" + mobileNumber;

// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());

                        String jsonStr=response.toString();
                        if (jsonStr != null) {
                            try {
                                JSONObject jsonObj = new JSONObject(jsonStr);

                                /*
                                // Getting JSON Array node
                                JSONArray contacts = jsonObj.getJSONArray("contacts");

                                // looping through All Contacts
                                for (int i = 0; i < contacts.length(); i++) {
                                    JSONObject c = contacts.getJSONObject(i);

                                    String id = c.getString("id");
                                    String name = c.getString("name");
                                }
                                */
                                String json_response = jsonObj.getString("response");

                                String response_status = "true";
                                if(response_status.equals(json_response)) {
                                    /*
                                    alertDialogBuilder123.setMessage("click performed "+response.toString());
                                    AlertDialog alertDialog = alertDialogBuilder123.create();
                                    alertDialog.show();
*/
                                    Intent intent_home = new Intent(OtpActivity.this, MainActivity.class);
                                    intent_home.putExtra("PASS_MOBILE", mobileNumber);
                                    startActivity(intent_home);
                                }
                                else{
                                    alertDialogBuilder123.setMessage("Something Wents Wrong");
                                    AlertDialog alertDialog = alertDialogBuilder123.create();
                                    alertDialog.show();
                                }

                            } catch (final JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + e.getMessage(),
                                                Toast.LENGTH_LONG)
                                                .show();
                                    }
                                });

                            }
                        }




                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

// add it to the RequestQueue

        if(otpNumber.equals(getIntent().getStringExtra("PASS_OTP").toString())) {

            queue.add(getRequest);
        }
        else{
            alertDialogBuilder123.setMessage("Entered OTP Not Correct");
            AlertDialog alertDialog = alertDialogBuilder123.create();
            alertDialog.show();
        }
        loading.dismiss();
        showProgress(false);


    }

    // resend otp
    private void resendOtp() {

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        //loader
        ProgressDialog loading = null;

        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage("Resending OTP");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        loading.show();

        final String mobileNumber=getIntent().getStringExtra("PASS_MOBILE");
        RequestQueue queue = Volley.newRequestQueue(this);  // this = context

        final String url = "http://docref.in/api/doctors/send_otp.php?mobile_number=" + mobileNumber;

        final AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(this);

// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
/*
                        alertDialogBuilder2.setMessage("else condition "+response.toString()+mMobileNumber.getText().toString());
                        AlertDialog alertDialog = alertDialogBuilder2.create();
                        alertDialog.show();
*/

                        String jsonStr=response.toString();
                        if (jsonStr != null) {
                            try {
                                JSONObject jsonObj = new JSONObject(jsonStr);

                                /*
                                // Getting JSON Array node
                                JSONArray contacts = jsonObj.getJSONArray("contacts");

                                // looping through All Contacts
                                for (int i = 0; i < contacts.length(); i++) {
                                    JSONObject c = contacts.getJSONObject(i);

                                    String id = c.getString("id");
                                    String name = c.getString("name");
                                }
                                */
                                String json_response = jsonObj.getString("response");
                                String json_mobile = jsonObj.getString("mobile");
                                String json_otp = jsonObj.getString("otp");
                                String response_status = "true";
                                if(response_status.equals(json_response)) {
                                    Intent intent_home = new Intent(OtpActivity.this, OtpActivity.class);
                                    intent_home.putExtra("PASS_MOBILE", json_mobile);
                                    intent_home.putExtra("PASS_OTP", json_otp);
                                    startActivity(intent_home);
                                }
                                else{
                                    alertDialogBuilder2.setMessage("Something Wents Wrong");
                                    AlertDialog alertDialog = alertDialogBuilder2.create();
                                    alertDialog.show();
                                }

                            } catch (final JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + e.getMessage(),
                                                Toast.LENGTH_LONG)
                                                .show();
                                    }
                                });

                            }
                        }



                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

// add it to the RequestQueue
        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matcher = pattern.matcher(mobileNumber);
        if (matcher.matches()) {
            queue.add(getRequest);
        }
        else{
            alertDialogBuilder2.setMessage("Mobile Number Not Correct");
            AlertDialog alertDialog = alertDialogBuilder2.create();
            alertDialog.show();
        }



        loading.dismiss();
        showProgress(false);

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mMobileLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mMobileLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mMobileLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mMobileLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}