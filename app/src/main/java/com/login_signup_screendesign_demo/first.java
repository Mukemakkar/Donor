package com.login_signup_screendesign_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class first extends AppCompatActivity {
    private static FragmentManager fragmentManager;
    Button b,b1,b2;
    AdView mAdView;
    public static boolean user=false;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);

        MobileAds.initialize(this, "ca-app-pub-4499196016486715~5943501218");
        mAdView=(AdView)findViewById(R.id.adView);
        //mAdView.setsi
        AdRequest a=new AdRequest.Builder().build();
        mAdView.loadAd(a);
        openLogin();
        fragmentManager = getSupportFragmentManager();
        signup();
        b2=(Button)findViewById(R.id.finddonor);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent("com.login_signup_screendesign_demo.search");
                startActivity(i);
            }
        });
    }
     public void openLogin(){
        b=(Button)findViewById(R.id.adonor);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent("com.login_signup_screendesign_demo.MainActivity");
                startActivity(i);
            }
        });
     }

     public void signup(){

         b1=(Button)findViewById(R.id.wannabedonor);
         b1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                // setContentView(R.layout.activity_main);
                 user=true;
                 Intent i=new Intent("com.login_signup_screendesign_demo.MainActivity");
                 startActivity(i);
                // replaceLoginFragment();
             }
         });
    }

    public boolean getuser(){
        return user;
    }


}
