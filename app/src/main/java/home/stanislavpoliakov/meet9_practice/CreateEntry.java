package home.stanislavpoliakov.meet9_practice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class CreateEntry extends AppCompatActivity {
    private EditText editTitle, editText;
    private Button createButton;
    private Entry entry;
    private static final String TAG = "meet9_logs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        initItems();
    }

    private void initItems() {
        editTitle = findViewById(R.id.editTitle);
        editText = findViewById(R.id.editText);
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
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, CreateEntry.class);
    }
}
