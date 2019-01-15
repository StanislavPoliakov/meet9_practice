package home.stanislavpoliakov.meet9_practice;

import java.util.List;

/**
 * Интерфейс взаимодействия Activity и Фрагмента, отображающего список записей, по схеме: Activity -> Fragment
 */
public interface DataSetChangeListener {
    void updateDataSet(List<Entry> entries);
}
