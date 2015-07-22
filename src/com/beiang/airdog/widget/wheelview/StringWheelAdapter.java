package com.beiang.airdog.widget.wheelview;

import java.util.List;

public class StringWheelAdapter implements WheelAdapter {
	List<String> datas;

	public StringWheelAdapter(List<String> datas) {
		// TODO Auto-generated constructor stub
		this.datas = datas;
	}

	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return datas == null ? 0 : datas.size();
	}

	@Override
	public String getItem(int index) {
		// TODO Auto-generated method stub
		return datas == null ? null : datas.get(index);
	}

	@Override
	public int getMaximumLength() {
		// TODO Auto-generated method stub
		return datas == null ? 0 : datas.size();
	}

}
