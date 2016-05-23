package com.example.mangpande.radityaholding;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    ImageButton ibtagihan;
    ImageButton ibKreditBaru;
    ImageButton ibLihatKredit;
    /*ImageButton ibLogout;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_petugas);

        ibtagihan = (ImageButton) findViewById(R.id.ibTagihan);
        ibKreditBaru = (ImageButton) findViewById(R.id.ibLihatKredit);
        ibLihatKredit = (ImageButton) findViewById(R.id.ibLihatKredit);
       /* ibLogout = (ImageButton) findViewById(R.id.ibLogout );*/

        ibtagihan.setOnClickListener(this);
        ibKreditBaru.setOnClickListener(this);
        ibLihatKredit.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == ibtagihan){
            startActivity(new Intent(this, Tagihan.class));
        }
        if (v == ibKreditBaru){
            startActivity(new Intent(this, KreditBaru.class));
        }
        if (v == ibLihatKredit){
            startActivity(new Intent(this, LihatKredit.class));
        }

    }
}
