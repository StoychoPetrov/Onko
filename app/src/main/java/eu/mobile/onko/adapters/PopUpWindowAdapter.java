package eu.mobile.onko.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import eu.mobile.onko.R;

public class PopUpWindowAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> items;

    public PopUpWindowAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, R.layout.item_popup, objects);

        mContext = context;
        items    = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater    = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view                       = inflater.inflate(R.layout.item_popup, null);

            viewHolder                 = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.label.setText(items.get(position));

        return view;
    }

    static class ViewHolder {

        TextView label;

        ViewHolder(View view) {
            label = view.findViewById(R.id.label_txt);
        }
    }
}