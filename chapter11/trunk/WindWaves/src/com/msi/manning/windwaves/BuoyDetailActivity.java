package com.msi.manning.windwaves;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.msi.manning.windwaves.data.BuoyData;

// ENHANCE build charts from the realtime data (http://www.ndbc.noaa.gov/data/realtime2/41002.txt) or scrape
// ENHANCE hook in with other NOAA data - http://www.weather.gov/rss/

/**
 * BuoyDetail Activity for WindWaves.
 * 
 * @author charliecollins
 * 
 */
public class BuoyDetailActivity extends Activity {

    private static final String CLASSTAG = BuoyDetailActivity.class.getSimpleName();

    public static final String BUOY_DETAIL_URL = "http://www.ndbc.noaa.gov/station_page.php?station=";

    public static BuoyData buoyData = null;

    private Button buttonWeb;
    private TextView title;
    private TextView location;
    private TextView date;
    private TextView airTemp;
    private TextView waterTemp;
    private TextView atmoPress;
    private TextView atmoTend;
    private TextView windDir;
    private TextView windSpeed;
    private TextView windGust;
    private TextView waveHeight;
    private TextView wavePeriod;

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        Log.v(Constants.LOGTAG, " " + BuoyDetailActivity.CLASSTAG + " onCreate");

        this.setContentView(R.layout.buoydetail_activity);

        this.buttonWeb = (Button) this.findViewById(R.id.button_web);
        this.title = (TextView) this.findViewById(R.id.bd_title);
        this.location = (TextView) this.findViewById(R.id.bd_location);
        this.date = (TextView) this.findViewById(R.id.bd_date);
        this.airTemp = (TextView) this.findViewById(R.id.bd_air_temp);
        this.waterTemp = (TextView) this.findViewById(R.id.bd_water_temp);
        this.atmoPress = (TextView) this.findViewById(R.id.bd_atmo_press);
        this.atmoTend = (TextView) this.findViewById(R.id.bd_atmo_tend);
        this.windDir = (TextView) this.findViewById(R.id.bd_wind_dir);
        this.windSpeed = (TextView) this.findViewById(R.id.bd_wind_speed);
        this.windGust = (TextView) this.findViewById(R.id.bd_wind_gust);
        this.waveHeight = (TextView) this.findViewById(R.id.bd_wave_height);
        this.wavePeriod = (TextView) this.findViewById(R.id.bd_wave_period);
        
        this.title.setText(buoyData.title);
        this.location.setText("Location:" + buoyData.location);
        this.date.setText("Data Poll Date: " + (buoyData.dateString != null ? buoyData.dateString : "NA"));
        this.airTemp.setText("Air Temp: " + (buoyData.airTemp != null ? buoyData.airTemp : "NA"));
        this.waterTemp.setText("Water Temp: " + (buoyData.waterTemp != null ? buoyData.waterTemp : "NA"));
        this.atmoPress.setText("Barometric Press: " + (buoyData.atmoPressure != null ? buoyData.atmoPressure : "NA"));
        this.atmoTend.setText("Barometric Trend: " + (buoyData.atmoPressureTendency != null ? buoyData.atmoPressureTendency : "NA"));
        this.windDir.setText("Wind Direction: " + (buoyData.windDirection != null ? buoyData.windDirection : "NA"));
        this.windSpeed.setText("Wind Speed: " + (buoyData.windSpeed != null ? buoyData.windSpeed : "NA"));
        this.windGust.setText("Wind Gust: " + (buoyData.windGust != null ? buoyData.windGust : "NA"));
        this.waveHeight.setText("Wave Height: " + (buoyData.waveHeight != null ? buoyData.waveHeight : "NA"));
        this.wavePeriod.setText("Wave Period: " + (buoyData.wavePeriod != null ? buoyData.wavePeriod : "NA"));        

        this.buttonWeb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BuoyDetailActivity.this.startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse(BuoyDetailActivity.buoyData.link)));
            };
         });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(Constants.LOGTAG, " " + BuoyDetailActivity.CLASSTAG + " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}