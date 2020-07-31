package com.shivam.kritika_01_sell;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Fragment extends Fragment implements View.OnClickListener {

    private View view;
    private EditText fullName, emailId, mobileNumber, location,password, confirmPassword;
    private TextView login;
    private Button signUpButton;
    private CheckBox terms_conditions;

    private FirebaseAuth mAuth;

    public SignUp_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up_, container, false);

        initViews();
        setListeners();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createUser();

            }
        });

        return view;
    }

    private void setListeners() {

        //signUpButton.setOnClickListener(this);
        // login.setOnClickListener(this);

    }

    private void initViews() {

        mAuth = FirebaseAuth.getInstance();

        fullName = view.findViewById(R.id.fullName);
        emailId = view.findViewById(R.id.userEmailId);
        mobileNumber = view.findViewById(R.id.mobileNumber);
        location = view.findViewById(R.id.location);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        signUpButton = view.findViewById(R.id.signUpBtn);
        login = view.findViewById(R.id.already_user);
        terms_conditions = view.findViewById(R.id.terms_conditions);

        // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
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
               // new MainActivity().replaceLoginFragment();
                ((RegisterActivity) Objects.requireNonNull(getActivity())).replaceLoginFragment();
                break;
        }

    }

    private boolean validatePassword() {

        String password1 = password.getText().toString().trim();

        if (password1.isEmpty()) {
            password.setError("Password is required. Can't be empty.");
            return false;
        } else if (password.length() < 6) {
            password.setError("Password short. Minimum 6 characters required.");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    private boolean validateEmailAddress() {

        String email = emailId.getText().toString().trim();

        if (email.isEmpty()) {
            emailId.setError("Email is required. Can't be empty.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailId.setError("Invalid Email. Enter valid email address.");
            return false;
        } else {
            emailId.setError(null);
            return true;
        }
    }

    private void createUser() {

        if (!validateEmailAddress() | !validatePassword()) {
            // Email or Password not valid,
            return;
        }
        //Email and Password valid, create user here
        String email = emailId.getText().toString().trim();
        String password1 = password.getText().toString().trim();
        //showProgressBar();

        mAuth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "User created", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);
                            //hideProgressBar();
//                            updateUI();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //hideProgressBar();
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getActivity(), "Email already in use", Toast.LENGTH_SHORT).show();
                           // mOutputText.setText("Email already in use");
                        }
                    }
                });
    }

    private void checkValidation() {

        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getLocation = location.getText().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0){

            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
        }

        //new CustomToast().Show_Toast(getActivity(), view,
        //        "All fields are required.");

        // Check if email id valid or not
        else if (!m.find())
            emailId.setError("Your Email Id is Invalid");

            // new CustomToast().Show_Toast(getActivity(), view,
            //         "Your Email Id is Invalid.");

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword)){
            password.setError("Both password doesn't match.");
            password.setError("Both password doesn't match.");
        }

        //new CustomToast().Show_Toast(getActivity(), view,
        //        "Both password doesn't match.");

        // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked()){

            Toast.makeText(getActivity(), "Please select Terms and Conditions.", Toast.LENGTH_SHORT).show();
        }

        //new CustomToast().Show_Toast(getActivity(), view,
        //        "Please select Terms and Conditions.");

        // Else do signup or do your stuff
        else
            Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
                    .show();

    }

}