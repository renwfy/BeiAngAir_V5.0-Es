package com.beiang.airdog.ui.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentHomer;
import com.beiang.airdog.net.business.ihomer.GetHomerPair.RspGetHomer;
import com.beiang.airdog.net.business.ihomer.GetHomerPair.RspGetHomer.Data;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.adapter.IHomerNewHomeAdapter;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.view.AlertDialog;
import com.beiang.airdog.view.AlertDialog.AlertDialogCallBack;
import com.beiang.airdog.view.IHomerDeviceDialogs;
import com.beiang.airdog.view.IHomerDeviceDialogs.DialogClickListener;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;

public class IHomerNewHomeActivity extends BaseMultiPartActivity implements OnClickListener, OnItemClickListener,OnItemLongClickListener {
	EditText iv_homername;
	TextView tv_save;
	ListView ihome_list;

	IHomerNewHomeAdapter adapter;
	boolean isEdit = false;
	
	RspGetHomer.Data curData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_ihomer);
		setMenuEnable(false);

		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadData();
	}

	void initView() {
		iv_homername = (EditText) findViewById(R.id.iv_homername);
		(tv_save = (TextView) findViewById(R.id.tv_save)).setOnClickListener(this);

		ihome_list = (ListView) findViewById(R.id.ihome_list);
		ihome_list.setAdapter(adapter = new IHomerNewHomeAdapter());
		ihome_list.setOnItemClickListener(this);
		ihome_list.setOnItemLongClickListener(this);
	}

	void loadData() {
		/** 获取家庭数据 */
		BsOperationHub.instance().getHomer(new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					RspGetHomer rsp = (RspGetHomer) rspData;
					List<RspGetHomer.Data> datas = rsp.datas;
					if (datas.size() > 0) {
						adapter.setData(datas);
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void addHomer() {
		String homeName = iv_homername.getText().toString();
		if (TextUtils.isEmpty(homeName)) {
			Toast.show(mActivity, "请输入家庭名称");
			return;
		}
		BsOperationHub.instance().addHomer(homeName, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if(rspData.isSuccess()){
					Toast.show(mActivity, "添加成功");
					loadData();
					return;
				}
				Toast.show(mActivity, "添加失败");
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "添加失败");
			}
		});
	}
	
	
	private void isEditModel(RspGetHomer.Data data){
		
	}
	
	private void updateHomer(RspGetHomer.Data data) {
		String homeName = iv_homername.getText().toString();
		if (TextUtils.isEmpty(homeName)) {
			Toast.show(mActivity, "请输入家庭名称");
			return;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_save:
			addHomer();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
		// TODO Auto-generated method stub
		final RspGetHomer.Data data = (Data) adapter.getItem(position);
		/*IHomerDeviceDialogs.showNewHomeItemClickDialog(mActivity, new DialogClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.tv_mroom:
					break;
				case R.id.tv_mdev:
					CurrentHomer.instance().curHomer = curData;
					startActivity(new Intent(mActivity,IHomerDeviceActivity.class));
					break;
				default:
					break;
				}
			}
		});*/
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		// TODO Auto-generated method stub
		final RspGetHomer.Data data = (Data) adapter.getItem(position);
		AlertDialog.show(mActivity, 0, "删除家庭", true, new AlertDialogCallBack() {
			@Override
			public void onRight() {
				// TODO Auto-generated method stub
				BsOperationHub.instance().delHomer(data.home_id, new ReqCbk<BaseMsg.RspMsgBase>(){
					@Override
					public void onSuccess(BaseMsg.RspMsgBase rspData) {
						// TODO Auto-generated method stub
						if(rspData.isSuccess()){
							adapter.remove(position);
						}
					}

					@Override
					public void onFailure(com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk.ErrorObject err) {
						// TODO Auto-generated method stub
					}
				});
			}
			
			@Override
			public void onLeft() {
				// TODO Auto-generated method stub
			}
		});
		return false;
	}
}
