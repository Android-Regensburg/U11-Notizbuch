package de.ur.mi.android.notizbuch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.ur.mi.android.notizbuch.R;
import de.ur.mi.android.notizbuch.note.Note;

public class ContentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        return view;
    }

    public interface OnInputSubmitListener {

        void onNoteCreated(Note note);
        void onNoteUpdated(Note note);
        void onNoteDeleted(Note note);
    }
}
