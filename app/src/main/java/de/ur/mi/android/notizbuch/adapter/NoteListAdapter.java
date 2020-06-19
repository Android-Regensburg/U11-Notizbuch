package de.ur.mi.android.notizbuch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.ur.mi.android.notizbuch.R;
import de.ur.mi.android.notizbuch.note.Note;

public class NoteListAdapter extends ArrayAdapter<Note> {

    private ArrayList<Note> notes;
    private Context context;


    public NoteListAdapter(Context context, ArrayList<Note> notes) {
        super(context, R.layout.note_layout, notes);
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.note_layout, null);
        }
        TextView title = v.findViewById(R.id.txt_title),
                content = v.findViewById(R.id.txt_content),
                date = v.findViewById(R.id.txt_date);
        Note note = notes.get(position);
        title.setText(note.getTitle());
        content.setText(note.getContent());
        date.setText(note.getDate());

        return v;
    }

    public void addNoteList(List<Note> notes) {
        this.notes.addAll(notes);
        this.notifyDataSetChanged();
    }

    public void addNote(Note note) {
        this.notes.add(note);
        this.notifyDataSetChanged();
    }

    public void updateNote(Note note) {
        notes.set(findNoteIndexForID(note.getId()), note);
        this.notifyDataSetChanged();
    }

    public void deleteNote(Note note) {
        notes.remove(findNoteIndexForID(note.getId()));
        this.notifyDataSetChanged();
    }

    private int findNoteIndexForID(int id) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
