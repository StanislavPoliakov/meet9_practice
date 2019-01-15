package home.stanislavpoliakov.meet9_practice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс работы с базой данных
 */
public class DBManager {
    private static final String TAG = "meet9_logs";
    private DBHelper dbHelper;

    public DBManager(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    /**
     * Метод получения данных из открытой для чтения базы. В этом методе мы делаем запрос
     * в базу данных, получаем объект Курсор на результаты выборки.
     * @return список записей базы (весь, без выборки SELECT)
     */
    public List<Entry> getEntries() {
        List<Entry> entryList = new ArrayList<>();

        SQLiteDatabase database = dbHelper.getReadableDatabase(); // Получаем базу, открытую для чтения
        try {
            database.beginTransaction();

            // Запрос в базу на вывод всех ее элементов. Получаем объект Курсор, перебирая который
            // получаем все записи базы по указанному запросу
            Cursor cursor = database.query("entries", null, null, null,
                    null, null, null);
            entryList = getEntryList(cursor);
            cursor.close(); // Закрываем Курсор

            database.setTransactionSuccessful();
            database.endTransaction(); // Закрываем транзакцию
        } catch (SQLException ex) {
            Log.w(TAG, "getEntries: ", ex);
        } finally {
            database.close(); // Закрываем базу данных
        }
        return entryList;
    }

    /**
     * Метод получения данных из выборки по запросу
     * @param cursor объект Курсора на результаты выборки
     * @return список элементов выборки
     */
    private List<Entry> getEntryList(Cursor cursor) {
        List<Entry> entryList = new ArrayList<>();

        // Устанавливаем Курсор в первую позицию выборки и листаем записи до тех пор, пока
        // Курсор не пройдет их все и не встанет за последним элементом
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            // Получаем поля из выборки
            String timeStamp = cursor.getString(cursor.getColumnIndex("timestamp"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String text = cursor.getString(cursor.getColumnIndex("entry_text"));

            Entry entry = new Entry(title, text);

            // TODO После реализации всех фич принять решение добавлять ли timestamp в конструктор
            // TODO или оставлять сеттер
            entry.setTimeStamp(timeStamp);

            entryList.add(entry);

            cursor.moveToNext(); // Перемещаем Курсор к следующему элементу
        }
        return entryList;
    }

    /**
     * Метод для записи объекта данных в базу, открытую для записи
     * @param entry объект данных, который необходимо сохранить в базе
     */
    public void putEntry(Entry entry) {
        SQLiteDatabase database = dbHelper.getWritableDatabase(); // Получаем базу, открытую для записи

        // contentValues - набор значений (имя столбца таблицы и значение), которые мы будем записывать в базу
        // Набор значений получаем в отдельном методе
        ContentValues contentValues = getContentValues(entry);

        try {
            database.beginTransaction();

            database.insert("entries", null, contentValues);

            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (SQLException ex) {
            Log.w(TAG, "putEntry: ", ex);
        } finally {
            database.close();
        }
    }

    /**
     * Метод формирования набора значений для записи в базу данных
     * @param entry Объект данных, который мы будем парсить
     * @return набор данных для записи
     */
    private ContentValues getContentValues(Entry entry) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("title", entry.getTitle()); // Заголовок записи
        contentValues.put("entry_text", entry.getText()); // Текст записи
        // Стоит отметить, что состояние объекта записи timestamp мы формируем не в самом объекте класса Entry,
        // а в момент записи в базу данных. Фактически, timestamp - это поле базы данных, которое формируется
        // в момент записи данных (insert). Мы же присваиваем это значение объекту класса Entry во
        // время чтения записи из базы данных

        return contentValues;
    }
}
