package com.easivend.view;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.common.ToolClass;
import com.easivend.http.EVServerhttp;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class EVServerService extends Service {
	private final int SPLASH_DISPLAY_LENGHT = 3000; // �ӳ�3��
	Timer timer; 
	private Thread thread=null;
    private Handler mainhand=null,childhand=null;   
    EVServerhttp serverhttp=null;
    boolean isev=false;
    ActivityReceiver receiver;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service bind","server.txt");
		return null;
	}
	//8.����activity�Ľ������㲥��������������
	public class ActivityReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent)
		{
			// TODO Auto-generated method stub
			Bundle bundle=intent.getExtras();
			int EVWhat=bundle.getInt("EVWhat");
			switch(EVWhat)
			{
			case EVServerhttp.SETCHILD:
				String vmc_no=bundle.getString("vmc_no");
				String vmc_auth_code=bundle.getString("vmc_auth_code");
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","receiver:vmc_no="+vmc_no+"vmc_auth_code="+vmc_auth_code,"server.txt");
				//������յ�������,����ǩ��������߳���
				//��ʼ��һ:����ǩ��ָ��
	        	childhand=serverhttp.obtainHandler();
	    		Message childmsg=childhand.obtainMessage();
	    		childmsg.what=EVServerhttp.SETCHILD;
	    		JSONObject ev=null;
	    		try {
	    			ev=new JSONObject();
	    			ev.put("vmc_no", vmc_no);
	    			ev.put("vmc_auth_code", vmc_auth_code);
	    			ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send0.1="+ev.toString(),"server.txt");
	    		} catch (JSONException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		childmsg.obj=ev;
	    		childhand.sendMessage(childmsg);
	    		break;
			}			
		}

	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service create","server.txt");
		super.onCreate();
		timer = new Timer();
		//9.ע�������
		receiver=new ActivityReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("android.intent.action.vmserversend");
		this.registerReceiver(receiver,filter);						
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub		
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service destroy","server.txt");
		isev=false;//��ʹservice�����߳�Ҳ����ֹͣ����������ͨ������isev��ֹͣ�߳�
		//���ע�������
		this.unregisterReceiver(receiver);
		super.onDestroy();
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service start","server.txt");
		//***********************
		//�߳̽���vmserver����
		//***********************
		mainhand=new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				Intent intent;
				// TODO Auto-generated method stub				
				switch (msg.what)
				{
					case EVServerhttp.SETMAIN://���߳̽������߳���Ϣǩ�����
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ǩ���ɹ�","server.txt");
						isev=true;
						//���ظ�activity�㲥
						intent=new Intent();
						intent.putExtra("EVWhat", EVServerhttp.SETMAIN);
						intent.setAction("android.intent.action.vmserverrec");//action���������ͬ
						sendBroadcast(intent);
						//��ʼ����:��ȡ��Ʒ������Ϣ
						childhand=serverhttp.obtainHandler();
		        		Message childmsg=childhand.obtainMessage();
		        		childmsg.what=EVServerhttp.SETCLASSCHILD;
		        		childhand.sendMessage(childmsg);
						break;
					case EVServerhttp.SETFAILMAIN://���߳̽������߳���Ϣǩ��ʧ��
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ʧ�ܣ��������","server.txt");
						//���ظ�activity�㲥
						intent=new Intent();
						intent.putExtra("EVWhat", EVServerhttp.SETFAILMAIN);
						intent.setAction("android.intent.action.vmserverrec");//action���������ͬ
						sendBroadcast(intent);	
						break;
					case EVServerhttp.SETERRFAILMAIN://���߳̽������߳���Ϣǩ��ʧ��
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ǩ��ʧ�ܣ�ԭ��="+msg.obj.toString(),"server.txt");
						//���ظ�activity�㲥
						intent=new Intent();
						intent.putExtra("EVWhat", EVServerhttp.SETFAILMAIN);
						intent.setAction("android.intent.action.vmserverrec");//action���������ͬ
						sendBroadcast(intent);	
						break;	
				}				
			}
			
		};
		//�����û��Լ�������࣬�����߳�
  		serverhttp=new EVServerhttp(mainhand);
  		thread=new Thread(serverhttp,"serverhttp Thread");
  		thread.start();
		
	    //ÿ��һ��ʱ�䣬����ͬ��һ��
	    timer.schedule(new TimerTask() { 
	        @Override 
	        public void run() { 
	        	if(isev)
	        	{
		        	//��������������߳���
	            	childhand=serverhttp.obtainHandler();
	        		Message childmsg=childhand.obtainMessage();
	        		childmsg.what=EVServerhttp.SETHEARTCHILD;
	        		childhand.sendMessage(childmsg);	
	        	}
	        } 
	    }, 15*1000, 3*60*1000);       // timeTask 

	}	

}
