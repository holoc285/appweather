package com.holoc284_v001.appweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by holoc on 1/4/2018.
 */

public class WeatherAdapter extends BaseAdapter {
    Context context;
    ArrayList<Weather> arrayList;

    public WeatherAdapter(Context context, ArrayList<Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.row_layout,null);

        Weather weather = (Weather) getItem(i);

        ImageView imgStatus = view.findViewById(R.id.row_imageViewStatus);
        TextView txtDay = view.findViewById(R.id.row_textViewDay);
        TextView txtMaxTemp = view.findViewById(R.id.row_textViewMaxTemp);
        TextView txtMinTemp = view.findViewById(R.id.row_textViewMinTemp);
        TextView txtStatus = view.findViewById(R.id.row_textViewStatus);

        txtDay.setText(weather.Day);
        txtMaxTemp.setText(weather.MaxTemp+" C");
        txtMinTemp.setText(weather.MinTemp+" C");
        txtStatus.setText(weather.Status);
        Picasso.with(context).load("http:"+weather.Image).into(imgStatus);

//        Picasso.with(context).load("http://openweathermap.org/img/w/"+weather.Image+".png").into(imgStatus);

        return view;
    }
}
