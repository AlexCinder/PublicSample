package com.example.notesapp;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.Executor;

public class MainViewModel extends AndroidViewModel {


    private final LiveData<List<Note>> allNotes;
    private MutableLiveData<String> allQueryNotes;
    private final NotesRepo notesRepo;



    public MainViewModel(@NonNull Application application) {
        super(application);
        notesRepo = new NotesRepo(application);
        allQueryNotes = new MutableLiveData<>("");
        allNotes = Transformations.switchMap(allQueryNotes, input -> {
            if (TextUtils.isEmpty(input)) {
                return notesRepo.allNotes();
            } else return notesRepo.allQueryNotes(input);
        });


    }

    public void deleteNotes() {
        notesRepo.deleteNotes();
    }

    public void syncData(List<Note> note) {
        notesRepo.syncDataRemote(note);
    }

    public LiveData<List<Note>> getLiveNotes() {
        return allNotes;
    }

    public void setFilter(String query) {
        this.allQueryNotes.setValue(query);
    }

    public void addNoteFromCloud(List<Note> note) {
        notesRepo.addNoteFromCloud(note);
    }

    public void deleteNoteTask(Note note) {
        notesRepo.deleteNoteTask(note);
    }


    @Override
    protected void onCleared() {
        super.onCleared();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getCloudData(NoteCallback noteCallback) {

         notesRepo.getCloudData(noteCallback);
    }

    public void signOut() {
        notesRepo.signOut();
    }

    public boolean checkUser() {
        return notesRepo.checkUser();
    }
}
