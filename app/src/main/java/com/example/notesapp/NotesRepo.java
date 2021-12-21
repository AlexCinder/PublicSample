package com.example.notesapp;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class NotesRepo {
    private final NotesDao notesDao;
    private final NotesDatabase database;
    private final Executor executor;
    private final FirebaseFirestore fireStore;
    private final FirebaseAuth mAuth;
    private static final String TAG = "TAG";
    private List<Note> notes;

    public NotesRepo(Application application) {
        database = NotesDatabase.getInstance(application);
        notesDao = database.notesDao();
        executor = database.getExecutor();
        fireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        notes = new ArrayList<>();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getCloudData(NoteCallback noteCallback) {
        if (mAuth.getCurrentUser() != null) {
            fireStore.collection(mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            notes = queryDocumentSnapshots.toObjects(Note.class);
                            addNoteFromCloud(notes);
                            Log.i(TAG, "getCloudData inside OnSuccess: " + notes.size());
                            Log.i(TAG, "Thread: " + Thread.currentThread().getName());
                        }

                    }).addOnFailureListener(e -> Log.d(TAG, "onFailure: Get" + e.getMessage()));
        }
        Log.i(TAG, "getCloudData outside OnSuccess: " + notes.size());
//        Log.i(TAG, "Thread: " + Thread.currentThread().getName());

    }

    public LiveData<List<Note>> allNotes() {
        return notesDao.getAllNotes();
    }

    public LiveData<List<Note>> allQueryNotes(String newText) {
        return notesDao.querySearch(newText);
    }

    public void insertNoteTask(Note note) {

        executor.execute(() -> notesDao.insertNote(note));
    }

    public void deleteNoteTask(Note note) {
        executor.execute(() -> notesDao.deleteNote(note));

    }

    public void upNoteTask(Note note) {
        executor.execute(() -> notesDao.update(note));

    }

    public void syncDataRemote(List<Note> note) {
        Log.d(TAG, "syncDataRemote: check");
        if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "syncDataRemote: check2");
            for (Note n : note) {
                fireStore.collection(mAuth.getCurrentUser().getUid())
                        .document(String.valueOf(note.indexOf(n)))
                        .set(n)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                Log.d(TAG, "onSuccess: Add");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Add" + e.getMessage());
                    }
                });

            }

        }
    }

    public void deleteNotes() {
        executor.execute(notesDao::deleteAllNotes);
    }

    public void signOut() {
        deleteNotes();
        if (checkUser()) mAuth.signOut();
    }

    public boolean checkUser() {
        return mAuth.getCurrentUser() != null;
    }

    public void addNoteFromCloud(List<Note> note) {
        executor.execute(() -> {
            notesDao.addNotesFromCloud(note);
            Log.d(TAG, "run: " + note.size());
        });
    }
}
