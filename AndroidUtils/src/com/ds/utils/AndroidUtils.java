package com.ds.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
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
	@SuppressWarnings("deprecation")
	public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifi.isConnected() || mobile.isConnected();
    }
	@SuppressWarnings("deprecation")
	public static void wakeUpScreen(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        wl.acquire();
        wl.release();
	}
	public static String getAppVersion(Context context,String packageName) {
        String versionName=null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (Exception e) {
        }
        return versionName;
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
	public static TelephonyManager getPhoneManager(Context context) {
	    return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	public static String getNetworkNumber(Context context) {
	    return getPhoneManager(context).getNetworkOperator();
	}
    public static boolean isSimReady(Context context) {
    	return getPhoneManager(context).getSimState() == TelephonyManager.SIM_STATE_READY;
	}
    public static String getKernelVersion() {
        String version = "";
        String info = "";
        File file = new File("/proc/version");
        if (!file.exists())
            return version;
        InputStream instream = null;
        BufferedReader reader = null;
        try {
            instream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(instream),
                    8 * 1024);
            String line = "";
            while ((line = reader.readLine()) != null)
                info += line;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (instream != null)
                    instream.close();
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        if (info == null || "".equalsIgnoreCase(info))
            return version;
        String keyword = "version ";
        try {
            version = info.substring(info.indexOf(keyword) + keyword.length());
            version = version.substring(0, version.indexOf(" "));
        } catch (IndexOutOfBoundsException e) {
        }
        return version;
    }
    public static String getLocalIpAddress(int type) {
        String ip4 = "";
        String ip6 = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface inf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = inf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        ip4 = inetAddress.getHostAddress();
                    } else if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet6Address) {
                        ip6 = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (type == 6)
            return ip6;
        else
            return ip4;
    }
    public static String getNICName() {
        String nicnames = "";
        try {
            List<NetworkInterface> networkInterfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface in : networkInterfaces) {
                if (in.isUp() && !in.isLoopback()) {
                    String displayName = in.getDisplayName();
                    boolean search = false;
                    for (Enumeration<InetAddress> enumerInet = in
                            .getInetAddresses(); enumerInet.hasMoreElements();) {
                        InetAddress inetAddress = enumerInet.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            search = true;
                        }
                    }
                    if (search) {
                        if ("".equals(nicnames))
                            nicnames += displayName;
                        else
                            nicnames += "," + displayName;
                    }
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return nicnames;
    }
    public static String getLocalIp4OrIp6() {
        String ip4 = getLocalIp4Address();
        if (StringUtils.isEmpty(ip4))
            return getLocalIp6Address();
        return ip4;
    }

    public static String getLocalIp6Address() {
        return getLocalIpAddress(6);
    }

    public static String getLocalIp4Address() {
        return getLocalIpAddress(4);
    }
    public static boolean isIp4Connected(Context context) {
        boolean connected = isNetworkAvailable(context);
        if (connected) {
            String ip4 = getLocalIp4Address();
            if (ip4 != null && !"".equals(ip4.trim()))
                return true;
        }
        return false;
    }
    public static boolean isIp6Connected(Context context) {
        boolean connected = isNetworkAvailable(context);
        if (connected) {
            String ip6 = getLocalIp6Address();
            if (ip6 != null && !"".equals(ip6.trim()))
                return true;
        }
        return false;
    }
    public static void toggleWifi(Context context, boolean enable) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }
    public static void toggleMobileData(Context context, boolean enable) {
        ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class<?> ownerClass = connectionManager.getClass();
        Class<?>[] argsClass = new Class<?>[1];
        argsClass[0] = boolean.class;
        try {
            Method method = ownerClass.getMethod("setMobileDataEnabled",argsClass);
            method.invoke(connectionManager, enable);
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
    }
    
}
