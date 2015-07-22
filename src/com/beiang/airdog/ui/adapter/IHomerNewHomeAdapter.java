package com.beiang.airdog.ui.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiang.airdog.net.business.ihomer.GetHomerPair.RspGetHomer;
import com.broadlink.beiangair.R;

/***
 * 
 * 创建新家庭
 * 
 * @author lsd
 * 
 */
public class IHomerNewHomeAdapter extends BaseAdapter {
	List<RspGetHomer.Data> list;

	public void setData(List<RspGetHomer.Data> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}
	
	public void remove(int position) {
		list.remove(position);
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ihomer_newhome_item, null);
			tag.home_icon = (ImageView) convertView.findViewById(R.id.home_icon);
			tag.home_name = (TextView) convertView.findViewById(R.id.home_name);
			tag.home_info = (TextView) convertView.findViewById(R.id.home_info);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		RspGetHomer.Data data = (RspGetHomer.Data) getItem(position);
		// device
		if (data != null) {
			tag.home_name.setText(data.name);
		}
		return convertView;
	}

	class Tag {
		ImageView home_icon;
		TextView home_name;
		TextView home_info;
	}
}
