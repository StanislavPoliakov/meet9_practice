package home.stanislavpoliakov.meet9_practice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CreateEntry extends AppCompatActivity {
    private EditText editTitle, editText;
    private Button createButton;
    private Entry entry;
    private static final String TAG = "meet9_logs";
    String defaultTitle;
    String defaultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_create_entry);

        initItems();
    }

    private void initItems() {
        defaultTitle = getResources().getString(R.string.def_title);
        defaultText = getResources().getString(R.string.def_text);

        editTitle = findViewById(R.id.editTitle);
        editText = findViewById(R.id.editText);

        editTitle.setOnFocusChangeListener(mFocusChangeListener);
        editText.setOnFocusChangeListener(mFocusChangeListener);

        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString();
                String text = editText.getText().toString();

                entry = new Entry(title, text);

                //Log.d(TAG, "onClick: " + editText.getText().toString());

                startActivity(MainActivity.newIntent(CreateEntry.this));
            }
        });

        //createButton.setOnHoverListener(hoverListener);
    }

    private View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            final int NO_FOCUS_COLOR = getResources().getColor(R.color.unfocusedColor, getTheme());
            final int ON_FOCUS_COLOR = getResources().getColor(R.color.focusedColor, getTheme());

            if (v != null) {
                EditText editView = (EditText) v;
                String currentValue = ((EditText) v).getText().toString();
                String defaultValue = (v.getId() == R.id.editTitle) ? defaultTitle : defaultText;

                if (hasFocus && defaultValue.equals(currentValue)) {
                    editView.setText("");
                    editView.setTextColor(ON_FOCUS_COLOR);
                    editView.setGravity(Gravity.NO_GRAVITY);
                } else if (!hasFocus && currentValue.isEmpty()) {
                    editView.setText(defaultValue);
                    editView.setTextColor(NO_FOCUS_COLOR);
                    editView.setGravity(Gravity.CENTER);
                }
            }
        }
    };

  /*  private View.OnHoverListener hoverListener = new View.OnHoverListener() {
        @Override
        public boolean onHover(View v, MotionEvent event) {
            String currentTitle = editTitle.getText().toString();
            String currentText = editText.getText().toString();

            if (defaultTitle.equals(currentTitle) || defaultText.equals(currentText)
                || currentText.isEmpty() || currentTitle.isEmpty()) v.setEnabled(false);
            else v.setEnabled(true);

            return v.isHovered();
        }
    };*/

    public static Intent newIntent(Context context) {
        //Log.d(TAG, "newIntent: ");
        return new Intent(context, CreateEntry.class);
    }
}
