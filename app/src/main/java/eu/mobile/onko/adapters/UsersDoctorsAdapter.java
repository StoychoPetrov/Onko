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
import eu.mobile.onko.models.UserModel;

public class UsersDoctorsAdapter extends RecyclerView.Adapter<UsersDoctorsAdapter.UsersDoctorsViewHolder> {

    private ArrayList<UserModel>  mUsersDoctors;

    private OnItemClickListener   mListener;

    public UsersDoctorsAdapter(ArrayList<UserModel> userModels, OnItemClickListener listener) {
        mUsersDoctors   = userModels;
        mListener       = listener;
    }

    @NonNull
    @Override
    public UsersDoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersDoctorsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_doctor, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull UsersDoctorsViewHolder holder, int position) {
        UserModel userModel  = mUsersDoctors.get(position);

        holder.mNameTxt.setText(userModel.getmFirstName() + " " + userModel.getmLastName());
        holder.mEmailTxt.setText(userModel.getmEmail());
        holder.mPhoneTxt.setText(userModel.getmPhone());
    }

    @Override
    public int getItemCount() {
        return mUsersDoctors.size();
    }

    class UsersDoctorsViewHolder extends RecyclerView.ViewHolder {

        private TextView    mNameTxt;
        private TextView    mEmailTxt;
        private TextView    mPhoneTxt;

        UsersDoctorsViewHolder(View itemView) {
            super(itemView);

            mNameTxt         = itemView.findViewById(R.id.nameTxt);
            mEmailTxt        = itemView.findViewById(R.id.emailTxt);
            mPhoneTxt        = itemView.findViewById(R.id.phoneTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickItem(getAdapterPosition());
                }
            });
        }
    }
}