package com.beiang.airdog.ui.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.db.DB_Location;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.CurrentUser;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.QueryDevDataPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.adapter.DeviceAdapter;
import com.beiang.airdog.ui.adapter.WeatherViewPagerAdapter;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.FirmwareEntity;
import com.beiang.airdog.ui.model.MenuEntity;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.view.AlertDialog;
import com.beiang.airdog.view.AlertDialog.AlertDialogCallBack;
import com.beiang.airdog.view.WeatherView;
import com.beiang.airdog.widget.Toast;
import com.beiang.airdog.widget.pagerindicator.CirclePageIndicator;
import com.beiang.airdog.widget.pullrefresh.PullToRefreshBase;
import com.beiang.airdog.widget.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.beiang.airdog.widget.pullrefresh.PullToRefreshGridView;
import com.broadlink.beiangair.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class DeviceActivity extends BaseMultiPartActivity implements OnClickListener, OnLongClickListener, OnItemClickListener {
	private RelativeLayout dev_layout;
	private WeatherView weather_layout;
	private PullToRefreshGridView pull_to_refresh_gridview;
	private GridView gridView;
	private DeviceAdapter adapter;
	private RelativeLayout rl_weather_layout;
	private ViewPager viewPager;
	private CirclePageIndicator pageIndicator;
	Handler mHandler;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device);
		
		// 设置父组件
		setSwipeBackEnable(false);
		setMenuEnable(true);
		setOnItemClickListener(this);

		// 设置菜单
		initMenuData();
		mHandler = new Handler();

		// 初始化
		initView();
		setListener();

		// 获取数据
		initData();
		//weather_layout.setCanChange(true);
		//weather_layout.load();
		
		//设置布局高度
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				initWeatherLayoutPama();
			}
		}, 200);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refrashWeather();
		//startMxRefreshTimer();
	}

	private void initMenuData() {
		// TODO Auto-generated method stub
		List<MenuEntity> menus = new ArrayList<MenuEntity>();
		MenuEntity menu = new MenuEntity();
		menu.setMenu_key("acout");
		menu.setMenu_name("我的账号");
		menu.setMenu_icon(R.drawable.ic_menu_acout);
		menus.add(menu);

		menu = new MenuEntity();
		menu.setMenu_key("plus");
		menu.setMenu_name("添加设备");
		menu.setMenu_icon(R.drawable.ic_menu_plus);
		menus.add(menu);

		menu = new MenuEntity();
		menu.setMenu_key("minus");
		menu.setMenu_name("删除设备");
		menu.setMenu_icon(R.drawable.ic_menu_minus);
		menus.add(menu);

		menu = new MenuEntity();
		menu.setMenu_key("help");
		menu.setMenu_name("帮助");
		menu.setMenu_icon(R.drawable.ic_menu_help);
		menus.add(menu);
		
		menu = new MenuEntity();
		menu.setMenu_key("cloud");
		menu.setMenu_name("云家庭");
		menu.setMenu_icon(R.drawable.ic_menu_help);
		menus.add(menu);

		menu = new MenuEntity();
		menu.setMenu_key("share");
		menu.setMenu_name("分享");
		menu.setMenu_icon(R.drawable.ic_menu_share);
		menus.add(menu);
		prepareOptionsMenu(menus);
	}

	private void initView() {
		weather_layout = (WeatherView) findViewById(R.id.weather_layout);
		dev_layout = (RelativeLayout) findViewById(R.id.dev_layout);

		pull_to_refresh_gridview = (PullToRefreshGridView) findViewById(R.id.gv_device);
		pull_to_refresh_gridview.setPullLoadEnabled(false);
		pull_to_refresh_gridview.setScrollLoadEnabled(true);
		gridView = pull_to_refresh_gridview.getRefreshableView();
		gridView.setNumColumns(2);
		gridView.setHorizontalSpacing(6);
		gridView.setVerticalSpacing(6);
		gridView.setCacheColorHint(Color.parseColor("#00000000"));
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setVerticalScrollBarEnabled(false);
		gridView.setHorizontalFadingEdgeEnabled(false);
		
		adapter = new DeviceAdapter();
		adapter.setOnClickListener(this);
		adapter.setOnLongClickListener(this);
		gridView.setAdapter(adapter);
		
		rl_weather_layout = (RelativeLayout) findViewById(R.id.rl_weather_layout);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		pageIndicator = (CirclePageIndicator) findViewById(R.id.page_indicator);
		
		pull_to_refresh_gridview.setOnRefreshListener(new OnRefreshListener<GridView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				getDataFromNet();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	void initWeatherLayoutPama(){
		int height = weather_layout.getHeight();
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)viewPager.getLayoutParams();
		params.height = height;
		viewPager.setLayoutParams(params);
	}
	
	private void refrashWeather() {
		DB_Location dbLocation = new DB_Location(mActivity);
		List<String> citys = dbLocation.getCityArray();
		if (citys != null) {
			boolean b = true;
			for (String city : citys) {
				if (city.contains(dbLocation.getCurCity())) {
					b = false;
					break;
				}
			}
			if (b) {
				citys.add(0, dbLocation.getCurCity());
			}
		} else {
			citys = new ArrayList<String>();
			citys.add(dbLocation.getCurCity());
		}
		viewPager.setAdapter(new WeatherViewPagerAdapter(mActivity, citys));
		pageIndicator.setViewPager(viewPager);
	}
	
	private void setLastUpdateTime() {
		String text = formatDateTime(System.currentTimeMillis());
		pull_to_refresh_gridview.setLastUpdatedLabel(text);
	}
	private String formatDateTime(long time) {
		if (0 == time) {
			return "";
		}
		SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
		return mDateFormat.format(new Date(time));
	}
	

	private void setListener() {
		// 边上点击
		dev_layout.setOnClickListener(this);
	}

	private void initData() {
		getDataFromNet();
	}
	
	/**************
	 * 定时器
	 */
	private void startMxRefreshTimer() {
		mHandler.postDelayed(task, 10000);
	}

	private void stopMxRefreshTimer() {
		mHandler.removeCallbacks(task);
	}

	private Runnable task = new Runnable() {
		@Override
		public void run() {
			getDataFromNet();
			startMxRefreshTimer();
		}
	};

	private void getDataFromNet() {
		BsOperationHub.instance().queryBindList(CurrentUser.instance().getUserId(), new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				pull_to_refresh_gridview.onPullDownRefreshComplete();
				setLastUpdateTime();
				refrashData();
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				pull_to_refresh_gridview.onPullDownRefreshComplete();
				adapter.setData(null);
			}
		});
	}

	private void refrashData() {
		List<DevEntity> devList = CurrentDevice.instance().devList;
		if (devList != null && devList.size() > 0) {
			if (devList.size() <= 2) {
				gridView.setNumColumns(1);
			} else {
				gridView.setNumColumns(2);
			}
		}
		adapter.setData(devList);
	}

	@Override
	public void onBackPressed() {
		AlertDialog.show(mActivity, 0, "退出应用？", true, new AlertDialogCallBack() {
			@Override
			public void onRight() {
				// TODO Auto-generated method stub
				startActivity(new Intent(mActivity, ExitActivity.class));
				finish();
			}

			@Override
			public void onLeft() {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == 100) {
			LogUtil.i("LSD", "config ok");
			getDataFromNet();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dev_layout:
		case R.id.ll_shade:
			adapter.cleanDelStatus();
			break;
		case R.id.dev_icon:
		case R.id.dev_outline:
			int position = (Integer) v.getTag();
			onItemClick(position);
			break;
		case R.id.dev_name:
			int p = (Integer) v.getTag();
			DevEntity curDevice = (DevEntity) adapter.getItem(p);
			CurrentDevice.instance().curDevice = curDevice;
			startActivity(new Intent(mActivity, EditInfoActivity.class));
			break;
		case R.id.dev_del:
			int pstion = (Integer) v.getTag();
			onItemLongClick(pstion);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		stopMxRefreshTimer();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		int position = (Integer) v.getTag();
		adapter.delCurDev(position);
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		MenuEntity ety = (MenuEntity) parent.getAdapter().getItem(position);
		if ("minus".equals(ety.getMenu_key())) {
			adapter.delAllDev();
		}
	}

	private void onItemClick(int position) {
		Intent intent = new Intent();
		DevEntity curDevice = (DevEntity) adapter.getItem(position);
		CurrentDevice.instance().curDevice = curDevice;
		if ("offline".equals(curDevice.status)) {
			intent.setClass(DeviceActivity.this, AirdogActivity.class);
			startActivityForResult(intent, 200);
			Toast.show(mActivity, "设备不在线");
			return;
		}
		/*if (curDevice.airInfo == null) {
			Toast.show(mActivity, "未正确获取数据");
			return;
		}*/

		
		if (curDevice.devType == Device.DT_Airdog) { // AirDog
			intent.setClass(DeviceActivity.this, AirdogActivity.class);
		} else if (curDevice.devType == Device.DT_TAir) { // TAir
			intent.setClass(DeviceActivity.this, TairActivity.class);
		}else if (curDevice.devType == Device.DT_Light) { // Light
			intent.setClass(DeviceActivity.this, DeviceTestActivity.class);
		}else if (curDevice.devType == Device.DT_PowerSocket) { // PowerSocket
			intent.setClass(DeviceActivity.this, DeviceTestActivity.class);
		} else {
			intent.setClass(DeviceActivity.this, DeviceControlActivity.class);
		}

		startActivityForResult(intent, 200);
	}

	private void onItemLongClick(final int position) {
		final DevEntity device = (DevEntity) adapter.getItem(position);
		String hintStr = getString(R.string.delete_device_hint);
		final String right = device.role;
		if ("owner".equals(right)) {
			hintStr = getString(R.string.delete_device_hint2);
		}
		AlertDialog.show(mActivity, 0, "删除设备\n" + hintStr, true, new AlertDialogCallBack() {
			@Override
			public void onLeft() {
				// TODO Auto-generated method stub
				adapter.cleanDelStatus();
			}

			@Override
			public void onRight() {
				// TODO Auto-generated method stub
				adapter.cleanDelStatus();
				if ("owner".equals(right)) {
					// ///// 复位
					//resetDevice(device, position);
					unbindDevice(device, position);
				} else {
					// //// 解绑
					unbindDevice(device, position);
				}
			}
		});
	}
	
	//100000051  这个用户绑定了  设备 100000845：   //1012774

	/*private void resetDevice(final DevEntity device, final int position) {
		BsOperationHub.instance().queryDevFirmware(device, "", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					QueryDevDataPair.RspQueryDevData rsp = (QueryDevDataPair.RspQueryDevData) rspData;
					if (rsp.data != null && rsp.reply != null) {
						String firmwareStr = new String(rsp.reply);
						if (!TextUtils.isEmpty(firmwareStr)) {
							FirmwareEntity fEntity = null;
							try {
								fEntity = new Gson().fromJson(firmwareStr, FirmwareEntity.class);
							} catch (JsonSyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							if (fEntity != null && !TextUtils.isEmpty(fEntity.getVersioncode())) {
								// //// 新设备可以复位
								BsOperationHub.instance().resetDevice(device.devId, new ReqCbk<RspMsgBase>() {
									@Override
									public void onSuccess(RspMsgBase rspData) {
										// TODO Auto-generated method stub
										if (rspData.isSuccess()) {
											adapter.removeDevice(position);
											if (adapter.getCount() <= 2) {
												gridView.setNumColumns(1);
											} else {
												gridView.setNumColumns(2);
											}
										}
									}

									@Override
									public void onFailure(ErrorObject err) {
										// TODO Auto-generated method stub
									}
								});
							}
						}
					}
				} else {
					// 不在线或者老的设备不能复位，设备列表删除不了，会越来越多（所以再进行一次解绑操作）
					unbindDevice(device, position);
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}*/
	
	private void resetDevice(final DevEntity device, final int position) {
		BsOperationHub.instance().resetDevice(device.devId, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					adapter.removeDevice(position);
					if (adapter.getCount() <= 2) {
						gridView.setNumColumns(1);
					} else {
						gridView.setNumColumns(2);
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void unbindDevice(final DevEntity device, final int position) {
		BsOperationHub.instance().unbindDevice(device.devId, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					adapter.removeDevice(position);
					if (adapter.getCount() <= 2) {
						gridView.setNumColumns(1);
					} else {
						gridView.setNumColumns(2);
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

}
