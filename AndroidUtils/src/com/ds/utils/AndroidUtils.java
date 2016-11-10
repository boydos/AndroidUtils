package com.ds.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class AndroidUtils {
	
	public static void hiddenSoftInput(Context context,IBinder token) {
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive())imm.hideSoftInputFromWindow(token, 0);
	}
	
	public static boolean isShouldHideKeyboard(View v,MotionEvent event) {
		if(v!=null&& v instanceof EditText) {
			int[] location={0,0};
			v.getLocationInWindow(location);
			int left = location[0];
			int top = location[1];
			int bottom = top+v.getHeight();
			int right = left+v.getWidth();
			if(event.getX()>left && event.getX()<right
			   &&event.getY()>top && event.getY()<bottom) {
				return false;
			} 
			return true;
		}
		return false;
	}
	public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifi.isConnected() || mobile.isConnected();
    }
	
	public static boolean isWifiEnable(Context context) {
		WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
	}
	public static boolean getMobileDataEnable(Context context) {
        ConnectivityManager connectionManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Class<?> ownerClass = connectionManager.getClass();

        try {
            Class<?>[] parent = null;
            Object[] args = null;
            Method method = ownerClass
                    .getMethod("getMobileDataEnabled", parent);
            Boolean isOpen = (Boolean) method.invoke(connectionManager, args);
            return isOpen;
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
