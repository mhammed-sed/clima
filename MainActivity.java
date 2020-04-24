package com.example.clima;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity<v> extends AppCompatActivity {
    final int REQUEST_CODE=123;
    //Todo declare the const here
    long minitime = 5000;
    float minidestnt = 1000;

    // todo:url of site web whiche use to give us the weather of place

    String weather = "http://api.openweathermap.org/data/2.5/weather";

    String APPID="e72ca729af228beabd5d20e3b7749713";

    String private_location = LocationManager.GPS_PROVIDER;

       //this declaration below for interface so it has a connection with @activity_main.xml


    TextView temp=(TextView)findViewById(R.id.weatherSymbolIV);

    TextView city=(TextView)findViewById(R.id.city);

    ImageView weatherIMG=(ImageView)findViewById(R.id.dunno);

    //Todo declare mlocation and locationlisten

    LocationManager mlocation;

    LocationListener locationlisten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton changeCity=(ImageButton)findViewById(R.id.changeCityButton);
        changeCity.setOnClickListener(new View.OnClickListener() {
            // When the button is pressed/clicked, it will run the code below
            @Override
            public void onClick(View v) {
                // Intent is what you use to start another activity
                Intent intent;
                intent = new Intent(MainActivity.this, weather_control.class);
                startActivity(intent);
            }
        });



     }

    @Override
    protected void onResume() {
        super.onResume();
        Intent myIntent=getIntent();
        String city=myIntent.getStringExtra("City");
        if(city != null){
            RequestParams params=new RequestParams();
            params.put("q",city);
            params.put("appid",APPID);
            letsdoSomNet(params);
        }
        else {
            getrequeslocation();
           }
        }

    @Override
    protected void onPause() {
        super.onPause();
       if (mlocation != null) mlocation.removeUpdates(locationlisten);
    }

    private void getrequeslocation() {
        mlocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationlisten = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String longitude = String.valueOf(location.getLongitude());

                String latitude = String.valueOf(location.getAltitude());

                Log.d("clima","changedlovlocation "+longitude+"/ "+latitude);
                RequestParams PRAMS= new RequestParams();
                PRAMS.put("lat",latitude);
                PRAMS.put("lon",longitude);
                PRAMS.put("api",APPID);
                letsdoSomNet(PRAMS);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
              // TODO: Consider calling
              //    Activity#requestPermissions
              // here to request the missing permissions, and then overriding
              //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
              //                                          int[] grantResults)
              // to handle the case where the user grants the permission. See the documentation
              // for Activity#requestPermissions for more details.

                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
                return;
            }
        }

        mlocation.requestLocationUpdates(private_location, minitime, minidestnt, locationlisten);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Log.d("clima","onRequestPermissionsResult:");

                getrequeslocation();
            }
        }
    }
    private void letsdoSomNet(RequestParams params){
        AsyncHttpClient client=new AsyncHttpClient();

        client.get(weather,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("clima","success:JSON:"+response.toString());

                Toast.makeText(MainActivity.this,"request success",Toast.LENGTH_SHORT).show();

                weather_control weathr=weather_control.fromJson(response);
                upUi(weathr);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Toast.makeText(MainActivity.this,"request failure",Toast.LENGTH_SHORT).show();
            }

        });

    }
    private void upUi(weather_control weather){
        temp.setText(weather.getMtemp());
        city.setText(weather.getMcity());
        int res=getResources().getIdentifier(weather.getmIcon(),"drawable",getPackageName());
        weatherIMG.setImageResource(res);
    }

}
