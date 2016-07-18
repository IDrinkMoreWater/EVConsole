/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           DogService.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        Dog������������жϵ�Ӧ�ùر�ʱ���͸����Զ�����        
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.view;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.easivend.app.maintain.MaintainActivity;
import com.easivend.common.ToolClass;
import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;

public class DogService extends Service {

	ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
	private int allopen=1;//1��ʾһֱ��Ӧ��,0��ʾ�رպ󲻴�Ӧ��
	private int logno=0;//��ʾ����
	ActivityReceiver receiver;
	//LocalBroadcastManager localBroadreceiver;
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","dog bind","dog.txt");
		return null;
	}
	//8.����activity�Ľ������㲥��������������
	public class ActivityReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent)
		{
			// TODO Auto-generated method stub
			Bundle bundle=intent.getExtras();
			setAllopen(bundle.getInt("isallopen"));	
		}

	}
			
	public int getAllopen() {
		return allopen;
	}
	public void setAllopen(int allopen) {
		this.allopen = allopen;
		ToolClass.Log(ToolClass.INFO,"EV_DOG","setAllopen="+allopen,"dog.txt");
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ToolClass.Log(ToolClass.INFO,"EV_DOG","dog create","dog.txt");
		//9.ע�������
		//localBroadreceiver = LocalBroadcastManager.getInstance(this);
		receiver=new ActivityReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("android.intent.action.dogserversend");
		//localBroadreceiver.registerReceiver(receiver,filter);	
		registerReceiver(receiver,filter);	
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub		
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","dog destroy","dog.txt");
		//���ע�������
		//localBroadreceiver.unregisterReceiver(receiver);
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		ToolClass.Log(ToolClass.INFO,"EV_DOG","dog start","dog.txt");
		timer.scheduleWithFixedDelay(new Runnable() { 
	        @Override 
	        public void run() { 	        	
	        	if(allopen==1)
        		{
	        		//�ж�Ӧ���Ƿ������� 
	        		ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
	        		List<RunningTaskInfo> list = activityManager.getRunningTasks(100);
	        		String MY_PKG_NAME = "com.example.evconsole";
	        		int isopen=0;//1�����������У�0����û������
	        		for (RunningTaskInfo info : list) 
	        		{	        			 
	        			 ToolClass.Log(ToolClass.INFO,"EV_DOG","appName:"+info.topActivity.getClassName()+"-->pack:"+info.topActivity.getPackageName(),"dog.txt");
	        			 if (info.topActivity.getPackageName().equals(MY_PKG_NAME)||info.baseActivity.getPackageName().equals(MY_PKG_NAME)) 
	        			 {	        				  
	        				  isopen=1;
	        				  break;
        				 }
	        		}
	        		if(isopen==1)
	        		{
	        			ToolClass.Log(ToolClass.INFO,"EV_DOG","applicationrun","dog.txt");
	        		}
	        		else
	        		{
	        			ToolClass.Log(ToolClass.INFO,"EV_DOG","applicationstop","dog.txt");
		        		//1.����Ӧ�ó���
		        		Intent intent=new Intent(DogService.this,MaintainActivity.class);
		        		intent.setAction(Intent.ACTION_MAIN);  
		        		intent.addCategory(Intent.CATEGORY_LAUNCHER);  
		        		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		        		intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);  
		        		DogService.this.startActivity(intent);	
					}
	        		
        		}
	        	else
	        	{
	        		ToolClass.Log(ToolClass.INFO,"EV_DOG","unopen","dog.txt");
				}
	        	
	        	//������־�ļ���
	        	ToolClass.Log(ToolClass.INFO,"EV_DOG","logno["+Thread.currentThread().getId()+"]="+logno,"dog.txt");
	        	if(logno<5760)//5760
	        	{
	        		logno++;
	        	}
	        	else 
	        	{
	        		logno=0;
	        		ToolClass.optLogFile(); 
				}	        	
	        } 
	    },15,15,TimeUnit.SECONDS);       // timeTask 
	}
	

}
