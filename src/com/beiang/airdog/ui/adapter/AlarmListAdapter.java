package com.beiang.airdog.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiang.airdog.constant.Constants.AlarmType;
import com.beiang.airdog.ui.activity.AirdogGetUpAlarmActivity;
import com.beiang.airdog.ui.activity.AirdogNoticeAlarmActivity;
import com.beiang.airdog.ui.model.AirdogAlarm.Alarm;
import com.broadlink.beiangair.R;

/***
 * 所有闹钟适配器
 * 
 * @author lsd
 * 
 */
public class AlarmListAdapter extends BaseAdapter {
	Context context;
	List<Alarm> list;

	public AlarmListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setData(List<Alarm> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return list == null ? 0 : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Tag tag = null;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(context).inflate(R.layout.layout_alarm_list_item, null);
			tag.content_layout = (RelativeLayout) convertView.findViewById(R.id.content_layout);
			tag.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			tag.tv_time_section = (TextView) convertView.findViewById(R.id.tv_time_section);
			tag.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			tag.tv_repeat = (TextView) convertView.findViewById(R.id.tv_repeat);
			tag.ll_details = (LinearLayout) convertView.findViewById(R.id.ll_details);
			tag.tv_dt_date = (TextView) convertView.findViewById(R.id.tv_dt_date);
			tag.tv_dt_content = (TextView) convertView.findViewById(R.id.tv_dt_content);
			tag.iv_check = (ImageView) convertView.findViewById(R.id.iv_check);
			tag.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		final Alarm ety = (Alarm) getItem(position);
		/** 点击事件 */
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ety != null) {
					if (AlarmType.GETUP.getValue() == ety.type) {
						((Activity) context).startActivityForResult(
								new Intent(context, AirdogGetUpAlarmActivity.class).putExtra("alarm", ety), 100);
					} else {
						((Activity) context).startActivityForResult(
								new Intent(context, AirdogNoticeAlarmActivity.class).putExtra("alarm", ety), 100);

					}
				}
			}
		});
		tag.iv_check.setSelected(true);

		/** 设置内容 */
		if (ety != null) {
			tag.tv_time.setText(ety.hour + "  ： " + ety.minute);
			if (ety.hour > 12) {
				tag.tv_time_section.setText("下午");
			} else {
				tag.tv_time_section.setText("上午");
			}

			int type = ety.type ;
			if (AlarmType.GETUP.getValue() == type) {
				tag.tv_type.setText("起床");
			} else if (AlarmType.REST.getValue() == type) {
				tag.tv_type.setText("休息");
			} else if (AlarmType.SLEEP.getValue() == type) {
				tag.tv_type.setText("睡觉");
			}else if (AlarmType.MEETING.getValue() == type) {
				tag.tv_type.setText("开会");
			}else if (AlarmType.DEFINE.getValue() == type) {
				tag.tv_type.setText("自定义");
			}else if (AlarmType.ORTHER.getValue() == type) {
				tag.tv_type.setText("其他");
			}else if (AlarmType.EVENT.getValue() == type) {
				tag.tv_type.setText("事件");
			}
			tag.tv_type.setSelected(true);

			tag.tv_repeat.setText("".equals(getWeekStatus(ety.week)) ? "无" : getWeekStatus(ety.week));
			tag.tv_dt_date.setText(ety.year + "/" + ety.month + "/" + ety.day);
			tag.tv_dt_content.setText("无");

			if (AlarmType.GETUP.getValue() == type) {
				tag.iv_check.setVisibility(View.INVISIBLE);
				tag.iv_delete.setVisibility(View.GONE);

				tag.tv_time_section.setVisibility(View.VISIBLE);
				tag.tv_type.setVisibility(View.GONE);

				tag.tv_repeat.setVisibility(View.VISIBLE);
				tag.ll_details.setVisibility(View.GONE);
			} else {
				tag.iv_check.setVisibility(View.INVISIBLE);
				tag.iv_delete.setVisibility(View.GONE);

				tag.tv_time_section.setVisibility(View.GONE);
				tag.tv_type.setVisibility(View.VISIBLE);

				tag.tv_repeat.setVisibility(View.GONE);
				tag.ll_details.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	class Tag {
		RelativeLayout content_layout;
		TextView tv_time;
		TextView tv_time_section;
		TextView tv_type;
		TextView tv_repeat;
		LinearLayout ll_details;
		TextView tv_dt_date;
		TextView tv_dt_content;
		ImageView iv_check;
		ImageView iv_delete;
	}

	private String getWeekStatus(int weekSum) {
		String weekString = "";
		if (weekSum >= 64) {
			weekSum = weekSum - 64;
			weekString = "周日" + weekString;
		}
		if (weekSum >= 32) {
			weekSum = weekSum - 32;
			weekString = "周六  " + weekString;
		}
		if (weekSum >= 16) {
			weekSum = weekSum - 16;
			weekString = "周五  " + weekString;
		}
		if (weekSum >= 8) {
			weekSum = weekSum - 8;
			weekString = "周四  " + weekString;
		}
		if (weekSum >= 4) {
			weekSum = weekSum - 4;
			weekString = "周三  " + weekString;
		}
		if (weekSum >= 2) {
			weekSum = weekSum - 2;
			weekString = "周二  " + weekString;
		}
		if (weekSum == 1) {
			weekString = "周一  " + weekString;
		}
		return weekString;
	}
}
