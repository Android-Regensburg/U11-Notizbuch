package de.ur.mi.android.notizbuch.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.ur.mi.android.notizbuch.note.Note;

@Dao
public interface NoteDao {

    @Insert
    void addNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM note")
    List<Note> getAllNotes();
}
