package com.example.paybizsdk.entity;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.paybizsdk.R;

public class ProgressDialog extends Dialog {

    public ProgressDialog(Context context) {
        super(context);
        init(context);
    }

    public ProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View progressView = inflater.inflate(R.layout.progress_view, null);
        ImageView logoImageView = progressView.findViewById(R.id.logoImageView);
        ProgressBar progressBar = progressView.findViewById(R.id.progressBar);
        setContentView(progressView);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

}
