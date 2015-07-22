package com.beiang.airdog.ui.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.broadlink.beiangair.R;

public class IHomerAdapter extends BaseAdapter {
	private List<DevEntity> list;

	public void setData(List<DevEntity> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}
	
	public void delDevice(int position){
		this.list.remove(position);
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_homer_device_item, null);
			tag.dev_icon = (ImageView) convertView.findViewById(R.id.dev_icon);
			tag.dev_outline = (ImageView) convertView.findViewById(R.id.dev_outline);
			tag.dev_name = (TextView) convertView.findViewById(R.id.dev_name);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		// device
		final DevEntity mDevice = (DevEntity) getItem(position);
		if (mDevice != null) {
			tag.dev_name.setText(mDevice.nickName);
			setItemIcon(mDevice.devType, tag.dev_icon);

			if ("offline".equals(mDevice.status)) {
				tag.dev_outline.setVisibility(View.VISIBLE);
			} else {
				tag.dev_outline.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	/***
	 * 设置图片
	 * 
	 * @param v
	 */
	private void setItemIcon(int type, ImageView v) {
		switch (type) {
		case Device.DT_280B:
			v.setImageResource(R.drawable.ic_dev_airdog);
			break;
		case Device.DT_280E:
			v.setImageResource(R.drawable.ic_dev_280e);
			break;
		case Device.DT_CAR:
			v.setImageResource(R.drawable.ic_dev_airdog);
			break;
		case Device.DT_AURA100:
			v.setImageResource(R.drawable.ic_dev_airdog);
			break;
		case Device.DT_Airdog:
			v.setImageResource(R.drawable.ic_dev_airdog);
			break;
		case Device.DT_JY300:
		case Device.DT_JY300S:
			v.setImageResource(R.drawable.ic_dev_jy300);
			break;
		case Device.DT_JY500:
			v.setImageResource(R.drawable.ic_dev_jy500);
			break;
		case Device.DT_TAir:
			v.setImageResource(R.drawable.ic_dev_tari);
			break;
		case Device.DT_PowerSocket:
			v.setImageResource(R.drawable.ic_dev_powersocket);
			break;
		default:
			v.setImageResource(R.drawable.ic_dev_default);
			break;
		}
	}

	class Tag {
		ImageView dev_icon;
		ImageView dev_outline;
		TextView dev_name;
	}
}
