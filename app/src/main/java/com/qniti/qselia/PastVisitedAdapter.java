package com.qniti.qselia;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PastVisitedAdapter extends RecyclerView.Adapter<PastVisitedAdapter.PastVisitedViewHolder> {


    private Context mCtx;
    private List<Log> logList;
    private OnItemClicked onClick;


    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public PastVisitedAdapter(Context mCtx, List<Log> logList) {
        this.mCtx = mCtx;
        this.logList = logList;
    }

    @Override
    public PastVisitedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.past_visited_list, null);
        return new PastVisitedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PastVisitedViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Log log = logList.get(position);


         //getName
        holder.userName.setText(capitalize(log.getUserName()));
        holder.enterDate.setText(log.getLogDate());
        holder.enterTime.setText(log.getLogTime());

        if (!("null".equalsIgnoreCase(log.getRating()))){

            holder.noRating.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating(Float.valueOf(log.getRating()));

        }else {

            holder.noRating.setVisibility(View.VISIBLE);
            holder.ratingBar.setVisibility(View.GONE);


        }

        holder.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    class PastVisitedViewHolder extends RecyclerView.ViewHolder {

        TextView enterDate, userName,enterTime, noRating;
        // ImageView imageView;
        RelativeLayout test;
        ScaleRatingBar ratingBar;

        public PastVisitedViewHolder(View itemView) {
            super(itemView);

            test=itemView.findViewById(R.id.testing);
            enterDate = itemView.findViewById(R.id.enterDate);
            userName = itemView.findViewById(R.id.placeName);
            enterTime = itemView.findViewById(R.id.enterTime);
            ratingBar = itemView.findViewById(R.id.ratingBarList);
            noRating = itemView.findViewById(R.id.noratingTv);
        }
    }
    //Auto Capitalize First Character of Each Word
    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z-éá])([a-z-éá]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}
