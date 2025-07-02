package com.minovative.simpleshoppingcart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText emailText;
    private EditText passText;
    private EditText reEmailText;
    private EditText rePassText;
    private TextView loginTextView;
    private TextView errorText;
    private TextView passErrorText;
    private TextView regCon;
    private TextView signupBtn;
    private TextView userShow;
    private Button loginBtn;

    private Button regBtn;
    private LinearLayout regOpt;
    private LinearLayout loginForm;
    private LinearLayout welcomePage;
    private String username;
    private String email;
    private String reEmailName;
    private String password;
    private String rePassword;
    private String currentUser;
    private AppDatabase db;
    private UserDao userDao;

    List<User> userOnDb;
    private CheckBox remember;
    private boolean accStatus;
    private boolean logoutStatus;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Log.d("DEBUG", "On create being called");
        loginTextView = findViewById(R.id.logInTextView);
        usernameText = findViewById(R.id.username);
        emailText = findViewById(R.id.emailText);
        passText = findViewById(R.id.passText);
        reEmailText = findViewById(R.id.reEmail);
        rePassText = findViewById(R.id.rePassword);
        errorText = findViewById(R.id.errorText);
        passErrorText = findViewById(R.id.passErrorText);
        regCon = findViewById(R.id.regCon);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signUpBtn);
        regBtn = findViewById(R.id.regisButton);
        regOpt = findViewById(R.id.regOpt);
        loginForm = findViewById(R.id.loginForm);
        welcomePage = findViewById(R.id.welcomePage);
        userShow = findViewById(R.id.usernameShow);
        remember = findViewById(R.id.rememberCheck);

        db = AppDatabase.getInstance(this);
        userDao = db.userDao();


        logoutStatus = getIntent().getBooleanExtra("ACCOUNT_STATUS",false);
        if (logoutStatus) {
            accStatus = false;
            remember.setChecked(false);
        } else {

            Log.d("DEBUG","Retrieve data is trigger");
            retrieveData(currentUser);

                Log.d("DEBUG","Account status: " + accStatus);
            }
        signupBtn.setOnClickListener(v -> {

            remember.setVisibility(View.GONE);
            loginTextView.setText("REGISTER");
            usernameText.setVisibility(View.VISIBLE);
            reEmailText.setVisibility(View.VISIBLE);
            rePassText.setVisibility(View.VISIBLE);
            regBtn.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
            regOpt.setVisibility(View.GONE);
        });

        regBtn.setOnClickListener(v -> {

            username = usernameText.getText().toString().trim();
            email = emailText.getText().toString().trim();
            reEmailName = reEmailText.getText().toString().trim();
            password = passText.getText().toString().trim();
            rePassword = rePassText.getText().toString().trim();

            if (!email.equals(reEmailName)) {

                setErrorText(errorText,"Email doesn't match. Please try again.");
                setEmptyText(reEmailText);
                return;
            }

            if (!password.equals(rePassword)) {
                setErrorText(passErrorText,"Password doesn't match. Please try again.");
                setEmptyText(rePassText);
                return;

            } else if (password.isEmpty() && rePassword.isEmpty() || email.isEmpty() && reEmailName.isEmpty()) {
                setErrorText(errorText,"Field is required");
                return;
            }

            new Thread(( ) -> {
                userOnDb = userDao.getUserLogin(email,password);
                boolean exists = false;

                for (User user : userOnDb){
                    if (email.equals(user.getEmail())) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {

                    runOnUiThread(( ) -> {
                        removeView();
                        setEmptyText(loginTextView);
                        setEmptyText(emailText);
                        setEmptyText(passText);
                        regCon.setText("You already have an account. Please login.");
                        regBtn.setVisibility(View.GONE);
                        loginBtn.setVisibility(View.VISIBLE);
                    });
                }

                if (!exists) {
                    User newUser = new User(username,email,password);
                    userDao.insertUser(newUser);
                }
            }).start();

            new android.os.Handler().postDelayed(( ) -> {

                setEmptyText(errorText);

            },2500);

            if (email.equals(reEmailName) && Objects.equals(password,rePassword)) {
                removeView();
                setEmptyText(emailText);
                setEmptyText(passText);
                remember.setVisibility(View.VISIBLE);
                loginTextView.setText("WELCOME");
                regCon.setVisibility(View.VISIBLE);
                regCon.setText("Registered successfully! You can now log in to your account.");
                regBtn.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
            }
        });

        loginBtn.setOnClickListener(v -> {
            loginTextView.setText("LOGIN");
            emailText.setVisibility(View.VISIBLE);
            passText.setVisibility(View.VISIBLE);
            regCon.setVisibility(View.GONE);
            regBtn.setVisibility(View.GONE);

            String loginEmail = emailText.getText().toString().trim();
            String loginPass = passText.getText().toString().trim();


            if (loginEmail.isEmpty() && loginPass.isEmpty()) {
                Log.d("DEBUG","LoginEmail " + loginEmail + "loginPass " + loginPass);
                return;
            }

            new Thread(( ) -> {

                userOnDb = userDao.getUserLogin(loginEmail,loginPass);

                runOnUiThread(( ) -> {
                    Log.d("DEBUG","User on DB " + userOnDb);
                    boolean isCorrect = false;

                    for (User user : userOnDb){
                        currentUser = user.getUsername();
                        if (loginEmail.equals(user.getEmail()) && loginPass.equals(user.getPassword())) {

                            Log.d("DEBUG","LoginEmail: " + loginEmail + "userEmail: " + user.getEmail() +
                                    "loginpass: " + loginPass + "userPass: " + user.getPassword());
                            isCorrect = true;
                            break;
                        }
                    }
                    if (isCorrect) {

                        saveData(loginEmail,loginPass, currentUser);
                        Log.d("DEBUG", "Username from login: " +currentUser);

                        Log.d("DEBUG","Account status from login: " + accStatus);
                        Intent intent = new Intent(MainActivity.this,ShoppingActivity.class);
                        intent.putExtra("EMAIL",loginEmail);
                        intent.putExtra("USERNAME",currentUser);
                        startActivity(intent);
                       /*
                        email = String.valueOf(emailText.getText());
                        password = String.valueOf(passText.getText());
                        welcomePage.setVisibility(View.VISIBLE);
                        emailText.setVisibility(View.VISIBLE);
                        passText.setVisibility(View.VISIBLE);
                        setEmptyText(passErrorText);
                        passErrorText.setVisibility(View.GONE);
                        loginForm.setVisibility(View.GONE);
                        userShow.setText(username);
*/
                    } else {
                        setErrorText(errorText,"Email or password is incorrect. Please try again.");
                    }
                });
            }).start();
        });


    }

    public void saveData(String email,String password, String currentUser) {
        sharedPreferences = getSharedPreferences("saveData",Context.MODE_PRIVATE);
        if (remember.isChecked()) {
            accStatus = true;
        } else {
            accStatus = false;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key name",email);
        editor.putString("key password",password);
        editor.putString("key username",currentUser);
        editor.putBoolean("key remember",accStatus);
        Log.d("DEBUG","Save by SP currentUser = " + currentUser);
        editor.apply();
        Toast.makeText(getApplicationContext(),"Your data is saved",Toast.LENGTH_LONG).show();
    }

    public void retrieveData(String currentUser) {
        sharedPreferences = getSharedPreferences("saveData",MODE_PRIVATE);
        if (currentUser == null) {
        currentUser = sharedPreferences.getString("key username",null);}

        accStatus = sharedPreferences.getBoolean("key remember",false);

        if (accStatus) {
            remember.setChecked(true);
            Intent intent = new Intent(MainActivity.this,ShoppingActivity.class);
            Log.d("DEBUG","Starting intent from retrieve data");
            intent.putExtra("USERNAME",currentUser);
            startActivity(intent);
            finish();
        } else {
            remember.setChecked(false);
        }
    }

    public void removeView( ) {
        usernameText.setVisibility(View.GONE);
        emailText.setVisibility(View.GONE);
        reEmailText.setVisibility(View.GONE);
        passText.setVisibility(View.GONE);
        rePassText.setVisibility(View.GONE);
    }

    public void setErrorText(TextView t,String str) {

        t.setVisibility(View.VISIBLE);
        t.setText(str);

        new android.os.Handler().postDelayed(( ) -> {

            setEmptyText(t);

        },2000);
    }

    public void setEmptyText(TextView t) {
        t.setText("");
    }

}