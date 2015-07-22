package com.beiang.airdog.widget;

import android.content.Context;
import android.widget.ListView;

public class CannotRollListView extends ListView {

	public CannotRollListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CannotRollListView(android.content.Context context,
			android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
