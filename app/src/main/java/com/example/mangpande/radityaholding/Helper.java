package com.example.mangpande.radityaholding;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Master on 5/23/2016.
 */
public class Helper extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "username";
    public static final String Pass = "password";
    public static final String ID = "id_profile";

    SharedPreferences sharedpreferences;
    public Helper(Context mContext){
        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public boolean validateLogin(){

        if(sharedpreferences.contains(Name)){
            return true;
        }
        return false;
    }

    public String getUserID(){
        String id=sharedpreferences.getString(ID,"");

        return id;
    }

    public String getUserName(){
        String name=sharedpreferences.getString(Name,"");

        return name;
    }

    public String formatNumber(Integer number){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        kursIndonesia.setMaximumFractionDigits(0);

        return kursIndonesia.format(number);
    }

    public void logout(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }
}
