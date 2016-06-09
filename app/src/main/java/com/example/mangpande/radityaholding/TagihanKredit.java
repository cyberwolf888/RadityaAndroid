package com.example.mangpande.radityaholding;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

public class TagihanKredit extends AppCompatActivity {

    HashMap<String, String> RowData;

    EditText etTotalbayar;

    Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new Helper(TagihanKredit.this);

        Intent intent = getIntent();
        RowData = (HashMap<String, String>)intent.getSerializableExtra("RowData");
        Log.d("RowData :","> " + RowData);

        setContentView(R.layout.activity_tagihan_kredit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvBarang = (TextView) findViewById(R.id.tvBarang);
        TextView tvNama = (TextView) findViewById(R.id.tvNama);
        TextView tvTelp = (TextView) findViewById(R.id.tvTelp);
        TextView tvAlamat = (TextView) findViewById(R.id.tvAlamat);
        TextView tvHutang = (TextView) findViewById(R.id.tvHutang);
        TextView tvSisa = (TextView) findViewById(R.id.tvSisa);
        TextView tvCicilan = (TextView) findViewById(R.id.tvCicilan);
        TextView tvAngsuran = (TextView) findViewById(R.id.tvAngsuran);
        TextView tvBunga = (TextView) findViewById(R.id.tvBunga);
        TextView tvDenda = (TextView) findViewById(R.id.tvDenda);

        etTotalbayar = (EditText) findViewById(R.id.txt_bayar);

        tvBarang.setText(RowData.get(helper.TAG_NAMA_BARANG));
        tvNama.setText(RowData.get(helper.TAG_NAMA_CUST));
        tvTelp.setText(RowData.get(helper.TAG_TELP));
        tvAlamat.setText(RowData.get(helper.TAG_ALAMAT));
        tvHutang.setText(RowData.get(helper.TAG_HARGA));
        tvSisa.setText(RowData.get(helper.TAG_SISA));
        tvCicilan.setText(RowData.get(helper.TAG_LAMA_CICILAN)+" bulan");
        tvAngsuran.setText(RowData.get(helper.TAG_ANGSURAN_KE));
        tvBunga.setText(helper.formatNumber(Integer.valueOf(RowData.get(helper.TAG_BUNGA))));
        tvDenda.setText(helper.formatNumber(Integer.valueOf(RowData.get(helper.TAG_DENDA))));

        etTotalbayar.setText(RowData.get(helper.TAG_TOTAL_BAYAR));

        Button btn_tagih = (Button) findViewById(R.id.btn_tagih);

        btn_tagih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagih();
            }
        });

    }

    private void tagih(){
        String bayar = etTotalbayar.getText().toString();
        TagihTask mTagihTask = new TagihTask(bayar);
        mTagihTask.execute((Void) null);
    }

    public class TagihTask extends AsyncTask<Void, Void, Void> {

        private final String mBayar;
        private String jsonStr;
        ProgressDialog pDialog;
        TagihTask(String bayar) {
            mBayar = bayar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(TagihanKredit.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            Server webreq = new Server();
            HashMap<String, String> data = new HashMap<String,String>();
            data.put("id_petugas",helper.getUserID());
            data.put("id_kredit",RowData.get(helper.TAG_ID_KREDIT));
            data.put("total_bayar",mBayar);
            data.put("bunga",RowData.get(helper.TAG_BUNGA));
            data.put("denda",RowData.get(helper.TAG_DENDA));
            data.put("angsuran_ke",RowData.get(helper.TAG_ANGSURAN_KE));

            Log.d("Data: ", "> " + data);

            jsonStr = webreq.sendPostRequest("/mobile/bayar_tagihan.php", data);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Log.d("Response: ", "> " + jsonStr);

            if(jsonStr != null && !jsonStr.isEmpty()){
                try{
                    JSONObject jObj = new JSONObject(jsonStr);
                    String status =  jObj.getString("status");
                    if(status.equals("1")){
                        Intent intent = new Intent(getApplicationContext(), Tagihan.class);
                        //intent.putExtra("RowData", RowData);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Gagal menyimpan data pada server!", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e) {
                    Log.e(Login.class.getSimpleName(), e.toString());
                }
            }else{
                Toast.makeText(getApplicationContext(), "Gagal menyambung ke server, silahkan coba lagi.", Toast.LENGTH_LONG).show();
            }
            // Dismiss the progress dialog
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }
        }

    }

}
