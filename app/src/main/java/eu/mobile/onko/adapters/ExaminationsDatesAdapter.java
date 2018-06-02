package eu.mobile.onko.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import eu.mobile.onko.R;
import eu.mobile.onko.models.ExaminationDateModel;

/**
 * Created by Stoycho Petrov on 22.5.2018 г..
 */

public class ExaminationsDatesAdapter extends RecyclerView.Adapter<ExaminationsDatesAdapter.ViewHolder>{

    private Context                             mContext;
    private ArrayList<ExaminationDateModel>     mItemsArrayList;
    private OnItemClick                         mListener;

    public interface OnItemClick {
        void onItemClickListener(int position);
        void onEditImgClicked(int position);
    }

    public ExaminationsDatesAdapter(Context context, ArrayList<ExaminationDateModel> itemsArrayList) {
        mContext            = context;
        mItemsArrayList     = itemsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_examination_date, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        try {

            if(position == 0){
                holder.mSectionTxt.setVisibility(View.VISIBLE);
                holder.mSectionTxt.setText(R.string.past);
            }
            else if(mItemsArrayList.get(position).getmType() != mItemsArrayList.get(position - 1).getmType()){
                holder.mSectionTxt.setVisibility(View.VISIBLE);
                holder.mSectionTxt.setText(R.string.predicted_examinations);
            }

            if(position != mItemsArrayList.size() - 1
                    && mItemsArrayList.get(position).getmType() != mItemsArrayList.get(position + 1).getmType())
                holder.itemView.findViewById(R.id.divider).setVisibility(View.GONE);

            if(mItemsArrayList.get(position).getmType() == ExaminationDateModel.PAST_EXAMINATION) {
                holder.mDateTxt.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
                holder.mEditImg.setVisibility(View.VISIBLE);

                holder.mEditImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mListener != null)
                            mListener.onEditImgClicked(position);
                    }
                });
            }
            else {
                holder.mDateTxt.setTextColor(ContextCompat.getColor(mContext, android.R.color.darker_gray));
                holder.mEditImg.setVisibility(View.GONE);
            }

            String date = mItemsArrayList.get(position).getmDate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = format.parse(date);

            format = new SimpleDateFormat("dd MMMM yyyy");

            holder.mDateTxt.setText(format.format(newDate));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null)
                        mListener.onEditImgClicked(position);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null)
                        mListener.onItemClickListener(position);
                }
            });

        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mItemsArrayList.size();
    }

    public OnItemClick getmListener() {
        return mListener;
    }

    public void setmListener(OnItemClick mListener) {
        this.mListener = mListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView    mSectionTxt;
        private TextView    mDateTxt;
        private ImageView   mEditImg;

        ViewHolder(View itemView) {
            super(itemView);

            mSectionTxt         = itemView.findViewById(R.id.section_title_txt);
            mDateTxt            = itemView.findViewById(R.id.date_txt);
            mEditImg            = itemView.findViewById(R.id.edit_img);
        }
    }
}
