package me.smartfarm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.smartfarm.R;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.Message;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private List<Message> messageList;
    private String currentUserId;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
        this.currentUserId = Util.getCurrentUser().getId();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.getSender().equals(currentUserId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_receive, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).bind(message);
        } else {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // ViewHolder for sent messages
    public static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMessage;

        public SentMessageViewHolder(View view) {
            super(view);
            textViewMessage = view.findViewById(R.id.txtMessageContent);
        }

        public void bind(Message message) {
            textViewMessage.setText(message.getText());
        }
    }

    // ViewHolder for received messages
    public static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMessage;

        public ReceivedMessageViewHolder(View view) {
            super(view);
            textViewMessage = view.findViewById(R.id.txtMessageContent);

        }

        public void bind(Message message) {
            textViewMessage.setText(message.getText());
        }
    }
}
