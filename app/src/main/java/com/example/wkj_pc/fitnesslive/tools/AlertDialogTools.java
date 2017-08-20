package com.example.wkj_pc.fitnesslive.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by wkj_pc on 2017/8/20.
 */

public class AlertDialogTools {
    private static AlertDialog dialog;
    public static void showDialog(Context context, int iconid, boolean cancelable, String positiveBtn,
                                   DialogInterface.OnClickListener listener,String title,String message){
        dialog = new AlertDialog.Builder(context)
                .setIcon(iconid)
                .setCancelable(cancelable)
                .setPositiveButton(positiveBtn, listener)
                .setTitle(title)
                .setMessage(message)
                .create();
        dialog.show();
    }
}
