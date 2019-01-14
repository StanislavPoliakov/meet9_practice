package home.stanislavpoliakov.meet9_practice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "meet9_logs";
    private List<Entry> entries;
    private Context context;

    public MyAdapter(Context context, List<Entry> entries){
        this.entries = entries;
        this.context = context;
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

    private View previousView;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView, textView, timeStampLabel;
        private LinearLayout linearLayout;

        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            //private final int DEFAULT_COLOR =

            @Override
            public void onClick(View v) {

            }
        };

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.add(0,0,0,"DELETE");
                }
            });

            titleView = itemView.findViewById(R.id.titleTextView);
            textView = itemView.findViewById(R.id.entryTextView);
            timeStampLabel = itemView.findViewById(R.id.timeStampLabel);
            linearLayout = itemView.findViewById(R.id.LinearLayout);
            //final boolean isGone = true;

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    /*if (previousView != null) {
                        previousView.setBackgroundColor(0xFFFFFFFF);
                    }
                    //Toast.makeText(this, "Click", Toast.LENGTH_SHORT);
                    Log.d(TAG, "Click");
                    v.setBackgroundColor(0xFF888888);
                    previousView = v;*/

                    //Log.d(TAG, "onClick: X / Y = " + v.getX() + " / " + v.getY());
                    linearLayout.setVisibility(
                            (linearLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);

                    ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    textView.setLayoutParams(layoutParams);

                    Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slidedown);
                    linearLayout.startAnimation(slideDown);
                }

            });


        }


    }
}
