package com.firebasedevday.codebattle.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebasedevday.codebattle.R;
import com.firebasedevday.codebattle.chat.ChatActivity;
import com.firebasedevday.library.BaseActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;

public class LoginActivity extends BaseActivity {
    private static final int REQUEST_CODE_GOOGLE_SIGN_IN = 1234;

    private SignInButton signInButton;

    // TODO Login 1 : Declare google api client instance
    // TODO Login 4 : Declare firebase auth and listener

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindView();
        setupView();

        // Google Authentication
        setupGoogleSignIn();

        // Firebase Authentication
        setupFirebaseAuth();
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO Login 6 : Manage firebase auth when activity starting
    }

    @Override
    public void onStop() {
        super.onStop();
        // TODO Login 7 : Manage firebase auth when activity starting
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                onLoginFailure();
            }
        }
    }

    private void bindView() {
        signInButton = (SignInButton) findViewById(R.id.btn_sign_in);
    }

    private void setupView() {
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(onSignInClick());
    }

    private void setupGoogleSignIn() {
        // TODO Login 2 : Setup google api client and signin options here
    }

    private void setupFirebaseAuth() {
        // TODO Login 5 : Setup Firebase Auth
    }

    private View.OnClickListener onSignInClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        };
    }

    private void signIn() {
        showLoading();
        signInButton.setEnabled(false);
        // TODO Login 3 : Call google sign in
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // TODO Login 8 : Sign in to firebase auth with google account
    }

    private void onLoginCompleted() {
        hideLoading();
        startActivity(new Intent(this, ChatActivity.class));
        finish();
    }

    private void onLoginFailure() {
        hideLoading();
        signInButton.setEnabled(true);
        showAlert(R.string.login_authentication_failure);
    }
}

