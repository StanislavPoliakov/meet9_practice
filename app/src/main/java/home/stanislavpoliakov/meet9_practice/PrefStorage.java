package home.stanislavpoliakov.meet9_practice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PrefStorage {
    private static final String TAG = "meet9_logs";
    private static final String CREATE_AND_EDIT = "CE_PREF";
    private static final String RECYCLER_VIEW = "RV_PREF";

    private static final String TITLE_SIZE = "TITLE_SIZE";
    private static final String TITLE_STYLE = "TITLE_STYLE";
    private static final String TITLE_COLOR = "TITLE_COLOR";

    private static final String TEXT_SIZE = "TEXT_SIZE";
    private static final String TEXT_STYLE = "TEXT_STYLE";
    private static final String TEXT_COLOR = "TEXT_COLOR";

    private static final String TIMESTAMP_SIZE = "TIMESTAMP_SIZE";
    private static final String TIMESTAMP_STYLE = "TIMESTAMP_STYLE";
    private static final String TIMESTAMP_COLOR = "TIMESTAMP_COLOR";

    private Context context;
    private static PrefStorage instance;

    public PrefStorage(Context context) {
        this.context = context;
    }

    private PrefData setupDefaultCETitlePrefs() {
        PrefData ceTitleDefault = new PrefData();
        ceTitleDefault.size = 18;
        ceTitleDefault.style = 0;
        ceTitleDefault.color = Color.parseColor("#A2AAB3");

        return ceTitleDefault;
    }

    private PrefData setupDefaultCETextPrefs() {
        PrefData ceTextDefault = new PrefData();
        ceTextDefault.size = 18;
        ceTextDefault.style = 0;
        ceTextDefault.color = Color.parseColor("#A2AAB3");

        return ceTextDefault;
    }

    private PrefData setupDefaultRVTitlePrefs() {
        PrefData rvTitleDefault = new PrefData();
        rvTitleDefault.size = 16;
        rvTitleDefault.style = 1;
        rvTitleDefault.color = Color.parseColor("#457D8F");

        return rvTitleDefault;
    }

    private PrefData setupDefaultRVTextPrefs() {
        PrefData rvTextDefault = new PrefData();
        rvTextDefault.size = 14;
        rvTextDefault.style = 2;
        rvTextDefault.color = Color.parseColor("#457D8F");

        return rvTextDefault;
    }

    private PrefData setupDefaultRVTimestampPrefs() {
        PrefData rvTimestampDefault = new PrefData();
        rvTimestampDefault.size = 10;
        rvTimestampDefault.style = 3;
        rvTimestampDefault.color = Color.parseColor("#457D8F");

        return rvTimestampDefault;
    }

    public PrefData getCETitlePrefs() {
        PrefData prefs = new PrefData();
        PrefData defaultPrefs = setupDefaultCETitlePrefs();

        SharedPreferences preferences = context.getSharedPreferences(CREATE_AND_EDIT, Context.MODE_PRIVATE);

        prefs.size = preferences.getFloat(TITLE_SIZE, defaultPrefs.size);
        prefs.style = preferences.getInt(TITLE_STYLE, defaultPrefs.style);
        prefs.color = preferences.getInt(TITLE_COLOR, defaultPrefs.color);

        return prefs;
    }

    public PrefData getCETextPrefs() {
        PrefData prefs = new PrefData();
        PrefData defaultPrefs = setupDefaultCETextPrefs();

        SharedPreferences preferences = context.getSharedPreferences(CREATE_AND_EDIT, Context.MODE_PRIVATE);

        prefs.size = preferences.getFloat(TEXT_SIZE, defaultPrefs.size);
        prefs.style = preferences.getInt(TEXT_STYLE, defaultPrefs.style);
        prefs.color = preferences.getInt(TEXT_COLOR, defaultPrefs.color);

        return prefs;
    }

    public void saveCETitlePrefs(PrefData ceTitle) {
        SharedPreferences preferences = context.getSharedPreferences(CREATE_AND_EDIT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(TITLE_SIZE, ceTitle.size);
        editor.putInt(TITLE_STYLE, ceTitle.style);
        editor.putInt(TITLE_COLOR, ceTitle.color);
        editor.apply();
    }

    public void saveCETextPrefs(PrefData ceText) {
        SharedPreferences preferences = context.getSharedPreferences(CREATE_AND_EDIT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(TEXT_SIZE, ceText.size);
        editor.putInt(TEXT_STYLE, ceText.style);
        editor.putInt(TEXT_COLOR, ceText.color);
        editor.apply();
    }

    public PrefData getRVTitlePrefs() {
        PrefData prefs = new PrefData();
        PrefData defaultPrefs = setupDefaultRVTitlePrefs();

        SharedPreferences preferences = context.getSharedPreferences(RECYCLER_VIEW, Context.MODE_PRIVATE);

        prefs.size = preferences.getFloat(TITLE_SIZE, defaultPrefs.size);
        prefs.style = preferences.getInt(TITLE_STYLE, defaultPrefs.style);
        prefs.color = preferences.getInt(TITLE_COLOR, defaultPrefs.color);
        Log.d(TAG, "getRVTitlePrefs: ");
        return prefs;
    }

    public PrefData getRVTextPrefs() {
        PrefData prefs = new PrefData();
        PrefData defaultPrefs = setupDefaultRVTextPrefs();

        SharedPreferences preferences = context.getSharedPreferences(RECYCLER_VIEW, Context.MODE_PRIVATE);

        prefs.size = preferences.getFloat(TEXT_SIZE, defaultPrefs.size);
        prefs.style = preferences.getInt(TEXT_STYLE, defaultPrefs.style);
        prefs.color = preferences.getInt(TEXT_COLOR, defaultPrefs.color);
        Log.d(TAG, "getRVTextPrefs: ");
        return prefs;
    }

    public PrefData getRVTimestampPrefs() {
        PrefData prefs = new PrefData();
        PrefData defaultPrefs = setupDefaultRVTimestampPrefs();

        SharedPreferences preferences = context.getSharedPreferences(RECYCLER_VIEW, Context.MODE_PRIVATE);

        prefs.size = preferences.getFloat(TIMESTAMP_SIZE, defaultPrefs.size);
        prefs.style = preferences.getInt(TIMESTAMP_STYLE, defaultPrefs.style);
        prefs.color = preferences.getInt(TIMESTAMP_COLOR, defaultPrefs.color);

        return prefs;
    }

    public void saveRVTitlePrefs(PrefData rvTitle) {
        SharedPreferences preferences = context.getSharedPreferences(RECYCLER_VIEW, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(TITLE_SIZE, rvTitle.size);
        editor.putInt(TITLE_STYLE, rvTitle.style);
        editor.putInt(TITLE_COLOR, rvTitle.color);
        editor.apply();
    }

    public void saveRVTextPrefs(PrefData rvText) {
        SharedPreferences preferences = context.getSharedPreferences(RECYCLER_VIEW, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(TEXT_SIZE, rvText.size);
        editor.putInt(TEXT_STYLE, rvText.style);
        editor.putInt(TEXT_COLOR, rvText.color);
        editor.apply();
    }

    public void saveRVTimestampPrefs(PrefData rvTimestamp) {
        SharedPreferences preferences = context.getSharedPreferences(RECYCLER_VIEW, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(TIMESTAMP_SIZE, rvTimestamp.size);
        editor.putInt(TIMESTAMP_STYLE, rvTimestamp.style);
        editor.putInt(TIMESTAMP_COLOR, rvTimestamp.color);
        editor.apply();
    }
}
