package main.society365.splash_screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import main.society365.login.Login;
import main.society365.R;
import main.society365.maneger.MainActivity;
import main.society365.user.Home;

public class Splash_screen extends AppCompatActivity
{
    public SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences("main.society365", Context.MODE_PRIVATE);

        handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                SharedPreferences.Editor editor = sharedPreferences.edit();


                if(sharedPreferences.getBoolean("DATA",false))
                {
                   /* Intent splashLogin = new Intent(Splash_screen.this, Login.class);
                    splashLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(splashLogin);
                    finish();
                     */
                   if(sharedPreferences.getString("USERTYPE", "0").equals("1"))
                   {
                       Intent splashLogin = new Intent(Splash_screen.this, MainActivity.class);
                       splashLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(splashLogin);
                       finish();
                   }else if(sharedPreferences.getString("USERTYPE", "0").equals("2")) {
                       Intent splashLoginm = new Intent(Splash_screen.this, Home.class);
                       splashLoginm.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(splashLoginm);
                       finish();
                   }
                   else {
                       Log.d("splashsc","nouser");
                   }

                }else
                {
                    Intent splashLogin = new Intent(Splash_screen.this, Login.class);
                    splashLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(splashLogin);
                    finish();
                }


            }
        },1500);
    }
}
