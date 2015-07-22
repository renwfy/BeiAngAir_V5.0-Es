package com.beiang.airdog.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.utils.AssetsUtils;
import com.broadlink.beiangair.R;

public class AboutActivity extends BaseMultiPartActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		setMenuEnable(false);

		String string = AssetsUtils.getAgreementByName("data/introduce.txt");
		((TextView) findViewById(R.id.introduce)).setText(string);

		findViewById(R.id.pro_introduce).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(mActivity, ProductActivity.class));
			}
		});
	}
}
