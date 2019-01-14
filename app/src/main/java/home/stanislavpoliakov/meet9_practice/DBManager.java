package home.stanislavpoliakov.meet9_practice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static final String TAG = "meet9_logs";
    private DBHelper dbHelper;

    public DBManager(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    public void test() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        //Log.d(TAG, "test: db = " + database.getPath());
        database.close();
    }

    public List<Entry> getEntries() {
        List<Entry> entries = new ArrayList<>();

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        try {
            database.beginTransaction();
            Cursor cursor = database.query("entries", null, null, null,
                    null, null, null);
            //getEntryByCursor(cursor);
            entries = getEntryList(cursor);
            cursor.close();
            //Log.d(TAG, "getEntries: db.size = " + database.);
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (SQLException ex) {
            //Log.d(TAG, "getEntries: SQL Exception = " + ex.getMessage());
            Log.w(TAG, "getEntries: ", ex);
            //ex.printStackTrace();
        } finally {
            database.close();
        }

        Log.d(TAG, "getEntries: entries.size = " + entries.size());

        return entries;
    }

    private List<Entry> getEntryList(Cursor cursor) {
        List<Entry> entryList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
           // Log.d(TAG, "entry title = " + cursor.getString(cursor.getColumnIndex("name")));
            String title = cursor.getString(cursor.getColumnIndex("name"));
            String text = cursor.getString(cursor.getColumnIndex("entry_text"));
            entryList.add(new Entry(title, text));

            cursor.moveToNext();
        }
        return entryList;
    }

    public void putEntry(Entry entry) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
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

    private ContentValues getContentValues(Entry entry) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", entry.getTitle());
        contentValues.put("entry_text", entry.getText());
        return contentValues;
    }
}
