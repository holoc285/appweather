package com.holoc284_v001.appweather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import static com.holoc284_v001.appweather.MainActivity.GetCurrentWeatherData;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    ImageView imgSearch;
    Place place;
    Marker mar;
    ImageView view;
    TextView txtTitle;
    TextView txtTemp;
    TextView txtStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initView();
        MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

    }

    private void initView() {
        imgSearch = (ImageView) findViewById(R.id.imgbtnsearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(MapsActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                } catch (GooglePlayServicesNotAvailableException e) {
                }
            }
        });

    }
    class CustomInfoWindow implements GoogleMap.InfoWindowAdapter{
        View mWindow;

        CustomInfoWindow(){
            mWindow = getLayoutInflater().inflate(R.layout.info_window, null);
        }
        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        private void render(Marker marker, View mWindow) {
            view =  mWindow.findViewById(R.id.imgIcon);
            txtTitle =  mWindow.findViewById(R.id.tvCity);
            txtTemp =  mWindow.findViewById(R.id.tvTemp);
            txtStatus =  mWindow.findViewById(R.id.tvStatus);

            GetCurrentWeatherData(place.getName().toString(),getApplicationContext());
            Picasso.with(MapsActivity.this).load("http:"+ MainActivity.icon1 ).into(view);
            txtTitle.setText(mar.getTitle());
            txtTemp.setText(MainActivity.temp1 + " C ");
            txtStatus.setText(MainActivity.status1);

        }
        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                place = PlaceAutocomplete.getPlace(this, data);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 10));

                mar = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                        .position(place.getLatLng())
                        .title(place.getName().toString())

                );
                mar.showInfoWindow();

            }else if (resultCode == PlaceAutocomplete.RESULT_ERROR){
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(this, status.getStatusMessage() , Toast.LENGTH_SHORT).show();
            }else if (resultCode == RESULT_CANCELED){
            }

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter( new CustomInfoWindow());

        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }

    }


}
