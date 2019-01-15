package home.stanislavpoliakov.meet9_practice;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class CreateEntryDialogFragment extends DialogFragment {
    private EditText editTitle, editText;
    private Button createButton;
    private Entry entry;
    private static final String TAG = "meet9_logs";
    String defaultTitle;
    String defaultText;
    private CRUDOperationsListener mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivity = (CRUDOperationsListener) context;
        } catch (ClassCastException ex) {
            Log.d(TAG, "Main Activity must implement CRUDOperationsListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_create_entry, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initItems(view);
    }

    private void initItems(View view) {
        defaultTitle = getResources().getString(R.string.def_title);
        defaultText = getResources().getString(R.string.def_text);

        editTitle = view.findViewById(R.id.editTitle);
        editText = view.findViewById(R.id.editText);

        editTitle.setOnFocusChangeListener(mFocusChangeListener);
        editText.setOnFocusChangeListener(mFocusChangeListener);

        createButton = view.findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString();
                String text = editText.getText().toString();

                entry = new Entry(title, text);

                mActivity.putEntry(entry);
                dismiss();
                //Log.d(TAG, "onClick: " + editText.getText().toString());

                //startActivity(MainActivity.newIntent(CreateEntry.this));
            }
        });

        //createButton.setOnHoverListener(hoverListener);
    }

    private View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            final int NO_FOCUS_COLOR = getResources().getColor(R.color.unfocusedColor, getContext().getTheme());
            final int ON_FOCUS_COLOR = getResources().getColor(R.color.focusedColor, getContext().getTheme());

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
}
