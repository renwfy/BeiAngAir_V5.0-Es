package com.beiang.airdog.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.beiang.airdog.BeiAngAirApplaction;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.broadlink.beiangair.R;

public class ExitActivity extends BaseMultiPartActivity implements OnClickListener{
	ImageView iv_exit;
	AnimationDrawable animExit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exit);

		setMenuEnable(false);
		setSwipeBackEnable(false);

		iv_exit = (ImageView) findViewById(R.id.iv_exit);
		iv_exit.setOnClickListener(this);

		loadAnimation();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		exit();
	}

	private void loadAnimation() {
		iv_exit.setBackgroundResource(R.anim.config_ok_anim);
		animExit = (AnimationDrawable) iv_exit.getBackground();
	}
	
	private void exit(){
		animExit.stop();
		animExit.start();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				animExit.stop();
				BeiAngAirApplaction.getInstance().exit();
			}
		}, 1500);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		animExit.stop();
		animExit.start();
	}
}
