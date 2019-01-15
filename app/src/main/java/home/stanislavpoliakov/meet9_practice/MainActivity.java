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
    private DBManager dbManager;
    private DataSetChangeListener mFragment;
    private boolean isEmptyState;
    //private boolean isFirstStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewItems();
        initFragments();
    }

    /**
     * Инициализируем элементы Activity
     */
    private void initViewItems() {
        // Верхняя панель с меню настроек TODO реализовать изменения SharedPreferences
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbManager = new DBManager(this);
        isEmptyState = dbManager.getEntries().size() == 0;

        // Кнопка добавления новой записи
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            /**
             * По нажатию кнопки открываем dialog-фрагмент
             * @param v
             */
            @Override
            public void onClick(View v) {

                CreateEntryDialogFragment dialogFragment = new CreateEntryDialogFragment();

                //TRANSITION_FRAGMENT_CLOSE - удаляет фрагмент из стека. Не релизовывал

                fragmentManager.beginTransaction()
                        .add(dialogFragment, "dialog")
                        .commit();
            }
        });
    }

    /**
     * Метод инициализации фрагментов. Если записей в базе нет - один фрагмент, если есть - другой
     */
    private void initFragments(){
        if (isEmptyState) {
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, NoEntries.newInstance(), "No Entries")
                    .commitNow();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, EntriesFragment.newInstance(), "EntriesFragment")
                    .commitNow();
            mFragment = (DataSetChangeListener) fragmentManager.findFragmentByTag("EntriesFragment");
            fillRecycler();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateFragment(List<Entry> data) {
        /*Fragment fragment = fragmentManager.findFragmentByTag("EntriesFragment");
        fragment.set*/
    }

    /**
     * Метод интерфейса взаимаодействия. Метод добавления новой записи в базу
     * @param entry запись, которую необходимо добавить
     */
    @Override
    public void putEntry(Entry entry) {
        dbManager.putEntry(entry);

        // Если в базе не было записей, то сначала инициализируем соответствующий фрагмент
        if (isEmptyState) {
            isEmptyState = false;
            initFragments();
        } else fillRecycler();
    }

    /**
     * Метод удаления выбранной записи.
     * @param entryPosition позиция элемента, на котором было вызвано контекстное меню и нажата кнопка
     *                      DELETE
     */
    @Override
    public void deleteEntry(int entryPosition) {
        dbManager.deleteEntry(entryPosition);
        fillRecycler();
    }

    /**
     * Метод заполнения RecyclerView данными из базы
     */
    private void fillRecycler() {
        mFragment.updateDataSet(dbManager.getEntries());
    }
}
