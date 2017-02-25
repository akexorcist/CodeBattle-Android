package com.firebasedevday.codebattle.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebasedevday.codebattle.R;
import com.firebasedevday.codebattle.chat.model.Message;
import com.firebasedevday.codebattle.login.LoginActivity;
import com.firebasedevday.library.BaseActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import siclo.com.ezphotopicker.api.EZPhotoPick;
import siclo.com.ezphotopicker.api.EZPhotoPickStorage;
import siclo.com.ezphotopicker.api.models.EZPhotoPickConfig;
import siclo.com.ezphotopicker.api.models.PhotoSource;

public class ChatActivity extends BaseActivity {
    private RecyclerView rvChat;
    private EditText etMessage;
    private ImageButton btnAddPhoto;
    private Button btnSendMessage;
    private List<Message> messageList;
    private ChatAdapter chatAdapter;

    // TODO Chat 1 : Declare firebase user and database reference
    // TODO Chat 2 : Declare google api client
    // TODO Chat 9 : Declare Firebase Storage instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        bindView();
        setupView();

        setupGoogleApiClient();
        setupFirebaseAuth();
        setupFirebaseDatabase();
        setupFirebaseStorage();
        loadChat();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void bindView() {
        rvChat = (RecyclerView) findViewById(R.id.rv_chat);
        etMessage = (EditText) findViewById(R.id.et_message);
        btnSendMessage = (Button) findViewById(R.id.btn_send_message);
        btnAddPhoto = (ImageButton) findViewById(R.id.btn_add_photo);
    }

    private void setupView() {
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        btnSendMessage.setOnClickListener(onSendMessageClick());
        btnAddPhoto.setOnClickListener(onAddPhotoClick());
    }

    private void setupGoogleApiClient() {
        // TODO Chat 3 : Setup google api client
    }

    private void setupFirebaseAuth() {
        // TODO Chat 4 : Setup firebase auth
    }

    private void setupFirebaseDatabase() {
        // TODO Chat 5 : Setup firebase realtime database
    }

    private void setupFirebaseStorage() {
        // TODO Chat 10 : Setup firebase storage
    }

    private void setupChat(String uid) {
        chatAdapter = new ChatAdapter(uid);
        rvChat.setAdapter(chatAdapter);
        messageList = new ArrayList<>();
        chatAdapter.setMessageList(messageList);
    }

    private void loadChat() {
        showLoading();
        // TODO Chat 6 : Initialize chat message
    }

    private void goToLoginScreen() {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private View.OnClickListener onSendMessageClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        };
    }

    private View.OnClickListener onAddPhotoClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        };
    }

    private void sendMessage() {
        String message = etMessage.getText().toString();
        if (!message.isEmpty()) {
            sendMessageToDatabase(message);
            etMessage.setText("");
        } else {
            onMessageEmpty();
        }
    }

    private void sendMessageToDatabase(String text) {
        // TODO Chat 8 : Send message to realtime database
    }

    private void chooseImage() {
        EZPhotoPickConfig config = new EZPhotoPickConfig();
        config.photoSource = PhotoSource.GALERY;
        config.exportingSize = 900;
        EZPhotoPick.startPhotoPickActivity(this, config);
    }

    private void sendImage(Bitmap bitmap) {
        // TODO Chat 11 : Prepare to upload image
    }

    private void uploadFileToStorage(final DatabaseReference databaseReference, Bitmap bitmap, String filename) {
        // TODO Chat 12 : Upload file to firebase storage
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EZPhotoPick.PHOTO_PICK_REQUEST_CODE &&
                resultCode == RESULT_OK) {
            try {
                Bitmap pickedPhoto = new EZPhotoPickStorage(this).loadLatestStoredPhotoBitmap();
                sendImage(pickedPhoto);
            } catch (IOException e) {
                e.printStackTrace();
                onUploadPhotoFailure();
            }
        }
    }

    private void onMessageEmpty() {
        showAlert(R.string.chat_empty_message);
    }

    @SuppressWarnings("VisibleForTests")
    private void onUploadPhotoSuccess(DatabaseReference databaseReference, UploadTask.TaskSnapshot taskSnapshot) {
        // TODO Chat 13 : Send message data to realtime database
    }

    private void onUploadPhotoFailure() {
        showAlert(R.string.chat_upload_photo_failure);
    }

    private void onSignOutFailure() {
        showAlert(R.string.chat_sign_out_failure);
    }

    private void signOut() {
        // TODO Chat 19 : Sign out from google account and firebase auth
    }
}

