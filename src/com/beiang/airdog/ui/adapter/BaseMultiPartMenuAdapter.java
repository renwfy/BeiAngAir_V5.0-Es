package com.beiang.airdog.ui.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiang.airdog.ui.model.MenuEntity;
import com.broadlink.beiangair.R;

public class BaseMultiPartMenuAdapter extends BaseAdapter {
	List<MenuEntity> list;

	public void setData(List<MenuEntity> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Tag tag;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_item, null);
			tag.item_icon = (ImageView) convertView.findViewById(R.id.item_icon);
			tag.item_name = (TextView) convertView.findViewById(R.id.item_name);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		final MenuEntity ety = (MenuEntity) getItem(position);
		if (ety != null) {
			int icon = ety.getMenu_icon();
			if(0 != icon){
				tag.item_icon.setImageResource(icon);
			}
			tag.item_name.setText(ety.getMenu_name());
		}
		return convertView;

	}

	class Tag {
		ImageView item_icon;
		TextView item_name;
	}
}
