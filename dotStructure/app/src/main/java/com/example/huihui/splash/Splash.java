package com.example.huihui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;

/**
 * Created by HuiHui on 3/23/2015.
 */
public class Splash extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 5000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.layout);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
//                Intent i = new Intent(Splash.this, MainActivity.class);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                Splash.this.startActivity(i);
//                Intent mainIntent = new Intent(Splash.this,MainActivity.class);
//                Splash.this.startActivity(mainIntent);
//                Splash.this.finish();
                Intent i = new Intent(Splash.this, MainActivity.class);
                Splash.this.startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}