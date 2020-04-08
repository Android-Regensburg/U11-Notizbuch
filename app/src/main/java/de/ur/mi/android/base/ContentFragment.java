package de.ur.mi.android.base;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import de.ur.mi.android.base.db.NoteDB;

public class ContentFragment extends Fragment implements View.OnClickListener {

    private OnListItemChangedListener mCallback;

    /**
     * This interface allows the fragment to communicate with the activity.
     * <p/>
     * <b>NOTE: When using the one fragment layout, this Interface must be implemented by the
     * <code>ContentActiviy</code> because the <code>ContentFragment</code> resides there. In the two
     * fragment layout on big devices, this fragment resides in the <code>MainActivity</code>.
     * Therefore, this Activity must implement the Interface, too.</b>
     */
    public interface OnListItemChangedListener {
        public void onListItemChanged();
    }

    public final static String ARG_ID = "id";
    private Note mNote = null;
    private boolean newNote = true;
    // Sets an ID for the notification
    private final static int NOTIFICATION_ID = 0;
    private final static String NOTIFICATION_CHANNEL_NAME = "CH0";
    private final static String NOTIFICATION_CHANNEL_ID = "0";

    public ContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        Button createOrUpdateButton = (Button) view.findViewById(R.id.button_updateNote);
        Button removeButton = (Button) view.findViewById(R.id.button_removeNote);
        createOrUpdateButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
        return view;
    }

    /**
     * Loads and views a note based on its id.
     *
     * @param id the id of the note to display
     */
    public void viewContent(int id) {
        mNote = new NoteDB(getActivity()).readEntryById(id);
        if (mNote != null) {
            EditText title = (EditText) getView().findViewById(R.id.content_title);
            TextView date = (TextView) getView().findViewById(R.id.content_date);
            EditText text = (EditText) getView().findViewById(R.id.content_text);
            title.setText(mNote.getTitle());
            date.setText(mNote.getDate());
            text.setText(mNote.getText());
            newNote = false;
        } else {
            loadEmptyView();
            Toast.makeText(getActivity(), "Error loading note!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Opens an empty content view for entering a new note
     */
    public void loadEmptyView() {
        mNote = new Note();
        emptyContentView();
        newNote = true;
    }

    /**
     * Removes the current entry
     *
     * @param view
     */
    public void removeEntry(View view) {
        if (mNote == null) {
            Toast.makeText(getActivity(), "Error, nothing was removed!", Toast.LENGTH_SHORT).show();
            return;
        }
        int numRemovedRows = new NoteDB(getActivity()).removeEntry(mNote.getId());
        if (numRemovedRows == 1) {
            Toast.makeText(getActivity(), "Note removed!", Toast.LENGTH_SHORT).show();
        } else if (numRemovedRows <= 0) {
            Toast.makeText(getActivity(), "Error, nothing was removed!", Toast.LENGTH_SHORT).show();
        }
        mNote = null;
        emptyContentView();
        // TODO Switch back to list fragment if we are in one fragment layout
    }

    private void emptyContentView() {
        EditText title = (EditText) getView().findViewById(R.id.content_title);
        TextView date = (TextView) getView().findViewById(R.id.content_date);
        EditText text = (EditText) getView().findViewById(R.id.content_text);
        title.setText("");
        date.setText("");
        text.setText("");
    }

    /**
     * Updates or creates the current note entry in the database
     *
     * @param view
     */
    public void updateOrSaveEntry(View view) {
        long status;
        EditText titleEditText = (EditText) getView().findViewById(R.id.content_title);
        EditText textEditText = (EditText) getView().findViewById(R.id.content_text);
        String title = titleEditText.getText().toString();
        if (title.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter a title!", Toast.LENGTH_SHORT).show();
            return;
        }
        String text = textEditText.getText().toString();
        mNote.setText(text);
        mNote.setTitle(title);
        if (newNote) {
            status = new NoteDB(getActivity()).insertEntry(text, title);
            mNote.setId((int) status);
            if (status == -1) {
                Toast.makeText(getActivity(), "Error inserting the note!", Toast.LENGTH_SHORT).show();
            } else if (status >= 0) {
                newNote = false;
                Toast.makeText(getActivity(), "Note inserted!", Toast.LENGTH_SHORT).show();
            }
            // Update the current entry
        } else {
            status = new NoteDB(getActivity()).updateEntry(mNote.getId(), text, title);
            if (status == -1) {
                Toast.makeText(getActivity(), "Error inserting the note!", Toast.LENGTH_SHORT).show();
            } else if (status >= 0) {
                Toast.makeText(getActivity(), "Note updated!", Toast.LENGTH_SHORT).show();
            }
        }
        if (status >= 0) {
            createNotification(title, text);
        }
    }

    /**
     * Shows a notification about the recently created or updated note
     *
     * @param title the title of the note
     * @param text  the text of the note
     */
    private void createNotification(String title, String text) {
        createNotificationChannel();
        Notification.Builder mBuilder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mBuilder =
                    new Notification.Builder(getActivity(), NOTIFICATION_CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(getString(R.string.app_name)).setContentText(title)
                            .setStyle(new Notification.BigTextStyle().bigText(text)).setAutoCancel(true);
        }
        else {
            mBuilder =
                    new Notification.Builder(getActivity()).setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(getString(R.string.app_name)).setContentText(title)
                            .setStyle(new Notification.BigTextStyle().bigText(text)).setAutoCancel(true);
        }

        // TODO set visibility to VISIBILITY_SECRET if we are on Lollipop or higher
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getActivity(), MainActivity.class);
        resultIntent
                .putExtra(MainActivity.INTENT_ITEM_SELECTED_NAME, MainActivity.INTENT_ITEM_SELECTED_ID);
        resultIntent.putExtra(ContentFragment.ARG_ID, mNote.getId());

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application to
        // the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_LOW;
            CharSequence channelName = NOTIFICATION_CHANNEL_NAME;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_updateNote:
                updateOrSaveEntry(view);
                mCallback.onListItemChanged();
                break;
            case R.id.button_removeNote:
                removeEntry(view);
                mCallback.onListItemChanged();
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("deprecation")
    // The new method onAttach(Context context) doesn't exist in API level 22 and below
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mNote = new Note();
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnListItemChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement OnListItemChangedListener!");
        }
    }
}
