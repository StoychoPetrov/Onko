package eu.mobile.onko.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import eu.mobile.onko.R;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.DrawerMenuItemModel;
import eu.mobile.onko.models.ProfileModel;

/**
 * Created by stoycho.petrov on 04/09/2017.
 */

public class NavDrawerAdapter extends BaseAdapter{

    private static final int TYPE_HEADER        = 0;
    private static final int TYPE_ITEM          = 1;

    private Context                             mContext;
    private ArrayList<DrawerMenuItemModel>      mNavMenuItems = new ArrayList<>();
    private ProfileModel                        mProfileModel;
    private LayoutInflater                      mLayoutInflater;


    public NavDrawerAdapter(Context context, ArrayList<DrawerMenuItemModel> navMenuItems, ProfileModel profileModel) {
        mContext                    = context;
        mNavMenuItems               = navMenuItems;
        mProfileModel               = profileModel;
        mLayoutInflater             = ((Activity)mContext).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mNavMenuItems.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null){
            ViewHolder viewHolder;
            if(position != 0) {
                view = mLayoutInflater.inflate(R.layout.drawer_item, null);
                viewHolder = new ViewHolder(mContext,view,TYPE_ITEM);
                view.setTag(viewHolder);
            }
            else {
                view = mLayoutInflater.inflate(R.layout.drawer_header, null);
                viewHolder = new ViewHolder(mContext,view,TYPE_HEADER);
                view.setTag(viewHolder);
            }

            viewHolder = (ViewHolder) view.getTag();

            if(position == TYPE_HEADER){
                viewHolder.mNameTxt.setText(mProfileModel.getmName());
                viewHolder.mEmailTxt.setText(mProfileModel.getmEmail());
                viewHolder.mProfileImageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_account_circle));

                Utils.setTypeFace(mContext,viewHolder.mNameTxt,Utils.ROBOTO_MEDIUM);
                Utils.setTypeFace(mContext,viewHolder.mEmailTxt,Utils.ROBOTO_REGULAR);
            }
            else{
                DrawerMenuItemModel item = mNavMenuItems.get(position - 1);
                viewHolder.mTitleCellTxt.setText(item.getmTitle());
                viewHolder.mImageCell.setImageDrawable(item.getmImage());

                Utils.setTypeFace(mContext,viewHolder.mTitleCellTxt,Utils.ROBOTO_MEDIUM);
            }
        }

        return view;
    }

    private class ViewHolder {

        ImageView   mProfileImageView;
        ImageView   mImageCell;
        TextView    mNameTxt;
        TextView    mEmailTxt;
        TextView    mTitleCellTxt;

        ViewHolder(Context context, View itemView, int viewType){

            if(viewType == TYPE_HEADER){
                mProfileImageView   = (CircleImageView) itemView.findViewById(R.id.profile_image);
                mNameTxt            = (TextView)        itemView.findViewById(R.id.name_txt);
                mEmailTxt           = (TextView)        itemView.findViewById(R.id.email_txt);
            }
            else{
                mImageCell          = (ImageView)       itemView.findViewById(R.id.image_cell);
                mTitleCellTxt       = (TextView)        itemView.findViewById(R.id.title_cell);
            }

        }
    }
}
