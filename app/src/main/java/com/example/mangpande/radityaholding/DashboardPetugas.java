package com.example.mangpande.radityaholding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class DashboardPetugas extends AppCompatActivity {
    ImageButton ibTagihan;
    ImageButton ibLihatKredit;
    Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new Helper(DashboardPetugas.this);
        if(!helper.validateLogin()){
            startActivity(new Intent(this, Login.class));
        }
        setContentView(R.layout.activity_dashboard_petugas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ibTagihan = (ImageButton) findViewById(R.id.ibTagihan);
        ibLihatKredit = (ImageButton) findViewById(R.id.ibLihatKredit);

        ibTagihan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashboardPetugas.this, Tagihan.class);
                startActivity(it);
            }
        });

        ibLihatKredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashboardPetugas.this, LihatKredit.class);
                startActivity(it);
            }
        });
    }
    @Override
    protected void onResume()
    {
        super.onResume();

        if(!helper.validateLogin()){
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
            helper.logout();
            startActivity(new Intent(this, Login.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
