package com.cubebox.tiaoyitiao.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cubebox.tiaoyitiao.data.ConstantsPreference;


/**
 * preference 存储管理，提供统一管理preference的地方。
 *
 * @author lorcan
 */
public class PreferencesManager<T> implements ConstantsPreference {


    private static PreferencesManager instance;

    private SharedPreferences mPrefer;
    private Editor editor;

    public PreferencesManager(Context context) {
        mPrefer = context.getSharedPreferences(ConstantsPreference.PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = mPrefer.edit();
    }

    public static final PreferencesManager getInstance(Context context) {
        synchronized (PreferencesManager.class) {
            if (instance == null)
                instance = new PreferencesManager(context);
            return instance;
        }
    }

    /**
     * 存放String 值
     */
    public void putString(String name, String values) {
        editor.putString(name, values);
        editor.commit();
    }

    /**
     * 存放String 值
     */
    public void putInteger(String name, int values) {
        editor.putInt(name, values);
        editor.commit();
    }

    /**
     * 存放long 值
     */
    public void putLong(String name, long values) {
        editor.putLong(name, values);
        editor.commit();
    }

    /**
     * 得到xml文件中的键值对名 如果没有值则默认为“”
     */
    public String getString(String name, String defValue) {
        return mPrefer.getString(name, defValue);
    }

    /**
     * 得到xml文件中的键值对名 如果没有值则默认为“”
     */
    public long getLong(String name, long defValue) {
        return mPrefer.getLong(name, defValue);
    }

    /**
     * 得到xml文件中的键值对名 如果没有值则默认为“”
     */
    public int getInteter(String name, int defValue) {
        return mPrefer.getInt(name, defValue);
    }


    public boolean getBoolean(String name, boolean defValue) {
        return mPrefer.getBoolean(name, defValue);
    }

    public void putBoolean(String name, boolean values) {
        editor.putBoolean(name, values);
        editor.commit();
    }


}
