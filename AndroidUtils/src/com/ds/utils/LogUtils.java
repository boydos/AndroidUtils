package com.ds.utils;

import android.util.Log;

public class LogUtils {
	  private static final boolean LOG_DEBUG =true;
      private static final String LOG_TAG="tds";      
      public static void i(String TAG,String msg) {
          
          if(LOG_DEBUG)Log.i(String.format("[%s] [%s] [%s]", DateUtils.getDate(System.currentTimeMillis()),LOG_TAG,TAG), String.format("[%s]",msg));
      }
      
      public static void d(String TAG,String msg) {
          if(LOG_DEBUG)Log.d(String.format("[%s] [%s] [%s]", DateUtils.getDate(System.currentTimeMillis()),LOG_TAG,TAG), String.format("[%s]",msg));
      }
      
      public static void e(String TAG,String msg) {
          if(LOG_DEBUG)Log.e(String.format("[%s] [%s] [%s]", DateUtils.getDate(System.currentTimeMillis()),LOG_TAG,TAG), String.format("[%s]",msg));
      }
      
      public static void w(String TAG,String msg) {
          if(LOG_DEBUG)Log.w(String.format("[%s] [%s] [%s]", DateUtils.getDate(System.currentTimeMillis()),LOG_TAG,TAG), String.format("[%s]",msg));
      }
    
    
}
