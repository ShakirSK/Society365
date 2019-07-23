package main.society365.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import main.society365.ForgotPassword;
import main.society365.InernetConnection.Connectivity;
import main.society365.R;
import main.society365.SingleTonRequest.MySingleton;
import main.society365.maneger.MainActivity;
import main.society365.staticurl.StaticUrl;
import main.society365.user.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class Login extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    FloatingActionButton button;
    Spinner spinner;
    String item,usertype;
    String strtemailid,strtpassword;
    EditText emailid,password;
    SpotsDialog spotsDialog;
    TextView forgot;

    String ufname,ulname,societyid,usrid,uemail,mno,utype,uprof,fltno,bldid,fltstatus,sname,saddress,stallyid,joiningdate,societyname,address,building,flat;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.floatingActionButton);
        emailid=findViewById(R.id.email);
        password=findViewById(R.id.password);
        spinner = findViewById(R.id.spinner);
        forgot= findViewById(R.id.forgetpass);

        sharedPreferences = getSharedPreferences("main.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });

        spotsDialog = new SpotsDialog(this, R.style.Custom);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                item = adapterView.getItemAtPosition(i).toString();
                Log.d("item", item);

                if (item.equals("Manager"))
                {
                    usertype="1";
                    Log.d("usertype", usertype);
                }else if (item.equals("User"))
                {
                    usertype="2";
                    Log.d("usertypeu",usertype);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add("User");
        categories.add("Manager");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
    }

  @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        item = adapterView.getItemAtPosition(i).toString();
        Log.d("select", item);
        Toast.makeText(adapterView.getContext(), "Selected:" + item, Toast.LENGTH_SHORT).show();

    }
      @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void validation()
    {
        if (emailid.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Enter Emailid",Toast.LENGTH_SHORT).show();
        }else if(password.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
        }else
        {
            login();
        }
            //strtemailid = emailid.getText().toString();
       // strtpassword = password.getText().toString();
    }



    private void login()
    {
        String login = StaticUrl.login+
               "?email="+emailid.getText().toString()+
               "&password="+password.getText().toString()+
               "&token=197dsfsdf"+
               "&user_type="+usertype;
        //String Loginurl="http://makonlinesolutions.com/aditi/societymgt/api/login.php?email=aditichavan1111@gmail.com
        // &password=L76HRehF&token=197dsfsdf&user_type=1";
       Log.d("loginurl", login);


        if (Connectivity.isConnected(getApplicationContext())){

            spotsDialog.show();

            final StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("responsenew30", response);
                           spotsDialog.dismiss();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.getString("status").equals("1"))
                                {
                                    editor.clear();
                                    editor.apply();

                                    flat=jsonObject.getString("flat");
                                    building=jsonObject.getString("building");

                                    Log.d("msg", jsonObject.getString("message"));
                                    JSONArray jsonArray1=jsonObject.getJSONArray("user");
                                    JSONObject jsonObject1=jsonArray1.getJSONObject(0);

                                    ufname=jsonObject1.getString("first_name");
                                    Log.d("jsonObject1", ufname);
                                    ulname=jsonObject1.getString("last_name");
                                    societyid=jsonObject1.getString("society_id");
                                    usrid=jsonObject1.getString("user_id");
                                    uemail=jsonObject1.getString("email");
                                    mno=jsonObject1.getString("mobile_no");
                                    utype=jsonObject1.getString("user_type");
                                    uprof=jsonObject1.getString("profile_pic_url");
                                    fltno=jsonObject1.getString("flat_no");
                                    bldid=jsonObject1.getString("building_no");
                                    fltstatus=jsonObject1.getString("flat_status");
                                    joiningdate=jsonObject1.getString("join_date");


                                    JSONArray jsonArraySociety = jsonObject.getJSONArray("society");
                                    JSONObject jsonObjSociety = jsonArraySociety.getJSONObject(0);

                                    sname=jsonObjSociety.getString("name");
                                    saddress=jsonObjSociety.getString("address");

                                    stallyid=jsonObjSociety.getString("tally_company_id");


                                    if(usertype.equals("1"))
                                         {
                                                //ufname,ulname,societyid,usrid,uemail,mno,utype,uprof,fltno,bldid,fltstatus
                                             //Intent intent=new Intent(Login.this, MainActivity.class);
                                             editor.putBoolean("DATA", true);
                                             editor.putString("USERID",usrid);
                                             editor.putString("SOCIETYID", societyid);
                                             editor.putString("USERTYPE", usertype);

                                             editor.putString("firstname", ufname);
                                             editor.putString("lastname", ulname);
                                             editor.putString("email", uemail);
                                             editor.putString("mobile", mno);
                                             editor.putString("usertype", utype);
                                             editor.putString("profile", uprof);
                                             editor.putString("flatno", fltno);
                                             editor.putString("buildingno", bldid);
                                             editor.putString("buildingname", building);
                                             editor.putString("flatname", flat);
                                             editor.putString("flatstatus", fltstatus);
                                             editor.putString("sname", sname);
                                             editor.putString("saddress", saddress);
                                             editor.putString("stallyid",stallyid);
                                             editor.putString("joindate", joiningdate);

                                             editor.commit();
                                             editor.apply();

                                             Intent intent=new Intent(Login.this, MainActivity.class);
                                             intent.putExtra("firstname", ufname);
                                             intent.putExtra("lastname", ulname);
                                             intent.putExtra("societyid", societyid);
                                             intent.putExtra("userid", usrid);
                                             intent.putExtra("email", uemail);
                                             intent.putExtra("mobile", mno);
                                             intent.putExtra("usertype", utype);
                                             intent.putExtra("profile", uprof);
                                             intent.putExtra("flatno", fltno);
                                             intent.putExtra("buildingno", bldid);
                                             intent.putExtra("flatstatus", fltstatus);
                                             intent.putExtra("sname", sname);
                                             intent.putExtra("saddress", saddress);
                                             intent.putExtra("stallyid",stallyid);

                                             intent.putExtra("joindate", joiningdate);
                                             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                             startActivity(intent);

                                         }else   if(usertype.equals("2"))
                                         {
                                             editor.putBoolean("DATA", true);
                                             editor.putString("USERID",usrid);
                                             editor.putString("SOCIETYID", societyid);
                                             editor.putString("USERTYPE", usertype);
                                             editor.putString("USERTYPE", usertype);

                                             editor.putString("firstname", ufname);
                                             editor.putString("lastname", ulname);
                                             editor.putString("email", uemail);
                                             editor.putString("mobile", mno);
                                             editor.putString("usertype", utype);
                                             editor.putString("profile", uprof);
                                             editor.putString("flatno", fltno);
                                             editor.putString("buildingno", bldid);
                                             editor.putString("buildingname", building);
                                             editor.putString("flatname", flat);
                                             editor.putString("flatstatus", fltstatus);
                                             editor.putString("sname", sname);
                                             editor.putString("saddress", saddress);
                                             editor.putString("joindate", joiningdate);
                                             editor.putString("stallyid",stallyid);
                                             editor.commit();
                                             editor.apply();

                                             Intent intent=new Intent(Login.this, Home.class);
                                             intent.putExtra("firstname", ufname);
                                             intent.putExtra("lastname", ulname);
                                             intent.putExtra("societyid", societyid);
                                             intent.putExtra("userid", usrid);
                                             intent.putExtra("email", uemail);
                                             intent.putExtra("mobile", mno);
                                             intent.putExtra("usertype", utype);
                                             intent.putExtra("profile", uprof);
                                             intent.putExtra("flatno", fltno);
                                             intent.putExtra("buildingno", bldid);
                                             intent.putExtra("buildingname", building);
                                             intent.putExtra("flatname", flat);
                                             intent.putExtra("flatstatus", fltstatus);
                                             intent.putExtra("sname", sname);
                                             intent.putExtra("saddress", saddress);
                                             intent.putExtra("joindate", joiningdate);
                                             intent.putExtra("stallyid",stallyid);
                                             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                             startActivity(intent);
                                         }
                                } else
                                {
                                   Log.d("msg", jsonObject.getString("message"));
                                   String msg = jsonObject.getString("message");
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                }
                                 spotsDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            );
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }else {
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    private void submit()
    {
        if ( item.equals("Manager"))
        {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }else if (item.equals("User"))
        {
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);

        }

    }
}
