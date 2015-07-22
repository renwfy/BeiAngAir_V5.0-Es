package com.beiang.airdog.adaptive;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 分配率通配类
 * 
 * @author gejw
 * 
 */
public class LayoutAdapter {
	/**
	 * 基准分辨率的宽
	 */
	public float STANDARD_SCREEN_WIDTH;

	/**
	 * 基准分辨率的高
	 */
	public float STANDARD_SCREEN_HEIGHT;

	/**
	 * 系统当前的分辨率的宽
	 */
	public float CURRENT_SCREEN_WIDTH;

	/**
	 * 系统当前的分辨率的高
	 */
	public float CURRENT_SCREEN_HEIGHT;

	/**
	 * 基准屏幕密度
	 */
	public static final float STANDARD_DENSITY = 160;

	/**
	 * 当前屏幕密度
	 */
	private float CURRENT_DENSITY;

	/**
	 * 屏幕密度比例
	 */
	private float DENSITY_RATIO;

	/**
	 * 屏幕宽度比例
	 */
	private float WIDTH_RATIO;

	/**
	 * 屏幕高度比例
	 */
	private float HEIGHT_RATIO;

	/**
	 * 组件基准的宽度
	 */
	private float viewStandardWidth;

	/**
	 * 组件基准的高度
	 */
	private float viewStandardHeight;

	/**
	 * 组件基准的距离左边的距离
	 */
	private float viewStandardMarginLeft;

	/**
	 * 组件基准的距离顶部的距离
	 */
	private float viewStandardMarginTop;

	/**
	 * 组件基准的距离右的距离
	 */
	private float viewStandardMarginRight;

	/**
	 * 组件基准的距离底部的距离
	 */
	private float viewStandardMarginBottom;

	/**
	 * 组建基准的比重
	 * */
	private float viewStandarWeight;

	/**
	 * 组件当前的宽
	 */
	private float viewCurrentWidth;

	/**
	 * 组件当前的高
	 */
	private float viewCurrentHeight;

	/**
	 * 组件当前距离左边的距离
	 */
	private float viewCurrentMarginLeft;

	/**
	 * 组件当前距离右边的距离
	 */
	private float viewCurrentMarginRight;

	/**
	 * 组件当前距离底部的距离
	 */
	private float viewCurrentMarginBottom;

	/**
	 * 组件当前距离顶部的距离
	 */
	private float viewCurrentMarginTop;

	/**
	 * 组件当前比重
	 * */
	private float viewCurrentWeight;

	/**
	 * UI组件的对象
	 */
	private View view;

	/**
	 * 此View的父类布局的类型
	 */
	private int parentLayoutType;

	/**
	 * 父类布局的类型为相对布局
	 */
	private final int LAYOUT_TYPE_RELATiVELAYOUT = LayoutInformation.R;

	/**
	 * 父类布局的类型为线性布局
	 */
	private final int LAYOUT_TYPE_LINEARLAYOUT = LayoutInformation.L;

	/**
	 * 布局属性为wrap_content
	 */
	private final int LAYOUTPARAMS_WARP_CONTENT = LayoutParams.WRAP_CONTENT;

	/**
	 * 布局属性为fill_parent
	 */
	private final int LAYOUTPARAMS_FILL_PARENT = LayoutParams.FILL_PARENT;

	private Context context;

	/**
	 * 类对象实例化时,设置 基准屏幕宽度,高度
	 * 
	 * @param context
	 *            Context
	 * @param standardWidth
	 *            基准屏幕的宽
	 * @param standardHeight
	 *            基准屏幕的高
	 */
	public LayoutAdapter(Context context, float standardWidth,
			float standardHeight) {
		this.context = context;
		getScreenSize();
		STANDARD_SCREEN_HEIGHT = standardHeight;
		STANDARD_SCREEN_WIDTH = standardWidth;
		// 计算宽高比率
		WIDTH_RATIO = CURRENT_SCREEN_WIDTH / STANDARD_SCREEN_WIDTH;
		HEIGHT_RATIO = CURRENT_SCREEN_HEIGHT / STANDARD_SCREEN_HEIGHT;
	}

	/**
	 * 获取当前屏幕大小和密度
	 */
	private void getScreenSize() {
		SharedPreferences preferences = context.getSharedPreferences("screenconfig", 0);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		//宽度
		if(preferences.getInt("width", 0) != displayMetrics.widthPixels){
			CURRENT_SCREEN_WIDTH = displayMetrics.widthPixels;
			preferences.edit().putInt("width", displayMetrics.widthPixels).commit();
		}
		else{
			CURRENT_SCREEN_WIDTH = preferences.getInt("width", 0);
		}
		//高度
		if(preferences.getInt("height", 0) != displayMetrics.heightPixels){
			CURRENT_SCREEN_HEIGHT = displayMetrics.heightPixels;
			preferences.edit().putInt("height", displayMetrics.heightPixels).commit();
		}
		else{
			CURRENT_SCREEN_HEIGHT = preferences.getInt("height", 0);
		}
		//dpi值
		if(preferences.getInt("densityDpi", 0) != displayMetrics.densityDpi){
			CURRENT_DENSITY = displayMetrics.densityDpi;
			preferences.edit().putInt("densityDpi", displayMetrics.densityDpi).commit();
		}
		else{
			CURRENT_DENSITY = preferences.getInt("densityDpi", 0);
		}
//		CURRENT_SCREEN_HEIGHT = displayMetrics.heightPixels;
//		CURRENT_DENSITY = displayMetrics.densityDpi;
		DENSITY_RATIO = STANDARD_DENSITY / CURRENT_DENSITY;
	}

	public void addViewLayout(LayoutInformation information) {
		view = information.getView();
		viewStandardWidth = information.getViewWidth();
		viewStandardHeight = information.getViewHeight();
		viewStandardMarginLeft = information.getViewMarginLeft();
		viewStandardMarginTop = information.getViewMarginTop();
		viewStandardMarginRight = information.getViewMarginRight();
		viewStandardMarginBottom = information.getViewMarginBottom();
		viewStandarWeight = information.getWeight();

		setLayoutParams();
		viewCurrentMarginLeft = viewStandardMarginLeft * WIDTH_RATIO;
		viewCurrentMarginTop = viewStandardMarginTop * HEIGHT_RATIO;
		viewCurrentMarginRight = viewStandardMarginRight * WIDTH_RATIO;
		viewCurrentMarginBottom = viewStandardMarginBottom * HEIGHT_RATIO;
		viewCurrentWeight = viewStandarWeight;
		if (view.getParent() instanceof LinearLayout)
			parentLayoutType = LayoutInformation.L;
		if (view.getParent() instanceof RelativeLayout)
			parentLayoutType = LayoutInformation.R;
		setLayoutByParentLayoutType();
	}

	/**
	 * 进行通配
	 * 
	 * @param listdata
	 */
	public void setViewLayout(List<LayoutInformation> listdata) {

		for (int i = 0; i < listdata.size(); i++) {

			view = listdata.get(i).getView();
			viewStandardWidth = listdata.get(i).getViewWidth();
			viewStandardHeight = listdata.get(i).getViewHeight();
			viewStandardMarginLeft = listdata.get(i).getViewMarginLeft();
			viewStandardMarginTop = listdata.get(i).getViewMarginTop();
			viewStandardMarginRight = listdata.get(i).getViewMarginRight();
			viewStandardMarginBottom = listdata.get(i).getViewMarginBottom();
			viewStandarWeight = listdata.get(i).getWeight();

			setLayoutParams();
			viewCurrentMarginLeft = viewStandardMarginLeft * WIDTH_RATIO;
			viewCurrentMarginTop = viewStandardMarginTop * HEIGHT_RATIO;
			viewCurrentMarginRight = viewStandardMarginRight * WIDTH_RATIO;
			viewCurrentMarginBottom = viewStandardMarginBottom * HEIGHT_RATIO;
			viewCurrentWeight = viewStandarWeight;
			if (listdata.get(i).getView().getParent() instanceof LinearLayout)
				parentLayoutType = LayoutInformation.L;
			if (listdata.get(i).getView().getParent() instanceof RelativeLayout)
				parentLayoutType = LayoutInformation.R;
			setLayoutByParentLayoutType();
		}
	}

	/**
	 * 判断布局属性的值，设置布局的属性
	 */
	private void setLayoutParams() {
		// 如果基准的宽是wrap_content或者fill_parent则使用原值，否则进行计算得到通配后的值
		if (viewStandardWidth == LAYOUTPARAMS_WARP_CONTENT
				|| viewStandardWidth == LAYOUTPARAMS_FILL_PARENT) {
			viewCurrentWidth = viewStandardWidth;
		} else {
			viewCurrentWidth = viewStandardWidth * WIDTH_RATIO;
		}

		// 如果基准的宽是wrap_content或者fill_parent则使用原值，否则进行计算得到通配后的值
		if (viewStandardHeight == LAYOUTPARAMS_WARP_CONTENT
				|| viewStandardHeight == LAYOUTPARAMS_FILL_PARENT) {
			viewCurrentHeight = viewStandardHeight;
		} else {
			viewCurrentHeight = viewStandardHeight * HEIGHT_RATIO;
		}
	}

	/**
	 * 通过判断此View父类的布局类型,给此View设置布局
	 */
	private void setLayoutByParentLayoutType() {

		if (parentLayoutType == LAYOUT_TYPE_RELATiVELAYOUT) {
			RelativeLayout.LayoutParams params = null;
			if (view.getLayoutParams() == null)
				params = new RelativeLayout.LayoutParams(
						(int) viewCurrentWidth, (int) viewCurrentHeight);
			else
				params = (RelativeLayout.LayoutParams) view.getLayoutParams();
			params.width = (int) viewCurrentWidth;
			params.height = (int) viewCurrentHeight;

			params.setMargins((int) viewCurrentMarginLeft,
					(int) viewCurrentMarginTop, (int) viewCurrentMarginRight,
					(int) viewCurrentMarginBottom);

			view.setLayoutParams(params);

		} else if (parentLayoutType == LAYOUT_TYPE_LINEARLAYOUT) {

			LinearLayout.LayoutParams params = null;
			if (view.getLayoutParams() == null)
				params = new LinearLayout.LayoutParams(
						(int) viewCurrentWidth, (int) viewCurrentHeight);
			else
				params = (LinearLayout.LayoutParams) view.getLayoutParams();
			params.width = (int) viewCurrentWidth;
			params.height = (int) viewCurrentHeight;
			if (viewCurrentWeight != -1)
				params.weight = (float) viewCurrentWeight;
			params.setMargins((int) viewCurrentMarginLeft,
					(int) viewCurrentMarginTop, (int) viewCurrentMarginRight,
					(int) viewCurrentMarginBottom);
			view.setLayoutParams(params);
		}
	}

	/**
	 * 设置字体大小
	 * 
	 * @param standardSize
	 *            原始大小
	 * @return int
	 */
	public int setTextSize(int standardSize) {
		int currentSize;
		currentSize = (int) (standardSize * WIDTH_RATIO * DENSITY_RATIO);

		return currentSize;
	}

	public int CalcWidth(int width) {
		return (int) (width * WIDTH_RATIO);
	}

	public int CalcHeight(int height) {
		return (int) (WIDTH_RATIO * height);
	}

	public int CalcReverseWidth(int width) {
		return (int) ((float) width / WIDTH_RATIO);
	}

	public int CalcReverseHeight(int height) {
		return (int) ((float) height / HEIGHT_RATIO);
	}

	public float CalcWidth(float width) {
		return (float) (width * WIDTH_RATIO);
	}

	public float CalcHeight(float height) {
		return (float) (HEIGHT_RATIO * height);
	}

	public float CalcReverseWidth(float width) {
		return (float) (width / WIDTH_RATIO);
	}

	public float CalcReverseHeight(float height) {
		return (float) (height / HEIGHT_RATIO);
	}
}