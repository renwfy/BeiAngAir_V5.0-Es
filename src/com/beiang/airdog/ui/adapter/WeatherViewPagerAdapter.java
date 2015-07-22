package com.beiang.airdog.ui.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.beiang.airdog.view.WeatherView;

public class WeatherViewPagerAdapter extends PagerAdapter {

	private Context context;
	List<String> citys;

	public WeatherViewPagerAdapter(Context context, List<String> citys) {
		this.context = context;
		this.citys = citys;
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
		return citys.size();
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

	public View getView(int position, View convertView, ViewGroup parent) {
		final String city = citys.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = holder.weatherView = new WeatherView(context);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.weatherView.setCanLoadMore(true);
		holder.weatherView.load(city);
		return convertView;
	}

	private static class ViewHolder {
		WeatherView weatherView;
	}
}
