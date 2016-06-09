package com.example.mangpande.radityaholding;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
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

public class Detail extends AppCompatActivity  {

    ListView listView;

    HashMap<String, String> RowData;

    Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new Helper(Detail.this);

        Intent intent = getIntent();
        RowData = (HashMap<String, String>)intent.getSerializableExtra("RowData");
        Log.d("RowData :","> " + RowData);

        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvBarang = (TextView) findViewById(R.id.tvBarang);
        TextView tvNama = (TextView) findViewById(R.id.tvNama);
        TextView tvTelp = (TextView) findViewById(R.id.tvTelp);
        TextView tvAlamat = (TextView) findViewById(R.id.tvAlamat);
        TextView tvHutang = (TextView) findViewById(R.id.tvHutang);
        TextView tvSisa = (TextView) findViewById(R.id.tvSisa);
        TextView tvCicilan = (TextView) findViewById(R.id.tvCicilan);
        TextView tvStatus = (TextView) findViewById(R.id.tvStatus);

        tvBarang.setText(RowData.get(helper.TAG_NAMA_BARANG));
        tvNama.setText(RowData.get(helper.TAG_NAMA_CUST));
        tvTelp.setText(RowData.get(helper.TAG_TELP));
        tvAlamat.setText(RowData.get(helper.TAG_ALAMAT));
        tvHutang.setText(RowData.get(helper.TAG_HARGA));
        tvSisa.setText(RowData.get(helper.TAG_SISA));
        tvCicilan.setText(RowData.get(helper.TAG_LAMA_CICILAN)+" bulan");
        if(RowData.get(helper.TAG_STATUS).equals("1")){
            tvStatus.setText("Proses");
        }else if (RowData.get(helper.TAG_STATUS).equals("2")){
            tvStatus.setText("Lunas");
        }else{
            tvStatus.setText(RowData.get(helper.TAG_STATUS));
        }

        listView = (ListView) findViewById(R.id.mList_angsuran);
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        setListViewHeightBasedOnChildren(listView);

        new GetListAngsuran().execute();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, Toolbar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private class GetListAngsuran extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> itemList;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Detail.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            Server webreq = new Server();
            HashMap<String, String> data = new HashMap<String,String>();
            data.put("id_kredit",RowData.get(helper.TAG_ID_KREDIT));

            // Making a request to url and getting response
            String jsonStr = webreq.sendPostRequest("/mobile/listAngsuran.php", data);

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
            Log.d("Response: ", "> " + itemList);
            if(itemList != null){
                ListAdapter adapter = new SimpleAdapter(
                        Detail.this,
                        itemList,
                        R.layout.list_angsuran,
                        new String[]{helper.TAG_TGL_ANGSURAN, helper.TAG_BAYAR_ANGSURAN},
                        new int[]{R.id.tgl_angsuran, R.id.bayar_angsuran}
                );

                listView.setAdapter(adapter);
            }else{
                listView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "List angsuran kosong!", Toast.LENGTH_LONG).show();
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
                JSONArray listitem = jsonObj.getJSONArray(helper.TAG_LIST_ANGSURAN);

                // looping through All Students
                for (int i = 0; i < listitem.length(); i++) {
                    JSONObject c = listitem.getJSONObject(i);

                    String tgl_angsuran = c.getString(helper.TAG_TGL_ANGSURAN);
                    String bayar_angsuran = helper.formatNumber(Integer.valueOf(c.getString(helper.TAG_BAYAR_ANGSURAN)));

                    // tmp hashmap for single student
                    HashMap<String, String> dataList = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    dataList.put(helper.TAG_TGL_ANGSURAN, tgl_angsuran);
                    dataList.put(helper.TAG_BAYAR_ANGSURAN, bayar_angsuran);

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
