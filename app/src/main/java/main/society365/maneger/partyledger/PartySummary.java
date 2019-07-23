package main.society365.maneger.partyledger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import main.society365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static main.society365.maneger.Sales_Register.SalesRegisterActivity.partynameusedfor_partysummary;
import static main.society365.maneger.Sales_Register.SalesRegisterDetailPage.snamesales;
import static main.society365.maneger.partyledger.PartyLedgerDetailPage.sname;

public class PartySummary extends AppCompatActivity {


    RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private RecyclerView.Adapter adapter;
    private List<partydetailmodel> movieList;
    TextView date,partysummaryname,TOtalamount,summaryname;
    //for receipt data vochure type
    TextView BAtotal,Narrationvalue,chequeno,chequedate,bankdate,Narrationvalueforjournal,TOtalamountCash;
    View lineview;


    //whole body visible after data
    RelativeLayout wholebody,grosstotal_layout,bankacc_layout,bankacc_layoutforcash;
    LinearLayout receiptbody,receiptbodyjournal;
    String vtype,vnumber,ledgername,Period;


    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String stallyid;

    //progress bar
    SpotsDialog spotsDialog;
    String dsd ;
    String dsd2 ;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_summary);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext() ,R.color.colorPrimary));

        recyclerView = (RecyclerView)findViewById(R.id.recycleviewsummary);
        movieList = new ArrayList<>();
        adapter = new partySummaryAdapter(getApplicationContext(),movieList);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      /*  dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
*/
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        lineview = (View)findViewById(R.id.lineview);

        date =(TextView)findViewById(R.id.date);
        summaryname = (TextView)findViewById(R.id.summaryname);
        partysummaryname = (TextView)findViewById(R.id.partysummaryname);
        TOtalamount = (TextView)findViewById(R.id.TOtalamount);

        //for receipt data vochure type
        BAtotal = (TextView)findViewById(R.id.BAtotal);
        Narrationvalue  = (TextView)findViewById(R.id.Narrationvalue);
        chequeno = (TextView)findViewById(R.id.chequeno);
        chequedate = (TextView)findViewById(R.id.chequedate);
        bankdate = (TextView)findViewById(R.id.bankdate);
        Narrationvalueforjournal= (TextView)findViewById(R.id.Narrationvalueforjournal);
        TOtalamountCash= (TextView)findViewById(R.id.TOtalamountCash);

        wholebody = (RelativeLayout)findViewById(R.id.wholebody);
        grosstotal_layout= (RelativeLayout)findViewById(R.id.grosstotal_layout);
        bankacc_layout = (RelativeLayout)findViewById(R.id.bankacc_layout);
        bankacc_layoutforcash = (RelativeLayout)findViewById(R.id.bankacc_layoutforcash);
        receiptbody = (LinearLayout)findViewById(R.id.receiptbody);
        receiptbodyjournal = (LinearLayout)findViewById(R.id.receiptbodyjournal);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                wholebody.setVisibility(View.VISIBLE);
            }
        }, 5000);

        Intent intent = getIntent();
        vtype = intent.getStringExtra("vouchertype");
        vnumber = intent.getStringExtra("vouchernumber");
        ledgername = intent.getStringExtra("ledgername");
        Period = intent.getStringExtra("Period");

        Log.d("perioddate",Period+partynameusedfor_partysummary);
        String[] splited = Period.split("-");

        dsd = splited[0];

        dsd2 =splited[1];


        sharedPreferences = getSharedPreferences("main.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid","0" );

        spotsDialog = new SpotsDialog(this,R.style.Custom);
        getData();

    }


    private void getData() {
        spotsDialog.show();
        String name = "Kamat Pratap Waman";



        String url = null;


        try {
            if (ledgername.equals("test"))
            {
                url = "http://150.242.14.196:8012/society/service/Party_ledger_detail_appapi.php/" +
                        "getpartyledgermobiledata?ledger_name=" + URLEncoder.encode(sname, "UTF-8").replaceAll("\\+", "%20") +
                        "&voucher_number=" + vnumber + "&company_id=" + stallyid + "&voucher_type=" + vtype + "&filter_date=" +
                        URLEncoder.encode(dsd, "UTF-8") +
                        "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8") + "&add_prev_bal=" + 0;
        }

        else if (ledgername.equals("from_sales"))
            {
                if(vtype.equals("Bill"))
                {
                    url = "http://150.242.14.196:8012/society/service/Party_ledger_detail_appapi.php/" +
                            "getpartyledgermobiledata?ledger_name=" + URLEncoder.encode(snamesales, "UTF-8").replaceAll("\\+", "%20") +
                            "&voucher_number=" + vnumber + "&company_id=" + stallyid + "&voucher_type=" + vtype + "&filter_date=" +
                            URLEncoder.encode(dsd, "UTF-8") +
                            "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8") + "&add_prev_bal=" + 0;

                }
                else
                {/*
                    url = "http://150.242.14.196:8012/society/service/app/get_sales_details1.php/getpartyledgermobiledata?ledger_name="
                            + URLEncoder.encode(snamesales, "UTF-8").replaceAll("\\+", "%20") +
                            "&voucher_number=" + vnumber + "&company_id=" + stallyid + "&voucher_type=" + vtype + "&filter_date=" +
                            URLEncoder.encode(dsd, "UTF-8") +
                            "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8") + "&add_prev_bal=" + 0;
                    */
                    url = "http://150.242.14.196:8012/society/service/Party_ledger_detail_appapi.php/getpartyledgermobiledata?ledger_name="
                            + URLEncoder.encode(snamesales, "UTF-8").replaceAll("\\+", "%20") +
                            "&voucher_number=" + vnumber + "&company_id=" + stallyid + "&voucher_type=" + vtype + "&filter_date=" +
                            URLEncoder.encode(dsd, "UTF-8") +
                            "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8") + "&add_prev_bal=" + 0;
                }

            }
        else
        {
            url = "http://150.242.14.196:8012/society/service/Party_ledger_detail_appapi.php/" +
                    "getpartyledgermobiledata?ledger_name=" + URLEncoder.encode(ledgername, "UTF-8").replaceAll("\\+", "%20") +
                    "&voucher_number=" + vnumber + "&company_id=" + stallyid + "&voucher_type=" + vtype + "&filter_date=" +
                    URLEncoder.encode(dsd, "UTF-8") +
                    "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8") + "&add_prev_bal=" + 0;

        }

            Log.d("onResponse:summary ", url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest jsonArrayRequest =
                new StringRequest
                        (url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:summary2 ", response);

                                        try {

                                            JSONObject jsonObject= new JSONObject(response);


                                            //toolbar name
                                            getSupportActionBar().setTitle("Voucher no: "+jsonObject.getString("voucher_number"));

                                            date.setText(jsonObject.getString("Period")+" | "+jsonObject.getString("voucher_type"));
                                            partysummaryname.setText(jsonObject.getString("party_name"));

                                            TOtalamount.setText("\u20B9 "+jsonObject.getString("grand_total"));

                                            if(jsonObject.getString("voucher_type").equals("Bill")||jsonObject.getString("voucher_type").equals("Journal"))
                                            {
                                                if(jsonObject.getString("voucher_type").equals("Journal")) {
                                                    summaryname.setText("ENTRIES");
                                                    grosstotal_layout.setVisibility(View.GONE);
                                                    receiptbodyjournal.setVisibility(View.VISIBLE);
                                                    Narrationvalueforjournal.setText(jsonObject.getString("narration"));
                                                }
                                                else
                                                {
                                                    summaryname.setText("SUMMARY");
                                                    receiptbodyjournal.setVisibility(View.GONE);
                                                }
                                                receiptbody.setVisibility(View.GONE);
                                                JSONArray jsonArray1=jsonObject.getJSONArray("final_datas");

                                                Log.d("partyledger2 ", String.valueOf(jsonArray1));

                                                for (int i=0;i<jsonArray1.length();i++)
                                                {
                                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                                    partydetailmodel movie = new partydetailmodel();
                                                    movie.setAmount(jsonObject1.getString("amount"));
                                                    movie.setVoucher_number(jsonObject1.getString("ledger_name"));
                                                    // movie.setYear(jsonObject1.getInt("releaseYear"));

                                                    movieList.add(movie);

                                                }
                                            }
                                            else
                                            {
                                                lineview.setVisibility(View.GONE);
                                                summaryname.setVisibility(View.GONE);
                                                recyclerView.setVisibility(View.GONE);


                                                if(jsonObject.getString("cheque_no").equals("")) {
                                                   /* receiptbody.setVisibility(View.GONE);
                                                    receiptbodyjournal.setVisibility(View.VISIBLE);
                                                    Narrationvalueforjournal.setText(jsonObject.getString("narration"));
*/

                                                            bankacc_layout.setVisibility(View.GONE);
                                                    bankacc_layoutforcash.setVisibility(View.VISIBLE);
                                                    TOtalamountCash.setText("\u20B9 "+jsonObject.getString("grand_total"));
                                                    Narrationvalue.setText(jsonObject.getString("narration"));
                                                }
                                                else {

                                                    bankacc_layoutforcash.setVisibility(View.GONE);
                                                    bankacc_layout.setVisibility(View.VISIBLE);
                                                    BAtotal.setText("\u20B9 " + jsonObject.getString("bank_ac"));
                                                    Narrationvalue.setText(jsonObject.getString("narration"));
                                                    chequeno.setText("Cheque/DD | No:" + jsonObject.getString("cheque_no"));
                                                    chequedate.setText("Chq Date:" + jsonObject.getString("cheque_date"));
                                                    bankdate.setText("Bank Date:" + jsonObject.getString("bank_date"));
                                                }
                                            }

                                            adapter.notifyDataSetChanged();


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            spotsDialog.dismiss();
                                        }

                                        adapter.notifyDataSetChanged();
                                        spotsDialog.dismiss();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley", error.toString());
                                spotsDialog.dismiss();
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
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
