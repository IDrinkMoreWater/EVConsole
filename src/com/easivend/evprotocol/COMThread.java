/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           COMSerial.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        �봮���豸���ӵ��߳�
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.evprotocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easivend.common.ToolClass;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.http.EVServerhttp;
import com.google.gson.Gson;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class COMThread implements Runnable 
{
	//=====================��ݹ�����==============================================================================
	public static final int EV_BENTO_CHECKALLCHILD = 1;	//��ݹ�ȫ����ѯ
	public static final int EV_BENTO_CHECKALLMAIN	= 2;//��ݹ�ȫ����ѯ����
	public static final int EV_BENTO_CHECKCHILD = 3;	//��ݹ��ѯ
	public static final int EV_BENTO_OPENCHILD 	= 4;	//��ݹ���
	public static final int EV_BENTO_LIGHTCHILD = 5;	//��ݹ�����
	public static final int EV_BENTO_COOLCHILD 	= 6;	//��ݹ�����
	public static final int EV_BENTO_HOTCHILD 	= 7;	//��ݹ����
	
	public static final int EV_BENTO_CHECKMAIN	= 8;	//��ݹ��ѯ����
	public static final int EV_BENTO_OPTMAIN	= 9;	//��ݹ��������
	
	private Handler mainhand=null,childhand=null;
	int cabinet=0,column=0,opt=0;
	FileOutputStream bentOutputStream;  
    FileInputStream bentInputStream;      
	int devopt=0;//��������
	int retry=0;
	boolean comrecv=false;//true��ʾ�����Ѿ��������
	boolean issend=false;
    
	public COMThread(Handler mainhand) {
		this.mainhand=mainhand;		
	}
	public Handler obtainHandler()
	{
		return this.childhand;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();//�û��Լ�������࣬�����߳���Ҫ�Լ�׼��loop
		ToolClass.Log(ToolClass.INFO,"EV_COM","Thread start["+Thread.currentThread().getId()+"]","com.txt");
//		//�򿪸��ӹ񴮿�
//		if(ToolClass.getBentcom()!=null)
//		{
//			try {  
//				sp=new SerialPort(new File(ToolClass.getBentcom()),9600,0); 
//				bentInputStream=(FileInputStream) sp.getInputStream();
//				bentOutputStream=(FileOutputStream) sp.getOutputStream();
//	        } catch (SecurityException e) {  
//	            // TODO Auto-generated catch block  
//	            e.printStackTrace();  
//	        } catch (IOException e) {  
//	            // TODO Auto-generated catch block  
//	            e.printStackTrace();  
//	        }
//		}
				
		childhand=new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				devopt=msg.what;
				switch (msg.what)
				{
				case EV_BENTO_CHECKALLCHILD://���߳̽������̸߳��Ӳ�ѯ��Ϣ		
//					//1.�õ���Ϣ
//					JSONObject ev6=null;
//					try {
//						ev6 = new JSONObject(msg.obj.toString());
//						cabinet=ev6.getInt("cabinet");
//						ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSend0.2=cabinet="+cabinet,"com.txt");
//					} catch (JSONException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSend=CHECKALL","com.txt");
					//�����̷߳�����Ϣ
	  				Message tomain=mainhand.obtainMessage();
	  				tomain.what=EV_BENTO_CHECKALLMAIN;							
	  				tomain.obj="";
	  				mainhand.sendMessage(tomain); // ������Ϣ
					
					break;
				case EV_BENTO_CHECKCHILD://���߳̽������̸߳��Ӳ�ѯ��Ϣ		
					//1.�õ���Ϣ
					JSONObject ev=null;
					try {
						ev = new JSONObject(msg.obj.toString());
						cabinet=ev.getInt("cabinet");
						ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSend0.2=cabinet="+cabinet,"com.txt");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					break;
				case EV_BENTO_OPENCHILD://���߳̽������̸߳��ӿ���
					//1.�õ���Ϣ
					JSONObject ev2=null;
					try {
						ev2 = new JSONObject(msg.obj.toString());
						cabinet=ev2.getInt("cabinet");
						column=ev2.getInt("column");
						ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSend0.2=cabinet="+cabinet+"column="+column,"com.txt");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					break;
				case EV_BENTO_LIGHTCHILD://���߳̽������߳�����
					//1.�õ���Ϣ
					JSONObject ev3=null;
					try {
						ev3 = new JSONObject(msg.obj.toString());
						cabinet=ev3.getInt("cabinet");
						opt=ev3.getInt("opt");
						ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSend0.2=cabinet="+cabinet+"opt="+opt,"com.txt");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					break;
				case EV_BENTO_COOLCHILD://���߳̽������߳�����
					//1.�õ���Ϣ
					JSONObject ev4=null;
					try {
						ev4 = new JSONObject(msg.obj.toString());
						cabinet=ev4.getInt("cabinet");
						opt=ev4.getInt("opt");
						ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSend0.2=cabinet="+cabinet+"opt="+opt,"com.txt");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					break;	
				case EV_BENTO_HOTCHILD://���߳̽������̼߳���	
					//1.�õ���Ϣ
					JSONObject ev5=null;
					try {
						ev5 = new JSONObject(msg.obj.toString());
						cabinet=ev5.getInt("cabinet");
						opt=ev5.getInt("opt");
						ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSend0.2=cabinet="+cabinet+"opt="+opt,"com.txt");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					break;		
				default:
					break;
				}
			}
			
		};
		
		Looper.loop();//�û��Լ�������࣬�����߳���Ҫ�Լ�׼��loop
	}
	
	

}
