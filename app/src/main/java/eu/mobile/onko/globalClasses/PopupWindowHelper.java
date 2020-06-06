package eu.mobile.onko.globalClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import eu.mobile.onko.R;
import eu.mobile.onko.adapters.PopUpWindowAdapter;

public class PopupWindowHelper implements AdapterView.OnItemClickListener {

    private Context         mContext;
    private List<String>    mValues;
    private PopupWindow     popupWindow;

    private OnItemSelected  mListener;

    public interface OnItemSelected {
        void onItemChosen(String item, int position);
    }

    public PopupWindowHelper(Context context, ArrayList<String> values) {
        mContext    = context;
        mValues     = values;

        View popupContentView     = LayoutInflater.from(context).inflate(R.layout.popup_window, null);
        popupWindow = new PopupWindow(context);
        popupWindow.setContentView(popupContentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(null);

        setAdapter((ListView) popupContentView.findViewById(R.id.popupListView));
    }

    private void setAdapter(ListView listView){
        PopUpWindowAdapter adapter = new PopUpWindowAdapter(mContext, mValues);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }

    public void showPopup(View clickedView){
        if(!popupWindow.isShowing())
            popupWindow.showAsDropDown(clickedView);
    }

    public void setChosenListener(OnItemSelected listener) {
        mListener   = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        popupWindow.dismiss();

        if(mListener != null)
            mListener.onItemChosen(mValues.get(position), position);
    }
}