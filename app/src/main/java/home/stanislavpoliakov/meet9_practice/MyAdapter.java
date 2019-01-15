package home.stanislavpoliakov.meet9_practice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "meet9_logs";
    private List<Entry> entries;
   // private Context context; //Контекст был введен для теста анимации

    public MyAdapter(Context context, List<Entry> entries){
        this.entries = entries;
        //this.context = context;
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

    /**
     * Класс для ViewHolder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView, textView, timeStampLabel;
        private boolean isSingleLine = true; // для эффекта разворачивающегося списка

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

            titleView = itemView.findViewById(R.id.titleTextView);
            textView = itemView.findViewById(R.id.entryTextView);
            timeStampLabel = itemView.findViewById(R.id.timeStampLabel);

            itemView.setOnClickListener(new View.OnClickListener() {

                /**
                 * По нажатию на элемент списка разворачиваем текст записи
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    isSingleLine = !isSingleLine;
                    textView.setSingleLine(isSingleLine);
                }
            });
        }
    }
}
