package com.example.notesapp;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffCallBack extends DiffUtil.Callback{

    public DiffCallBack(List<Note> oldList, List<Note> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    private final List<Note> oldList;
    private final List<Note> newList;


    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldNote  = oldList.get(oldItemPosition);
        Note newNote  = newList.get(newItemPosition);


        return oldNote.getId() == newNote.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldNote  = oldList.get(oldItemPosition);
        Note newNote  = newList.get(newItemPosition);
        return oldNote.equals(newNote);
    }
}
