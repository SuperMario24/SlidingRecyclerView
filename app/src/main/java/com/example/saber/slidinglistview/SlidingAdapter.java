package com.example.saber.slidinglistview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by saber on 2017/7/25.
 */

public class SlidingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> list;
    private Context context;
    private OnClickListenerEditOrDelete onClickListenerEditOrDelete;

    public SlidingAdapter(List<String> list, Context context, OnClickListenerEditOrDelete onClickListenerEditOrDelete) {
        this.list = list;
        this.context = context;
        this.onClickListenerEditOrDelete = onClickListenerEditOrDelete;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.rlTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"dsad",Toast.LENGTH_SHORT).show();
            }
        });

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListenerEditOrDelete != null){
                    onClickListenerEditOrDelete.OnClickListenerEdit(holder.getAdapterPosition());
                }
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListenerEditOrDelete != null){
                    onClickListenerEditOrDelete.OnClickListenerDelete(holder.getAdapterPosition());
                }
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).tvName.setText(list.get(position));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    private class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rlTop;
        TextView tvName,tvEdit,tvDelete;
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            rlTop = (RelativeLayout) itemView.findViewById(R.id.rl_top);
            tvName = (TextView) itemView.findViewById(R.id.tv_title);
            tvEdit = (TextView) itemView.findViewById(R.id.tv_edit);
            tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }


    public void setOnClickListenerEditOrDelete(OnClickListenerEditOrDelete onClickListenerEditOrDelete1){
        this.onClickListenerEditOrDelete=onClickListenerEditOrDelete1;
    }

    public interface OnClickListenerEditOrDelete{
        void OnClickListenerEdit(int position);
        void OnClickListenerDelete(int position);
    }
}
