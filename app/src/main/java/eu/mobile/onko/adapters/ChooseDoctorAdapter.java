package eu.mobile.onko.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import eu.mobile.onko.R;
import eu.mobile.onko.models.DoctorModel;

public class ChooseDoctorAdapter extends RecyclerView.Adapter<ChooseDoctorAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<DoctorModel> mDoctorsArrayList;
    private OnItemClick                 mListener;

    public interface OnItemClick {
        void onItemClickListener(int position);
    }

    public ChooseDoctorAdapter(Context context, ArrayList<DoctorModel> doctorsArrayList) {
        mContext           = context;
        mDoctorsArrayList  = doctorsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mDoctorTitleTxt.setText(mDoctorsArrayList.get(position).getmDoctorName());
    }

    @Override
    public int getItemCount() {
        return mDoctorsArrayList.size();
    }

    public void setListener(OnItemClick mListener) {
        this.mListener = mListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView    mDoctorTitleTxt;

        ViewHolder(View itemView) {
            super(itemView);

            mDoctorTitleTxt     = itemView.findViewById(R.id.doctor_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null)
                        mListener.onItemClickListener(getAdapterPosition());
                }
            });
        }
    }
}
