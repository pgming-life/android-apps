package com.jackbee.allstars.settings;

import com.jackbee.allstars.AllStarsLiveWallpaper;
import com.jackbee.allstars.R;
import com.jackbee.email.Email;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;


public class PrefSettings extends PreferenceActivity implements 
	SharedPreferences.OnSharedPreferenceChangeListener {
	
	public static final String KEY_NAME                 = "COSMOS";
	public static final String KEY_PLANETS              = "PLANETS";
	public static final String KEY_UNIVERSES            = "UNIVERSES";
	public static final String KEY_CONSTELLATION_SWITCH = "CONSTELLATION_SWITCH";
	public static final String KEY_STAR_COUNT           = "STAR_COUNT";
	public static final String KEY_PLANET_SPEED         = "PLANET_SPEED";
	public static final String KEY_UNIVERSE_SPEED       = "UNIVERSE_SPEED";
	public static final String KEY_CONSTELLATION_SPEED  = "CONSTELLATION_SPEED";
	public static final String KEY_PLANET_TOUCH_SENSE   = "PLANET_TOUCH_SENSE";
	
	
	@Override
	protected void onCreate(Bundle icicle) {
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
							String p = AllStarsLiveWallpaper.class.getPackage().getName();
							String c = AllStarsLiveWallpaper.class.getCanonicalName();
							iWallpaper.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(p, c));
							
						}
						else {
							iWallpaper.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
							
						}
						
					}
					catch(ActivityNotFoundException e) {
						iWallpaper.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
						
					}
					
					startActivity(iWallpaper);
					
					return true;
					
				}
				
			});
			root.addPreference(setWallpaper);
			
			SharedPreferences sp = getSharedPreferences(KEY_NAME, 0);
			
//********** PLANET
			PreferenceCategory planetCate = new PreferenceCategory(this);
			planetCate.setTitle(R.string.planet);
			root.addPreference(planetCate);
			
			ListPreference planets = new ListPreference(this);
			planets.setKey(KEY_PLANETS);
			planets.setTitle(R.string.planets);
			planets.setSummary(R.string.set_the_favorite_planet);
			planets.setDialogTitle(R.string.planet_name);
			planets.setEntries(R.array.planets);
			planets.setEntryValues(R.array.values);
			root.addPreference(planets);
			
//********** APPEARANCE
			PreferenceCategory appCate = new PreferenceCategory(this);
			appCate.setTitle(R.string.appearance);
			root.addPreference(appCate);
			
			ListPreference universes = new ListPreference(this);
			universes.setKey(KEY_UNIVERSES);
			universes.setTitle(R.string.universes);
			universes.setSummary(R.string.set_the_favorite_universe);
			universes.setDialogTitle(R.string.universe_name);
			universes.setEntries(R.array.universe_view);
			universes.setEntryValues(R.array.values);
			root.addPreference(universes);
			
			int starDef = sp.getInt(PrefSettings.KEY_STAR_COUNT, 67);
			final PrefSeekBar starCount = 
					new PrefSeekBar(PrefSettings.this,
							"Count", " Stars", starDef, 100);
			starCount.setKey(KEY_STAR_COUNT);
			starCount.setTitle(R.string.star_count);
			starCount.setSummary(R.string.set_the_number_of_the_stars);
			root.addPreference(starCount);
			
//********** SPEED
			PreferenceCategory speedCate = new PreferenceCategory(this);
			speedCate.setTitle(R.string.speed);
			root.addPreference(speedCate);
			
			int planetDef = sp.getInt(PrefSettings.KEY_PLANET_SPEED, 3);
			final PrefSeekBar planetSpeed = 
					new PrefSeekBar(PrefSettings.this, 
							"Speed", " ", planetDef, 10);
			planetSpeed.setKey(KEY_PLANET_SPEED);
			planetSpeed.setTitle(R.string.planet_speed);
			planetSpeed.setSummary(R.string.set_the_speed_of_rotating_the_planet);
			root.addPreference(planetSpeed);
			
			int universeDef = sp.getInt(PrefSettings.KEY_UNIVERSE_SPEED, 3);
			final PrefSeekBar uniSpeed = 
					new PrefSeekBar(PrefSettings.this, 
							"Speed", " ", universeDef, 10);
			uniSpeed.setKey(KEY_UNIVERSE_SPEED);
			uniSpeed.setTitle(R.string.universe_speed);
			uniSpeed.setSummary(R.string.set_the_speed_of_the_universe);
			root.addPreference(uniSpeed);
			
			int consteDef = sp.getInt(PrefSettings.KEY_CONSTELLATION_SPEED, 5);
			final PrefSeekBar consteSpeed = 
					new PrefSeekBar(PrefSettings.this, 
							"Speed", " ", consteDef, 10);
			consteSpeed.setKey(KEY_CONSTELLATION_SPEED);
			consteSpeed.setTitle(R.string.constellation_speed);
			consteSpeed.setSummary(R.string.set_the_speed_of_the_constellation);
			root.addPreference(consteSpeed);
			
//********** SENSE
			PreferenceCategory senceCate = new PreferenceCategory(this);
			senceCate.setTitle(R.string.sense);
			root.addPreference(senceCate);
			
			int senseDef = sp.getInt(PrefSettings.KEY_PLANET_TOUCH_SENSE, 2);
			final PrefSeekBar touchSense = 
					new PrefSeekBar(PrefSettings.this, 
							"Sense", " ", senseDef, 5);
			touchSense.setKey(KEY_PLANET_TOUCH_SENSE);
			touchSense.setTitle(R.string.planet_sense);
			touchSense.setSummary(R.string.set_the_touch_sense_of_the_rotating_planet);
			root.addPreference(touchSense);
			
			PreferenceCategory spaceEnd = new PreferenceCategory(this);
			root.addPreference(spaceEnd);
			
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
					Uri uriValuation = Uri.parse("https://play.google.com/store/apps/details?id=com.jackbee.allstars");
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
