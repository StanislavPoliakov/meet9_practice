package home.stanislavpoliakov.meet9_practice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "database"; //database.db? -> database.db.sqlite?

    public DBHelper(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    private DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS entries" +
                "(" +
                "entry_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "title TEXT NOT NULL," +
                "entry_text TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTable(db);
        onCreate(db);
    }

    public void deleteTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS entries");
    }
}
