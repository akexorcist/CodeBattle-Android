package com.firebasedevday.library.dialog;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Akexorcist on 9/2/2016 AD.
 */

public class QuickDialog {
    private static final String TAG_DIALOG = "dialog";
    private static QuickDialog quickDialog;

    public static QuickDialog getInstance() {
        if (quickDialog == null) {
            quickDialog = new QuickDialog();
        }
        return quickDialog;
    }

    private DialogFragment dialog;

    public void showLoadingDialog(FragmentManager fragmentManager) {
        dismissDialog();
        dialog = new QuickLoadingDialog.Builder()
                .build();
        dialog.show(fragmentManager, TAG_DIALOG);
    }

    public boolean isDialogShowing(FragmentManager fragmentManager) {
        return dialog != null && fragmentManager.findFragmentByTag(TAG_DIALOG) != null;
    }

    public boolean isDialogShowing(FragmentManager fragmentManager, String tag) {
        return dialog != null && fragmentManager.findFragmentByTag(tag) != null;
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isAdded()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
