package com.example.mangpande.radityaholding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LihatKredit extends AppCompatActivity implements View.OnClickListener{

    Button btLihat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_kredit);

        btLihat = (Button) findViewById(R.id.btLihat);

        btLihat.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btLihat){
            startActivity(new Intent(this, Detail.class));
        }

    }
}
