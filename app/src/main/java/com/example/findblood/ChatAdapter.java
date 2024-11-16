package com.example.findblood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Chat> chatList;

    public ChatAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == Chat.TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_sent, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_received, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        if (holder.name != null) {
            holder.name.setText(chat.getSenderName());
        }
        holder.date.setText(chat.getTimestamp());
        holder.content.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return chatList.get(position).isSent() ? Chat.TYPE_SENT : Chat.TYPE_RECEIVED;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, content;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView.findViewById(R.id.sentMessageTextView) != null) {
                content = itemView.findViewById(R.id.sentMessageTextView);
                date = itemView.findViewById(R.id.timestampTextView);
            } else {
                name = itemView.findViewById(R.id.senderNameTextView);
                content = itemView.findViewById(R.id.receivedMessageTextView);
                date = itemView.findViewById(R.id.timestampTextView);
            }
        }
    }
}