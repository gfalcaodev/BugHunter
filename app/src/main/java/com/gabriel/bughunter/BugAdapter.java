package com.gabriel.bughunter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.bughunter.databinding.ItemBugBinding;

import java.util.List;

public class BugAdapter extends RecyclerView.Adapter<BugViewHolder> {

    public interface OnBugClickListener {
        void onBugClick(Bug bug);
    }

    public interface OnBugLongClickListener {
        void onBugLongClick(Bug bug);
    }

    private final List<Bug> bugs;
    private final OnBugClickListener clickListener;
    private final OnBugLongClickListener longClickListener;

    public BugAdapter(List<Bug> bugs,
                      OnBugClickListener clickListener,
                      OnBugLongClickListener longClickListener) {
        this.bugs = bugs;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public BugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBugBinding binding = ItemBugBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new BugViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BugViewHolder holder, int position) {
        Bug bug = bugs.get(position);
        holder.bind(bug, clickListener, longClickListener);
    }

    @Override
    public int getItemCount() {
        return bugs.size();
    }
}
