package com.firebasedevday.codebattle.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebasedevday.codebattle.R;
import com.firebasedevday.codebattle.chat.model.Message;
import com.firebasedevday.codebattle.chat.viewholder.MessageViewHolder;

import java.util.List;


/**
 * Created by Akexorcist on 2/19/2017 AD.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_YOUR_MESSAGE = 0;
    private static final int TYPE_OTHER_MESSAGE = 1;
    private List<Message> messageList;
    private String uid;
    private OnMessageClickListener onMessageClickListener;

    public ChatAdapter(String uid) {
        this.uid = uid;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public void setOnMessageClickListener(OnMessageClickListener onMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_YOUR_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_your_message_item, parent, false);
            return new MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_other_message_item, parent, false);
            return new MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Message message = messageList.get(position);
        if (holder instanceof MessageViewHolder) {
            MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
            Glide.with(messageViewHolder.itemView.getContext()).load(message.getAvatar()).into(messageViewHolder.ivProfile);
            messageViewHolder.tvName.setText(message.getUsername());
            messageViewHolder.tvMessage.setVisibility(View.GONE);
            messageViewHolder.ivMessage.setVisibility(View.GONE);
            if (message.getType().equals(Message.TYPE_TEXT)) {
                messageViewHolder.tvMessage.setText(message.getData());
                messageViewHolder.tvMessage.setVisibility(View.VISIBLE);
            } else if (message.getType().equals(Message.TYPE_IMAGE)) {
                Glide.with(messageViewHolder.itemView.getContext()).load(message.getData()).into(messageViewHolder.ivMessage);
                messageViewHolder.ivMessage.setVisibility(View.VISIBLE);
                messageViewHolder.ivMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onMessageClickListener != null) {
                            onMessageClickListener.onImageClick(message);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.getSenderId() != null && message.getSenderId().equals(uid)) {
            return TYPE_YOUR_MESSAGE;
        } else {
            return TYPE_OTHER_MESSAGE;
        }
    }

    @Override
    public int getItemCount() {
        return messageList != null ? messageList.size() : 0;
    }

    private interface OnMessageClickListener {
        void onImageClick(Message message);
    }
}


