package com.example.mangpande.radityaholding;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    EditText username,password;

    SharedPreferences sharedpreferences;

    Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new Helper(Login.this);
        if(helper.validateLogin()){
            //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
            Intent it = new Intent(this, DashboardPetugas.class);
            startActivity(it);
            finish();
        }
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username=(EditText)findViewById(R.id.etUsername);
        password=(EditText)findViewById(R.id.etPassword);
        sharedpreferences = getSharedPreferences(helper.MyPREFERENCES, Context.MODE_PRIVATE);


    }
    protected void onResume()
    {
        super.onResume();
        if(helper.validateLogin()){
            //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
            Intent it = new Intent(this, DashboardPetugas.class);
            startActivity(it);
            finish();
        }
    }

    public void login(View view) {

        final String uName  = username.getText().toString().trim();
        final String uPass  = password.getText().toString().trim();

        class LoginUser extends AsyncTask<String,Void, String>{
            ProgressDialog loading;
            Server server = new Server();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
               // Log.d(Login.class.getSimpleName(), s);

                try {

                    JSONObject jObj = new JSONObject(s);
                    //Log.d(Login.class.getSimpleName(), jObj.toString());
                    String status =  jObj.getString("status");
                    //Log.d(Login.class.getSimpleName(), status);
                    if(status.equals("1")){
                        String username = jObj.getString("username");
                        String id_profile = jObj.getString("id");
                        //Log.d(Login.class.getSimpleName(), username+"--"+id_profile);

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(helper.Name, username);
                        editor.putString(helper.ID, id_profile);
                        editor.commit();
                        Intent it = new Intent(Login.this, DashboardPetugas.class);
                        startActivity(it);
                        finish();

                    }else{
                        Toast.makeText(getApplicationContext(), "Password atau Username salah!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e(Login.class.getSimpleName(), e.toString());
                }

            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("username",params[0]);
                data.put("password",params[1]);

                String result = server.sendPostRequest("/mobile/login.php",data);

                return result;
            }
        }

        LoginUser login = new LoginUser();
        login.execute(uName,uPass);
    }



}
