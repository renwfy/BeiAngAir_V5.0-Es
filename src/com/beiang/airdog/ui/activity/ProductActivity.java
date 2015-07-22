package com.beiang.airdog.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beiang.airdog.ui.adapter.ViewPagerAdapter;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.broadlink.beiangair.R;

public class ProductActivity extends BaseMultiPartActivity {
	private int viewSize = 10;
	private int[] imgs = new int[] { R.drawable.pd01, R.drawable.pd02, R.drawable.pd03, R.drawable.pd04, R.drawable.pd05, R.drawable.pd06,
			R.drawable.pd07, R.drawable.pd08, R.drawable.pd09, R.drawable.pd10 };
	ViewPager viewPager;
	ViewPagerAdapter adapter;
	LinearLayout viewPoints;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setMenuEnable(false);
		setContentView(R.layout.activity_product);

		init();
	}

	private void init() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		adapter = new ViewPagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(onPageChangeListener);

		viewPoints = (LinearLayout) findViewById(R.id.points);
		adapter.setViews(initViewPage());
		setGuidePoint();
	}

	/** 取得viewPager 的views */
	private List<View> initViewPage() {
		List<View> views = new ArrayList<View>();
		for (int i = 0; i < viewSize; i++) {
			View view = LayoutInflater.from(mActivity).inflate(R.layout.layout_pd_image, null);
			ImageView image = (ImageView) view.findViewById(R.id.guide_image);
			LinearLayout.LayoutParams tempLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			image.setLayoutParams(tempLayoutParams);
			image.setImageResource(imgs[i]);
			views.add(view);
		}
		return views;
	}

	/** 设置引导圆点 */
	private void setGuidePoint() {
		for (int i = 0; i < viewSize; i++) {
			ImageView imageView = new ImageView(mActivity);
			LinearLayout.LayoutParams tempLayoutParams = new LinearLayout.LayoutParams(10, 10);
			tempLayoutParams.setMargins(5, 5, 5, 5);
			imageView.setLayoutParams(tempLayoutParams);
			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.radio_sel);
			} else {
				imageView.setBackgroundResource(R.drawable.radio);
			}
			viewPoints.addView(imageView);
		}
	}

	/** View */
	OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			for (int w = 0; w < viewSize; w++) {
				ImageView imageView = (ImageView) viewPoints.getChildAt(w);
				imageView.setBackgroundResource(R.drawable.radio);
			}
			ImageView imageView = (ImageView) viewPoints.getChildAt(position);
			imageView.setBackgroundResource(R.drawable.radio_sel);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
		}
	};

}
