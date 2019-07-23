package main.society365.maneger.Cash_Bank_Balance;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import main.society365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class CashBankBalanceActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String stallyid,amountTo;
    TextView amountcash,amountbank;

    //progress bar
    SpotsDialog spotsDialog;
    TextView dateendandstart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_bank_balance);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cash / Bank Balance");

        amountcash = (TextView)findViewById(R.id.amountcash);
        amountbank = (TextView)findViewById(R.id.amountbank);

        sharedPreferences = getSharedPreferences("main.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid","0" );
        spotsDialog = new SpotsDialog(this,R.style.Custom);

        dateendandstart =(TextView)findViewById(R.id.summaryname);

        String dsd;
        String dsd2;

        final Calendar calendartp= Calendar.getInstance();
        //setting calender to custom date




        dsd2=calendartp.get(Calendar.YEAR)+"-"+"04"+"-"+"01";

        calendartp.add(Calendar.YEAR, -1);
        dsd= calendartp.get(Calendar.YEAR)+"-"+"03"+"-"+"31";

        dateendandstart.setText(dsd+" - "+dsd2);
        Log.d("datedate",dsd+" "+dsd2);

        String url=null,url2 = null;
        try {
            url = "http://150.242.14.196:8012/society/service/app/test_cb1.php?company_id="
                    + stallyid+ "&filter_date=" +
                    URLEncoder.encode(dsd, "UTF-8") +
                    "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8");

            url2 = "http://150.242.14.196:8012/society/service/app/test_cb2.php?company_id="
                    + stallyid+ "&filter_date=" +
                    URLEncoder.encode(dsd, "UTF-8") +
                    "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        spotsDialog.show();
        Log.d("onResponse:summary2 ", url);

        StringRequest jsonArrayRequest =
                new StringRequest
                        (url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:summary2 ", response);

                                        try {
                                            spotsDialog.dismiss();
                                            JSONObject jsonObject= new JSONObject(response);
                                            amountTo=jsonObject.getString("sub_total_amount");
                                            amountcash.setText(amountTo);
                                            //      Toast.makeText(getContext(),amountTo,Toast.LENGTH_SHORT).show();


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            spotsDialog.dismiss();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                spotsDialog.dismiss();
                                Log.e("Volley", error.toString());
                            }
                        });
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);

        StringRequest jsonArrayRequest2 =
                new StringRequest
                        (url2,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:summary2 ", response);

                                        try {

                                            JSONObject jsonObject= new JSONObject(response);
                                            amountTo=jsonObject.getString("sub_total_amount");
                                            amountbank.setText(amountTo);
                                            //      Toast.makeText(getContext(),amountTo,Toast.LENGTH_SHORT).show();


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley", error.toString());
                            }
                        });
        jsonArrayRequest2.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(jsonArrayRequest2);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id ==  android.R.id.home) {
            onBackPressed();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

}
