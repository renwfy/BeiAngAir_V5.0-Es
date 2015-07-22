package com.beiang.airdog.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiang.airdog.widget.CustomDialog;
import com.beiang.airdog.widget.CustomDialog.CGravity;
import com.broadlink.beiangair.R;

public class AlertDialog {

	public static void show(Context context, int contentImg, String contentTxt, boolean showLeft, final AlertDialogCallBack callBack) {
		if (showLeft) {
			show(context, contentImg, contentTxt, "取消", "确定", callBack);
		} else {
			show(context, contentImg, contentTxt, "", "确定", callBack);
		}
	}

	public static void show(Context context, int contentImg, String contentTxt, String leftTxt, String rightTxt,
			final AlertDialogCallBack callBack) {
		final View view = LayoutInflater.from(context).inflate(R.layout.layout_comm_alert_dialog, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER);

		ImageView img = (ImageView) view.findViewById(R.id.content_img);
		TextView txt = (TextView) view.findViewById(R.id.content_txt);
		TextView left = (TextView) view.findViewById(R.id.left);
		TextView right = (TextView) view.findViewById(R.id.right);

		if (contentImg != 0) {
			img.setImageResource(contentImg);
		}
		txt.setText(contentTxt);
		left.setText(leftTxt);
		right.setText(rightTxt);
		boolean showLeft = !TextUtils.isEmpty(leftTxt);
		if (showLeft) {
			left.setVisibility(View.VISIBLE);
			left.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if (callBack != null)
						callBack.onLeft();
				}
			});
			right.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if (callBack != null)
						callBack.onRight();
				}
			});
		} else {
			right.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if (callBack != null)
						callBack.onRight();
				}
			});
		}

		dialog.show();
	}

	public interface AlertDialogCallBack {
		void onLeft();

		void onRight();
	}

}
