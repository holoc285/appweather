package com.holoc284_v001.appweather;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText edtSearch;
    Button btnSearch,btnChangeDay,btnMaps;
    static ImageView imgHinh;
    static TextView txtCity;
    static TextView txtCountry;
    static TextView txtTemp;
    static TextView txtStatus;
    static TextView txtHumidity;
    static TextView txtCloud;
    static TextView txtMill;
    static TextView txtDay;
    String City="";
    public static String status1="",temp1="", icon1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        if (isConnected()==true){

        }else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.idLayout), "No Internet Connection", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
            snackbar.show();
        }

        //Default
        GetCurrentWeatherData("Hanoi", MainActivity.this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                if (city.isEmpty()){
                    City = "Hanoi";
                    GetCurrentWeatherData(City, MainActivity.this);
                    Toast.makeText(MainActivity.this, "Vui lòng nhập vào tên thành phố!", Toast.LENGTH_SHORT).show();
                }else {
                    City = city;
                    GetCurrentWeatherData(City, MainActivity.this);
                }
            }
        });
        btnChangeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("name",city);
                startActivity(intent);
            }
        });
        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MapsActivity.class));
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static String GetCurrentWeatherData(String data, final Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url1 = "http://api.apixu.com/v1/current.json?key=862f25826ef34c3a8fc60958180401&q="+data+"";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectLocation = jsonObject.getJSONObject("location");
                            String name = jsonObjectLocation.getString("name");

                            String country = jsonObjectLocation.getString("country");

                            JSONObject jsonObjectCurrent = jsonObject.getJSONObject("current");
                            String time = jsonObjectCurrent.getString("last_updated");
                            String temp = jsonObjectCurrent.getString("temp_c");
                            String wind = jsonObjectCurrent.getString("wind_mph");
                            String humidiy = jsonObjectCurrent.getString("humidity");
                            String cloud = jsonObjectCurrent.getString("cloud");

                            JSONObject jsonObjectCondition = jsonObjectCurrent.getJSONObject("condition");
                            String status = jsonObjectCondition.getString("text");
                            String icon = jsonObjectCondition.getString("icon");

                            txtCity.setText("Thành Phố : "+name);
                            txtCountry.setText("Quốc Gia : "+country);
                            txtDay.setText(time);
                            txtStatus.setText(status);
                            txtTemp.setText(temp+" C");
                            txtMill.setText(wind+" m/s");
                            txtCloud.setText(cloud+" %");
                            txtHumidity.setText(humidiy+" %");
                            Picasso.with(context).load("http:"+icon).into(imgHinh);

                            temp1 = temp;
                            status1 = status;
                            icon1 = icon;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Không tìm thấy thành phố!!!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
        return url1;
    }

    private void AnhXa(){
        edtSearch       = (EditText) findViewById(R.id.edittextSearch);

        btnSearch       = (Button)   findViewById(R.id.buttonSearch);
        btnChangeDay    = (Button)   findViewById(R.id.buttonChangeDay);
        btnMaps         = (Button)   findViewById(R.id.buttonMap);

        imgHinh         = (ImageView)findViewById(R.id.imageViewIcon);

        txtCity         = (TextView) findViewById(R.id.textViewCity);
        txtCountry      = (TextView) findViewById(R.id.textViewCountry);
        txtCloud        = (TextView) findViewById(R.id.textViewCloud);
        txtTemp         = (TextView) findViewById(R.id.textViewTemp);
        txtStatus       = (TextView) findViewById(R.id.textViewStatus);
        txtHumidity     = (TextView) findViewById(R.id.textViewHumidity);
        txtMill         = (TextView) findViewById(R.id.textViewMill);
        txtDay          = (TextView) findViewById(R.id.textViewDay);
    }
}
