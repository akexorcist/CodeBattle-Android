package com.firebasedevday.library.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.firebasedevday.library.R;


/**
 * Created by Akexorcist on 8/3/15 AD.
 */
public class QuickLoadingDialog extends DialogFragment {

    public static QuickLoadingDialog newInstance() {
        return new QuickLoadingDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);
        return inflater.inflate(R.layout.view_dialog_loading, container);
    }

    public static class Builder {

        public Builder() {
        }

        public QuickLoadingDialog build() {
            return QuickLoadingDialog.newInstance();
        }
    }
}
