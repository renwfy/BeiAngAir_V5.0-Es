package com.beiang.airdog.ui.base;

import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.beiang.airdog.ui.Mode_Manger;
import com.beiang.airdog.ui.adapter.BaseMultiPartMenuAdapter;
import com.beiang.airdog.ui.model.MenuEntity;
import com.broadlink.beiangair.R;

/***
 * 支持左滑关闭右滑菜单
 * 
 * @author LSD
 * 
 */
public class BaseMultiPartActivity extends BaseSwipebackActivity {
	private SwipeBackLayout mSwipeBackLayout;
	private FrameLayout mContent;
	private DrawerLayout mDrawerLayout;
	private ListView mMenuList;
	private BaseMultiPartMenuAdapter adapter;
	private OnItemClickListener itemClickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_multi_base);

		findView();
		initMenuList();
	}

	@Override
	public void setContentView(int layoutResID) {
		getLayoutInflater().inflate(layoutResID, mContent, true);
	}

	private void findView() {
		mSwipeBackLayout = getSwipeBackLayout();
		mContent = (FrameLayout) findViewById(R.id.content_frame);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mMenuList = (ListView) findViewById(R.id.right_drawer);
	}

	private void initMenuList() {
		mDrawerLayout.setScrimColor(Color.parseColor("#30000000"));
		mDrawerLayout.setDrawerShadow(R.drawable.shadow, GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK);
		adapter = new BaseMultiPartMenuAdapter();
		mMenuList.setAdapter(adapter);
		mMenuList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawers();
				MenuEntity ety = (MenuEntity) adapter.getItem(position);
				if (itemClickListener != null) {
					itemClickListener.onItemClick(parent, view, position, id);
				}
				Mode_Manger.startModesActivity(mActivity, ety);
			}
		});
	}

	/***
	 * 设置左滑关闭是否可用
	 * 
	 * @param enable
	 */
	public void setSwipeBackEnable(boolean enable) {
		mSwipeBackLayout.setEnableGesture(enable);
	}

	/***
	 * 设置菜单是否可用
	 * 
	 * @param enable
	 */
	public void setMenuEnable(boolean enable) {
		if (enable) {
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		} else {
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}
	}

	/***
	 * 设置菜单数据
	 * 
	 * @param menus
	 */
	public void prepareOptionsMenu(List<MenuEntity> menus) {
		adapter.setData(menus);
	}

	/***
	 * 菜单点击回调
	 * 
	 * @param itemClickListener
	 */
	public void setOnItemClickListener(OnItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}
}
