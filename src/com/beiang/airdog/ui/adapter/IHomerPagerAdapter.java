package com.beiang.airdog.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiang.airdog.net.business.entity.CurrentHomer;
import com.beiang.airdog.net.business.ihomer.GetHomerPair.RspGetHomer;
import com.beiang.airdog.net.business.ihomer.GetHomerPair.RspGetHomer.Data;
import com.beiang.airdog.ui.activity.IHomerCenterActivity;
import com.broadlink.beiangair.R;

public class IHomerPagerAdapter extends PagerAdapter {
	Context context;
	List<RspGetHomer.Data> list;

	public IHomerPagerAdapter(Context context) {
		this.context = context;
	}

	public void setData(List<RspGetHomer.Data> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public final Object instantiateItem(ViewGroup container, int position) {
		View view = null;
		view = getView(position, view, container);
		container.addView(view);
		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	public Object getItem(int position) {
		return list == null ? null : list.get(position);
	}

	@Override
	public final void destroyItem(ViewGroup container, int position, Object object) {
		View view = (View) object;
		container.removeView(view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final RspGetHomer.Data data = (RspGetHomer.Data) getItem(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.layout_homer_page_item, null);
			holder.iv_homerimg = (ImageView) convertView.findViewById(R.id.iv_homerimg);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.iv_homerimg.setImageResource(R.drawable.ic_homer_logo_default_1);
		holder.tv_name.setText(data.name);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RspGetHomer.Data data = (Data) getItem(position);
				CurrentHomer.instance().curHomer = data;
				context.startActivity(new Intent(context,IHomerCenterActivity.class));
			}
		});
		return convertView;
	}

	private static class ViewHolder {
		ImageView iv_homerimg;
		TextView tv_name;
	}
}
