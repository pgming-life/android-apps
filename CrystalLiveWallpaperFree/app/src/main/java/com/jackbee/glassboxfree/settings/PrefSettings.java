package com.jackbee.glassboxfree.settings;

import com.jackbee.email.Email;
import com.jackbee.glassboxfree.CrystalLiveWallpaperFree;
import com.jackbee.glassboxfree.R;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;


public class PrefSettings extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_NAME = "CRYSTAL";


    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        getPreferenceManager().setSharedPreferencesName(
                PrefSettings.KEY_NAME);
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        setPreferenceScreen(createPreferenceHierarchy());

    }


    private PreferenceScreen createPreferenceHierarchy() {

        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);

        try {
//********** OTOGI
            PreferenceCategory otogiCate = new PreferenceCategory(this);
            otogiCate.setTitle(R.string.otogi);
            root.addPreference(otogiCate);

            // OTOGI ＞ Link
            Preference otogiLink = new Preference(this);
            otogiLink.setLayoutResource(R.layout.otogi_link);
            otogiLink.setEnabled(false);
            root.addPreference(otogiLink);

            // APPS
            Preference appsLink = new Preference(this);
            appsLink.setLayoutResource(R.layout.apps_link);
            appsLink.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // pub:OTOGI
                    Uri uriApps = Uri.parse("https://play.google.com/store/apps/developer?id=OTOGI");
                    Intent iApps = new Intent(Intent.ACTION_VIEW, uriApps);
                    startActivity(iApps);

                    return false;

                }

            });
            root.addPreference(appsLink);

            // FACEBOOK
            Preference facebookLink = new Preference(this);
            facebookLink.setLayoutResource(R.layout.facebook_link);
            facebookLink.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // OTOGI Appslications facebook
                    Uri uriFacebook = Uri.parse("https://www.facebook.com/otogi.jackbee");
                    Intent iFacebook = new Intent(Intent.ACTION_VIEW, uriFacebook);
                    startActivity(iFacebook);

                    return false;

                }

            });
            root.addPreference(facebookLink);

            // TWITTER
            Preference twitterLink = new Preference(this);
            twitterLink.setLayoutResource(R.layout.twitter_link);
            twitterLink.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // otogiApps twitter
                    Uri uriTwitter = Uri.parse("https://twitter.com/otogiApps");
                    Intent iTwitter = new Intent(Intent.ACTION_VIEW, uriTwitter);
                    startActivity(iTwitter);

                    return false;

                }

            });
            root.addPreference(twitterLink);

            // E-MAIL
            Preference emailLink = new Preference(this);
            emailLink.setLayoutResource(R.layout.email_link);
            emailLink.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // JACKBee e-mail
                    Intent iEmail = new Intent(getApplicationContext(), Email.class);
                    startActivity(iEmail);

                    return false;

                }

            });
            root.addPreference(emailLink);

            PreferenceCategory otogiLinkSpace = new PreferenceCategory(this);
            root.addPreference(otogiLinkSpace);

//********** Wallpaper
            PreferenceCategory wallpaperCate = new PreferenceCategory(this);
            wallpaperCate.setTitle(R.string.main_wallpaper);
            root.addPreference(wallpaperCate);

            Preference setWallpaper = new Preference(this);
            setWallpaper.setTitle(R.string.set);
            setWallpaper.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent iWallpaper = new Intent();

                    try {

                        if(Build.VERSION.SDK_INT > 15) {
                            iWallpaper.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                            String p = CrystalLiveWallpaperFree.class.getPackage().getName();
                            String c = CrystalLiveWallpaperFree.class.getCanonicalName();
                            iWallpaper.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(p, c));

                        }
                        else {
                            iWallpaper.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);

                        }

                    }
                    catch(ActivityNotFoundException e) {
                        // Fallback to the old method, some devices greater than SDK 15 are crashing
                        iWallpaper.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);

                    }

                    startActivity(iWallpaper);

                    return true;

                }

            });
            root.addPreference(setWallpaper);

            PreferenceCategory wallpaperSpace = new PreferenceCategory(this);
            root.addPreference(wallpaperSpace);

//********** OTHER
            PreferenceCategory otherCate = new PreferenceCategory(this);
            otherCate.setTitle(R.string.other);
            root.addPreference(otherCate);

            // Valuation
            Preference valuationLink = new Preference(this);
            valuationLink.setTitle(R.string.valuation_link);
            valuationLink.setSummary(R.string.please_rate_if_good);
            valuationLink.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Uri uriValuation = Uri.parse("https://play.google.com/store/apps/details?id=com.jackbee.glassboxfree");
                    Intent iValuation = new Intent(Intent.ACTION_VIEW, uriValuation);
                    startActivity(iValuation);

                    return false;

                }

            });
            root.addPreference(valuationLink);

            PreferenceCategory otherSpace = new PreferenceCategory(this);
            root.addPreference(otherSpace);



        }
        catch(Exception ex) {

        }
        return root;

    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {

        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);

        super.onDestroy();

    }


    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {

    }

}
