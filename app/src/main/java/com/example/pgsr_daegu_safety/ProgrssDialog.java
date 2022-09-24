package com.example.pgsr_daegu_safety;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

class ProgressDialog extends Dialog {
    public ProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
    }
}