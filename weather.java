package com.example.clima;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class weather extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_control);
        ImageButton back = (ImageButton) findViewById(R.id.left);
      final EditText editText=(EditText)findViewById(R.id.entercity);
        back.setOnClickListener(new View.OnClickListener() {
            // When the button is pressed/clicked, it will run the code below
            @Override
            public void onClick(View v) {
                // Intent is what you use to start another activity
                finish();
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String edit=editText.getText().toString();
                Intent newcity=new Intent(weather.this,MainActivity.class);
                newcity.putExtra("City",edit);
                startActivity(newcity);
                        return false;
            }
        });

    }
}
