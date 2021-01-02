package com.android.mechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mechat.DB.AddressDBService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 适配器
 */
public class MsgRecyclerAdapter extends RecyclerView.Adapter<MsgRecyclerAdapter.MsgViewHolder> {

    private ArrayList<HashMap<String, String>> mList;
    private Context mContext;
    private String mSendIdLeft;
    private String mSendIdRight;

    public MsgRecyclerAdapter(Context context, ArrayList<HashMap<String, String>> list, String send1, String send2) {
        this.mContext = context;
        this.mList = list;
        this.mSendIdLeft = send1;
        this.mSendIdRight = send2;
    }

    /**
     * 该方法会在RecyclerView需要展示一个item的时候回调，
     * 重写该方法，使ViewHolder加载item的布局，布局复用，提高性能，
     * 就ListView的优化一样，只不过RecyclerView把这个集成到官方方法中了。
     */
    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MsgViewHolder holder = new MsgViewHolder(
                LayoutInflater.from(mContext)
                        .inflate(
                                R.layout.item_msg,
                                parent,
                                false));
        return holder;
    }

    /**
     * 该方法是填充绑定item数据的
     */
    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {

        String id1 = mList.get(position).get("send1"); //收件人
        String id2 = mList.get(position).get("send2"); //发件人

        boolean isTwo = false; //判断是否属于这个两个人的信息
        if (id1.equals(mSendIdLeft) && id2.equals(mSendIdRight)) {
            isTwo = true;
        }
        if (id1.equals(mSendIdRight) && id2.equals(mSendIdLeft)) {
            isTwo = true;
        }

        //不是他们的信息，就跳过
        if (!isTwo) {
            holder.leftMsg.setVisibility(View.GONE);
            holder.rightMsg.setVisibility(View.GONE);
            return;
        }

        //收件人
        String nameLeft = AddressDBService.getInstance().queryAddressByID(mContext, mSendIdLeft).get(0).get("name");

        //发件人
        String nameRight = AddressDBService.getInstance().queryAddressByID(mContext, mSendIdRight).get(0).get("name");
        holder.nameTv1.setText(nameLeft);
        holder.nameTv2.setText(nameRight);

        holder.msgTv1.setText(mList.get(position).get("msg"));
        holder.msgTv2.setText(mList.get(position).get("msg"));

        //等于本地用户，则显示右边。不等于则显示左边
        if (mSendIdRight.equals(id2)) {
            holder.leftMsg.setVisibility(View.GONE);
            holder.rightMsg.setVisibility(View.VISIBLE);
        } else {
            holder.leftMsg.setVisibility(View.VISIBLE);
            holder.rightMsg.setVisibility(View.GONE);
        }
    }

    /**
     * 该方法是返回item的数量。
     */
    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MsgViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv1;
        TextView msgTv1;

        TextView nameTv2;
        TextView msgTv2;

        RelativeLayout leftMsg;
        RelativeLayout rightMsg;

        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv1 = itemView.findViewById(R.id.msg_name1);
            nameTv2 = itemView.findViewById(R.id.msg_name2);

            msgTv1 = itemView.findViewById(R.id.msg1);
            msgTv2 = itemView.findViewById(R.id.msg2);

            leftMsg = itemView.findViewById(R.id.left_msg);
            rightMsg = itemView.findViewById(R.id.right_msg);
        }
    }
}

