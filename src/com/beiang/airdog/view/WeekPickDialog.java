package com.beiang.airdog.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.beiang.airdog.widget.CustomDialog;
import com.beiang.airdog.widget.CustomDialog.CGravity;
import com.broadlink.beiangair.R;

public class WeekPickDialog implements OnClickListener {
	CustomDialog dialog;
	RepeatDialogCallBack callBack;

	int weekSum;

	public void show(Context context, int weekSum, final RepeatDialogCallBack callBack) {
		this.callBack = callBack;
		final View view = LayoutInflater.from(context).inflate(R.layout.layout_week_pick_dialog, null);
		dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER);

		initView(weekSum, view);

		dialog.show();
	}

	private void initView(int weekSum, View view) {
		TextView no = (TextView) view.findViewById(R.id.no);
		no.setOnClickListener(this);
		TextView yes = (TextView) view.findViewById(R.id.yes);
		yes.setOnClickListener(this);

		TextView week_1 = (TextView) view.findViewById(R.id.week_1);
		week_1.setTag(1);
		week_1.setOnClickListener(this);
		TextView week_2 = (TextView) view.findViewById(R.id.week_2);
		week_2.setTag(2);
		week_2.setOnClickListener(this);
		TextView week_3 = (TextView) view.findViewById(R.id.week_3);
		week_3.setTag(4);
		week_3.setOnClickListener(this);
		TextView week_4 = (TextView) view.findViewById(R.id.week_4);
		week_4.setTag(8);
		week_4.setOnClickListener(this);
		TextView week_5 = (TextView) view.findViewById(R.id.week_5);
		week_5.setTag(16);
		week_5.setOnClickListener(this);
		TextView week_6 = (TextView) view.findViewById(R.id.week_6);
		week_6.setTag(32);
		week_6.setOnClickListener(this);
		TextView week_7 = (TextView) view.findViewById(R.id.week_7);
		week_7.setTag(64);
		week_7.setOnClickListener(this);

		if (weekSum >= 64) {
			weekSum = weekSum - 64;
			this.weekSum = this.weekSum + 64;
			week_7.setSelected(true);
		}
		if (weekSum >= 32) {
			weekSum = weekSum - 32;
			this.weekSum = this.weekSum + 32;
			week_6.setSelected(true);
		}
		if (weekSum >= 16) {
			weekSum = weekSum - 16;
			this.weekSum = this.weekSum + 16;
			week_5.setSelected(true);
		}
		if (weekSum >= 8) {
			weekSum = weekSum - 8;
			this.weekSum = this.weekSum + 8;
			week_4.setSelected(true);
		}
		if (weekSum >= 4) {
			weekSum = weekSum - 4;
			this.weekSum = this.weekSum + 4;
			week_3.setSelected(true);
		}
		if (weekSum >= 2) {
			weekSum = weekSum - 2;
			this.weekSum = this.weekSum + 2;
			week_2.setSelected(true);
		}
		if (weekSum == 1) {
			this.weekSum = this.weekSum + 1;
			week_1.setSelected(true);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.yes:
			dialog.dismiss();

			if (callBack != null)
				callBack.onConfirm(weekSum);
			break;
		case R.id.no:
			dialog.dismiss();
			if (callBack != null)
				callBack.onCancel();
			break;
		case R.id.week_1:
		case R.id.week_2:
		case R.id.week_3:
		case R.id.week_4:
		case R.id.week_5:
		case R.id.week_6:
		case R.id.week_7:
			if (v.isSelected()) {
				v.setSelected(false);
				weekSum -= (Integer) v.getTag();
			} else {
				v.setSelected(true);
				weekSum += (Integer) v.getTag();
			}
		default:
			break;
		}

	}

	public interface RepeatDialogCallBack {
		void onConfirm(int select);

		void onCancel();
	}
}
