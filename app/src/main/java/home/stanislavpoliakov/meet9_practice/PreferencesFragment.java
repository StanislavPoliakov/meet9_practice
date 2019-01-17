package home.stanislavpoliakov.meet9_practice;


import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Фрагмент настроек
 */
public class PreferencesFragment extends DialogFragment {
    private static final String TAG = "meet9_logs";
    private TextView editTitle, editText, recyclerViewTitle, recyclerViewText, recyclerViewTimeStamp;
    private EditText fontColor;
    private Spinner fontSize, fontStyle;
    private View divider;
    private ImageView prefTitleStar, prefTextStar, prefRecTitleStar, prefRecTextStar, prefRecTimeStar; // звездочки
    private ArrayAdapter<CharSequence> styleAdapter, sizeAdapter, colorAdapter;
    private View currentTextView;
    private PrefStorage prefStorage;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preferences_fragment, container, false);
    }

    /**
     * Заполняем фрагмент по ширине (на экран) и по высоте (на контент)
     */
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
    }

    private void init(View view) {
        prefStorage = new PrefStorage(getContext());

        Button savePrefsButton = view.findViewById(R.id.savePrefsButton);
        savePrefsButton.setOnClickListener(new View.OnClickListener() {

            /**
             * По нажатию на кнопку "Сохранить" обходим все TextView из превью, значения которых надо сохранить,
             * собираем значения и отправляем на сохранение в SharedPreferences
             * @param v
             */
            @Override
            public void onClick(View v) {

                prefStorage.saveCETitlePrefs(getPrefs(editTitle));
                prefStorage.saveCETextPrefs(getPrefs(editText));
                prefStorage.saveRVTitlePrefs(getPrefs(recyclerViewTitle));
                prefStorage.saveRVTextPrefs(getPrefs(recyclerViewText));
                prefStorage.saveRVTimestampPrefs(getPrefs(recyclerViewTimeStamp));

                dismiss();
            }
        });

        fontColor = view.findViewById(R.id.fontColor);

        /**
         * Этот Watcher нужен для того, чтобы изменить цвет текста элемента, когда значение в поле
         * цвета валидно (равняется шести знакам. Ограничения на используемые знаки, а также на
         * максимальное количество симоволов ввода, установлены в XML-файле
         */
        fontColor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (s.length() == 6) {

                        ((TextView) currentTextView).setTextColor(Color.parseColor("#" + s.toString()));
                    }
                } catch (IllegalArgumentException ex) {
                    Log.w(TAG, "Error: ", ex);
                }
            }
        });

        prepareCreateEditExample(view);
        prepareRecyclerViewExample(view);
        initSpinners(view);
    }

    /**
     *  Метод "подготовки" превью для элементов RecyclerView. Инициализируем элементы,
     *  устанавливаем в них значения из настроек (SP)
     * @param view общая ViewGroup, среди которой будем искать наши элементы
     */
    private void prepareRecyclerViewExample(View view) {

        View recyclerViewExample = view.findViewById(R.id.recyclerViewExample);

        recyclerViewTitle = recyclerViewExample.findViewById(R.id.titleTextView);
        setLoadedPrefs(recyclerViewTitle, prefStorage.getRVTitlePrefs());
        recyclerViewTitle.setOnClickListener(mOnClickListener);

        recyclerViewText = recyclerViewExample.findViewById(R.id.entryTextView);
        setLoadedPrefs(recyclerViewText, prefStorage.getRVTextPrefs());
        recyclerViewText.setOnClickListener(mOnClickListener);

        recyclerViewTimeStamp = recyclerViewExample.findViewById(R.id.timeStampLabel);
        setLoadedPrefs(recyclerViewTimeStamp, prefStorage.getRVTimestampPrefs());
        recyclerViewTimeStamp.setOnClickListener(mOnClickListener);
    }

    /**
     *  Метод для "подготовки" превью для фрагментов Создания и Редактирования элементов
     * @param view
     */
    private void prepareCreateEditExample(View view) {

        View createExample = view.findViewById(R.id.createExample);

        prefTitleStar = createExample.findViewById(R.id.prefTitleStar);
        prefTitleStar.setVisibility(View.VISIBLE);

        // Это звездочка. Добавлена был для идеи того, чтобы наглядно было видно, что изменилось, а
        // что нет в настройках. То есть для трекинга изменений. На реализацию не осталось времени.
        // Здесь сделали ее прозрачной на 50% - в глаза слишком бросалась.
        prefTitleStar.setAlpha(0.5f);

        prefTextStar = createExample.findViewById(R.id.prefTextStar);
        prefTextStar.setVisibility(View.VISIBLE);
        prefTextStar.setAlpha(0.5f);

        editTitle = createExample.findViewById(R.id.editTitle);
        setLoadedPrefs(editTitle, prefStorage.getCETitlePrefs());
        editTitle.setOnClickListener(mOnClickListener);

        editText = createExample.findViewById(R.id.editText);
        setLoadedPrefs(editText, prefStorage.getCETextPrefs());
        editText.setOnClickListener(mOnClickListener);

        // Чтобы нельзя было нажать на EditText и менять там значения. setEnabled(false) не подходит,
        // потому что становится не наглядным из-за своей обработки UI
        editTitle.setFocusable(false);
        editTitle.setClickable(false);

        editText.setFocusable(false);
        editTitle.setClickable(false);

        divider = view.findViewById(R.id.divider3); // Инициализирую, потому что буду прятать
    }

    private void setLoadedPrefs(TextView view, PrefData preferences) {
        view.setTextSize(preferences.size);
        view.setTypeface(view.getTypeface(), preferences.style);
        view.setTextColor(preferences.color);
    }

    /**
     *  Метод полчения данных от TextView, которые необходимо сохранять
     * @param view
     * @return
     */
    private PrefData getPrefs(TextView view) {
        PrefData preferences = new PrefData();
        float pxTextSize = view.getTextSize();
        float spTextSize = Math.round(pxTextSize / getResources().getDisplayMetrics().scaledDensity);
        preferences.size = spTextSize;
        preferences.style = view.getTypeface().getStyle();
        preferences.color = view.getTextColors().getDefaultColor();
        return preferences;
    }

    /**
     * Это обработка нажатий на элементы TextView, для того, чтобы в спиннерах отображались текущие
     * для TextView элементы, а в поле цвета - hex-код цвета
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentTextView = v;
            setOptionsVisibility(true);

            float pxTextSize = ((TextView) v).getTextSize();
            int spTextSize = Math.round(pxTextSize / getResources().getDisplayMetrics().scaledDensity);

            int sizeAdapterPosition = sizeAdapter.getPosition(String.valueOf(spTextSize));
            fontSize.setSelection(sizeAdapterPosition);

            Typeface textTypeface = ((TextView) v).getTypeface();
            int textStyle = textTypeface.getStyle();
            fontStyle.setSelection(textStyle);

            ColorStateList stateList = ((TextView) v).getTextColors();
            String hexColor = String.format("%06X", (0xFFFFFF & stateList.getDefaultColor()));

            fontColor.setText(hexColor);
        }
    };

    /**
     * Инициализация спиннеров
     * @param view
     */
    private void initSpinners(View view) {
        fontSize = view.findViewById(R.id.fontSize);

        sizeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.font_size, android.R.layout.simple_spinner_item);

        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fontSize.setAdapter(sizeAdapter);
        fontSize.setSelection(1);
        fontSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /**
             * Обработка события выбора размера текста в спиннере. Вызывается каждый раз, когда мы кликаем
             * по TextView, потому что в onClick мы делаем setSelection для спиннеров
             * @param parent адаптер спиннера
             * @param view сам спиннер. Интересно, что можно установить адаптивные значения. Приятно.
             * @param position позиция выбора в списке значений
             * @param id полагаю, что это id значения
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getSelectedItem();
                float size = Float.parseFloat(selectedItem);
                ((TextView) currentTextView).setTextSize(size);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fontStyle = view.findViewById(R.id.fontStyle);
        styleAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.font_style, android.R.layout.simple_spinner_item);
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontStyle.setAdapter(styleAdapter);
        fontStyle.setSelection(1);
        fontStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /**
             * То же самое, только для стиля текста
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Typeface typeface = ((TextView) currentTextView).getTypeface();
                ((TextView) currentTextView).setTypeface(typeface, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setOptionsVisibility(false);
    }

    /**
     * Метод, который прячет (по умолчанию) и показывает (по нажатию) панель инструментов для выбора
     * настроек текста
     * @param isVisible - делаем видимым?
     */
    private void setOptionsVisibility(boolean isVisible) {
        fontSize.setVisibility((isVisible) ? View.VISIBLE : View.GONE);
        fontColor.setVisibility((isVisible) ? View.VISIBLE : View.GONE);
        fontStyle.setVisibility((isVisible) ? View.VISIBLE : View.GONE);
        divider.setVisibility((isVisible) ? View.VISIBLE : View.GONE);
    }
}
