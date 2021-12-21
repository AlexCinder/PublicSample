package com.example.notesapp;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

public class AddNoteViewModel extends AndroidViewModel {

    private final NotesRepo notesRepo;

    public AddNoteViewModel(@NonNull Application application) {
        super(application);
        notesRepo = new NotesRepo(application);

    }

    public void upNoteTask(Note note) {
        notesRepo.upNoteTask(note);

    }

    public void insertNoteTask(Note note) {
         notesRepo.insertNoteTask(note);
    }
}
