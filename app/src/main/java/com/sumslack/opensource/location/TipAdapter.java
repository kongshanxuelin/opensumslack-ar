package com.sumslack.opensource.location;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.sumslack.opensource.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/1/001.
 */

public class TipAdapter extends BaseAdapter implements Filterable {
    private ArrayFilter mFilter;
    private List<Tip> mList;
    private Context context;
    private ArrayList<Tip> mUnfilteredData;

    public TipAdapter(List<Tip> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList==null ? 0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if(convertView==null){
            view = View.inflate(context, R.layout.nav_list_item, null);

            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.listview_nav_name);
            holder.address = (TextView) view.findViewById(R.id.listview_nav_addr);

            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        Tip pc = mList.get(position);

        holder.name.setText(pc.getName());
        holder.address.setText(pc.getAddress());

        return view;
    }

    static class ViewHolder{
        public TextView name;
        public TextView address;
    }
    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<Tip>(mList);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<Tip> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<Tip> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<Tip> newValues = new ArrayList<Tip>(count);

                for (int i = 0; i < count; i++) {
                    Tip pc = unfilteredValues.get(i);
                    if (pc != null) {
                        if (pc.getName() != null && pc.getName().startsWith(prefixString)) {

                            newValues.add(pc);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            //noinspection unchecked
            mList = (List<Tip>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
    }
