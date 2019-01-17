package home.stanislavpoliakov.meet9_practice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntriesFragment extends Fragment implements DataSetChangeListener {
    private static final String TAG = "meet9_logs";
    private MyAdapter mAdapter;
    private List<Entry> entries = new ArrayList<>();
    private CRUDOperationsListener mActivity;

    public static EntriesFragment newInstance() {
        return new EntriesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivity = (CRUDOperationsListener) context;
        } catch (ClassCastException ex) {
            Log.w(TAG, "onAttach: Activity must implement CRUDOperationsListener", ex);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entries, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
    }

    /**
     * Метод инициализации RecyclerView
     * @param view созданное отображение фрагмента
     */
    private void initRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        mAdapter = new MyAdapter(getContext(), entries);
        recyclerView.setAdapter(mAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    /**
     * Метод для обработки пунтктов контекстного меню, созданного для конкретного ViewHolder'-а
     * @param item выбранный пункт
     * @return TODO пока не реализовано
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Log.d(TAG, "onContextItemSelected: " + item.getItemId());
        mActivity.deleteEntry(item.getItemId());
        return super.onContextItemSelected(item);
    }

    /**
     * Метод обновления данных. При начальной инициализации фрагмента mAdapter = null,
     * поэтому устанавливаем элементы в глобальной перменной, а Адаптер будет инициализирован через
     * свой метод (initRecyclerView). Впоследствии, при CRUD-операциях Адаптер уже будет
     * инициализирован, поэтому мы передаем управление обновлением ему.
     * @param entries
     */
    @Override
    public void updateDataSet(List<Entry> entries) {
        if (mAdapter == null) this.entries = entries;
        else mAdapter.onNewData(this.entries, entries);
    }
}
