package com.example.notesapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    private static volatile NotesDatabase database;
    private static final String DB_NAME = "room_notes.db";
    private static final Executor notesWriter =
            Executors.newSingleThreadExecutor();
    public static NotesDatabase getInstance(final Context context) {
        if (database == null) {
            synchronized (NotesDatabase.class) {
                if (database == null) {
                    database = Room
                            .databaseBuilder(context.getApplicationContext(),
                                    NotesDatabase.class, DB_NAME)
                            .build();


                }
            }

        }
        return database;
    }

    public Executor getExecutor(){
        return notesWriter;
    }

    public abstract NotesDao notesDao();

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
