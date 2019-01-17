package home.stanislavpoliakov.meet9_practice;

import android.support.v7.util.DiffUtil;
import android.util.Log;

import java.util.List;

/**
 * Класс обработки различий DiffUtils
 */
public class DiffCall extends DiffUtil.Callback {
    private static final String TAG = "meet9_logs";
    private List<Entry> oldData, newData;

    public DiffCall(List<Entry> oldData, List<Entry> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData.size();
    }

    //TODO Обработать различия в содержимом

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
