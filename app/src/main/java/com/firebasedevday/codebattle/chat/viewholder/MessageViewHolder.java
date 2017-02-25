package com.firebasedevday.codebattle.chat.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebasedevday.codebattle.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Akexorcist on 2/19/2017 AD.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView tvMessage;
    public TextView tvName;
    public CircleImageView ivProfile;
    // TODO Chat 16 : Declare message image view instance

    public MessageViewHolder(View itemView) {
        super(itemView);
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        ivProfile = (CircleImageView) itemView.findViewById(R.id.iv_profile);
        // TODO Chat 17 : Bind message image view
    }
}
