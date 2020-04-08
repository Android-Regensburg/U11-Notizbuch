package de.ur.mi.android.base;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

public class ContentActivity extends AppCompatActivity implements ContentFragment.OnListItemChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // TODO Use the ContentFragment in order to show the article or load an empty
        // view (dummy id is -1) when a new note should be created

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
