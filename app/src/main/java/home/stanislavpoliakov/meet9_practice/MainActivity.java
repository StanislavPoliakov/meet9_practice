package home.stanislavpoliakov.meet9_practice;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    /**
     * Обработка выбора элемента контекстного меню
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.preferences) {

            PreferencesFragment fragment = new PreferencesFragment();

            fragmentManager.beginTransaction()
                    .add(fragment, "").commitNow();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (dbManager.getEntries().isEmpty()) {
            isEmptyState = true;
            initFragments();
        }
    }

    /**
     * Метод интерфейса для взаимодействия Фрагмента с Активити. Здесь мы запускаем Фрагмент
     * для редактирования записи, которому в качестве аргументов (setArguments) передаем значения
     * полей и позицию изменяемого элемента, которую мы просто вернем по dismiss в метод update
     * @param entryPosition
     */
    @Override
    public void editEntry(int entryPosition) {
        Bundle bundle = new Bundle();
        Entry entry = dbManager.getEntry(entryPosition);
        bundle.putString("title", entry.getTitle());
        bundle.putString("text", entry.getText());
        bundle.putInt("position", entryPosition);

        startEditFragment(bundle);
    }

    /**
     * Метод запуска фрагмента для редактирования записи
     * @param defaultFields
     */
    private void startEditFragment(Bundle defaultFields) {
        EditEntryDialogFragment dialogFragment = new EditEntryDialogFragment();
        dialogFragment.setArguments(defaultFields);
        fragmentManager.beginTransaction()
                .add(dialogFragment, "edit")
                .commitNow();
    }


    /**
     * Метод обновления записи в базе по позиции. Строго говоря, не обновления, а замены.
     * @param entry
     * @param entryPosition
     */
    @Override
    public void updateEntry(Entry entry, int entryPosition) {
        dbManager.putEntryIntoPosition(entry, entryPosition);
        fillRecycler();
    }

    /**
     * Метод заполнения RecyclerView данными из базы
     */
    private void fillRecycler() {
        mFragment.updateDataSet(dbManager.getEntries());
    }
}
