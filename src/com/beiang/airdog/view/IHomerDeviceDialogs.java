package com.beiang.airdog.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.beiang.airdog.widget.CustomDialog;
import com.beiang.airdog.widget.CustomDialog.CGravity;
import com.broadlink.beiangair.R;

public class IHomerDeviceDialogs {
	public interface DialogClickListener {
		void onClick(View v);
	}

	public static void showLongTouchDialog(Context context) {
		final View view = LayoutInflater.from(context).inflate(R.layout.layout_homer_device_dialog, null);
		final CustomDialog dialog = CustomDialog.create(context, R.style.customDialogOne);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.BOTTOM);
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	/***
	 * 设置脚本
	 * 
	 * @param context
	 */
	public static void showHomeSetMasterDialog(Context context) {
		final View view = LayoutInflater.from(context).inflate(R.layout.layout_homer_setmaster_dialog, null);
		final CustomDialog dialog = CustomDialog.create(context, R.style.customDialogOne);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.BOTTOM);
		dialog.show();
	}

	/***
	 * 
	 * 
	 * @param context
	 */
	public static void showNewHomeItemClickDialog(Context context, final DialogClickListener listener) {
		final View view = LayoutInflater.from(context).inflate(R.layout.layout_homer_newhome_itemclick_dialog, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER);
		view.findViewById(R.id.no).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.tv_mroom).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				listener.onClick(v);
			}
		});
		view.findViewById(R.id.tv_mdev).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				listener.onClick(v);
			}
		});
		dialog.show();
	}

}
