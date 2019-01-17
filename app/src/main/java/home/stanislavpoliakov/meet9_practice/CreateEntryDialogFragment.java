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
public class CreateEntryDialogFragment extends DialogFragment {
    private EditText editTitle, editText;
    private Button createButton;
    private Entry entry;
    private static final String TAG = "meet9_logs";
    String defaultTitle;
    String defaultText;
    private CRUDOperationsListener mActivity;
    private PrefStorage prefStorage;

    /**
     * Определяем переменную типа TextWatcher для того, чтобы отлавливать изменения в поле ввода
     * в процессе ввода значений.
     */
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        /**
         * Описываю реализацию только последнего метода, так как нужно уже по факту (а не в процессе)
         * изменять состояние кнопки. Надо сказать, что получается все равно в процессе. По факту
         * введенного символа. Учитывая, что кнопка становится активной, если введенные значения
         * не пустые и не дефолтные - то подойдет любой метод.
         * @param s
         */
        @Override
        public void afterTextChanged(Editable s) {
            if (editTitle.getText().toString().equals(defaultTitle)
                    || editTitle.getText().toString().isEmpty()
                    || editText.getText().toString().equals(defaultText)
                    || editText.getText().toString().isEmpty()) createButton.setEnabled(false);
            else createButton.setEnabled(true);
        }
    };

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

    private void initItems(View view) {

        prefStorage = new PrefStorage(getContext()); // Загружаем настройки

        // Инициализируем default-значения полей ввода через R.string
        defaultTitle = getResources().getString(R.string.def_title);
        defaultText = getResources().getString(R.string.def_text);

        editTitle = view.findViewById(R.id.editTitle);
        setLoadedPrefs(editTitle, prefStorage.getCETitlePrefs()); // применяем настройки
        editText = view.findViewById(R.id.editText);
        setLoadedPrefs(editText, prefStorage.getCETextPrefs()); // применяем настройки

        //mFocusChangeListener - для добавления эффекта "значений по умолчанию" для пустого поля
        editTitle.setOnFocusChangeListener(mFocusChangeListener);
        editText.setOnFocusChangeListener(mFocusChangeListener);

        // Добавляем обработчик события ввода текста для активации (или деактивации) кнопки
        editTitle.addTextChangedListener(textWatcher);
        editText.addTextChangedListener(textWatcher);

        createButton = view.findViewById(R.id.createButton);

        createButton.setOnClickListener(new View.OnClickListener() {

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
                mActivity.putEntry(entry);

                // Закрываем dialog-окно
                dismiss();
            }
        });
    }

    /**
     * Метод применения настроек
     * @param view, к которой мы будем применять изменения
     * @param preferences - загруженные настройки, которые мы будем применять
     */
    private void setLoadedPrefs(TextView view, PrefData preferences) {
        view.setTextSize(preferences.size);
        view.setTypeface(view.getTypeface(), preferences.style);
        view.setTextColor(preferences.color);
    }

    /**
     * Реализация эффекта "значений полей по умолчанию" при пустых полях ввода. Также меняем цвет
     * полей и расположение текста.
     *
     * ВНИМАНИЕ: цвет будет меняться в зависимости от настроек и в зависимости от значений полей.
     * Может показаться, что это изменение неподконтрольно, однако, я специально не дорабатывал
     * до конца реализацию, чтобы продемонстрировать обе фичи. Вообще, нужно определить "светлый" тон
     * и "темный" тон для текста, и в зависимости от настроек цвета выставлять тон математически.
     * Ничего сложно - времени просто не осталось :) TODO сделать математический обсчет тонов
     */
    private View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            // Серый цвет текста, если значение в поле не введено (отображается default-значение)
            final int NO_FOCUS_COLOR = getResources().getColor(R.color.unfocusedColor, getContext().getTheme());

            // Черный цвет текста, если значение в поле введено
            final int ON_FOCUS_COLOR = getResources().getColor(R.color.focusedColor, getContext().getTheme());
            //int ON_FOCUS_COLOR = prefStorage.getCETitlePrefs().color;

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
