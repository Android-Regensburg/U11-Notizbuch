package de.ur.mi.android.notizbuch.database;

import android.app.Activity;

import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executors;

import de.ur.mi.android.notizbuch.note.Note;

public class NoteDatabaseHelper {

    private static final String DATABASE_NAME = "note-db";
    private NoteDatabase db;

    private final Activity activityContext;

    public NoteDatabaseHelper(Activity activityContext) {
        this.activityContext = activityContext;
        initDatabase();
    }

    private void initDatabase() {
        db = Room.databaseBuilder(activityContext, NoteDatabase.class, DATABASE_NAME).build();
    }

    public void addNoteToDatabase(Note note) {
        new AddNoteTask(note).start();
    }

    public void updateNoteInDatabase(Note note) {
        new UpdateNoteTask(note).start();
    }

    public void deleteNoteFromDatabase(Note note) {
        new DeleteSingleNoteTask(note).start();
    }

    public void retrieveNotesFromDatabase(NoteQueryResultListener listener) {
        new RetrieveNoteListTask(listener).start();
    }

    private static abstract class DBTask implements Runnable{

        void start() {
            Executors.newSingleThreadExecutor().submit(this);
        }
    }

    private class AddNoteTask extends DBTask {

        private Note note;

        public AddNoteTask(Note note) {
            this.note = note;
        }

        @Override
        public void run() {
            db.noteDao().addNote(note);
        }
    }

    private class UpdateNoteTask extends DBTask {

        private Note note;

        public UpdateNoteTask(Note note) {
            this.note = note;
        }

        @Override
        public void run() {
            db.noteDao().updateNote(note);
        }
    }

    private class DeleteSingleNoteTask extends DBTask {

        private Note note;

        public DeleteSingleNoteTask(Note note) {
            this.note = note;
        }

        @Override
        public void run() {
            db.noteDao().deleteNote(note);
        }
    }

    private class RetrieveNoteListTask extends DBTask {

        private NoteQueryResultListener listener;

        public RetrieveNoteListTask(NoteQueryResultListener listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            final List<Note> list = db.noteDao().getAllNotes();
            activityContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.onListQueryResult(list);
                }
            });
        }
    }
}
