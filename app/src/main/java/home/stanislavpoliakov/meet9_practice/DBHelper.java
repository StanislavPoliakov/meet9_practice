package home.stanislavpoliakov.meet9_practice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * Класс, настраивающий базу данных для работы
 */
public class DBHelper extends SQLiteOpenHelper {

    // Версия и имя базы данных устанавливаем константно
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "database"; //database.db? -> database.db.sqlite?

    /**
     * Перегружаем конструктор, чтобы снизить количество параметров для вызова
     * Отсюда запускаем нужный конструтор (который мы сделали приватным)
     * @param context контекст вызова
     */
    public DBHelper(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    private DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Метод создания базы данных.
     * Уникальный ключ формируем из id-записи, то есть ключ записи делаем синтетическим.
     * Возможен и натуральный ключ - это timestamp. Оставил synthetic.
     * timestamp - генерируемый базой штамп времени. Удобно.
     * @param db база данных, которую мы создаем
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        deleteTable(db);
        db.execSQL("CREATE TABLE IF NOT EXISTS entries" +
                "(" +
                "entry_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "timestamp DATETIME NOT NULL DEFAULT (strftime('%H:%M / %d.%m', 'now', 'localtime'))," +
                "title TEXT NOT NULL," +
                "entry_text TEXT)");

        // strftime - это STRING FORMAT TIME для штампа времени SQLite
        // '%H:%M / %d.%m' - собственно, сам формат даты и времени
        //now - текущие дата и время
        //localtime - время текущего региона

        // TODO выбрать PRIMARY KEY: synthetic или natural?
    }

    /**
     * Обновление через сброс
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTable(db);
        onCreate(db);

        //TODO Если этот метод когда-нибудь потребуется - сделать обновление через контроль версий
        //TODO и посредство написания ALTER MODIFY запросов
    }

    /**
     * Сброс базы
     * @param db
     */
    private void deleteTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS entries");
    }
}
