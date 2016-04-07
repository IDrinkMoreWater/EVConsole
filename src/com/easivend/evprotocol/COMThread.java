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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
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
	int devopt=0;//��������
	int retry=0;
	private static Map<String,Object> allSet = new LinkedHashMap<String,Object>() ;
	
	
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
		if(ToolClass.getBentcom().equals("")==false)
		{
			String bentcom = EVprotocol.EVPortRegister(ToolClass.getBentcom());
			ToolClass.Log(ToolClass.INFO,"EV_COM","Threadbentcom="+bentcom,"com.txt");
			ToolClass.setBentcom_id(Resetportid(bentcom));
		}
		
				
		childhand=new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				devopt=msg.what;
				switch (msg.what)
				{
				case EV_BENTO_CHECKALLCHILD://���߳̽������̸߳��Ӳ�ѯ��Ϣ		
//					//1.�õ���Ϣ
					JSONObject ev6=null;
					try {
						ev6 = new JSONObject(msg.obj.toString());
						cabinet=ev6.getInt("cabinet");
						ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSend0.2=bentid="+ToolClass.getBentcom_id()+" cabinet="+cabinet,"com.txt");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String rec6=EVprotocol.EVBentoCheck(ToolClass.getBentcom_id(), cabinet);
					ToolClass.Log(ToolClass.INFO,"EV_COM","API<<"+rec6.toString(),"log.txt");
					
					//2.�������
					try {
						JSONObject jsonObject6 = new JSONObject(rec6); 
						//����keyȡ������
						JSONObject ev_head6 = (JSONObject) jsonObject6.getJSONObject("EV_json");
						int str_evType6 =  ev_head6.getInt("EV_type");
						if(str_evType6==EVprotocol.EV_BENTO_CHECK)
						{
							if(ev_head6.getInt("is_success")>0)
							{
								//���ӿڻص���Ϣ
								allSet.clear();
								allSet.put("EV_TYPE", EVprotocol.EV_BENTO_CHECK);
								allSet.put("cool", ev_head6.getInt("cool"));
								allSet.put("hot", ev_head6.getInt("hot"));
								allSet.put("light", ev_head6.getInt("light"));
								JSONArray arr6=ev_head6.getJSONArray("column");//����json����
								//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����2:"+arr.toString());
								for(int i=0;i<arr6.length();i++)
								{
									JSONObject object2=arr6.getJSONObject(i);
									allSet.put(String.valueOf(object2.getInt("no")), object2.getInt("state"));								
								}
								//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����3:"+allSet.toString());								
							}
							else
							{
								//���ӿڻص���Ϣ
								allSet.clear();
								allSet.put("EV_TYPE", EVprotocol.EV_BENTO_CHECK);
								allSet.put("cool", 0);
								allSet.put("hot", 0);
								allSet.put("light", 0);
//								JSONArray arr=ev_head.getJSONArray("column");//����json����
//								//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����2:"+arr.toString());
//								for(int i=0;i<arr.length();i++)
//								{
//									JSONObject object2=arr.getJSONObject(i);
//									allSet.put(String.valueOf(object2.getInt("no")), object2.getInt("state"));								
//								}
								//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����3:"+allSet.toString());								
							}
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//3.�����̷߳�����Ϣ
	  				Message tomain6=mainhand.obtainMessage();
	  				tomain6.what=EV_BENTO_CHECKALLMAIN;							
	  				tomain6.obj=allSet;
	  				mainhand.sendMessage(tomain6); // ������Ϣ
					break;
				case EV_BENTO_CHECKCHILD://���߳̽������̸߳��Ӳ�ѯ��Ϣ		
					//1.�õ���Ϣ
					JSONObject ev=null;
					try {
						ev = new JSONObject(msg.obj.toString());
						cabinet=ev.getInt("cabinet");
						ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSend0.2=bentid="+ToolClass.getBentcom_id()+" cabinet="+cabinet,"com.txt");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String rec=EVprotocol.EVBentoCheck(ToolClass.getBentcom_id(), cabinet);
					ToolClass.Log(ToolClass.INFO,"EV_COM","API<<"+rec.toString(),"log.txt");
					
					//2.�������
					try {
						JSONObject jsonObject = new JSONObject(rec); 
						//����keyȡ������
						JSONObject ev_head = (JSONObject) jsonObject.getJSONObject("EV_json");
						int str_evType =  ev_head.getInt("EV_type");
						if(str_evType==EVprotocol.EV_BENTO_CHECK)
						{
							if(ev_head.getInt("is_success")>0)
							{
								//���ӿڻص���Ϣ
								allSet.clear();
								allSet.put("EV_TYPE", EVprotocol.EV_BENTO_CHECK);
								allSet.put("cool", ev_head.getInt("cool"));
								allSet.put("hot", ev_head.getInt("hot"));
								allSet.put("light", ev_head.getInt("light"));
								JSONArray arr=ev_head.getJSONArray("column");//����json����
								//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����2:"+arr.toString());
								for(int i=0;i<arr.length();i++)
								{
									JSONObject object2=arr.getJSONObject(i);
									allSet.put(String.valueOf(object2.getInt("no")), object2.getInt("state"));								
								}
								//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����3:"+allSet.toString());								
							}
							else
							{
								//���ӿڻص���Ϣ
								allSet.clear();
								allSet.put("EV_TYPE", EVprotocol.EV_BENTO_CHECK);
								allSet.put("cool", 0);
								allSet.put("hot", 0);
								allSet.put("light", 0);
//								JSONArray arr=ev_head.getJSONArray("column");//����json����
//								//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����2:"+arr.toString());
//								for(int i=0;i<arr.length();i++)
//								{
//									JSONObject object2=arr.getJSONObject(i);
//									allSet.put(String.valueOf(object2.getInt("no")), object2.getInt("state"));								
//								}
								//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����3:"+allSet.toString());								
							}
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//3.�����̷߳�����Ϣ
	  				Message tomain=mainhand.obtainMessage();
	  				tomain.what=EV_BENTO_CHECKMAIN;							
	  				tomain.obj=allSet;
	  				mainhand.sendMessage(tomain); // ������Ϣ
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
					String rec2=EVprotocol.EVBentoOpen(ToolClass.getBentcom_id(), cabinet,column);
					ToolClass.Log(ToolClass.INFO,"EV_COM","API<<"+rec2.toString(),"log.txt");

					//2.�������
					try {
						JSONObject jsonObject2 = new JSONObject(rec2); 
						//����keyȡ������
						JSONObject ev_head2 = (JSONObject) jsonObject2.getJSONObject("EV_json");
						int str_evType2 =  ev_head2.getInt("EV_type");
						if(str_evType2==EVprotocol.EV_BENTO_OPEN)
						{
							if(ev_head2.getInt("is_success")>0)
					    	{
								//���ӿڻص���Ϣ
								allSet.clear();
								allSet.put("EV_TYPE", EVprotocol.EV_BENTO_OPEN);
								allSet.put("addr", ev_head2.getInt("addr"));//���ӵ�ַ
								allSet.put("box", ev_head2.getInt("box"));//���ӵ�ַ
								allSet.put("result", ev_head2.getInt("result"));								
					    	}
					    	else
					    	{
								//���ӿڻص���Ϣ
								allSet.clear();
								allSet.put("EV_TYPE", EVprotocol.EV_BENTO_OPEN);
								allSet.put("addr", 0);//���ӵ�ַ
								allSet.put("box", 0);//���ӵ�ַ
								allSet.put("result", 0);								
					    	}
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//3.�����̷߳�����Ϣ
	  				Message tomain2=mainhand.obtainMessage();
	  				tomain2.what=EV_BENTO_OPTMAIN;							
	  				tomain2.obj=allSet;
	  				mainhand.sendMessage(tomain2); // ������Ϣ
					
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
					String rec3=EVprotocol.EVBentoLight(ToolClass.getBentcom_id(), cabinet,opt);
					ToolClass.Log(ToolClass.INFO,"EV_COM","API<<"+rec3.toString(),"log.txt");

					//2.�������
					try {
						JSONObject jsonObject3 = new JSONObject(rec3); 
						//����keyȡ������
						JSONObject ev_head3 = (JSONObject) jsonObject3.getJSONObject("EV_json");
						int str_evType3 =  ev_head3.getInt("EV_type");
						if(str_evType3==EVprotocol.EV_BENTO_LIGHT)
						{
							if(ev_head3.getInt("is_success")>0)
					    	{
								//���ӿڻص���Ϣ
								allSet.clear();
								allSet.put("EV_TYPE", EVprotocol.EV_BENTO_LIGHT);
								allSet.put("addr", ev_head3.getInt("addr"));//���ӵ�ַ
								allSet.put("opt", ev_head3.getInt("opt"));//�����ǹ�
								allSet.put("result", ev_head3.getInt("result"));								
					    	}
					    	else 
					    	{
								//���ӿڻص���Ϣ
								allSet.clear();
								allSet.put("EV_TYPE", EVprotocol.EV_BENTO_LIGHT);
								allSet.put("addr", 0);//���ӵ�ַ
								allSet.put("opt", 0);//�����ǹ�
								allSet.put("result", 0);								
					    	}
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//3.�����̷߳�����Ϣ
	  				Message tomain3=mainhand.obtainMessage();
	  				tomain3.what=EV_BENTO_OPTMAIN;							
	  				tomain3.obj=allSet;
	  				mainhand.sendMessage(tomain3); // ������Ϣ
					
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
					String rec4=EVprotocol.EVBentoLight(ToolClass.getBentcom_id(), cabinet,opt);
					ToolClass.Log(ToolClass.INFO,"EV_COM","API<<"+rec4.toString(),"log.txt");

					//2.�������
					try {
						JSONObject jsonObject4 = new JSONObject(rec4); 
						//����keyȡ������
						JSONObject ev_head4 = (JSONObject) jsonObject4.getJSONObject("EV_json");
						int str_evType4 =  ev_head4.getInt("EV_type");
						if(str_evType4==EVprotocol.EV_BENTO_LIGHT)
						{
							if(ev_head4.getInt("is_success")>0)
					    	{
								//���ӿڻص���Ϣ
								allSet.clear();
								allSet.put("EV_TYPE", EVprotocol.EV_BENTO_LIGHT);
								allSet.put("addr", ev_head4.getInt("addr"));//���ӵ�ַ
								allSet.put("opt", ev_head4.getInt("opt"));//�����ǹ�
								allSet.put("result", ev_head4.getInt("result"));								
					    	}
					    	else 
					    	{
								//���ӿڻص���Ϣ
								allSet.clear();
								allSet.put("EV_TYPE", EVprotocol.EV_BENTO_LIGHT);
								allSet.put("addr", 0);//���ӵ�ַ
								allSet.put("opt", 0);//�����ǹ�
								allSet.put("result", 0);								
					    	}
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//3.�����̷߳�����Ϣ
	  				Message tomain4=mainhand.obtainMessage();
	  				tomain4.what=EV_BENTO_OPTMAIN;							
	  				tomain4.obj=allSet;
	  				mainhand.sendMessage(tomain4); // ������Ϣ
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
					String rec5=EVprotocol.EVBentoLight(ToolClass.getBentcom_id(), cabinet,opt);
					ToolClass.Log(ToolClass.INFO,"EV_COM","API<<"+rec5.toString(),"log.txt");

					//2.�������
					try {
						JSONObject jsonObject5 = new JSONObject(rec5); 
						//����keyȡ������
						JSONObject ev_head5 = (JSONObject) jsonObject5.getJSONObject("EV_json");
						int str_evType5 =  ev_head5.getInt("EV_type");
						if(str_evType5==EVprotocol.EV_BENTO_LIGHT)
						{
							if(ev_head5.getInt("is_success")>0)
					    	{
								//���ӿڻص���Ϣ
								allSet.clear();
								allSet.put("EV_TYPE", EVprotocol.EV_BENTO_LIGHT);
								allSet.put("addr", ev_head5.getInt("addr"));//���ӵ�ַ
								allSet.put("opt", ev_head5.getInt("opt"));//�����ǹ�
								allSet.put("result", ev_head5.getInt("result"));								
					    	}
					    	else 
					    	{
								//���ӿڻص���Ϣ
								allSet.clear();
								allSet.put("EV_TYPE", EVprotocol.EV_BENTO_LIGHT);
								allSet.put("addr", 0);//���ӵ�ַ
								allSet.put("opt", 0);//�����ǹ�
								allSet.put("result", 0);								
					    	}
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//3.�����̷߳�����Ϣ
	  				Message tomain5=mainhand.obtainMessage();
	  				tomain5.what=EV_BENTO_OPTMAIN;							
	  				tomain5.obj=allSet;
	  				mainhand.sendMessage(tomain5); // ������Ϣ
					break;		
				default:
					break;
				}
			}
			
		};
		
		Looper.loop();//�û��Լ�������࣬�����߳���Ҫ�Լ�׼��loop
	}
	
	//��ȡ��portid
	private int Resetportid(String bentcom)
	{
		int bentcom_id=0;
		//2.�������
		try {
			JSONObject jsonObject = new JSONObject(bentcom); 
			//����keyȡ������
			JSONObject ev_head = (JSONObject) jsonObject.getJSONObject("EV_json");
			int str_evType =  ev_head.getInt("EV_type");
			if(str_evType==EVprotocol.EV_REGISTER)
			{
				bentcom_id=ev_head.getInt("port_id");				
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return bentcom_id;
	}

}
