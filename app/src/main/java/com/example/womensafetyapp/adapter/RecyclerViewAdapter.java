package com.example.womensafetyapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafetyapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Activity activity;
//    String item;
//    Context context;
    ArrayList<String> contacts_list;
    TextView contacts_empty;
    MainViewModel mainViewModel;

    boolean isEnable = false;
    boolean isSelectedAll = false;

    ArrayList<String> selectlist = new ArrayList<>();

//    public RecyclerViewAdapter(Context context, ArrayList<String> contacts_list, ) {
//        this.context = context;
//        this.contacts_list = contacts_list;
//        this.contacts_empty = c
//    }

    public RecyclerViewAdapter(Activity activity, ArrayList<String> contacts_list, TextView contacts_empty) {
        this.activity = activity;
        this.contacts_list = contacts_list;
        this.contacts_empty = contacts_empty;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_card,parent,false);
        mainViewModel = ViewModelProviders.of((FragmentActivity) activity).get(MainViewModel.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.contact_number.setText(contacts_list.get(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!isEnable){
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            MenuInflater menuInflater = mode.getMenuInflater();
                            menuInflater.inflate(R.menu.menu,menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            isEnable = true;
                            ClickItem(holder);
                            mainViewModel.getText().observe((LifecycleOwner) activity, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    mode.setTitle(String.format("%s Selected",s));
                                }
                            });
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            int id = item.getItemId();
                            switch (id){
                                case R.id.menu_delete:
                                    for(String s:selectlist){
                                        contacts_list.remove(s);
                                    }
                                    if(contacts_list.size()==0){
                                        contacts_empty.setVisibility(View.VISIBLE);
                                    }
                                    mode.finish();
                                    break;
                                case R.id.menu_select_all:
                                    if(selectlist.size()==contacts_list.size()){
                                        isSelectedAll = false;
                                        selectlist.clear();
                                    }else{
                                        isSelectedAll = true;
                                        selectlist.clear();
                                        selectlist.addAll(contacts_list);
                                    }
                                    mainViewModel.setText(String.valueOf(selectlist.size()));
                                    notifyDataSetChanged();
                                    break;
                            }
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            isEnable = false;
                            isSelectedAll = false;
                            selectlist.clear();
                            notifyDataSetChanged();
                        }
                    };
                    ((AppCompatActivity) v.getContext()).startActionMode(callback);
                }else {
                    ClickItem(holder);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEnable){
                    ClickItem(holder);
                }else{
                    Toast.makeText(activity, "You Clicked:"+contacts_list.get(holder.getBindingAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(isSelectedAll){
            holder.contact_delete.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }else{
            holder.contact_delete.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void ClickItem(ViewHolder holder) {
        String s = contacts_list.get(holder.getBindingAdapterPosition());
        if(holder.contact_delete.getVisibility() == View.GONE){
            holder.contact_delete.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            selectlist.add(s);
        }else {
            holder.contact_delete.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            selectlist.remove(s);
        }
        mainViewModel.setText(String.valueOf(selectlist.size()));
    }

    @Override
    public int getItemCount() {
        return contacts_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView contact_number;
        ImageView contact_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            Integer pos = getBindingAdapterPosition();
            contact_number = itemView.findViewById(R.id.contact_number);
            contact_delete = itemView.findViewById(R.id.contact_delete);
//            contact_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    contacts_list.remove(pos);
//                    notifyItemRemoved(pos);
//                }
//            });

        }

        @Override
        public void onClick(View v) {
        }
    }

}
