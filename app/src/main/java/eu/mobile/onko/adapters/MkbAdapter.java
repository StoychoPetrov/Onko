package eu.mobile.onko.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import eu.mobile.onko.R;
import eu.mobile.onko.models.MkbModel;

/**
 * Created by Stoycho Petrov on 5.5.2018 г..
 */

public class MkbAdapter extends ArrayAdapter<MkbModel> {

    private Context             mContext;
    private ArrayList<MkbModel> mMkbArrayList;
    private LayoutInflater      mInflater;
    private OnButtonsClicked    mListener;

    public interface OnButtonsClicked {
        void onDeleteClicked(int position);
    }

    public MkbAdapter(@NonNull Context context, @NonNull ArrayList<MkbModel> objects, OnButtonsClicked listener) {
        super(context, R.layout.item_mkb, objects);

        mContext        = context;
        mMkbArrayList   = objects;
        mInflater       = ((Activity) context).getLayoutInflater();
        mListener       = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_mkb, null);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        MkbModel mkbModel = mMkbArrayList.get(position);

        String mkbName = mkbModel.getmMkbName().substring(0,1).toUpperCase() + mkbModel.getmMkbName().substring(1);

        viewHolder.mMkbNameTxt.setText(mkbName);

        viewHolder.mDeleteImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onDeleteClicked(position);
            }
        });

        return convertView;
    }

    public class ViewHolder {

        TextView        mMkbNameTxt;
        ImageView       mDeleteImgBtn;

        ViewHolder(View view){
            mMkbNameTxt     = view.findViewById(R.id.mkb_name_txt);
            mDeleteImgBtn   = view.findViewById(R.id.delete_icon);
        }
    }
}
