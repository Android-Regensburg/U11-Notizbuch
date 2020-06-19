package de.ur.mi.android.notizbuch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.ur.mi.android.notizbuch.config.AppConfig;
import de.ur.mi.android.notizbuch.note.Note;
import de.ur.mi.android.notizbuch.database.NoteDatabaseHelper;
import de.ur.mi.android.notizbuch.database.NoteQueryResultListener;
import de.ur.mi.android.notizbuch.fragments.ContentFragment;
import de.ur.mi.android.notizbuch.fragments.NoteListFragment;

public class MainActivity extends AppCompatActivity implements NoteQueryResultListener, NoteListFragment.OnListItemSelectedListener, ContentFragment.OnInputSubmitListener {

    NoteListFragment noteListFragment;
    NoteDatabaseHelper noteDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initUI();
        initDB();
    }

    private void initUI() {
        noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_list);
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
    }

    private void initDB() {
        noteDatabaseHelper = new NoteDatabaseHelper(this);
        noteDatabaseHelper.retrieveNotesFromDatabase(this);
    }

    @Override
    public void onListItemSelected(Note note) {
    }

    @Override
    public void onNoteCreated(Note note) {
    }

    @Override
    public void onNoteUpdated(Note note) {
    }

    @Override
    public void onNoteDeleted(Note note) {
    }

    @Override
    public void onListQueryResult(List<Note> notes) {
        if (noteListFragment != null) {
            noteListFragment.populateList(notes);
        }
    }
}
