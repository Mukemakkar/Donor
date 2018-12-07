package com.login_signup_screendesign_demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.login_signup_screendesign_demo.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp_Fragment extends Fragment implements OnClickListener {
	private static View view;
    private static FragmentManager fragmentManager;
	private static EditText fullName, emailId, mobileNumber, location,
			password, confirmPassword,blood,city;
	private static TextView login;
	private static Button signUpButton;
	private static CheckBox terms_conditions;
	private ProgressDialog pd;



	public SignUp_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fragmentManager=getFragmentManager();
		view = inflater.inflate(R.layout.signup_layout, container, false);
		pd=new ProgressDialog(getContext());

		initViews();
		setListeners();
		return view;
	}

	// Initialize all views
	private void initViews() {
		fullName = (EditText) view.findViewById(R.id.fullName);
		emailId = (EditText) view.findViewById(R.id.userEmailId);
		mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
		location = (EditText) view.findViewById(R.id.location);
		password = (EditText) view.findViewById(R.id.password);
		confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
		signUpButton = (Button) view.findViewById(R.id.signUpBtn);
		login = (TextView) view.findViewById(R.id.already_user);
		terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);
		blood=(EditText)view.findViewById(R.id.blood);
		city=(EditText)view.findViewById(R.id.ct);
		

		// Setting text selector over textviews
		XmlResourceParser xrp=null; //= getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			login.setTextColor(csl);
			terms_conditions.setTextColor(csl);
		} catch (Exception e) {
		}
	}


	// Set Listeners
	private void setListeners() {
		signUpButton.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signUpBtn:

			// Call checkValidation method
			checkValidation();
			break;

		case R.id.already_user:

			// Replace login fragment
			new MainActivity().replaceLoginFragment();
			break;
		}

	}

	// Check Validation Method
	private void checkValidation() {

		// Get all edittext texts
        Helper.getFullName = fullName.getText().toString();
        Helper.getEmailId = emailId.getText().toString();
        Helper.getMobileNumber = mobileNumber.getText().toString();
        Helper.getLocation = location.getText().toString();
        Helper.getBlood = blood.getText().toString();
        Helper.getPassword = password.getText().toString();
        Helper.getConfirmPassword = confirmPassword.getText().toString();
        Helper.getcity=city.getText().toString();
		/*blood.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {

				}
			}
		}); */

		// Pattern match for email id
		Pattern p = Pattern.compile(Utils.regEx);
		Matcher m = p.matcher(Helper.getEmailId);

		// Check if all strings are null or not
		if (Helper.getFullName.equals("") || Helper.getFullName.length() == 0
				|| Helper.getEmailId.equals("") || Helper.getEmailId.length() == 0
				|| Helper.getMobileNumber.equals("") || Helper.getMobileNumber.length() == 0
				|| Helper.getLocation.equals("") || Helper.getLocation.length() == 0
				|| Helper.getPassword.equals("") || Helper.getPassword.length() == 0
				|| Helper.getConfirmPassword.equals("")
				|| Helper.getConfirmPassword.length() == 0
                || Helper.getcity.equals("")
                || Helper.getcity.length() == 0
				|| Helper.getBlood.equals("")
				|| Helper.getBlood.length() == 0)

			new CustomToast().Show_Toast(getActivity(), view,
					"All fields are required.");

		// Check if email id valid or not

		else if (!m.find())
			new CustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");

		// Check if both password should be equal
		else if (!Helper.getConfirmPassword.equals(Helper.getPassword))
			new CustomToast().Show_Toast(getActivity(), view,
					"Both password doesn't match.");

		else if (Helper.getConfirmPassword.length()<6)
			new CustomToast().Show_Toast(getActivity(), view,
					"Password should be minimum 6 characters long");

		// Make sure user should check Terms and Conditions checkbox
		else if (!terms_conditions.isChecked())
			new CustomToast().Show_Toast(getActivity(), view,
					"Please select the checkbox.");

		// Else do signup or do your stuff
		else {
		    Helper.ph=Helper.getMobileNumber;
			Intent i = new Intent(getActivity(), phverify.class);
			startActivity(i);

		}

	}


}
