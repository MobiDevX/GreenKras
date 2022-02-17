package com.reaver.greenkras.ui.vote;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.reaver.greenkras.R;
import java.util.ArrayList;
import java.util.List;


public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.voteView> {
    private List<Item> itemList;
    private OnItemClickListener listener;

    private ArrayList<Item>itemArrayList;

    VoteAdapter(ArrayList<Item> itemList) {
        this.itemList = itemList;
        itemArrayList = itemList;
    }

    @NonNull
    @Override
    public voteView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vote_item, viewGroup, false);
        return new voteView(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(voteView voteView, int i) {

        Item item = itemList.get(i);
        voteView.park_title.setText(item.getParkname());
        voteView.vote_count.setText("Голосов: "+item.getParkcount());
        voteView.login_user.setText("Добавил пользователь: "+item.getParklogin());
    }

    void agregarItem(Item item){
        itemList.add(item);
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
    class voteView extends RecyclerView.ViewHolder{

        private TextView park_title, vote_count, login_user;

        voteView(@NonNull View itemView) {
            super(itemView);
            park_title = itemView.findViewById(R.id.park_title);
            vote_count = itemView.findViewById(R.id.vote_count);
            login_user = itemView.findViewById(R.id.login_user);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemArrayList.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
