package home.stanislavpoliakov.meet9_practice;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Класс Фрагмента, описвающего поведение при отстутствии записей в базе
 */
public class NoEntries extends Fragment {

    public static NoEntries newInstance() {
        return new NoEntries();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_entries, container, false);
    }



}
