package com.firebasedevday.codebattle.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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

    private GoogleApiClient googleApiClient;
    private FirebaseUser user;
    private DatabaseReference messageReference;
    private StorageReference storageReference;

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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void setupFirebaseAuth() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            setupChat(uid);
        } else {
            goToLoginScreen();
        }
    }

    private void setupFirebaseDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        messageReference = database.getReference("messages");
    }

    private void setupFirebaseStorage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void setupChat(String uid) {
        chatAdapter = new ChatAdapter(uid);
        rvChat.setAdapter(chatAdapter);
        messageList = new ArrayList<>();
        chatAdapter.setMessageList(messageList);
    }

    private void loadChat() {
        showLoading();
        messageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideLoading();
                dataSnapshot.getRef().removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                hideLoading();
                showAlert(R.string.chat_initialize_message_failure);
            }
        });
        messageReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                messageList.add(dataSnapshot.getValue(Message.class));
                chatAdapter.notifyDataSetChanged();
                rvChat.smoothScrollToPosition(chatAdapter.getItemCount());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        if (user != null) {
            DatabaseReference databaseReference = messageReference.push();
            Message message = new Message()
                    .setAvatar(user.getPhotoUrl().toString())
                    .setData(text)
                    .setType(Message.TYPE_TEXT)
                    .setSenderId(user.getUid())
                    .setUsername("Android");
            databaseReference.setValue(message);
        } else {
            goToLoginScreen();
        }
    }

    private void chooseImage() {
        EZPhotoPickConfig config = new EZPhotoPickConfig();
        config.photoSource = PhotoSource.GALERY;
        config.exportingSize = 900;
        EZPhotoPick.startPhotoPickActivity(this, config);
    }

    private void sendImage(Bitmap bitmap) {
        DatabaseReference databaseReference = messageReference.push();
        String key = databaseReference.getKey();
        uploadFileToStorage(databaseReference, bitmap, key);
    }

    private void uploadFileToStorage(final DatabaseReference databaseReference, Bitmap bitmap, String filename) {
        StorageReference photoReference = storageReference.child(filename + ".jpg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = photoReference.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                onUploadPhotoSuccess(databaseReference, taskSnapshot);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                onUploadPhotoFailure();
            }
        });
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
        if (user != null) {
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            Message message = new Message()
                    .setAvatar(user.getPhotoUrl().toString())
                    .setData(downloadUrl.toString())
                    .setType(Message.TYPE_IMAGE)
                    .setSenderId(user.getUid())
                    .setUsername("Android");
            databaseReference.setValue(message);
        } else {
            goToLoginScreen();
        }
    }

    private void onUploadPhotoFailure() {
        showAlert(R.string.chat_upload_photo_failure);
    }

    private void onSignOutFailure() {
        showAlert(R.string.chat_sign_out_failure);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            FirebaseAuth.getInstance().signOut();
                            goToLoginScreen();
                        } else {
                            onSignOutFailure();
                        }
                    }
                });
    }
}

