package com.login_signup_screendesign_demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class profile extends AppCompatActivity {

    private FirebaseAuth fa;
    boolean success=false;
    private List DonorList;
    private TextView name,email,phone,address,bloodgroup,verify;
    private Button update,delete,logout,date;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;
    ProgressDialog pd;
    private boolean again=true;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        MobileAds.initialize(this, "ca-app-pub-4499196016486715~5943501218");
        mAdView=(AdView)findViewById(R.id.adView);
        AdRequest a=new AdRequest.Builder().build();
        mAdView.loadAd(a);
        DonorList = new ArrayList<Donor>();
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Please wait");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        bloodgroup = (TextView) findViewById(R.id.blood);
        verify = (TextView) findViewById(R.id.verify);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        logout = (Button) findViewById(R.id.logout);
        date = (Button) findViewById(R.id.date);


        if (Helper.throughSignIn) {
            mAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference();
            pd.show();
            FirebaseUser user = mAuth.getCurrentUser();
            userID = user.getUid();
            // userID="fdGC1pHFgBMVial7f2MweNE8QDm2";

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        UserInformation uInfo = new UserInformation();
                        uInfo.address = ds.child(userID).getValue(UserInformation.class).address;
                        uInfo.blood = ds.child(userID).getValue(UserInformation.class).blood;
                        uInfo.email = ds.child(userID).getValue(UserInformation.class).email;
                        uInfo.name = ds.child(userID).getValue(UserInformation.class).name;
                        uInfo.phone = ds.child(userID).getValue(UserInformation.class).phone;//set the name
                        //uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail()); //set the email

                        name.setText("Name: " + uInfo.name);
                        bloodgroup.setText("Blood Group: " + uInfo.blood);
                        email.setText("Email: " + uInfo.email);
                        phone.setText("Phone: " + uInfo.phone);
                        address.setText("Address: " + uInfo.address);

                        pd.dismiss();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            if (Helper.flag) {

                doSomeStuff();
            }
        }

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Toast tag = Toast.makeText(getBaseContext(), "Please delete this account and make a new account if you want to update information.",Toast.LENGTH_SHORT);

                    tag.show();

                    new CountDownTimer(6000, 1000)
                    {

                        public void onTick(long millisUntilFinished) {tag.show();}
                        public void onFinish() {tag.show();}

                    }.start();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pd.show();
                    again = false;
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String ID = user.getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(ID);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        pd.hide();
                                        Toast.makeText(getApplicationContext(),"user deleted successfully",Toast.LENGTH_LONG).show();
                                        Log.d(TAG, "User account deleted.");
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Error: Please try again after logging in",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    pd.hide();
                }
            });

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getApplicationContext(),"Sign out successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(profile.this, first.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });

            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(profile.this, Calender.class);
                    startActivity(intent);
                }
            });

    }

      private synchronized void doSomeStuff() {
        pd.show();
        fa=FirebaseAuth.getInstance();
        fa.createUserWithEmailAndPassword(Helper.getEmailId, Helper.getPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser usr=fa.getCurrentUser();
                           // usr.sendEmailVerification();
                            String user = fa.getCurrentUser().getUid();
                            DatabaseReference dbr= FirebaseDatabase.getInstance().getReference().child("users").child(user);
                            Map info=new HashMap();
                            info.put("name",Helper.getFullName.toUpperCase());
                            info.put("blood",Helper.getBlood.toUpperCase().trim());
                            info.put("email",Helper.getEmailId);
                            info.put("address",Helper.getLocation.toUpperCase());
                            info.put("city",Helper.getcity.toUpperCase().trim());
                            info.put("phone",Helper.getMobileNumber);
                            info.put("date","date");
                            info.put("combo",Helper.getBlood.toUpperCase()+Helper.getcity.toUpperCase());

                            dbr.setValue(info);
                            success=true;
                            mAuth = FirebaseAuth.getInstance();
                            mFirebaseDatabase = FirebaseDatabase.getInstance();
                            myRef = mFirebaseDatabase.getReference();

                            FirebaseUser ur = mAuth.getCurrentUser();
                            userID = ur.getUid();
                            // userID="fdGC1pHFgBMVial7f2MweNE8QDm2";
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                        if(again){
                                        UserInformation uInfo = new UserInformation();
                                        uInfo.address = ds.child(userID).getValue(UserInformation.class).address;
                                        uInfo.blood = ds.child(userID).getValue(UserInformation.class).blood;
                                        uInfo.email = ds.child(userID).getValue(UserInformation.class).email;
                                        uInfo.name = ds.child(userID).getValue(UserInformation.class).name;
                                        uInfo.phone = ds.child(userID).getValue(UserInformation.class).phone;//set the name
                                        //uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail()); //set the email

                                        name.setText("Name: " + uInfo.name);
                                        bloodgroup.setText("Blood Group: " + uInfo.blood);
                                        email.setText("Email: " + uInfo.email);
                                        phone.setText("Phone: " + uInfo.phone);
                                        address.setText("Address: " + uInfo.address);

                                        pd.dismiss();

                                    } }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                           // onBackPressed();
                            // updateUI(user);
                        }
                        else {
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                    }
                });
    }
}
