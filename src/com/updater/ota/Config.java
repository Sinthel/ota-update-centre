package com.updater.ota;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

public class Config {
    public static final String GCM_SENDER_ID = "1068482628480";
    public static final String PULL_URL = "http://sensation-devs.org/romupdater2/pages/romupdate.php";
    public static final String OTA_ID_PROP = "otaupdater.otaid";
    
    private int lastVersion = -1;
    private String lastDevice = null;
    private String lastRomID = null;
    
    private int curVersion = -1;
    private String curDevice = null;
    private String curRomID = null;
    
    private boolean pruneFiles = false;
    
    private static final String PREFS_NAME = "prefs";
    private final SharedPreferences PREFS;
    
    private Config(Context ctx) {
        PREFS = ctx.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        
        lastVersion = PREFS.getInt("version", lastVersion);
        lastDevice = PREFS.getString("device", lastDevice);
        lastRomID = PREFS.getString("romid", lastRomID);
        pruneFiles = PREFS.getBoolean("prune", pruneFiles);
        
        try {
            curVersion = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
        }
        curDevice = android.os.Build.DEVICE.toLowerCase();
        curRomID = System.getProperty("otaupdater.otaid");
    }
    private static Config instance = null;
    public static synchronized Config getInstance(Context ctx) {
        if (instance == null) instance = new Config(ctx);
        return instance;
    }
    
    public int getLastVersion() {
        return lastVersion;
    }
    
    public String getLastDevice() {
        return lastDevice;
    }
    
    public String getLastRomID() {
        return lastRomID;
    }
    
    public void setValuesToCurrent() {
        synchronized (PREFS) {
            SharedPreferences.Editor editor = PREFS.edit();
            editor.putInt("version", curVersion);
            editor.putString("device", curDevice);
            editor.putString("romid", curRomID);
            editor.commit();
        }
    }
    
    public boolean upToDate() {
        return curVersion == lastVersion && curDevice.equals(lastDevice) && curRomID.equals(lastRomID);
    }
    
    public boolean getPruneFiles() {
        return pruneFiles;
    }
    
    public void setPruneFiles(boolean pruneFiles) {
        this.pruneFiles = pruneFiles;
        
        synchronized (PREFS) {
            SharedPreferences.Editor editor = PREFS.edit();
            editor.putBoolean("prune", pruneFiles);
            editor.commit();
        }
    }
}
