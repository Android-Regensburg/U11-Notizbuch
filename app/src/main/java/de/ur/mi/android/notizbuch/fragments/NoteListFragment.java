package de.ur.mi.android.notizbuch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

import de.ur.mi.android.notizbuch.adapter.NoteListAdapter;
import de.ur.mi.android.notizbuch.R;
import de.ur.mi.android.notizbuch.note.Note;

public class NoteListFragment extends ListFragment {

    private NoteListAdapter adapter;
    private OnListItemSelectedListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (OnListItemSelectedListener) context;
        init();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    private void init() {
        adapter = new NoteListAdapter(getActivity(), new ArrayList<Note>());
        setListAdapter(adapter);
    }

    public void populateList(List<Note> notes) {
        adapter.addNoteList(notes);
    }

    public void addNoteToList(Note note) {
        adapter.addNote(note);
    }

    public void updateNoteInList(Note note) {
        adapter.updateNote(note);
    }

    public void deleteNoteInList(Note note) {
        adapter.deleteNote(note);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        listener.onListItemSelected(adapter.getItem(position));
    }

    public interface OnListItemSelectedListener {
        void onListItemSelected(Note note);
    }
}
