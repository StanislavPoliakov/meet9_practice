package home.stanislavpoliakov.meet9_practice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CRUDOperationsListener {
    private static final String TAG = "meet9_logs";
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Messenger fragmentMessenger;
    //private Messenger activityMessenger = new Messenger(new IncomingHandler());
    private DBManager dbManager;
    private DataSetChangeListener mFragment;

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        initViewItems();
        initFragments();
    }

    private void initViewItems() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbManager = new DBManager(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(CreateEntry.newIntent(MainActivity.this));
                //Log.d(TAG, "onClick: ");

                CreateEntryDialogFragment dialogFragment = new CreateEntryDialogFragment();
                //dialogFragment.show(fragmentManager, "create dialog");

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(dialogFragment, "dialog")
                        .commit();
            }
        });
    }

    private void initFragments(){

        //TODO Сделать логику отображения фрагментов в зависимости от наличия записей

        //EntriesFragment fragment = (EntriesFragment) fragmentManager.findFragmentById(R.id.entriesFragment);


        //fragment = (fragment == null) ? new EntriesFragment() : fragment;
        //mFragment = (DataSetChangeListener) fragment;

        Bundle bundle = new Bundle();



        fragmentManager.beginTransaction()
         //       .add(R.id.mainFrame, NoEntries.newInstance(), "No Entries")
                .add(R.id.mainFrame, EntriesFragment.newInstance(), "EntriesFragment")
                .commitNow();

        mFragment = (DataSetChangeListener) fragmentManager.findFragmentByTag("EntriesFragment");


        fillRecycler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //((MenuBuilder) menu).setOptionalIconsVisible(true);
        //menu.add(menuItemWithIconAndText(R.drawable.font_change, "new"));
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
            DBManager dbManager = new DBManager(this);
            /*Entry entry1 = new Entry("Хуяйтл", "Хуекст");
            dbManager.putEntry(entry1);
            dbManager.putEntry(new Entry("123", "asdas"));*/
            List<Entry> entryList = dbManager.getEntries();
            for (Entry entry : entryList) {
                //Log.d(TAG, "TIMESTAMP = " + entry.getTimeStamp());
                Log.d(TAG, "TITLE = " + entry.getTitle());
                Log.d(TAG, "TEXT = " + entry.getText());
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateFragment(List<Entry> data) {
        /*Fragment fragment = fragmentManager.findFragmentByTag("EntriesFragment");
        fragment.set*/
    }

    @Override
    public void putEntry(Entry entry) {
        Log.d(TAG, "putEntry: ");
        dbManager.putEntry(entry);
    }

    private void fillRecycler() {
        Log.d(TAG, "fillRecycler: ");
        mFragment.updateDataSet(dbManager.getEntries());
        Log.d(TAG, "fillRecycler: ");
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}
