package com.login_signup_screendesign_demo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class search extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DonorAdapter adapter;
    private List<Donor> DonorList;
    private Button search;
    private EditText city,blood;
    String date1,date2;
    DatabaseReference db;
    SimpleDateFormat sdf;
    ProgressDialog pd;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        MobileAds.initialize(this, "ca-app-pub-4499196016486715~5943501218");
        mAdView=(AdView)findViewById(R.id.adView);
        AdRequest a=new AdRequest.Builder().build();
        mAdView.loadAd(a);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DonorList = new ArrayList<>();
        adapter = new DonorAdapter(this, DonorList);
        recyclerView.setAdapter(adapter);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        date1 = sdf.format(c.getTime());
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Please wait");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
       // date1.replaceAll("/"," ");
       // db=db.getDatabase().getReference("users");
        search=(Button)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pd.show();
                city=(EditText)findViewById(R.id.city);
                blood=(EditText)findViewById(R.id.bg);

                        String c=city.getText().toString().toUpperCase().trim();
                        String bg=blood.getText().toString().toUpperCase().trim();
                        if(bg.equals("AB+")) {
                            Query query = FirebaseDatabase.getInstance().getReference("users")
                                    .orderByChild("city").equalTo(c);
                            query.addListenerForSingleValueEvent(valueEventListener);
                        }
                        else {
                            Query query = FirebaseDatabase.getInstance().getReference("users")
                                    .orderByChild("combo").equalTo(bg + c);
                            query.addListenerForSingleValueEvent(valueEventListener);
                        }

                    }


        });

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            DonorList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                  //  if(snapshot.child("date").getValue().equals("date")) {   //change 1
                        Donor donor = snapshot.getValue(Donor.class);
                        donor.date = snapshot.getValue(Donor.class).date;
                        date2=donor.date;
                       // Toast.makeText(getApplicationContext(), "Date: "+date, Toast.LENGTH_LONG).show();

                        if(date2.equals("date")){
                            DonorList.add(donor);
                        }
                        else{
                            float daysBetween;
                            try {
                                Date dateBefore = sdf.parse(date2);
                                Date dateAfter = sdf.parse(date1);
                                long difference = dateAfter.getTime() - dateBefore.getTime();
                                daysBetween = (difference / (1000 * 60 * 60 * 24));
                                if(daysBetween>90.0){
                                    DonorList.add(donor);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Sorry, no results found", Toast.LENGTH_LONG).show();
                                }
                            }
                         catch (ParseException e) {
                        e.printStackTrace();
                          }

                        }

                    //}
                }
                adapter.notifyDataSetChanged();
            }
            else Toast.makeText(getApplicationContext(), "No results found", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
