package org.instnt.accept.sample.view;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.instnt.accept.instntsdk.R;

import java.util.concurrent.atomic.AtomicInteger;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mDialog;
    private AtomicInteger count = new AtomicInteger();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showProgressDialog(boolean show) {

        int current;

        if (show){
            current = count.getAndIncrement();
            if (current < 0){
                current = 0;
                count.set(0);
            }

            if (current == 0){
                showDialog();
            }
        } else {
            current = count.decrementAndGet();

            if (current < 0){
                current = 0;
                count.set(0);
            }

            if (current == 0)
                dismissDialog();
        }
    }

    private void showDialog(){
        if (mDialog == null) {
            mDialog = new ProgressDialog(this, R.style.ProgressTheme);
            mDialog.setCancelable(false);
            mDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            mDialog.setCancelable(false);

            try {
                mDialog.show();
            } catch (Exception e) {}
        }
    }

    private void dismissDialog(){
        if (mDialog != null) {
            try {
                mDialog.dismiss();
            } catch (Exception e) {}
            mDialog = null;
        }
    }
}
