package com.login_signup_screendesign_demo;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.login_signup_screendesign_demo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {
	AdView mAdView;
	private static FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MobileAds.initialize(this, "ca-app-pub-4499196016486715~5943501218");
		mAdView=(AdView)findViewById(R.id.adView);
		AdRequest a=new AdRequest.Builder().build();
		mAdView.loadAd(a);  //newer
		//AdView adView = new AdView(this);
		//adView.setAdSize(AdSize.BANNER);
		//adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

		fragmentManager = getSupportFragmentManager();
		if(new first().getuser()){
			new first().user=false;
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new SignUp_Fragment(),
                            Utils.SignUp_Fragment).commit();
        }
		else{if (savedInstanceState == null) {
			fragmentManager
					.beginTransaction()
					.replace(R.id.frameContainer, new Login_Fragment(),
							Utils.Login_Fragment).commit();
		}}

		// On close icon click finish activity
		findViewById(R.id.close_activity).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finish();

					}
				});

	}

	// Replace Login Fragment with animation
	protected void replaceLoginFragment() {
		fragmentManager
				.beginTransaction()
				.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
				.replace(R.id.frameContainer, new Login_Fragment(),
						Utils.Login_Fragment).commit();
	}

	@Override
	public void onBackPressed() {

		// Find the tag of signup and forgot password fragment
		Fragment SignUp_Fragment = fragmentManager
				.findFragmentByTag(Utils.SignUp_Fragment);
		Fragment ForgotPassword_Fragment = fragmentManager
				.findFragmentByTag(Utils.ForgotPassword_Fragment);

		// Check if both are null or not
		// If both are not null then replace login fragment else do backpressed
		// task

		if (SignUp_Fragment != null)
			replaceLoginFragment();
		//else if (ForgotPassword_Fragment != null) replaceLoginFragment();
		else
			super.onBackPressed();
	}
}
