package com.beiang.airdog.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.broadlink.beiangair.R;

public class DeviceAdapter extends BaseAdapter {
	private OnClickListener listener;
	private OnLongClickListener longClick;
	private List<DevEntity> mDeviceList;
	private List<Boolean> delStatus;

	public void setOnClickListener(OnClickListener listener){
		this.listener = listener;
	}

	public void setOnLongClickListener(OnLongClickListener longClick){
		this.longClick = longClick;
	}
	public void removeDevice(int position) {
		if (mDeviceList != null) {
			mDeviceList.remove(position);
		}
		this.notifyDataSetChanged();
	}

	public void setData(List<DevEntity> list) {
		delStatus = new ArrayList<Boolean>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				delStatus.add(i, false);
			}
		}
		mDeviceList = list;
		this.notifyDataSetChanged();
	}
	
	public void delCurDev(int position){
		if (mDeviceList == null || mDeviceList.size() == 0) {
			return;
		}
		delStatus.add(position, true);
		this.notifyDataSetChanged();
	}
	
	public void cleanDelStatus(){
		if (mDeviceList == null || mDeviceList.size() == 0) {
			return;
		}
		for (int i = 0; i < mDeviceList.size(); i++) {
			delStatus.set(i, false);
		}
		this.notifyDataSetChanged();
	}
	
	public void delAllDev(){
		if (mDeviceList == null || mDeviceList.size() == 0) {
			return;
		}
		for (int i = 0; i < mDeviceList.size(); i++) {
			delStatus.set(i, true);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDeviceList == null ? 0 : mDeviceList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDeviceList == null ? null : mDeviceList.get(position);
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_device_grid_item, null);
			tag.dev_icon = (ImageView) convertView.findViewById(R.id.dev_icon);
			tag.dev_outline = (ImageView) convertView.findViewById(R.id.dev_outline);
			tag.dev_name = (TextView) convertView.findViewById(R.id.dev_name);
			tag.rl_dev_pic_item = (RelativeLayout) convertView.findViewById(R.id.rl_dev_pic_item);
			tag.ll_shade = (RelativeLayout) convertView.findViewById(R.id.ll_shade);
			tag.dev_name1 = (TextView) convertView.findViewById(R.id.dev_name1);
			tag.dev_del = (ImageView) convertView.findViewById(R.id.dev_del);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		// device
		final DevEntity mDevice = (DevEntity) getItem(position);
		if (mDevice != null) {
			tag.dev_name.setText(mDevice.nickName);
			tag.dev_name1.setText(mDevice.nickName);
			setItemIcon(mDevice.devType, tag.dev_icon);

			if ("offline".equals(mDevice.status)) {
				tag.dev_outline.setVisibility(View.VISIBLE);
			} else {
				tag.dev_outline.setVisibility(View.GONE);
			}

			if (delStatus.get(position)) {
				tag.dev_name.setVisibility(View.INVISIBLE);
				tag.ll_shade.setVisibility(View.VISIBLE);
			} else {
				tag.dev_name.setVisibility(View.VISIBLE);
				tag.ll_shade.setVisibility(View.GONE);
			}
			
			//处理 短按 长按事件
			tag.dev_icon.setTag(position);
			tag.dev_name.setTag(position);
			tag.dev_outline.setTag(position);
			tag.dev_icon.setOnClickListener(listener);
			tag.dev_outline.setOnClickListener(listener);
			tag.dev_name.setOnClickListener(listener);
			tag.dev_icon.setOnLongClickListener(longClick);
			tag.dev_outline.setOnLongClickListener(longClick);
			
			//处理清除删除标志
			tag.ll_shade.setOnClickListener(listener);
			
			//处理删除设备
			tag.dev_del.setTag(position);
			tag.dev_del.setOnClickListener(listener);
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
		default:
			v.setImageResource(R.drawable.ic_dev_default);
			break;
		}
	}

	class Tag {
		ImageView dev_icon;
		ImageView dev_outline;
		TextView dev_name;
		RelativeLayout rl_dev_pic_item;
		RelativeLayout ll_shade;
		TextView dev_name1;
		ImageView dev_del;
	}
}
