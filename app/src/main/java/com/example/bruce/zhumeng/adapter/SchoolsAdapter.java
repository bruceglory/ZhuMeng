package com.example.bruce.zhumeng.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruce.zhumeng.R;

import com.example.bruce.zhumeng.entities.School;
import com.example.bruce.zhumeng.utils.RecyclerViewClickListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bruce on 2016/1/2.
 */
public class SchoolsAdapter extends RecyclerView.Adapter<SchoolViewHolder> {

    private Context context;
    private List<School> schoolList;
    private RecyclerViewClickListener recyclerViewClickListener;

    public SchoolsAdapter(List<School> schoolList) {
        this.schoolList = schoolList;
    }

    public List<School> getMovieList() {

        return schoolList;
    }

    public void setRecyclerListListener(RecyclerViewClickListener mRecyclerClickListener) {
        this.recyclerViewClickListener = mRecyclerClickListener;
    }

    @Override
    public SchoolViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View rowView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.school_fragment_card_view_schools, viewGroup, false);

        this.context = viewGroup.getContext();

        return new SchoolViewHolder(rowView, recyclerViewClickListener);
    }


    @Override
    public void onBindViewHolder(final SchoolViewHolder holder, final int position) {

        School selectedMovie = schoolList.get(position);

        holder.schoolName.setText(selectedMovie.getSchoolName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            holder.schoolPicture.setTransitionName("cover" + position);

        String posterURL = selectedMovie.getPictureUrl();

        Picasso.with(context)
                .load(posterURL)
                .fit().centerCrop()
                .into(holder.schoolPicture, new Callback() {
                    @Override
                    public void onSuccess() {

                        schoolList.get(position).setSchoolReady(true);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public boolean isSchoolReady(int position) {

        return schoolList.get(position).isSchoolReady();
    }

    @Override
    public int getItemCount() {

        return schoolList.size();
    }

    public void appendMovies(List<School> schoolList) {

        this.schoolList.addAll(schoolList);
        notifyDataSetChanged();
    }
}

class SchoolViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{
    private final RecyclerViewClickListener onClickListener;
    CardView cardView;
    TextView schoolName;
    ImageView schoolPicture;

    public SchoolViewHolder(View itemView, RecyclerViewClickListener onClickListener) {

        super(itemView);

        cardView      = (CardView) itemView.findViewById(R.id.card_view_schools);
        schoolName    = (TextView) itemView.findViewById(R.id.school_name);
        schoolPicture = (ImageView) itemView.findViewById(R.id.school_picture);
        schoolPicture.setDrawingCacheEnabled(true);
        schoolPicture.setOnTouchListener(this);
        this.onClickListener = onClickListener;
    }

    public void setReady(boolean ready) {

        schoolPicture.setTag(ready);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("zhang","onTouch");
        if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_MOVE) {
            Log.d("zhang","adapter onclisk");
            onClickListener.onClick(v, getPosition(), event.getX(), event.getY());
        }
        return true;
    }
}