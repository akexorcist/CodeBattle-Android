package com.firebasedevday.library;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firebasedevday.library.dialog.QuickDialog;


/**
 * Created by Akexorcist on 2/19/2017 AD.
 */

public class BaseActivity extends AppCompatActivity {
    public void showLoading() {
        QuickDialog.getInstance().showLoadingDialog(getSupportFragmentManager());
    }

    public void hideLoading() {
        if (QuickDialog.getInstance().isDialogShowing(getSupportFragmentManager())) {
            QuickDialog.getInstance().dismissDialog();
        }
    }

    public boolean isLoadingShowing() {
        return QuickDialog.getInstance().isDialogShowing(getSupportFragmentManager());
    }

    public void showAlert(int resId) {
        final Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), resId, Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
    }

    public void showAlert(String message) {
        final Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
    }
}
