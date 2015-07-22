package com.beiang.airdog.ui;

import java.util.Stack;

import android.app.Activity;
import android.util.Log;

public class ActivityManager {
	private static Stack<Activity> activityStack;
    private static ActivityManager instance;
    private ActivityManager() {
    	activityStack = new Stack<Activity>();
    }
    
    public static ActivityManager getScreenManager() {
        if (instance == null) {
            instance = new ActivityManager();            
        }
        return instance;
    }
    
    //退出栈顶Activity
    public void popActivity(Activity activity) {
        if (activity != null) {
           //在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }
    
    //获得当前栈顶Activity
    public Activity currentActivity() {
        Activity activity = null;
       if(!activityStack.empty())
         activity= activityStack.lastElement();
        return activity;
    }
    
    //将当前Activity推入栈中
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }
    
    //退出栈中所有Activity
    public void popAllActivity() {    	
        while (!activityStack.empty()) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
        Log.e("activity销毁", "activity销毁");
    }
}
