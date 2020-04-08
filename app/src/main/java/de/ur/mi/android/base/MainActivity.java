package de.ur.mi.android.base;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements MyListFragment.OnListItemSelectedListener, ContentFragment.OnListItemChangedListener {

    // Identifying the Intent that launched the app is required, because we have to differ between
    // launch from the app screen of the device and from a notification (this means a redirect to the
    // content view in the one fragment layout
    public static final int INTENT_ITEM_SELECTED_ID = 0;
    public static final String INTENT_ITEM_SELECTED_NAME = "IntentForLoadingSelectedNote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Load note when app was launched via notification and therefore started with an Intent
        Intent intent = getIntent();
        if (intent != null) {
            // Only switch when it was our own Intent (and not the one from the launch screen)
            if (intent.getIntExtra(INTENT_ITEM_SELECTED_NAME, -1) == INTENT_ITEM_SELECTED_ID) {
                onListItemSelected(intent.getIntExtra(ContentFragment.ARG_ID, -1));
            }
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentFragment cf =
                        (ContentFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_content);
                if (cf != null) {
                    cf.loadEmptyView();
                } else {
                    Toast.makeText(MainActivity.this, "Content Fragment not there, switching!",
                            Toast.LENGTH_SHORT).show();
                    // ContentFragment (Fragment B) is not in the layout (handset layout),
                    // so start ContentActivity (Activity B) and pass it the info about the selected item
                    Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemSelected(int id) {
        ContentFragment cf =
                (ContentFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        if (cf != null) {
            cf.viewContent(id);
        } else {
            Toast.makeText(this, "Content Fragment not there, switching!", Toast.LENGTH_SHORT).show();
            // ContentFragment (Fragment B) is not in the layout (handset layout),
            // so start ContentActivity (Activity B) and pass it the info about the selected item
            Intent intent = new Intent(this, ContentActivity.class);
            intent.putExtra(ContentFragment.ARG_ID, id);
            startActivity(intent);
            //cf = new ContentFragment();
            //Bundle args = new Bundle();
            //args.putInt(ContentFragment.ARG_ID, id);
            //cf.setArguments(args);
            //FragmentTransaction transaction = getFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            //transaction.replace(R.id.fragment_list, cf);
            //transaction.addToBackStack(null);
            //transaction.commit();
        }
    }

    @Override
    public void onListItemChanged() {
        MyListFragment lf =
                (MyListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_list);
        if (lf != null) {
            lf.populateList();
        }
        // No "else" cases needed because in the one fragment layout on small devices, the reloading
        // is done automatically because the list fragment will be reloaded
    }
}
