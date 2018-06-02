package eu.mobile.onko.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import eu.mobile.onko.R;
import eu.mobile.onko.models.DoctorRowModel;

/**
 * Created by Stoycho Petrov on 18.5.2018 г..
 */

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {

    private Context                     mContext;
    private ArrayList<DoctorRowModel>   mDoctorsArrayList   = new ArrayList<>();

    public DoctorsAdapter(Context context, ArrayList<DoctorRowModel> doctorsArrayList) {
        mContext           = context;
        mDoctorsArrayList  = doctorsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if(viewType == DoctorRowModel.SECTION_INDEX)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_section, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(holder.getItemViewType() == DoctorRowModel.SECTION_INDEX)
            holder.mSectionTxt.setText(mDoctorsArrayList.get(position).getmLabel());
        else
            holder.mDoctorTitleTxt.setText(mDoctorsArrayList.get(position).getmLabel());
    }

    @Override
    public int getItemCount() {
        return mDoctorsArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDoctorsArrayList.get(position).getmRowType();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView    mSectionTxt;
        private TextView    mDoctorTitleTxt;

        ViewHolder(View itemView) {
            super(itemView);

            mSectionTxt         = itemView.findViewById(R.id.mkb_group_name_txt);
            mDoctorTitleTxt     = itemView.findViewById(R.id.doctor_title);
        }
    }
}
