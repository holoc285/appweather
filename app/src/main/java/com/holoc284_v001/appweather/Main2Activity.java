package com.holoc284_v001.appweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {

    String namecity = "";
    TextView txtNameCity;
    ListView lvDays;
    WeatherAdapter adapter;
    ArrayList<Weather> arrayWeather;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Init();
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
//        Toast.makeText(this, ""+city, Toast.LENGTH_SHORT).show();
        if (city.isEmpty()){
            namecity = "Hanoi";
            Get7DaysData(namecity);
        }else {
            namecity = city;
            Get7DaysData(namecity);
        }
    }

    private void Init() {
//        imgback = (ImageView) findViewById(R.id.imageViewBack);
        txtNameCity = (TextView) findViewById(R.id.textViewCity2);
        lvDays = (ListView) findViewById(R.id.listViewDay);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        arrayWeather = new ArrayList<>();
        adapter = new WeatherAdapter(Main2Activity.this,arrayWeather);
        lvDays.setAdapter(adapter);
    }

    private void Get7DaysData(final String data) {
        String url1 = "http://api.apixu.com/v1/forecast.json?key=862f25826ef34c3a8fc60958180401&q="+data+"&days=7";
        RequestQueue requestQueue = Volley.newRequestQueue(Main2Activity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectLocation = jsonObject.getJSONObject("location");
                            String name = jsonObjectLocation.getString("name");

                            txtNameCity.setText("Thành Phố : "+name);

                            JSONObject jsonObjectForecast = jsonObject.getJSONObject("forecast");
                            JSONArray jsonArrayForecastday = jsonObjectForecast.getJSONArray("forecastday");
                            for (int i=0;i<jsonArrayForecastday.length();i++){
                                JSONObject jsonObjectList = jsonArrayForecastday.getJSONObject(i);
                                String day = jsonObjectList.getString("date_epoch");
                                long l = Long.valueOf(day);
                                Date date = new Date(l*1000L);
                                SimpleDateFormat format = new SimpleDateFormat("EEEE dd-MM-yyyy");
                                String Day = format.format(date);

                                JSONObject jsonObjectDay = jsonObjectList.getJSONObject("day");
                                String maxtemp = jsonObjectDay.getString("maxtemp_c");
                                String mintemp = jsonObjectDay.getString("mintemp_c");

                                //Lay so nguyen cua nhiet do
                                Double a = Double.valueOf(maxtemp);
                                Double b = Double.valueOf(mintemp);
                                String maxTemp = String.valueOf(a.intValue());
                                String minTemp = String.valueOf(b.intValue());

                                JSONObject jsonObjectCondition = jsonObjectDay.getJSONObject("condition");
                                String status = jsonObjectCondition.getString("text");
                                String icon = jsonObjectCondition.getString("icon");

                                arrayWeather.add(new Weather(Day,status,icon,maxTemp,minTemp));

                            }
                            adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
}
