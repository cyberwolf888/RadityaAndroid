package com.example.mangpande.radityaholding;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Tagihan extends AppCompatActivity {

    ListView listView;
    Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new Helper(Tagihan.this);

        setContentView(R.layout.activity_tagihan);

        listView = (ListView) findViewById(R.id.mList_tagihan);
        new GetList().execute();
    }

    private class GetList extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> itemList;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Tagihan.this);
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

            // Making a request to url and getting response
            String jsonStr = webreq.sendPostRequest("/mobile/listPenagihan.php", data);

            Log.d("Response: ", "> " + jsonStr);

            itemList = ParseJSON(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            /**
             * Updating parsed JSON data into ListView
             * */
            if(itemList != null){
                ListAdapter adapter = new SimpleAdapter(
                        Tagihan.this,
                        itemList,
                        R.layout.list_tagihan,
                        new String[]{helper.TAG_NAMA_BARANG, helper.TAG_NAMA_CUST, helper.TAG_TELP, helper.TAG_ALAMAT},
                        new int[]{R.id.nama_brg, R.id.nama_cust, R.id.telp, R.id.alamat}
                );

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        //Log.d("Cicked: ", "> " + position);

                        HashMap<String, String> selected = itemList.get(position);

                        Log.d("Selected :","> " + selected);

                        Intent intent = new Intent(getApplicationContext(), TagihanKredit.class);
                        intent.putExtra("RowData", selected);
                        startActivity(intent);
                        finish();
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(), "List tagihan kosong!", Toast.LENGTH_LONG).show();
            }

            // Dismiss the progress dialog
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }

        }

    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> itemList = new ArrayList<HashMap<String, String>>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray listitem = jsonObj.getJSONArray(helper.TAG_LIST_TAGIHAN);

                // looping through All Students
                for (int i = 0; i < listitem.length(); i++) {
                    JSONObject c = listitem.getJSONObject(i);

                    String id_kredit = c.getString(helper.TAG_ID_KREDIT);
                    String nama_cst = c.getString(helper.TAG_NAMA_CUST);
                    String nama_brg = c.getString(helper.TAG_NAMA_BARANG);
                    String harga = helper.formatNumber(Integer.valueOf(c.getString(helper.TAG_HARGA)));
                    String telah_bayar = helper.formatNumber(Integer.valueOf(c.getString(helper.TAG_TELAH_BAYAR)));
                    String sisa = helper.formatNumber(Integer.valueOf(c.getString(helper.TAG_SISA)));
                    String lama_cicilan = c.getString(helper.TAG_LAMA_CICILAN);
                    String tgl_kredit = c.getString(helper.TAG_TGL_KREDIT);
                    String tlp = c.getString(helper.TAG_TELP);
                    String alamat = c.getString(helper.TAG_ALAMAT);
                    String denda = c.getString(helper.TAG_DENDA);
                    String bunga = c.getString(helper.TAG_BUNGA);
                    String angsuran_ke = c.getString(helper.TAG_ANGSURAN_KE);
                    String total_bayar = c.getString(helper.TAG_TOTAL_BAYAR);

                    // tmp hashmap for single student
                    HashMap<String, String> dataList = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    dataList.put(helper.TAG_ID_KREDIT, id_kredit);
                    dataList.put(helper.TAG_NAMA_CUST, nama_cst);
                    dataList.put(helper.TAG_NAMA_BARANG, nama_brg);
                    dataList.put(helper.TAG_HARGA, harga);
                    dataList.put(helper.TAG_TELAH_BAYAR, telah_bayar);
                    dataList.put(helper.TAG_SISA, sisa);
                    dataList.put(helper.TAG_LAMA_CICILAN, lama_cicilan);
                    dataList.put(helper.TAG_TGL_KREDIT, tgl_kredit);
                    dataList.put(helper.TAG_TELP, tlp);
                    dataList.put(helper.TAG_ALAMAT, alamat);
                    dataList.put(helper.TAG_DENDA, denda);
                    dataList.put(helper.TAG_BUNGA, bunga);
                    dataList.put(helper.TAG_ANGSURAN_KE, angsuran_ke);
                    dataList.put(helper.TAG_TOTAL_BAYAR, total_bayar);

                    // adding student to students list
                    itemList.add(dataList);
                }
                return itemList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
    }
}
