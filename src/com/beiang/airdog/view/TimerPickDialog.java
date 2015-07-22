package com.beiang.airdog.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.beiang.airdog.widget.CustomDialog;
import com.beiang.airdog.widget.CustomDialog.CGravity;
import com.beiang.airdog.widget.wheelview.NumericWheelAdapter;
import com.beiang.airdog.widget.wheelview.OnWheelChangedListener;
import com.beiang.airdog.widget.wheelview.WheelView;
import com.broadlink.beiangair.R;

public class TimerPickDialog implements OnClickListener {
	CustomDialog dialog;
	TimerPickDialogCallBack callBack;

	int hour;
	int min;

	public void show(Context context,int hour,int min,final TimerPickDialogCallBack callBack) {
		this.callBack = callBack;
		final View view = LayoutInflater.from(context).inflate(R.layout.layout_wheel_pick_dialog, null);
		dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER);

		initView(hour,min,view);

		dialog.show();
	}

	private void initView(int hour,int min,View view) {
		WheelView timer_hour = (WheelView) view.findViewById(R.id.timer_hour);
		WheelView timer_min = (WheelView) view.findViewById(R.id.timer_min);

		TextView no = (TextView) view.findViewById(R.id.no);
		TextView yes = (TextView) view.findViewById(R.id.yes);

		timer_hour.addChangingListener(wheelChangedListener);
		timer_min.addChangingListener(wheelChangedListener);

		no.setOnClickListener(this);
		yes.setOnClickListener(this);

		timer_hour.setCyclic(true);
		timer_min.setCyclic(true);

		timer_hour.setLabel("时钟");
		timer_min.setLabel("分钟");
		
		timer_hour.setAdapter(new NumericWheelAdapter(0, 23, "%02d"));
		timer_min.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		
		for(int i = 0;i<23;i++){
			if(hour == i){
				this.hour = hour;
				timer_hour.setCurrentItem(hour);
				break;
			}
		}
		for(int j = 0;j<59;j++){
			if(min == j){
				this.min = min;
				timer_min.setCurrentItem(min);
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
			Integer[] select = new Integer[2];
			select[0] = hour;
			select[1] = min;

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
			case R.id.timer_hour:
				hour = newValue;
				break;
			case R.id.timer_min:
				min = newValue;
				break;
			default:
				break;
			}
		}
	};

	public interface TimerPickDialogCallBack {
		void onConfirm(Integer[] select);

		void onCancel();
	}
}
