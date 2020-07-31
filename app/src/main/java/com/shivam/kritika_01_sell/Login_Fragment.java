package com.shivam.kritika_01_sell;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login_Fragment extends Fragment implements View.OnClickListener {

    FirebaseAuth mAuth;

    private View view;

    private MaterialEditText emailid, password;
    private Button loginButton;
    private TextView forgotPassword, signUp;
    private CheckBox show_hide_password;
    private LinearLayout loginLayout;
    private Animation shakeAnimation;
    private FragmentManager fragmentManager;

    public Login_Fragment() {
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
        view =inflater.inflate(R.layout.fragment_login_, container, false);
        initViews();
        setListeners();
        return view;
    }

    private void setListeners() {

        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });

    }

    private void initViews() {

        mAuth=FirebaseAuth.getInstance();

        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();

        emailid = view.findViewById(R.id.login_emailid);
        password = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.loginBtn);
        forgotPassword = view.findViewById(R.id.forgot_password);
        signUp = view.findViewById(R.id.createAccount);
        show_hide_password = view.findViewById(R.id.show_hide_password);
        loginLayout = view.findViewById(R.id.login_layout);

        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);

        // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
        }


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.loginBtn:
                SignInUser();
                //checkValidation();
                break;

            case R.id.forgot_password:

                // Replace forgot password fragment with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer,
                                new ForgotPassword_Fragment(),
                                Utils.ForgotPassword_Fragment).commit();
                break;
            case R.id.createAccount:

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new SignUp_Fragment(),
                                Utils.SignUp_Fragment).commit();
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

        String email = emailid.getText().toString().trim();

        if (email.isEmpty()) {
            emailid.setError("Email is required. Can't be empty.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailid.setError("Invalid Email. Enter valid email address.");
            return false;
        } else {
            emailid.setError(null);
            return true;
        }
    }



    private void SignInUser(){


        if (!validateEmailAddress() | !validatePassword()) {
            // Email or Password not valid,
            return;
        }
        //Email and Password valid, sign in user here

        String email = emailid.getText().toString().trim();
        String password1 = password.getText().toString().trim();
        //showProgressBar();

        mAuth.signInWithEmailAndPassword(email, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                           // hideProgressBar();
                            Toast.makeText(getActivity(), "User logged in", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);
//                            updateUI();
                        } else {
                            //hideProgressBar();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getActivity(), "Invalid Password", Toast.LENGTH_SHORT).show();
                                password.setError("Invalid Password");
                                //mOutputText.setText("Invalid Password");
                            } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(getActivity(), "Email not is use", Toast.LENGTH_SHORT).show();
                                emailid.setError("Email not in use");
                                //mOutputText.setText("Email not in use");
                            }

                        }
                    }
                });
    }






    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            emailid.setError("Please Enter Email Id");

           // new CustomToast().Show_Toast(getActivity(), view,
           //         "Enter both credentials.");
        }
        else if(getPassword.equals("") || getPassword.length() == 0){
            loginLayout.startAnimation(shakeAnimation);

            password.setError("Password too short");

        }
        // Check if email id is valid or not
        else if (!m.find()){
            emailid.setError("Your Email Id is Invalid");
        }


            //new CustomToast().Show_Toast(getActivity(), view,
            //        "Your Email Id is Invalid.");
            // Else do login and do your stuff
        else
            Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT)
                    .show();

    }
}