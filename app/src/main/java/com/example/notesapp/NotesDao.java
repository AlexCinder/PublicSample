package com.example.notesapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface NotesDao {

    @Query("SELECT * FROM ROOM_notes ORDER BY priority ASC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM ROOM_notes WHERE id = :id")
    LiveData<Note> getById(long id);

    @Query("SELECT * FROM ROOM_notes WHERE" +
            " title LIKE '%' || :newText || '%' ORDER BY priority ASC")
    LiveData<List<Note>> querySearch(String newText);


    // returns id of the inserted element
    @Insert
    long insertNote(Note note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNotesFromCloud(List<Note> note);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM ROOM_notes")
    void deleteAllNotes();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Note note);

}
