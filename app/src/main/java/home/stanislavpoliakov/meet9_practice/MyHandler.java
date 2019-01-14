package home.stanislavpoliakov.meet9_practice;

import android.os.Handler;
import android.os.Message;

public class MyHandler extends Handler {
    public final static int MSG_UPDATE_RECYCLER_VIEW = 1;

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == MSG_UPDATE_RECYCLER_VIEW) {

        }
    }
}
