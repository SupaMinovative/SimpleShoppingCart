package com.minovative.simpleshoppingcart;

import static com.minovative.simpleshoppingcart.Helper.clearSharedPreferences;
import static com.minovative.simpleshoppingcart.Helper.saveData;
import static com.minovative.simpleshoppingcart.Helper.setEmptyText;
import static com.minovative.simpleshoppingcart.Helper.setErrorText;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private Button loginBtn;
    private Button regBtn;
    private LinearLayout regOpt;
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
        remember = findViewById(R.id.rememberCheck);

        db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        // Account status pass via intent
        logoutStatus = getIntent().getBooleanExtra("ACCOUNT_STATUS",false);

        if (logoutStatus) {

            accStatus = false;
            remember.setChecked(false);
            clearSharedPreferences(this);
        } else {
                retrieveData(currentUser);
        };

        // Sign up button event
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

        // Registration event
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

            // Checking for existing users on database
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
                        remember.setVisibility(View.GONE);
                        regCon.setText("You already have an account. Please login.");
                        regBtn.setVisibility(View.GONE);
                        loginBtn.setVisibility(View.VISIBLE);
                    });
                }

                if (!exists) {
                    User newUser = new User(username,email,password, false);
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
                remember.setVisibility(View.GONE);
                loginTextView.setText("WELCOME");
                regCon.setVisibility(View.VISIBLE);
                regCon.setText("Registered successfully! You can now log in to your account.");
                regBtn.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
            }
        });

        // Login event
        loginBtn.setOnClickListener(v -> {

            remember.setVisibility(View.VISIBLE);
            loginTextView.setText("LOGIN");
            emailText.setVisibility(View.VISIBLE);
            passText.setVisibility(View.VISIBLE);
            regCon.setVisibility(View.GONE);
            regBtn.setVisibility(View.GONE);

            String loginEmail = emailText.getText().toString().trim();
            String loginPass = passText.getText().toString().trim();

            if (loginEmail.isEmpty() && loginPass.isEmpty()) {
                return;
            }

            new Thread(( ) -> {

                userOnDb = userDao.getUserLogin(loginEmail,loginPass);

                runOnUiThread(( ) -> {
                    boolean isCorrect = false;

                    for (User user : userOnDb) {
                        currentUser = user.getUsername();
                        if (loginEmail.equals(user.getEmail()) && loginPass.equals(user.getPassword())) {

                            user.setStatus(true);

                            isCorrect = true;
                            break;
                        }
                    }
                    // On login successfully
                    if (isCorrect) {

                        saveData(loginEmail,loginPass, currentUser, accStatus, remember, this);
                        Intent intent = new Intent(MainActivity.this,ShoppingActivity.class);
                        intent.putExtra("EMAIL",loginEmail);
                        intent.putExtra("USERNAME",currentUser);
                        startActivity(intent);
                        finish();

                    } else {
                        setErrorText(errorText,"Email or password is incorrect. Please try again.");
                    }
                });
            }).start();
        });
    }

    public void retrieveData(String currentUser) {
        sharedPreferences = getSharedPreferences("saveData",MODE_PRIVATE);
        if (currentUser == null) {
            currentUser = sharedPreferences.getString("key username",null);}

        accStatus = sharedPreferences.getBoolean("key remember",false);

        if (accStatus) {
            remember.setChecked(true);
            Intent intent = new Intent(MainActivity.this,ShoppingActivity.class);
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

}