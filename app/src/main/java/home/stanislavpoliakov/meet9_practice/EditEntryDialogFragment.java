package home.stanislavpoliakov.meet9_practice;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Класс, описывающий создание новой записи. Запускается в dialog-фрагменте
 */
public class EditEntryDialogFragment extends DialogFragment {
    private EditText editTitle, editText;
    private Button updateButton;
    private Entry entry;
    private static final String TAG = "meet9_logs";
    String defaultTitle;
    String defaultText;
    private CRUDOperationsListener mActivity;

    /**
     * В момент присоединения фрагмента (attach) получаем вызывающий контекст для того,
     * чтобы фрагмент мог взаимодействовать с вызывающей его Activity
     * @param context
     */
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

    /**
     * В методе onStart устанавливаем размер dialog-окна по размеру вызывающей Activity, то есть,
     * фактически, по размеру экрана
     */
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

    /**
     * Watcher для обработки изменений текстовых полей
     */
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkEditViews(); // вытащил в отдельный метод
        }
    };

    /**
     * Метод проверки состояний полей ввода для активации (деактивации) кнопки
     */
    private void checkEditViews() {
        if (editTitle.getText().toString().equals(defaultTitle)
                || editTitle.getText().toString().isEmpty()
                || editText.getText().toString().equals(defaultText)
                || editText.getText().toString().isEmpty()) updateButton.setEnabled(false);
        else updateButton.setEnabled(true);
    }

    private void initItems(View view) {
        final Bundle bundle = getArguments();
        PrefStorage prefStorage = new PrefStorage(getContext());

        // Инициализируем default-значения полей ввода через R.string
        defaultTitle = getResources().getString(R.string.def_title);
        defaultText = getResources().getString(R.string.def_text);

        editTitle = view.findViewById(R.id.editTitle);
        setLoadedPrefs(editTitle, prefStorage.getCETitlePrefs());
        editText = view.findViewById(R.id.editText);
        setLoadedPrefs(editText, prefStorage.getCETextPrefs());

        editTitle.setText(bundle.getString("title", defaultTitle));
        editText.setText(bundle.getString("text", defaultText));

        if (editText.getText().toString().equals(defaultText)) {
            //editText.setTextColor(getResources().getColor(R.color.unfocusedColor, getContext().getTheme()));
            editText.setGravity(Gravity.CENTER);
        } else {
            //editText.setTextColor(getResources().getColor(R.color.focusedColor, getContext().getTheme()));
            editText.setGravity(Gravity.NO_GRAVITY);
        }

        if (editTitle.getText().toString().equals(defaultTitle)) {
            //editTitle.setTextColor(getResources().getColor(R.color.unfocusedColor, getContext().getTheme()));
            editTitle.setGravity(Gravity.CENTER);
        } else {
            //editTitle.setTextColor(getResources().getColor(R.color.focusedColor, getContext().getTheme()));
            editTitle.setGravity(Gravity.NO_GRAVITY);
        }

        //mFocusChangeListener - для добавления эффекта "значений по умолчанию" для пустого поля
        editTitle.setOnFocusChangeListener(mFocusChangeListener);
        editText.setOnFocusChangeListener(mFocusChangeListener);

        editTitle.addTextChangedListener(textWatcher);
        editText.addTextChangedListener(textWatcher);

        TextView entryLabel = view.findViewById(R.id.entryLabel);
        entryLabel.setText(getResources().getString(R.string.edit_entry_label));

        updateButton = view.findViewById(R.id.createButton);
        updateButton.setText(getResources().getString(R.string.button_update_label));
        checkEditViews();

        updateButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Нажатие на кнопку "создать"
             * @param v
             */
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString();
                String text = editText.getText().toString();

                entry = new Entry(title, text); // Создаем новый объект класса Entry

                // Через метод интерфейса просим Activity инициализировать добавление записи в базу данных
                mActivity.updateEntry(entry, bundle.getInt("position"));

                // Закрываем dialog-окно
                dismiss();
            }
        });
    }

    private void setLoadedPrefs(TextView view, PrefData preferences) {
        view.setTextSize(preferences.size);
        view.setTypeface(view.getTypeface(), preferences.style);
        view.setTextColor(preferences.color);
    }

    /**
     * Реализация эффекта "значений полей по умолчанию" при пустых полях ввода. Также меняем цвет
     * полей и расположение текста
     */
    private View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            // Серый цвет текста, если значение в поле не введено (отображается default-значение)
            final int NO_FOCUS_COLOR = getResources().getColor(R.color.unfocusedColor, getContext().getTheme());

            // Черный цвет текста, если значение в поле введено
            final int ON_FOCUS_COLOR = getResources().getColor(R.color.focusedColor, getContext().getTheme());

            if (v != null) {
                EditText editView = (EditText) v;
                String currentValue = ((EditText) v).getText().toString();
                String defaultValue = (v.getId() == R.id.editTitle) ? defaultTitle : defaultText;

                /**
                 * Если мы нажимаем на поле (hasFocus) и пока ничего не вводили (defaultValue.equals(currentValue)),
                 * делаем поле пустым, ставим цвет текста = черный, убираем Gravity
                 */
                if (hasFocus && defaultValue.equals(currentValue)) {
                    editView.setText("");
                    editView.setTextColor(ON_FOCUS_COLOR);
                    editView.setGravity(Gravity.NO_GRAVITY);

                    /**
                     * Если поле теряет фокус, но при этом ничего не было введено, возвращаем полям
                     * default-значения, ставим серый цвет и ровняем по центру
                     */
                } else if (!hasFocus && currentValue.isEmpty()) {
                    editView.setText(defaultValue);
                    editView.setTextColor(NO_FOCUS_COLOR);
                    editView.setGravity(Gravity.CENTER);
                }
            }
        }
    };
}
