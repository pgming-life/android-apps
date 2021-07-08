package com.jackbee.glassboxfree;

import com.jackbee.email.Email;
import com.jackbee.glassboxfree.settings.PrefSettings;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Launcher extends Activity implements OnClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout set ***********************************************************************
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.button_open)).setOnClickListener(this);
        ((Button)findViewById(R.id.button_settings)).setOnClickListener(this);
        ((Button)findViewById(R.id.button_valuation)).setOnClickListener(this);
        ((Button)findViewById(R.id.button_apps)).setOnClickListener(this);
        ((Button)findViewById(R.id.button_facebook)).setOnClickListener(this);
        ((Button)findViewById(R.id.button_twitter)).setOnClickListener(this);
        ((Button)findViewById(R.id.button_email)).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.button_open:
                // Wallpaper Open
                Intent iOpen = new Intent();

                try {

                    if(Build.VERSION.SDK_INT > 15) {
                        iOpen.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                        String p = CrystalLiveWallpaperFree.class.getPackage().getName();
                        String c = CrystalLiveWallpaperFree.class.getCanonicalName();
                        iOpen.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(p, c));

                    }
                    else {
                        iOpen.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);

                    }

                }
                catch(ActivityNotFoundException e) {
                    iOpen.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);

                }

                startActivity(iOpen);

                break;

            case R.id.button_settings:
                // Wallpaper Settings
                Intent iSettings = new Intent(getApplicationContext(), PrefSettings.class);
                startActivity(iSettings);
                break;

            case R.id.button_valuation:
                // Wallpaper Valuation
                Uri uriValuation = Uri.parse("https://play.google.com/store/apps/details?id=com.jackbee.glassboxfree");
                Intent iValuation = new Intent(Intent.ACTION_VIEW, uriValuation);
                startActivity(iValuation);
                break;

            case R.id.button_apps:
                // OTOGI Apps
                Uri uriApps = Uri.parse("https://play.google.com/store/apps/developer?id=OTOGI");
                Intent iApps = new Intent(Intent.ACTION_VIEW, uriApps);
                startActivity(iApps);
                break;

            case R.id.button_facebook:
                // OTOGI Applications facebook
                Uri uriFacebook = Uri.parse("https://www.facebook.com/otogi.jackbee");
                Intent iFacebook = new Intent(Intent.ACTION_VIEW, uriFacebook);
                startActivity(iFacebook);
                break;

            case R.id.button_twitter:
                // otogiApps twitter
                Uri uriTwitter = Uri.parse("https://twitter.com/otogiApps");
                Intent iTwitter = new Intent(Intent.ACTION_VIEW, uriTwitter);
                startActivity(iTwitter);
                break;

            case R.id.button_email:
                // JACKBee e-mail
                Intent iEmail = new Intent(getApplicationContext(), Email.class);
                startActivity(iEmail);
                break;

        }

    }

}
