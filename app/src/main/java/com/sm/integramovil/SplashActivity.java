package com.sm.integramovil;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        EasySplashScreen config=new EasySplashScreen(SplashActivity.this)
                .withFullScreen()

                .withTargetActivity(LoginActivity.class)
                .withSplashTimeOut(3000)
                .withBackgroundResource(R.color.primary_dark)
                .withLogo(R.drawable.ic_launcher_background)
                .withBeforeLogoText(getString(R.string.app_name))
                .withFooterText(getString(R.string.copyright))
                 ;


        config.getFooterTextView().setTextColor(Color.WHITE);
        config.getFooterTextView().setPadding(0,0,0,10);

        config.getBeforeLogoTextView().setTextSize(TypedValue.COMPLEX_UNIT_DIP,40);
        config.getBeforeLogoTextView().setTextColor(Color.WHITE);
        config.getBeforeLogoTextView().setPadding(0,0,0,200);



        config.getLogo().setMaxHeight(1000);
        config.getLogo().setMaxWidth(1000);

        Typeface pacificoFont = Typer.set(getApplicationContext()).getFont(Font.ROBOTO_REGULAR);
        config.getBeforeLogoTextView().setTypeface(pacificoFont);


        /*txtView2.setTypeface(Typer.set(yourContext).getFont(Font.ROBOTO_CONDENSED_ITALIC));
        txtView3.setTypeface(Typer.set(yourContext).getFont(Font.ROBOTO_THIN));
        txtView4.setTypeface(Typer.set(yourContext).getFont(Font.ROBOTO_BOLD));/
        */
        View view=config.create();
        setContentView(view);

    }
}