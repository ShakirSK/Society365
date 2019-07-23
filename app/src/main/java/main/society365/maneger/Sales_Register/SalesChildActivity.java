package main.society365.maneger.Sales_Register;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import main.society365.R;
import main.society365.maneger.Reciept.RecieptActivityAdapter;

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

public class SalesChildActivity extends AppCompatActivity {

    int year1,year2;
    int day1,day2;
    String month1,month2;

    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private SalesChildActivityAdapter adapter;
    String snamesales,amount;
    String date1,date2;
    TextView dateendandstart,totalamountgross,totalsa,totalsd;
    private List<sales_register_model> movieList;
    //progress bar
    SpotsDialog spotsDialog;


    private SearchView searchView;

    String stallyid;
    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    static String timeperiod1,timeperiod2;

    Calendar calendartp1,calendartp2;

    TextView summaryname;

    LinearLayout calander;
    AlertDialog.Builder builder;


    ArrayList<String> partynametype;
    ArrayList<String> partyamount;
    JSONArray jsonArray1;
   static String type ;
   LinearLayout salesaccount,sundry;

   //recycleview for sales account
   RecyclerView recyclerView2;
    private LinearLayoutManager linearLayoutManager2;
    private DividerItemDecoration dividerItemDecoration2;
    private RecieptActivityAdapter adapter2;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_child);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext() ,R.color.colorPrimary));

       // dateendandstart =(TextView)findViewById(R.id.summaryname);
      //  totalamountgross =(TextView)findViewById(R.id.totalamountgross);


        totalsa =(TextView)findViewById(R.id.totalsa);
        totalsd =(TextView)findViewById(R.id.totalsd);

        spotsDialog = new SpotsDialog(this,R.style.Custom);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        sundry = (LinearLayout)findViewById(R.id.sundry);
        salesaccount = (LinearLayout)findViewById(R.id.salesaccount);

        recyclerView2 = (RecyclerView)findViewById(R.id.recycleview2);

        //toolbar name
        if(type.equals("from_sales"))
        {
            getSupportActionBar().setTitle("Sales");
            sundry.setVisibility(View.VISIBLE);
           salesaccount.setVisibility(View.VISIBLE);
           recyclerView2.setVisibility(View.VISIBLE);

        }
        else
        {
            getSupportActionBar().setTitle("Receipt");
            sundry.setVisibility(View.GONE);
            salesaccount.setVisibility(View.GONE);
        }

       // dateendandstart.setText(date1+" - "+date2);
      //  totalamountgross.setText("3456");


        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        /*movieList = new ArrayList<>();*/
        partynametype =  new ArrayList<>();
        partyamount = new ArrayList<>();
        adapter = new SalesChildActivityAdapter(getApplicationContext(),partynametype,partyamount);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        day1 = 1;
        day2 = 31;
        month1 = "4";
        month2 = "3";
        year1 =2018;
        year2 =2019;


        sharedPreferences = getSharedPreferences("main.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid","0" );


        monthstring();

        timeperiod1=day1+" "+month1+" "+year1;

        timeperiod2=day2+" "+month2+" "+year2;


        getData();

        //recyclerView2.setNestedScrollingEnabled(false);
        movieList = new ArrayList<>();

        linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());

        dividerItemDecoration2 = new DividerItemDecoration(recyclerView2.getContext(), linearLayoutManager2.getOrientation());

        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        recyclerView2.addItemDecoration(dividerItemDecoration2);

        getDatasales_Account();

    }

    private void getData() {
      /*  final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/

        spotsDialog.show();




       /* calendartp1 = Calendar.getInstance();
        //setting calender to custom date
        calendartp1.set(2018, 1, 1);

        //tp1 date
        timeperiod1=(calendartp1.get(Calendar.MONTH)) +
                "-" + calendartp1.get(Calendar.DATE) + "-" + calendartp1.get(Calendar.YEAR);


        calendartp2 = Calendar.getInstance();
        //setting calender to custom date
        calendartp2.set(2019, 31, 1);


        //tp2 date and add year by plus 1

        timeperiod2=(calendartp2.get(Calendar.MONTH)) +
                "-" + calendartp2.get(Calendar.DATE) + "-" + calendartp2.get(Calendar.YEAR);*/


        totalamountgross =(TextView)findViewById(R.id.totalamountgross);
        summaryname =(TextView)findViewById(R.id.summaryname);
        summaryname.setText(timeperiod1+" - "+timeperiod2);
        String url = null;
        try {
            if(type.equals("from_receipt"))
            {
                url = "http://150.242.14.196:8012/society/service/app/get_salesregister.php?" +
                        "filter_date="+ URLEncoder.encode(timeperiod1, "UTF-8")+
                        "&filter_date_end="+URLEncoder.encode(timeperiod2, "UTF-8")
                        +"&company_id="+stallyid+"&type="+"receipt";
            }
            else {
                url = "http://150.242.14.196:8012/society/service/app/get_salesregister.php?" +
                        "filter_date="+ URLEncoder.encode(timeperiod1, "UTF-8")+
                        "&filter_date_end="+URLEncoder.encode(timeperiod2, "UTF-8")
                        +"&company_id="+stallyid+"&type="+"bill";
            }

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

                                            spotsDialog.dismiss();
                                            JSONObject jsonObject= new JSONObject(response);


                                            JSONArray jsonArray1=jsonObject.getJSONArray("records");

                                            Log.d("partyledger2 ", String.valueOf(jsonArray1));


                                            String jsonString = jsonArray1.toString();
                                            editor.putString("jsonStringSalesforallgroup", jsonString);
                                            editor.apply();

                                            partynametype.clear();
                                            partyamount.clear();



                                            int amountplus=0;
                                            for (int i=0;i<jsonArray1.length();i++)
                                            {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                              //  sales_register_model sales_register_model = new sales_register_model();
                                                //some party name as flatno with that only so below code to sepearate it
                                               /* if(jsonObject1.getString("name").contains("("))
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
*/
                                                amountplus = amountplus+jsonObject1.getInt("amount");
                                                Log.d("amountplus", String.valueOf(amountplus));
                                            /*    sales_register_model.setDate1(timeperiod1);
                                                sales_register_model.setDate2(timeperiod2);
*/
                                                partynametype.add(jsonObject1.getString("parent_name_2"));


                                              //  movieList.add(sales_register_model);

                                            }

                                                  /*  for all group*/
                                            Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(partynametype);
                                            partynametype.clear();
                                            partynametype.addAll(primesWithoutDuplicates);
                                            Collections.sort(partynametype, String.CASE_INSENSITIVE_ORDER);
                                            Log.d("amountplus",String.valueOf(partynametype));



                                            String amountall = String.valueOf(amountplus);
                                            totalsd.setText("\u20B9"+amountall);


                                            for (int j=0;j<partynametype.size();j++) {


                                                int amountplus2=0;
                                                for (int i = 0; i < jsonArray1.length(); i++) {
                                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                                    if (partynametype.get(j).equals(jsonObject1.getString("parent_name_2"))) {


                                                        amountplus2 = amountplus2 + jsonObject1.getInt("amount");


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
                                          /*  sales_register_model sales_register_model = new sales_register_model();
                                            sales_register_model.setTest("1");
                                          */  adapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            spotsDialog.dismiss();
                                        }

                                        adapter.notifyDataSetChanged();

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


    private void getDatasales_Account() {
      /*  final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/


        String url = null;
        try {
            url = "http://150.242.14.196:8012/society/service/app/get_bill.php?" +
                    "filter_date="+ URLEncoder.encode(timeperiod1, "UTF-8")+
                    "&filter_date_end="+URLEncoder.encode(timeperiod2, "UTF-8")
                    +"&company_id="+stallyid+"&type="+"bill";
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



                                            movieList.clear();

                                            int amountplus=0;
                                            for (int i=0;i<jsonArray1.length();i++)
                                            {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                                sales_register_model sales_register_model = new sales_register_model();

                                                    sales_register_model.setName(jsonObject1.getString("ledger_name"));
                                                    sales_register_model.setAmount(jsonObject1.getString("amount"));
                                                    sales_register_model.setFlatno(jsonObject1.getString("flat_no"));


                                                amountplus = amountplus+jsonObject1.getInt("amount");
                                                Log.d("amountplus", String.valueOf(amountplus));

                                                sales_register_model.setDate1(timeperiod1);
                                                sales_register_model.setDate2(timeperiod2);


                                                movieList.add(sales_register_model);

                                            }


                                            String amountall = String.valueOf(amountplus);
                                            totalsa.setText("\u20B9"+amountall);
                                        //    adapter.notifyDataSetChanged();
                                            adapter2 = new RecieptActivityAdapter(getApplicationContext(),movieList);

                                            recyclerView2.setAdapter(adapter2);



                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                        }

                                        //adapter.notifyDataSetChanged();

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.allreportmenu, menu);
        //menuItem = menu.findItem(R.id.search);
        // Associate searchable configuration with the SearchView
      /*  SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
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
        });*/
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
        else if (id == R.id.action_calender) {
            //info BS
            //    information_bottomsheet();
            builder = new AlertDialog.Builder(SalesChildActivity.this);


            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SalesChildActivity.this,
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
                        monthstring();
                        final Calendar calendartp= Calendar.getInstance();
                        //setting calender to custom date

                        timeperiod2=day2+" "+month2+" "+calendartp.get(Calendar.YEAR);

                        calendartp.add(Calendar.YEAR, -1);
                        timeperiod1=day1+" "+month1+" "+calendartp.get(Calendar.YEAR);
                        getData();
                        getDatasales_Account();
                    }
                    else if(strName=="Last Year")
                    {
                        monthstring();
                        final Calendar calendartp= Calendar.getInstance();
                        //setting calender to custom date
                        calendartp.add(Calendar.YEAR, -1);
                        timeperiod2=day2+" "+month2+" "+calendartp.get(Calendar.YEAR);

                        calendartp.add(Calendar.YEAR, -1);


                        timeperiod1=day1+" "+month1+" "+calendartp.get(Calendar.YEAR);




                        getData();
                        getDatasales_Account();

                    }
                    Toast toast=Toast.makeText(getApplicationContext(),strName,Toast.LENGTH_SHORT);
                    toast.show();
                    dialog.dismiss();
                }
            });
            builder.show();


        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem sharemenu = menu.findItem(R.id.share);
        MenuItem sharegroup = menu.findItem(R.id.group);
        MenuItem searchgroup = menu.findItem(R.id.action_search);


        searchgroup.setVisible(false);
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
        builder = new AlertDialog.Builder(SalesChildActivity.this);

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

                                monthstring();
                                timeperiod1=day1+" "+month1+" "+year1;


                                timeperiod2=day2+" "+month2+" "+year2;

                                getData();
                                getDatasales_Account();

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
    public void monthstring()
    {
        switch(month1) {
            case "1":
                month1="Jan";
                break;
            case "2":
                month1="Feb";
                break;
            case "3":
                month1="Mar";
                break;
            case "4":
                month1="Apr";
                break;
            case "5":
                month1="May";
                break;
            case "6":
                month1="Jun";
                break;
            case "7":
                month1="July";
                break;
            case "8":
                month1="Aug";
                break;
            case "9":
                month1="Sept";
                break;
            case "10":
                month1="Oct";
                break;
            case "11":
                month1="Nov";
                break;
            case "12":
                month1="Dec";
                break;

        }
        switch(month2) {
            case "1":
                month2="Jan";
                break;
            case "2":
                month2="Feb";
                break;
            case "3":
                month2="Mar";
                break;
            case "4":
                month2="Apr";
                break;

            case "5":
                month2="May";
                break;
            case "6":
                month2="Jun";
                break;
            case "7":
                month2="July";
                break;
            case "8":
                month2="Aug";
                break;
            case "9":
                month2="Sept";
                break;
            case "10":
                month2="Oct";
                break;
            case "11":
                month2="Nov";
                break;
            case "12":
                month2="Dec";
                break;


        }
    }

}
