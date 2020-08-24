package com.sm.integramovil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfiguracionActivity extends AppCompatActivity {
    public static String PREFS_NAME = "integramovil";
    Button btnGuardarApiUrl;
    EditText txtApiUrl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        btnGuardarApiUrl = (Button) findViewById(R.id.btnGuardarApiUrl);
        txtApiUrl = (EditText) findViewById(R.id.txtApiUrl);

        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String url=pref.getString("apiurl", "http://201.217.136.126/").toString();
        txtApiUrl.setText(url);

        btnGuardarApiUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences(
                        PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("apiurl", txtApiUrl.getText().toString());
                editor.apply();

                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                "Datos guardados", Toast.LENGTH_SHORT);

                toast1.show();

            }
        });
    }

}