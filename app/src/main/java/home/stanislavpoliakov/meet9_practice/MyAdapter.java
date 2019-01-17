package home.stanislavpoliakov.meet9_practice;

import android.app.FragmentManager;
import android.content.Context;
import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "meet9_logs";
    private List<Entry> entries;
    private Context context; //Контекст был введен для теста анимации
    private PrefStorage prefStorage;

    public MyAdapter(Context context, List<Entry> entries){
        this.entries = entries;
        this.context = context;
        this.prefStorage = new PrefStorage(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_view, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * Заполняем поля данными
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Entry entry = entries.get(position);

        String title = entry.getTitle();
        holder.titleView.setText(title);

        String text = entry.getText();
        holder.textView.setText(text);

        holder.timeStampLabel.setText(entry.getTimeStamp());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    /**
     * Метод для сравнения данных в DiffUtils
     * @param oldData старые данные
     * @param newData новые данные
     */
    public void onNewData(List<Entry> oldData, List<Entry> newData) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCall(oldData, newData));
        result.dispatchUpdatesTo(this);
        entries.clear();
        entries.addAll(newData);

        notifyDataSetChanged(); // Чтобы RecyclerView не мигал :) "Чтобы цепь не слетала" (с)
    }

    private View previousView;
    private boolean isPreviousViewCollapsed;

    /**
     * Класс для ViewHolder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView, textView, timeStampLabel;
        public ImageButton editButton, deleteButton;
        private ConstraintLayout constraintLayout;
        //private boolean isSingleLine = true; // для эффекта разворачивающегося списка


        public MyViewHolder(View itemView) {
            super(itemView);

            /**
             * Добавляем контекстное меню для удаления элемента. Реализации пока нет
             */
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    //Log.d(TAG, "onCreateContextMenu: " + getAdapterPosition());
                    menu.add(0, getAdapterPosition(),0,"DELETE");
                   // Log.d(TAG, "onCreateContextMenu: " + );
                }
            });


            constraintLayout = itemView.findViewById(R.id.entryLayout);
            titleView = itemView.findViewById(R.id.titleTextView);
            setLoadedPrefs(titleView, prefStorage.getRVTitlePrefs()); // устанавливаем pref'-ы
            textView = itemView.findViewById(R.id.entryTextView);
            setLoadedPrefs(textView, prefStorage.getRVTitlePrefs()); // еще
            timeStampLabel = itemView.findViewById(R.id.timeStampLabel);
            setLoadedPrefs(timeStampLabel, prefStorage.getRVTimestampPrefs()); // и еще
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            /**
             * Запуск операции удаления через каст полученного контекста и интерфейс, по нажатию
             * кнопки
             */
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        CRUDOperationsListener mActivity = (CRUDOperationsListener) MyAdapter.this.context;
                        mActivity.deleteEntry(getAdapterPosition());
                    } catch (ClassCastException ex) {

                    }
                }
            });

            /**
             * Запуск операции редактирования, через каст полученного контекста и интерфейс, по
             * нажатию кнопки
             */
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        CRUDOperationsListener mActivity = (CRUDOperationsListener) MyAdapter.this.context;
                        mActivity.editEntry(getAdapterPosition());
                    } catch (ClassCastException ex) {

                    }

                }
            });

            //До анимации руки дошли, но нужно тщательно курить мануал
            //final Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slidedown);
            //final Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slideup);

            itemView.setOnClickListener(new View.OnClickListener() {
                private boolean isSingleLine = true; // для эффекта разворачивающегося списка

                /**
                 * По нажатию на элемент списка разворачиваем текст записи, а предыдущую View (на
                 * которую мы нажимали) сворачиваем
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    if (previousView != null && !previousView.equals(v)) {
                        TextView pTextView = previousView.findViewById(R.id.entryTextView);
                        ImageButton pEditButton = previousView.findViewById(R.id.editButton);
                        ImageButton pDeleteButton = previousView.findViewById(R.id.deleteButton);

                        boolean needToHide = (pEditButton.getVisibility() != View.GONE);
                        if (needToHide) {
                            pTextView.setSingleLine(true);
                            pEditButton.setVisibility(View.GONE);
                            pDeleteButton.setVisibility(View.GONE);
                        }
                    }
                    boolean needToHide = (editButton.getVisibility() != View.GONE);
                    textView.setSingleLine(needToHide);
                    editButton.setVisibility((needToHide) ? View.GONE : View.VISIBLE);
                    deleteButton.setVisibility((needToHide) ? View.GONE : View.VISIBLE);
                    previousView = v;
                }
            });
        }

        private void setLoadedPrefs(TextView view, PrefData preferences) {
            view.setTextSize(preferences.size);
            view.setTypeface(view.getTypeface(), preferences.style);
            view.setTextColor(preferences.color);
        }
    }
}
