package de.ur.mi.android.notizbuch.database;

import java.util.List;

import de.ur.mi.android.notizbuch.note.Note;

public interface NoteQueryResultListener {

    void onListQueryResult(List<Note> notes);
}
