package com.login_signup_screendesign_demo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class phverify extends Activity {

    String verificationId;
    Button ok_btn;
    FirebaseAuth mAuth;
    EditText cod;
    Dialog dialog;
    ProgressDialog pd;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.phverify);
        MobileAds.initialize(this, "ca-app-pub-4499196016486715~5943501218");
        mAdView=(AdView)findViewById(R.id.adView);
        AdRequest a=new AdRequest.Builder().build();
        mAdView.loadAd(a);
        mAuth = FirebaseAuth.getInstance();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.phverify);
        cod=(EditText)findViewById(R.id.code);
        ok_btn = (Button) findViewById(R.id.ok);
        ok_btn.setEnabled(false);
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Please wait");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        String phone = "+91"+Helper.ph;
        Toast.makeText(getApplicationContext(),"Please wait until OTP arrives",Toast.LENGTH_LONG).show();
        if(phone.length() != 13 ){
            Toast.makeText(getApplicationContext(),"Please provide a valid phone number",Toast.LENGTH_LONG).show();
            return;
        }

        sendVerificationCode(phone);

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = cod.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    cod.setError("Enter code...");
                    cod.requestFocus();
                    return;
                }
                //Toast.makeText(getApplicationContext(),"code is "+code,Toast.LENGTH_LONG).show();
                verifyCode(code);
            }
        });

    }

    private void verifyCode(String code) {
       // Toast.makeText(getApplicationContext(),"code is "+code,Toast.LENGTH_LONG).show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        pd.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Helper.flag=true;
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Log.d(TAG, "User account deleted.");
                                            }
                                        }
                                    });
                            pd.hide();
                            Intent intent = new Intent(phverify.this, profile.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        } else {
                            Toast.makeText(phverify.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
       // progressBar.setVisibility(View.VISIBLE);
        pd.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                30,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
        pd.dismiss();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            ok_btn.setEnabled(true);
            Toast.makeText(getApplicationContext(),"OTP sent successfully",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
           // Toast.makeText(getApplicationContext(),"we are here",Toast.LENGTH_LONG).show();
            if (code != null) {
                cod.setText(code);
              //  Toast.makeText(getApplicationContext(),"automatic call to verify code",Toast.LENGTH_LONG).show();
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(phverify.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}


