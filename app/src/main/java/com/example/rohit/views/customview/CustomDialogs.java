package com.example.rohit.views.customview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.rohit.viewmodal.PlaceViewModal;
import com.rohit.solarcalulator.R;


/**
 * Creating a Helper class for custom dialogs
 * and other UI related Methods
 */
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void SettingsAlert(final Activity context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("GPS");

        alertDialog.setMessage("GPS is not enabled. Enable it in settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                context.finish();
            }
        });

        alertDialog.show();
    }

    public static void deleteForever(Context context, final PlaceViewModal mPlaceViewModal) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("Delete");

        alertDialog.setMessage("Delete all the entries?");

        alertDialog.setPositiveButton("Sure", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPlaceViewModal.deleteAllNotes();
                dialog.cancel();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}
