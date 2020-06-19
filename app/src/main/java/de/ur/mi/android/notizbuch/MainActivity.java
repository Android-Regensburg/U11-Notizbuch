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

public class MainActivity extends AppCompatActivity implements NoteListFragment.OnListItemSelectedListener, NoteQueryResultListener, ContentFragment.OnInputSubmitListener {

    ContentFragment contentFragment;
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
        contentFragment = (ContentFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_list);
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentLayout();
            }
        });
    }

    private void initDB() {
        noteDatabaseHelper = new NoteDatabaseHelper(this);
        noteDatabaseHelper.retrieveNotesFromDatabase(this);
    }

    private void showContentLayout() {
        if (contentFragment != null) {
            contentFragment.loadEmptyView();
        } else {
            Intent intent = new Intent(this, ContentActivity.class);
            startActivityForResult(intent, AppConfig.RESULT_REQUEST_CODE);
        }
    }

    @Override
    public void onListItemSelected(Note note) {
        if (contentFragment != null) {
            contentFragment.setNote(note);
        } else {
            Intent intent = new Intent(this, ContentActivity.class);
            intent.putExtra(AppConfig.NOTE_EXTRA_KEY, note);
            startActivityForResult(intent, AppConfig.RESULT_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.RESULT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Note note = data.getParcelableExtra(AppConfig.NOTE_EXTRA_KEY);
            int responseCode = data.getIntExtra(AppConfig.RESPONSE_EXTRA_KEY, -1);
            switch (responseCode) {
                case AppConfig.RESPONSE_ADD:
                    addNote(note);
                    break;
                case AppConfig.RESPONSE_UPDATE:
                    updateNote(note);
                    break;
                case AppConfig.RESPONSE_DELETE:
                    deleteNote(note);
                    break;
                default:
                    Toast.makeText(this, "Something went wrong with the database.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addNote(Note note) {
        if (contentFragment != null) contentFragment.loadEmptyView();
        noteDatabaseHelper.addNoteToDatabase(note);
        noteListFragment.addNoteToList(note);
    }

    private void updateNote(Note note) {
        if (contentFragment != null) contentFragment.loadEmptyView();
        noteDatabaseHelper.updateNoteInDatabase(note);
        noteListFragment.updateNoteInList(note);
    }

    private void deleteNote(Note note) {
        if (contentFragment != null) contentFragment.loadEmptyView();
        noteDatabaseHelper.deleteNoteFromDatabase(note);
        noteListFragment.deleteNoteInList(note);
    }

    @Override
    public void onListQueryResult(List<Note> notes) {
        if (noteListFragment != null) {
            noteListFragment.populateList(notes);
        }
    }

    @Override
    public void onNoteCreated(Note note) {
        addNote(note);
    }

    @Override
    public void onNoteUpdated(Note note) {
        updateNote(note);
    }

    @Override
    public void onNoteDeleted(Note note) {
        deleteNote(note);
    }
}
