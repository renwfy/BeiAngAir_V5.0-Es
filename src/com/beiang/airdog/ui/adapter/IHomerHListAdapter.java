package com.beiang.airdog.ui.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beiang.airdog.net.business.ihomer.GetHomerPair.RspGetHomer;
import com.broadlink.beiangair.R;

/***
 * 家庭列表适配
 * 
 * @author lsd
 * 
 */
public class IHomerHListAdapter extends BaseAdapter {
	List<RspGetHomer.Data> list;

	public void setData(List<RspGetHomer.Data> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return list == null ? 0 : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Tag tag;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_homer_page_item, null);
			tag.bt = (TextView) convertView.findViewById(R.id.bt);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		RspGetHomer.Data data = (RspGetHomer.Data) getItem(position);
		tag.bt.setText(data.name+"/"+data.home_id);
		return convertView;
	}

	class Tag {
		TextView bt;
	}
}
