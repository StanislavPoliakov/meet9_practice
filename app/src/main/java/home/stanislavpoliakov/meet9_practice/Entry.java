package home.stanislavpoliakov.meet9_practice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Entry {
    private String timeStamp, title, text;
    private boolean isLarge;
    private static final String TAG = "meet9_logs";

    public Entry(String title, String text) {
        //setTimeStamp();
        this.title = title;
        this.text = text;

        isLarge = false;
    }

    public void update(String title) {
        //setTimeStamp();
        this.title = title;
    }

    public void update(String title, String text) {
        //setTimeStamp();
        this.title = title;
        this.text = text;
    }

    public void setTimeStamp(String timeStamp) {
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm / dd.MM");
        //Log.d(TAG, "setTimeStamp: " + dateFormat.format(Calendar.getInstance().getTime()));
        Calendar calendar = Calendar.getInstance();
        timeStamp = dateFormat.format(calendar.getTime());*/
        this.timeStamp = timeStamp;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public String getTitle() {
        return this.title;
    }

    public String getText() {
        return this.text;
    }

    public boolean isLarge() {
        return this.isLarge;
    }
}
