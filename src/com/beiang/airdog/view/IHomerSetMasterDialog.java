package com.beiang.airdog.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.beiang.airdog.widget.CustomDialog;
import com.beiang.airdog.widget.CustomDialog.CGravity;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;

public class IHomerSetMasterDialog {
	public interface ConfirmListener{
		void start(String sH, String sM, String eH, String eM, String temp, String hum);
	}

	/***
	 * 设置脚本
	 * 
	 * @param context
	 */
	public static void showHome(final Context context,final ConfirmListener listener) {
		final View view = LayoutInflater.from(context).inflate(R.layout.layout_homer_setmaster_dialog, null);
		final CustomDialog dialog = CustomDialog.create(context, R.style.customDialogOne);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.BOTTOM);
		final EditText starthour = (EditText) view.findViewById(R.id.starthour);
		final EditText startmin = (EditText) view.findViewById(R.id.startmin);
		final EditText endthour = (EditText) view.findViewById(R.id.endthour);
		final EditText endmin = (EditText) view.findViewById(R.id.endmin);
		final EditText et_temp = (EditText) view.findViewById(R.id.et_temp);
		final EditText et_hum = (EditText) view.findViewById(R.id.et_hum);
		view.findViewById(R.id.tv_no).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.tv_yes).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String shour = starthour.getText().toString();
				String smin = startmin.getText().toString();
				String ehour = endthour.getText().toString();
				String emin = endmin.getText().toString();
				String temp = et_temp.getText().toString();
				String hum = et_hum.getText().toString();
				if(TextUtils.isEmpty(shour) || TextUtils.isEmpty(smin)|| TextUtils.isEmpty(ehour)|| TextUtils.isEmpty(emin)){
					Toast.show(context, "请设置生效时间段");
					return;
				}
				if(TextUtils.isEmpty(temp) || TextUtils.isEmpty(hum)){
					Toast.show(context, "请设置生效条件");
					return;
				}
				if(shour.length() !=2){
					shour = "0"+shour;
				}
				if(smin.length() !=2){
					smin = "0"+smin;
				}
				if(ehour.length() !=2){
					ehour = "0"+ehour;
				}
				if(emin.length() !=2){
					emin = "0"+emin;
				}
				dialog.dismiss();
				listener.start(shour, smin, ehour, emin, temp, hum);
			}
		});
		dialog.show();
	}

}
