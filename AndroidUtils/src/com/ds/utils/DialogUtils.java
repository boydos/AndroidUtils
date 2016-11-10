package com.ds.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtils  {
    
    public static ProgressDialog showCircelProgressDialog(Context context) {
        ProgressDialog circleDialog = new ProgressDialog(context);
        //circleDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        circleDialog.setIndeterminate(true);
        circleDialog.setCancelable(false);
        return circleDialog;
    }
}
