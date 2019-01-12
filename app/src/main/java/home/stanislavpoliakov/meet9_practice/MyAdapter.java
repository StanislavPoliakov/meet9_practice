package home.stanislavpoliakov.meet9_practice;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "meet9_logs";
    private List<Entry> entries;

    public MyAdapter(List<Entry> entries){
        this.entries = entries;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Entry entry = entries.get(position);

        String title = entry.getTitle();
        holder.titleView.setText(title);

        String text = entry.getText();
        //holder.textView.setText("");
        holder.textView.setText(text);
        /*//StringBuilder stringBuilder = new StringBuilder();
        for (String line : textLines) {
            //stringBuilder.append(line + "\n");
            holder.textView.append(line);
        }*/

        holder.timeStampLabel.setText(entry.getTimeStamp());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView, textView, timeStampLabel;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.titleTextView);
            textView = itemView.findViewById(R.id.entryTextView);
            timeStampLabel = itemView.findViewById(R.id.timeStampLabel);
        }
    }
}
