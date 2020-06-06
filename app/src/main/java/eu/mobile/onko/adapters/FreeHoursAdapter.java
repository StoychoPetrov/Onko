package eu.mobile.onko.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import eu.mobile.onko.OnItemClickListener;
import eu.mobile.onko.R;

public class FreeHoursAdapter extends RecyclerView.Adapter<FreeHoursAdapter.ViewHolder> {

    private ArrayList<String>   mItemsArrayList;
    private OnItemClickListener mListener;

    public FreeHoursAdapter(ArrayList<String> mItemsArrayList, OnItemClickListener listener) {
        this.mItemsArrayList = mItemsArrayList;
        this.mListener       = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_free_hour, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mFreeHourTxt.setText(mItemsArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemsArrayList.size();
    }

    public void setmItemsArrayList(ArrayList<String> mItemsArrayList) {
        this.mItemsArrayList = mItemsArrayList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView    mFreeHourTxt;

        ViewHolder(View itemView) {
            super(itemView);

            mFreeHourTxt         = itemView.findViewById(R.id.free_hour_txt);

            mFreeHourTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickItem(getAdapterPosition());
                }
            });
        }
    }
}
