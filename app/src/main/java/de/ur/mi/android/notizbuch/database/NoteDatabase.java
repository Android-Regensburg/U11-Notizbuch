package de.ur.mi.android.notizbuch.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import de.ur.mi.android.notizbuch.note.Note;

@Database(entities =  {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}
