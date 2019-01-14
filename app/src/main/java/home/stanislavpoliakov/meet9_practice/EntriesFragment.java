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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntriesFragment extends Fragment {
    private static final String TAG = "meet9_logs";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<MyAdapter.MyViewHolder> mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Entry> entries = new ArrayList<>();
    public static final int MSG_FRAGMENT_CREATED = 0;

    public static EntriesFragment newInstance() {
        return new EntriesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entries, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
    }

    private void initRecyclerView(@NonNull View view) {
        initList();

        recyclerView = view.findViewById(R.id.recyclerView);

        mAdapter = new MyAdapter(getContext(), entries);
        recyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setSelected(true);
        //recyclerView.setScroll
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d(TAG, "onContextItemSelected: ");
        return super.onContextItemSelected(item);
    }

    private void initList() {
        entries.add(new Entry("First Title", ""));
        entries.add(new Entry("First Title", ""));
        entries.add(new Entry("First Title", ""));
        entries.add(new Entry("First Title", ""));
        entries.add(new Entry("First Title", ""));
        entries.add(new Entry("First Title", ""));
        entries.add(new Entry("First Title", ""));
        entries.add(new Entry("First Title", ""));
        entries.add(new Entry("First Title", ""));
        entries.add(new Entry("First Title", ""));
        entries.add(new Entry("First Title", ""));
        entries.add(new Entry("First Title", ""));
    }
}
