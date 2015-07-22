package com.beiang.airdog.view;

import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.widget.CustomDialog;
import com.beiang.airdog.widget.CustomDialog.CGravity;
import com.beiang.airdog.widget.wheelview.NumericWheelAdapter;
import com.beiang.airdog.widget.wheelview.OnWheelChangedListener;
import com.beiang.airdog.widget.wheelview.WheelView;
import com.broadlink.beiangair.R;

public class DatePickDialog implements OnClickListener {
	CustomDialog dialog;
	DatePickDialogCallBack callBack;

	int year;
	int month;
	int day;

	public void show(Context context, int year, int month, int day, final DatePickDialogCallBack callBack) {
		this.callBack = callBack;
		final View view = LayoutInflater.from(context).inflate(R.layout.layout_date_pick_dialog, null);
		dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER);

		initView(year, month, day, view);

		dialog.show();
	}

	private void initView(int year, int month, int day, View view) {
		LogUtil.i("year = " + year);
		LogUtil.i("month = " + month);
		LogUtil.i("day = " + day);
		WheelView date_year = (WheelView) view.findViewById(R.id.date_year);
		WheelView date_month = (WheelView) view.findViewById(R.id.date_month);
		WheelView date_day = (WheelView) view.findViewById(R.id.date_day);

		TextView no = (TextView) view.findViewById(R.id.no);
		TextView yes = (TextView) view.findViewById(R.id.yes);

		date_year.addChangingListener(wheelChangedListener);
		date_month.addChangingListener(wheelChangedListener);
		date_day.addChangingListener(wheelChangedListener);

		no.setOnClickListener(this);
		yes.setOnClickListener(this);

		date_year.setCyclic(true);
		date_month.setCyclic(true);
		date_day.setCyclic(true);

		date_year.setLabel("年");
		date_month.setLabel("月");
		date_day.setLabel("日");

		Calendar curCalendar = Calendar.getInstance();
		int curY = curCalendar.get(Calendar.YEAR) - 1;

		date_year.setAdapter(new NumericWheelAdapter(curY, curY + 50, null));
		date_month.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
		date_day.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));

		int i = 0;
		for (int y = curY; y < curY + 50; y++) {
			if (year == y) {
				this.year = year;
				date_year.setCurrentItem(i);
				break;
			}
			i++;
		}

		for (int m = 1; m <= 12; m++) {
			if (month == m) {
				this.month = month;
				date_month.setCurrentItem(m - 1);
				break;
			}
		}

		for (int d = 1; d <= 30; d++) {
			if (day == d) {
				this.day = day;
				date_day.setCurrentItem(d - 1);
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.yes:
			dialog.dismiss();
			Integer[] select = new Integer[3];
			select[0] = year;
			select[1] = month;
			select[2] = day;

			if (callBack != null)
				callBack.onConfirm(select);
			break;
		case R.id.no:
			dialog.dismiss();
			if (callBack != null)
				callBack.onCancel();
			break;
		default:
			break;
		}

	}

	OnWheelChangedListener wheelChangedListener = new OnWheelChangedListener() {
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			// TODO Auto-generated method stub
			switch (wheel.getId()) {
			case R.id.date_year:
				year = Integer.valueOf(wheel.getAdapter().getItem(newValue));
				break;
			case R.id.date_month:
				month = Integer.valueOf(wheel.getAdapter().getItem(newValue));
				break;
			case R.id.date_day:
				day = Integer.valueOf(wheel.getAdapter().getItem(newValue));
				break;
			default:
				break;
			}
		}
	};

	public interface DatePickDialogCallBack {
		void onConfirm(Integer[] select);

		void onCancel();
	}
}
