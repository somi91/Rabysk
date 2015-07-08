package com.uslive.rabyks.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uslive.rabyks.R;
import org.json.JSONObject;

import java.util.Calendar;

import com.uslive.rabyks.AsyncTasks.Register;
import com.uslive.rabyks.helpers.SQLiteHandler;
import com.uslive.rabyks.helpers.SessionManager;

public class RegisterActivity extends ActionBarActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String username, final String email, final String password) {

        pDialog.setMessage("Registering ...");
        showDialog();

        Calendar c = Calendar.getInstance();

        String day = String.valueOf(c.get(Calendar.DATE));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String year = String.valueOf(c.get(Calendar.YEAR));
        String date = day + " - "+ month + " - " + year;


        // Inserting row in users table
//        db.addUser(username, email, password, date);
        hideDialog();

        // Get user phone number (requires user permissions in manifest file)
        TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String number = tMgr.getLine1Number();

        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.accumulate("username", username);
            jsonUser.accumulate("email", email);
            jsonUser.accumulate("password", password);
            jsonUser.accumulate("number", number);

            String userJson = jsonUser.toString();

            // Register user on server
            Register rast = new Register(getApplicationContext());
            rast.execute(getString(R.string.serverIP) + "/register", userJson);
            // Launch login activity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e("User json crap", e.getMessage());
        }
    }



}
