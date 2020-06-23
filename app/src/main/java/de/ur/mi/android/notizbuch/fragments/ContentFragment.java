package de.ur.mi.android.notizbuch.fragments;

import android.content.Context;
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

    private OnInputSubmitListener listener;
    private Note note;

    private TextView txtTitle, txtContent;
    private Button btnCreate,
            btnDelete;
    
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (OnInputSubmitListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        txtTitle = view.findViewById(R.id.content_title);
        txtContent = view.findViewById(R.id.content_text);
        btnCreate = view.findViewById(R.id.btn_create);
        btnDelete = view.findViewById(R.id.btn_delete);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntry();
            }
        });
    }

    private void saveEntry() {
        String title = txtTitle.getText().toString(),
                content = txtContent.getText().toString();
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(getActivity(), "Bitte alle Felder ausfüllen!", Toast.LENGTH_SHORT).show();
            return;
        }
        DateFormat df = new SimpleDateFormat("d MMM yyyy", Locale.GERMANY);
        String date = df.format(Calendar.getInstance().getTime());
        if (note != null) {
            note.setTitle(title);
            note.setContent(content);
            listener.onNoteUpdated(note);
            note = null;
            return;
        }
        listener.onNoteCreated(new Note(title, content, date));
    }

    private void deleteEntry() {
        if (note != null) {
            listener.onNoteDeleted(note);
        }
        note = null;
    }

    public void loadEmptyView() {
        txtTitle.setText("");
        txtContent.setText("");
        btnDelete.setVisibility(View.INVISIBLE);
        btnCreate.setText(getString(R.string.add_note));
    }

    public void setNote(Note note) {
        this.note = note;
        txtTitle.setText(note.getTitle());
        txtContent.setText(note.getContent());
        btnDelete.setVisibility(View.VISIBLE);
        btnCreate.setText(getString(R.string.update_note));
    }

    public interface OnInputSubmitListener {

        void onNoteCreated(Note note);
        void onNoteUpdated(Note note);
        void onNoteDeleted(Note note);
    }
}
