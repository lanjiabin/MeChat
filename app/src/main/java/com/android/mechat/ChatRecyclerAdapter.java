package com.android.mechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 适配器
 */
public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ChatViewHolder> {

    private ArrayList<HashMap<String, String>> mList;
    private Context mContext;

    public ChatRecyclerAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.mContext = context;
        this.mList = list;
    }

    /**
     * 该方法会在RecyclerView需要展示一个item的时候回调，
     * 重写该方法，使ViewHolder加载item的布局，布局复用，提高性能，
     * 就ListView的优化一样，只不过RecyclerView把这个集成到官方方法中了。
     */
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ChatViewHolder holder = new ChatViewHolder(
                LayoutInflater.from(mContext)
                        .inflate(
                                R.layout.item_chat,
                                parent,
                                false));
        return holder;
    }

    /**
     * 该方法是填充绑定item数据的
     */
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        holder.nameTv.setText(mList.get(position).get("name"));
        holder.msgTv.setText(mList.get(position).get("msg"));
        holder.timeTv.setText(mList.get(position).get("time"));
    }

    /**
     * 该方法是返回item的数量。
     */
    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        TextView msgTv;
        TextView timeTv;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.send1);
            msgTv = itemView.findViewById(R.id.msg);
            timeTv = itemView.findViewById(R.id.time);
        }
    }
}

