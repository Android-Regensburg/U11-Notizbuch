package de.ur.mi.android.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import de.ur.mi.android.base.db.NoteDB;

public class MyListFragment extends ListFragment {

    private OnListItemSelectedListener mCallback;

    /**
     * This interface allows the fragment to communicate with the activity
     */
    public interface OnListItemSelectedListener {
        void onListItemSelected(int id);
    }

    public MyListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateList();
    }

    /**
     * Loads the content of the list. This method is both called when the Fragment is loaded and when
     * an update is required (after an entry was added, updated or removed).
     * <p/>
     * <b>NOTE: Usually, updating a <code>ListView</code> should be done with calling the Adapters
     * <code>notifyDataSetChanged()</code> method. But in order to show the dummy entry when the list
     * is empty, the list is recreated from the beginning.</b>
     */
    protected void populateList() {
        Note[] values = new NoteDB(getActivity()).readEntriesTitle();
        // Use dummy not for informing about empty list
        if (values.length == 0) {
            Note dummy = new Note(getString(R.string.text_noNotes), "", "");
            dummy.setId(-1);
            values = new Note[]{dummy};
        }
        ArrayAdapter<Note> adapter =
                new ArrayAdapter<Note>(getActivity(), android.R.layout.simple_expandable_list_item_1,
                        values);
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateList();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Note clickedNote = (Note) l.getItemAtPosition(position);
        Toast.makeText(getActivity(),
                "Clicked on pos: " + position + "; id: " + id + "; Date: " + clickedNote.getDate(),
                Toast.LENGTH_LONG).show();
        // Prevent opening the dummy entry
        if (clickedNote.getId() == -1) {
            return;
        }
        mCallback.onListItemSelected(clickedNote.getId());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
            try {
                mCallback = (OnListItemSelectedListener) a;
            } catch (ClassCastException e) {
                throw new ClassCastException(
                        a.toString() + " must implement OnListItemSelectedListener!");
            }
        }
    }
}
