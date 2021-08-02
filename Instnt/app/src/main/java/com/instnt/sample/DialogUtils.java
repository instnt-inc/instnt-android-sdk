package com.instnt.sample;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

public class DialogUtils {

    /**
     * show default dialog with button click callback
     * @param activity : current activity
     * @param message : message to be showed
     * @param btnText : button text
     */
    public static void showAlertDialog(Activity activity, String title, String message, String btnText, boolean success){
        if (activity.isFinishing())
            return;

        AtomicBoolean positiveClicked = new AtomicBoolean(false);

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_default);

        TextView titleView = dialog.findViewById(R.id.title);
        titleView.setText(title);

        TextView messageView = dialog.findViewById(R.id.message);
        messageView.setText(message);

        if (success) {
            titleView.setTextColor(activity.getResources().getColor(R.color.blue));
        }else {
            titleView.setTextColor(activity.getResources().getColor(R.color.red));
        }
        TextView confirmView = dialog.findViewById(R.id.confirm);
        confirmView.setText(btnText);

        confirmView.setOnClickListener(v -> {
            positiveClicked.set(true);
            dialog.dismiss();
        });

        dialog.show();
    }
}
