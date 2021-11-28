package com.ducle.learnengforkids;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;


public class DialogLoading {
    private Activity activity;
    private Dialog dialog;

    public DialogLoading(Activity activity) {
        this.activity = activity;
    }

    public void show(){
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        if (dialog.getWindow() != null)
        {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        dialog.show();
    }
    public void dismissDialog(){
        dialog.dismiss();
    }
}
