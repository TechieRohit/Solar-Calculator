package com.example.rohit.views.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.rohit.solarcalulator.R;

public class CustomDialogs {

    public static void showSavedLocationDialogs(Context context,String sunrise,String sunset,String placename,String date) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_saved_locations);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);

        TextView sunriseText = (TextView)dialog.findViewById(R.id.textView_sunrise);
        sunriseText.setText("" + sunrise);

        TextView sunsetText = (TextView)dialog.findViewById(R.id.textView_sunset);
        sunsetText.setText("" + sunset);

        TextView city = dialog.findViewById(R.id.textView_placename);
        city.setText("" + placename);

        TextView dateText = dialog.findViewById(R.id.textdate);
        dateText.setText("Sunrise and Sunset for date " + date);
        dialog.show();
    }
}
