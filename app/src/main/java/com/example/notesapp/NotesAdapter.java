package com.example.notesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    private List<Note> notes;
    private OnNoteClickListener onNoteClickListener;



    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }

    interface OnNoteClickListener {
        void onNoteClick(int position);
    }

    public NotesAdapter(List<Note> notes) {
        this.notes = notes;
    }


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent,
                false);

        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvDescription.setText(note.getDescription());
        holder.tvDayOfWeek.setText(String.format("Remind on: %s",note.getDayOfWeek()));
        holder.tvLastEdit.setText(String.format("Last edit: %s", note.getLastEditDate()));
        int colorId;
        int priority = note.getPriority();
        switch (priority) {
            case 1:
                colorId = holder.itemView.getResources().getColor(R.color.priority_1);
                break;
            case 2:
                colorId = holder.itemView.getResources().getColor(R.color.priority_2);
                break;
            default:
                colorId = holder.itemView.getResources().getColor(R.color.priority_3);
        }
        holder.tvTitle.setTextColor(colorId);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onNoteClickListener != null) {
                    onNoteClickListener.onNoteClick(holder.getAdapterPosition());
                }
            }
        });


    }

    public void showChanges(List<Note> oldList, List<Note> newList) {
        DiffUtil.DiffResult onAdapterChanges = DiffUtil
                .calculateDiff(new DiffCallBack(oldList, newList));
        notes = newList;
        onAdapterChanges.dispatchUpdatesTo(this);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final TextView tvDescription;
        final TextView tvDayOfWeek;
        final TextView tvLastEdit;


        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            tvLastEdit = itemView.findViewById(R.id.last_date);


        }
    }
}
