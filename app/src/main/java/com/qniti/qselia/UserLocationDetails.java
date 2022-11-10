package com.qniti.qselia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserLocationDetails extends AppCompatActivity {

    String staffID,name,phone,email,logid,enterDate,enterTime,ratingDisplay,remarkDisplay,userID,undername,underphone,underemail;
    TextView userNametxt,userEmailtxt,phoneNumtxt,logIDtxt,enterdateTxt,entertimeTxt,remarkTxt,noratingTv,underNameTxt,underphoneTxt,underemailTxt;
    ScaleRatingBar ratingBarMain;
    String ratingMsg;
    int rating = 0;
    ScaleRatingBar ratingBar;
    EditText ratingMsgEditText;
    Button giveReviewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location_details);

        userNametxt = findViewById(R.id.userNametxt);
        userEmailtxt = findViewById(R.id.userEmailtxt);
        phoneNumtxt = findViewById(R.id.phoneNumtxt);
        logIDtxt = findViewById(R.id.logIDtxt);
        enterdateTxt = findViewById(R.id.enterdateTxt);
        entertimeTxt = findViewById(R.id.entertimeTxt);
        remarkTxt = findViewById(R.id.remarkTxt);
        noratingTv = findViewById(R.id.noratingTv);
        underNameTxt = findViewById(R.id.underNameTxt);
        underphoneTxt = findViewById(R.id.underphoneTxt);
        underemailTxt = findViewById(R.id.underemailTxt);
        ratingBarMain = findViewById(R.id.ratingBarList);
        giveReviewBtn = findViewById(R.id.giveReviewBtn);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        staffID = sharedPreferences.getString(Config.USER_ID2, "0");
        name = sharedPreferences.getString(Config.NAME_ID2, "0");
        phone= sharedPreferences.getString(Config.PHONE_ID2, "0");
        email = sharedPreferences.getString(Config.EMAIL_ID2, "0");
        logid = sharedPreferences.getString(Config.LOG_ID2, "New Entry");
        enterDate = sharedPreferences.getString(Config.SCAN_DATE, "0");
        enterTime = sharedPreferences.getString(Config.SCAN_TIME, "0");
        ratingDisplay = sharedPreferences.getString(Config.LOG_STATUS, "null");
        userID = sharedPreferences.getString(Config.PLACE_ID, "0");
        remarkDisplay = sharedPreferences.getString(Config.REMARK, "0");

        userNametxt.setText(capitalize(name));
        userEmailtxt.setText(email);
        phoneNumtxt.setText(phone);
        logIDtxt.setText(logid);
        enterdateTxt.setText(enterDate);
        entertimeTxt.setText(enterTime);
        remarkTxt.setText(remarkDisplay);


        if (!("null".equalsIgnoreCase(ratingDisplay))){

            loadUnder();
            noratingTv.setVisibility(View.GONE);
            ratingBarMain.setVisibility(View.VISIBLE);
            ratingBarMain.setRating(Float.valueOf(ratingDisplay));
            giveReviewBtn.setVisibility(View.GONE);

        }else {

            loadUnder();
            noratingTv.setVisibility(View.VISIBLE);
            ratingBarMain.setVisibility(View.GONE);
            giveReviewBtn.setVisibility(View.VISIBLE);


        }

        giveReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UserLocationDetails.this);
                //  builder.setTitle("Knowledge Base");
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        rating = (int) ratingBar.getRating();
                        ratingMsg = ratingMsgEditText.getText().toString().trim();

                          Toast.makeText(UserLocationDetails.this,String.valueOf(rating),Toast.LENGTH_LONG).show();


                        sendRating();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.rating_popup, null);

                ratingBar = dialogLayout.findViewById(R.id.ratingBar);
                ratingMsgEditText = dialogLayout.findViewById(R.id.editTextMsgPopup);


                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.show();

            }
        });
    }

    public void loadUnder(){

        final ProgressDialog loading = ProgressDialog.show(this,"Please Wait","Contacting Server",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.URL_API+"loadunder.php?underID="+userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject user = array.getJSONObject(i);

                                //adding the product to product list
                                undername = user.getString("username");
                                underphone = user.getString("userphone");
                                underemail = user.getString("useremail");
                            }

                            underNameTxt.setText(capitalize(undername));
                            underphoneTxt.setText(underphone);
                            underemailTxt.setText(underemail);

                            loading.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(UserLocationDetails.this,"No internet . Please check your connection",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{

                            Toast.makeText(UserLocationDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding our stringrequest to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public void sendRating(){

        final ProgressDialog loading = ProgressDialog.show(UserLocationDetails.this,"Please Wait","Contacting Server",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.URL_API+"enterlog.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loading.dismiss();

                if(response.contains("Success")){

                    Toast.makeText(UserLocationDetails.this, "Successfully review", Toast.LENGTH_LONG)
                            .show();

                    Intent intent = new Intent(UserLocationDetails.this, PastVisited.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }
                else  {

                    Toast.makeText(UserLocationDetails.this, "Error. Please try again", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(UserLocationDetails.this,"No internet . Please check your connection",
                            Toast.LENGTH_LONG).show();
                }
                else{

                    Toast.makeText(UserLocationDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("staffID", staffID);
                params.put("userID", userID);
                params.put("rating", String.valueOf(rating));
                params.put("remark", ratingMsg);

                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }
    public void onBackPressed() {
        Intent i = new Intent(UserLocationDetails.this, PastVisited.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z-éá])([a-z-éá]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
}
