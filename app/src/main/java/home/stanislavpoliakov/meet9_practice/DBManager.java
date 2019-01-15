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
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.deleteTable(db);
        db.close();
    }

    public List<Entry> getEntries() {
        List<Entry> entryList;

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        try {
            database.beginTransaction();
            Cursor cursor = database.query("entries", null, null, null,
                    null, null, null);
            //getEntryByCursor(cursor);
            entryList = getEntryList(cursor);
            cursor.close();
            //Log.d(TAG, "getEntries: db.size = " + database.);
            database.setTransactionSuccessful();
            database.endTransaction();
            return entryList;
        } catch (SQLException ex) {
            //Log.d(TAG, "getEntries: SQL Exception = " + ex.getMessage());
            Log.w(TAG, "getEntries: ", ex);
            //ex.printStackTrace();
        } finally {
            database.close();
        }

        //Log.d(TAG, "getEntries: entries.size = " + entries.size());

        return null;
    }

    private List<Entry> getEntryList(Cursor cursor) {
        List<Entry> entryList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
           // Log.d(TAG, "entry title = " + cursor.getString(cursor.getColumnIndex("name")));
            String timeStamp = cursor.getString(cursor.getColumnIndex("timestamp"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String text = cursor.getString(cursor.getColumnIndex("entry_text"));

            Entry entry = new Entry(title, text);
            entry.setTimeStamp(timeStamp);
            entryList.add(entry);

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
