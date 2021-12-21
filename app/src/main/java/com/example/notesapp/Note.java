package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "ROOM_notes")
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String description;
    private String dayOfWeek;
    private int priority;
    private String uri;

    @Ignore
    public Note() {
    }

    public String getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(String lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    private String lastEditDate;

    public Note(long id, String title, String description, String dayOfWeek, int priority,
                String uri, String lastEditDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dayOfWeek = dayOfWeek;
        this.priority = priority;
        this.uri = uri;
        this.lastEditDate = lastEditDate;
    }

    @Ignore
    public Note(String title, String description, String dayOfWeek, int priority,
                String uri, String lastEditDate) {
        this.title = title;
        this.description = description;
        this.dayOfWeek = dayOfWeek;
        this.priority = priority;
        this.uri = uri;
        this.lastEditDate = lastEditDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    public long getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public int getPriority() {
        return priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return getId() == note.getId() && getPriority() == note.getPriority() && Objects.equals(getTitle(), note.getTitle()) && Objects.equals(getDescription(), note.getDescription()) && getDayOfWeek().equals(note.getDayOfWeek()) && getLastEditDate().equals(note.getLastEditDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getDayOfWeek(), getPriority(), getLastEditDate());
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
