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
        // An dieser Stelle wird eine Instanz des NoteListFragments bezogen
        /*
         TODO: Beziehen und speichern Sie eine Instanz des ContentFragments.
          Diese hat den Wert 'null' wenn das Fragment im Layout nicht geladen wurde, in diesem Fall müssen Sie die ContentActivity starten
         */
        noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_list);
        /*
         TODO: Belegen Sie den Button mit einem Listener. Starten Sie in der Callback-Methode
           die ContentActivity für einen Rückgabewert, oder eine leere Eingabemaske im ContentFragment
         */
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
    }

    private void initDB() {
        noteDatabaseHelper = new NoteDatabaseHelper(this);
        noteDatabaseHelper.retrieveNotesFromDatabase(this);
    }

    @Override
    public void onListItemSelected(Note note) {
        /*
            In dieser Callback-Methode werden angeklickte Notizen übergeben

            TODO: Laden Sie die übergebene Notiz in die Felder der Eingabemaske.
             Sie müssen unterscheiden welche Layoutkonstellation vorliegt (ob das Fragment geladen wurde)
         */
    }

    /*
        TODO: In den folgenden Callback-Methoden muss die erhaltene Notiz verarbeitet werden.
         Diese muss mit den passenden Methoden über den NoteDatabaseHelper an die Datenbank
         und das NoteListFragment übergeben werden
     */

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
