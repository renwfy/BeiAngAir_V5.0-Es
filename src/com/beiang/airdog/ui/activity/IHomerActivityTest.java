package com.beiang.airdog.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;

import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.ihomer.HomeGetDevPair.RspHomeGetDev;
import com.beiang.airdog.net.business.ihomer.GetHomerPair.RspGetHomer;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.adapter.IHomerDeviceAdapter;
import com.beiang.airdog.ui.adapter.IHomeAdapter;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.MenuEntity;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.view.AlertDialog;
import com.beiang.airdog.view.AlertDialog.AlertDialogCallBack;
import com.beiang.airdog.widget.CannotRollGridView;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;

public class IHomerActivityTest extends BaseMultiPartActivity implements OnClickListener {
	private EditText home_name, dev_id, dev_name, dev_home_id, master_dev, master_home, orther_input;
	private CannotRollGridView homelist, devlist;
	IHomeAdapter hAdapter;
	IHomerDeviceAdapter dAdapter;

	EditText home_ac_id, home_ac_o_id, user_ac_id, user_ac_o_id, scrip_ac_id, scrip_ac_o_id,contrl_ac_id,contrl_ac_o_id;

	long curHomeId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ihomer_tm);

		initMenuData();

		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getHomer();
	}

	private void initMenuData() {
		// TODO Auto-generated method stub
		List<MenuEntity> menus = new ArrayList<MenuEntity>();

		MenuEntity menu = new MenuEntity();
		menu.setMenu_key("share");
		menu.setMenu_name("分享");
		menu.setMenu_icon(R.drawable.ic_menu_share);
		menus.add(menu);
		prepareOptionsMenu(menus);
	}

	private void initView() {
		findViewById(R.id.adddevice).setOnClickListener(this);
		findViewById(R.id.addhomer).setOnClickListener(this);
		findViewById(R.id.setmonitor).setOnClickListener(this);

		home_name = (EditText) findViewById(R.id.home_name);
		dev_id = (EditText) findViewById(R.id.dev_id);
		dev_name = (EditText) findViewById(R.id.dev_name);
		dev_home_id = (EditText) findViewById(R.id.dev_home_id);
		master_dev = (EditText) findViewById(R.id.master_dev);
		master_home = (EditText) findViewById(R.id.master_home);
		orther_input = (EditText) findViewById(R.id.orther_input);

		homelist = (CannotRollGridView) findViewById(R.id.homelist);
		homelist.setAdapter(hAdapter = new IHomeAdapter());
		homelist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				RspGetHomer.Data ety = (RspGetHomer.Data) hAdapter.getItem(position);
				curHomeId = ety.home_id;
				getDevice(curHomeId);
			}
		});

		devlist = (CannotRollGridView) findViewById(R.id.devlist);
		devlist.setAdapter(dAdapter = new IHomerDeviceAdapter());
		devlist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				RspHomeGetDev.Data ety = (RspHomeGetDev.Data) dAdapter.getItem(position);
				final long dev_id = ety.device_id;
				AlertDialog.show(mActivity, 0, "操作家中设备", "更新", "删除", new AlertDialogCallBack() {
					@Override
					public void onRight() {
						// TODO Auto-generated method stub
						delDev(curHomeId, dev_id);
					}

					@Override
					public void onLeft() {
						// TODO Auto-generated method stub
						String text = orther_input.getText().toString();
						if (TextUtils.isEmpty(text)) {
							Toast.show(mActivity, "请输入名称");
							return;
						}

						updateDev(curHomeId, dev_id, text);
						orther_input.setText("");
					}
				});
			}
		});

		home_ac_id = (EditText) findViewById(R.id.home_ac_id);
		home_ac_o_id = (EditText) findViewById(R.id.home_ac_o_id);
		user_ac_id = (EditText) findViewById(R.id.user_ac_id);
		user_ac_o_id = (EditText) findViewById(R.id.user_ac_o_id);
		scrip_ac_id = (EditText) findViewById(R.id.scrip_ac_id);
		scrip_ac_o_id = (EditText) findViewById(R.id.scrip_ac_o_id);
		contrl_ac_id= (EditText) findViewById(R.id.contrl_ac_id);
		contrl_ac_o_id= (EditText) findViewById(R.id.contrl_ac_o_id);

		findViewById(R.id.home_ac_add).setOnClickListener(this);
		findViewById(R.id.home_ac_del).setOnClickListener(this);
		findViewById(R.id.home_ac_update).setOnClickListener(this);
		findViewById(R.id.home_ac_get).setOnClickListener(this);

		findViewById(R.id.user_ac_add).setOnClickListener(this);
		findViewById(R.id.user_ac_del).setOnClickListener(this);
		findViewById(R.id.user_ac_update).setOnClickListener(this);
		findViewById(R.id.user_ac_get).setOnClickListener(this);

		findViewById(R.id.scrip_ac_add).setOnClickListener(this);
		findViewById(R.id.scrip_ac_del).setOnClickListener(this);
		findViewById(R.id.scrip_ac_update).setOnClickListener(this);
		findViewById(R.id.scrip_ac_get).setOnClickListener(this);
		
		findViewById(R.id.contrl).setOnClickListener(this);
	}

	private void getHomer() {
		BsOperationHub.instance().getHomer(new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					RspGetHomer rsp = (RspGetHomer) rspData;
					List<RspGetHomer.Data> datas = rsp.datas;
					hAdapter.setData(datas);
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void addHomer() {
		String name = home_name.getText().toString();
		BsOperationHub.instance().addHomer(name, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "添加成功");
				getHomer();
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "添加失败");
			}
		});
	}

	private void getDevice(final long home_id) {
		BsOperationHub.instance().homeGetDevice(home_id, 0,new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					RspHomeGetDev rsp = (RspHomeGetDev) rspData;
					List<RspHomeGetDev.Data> datas = rsp.datas;
					dAdapter.setData(datas);
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void updateDev(final long home_id, long dev_id, String name) {
		BsOperationHub.instance().homeUpdateDevice(home_id, dev_id, "", "devinfo","prodect_id",name, "", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "更新成功");
				getDevice(home_id);
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "更新失败");
			}
		});
	}

	private void delDev(final long home_id, long dev_id) {
		BsOperationHub.instance().homeDelDevice(home_id, dev_id, "", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "删除成功");
				getDevice(home_id);
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "删除失败");
			}
		});
	}

	private void setMonitor() {
		String homeIdStr = master_home.getText().toString();
		final long home_id = Long.parseLong(homeIdStr);
		String devIdStr = master_dev.getText().toString();
		final long dev_id = Long.parseLong(devIdStr);
		BsOperationHub.instance().homeSetMonitor(home_id, dev_id, "", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "设置成功");
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "设置失败");
			}
		});
	}

	private void adduser() {
		BsOperationHub.instance().homeAddUser(4, 100000051, 1012774, "atonn", "user", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void deluser() {
		BsOperationHub.instance().homeDelUser(4, 0, 100000051, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void getuser() {
		BsOperationHub.instance().homeGetUser(4, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void addTimer() {
		List<Integer> week = new ArrayList<Integer>();
		week.add(1);
		week.add(2);
		week.add(3);
		week.add(4);
		BsOperationHub.instance().homeAddTime(4, 1019235, "", 1, 96000, week, new byte[] { 0x00, 0x12 }, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void updateTimer() {
		List<Integer> week = new ArrayList<Integer>();
		week.add(1);
		week.add(2);
		week.add(3);
		week.add(4);
		BsOperationHub.instance().homeUpdateTime(4, 1, 1019235, "", 0, 36000, week, new byte[] { 0x00, 0x12 }, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void getTimer() {
		BsOperationHub.instance().homeGetTime(4, 0,new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void delTimer() {
		BsOperationHub.instance().homeDelTime(4, 1, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void getRoom() {
		BsOperationHub.instance().homeGetRoom(4, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void addRoom() {
		BsOperationHub.instance().homeAddRoom(4, "小黑屋", "关闭人呢", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void delRoom() {
		BsOperationHub.instance().homeDelRoom(4, 1, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void updateRoom() {
		BsOperationHub.instance().homeUpdateRoom(4, 1, "我的小黑屋", "反省用的", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void addScrip() {
		List<Integer> week = new ArrayList<Integer>();
		week.add(1);
		week.add(2);
		week.add(3);
		week.add(4);
		BsOperationHub.instance().homeAddScript(4, 1019235, "", 0, 0, 963000, week, "9-18", "temp>i.25", "{\"act\":\"open\"}",
				new ReqCbk<RspMsgBase>() {
					@Override
					public void onSuccess(RspMsgBase rspData) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onFailure(ErrorObject err) {
						// TODO Auto-generated method stub
					}
				});
	}

	private void updateScrip() {
		List<Integer> week = new ArrayList<Integer>();
		week.add(1);
		week.add(2);
		BsOperationHub.instance().homeUpdateScript(4, 1019235,2, "", 0, 0, 963000, week, "9-18", "temp>i.25", "{\"act\":\"open\"}",
				new ReqCbk<RspMsgBase>() {
					@Override
					public void onSuccess(RspMsgBase rspData) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onFailure(ErrorObject err) {
						// TODO Auto-generated method stub
					}
				});
	}

	private void getScrip() {
		BsOperationHub.instance().homeGetScript(4, 0, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void delScrip() {
		BsOperationHub.instance().homeDelScript(4, 1, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	byte b = 0;
	
	private void contrl() {
		if (b == 0x00) {
			b = 0x01;
		} else {
			b = 0x00;
		}
		
		byte[] cmd = new byte[]{(byte) 0xfe , 0x00 , 0x15 , 0x00 , (byte) 0xa0 , 0x00 , 0x02 , 0x01 , 0x00 , 0x05 , 0x07 , (byte) 0xdf , 0x06 , 0x02 , 0x00 , 0x0b , 0x08 , 0x00 , 0x00 , 0x00 , (byte) 0xaa};
		/*byte[] cmd = new byte[] { (byte) 0xfe, 0x41, 0x00, 0x02, b, 0x00, 0x01, 0x00, 0x00, 0x46, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xaa };*/
		LogUtil.i(cmd);
		BsOperationHub.instance().homeSendCmd(4, 100000845, "", cmd, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void addDevice() {
		String devName = dev_name.getText().toString();
		String homeIdStr = dev_home_id.getText().toString();
		final long home_id = Long.parseLong(homeIdStr);
		String devIdStr = dev_id.getText().toString();
		final long dev_id = Long.parseLong(devIdStr);
		BsOperationHub.instance().homeAddDevice(home_id, dev_id, "", "devInfo","prodect_id",devName, "0", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "添加成功");
				getDevice(home_id);
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "添加失败");
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.adddevice:
			addDevice();
			break;
		case R.id.addhomer:
			addHomer();
			break;
		case R.id.setmonitor:
			setMonitor();
			break;
		case R.id.scrip_ac_add:
			addScrip();
			break;
		case R.id.scrip_ac_del:
			delScrip();
			break;
		case R.id.scrip_ac_update:
			updateScrip();
			break;
		case R.id.scrip_ac_get:
			getScrip();
			break;
		case R.id.contrl:
			contrl();
			break;
		default:
			break;
		}

	}

}
