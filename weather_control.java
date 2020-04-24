package com.example.clima;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class weather_control{
    private String mtemp;
    private String mcity;
    private String mIcon;
    private int mconndition;

    public static weather_control fromJson(JSONObject jsonObject){
        weather_control weather=new weather_control();
        try {

            weather.mcity = jsonObject.getString("name");
            weather.mconndition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weather.mIcon=upicon(weather.mconndition);
            double templ=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int tem=(int) Math.rint(templ);
            weather.mtemp=Integer.toString(tem);
        }
        catch (JSONException e){
            e.printStackTrace();
            return null;
        }


    return weather;
    }
    private static String upicon(int conndition){
        if (conndition>=0 && conndition<300) return "tstorm1";
        else if (conndition<500) return "light_rain";
        else if (conndition<600) return "shower3";
        else if (conndition<=700) return "snow4";
        else if (conndition<=771)return "fog";
        else if (conndition<800) return "tstorm3";
        else if(conndition==800) return "sunny";
        else if (conndition<=804) return "cloudy2";
        else if (conndition>=900 && conndition<=902)return "tstorm3";
        else if (conndition==903) return "snow5";
        else if (conndition==904) return "sunny";
        else if (conndition>=905 && conndition <=1000) return "tstorm3";
        return "dunno";
    }

    public String getMtemp() {
        return mtemp+"°";
    }

    public String getMcity() {
        return mcity;
    }

    public String getmIcon() {
        return mIcon;
    }
}
