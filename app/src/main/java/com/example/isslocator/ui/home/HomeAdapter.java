package com.example.isslocator.ui.home;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isslocator.R;
import com.example.isslocator.model.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ResultViewHolder> {

    private final List<Result> results = new ArrayList<>();

    public void setData(@NonNull List<Result> data) {
        results.clear();
        results.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder resultViewHolder, int position) {
        resultViewHolder.bind(results.get(position));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDuration)
        TextView tvDuration;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvLatLong)
        TextView tvLatLong;


        ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Result result) {
            Resources resources = itemView.getContext().getResources();
            tvDuration.setText(resources.getString(R.string.duration_message, result.getDuration()));
            tvTime.setText(resources.getString(R.string.time_message, result.getTime()));
            tvLatLong.setText(resources.getString(R.string.location_message, result.getLatLong()));
        }
    }
}
