package com.ducle.learnengforkids;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;


public class DialogLoading {
    private Context context;
    private Dialog dialog;

    public DialogLoading(Context context) {
        this.context = context;
    }

    public void show(){
        dialog = new Dialog(context);
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
