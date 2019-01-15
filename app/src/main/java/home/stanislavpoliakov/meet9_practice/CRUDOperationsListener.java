package home.stanislavpoliakov.meet9_practice;

/**
 * Интерфейс взаимодействия Фрагмента (создание записи) и Activity по схеме: Fragment -> Activity
 */
public interface CRUDOperationsListener {
    void putEntry(Entry entry);
    void deleteEntry(int entryPosition);
}
