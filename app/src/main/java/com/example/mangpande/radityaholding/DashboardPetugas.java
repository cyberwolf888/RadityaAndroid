package com.example.mangpande.radityaholding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DashboardPetugas extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "username";
    public static final String Pass = "password";
    public static final String ID = "id_profile";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_petugas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardPetugas.this, TambahKredit.class);
                startActivity(i);
            }
        });*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(sharedpreferences.contains(Name)){
            String name=sharedpreferences.getString(Name,"");
            String pass=sharedpreferences.getString(Pass,"");
            String id=sharedpreferences.getString(ID,"");
            Toast.makeText(getApplicationContext(), name+" "+pass+" "+id, Toast.LENGTH_LONG).show();
        }else{
            startActivity(new Intent(this, Login.class));
        }
    }
    protected void onResume()
    {
        super.onResume();

        if(!sharedpreferences.contains(Name)){
            //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Login.class));
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(this, Login.class));
        }

        return super.onOptionsItemSelected(item);
    }
    public void tagihan (View view){

        startActivity(new Intent(DashboardPetugas.this, TagihanKredit.class));
    }
    public void lihatkredit (View view){

        startActivity(new Intent(DashboardPetugas.this, LihatKredit.class));
    }
}
