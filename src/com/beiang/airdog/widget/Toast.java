package com.beiang.airdog.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.broadlink.beiangair.R;

public class Toast {

	public static void show(Context context, String msg) {
		TextView txt = new TextView(context);
		txt.setText(msg);
		txt.setTextColor(Color.parseColor("#ffffffff"));
		txt.setBackgroundResource(R.drawable.rectangle_666666);
		txt.setTextSize(13);
		txt.setPadding(40, 10, 40, 10);
		android.widget.Toast toast = new android.widget.Toast(context);
		toast.setView(txt);
		toast.setDuration(android.widget.Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 80);
		toast.show();
	}
}
