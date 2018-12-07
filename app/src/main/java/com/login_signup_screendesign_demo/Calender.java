package com.login_signup_screendesign_demo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Calender extends AppCompatActivity {

    private Button beeda;
    private EditText dateoflast;
    ProgressDialog pd;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender);

        MobileAds.initialize(this, "ca-app-pub-4499196016486715~5943501218");
        mAdView=(AdView)findViewById(R.id.adView);
        AdRequest a=new AdRequest.Builder().build();
        mAdView.loadAd(a);
        beeda=(Button)findViewById(R.id.btn_date);
        dateoflast=(EditText)findViewById(R.id.none);
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Please wait");
        pd.setIndeterminate(true);
        pd.setCancelable(false);

        beeda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.lastTime=dateoflast.getText().toString().trim();
                pd.show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user==null) {
                    Toast.makeText(getApplicationContext(), "Error: Please login again and then try", Toast.LENGTH_SHORT).show();
                    return;
                }
                String ID = user.getUid();
                FirebaseDatabase.getInstance().getReference().child("users").child(ID).child("date").setValue(Helper.lastTime).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Date updated successfully, now your details will not be shown for a period of 90 days", Toast.LENGTH_LONG).show();
                        //Log.d(TAG,"updation done ");
                        pd.dismiss();
                    }
                });
            }
        });

    }
}
