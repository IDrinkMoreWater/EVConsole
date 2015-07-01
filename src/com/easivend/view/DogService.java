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
import java.util.Timer;
import java.util.TimerTask;

import com.easivend.app.maintain.MaintainActivity;
import com.easivend.common.ToolClass;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DogService extends Service {

	Timer timer;
	private final IBinder binder=new LocalBinder();
	private int allopen=1;//1��ʾһֱ��Ӧ��,0��ʾ�رպ󲻴�Ӧ��
	public class LocalBinder extends Binder
	{
		public DogService getService()
		{
			return DogService.this;
		}
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return binder;
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
		timer = new Timer(true);
		ToolClass.Log(ToolClass.INFO,"EV_DOG","dog create","dog.txt");
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		final int logno[]=new int[]{0};
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		ToolClass.Log(ToolClass.INFO,"EV_DOG","dog start","dog.txt");
		timer.schedule(new TimerTask() { 
	        @Override 
	        public void run() { 
	        	String str=null;	        	
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
	        	ToolClass.Log(ToolClass.INFO,"EV_DOG","logno="+logno[0],"dog.txt");
	        	if(logno[0]<300)
	        	{
	        		logno[0]++;
	        	}
	        	else 
	        	{
	        		logno[0]=0;
	        		ToolClass.optLogFile(); 
				}	        	
	        } 
	    }, 15*1000, 15*1000);       // timeTask 
	}
	

}
