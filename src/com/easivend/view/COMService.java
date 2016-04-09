/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           COMService.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        �봮���豸���ӵķ���      
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.app.maintain.MaintainActivity;
import com.easivend.common.SerializableMap;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_cabinetDAO;
import com.easivend.evprotocol.COMThread;
import com.easivend.evprotocol.EVprotocol;
import com.easivend.http.EVServerhttp;
import com.easivend.model.Tb_vmc_cabinet;
import com.easivend.view.EVServerService.ActivityReceiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

public class COMService extends Service {
	//=====================����==================================
	public static final int EV_CHECKALLCHILD= 1;	//��ѯȫ������״̬
	public static final int EV_CHECKALLMAIN	= 2;	//��ѯȫ������״̬����
	public static final int EV_CHECKCHILD	= 3;	//������ѯ	
	public static final int EV_CHUHUOCHILD	= 4;	//��������	
	public static final int EV_LIGHTCHILD	= 5;	//����	
	public static final int EV_COOLCHILD	= 6;	//����	
	public static final int EV_HOTCHILD		= 7;	//����
	
	public static final int EV_CHECKMAIN	= 8;	//������ѯ	����
	public static final int EV_OPTMAIN  	= 9;	//������������
	public static final int EV_SETHUOCHILD	= 10;	//��������
	
	//=====================�ֽ��豸==================================
	public static final int EV_MDB_ENABLE 	= 22;	//MDB�豸ʹ��
	public static final int EV_MDB_HEART 	= 23;	//MDB�豸����
	public static final int EV_MDB_B_INFO 	= 24;	//MDBֽ������Ϣ
	public static final int EV_MDB_C_INFO 	= 25;	//MDBӲ������Ϣ
	public static final int EV_MDB_COST 	= 26;	//MDB�豸�ۿ�
	public static final int EV_MDB_PAYBACK = 27;	//MDB�豸�˱�
	public static final int EV_MDB_PAYOUT 	= 28;	//MDB�豸�ұ�
	public static final int EV_MDB_B_CON 	= 29;	//MDBֽ��������
	public static final int EV_MDB_C_CON 	= 30;	//MDBӲ��������
	public static final int EV_MDB_HP_PAYOUT = 31;	//hopperӲ��������
	
	ActivityReceiver receiver;
	LocalBroadcastManager localBroadreceiver;
	private ExecutorService thread=null;
    private Handler mainhand=null,childhand=null; 
    COMThread comserial=null;
    Map<String,Integer> huoSet=new LinkedHashMap<String,Integer>();
    private String[] cabinetID = null;//���������������    
    private int huom = 0;// ����һ����ʼ��ʶ
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
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
			//��ѯȫ������״̬
			case EV_CHECKALLCHILD:
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ������ѯȫ��","com.txt");
				vmc_cabinetDAO cabinetDAO = new vmc_cabinetDAO(context);// ����InaccountDAO����
			    // 1.��ȡ���й��
			    List<Tb_vmc_cabinet> listinfos = cabinetDAO.getScrollData();
			    cabinetID = new String[listinfos.size()];// �����ַ�������ĳ���
			    // ����List���ͼ���
			    for (Tb_vmc_cabinet tb_inaccount : listinfos) 
			    {
			        cabinetID[huom] = tb_inaccount.getCabID();
			        ToolClass.Log(ToolClass.INFO,"EV_COM","��ȡ���="+cabinetID[huom],"com.txt");
				    huom++;// ��ʶ��1
			    }
			    huom=0;
			    if(listinfos.size()==0)
			    {
			    	//���ظ�activity�㲥
					Intent recintent=new Intent();
					recintent.putExtra("EVWhat", COMService.EV_CHECKALLMAIN);						
					//��������
			        SerializableMap myMap=new SerializableMap();
			        myMap.setMap(huoSet);//��map������ӵ���װ��myMap<span></span>��
			        Bundle bundlerec=new Bundle();
			        bundlerec.putSerializable("result", myMap);
			        recintent.putExtras(bundlerec);
			        recintent.setAction("android.intent.action.comrec");//action���������ͬ
			        localBroadreceiver.sendBroadcast(recintent);
			    }
			    else
				{
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    	childhand=comserial.obtainHandler();
	        		Message childrec=childhand.obtainMessage();
	        		//���һ�������
	        		vmc_cabinetDAO cabinetDAOrec = new vmc_cabinetDAO(context);// ����InaccountDAO����
	        		// ��ȡ����������Ϣ�����洢��List���ͼ�����
	        	    Tb_vmc_cabinet listinfosrec = cabinetDAOrec.findScrollData(cabinetID[huom]);
	        		//���ӹ�
	        	    if(listinfosrec.getCabType()==5)
	        		{
	        			ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ���ӹ��ѯ="+cabinetID[huom],"com.txt");
	        			childrec.what=COMThread.EV_BENTO_CHECKALLCHILD;
	        		}
	        		else
	        		{
	        			ToolClass.Log(ToolClass.INFO,"EV_COM","COMService �������ѯ="+cabinetID[huom],"com.txt");
	        			childrec.what=COMThread.EV_COLUMN_CHECKALLCHILD;
	        		}
	        		JSONObject evrec=null;
		    		try {
		    			evrec=new JSONObject();
		    			evrec.put("cabinet", cabinetID[huom]);	    			  			
		    			ToolClass.Log(ToolClass.INFO,"EV_COM","ServiceSend0.1="+evrec.toString(),"com.txt");
		    		} catch (JSONException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}
		    		childrec.obj=evrec;
	        		childhand.sendMessage(childrec);	        	
				}
				break;    
			//������ѯ	
			case EV_CHECKCHILD:
				
				childhand=comserial.obtainHandler();
        		Message child2=childhand.obtainMessage();
        		//���һ�������
        		vmc_cabinetDAO cabinetDAO2 = new vmc_cabinetDAO(context);// ����InaccountDAO����
        	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
        	    Tb_vmc_cabinet listinfos2 = cabinetDAO2.findScrollData(String.valueOf(bundle.getInt("cabinet")));
        		//���ӹ�
        	    if(listinfos2.getCabType()==5)
        		{
        			ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ���ӹ��ѯ="+String.valueOf(bundle.getInt("cabinet")),"com.txt");
        			child2.what=COMThread.EV_BENTO_CHECKCHILD;
        		}
        		else
        		{
        			ToolClass.Log(ToolClass.INFO,"EV_COM","COMService �������ѯ="+String.valueOf(bundle.getInt("cabinet")),"com.txt");
        			child2.what=COMThread.EV_COLUMN_CHECKCHILD;
        		}
        		JSONObject ev2=null;
	    		try {
	    			ev2=new JSONObject();
	    			ev2.put("cabinet", bundle.getInt("cabinet"));	    			  			
	    			ToolClass.Log(ToolClass.INFO,"EV_COM","ServiceSend0.1="+ev2.toString(),"com.txt");
	    		} catch (JSONException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		child2.obj=ev2;
        		childhand.sendMessage(child2);
				break;
			//��������	
			case EV_CHUHUOCHILD:		
				Message child3=childhand.obtainMessage();
				//���һ�������
        		vmc_cabinetDAO cabinetDAO3 = new vmc_cabinetDAO(context);// ����InaccountDAO����
        	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
        	    Tb_vmc_cabinet listinfos3 = cabinetDAO3.findScrollData(String.valueOf(bundle.getInt("cabinet")));
        		
        		JSONObject ev3=null;
	    		try {
	    			ev3=new JSONObject();
	    			ev3.put("cabinet", bundle.getInt("cabinet"));	
	    			//���ӹ�
	        	    if(listinfos3.getCabType()==5)
	        		{
	        	    	ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ���ӳ���","com.txt");
	    				child3.what=COMThread.EV_BENTO_OPENCHILD;
	    				ev3.put("column", bundle.getInt("column"));
	        		}
	        		else
	        		{
	        	    	ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ��������","com.txt");
	    				child3.what=COMThread.EV_COLUMN_OPENCHILD;
	    				ev3.put("column", ToolClass.columnChuhuo(bundle.getInt("column")));
	        		}	    			
	    			ToolClass.Log(ToolClass.INFO,"EV_COM","ServiceSend0.1="+ev3.toString(),"com.txt");
	    		} catch (JSONException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		child3.obj=ev3;
        		childhand.sendMessage(child3);	
				break;
			//��������
			case EV_SETHUOCHILD:		
				Message child7=childhand.obtainMessage();
				//���һ�������
        		vmc_cabinetDAO cabinetDAO7 = new vmc_cabinetDAO(context);// ����InaccountDAO����
        	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
        	    Tb_vmc_cabinet listinfos7 = cabinetDAO7.findScrollData(String.valueOf(bundle.getInt("cabinet")));
        		
        		JSONObject ev7=null;
	    		try {
	    			ev7=new JSONObject();
	    			ev7.put("cabinet", bundle.getInt("cabinet"));	
	    			//���ӹ�
	        	    if(listinfos7.getCabType()==5)
	        		{
	        	    	ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ���ӳ���","com.txt");
	    				child7.what=COMThread.EV_BENTO_OPENCHILD;
	    				ev7.put("column", bundle.getInt("column"));
	        		}
	        		else
	        		{
	        	    	ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ��������","com.txt");
	    				child7.what=COMThread.EV_COLUMN_OPENCHILD;
	    				ev7.put("column", bundle.getInt("column"));
	        		}	    			
	    			ToolClass.Log(ToolClass.INFO,"EV_COM","ServiceSend0.1="+ev7.toString(),"com.txt");
	    		} catch (JSONException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		child7.obj=ev7;
        		childhand.sendMessage(child7);	
				break;	
			//��ݹ�����	
			case EV_LIGHTCHILD:
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ����","com.txt");
				Message child4=childhand.obtainMessage();
				child4.what=COMThread.EV_BENTO_LIGHTCHILD;
        		JSONObject ev4=null;
	    		try {
	    			ev4=new JSONObject();
	    			ev4.put("cabinet", bundle.getInt("cabinet"));	
	    			ev4.put("opt", bundle.getInt("opt"));
	    			ToolClass.Log(ToolClass.INFO,"EV_COM","ServiceSend0.1="+ev4.toString(),"com.txt");
	    		} catch (JSONException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		child4.obj=ev4;
        		childhand.sendMessage(child4);	
				break;
			//��ݹ�����	
			case EV_COOLCHILD:
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ����","com.txt");
				Message child5=childhand.obtainMessage();
				child5.what=COMThread.EV_BENTO_COOLCHILD;
        		JSONObject ev5=null;
	    		try {
	    			ev5=new JSONObject();
	    			ev5.put("cabinet", bundle.getInt("cabinet"));	
	    			ev5.put("opt", bundle.getInt("opt"));
	    			ToolClass.Log(ToolClass.INFO,"EV_COM","ServiceSend0.1="+ev5.toString(),"com.txt");
	    		} catch (JSONException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		child5.obj=ev5;
        		childhand.sendMessage(child5);	
				break;
			//��ݹ����	
			case EV_HOTCHILD:
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ����","com.txt");
				Message child6=childhand.obtainMessage();
				child6.what=COMThread.EV_BENTO_HOTCHILD;
        		JSONObject ev6=null;
	    		try {
	    			ev6=new JSONObject();
	    			ev6.put("cabinet", bundle.getInt("cabinet"));	
	    			ev6.put("opt", bundle.getInt("opt"));
	    			ToolClass.Log(ToolClass.INFO,"EV_COM","ServiceSend0.1="+ev6.toString(),"com.txt");
	    		} catch (JSONException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		child6.obj=ev6;
        		childhand.sendMessage(child6);	
				break;
			//�ֽ��豸ʹ�ܽ���	
			case EV_MDB_ENABLE:
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ʹ�ܽ���","com.txt");
				Message child8=childhand.obtainMessage();
				child8.what=COMThread.EV_MDB_ENABLE;
        		JSONObject ev8=null;
	    		try {
	    			ev8=new JSONObject();
	    			ev8.put("bill", bundle.getInt("bill"));	
	    			ev8.put("coin", bundle.getInt("coin"));
	    			ev8.put("opt", bundle.getInt("opt"));
	    			ToolClass.Log(ToolClass.INFO,"EV_COM","ServiceSend0.1="+ev8.toString(),"com.txt");
	    		} catch (JSONException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		child8.obj=ev8;
        		childhand.sendMessage(child8);	
				break;
				//ֽ������ѯ�ӿ�
			case EV_MDB_B_INFO:
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ֽ������ѯ�ӿ�","com.txt");
				Message child9=childhand.obtainMessage();
				child9.what=COMThread.EV_MDB_B_INFO;
        		JSONObject ev9=null;
	    		ev9=new JSONObject();	    			
				ToolClass.Log(ToolClass.INFO,"EV_COM","ServiceSend0.1="+ev9.toString(),"com.txt");
	    		child9.obj=ev9;
        		childhand.sendMessage(child9);	
				break;
				//Ӳ������ѯ�ӿ�
			case EV_MDB_C_INFO:
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMService Ӳ������ѯ�ӿ�","com.txt");
				Message child10=childhand.obtainMessage();
				child10.what=COMThread.EV_MDB_C_INFO;
        		JSONObject ev10=null;
	    		ev10=new JSONObject();	    			
				ToolClass.Log(ToolClass.INFO,"EV_COM","ServiceSend0.1="+ev10.toString(),"com.txt");
	    		child10.obj=ev10;
        		childhand.sendMessage(child10);	
				break;		
				//Ӳ������ѯ�ӿ�
			case EV_MDB_HEART:
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMService EV_MDB_HEART�ӿ�","com.txt");
				Message child11=childhand.obtainMessage();
				child11.what=COMThread.EV_MDB_HEART;
        		JSONObject ev11=null;
	    		ev11=new JSONObject();	    			
				ToolClass.Log(ToolClass.INFO,"EV_COM","ServiceSend0.1="+ev11.toString(),"com.txt");
	    		child11.obj=ev11;
        		childhand.sendMessage(child11);	
				break;	
			}			
		}

	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		ToolClass.Log(ToolClass.INFO,"EV_COM","COMService create","com.txt");
		super.onCreate();
		//9.ע�������
		localBroadreceiver = LocalBroadcastManager.getInstance(this);
		receiver=new ActivityReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("android.intent.action.comsend");
		localBroadreceiver.registerReceiver(receiver,filter);	
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		ToolClass.Log(ToolClass.INFO,"EV_COM","COMService start","com.txt");
		//***********************
		//�߳̽���vmserver����
		//***********************
		mainhand=new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub				
				switch (msg.what)
				{
					//ȫ����ѯ
					case COMThread.EV_BENTO_CHECKALLMAIN://���߳̽������߳���Ϣǩ�����
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ����ȫ����ѯ����="+msg.obj,"com.txt");
						String tempno6=null; 
						Map<String, Object> Set6= (Map<String, Object>) msg.obj;
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ������ѯ����=2","com.txt");
						//�������
				        Set<Entry<String, Object>> allmap6=Set6.entrySet();  //ʵ����
				        Iterator<Entry<String, Object>> iter6=allmap6.iterator();
				        while(iter6.hasNext())
				        {
				            Entry<String, Object> me=iter6.next();
				            if(
				               (me.getKey().equals("cabinet")!=true)&&(me.getKey().equals("cool")!=true)
				               &&(me.getKey().equals("hot")!=true)&&(me.getKey().equals("light")!=true)
				               &&(me.getKey().equals("EV_TYPE")!=true)
				            )   
				            {
				            	if(Integer.parseInt(me.getKey())<10)
				    				tempno6="0"+me.getKey();
				    			else 
				    				tempno6=me.getKey();
				            	
				            	huoSet.put(cabinetID[huom]+tempno6,(Integer)me.getValue());
				            }
				        } 
				        ToolClass.Log(ToolClass.INFO,"EV_COM","COMService<<"+huoSet.size()+"����״̬:"+huoSet.toString(),"com.txt");	
				        huom++;
				        //2.������ȡ���л�����
				        if(huom<cabinetID.length)
				        {					        	
				        	childhand=comserial.obtainHandler();
			        		Message childrec=childhand.obtainMessage();
			        		//���һ�������
			        		vmc_cabinetDAO cabinetDAOrec = new vmc_cabinetDAO(COMService.this);// ����InaccountDAO����
			        		// ��ȡ����������Ϣ�����洢��List���ͼ�����
			        	    Tb_vmc_cabinet listinfosrec = cabinetDAOrec.findScrollData(cabinetID[huom]);
			        		//���ӹ�
			        	    if(listinfosrec.getCabType()==5)
			        		{
			        			ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ���ӹ��ѯ="+cabinetID[huom],"com.txt");
			        			childrec.what=COMThread.EV_BENTO_CHECKALLCHILD;
			        		}
			        		else
			        		{
			        			ToolClass.Log(ToolClass.INFO,"EV_COM","COMService �������ѯ="+cabinetID[huom],"com.txt");
			        			childrec.what=COMThread.EV_COLUMN_CHECKALLCHILD;
			        		}
			        		JSONObject evrec=null;
				    		try {
				    			evrec=new JSONObject();
				    			evrec.put("cabinet", cabinetID[huom]);	    			  			
				    			ToolClass.Log(ToolClass.INFO,"EV_COM","ServiceSend0.1="+evrec.toString(),"com.txt");
				    		} catch (JSONException e) {
				    			// TODO Auto-generated catch block
				    			e.printStackTrace();
				    		}
				    		childrec.obj=evrec;
			        		childhand.sendMessage(childrec);
				        }
				        else
				        {
				        	ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ���ӹ��������","com.txt");
				        	//���ظ�activity�㲥
							Intent recintent=new Intent();
							recintent.putExtra("EVWhat", COMService.EV_CHECKALLMAIN);						
							//��������
					        SerializableMap myMap=new SerializableMap();
					        myMap.setMap(huoSet);//��map������ӵ���װ��myMap<span></span>��
					        Bundle bundle=new Bundle();
					        bundle.putSerializable("result", myMap);
					        recintent.putExtras(bundle);
							recintent.setAction("android.intent.action.comrec");//action���������ͬ
							localBroadreceiver.sendBroadcast(recintent);
						}												
						break;
					//��ѯ
					case COMThread.EV_BENTO_CHECKMAIN://���߳̽������߳���Ϣǩ�����
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ������ѯ����="+msg.obj,"com.txt");
						//���ظ�activity�㲥
						Intent recintent=new Intent();
						recintent.putExtra("EVWhat", COMService.EV_CHECKMAIN);						
						//��������
				        SerializableMap myMap=new SerializableMap();
				        myMap.setMap((Map<String, Integer>) msg.obj);//��map������ӵ���װ��myMap<span></span>��
				        Bundle bundle=new Bundle();
				        bundle.putSerializable("result", myMap);
				        recintent.putExtras(bundle);
						recintent.setAction("android.intent.action.comrec");//action���������ͬ
						localBroadreceiver.sendBroadcast(recintent);						
						break;	
					//�������	
					case COMThread.EV_BENTO_OPTMAIN:
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMService ��������="+msg.obj,"com.txt");	
						//���ظ�activity�㲥
						Intent recintent2=new Intent();
						recintent2.putExtra("EVWhat", COMService.EV_OPTMAIN);						
						//��������
				        SerializableMap myMap2=new SerializableMap();
				        myMap2.setMap((Map<String, Integer>) msg.obj);//��map������ӵ���װ��myMap<span></span>��
				        Bundle bundle2=new Bundle();
				        bundle2.putSerializable("result", myMap2);
				        recintent2.putExtras(bundle2);
						recintent2.setAction("android.intent.action.comrec");//action���������ͬ
						localBroadreceiver.sendBroadcast(recintent2);
						break;
				}				
			}
			
		};
		//�����û��Լ�������࣬�����߳�
		comserial=new COMThread(mainhand);
  		thread=Executors.newFixedThreadPool(3);
  		thread.execute(comserial);	
	}
	
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub		
		ToolClass.Log(ToolClass.INFO,"EV_COM","COMService destroy","com.txt");
		EVprotocol.EVPortRelease(ToolClass.getBentcom_id());
		//���ע�������
		localBroadreceiver.unregisterReceiver(receiver);
		super.onDestroy();
	}
	

}
