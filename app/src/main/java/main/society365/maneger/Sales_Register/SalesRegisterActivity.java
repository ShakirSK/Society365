package main.society365.maneger.Sales_Register;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TabHost;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dmax.dialog.SpotsDialog;

import static main.society365.maneger.Sales_Register.SalesChildActivity.timeperiod1;
import static main.society365.maneger.Sales_Register.SalesChildActivity.timeperiod2;

public class SalesRegisterActivity extends AppCompatActivity {

    private SearchView searchView;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private SalesRegisterActivityAdapter adapter;
    private SalesAdapterforwingwiseSummary adapter_wingwise;
    private List<sales_register_model> movieList;

    String stallyid;
    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    Calendar calendartp1,calendartp2;

    TextView summaryname,totalamountgross;

    int year1,year2;
    int day1,day2;
    String month1,month2;

    //progress bar
    SpotsDialog spotsDialog;

    LinearLayout calander;
    AlertDialog.Builder builder;


    ArrayList<String> partynametype;
    ArrayList<String> partyamount;
    JSONArray jsonArray1;
    String partyledgertype;
    static String ledgertype;
    public static String partynameusedfor_partysummary;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_register);


        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Sales");
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext() ,R.color.colorPrimary));


        sharedPreferences = getSharedPreferences("main.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid","0" );


        Intent intent = getIntent();
        partyledgertype = intent.getStringExtra("partyledgertype");
        ledgertype = intent.getStringExtra("type");


        //calander to choose custom date
        calander = (LinearLayout) findViewById(R.id.calander);
        builder = new AlertDialog.Builder(this);

        totalamountgross =(TextView)findViewById(R.id.totalamountgross);
        summaryname =(TextView)findViewById(R.id.summaryname);

        partynametype =  new ArrayList<>();
        partyamount = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        movieList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);



        spotsDialog = new SpotsDialog(this,R.style.Custom);

        Log.d( "partyledgertype",partyledgertype);
   if(ledgertype.equals("from_sales_account"))
   {
       Log.d("onCreateledgertype",ledgertype);
       getData_Account();
       adapter = new SalesRegisterActivityAdapter(getApplicationContext(),movieList);
       recyclerView.setAdapter(adapter);
   }

   else if (ledgertype.equals("from_sales_wingwise"))
   {
       Log.d("onCreateledgertype",ledgertype);
       getData_wingwise();
       adapter_wingwise = new SalesAdapterforwingwiseSummary(getApplicationContext(),partynametype,partyamount);
       recyclerView.setAdapter(adapter_wingwise);
   }
   else if ( (ledgertype.equals("from_receipt")))
   {

       getSupportActionBar().setTitle("Receipt");
       Log.d("onCreateledgertype",ledgertype);
       getData();
       adapter = new SalesRegisterActivityAdapter(getApplicationContext(),movieList);
       recyclerView.setAdapter(adapter);
   }
   else
   {
       Log.d("onCreateledgertype",ledgertype);
       getData();
       adapter = new SalesRegisterActivityAdapter(getApplicationContext(),movieList);
       recyclerView.setAdapter(adapter);
   }
    }

    private void getData_wingwise()
    {
      /*  final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/

        spotsDialog.show();



        summaryname.setText(timeperiod1+" - "+timeperiod2);
        String url = null;
        try {

            if (partyledgertype.equals("Others")) {
                partyledgertype = "";
            }
            url = "http://150.242.14.196:8012/society/service/app/get_sundrydetails.php?"
                    + "&filter_date=" + URLEncoder.encode(timeperiod1, "UTF-8") +
                    "&filter_date_end=" + URLEncoder.encode(timeperiod2, "UTF-8") + "&company_id=" + stallyid+"&type="+"bill"
                    +"&flat="
                    + URLEncoder.encode(partyledgertype, "UTF-8").replaceAll("\\+", "%20");


            Log.d("onResponse:partyledger ", url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest jsonArrayRequest =
                new StringRequest
                        (url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:party ", response);

                                        try {

                                            JSONObject jsonObject= new JSONObject(response);


                                            JSONArray jsonArray1=jsonObject.getJSONArray("records");

                                            Log.d("partyledger2 ", String.valueOf(jsonArray1));


                                            String jsonString = jsonArray1.toString();
                                            editor.putString("jsonStringSalesforWingWise", jsonString);
                                            editor.apply();

                                            partynametype.clear();
                                            partyamount.clear();



                                            int amountplus=0;
                                            for (int i=0;i<jsonArray1.length();i++)
                                            {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);


                                                amountplus = (int) (amountplus+jsonObject1.getDouble("amount"));
                                                Log.d("amountplus", String.valueOf(amountplus));
                                            /*    sales_register_model.setDate1(timeperiod1);
                                                sales_register_model.setDate2(timeperiod2);
*/
                                                partynametype.add(jsonObject1.getString("ledger_name"));


                                                //  movieList.add(sales_register_model);

                                            }

                                                  /*  for all group*/
                                            Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(partynametype);
                                            partynametype.clear();
                                            partynametype.addAll(primesWithoutDuplicates);
                                            Collections.sort(partynametype, String.CASE_INSENSITIVE_ORDER);
                                            Log.d("partynametype",String.valueOf(partynametype));



                                            String amountall = String.valueOf(amountplus);
                                            totalamountgross.setText("\u20B9"+amountall);


                                            for (int j=0;j<partynametype.size();j++) {


                                                int amountplus2=0;
                                                for (int i = 0; i < jsonArray1.length(); i++) {
                                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                                    if (partynametype.get(j).equals(jsonObject1.getString("ledger_name"))) {


                                                        amountplus2 = (int) (amountplus2 + jsonObject1.getDouble("amount"));


                                                    }
                                                }
                                                partyamount.add(String.valueOf(amountplus2));
                                            }
                                            for (int i=0;i<partynametype.size();i++)
                                            {

                                                if (partynametype.get(i).equals(""))
                                                {
                                                    partynametype.set(i,"Others");
                                                }
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            spotsDialog.dismiss();
                                        }

                                        adapter_wingwise.notifyDataSetChanged();
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
    private void getData() {
    /*    final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/



        summaryname.setText(timeperiod1+" - "+timeperiod2);


       if (ledgertype.equals("from_receipt")) {
           String jsonString = sharedPreferences
                   .getString("jsonStringSalesforallgroup", "0");


           try {
               jsonArray1 = new JSONArray(jsonString);
               Log.d("ResponseHADtestArray", String.valueOf(jsonArray1));


           } catch (JSONException e) {
               e.printStackTrace();
           }

           movieList.clear();

           int amountplus = 0;
           for (int i = 0; i < jsonArray1.length(); i++) {
               JSONObject jsonObject1 = null;
               try {
                   jsonObject1 = jsonArray1.getJSONObject(i);

                   if (partyledgertype.equals("Others")) {
                       partyledgertype = "";
                   }

                   if (partyledgertype.equals(jsonObject1.getString("parent_name_2"))) {
                       sales_register_model sales_register_model = new sales_register_model();

                       if (jsonObject1.getString("name").contains("(")) {
                           String[] arrOfStr = jsonObject1.getString("name").split("\\(");
                           sales_register_model.setName(arrOfStr[0]);
                           partynameusedfor_partysummary=arrOfStr[0];
                           sales_register_model.setAmount(jsonObject1.getString("amount"));
                           sales_register_model.setVn(jsonObject1.getString("voucher_number"));
                           sales_register_model.setDate(jsonObject1.getString("date"));
                           sales_register_model.setType(jsonObject1.getString("type"));
                           if (arrOfStr[1].contains(")")) {
                               String[] arrOfStr2 = arrOfStr[1].split("\\)");
                               sales_register_model.setFlatno(arrOfStr2[0]);
                           }

                       } else {
                           sales_register_model.setName(jsonObject1.getString("name"));
                           partynameusedfor_partysummary = jsonObject1.getString("name");
                           sales_register_model.setAmount(jsonObject1.getString("amount"));
                           sales_register_model.setFlatno(jsonObject1.getString("flat_no"));
                           sales_register_model.setVn(jsonObject1.getString("voucher_number"));
                           sales_register_model.setDate(jsonObject1.getString("date"));
                           sales_register_model.setType(jsonObject1.getString("type"));
                       }
                       amountplus = amountplus + jsonObject1.getInt("amount");
                       Log.d("amountplus", String.valueOf(amountplus));

                       sales_register_model.setDate1(timeperiod1);
                       sales_register_model.setDate2(timeperiod2);


                       movieList.add(sales_register_model);


                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }

           String amountall = String.valueOf(amountplus);
           totalamountgross.setText("\u20B9" + amountall);

       }
       else
           {

               String jsonString = sharedPreferences
                       .getString("jsonStringSalesforWingWise", "0");


               try {
                   jsonArray1 = new JSONArray(jsonString);
                   Log.d("ResponseHADtestArray", String.valueOf(jsonArray1));


               } catch (JSONException e) {
                   e.printStackTrace();
               }

               movieList.clear();

               int amountplus = 0;
               for (int i = 0; i < jsonArray1.length(); i++) {
                   JSONObject jsonObject1 = null;
                   try {
                       jsonObject1 = jsonArray1.getJSONObject(i);

                       if (partyledgertype.equals("Others")) {
                           partyledgertype = "";
                       }

                       if (partyledgertype.equals(jsonObject1.getString("ledger_name"))) {
                           sales_register_model sales_register_model = new sales_register_model();

                           if (jsonObject1.getString("name").contains("(")) {
                               String[] arrOfStr = jsonObject1.getString("name").split("\\(");
                               sales_register_model.setName(arrOfStr[0]);
                               sales_register_model.setAmount(jsonObject1.getString("amount"));
                               if (arrOfStr[1].contains(")")) {
                                   String[] arrOfStr2 = arrOfStr[1].split("\\)");
                                   sales_register_model.setFlatno(arrOfStr2[0]);
                               }

                           } else {
                               sales_register_model.setName(jsonObject1.getString("name"));
                               sales_register_model.setAmount(jsonObject1.getString("amount"));
                               sales_register_model.setFlatno(jsonObject1.getString("flat_no"));

                           }
                           amountplus = amountplus + jsonObject1.getInt("amount");
                           Log.d("amountplus", String.valueOf(amountplus));

                           sales_register_model.setDate1(timeperiod1);
                           sales_register_model.setDate2(timeperiod2);


                           movieList.add(sales_register_model);


                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

               }

               String amountall = String.valueOf(amountplus);
               totalamountgross.setText("\u20B9" + amountall);

       }

    }


    private void getData_Account() {

        spotsDialog.show();
        summaryname.setText(timeperiod1+" - "+timeperiod2);
        String url = null;
        try {
            url = "http://150.242.14.196:8012/society/service/app/get_maindetails.php?"
                    + "&filter_date=" + URLEncoder.encode(timeperiod1, "UTF-8") +
                    "&filter_date_end=" + URLEncoder.encode(timeperiod2, "UTF-8") + "&company_id=" + stallyid+"&type="+"bill"
                    +"&ledger_name="
                    + URLEncoder.encode(partyledgertype, "UTF-8").replaceAll("\\+", "%20");
            Log.d("onResponse:partyledger ", url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest jsonArrayRequest =
                new StringRequest
                        (url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:party ", response);

                                        try {

                                            JSONObject jsonObject= new JSONObject(response);

                                            spotsDialog.dismiss();
                                            JSONArray jsonArray1=jsonObject.getJSONArray("records");

                                            Log.d("partyledger2 ", String.valueOf(jsonArray1));

                                            movieList.clear();

                                            partynametype =  new ArrayList<>();

                                            int amountplus=0;
                                            for (int i=0;i<jsonArray1.length();i++)
                                            {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                                sales_register_model sales_register_model = new sales_register_model();
                                                //some party name as flatno with that only so below code to sepearate it
                                                if(jsonObject1.getString("name").contains("("))
                                                {
                                                    String[] arrOfStr = jsonObject1.getString("name").split("\\(");
                                                    sales_register_model.setName(arrOfStr[0]);
                                                    sales_register_model.setAmount(jsonObject1.getString("amount"));
                                                    if(arrOfStr[1].contains(")"))
                                                    {
                                                        String[] arrOfStr2 = arrOfStr[1].split("\\)");
                                                        sales_register_model.setFlatno(arrOfStr2[0]);
                                                    }

                                                }
                                                else
                                                {
                                                    sales_register_model.setName(jsonObject1.getString("name"));
                                                    sales_register_model.setAmount(jsonObject1.getString("amount"));
                                                    sales_register_model.setFlatno(jsonObject1.getString("flat_no"));

                                                }

                                                amountplus = amountplus+jsonObject1.getInt("amount");
                                                Log.d("amountplus", String.valueOf(amountplus));
                                                sales_register_model.setDate1(timeperiod1);
                                                sales_register_model.setDate2(timeperiod2);


                                                partynametype.add(jsonObject1.getString("parent_name_2"));


                                                movieList.add(sales_register_model);

                                            }

                                                  /*  for all group*/
                                            Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(partynametype);
                                            partynametype.clear();
                                            partynametype.addAll(primesWithoutDuplicates);
                                            Collections.sort(partynametype, String.CASE_INSENSITIVE_ORDER);
                                            Log.d("partynametype",String.valueOf(partynametype));

                                            String amountall = String.valueOf(amountplus);
                                            totalamountgross.setText("\u20B9"+amountall);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.allreportmenu, menu);
        //menuItem = menu.findItem(R.id.search);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView( menu.findItem(R.id.action_search)
        );
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed

                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
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
        //noinspection Simp lifiableIfStatement
        else if (id == R.id.search) {
            //searchbar edittext
            return true;
        }
    /*    else if (id == R.id.action_calender) {
            //info BS
            //    information_bottomsheet();


            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SalesRegisterActivity.this,
                    android.R.layout.simple_selectable_list_item);
            arrayAdapter.add("This Year");
            arrayAdapter.add("Last Year");
            arrayAdapter.add("Custom Date");

            builder.setTitle("Select Time")
                    .setCancelable(true);



            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    if(strName=="Custom Date"){
                        showcalender();
                    }
                    else if(strName=="This Year")
                    {
                        final Calendar calendartp= Calendar.getInstance();
                        //setting calender to custom date

                        timeperiod2=day2+"-"+month2+"-"+calendartp.get(Calendar.YEAR);

                        calendartp.add(Calendar.YEAR, -1);
                        timeperiod1=day1+"-"+month1+"-"+calendartp.get(Calendar.YEAR);
                        getData();
                    }
                    else if(strName=="Last Year")
                    {

                        year1=year1-1;
                        year2=year2-1;


                        timeperiod1=day1+"-"+month1+"-"+year1;


                        timeperiod2=day2+"-"+month2+"-"+year2;

                        getData();

                    }
                    Toast toast=Toast.makeText(getApplicationContext(),strName,Toast.LENGTH_SHORT);
                    toast.show();
                    dialog.dismiss();
                }            });
            builder.show();


        }
*/
        return super.onOptionsItemSelected(item);
    }
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem sharemenu = menu.findItem(R.id.share);
        MenuItem sharegroup = menu.findItem(R.id.group);
        MenuItem action_calender = menu.findItem(R.id.action_calender);

        if (ledgertype.equals("from_sales_wingwise"))
        {
            MenuItem search = menu.findItem(R.id.action_search);
            search.setVisible(false);
        }


        action_calender.setVisible(false);
        sharegroup.setVisible(false);
        sharemenu.setVisible(false);
        return true;
    }

    private void showcalender(){
     /*   ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_date, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();*/

        AlertDialog.Builder builder;
        AlertDialog alertDialog;

        LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_date,
                (ViewGroup) findViewById(R.id.tabhost));

        TabHost tabs = (TabHost) layout.findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec tabpage1 = tabs.newTabSpec("FROM");
        tabpage1.setContent(R.id.ScrollView01);
        tabpage1.setIndicator("FROM");
        TabHost.TabSpec tabpage2 = tabs.newTabSpec("TO");
        tabpage2.setContent(R.id.ScrollView02);
        tabpage2.setIndicator("TO");
        tabs.addTab(tabpage1);
        tabs.addTab(tabpage2);


        final DatePicker datePicker = (DatePicker) layout.findViewById(R.id.datePicker);
/*
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

            }
        });
*/

        final DatePicker datePicker2 = (DatePicker) layout.findViewById(R.id.datePicker2);
        /*datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {


            }
        });*/
        builder = new AlertDialog.Builder(SalesRegisterActivity.this);

        builder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                day1 = datePicker.getDayOfMonth();
                                month1 = String.valueOf(datePicker.getMonth() + 1);
                                year1 = datePicker.getYear();


                                day2 = datePicker2.getDayOfMonth();
                                month2 = String.valueOf(datePicker2.getMonth() + 1);
                                year2 = datePicker2.getYear();


                                timeperiod1=day1+"-"+month1+"-"+year1;


                                timeperiod2=day2+"-"+month2+"-"+year2;


                               // getData();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
       /* builder.setTitle("Dialog with tabs");*/
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.show();

    }


}
