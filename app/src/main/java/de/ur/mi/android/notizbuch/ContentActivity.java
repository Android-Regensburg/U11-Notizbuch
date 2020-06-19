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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
    }

    @Override
    public void onNoteCreated(Note note) { ;
    }

    @Override
    public void onNoteUpdated(Note note) {
    }

    @Override
    public void onNoteDeleted(Note note) {

    }
}
