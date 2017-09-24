package com.example.wkj_pc.fitnesslive.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by wkj on 2017/9/12.
 */

public class AlertProgressDialogUtils {

    private static ProgressDialog pd;

    public static void alertProgressShow(Context context,String content){
        DialogInterface.OnCancelListener cancelListener;
        cancelListener = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        };
        pd = ProgressDialog.show(context, "提示", content, true, true, cancelListener);
    }
    public static void alertProgressClose() {
        pd.cancel();
    }
}
