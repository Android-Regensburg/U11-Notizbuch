package de.ur.mi.android.notizbuch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.ur.mi.android.notizbuch.config.AppConfig;
import de.ur.mi.android.notizbuch.note.Note;
import de.ur.mi.android.notizbuch.fragments.ContentFragment;

public class ContentActivity extends AppCompatActivity implements ContentFragment.OnInputSubmitListener{

    private ContentFragment contentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        Note note = getIntent().getParcelableExtra(AppConfig.NOTE_EXTRA_KEY);
        if (contentFragment != null) {
            if (note != null) {
                contentFragment.setNote(note);
            } else {
                contentFragment.loadEmptyView();
            }
        }
    }

    private void initUI() {
        setContentView(R.layout.activity_content);
        contentFragment = (ContentFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_content);
    }

    private void passInputResult(Note note, int responseCode) {
        Intent intent = new Intent();
        intent.putExtra(AppConfig.NOTE_EXTRA_KEY, note);
        intent.putExtra(AppConfig.RESPONSE_EXTRA_KEY, responseCode);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onNoteCreated(Note note) {
        passInputResult(note, AppConfig.RESPONSE_ADD);
    }

    @Override
    public void onNoteUpdated(Note note) {
        passInputResult(note, AppConfig.RESPONSE_UPDATE);
    }

    @Override
    public void onNoteDeleted(Note note) {
        passInputResult(note, AppConfig.RESPONSE_DELETE);
    }
}
