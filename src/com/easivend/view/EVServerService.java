package com.easivend.view;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.app.maintain.GoodsProSet;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_cabinetDAO;
import com.easivend.dao.vmc_classDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.http.EVServerhttp;
import com.easivend.model.Tb_vmc_cabinet;
import com.easivend.model.Tb_vmc_class;
import com.easivend.model.Tb_vmc_product;

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
    //��ȡ������Ϣ
    String[] cabinetID = null;//���������������
	int[] cabinetType=null;//��������
	int m = 0;// ����һ����ʼ��ʶ
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
		//ע�����������
  	    EVprotocolAPI.setCallBack(new JNIInterface() {
			
			@Override
			public void jniCallback(Map<String, Object> allSet) {
				// TODO Auto-generated method stub
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<huodao�������","server.txt");
				Map<String, Object> Set= allSet;
				int jnirst=(Integer)Set.get("EV_TYPE");
				switch (jnirst)
				{
					case EVprotocolAPI.EV_TRADE_RPT://�������߳���Ϣ
//						device=allSet.get("device");//�������
//						status=allSet.get("status");//�������
//						hdid=allSet.get("hdid");//����id
//						hdtype=allSet.get("type");//��������
//						cost=ToolClass.MoneyRec(allSet.get("cost"));//��Ǯ
//						totalvalue=ToolClass.MoneyRec(allSet.get("totalvalue"));//ʣ����
//						huodao=allSet.get("huodao");//ʣ��������
//						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�������"+"device=["+device+"],status=["+status+"],hdid=["+hdid+"],type=["+hdtype+"],cost=["
//								+cost+"],totalvalue=["+totalvalue+"],huodao=["+huodao+"]");	
//						
//						txthuorst.setText("device=["+device+"],status=["+status+"],hdid=["+hdid+"],type=["+hdtype+"],cost=["
//								+cost+"],totalvalue=["+totalvalue+"],huodao=["+huodao+"]");
//						sethuorst(status);
						break;
					case EVprotocolAPI.EV_COLUMN_RPT://�������߳���Ϣ
//						huoSet.clear();
//						//�������
//				        Set<Entry<String, Integer>> allmap=Set.entrySet();  //ʵ����
//				        Iterator<Entry<String, Integer>> iter=allmap.iterator();
//				        while(iter.hasNext())
//				        {
//				            Entry<String, Integer> me=iter.next();
//				            if(me.getKey().equals("EV_TYPE")!=true)
//				            	huoSet.put(me.getKey(), me.getValue());
//				        } 
//						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����״̬:"+huoSet.toString());	
//						showhuodao();						
						break;
					case EVprotocolAPI.EV_BENTO_CHECK://���ӹ��ѯ
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<huodao���ӹ��ѯ","server.txt");
//						String tempno=null;
//						
//						cool=(Integer)Set.get("cool");
//						hot=(Integer)Set.get("hot");
//						light=(Integer)Set.get("light");
//						ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����cool:"+cool+",hot="+hot+",light="+light,"log.txt");
//						if(light>0)
//						{
//							txtlight.setText("֧��");
//							switchlight.setEnabled(true);
//							
//						}
//						else
//						{
//							txtlight.setText("��֧��");
//							switchlight.setEnabled(false);
//						}
//						if(cool>0)
//						{
//							txtcold.setText("֧��");
//							switcold.setEnabled(true);
//						}
//						else
//						{
//							txtcold.setText("��֧��");
//							switcold.setEnabled(false);
//						}
//						if(hot>0)
//						{
//							txthot.setText("֧��");
//							switchhot.setEnabled(true);
//						}
//						else
//						{
//							txthot.setText("��֧��");
//							switchhot.setEnabled(false);
//						}
//						
//						huoSet.clear();
//						//�������
//				        Set<Entry<String, Object>> allmap=Set.entrySet();  //ʵ����
//				        Iterator<Entry<String, Object>> iter=allmap.iterator();
//				        while(iter.hasNext())
//				        {
//				            Entry<String, Object> me=iter.next();
//				            if(
//				               (me.getKey().equals("EV_TYPE")!=true)&&(me.getKey().equals("cool")!=true)
//				               &&(me.getKey().equals("hot")!=true)&&(me.getKey().equals("light")!=true)
//				            )   
//				            {
//				            	if(Integer.parseInt(me.getKey())<10)
//				    				tempno="0"+me.getKey();
//				    			else 
//				    				tempno=me.getKey();
//				            	
//				            	huoSet.put(tempno, (Integer)me.getValue());
//				            }
//				        } 
//				        huonum=huoSet.size();
//						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+huonum+"����״̬:"+huoSet.toString(),"log.txt");	
//						showhuodao();						
						break;	
					case EVprotocolAPI.EV_BENTO_OPEN://���ӹ����
						break;	
					case EVprotocolAPI.EV_BENTO_LIGHT://���ӹ񿪵�
						break;	
				}
			}
		});
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
					//ǩ��
					case EVServerhttp.SETMAIN://���߳̽������߳���Ϣǩ�����
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ǩ���ɹ�","server.txt");
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
					case EVServerhttp.SETERRFAILMAIN://���߳̽������߳���Ϣǩ��ʧ��
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ǩ��ʧ�ܣ�ԭ��="+msg.obj.toString(),"server.txt");
						//���ظ�activity�㲥
						intent=new Intent();
						intent.putExtra("EVWhat", EVServerhttp.SETFAILMAIN);
						intent.setAction("android.intent.action.vmserverrec");//action���������ͬ
						sendBroadcast(intent);	
						break;
					//��ȡ��Ʒ������Ϣ	
					case EVServerhttp.SETERRFAILCLASSMAIN://���߳̽������߳���Ϣ��ȡ��Ʒ������Ϣʧ��
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ��ȡ��Ʒ������Ϣʧ�ܣ�ԭ��="+msg.obj.toString(),"server.txt");
						break;
					case EVServerhttp.SETCLASSMAIN://���߳̽������߳���Ϣ��ȡ��Ʒ������Ϣ
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ��ȡ��Ʒ������Ϣ�ɹ�","server.txt");
						try 
						{
							updatevmcClass(msg.obj.toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//��ʼ����:��ȡ��Ʒ��Ϣ
						childhand=serverhttp.obtainHandler();
		        		Message childmsg2=childhand.obtainMessage();
		        		childmsg2.what=EVServerhttp.SETPRODUCTCHILD;
		        		childhand.sendMessage(childmsg2);
						break;
					//��ȡ��Ʒ��Ϣ	
					case EVServerhttp.SETERRFAILRODUCTMAIN://���߳̽������߳���Ϣ��ȡ��Ʒ��Ϣʧ��
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ��ȡ��Ʒ��Ϣʧ�ܣ�ԭ��="+msg.obj.toString(),"server.txt");
						break;
					case EVServerhttp.SETRODUCTMAIN://���߳̽������߳���Ϣ��ȡ��Ʒ��Ϣ
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ��ȡ��Ʒ��Ϣ�ɹ�","server.txt");
						try 
						{
							updatevmcProduct(msg.obj.toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//��ʼ����:��ȡ����������Ϣ
						childhand=serverhttp.obtainHandler();
		        		Message childmsg3=childhand.obtainMessage();
		        		childmsg3.what=EVServerhttp.SETHUODAOCHILD;
		        		childhand.sendMessage(childmsg3);
						break;	
					//��ȡ������Ϣ	
					case EVServerhttp.SETERRFAILHUODAOMAIN://���߳̽������߳���Ϣ��ȡ������Ϣʧ��
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ��ȡ������Ϣʧ�ܣ�ԭ��="+msg.obj.toString(),"server.txt");
						break;
					case EVServerhttp.SETHUODAOMAIN://���߳̽������߳���Ϣ��ȡ������Ϣ
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ��ȡ������Ϣ�ɹ�","server.txt");
						try 
						{
							getcolumnstat();
							updatevmcColumn(msg.obj.toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//��ʼ����:��������
						isev=true;
						break;	
					//�������
					case EVServerhttp.SETFAILMAIN://���߳̽������߳�����ʧ��
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ʧ�ܣ��������","server.txt");
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
	
	//������Ʒ������Ϣ
	private void updatevmcClass(String classrst) throws JSONException
	{
		JSONObject jsonObject = new JSONObject(classrst); 
		JSONArray arr1=jsonObject.getJSONArray("ProductClassList");
		for(int i=0;i<arr1.length();i++)
		{
			JSONObject object2=arr1.getJSONObject(i);
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","������Ʒ����CLASS_CODE="+object2.getString("CLASS_CODE")
					+"CLASS_NAME="+object2.getString("CLASS_NAME"),"server.txt");										
			// ����InaccountDAO����
        	vmc_classDAO classDAO = new vmc_classDAO(EVServerService.this);
            // ����Tb_inaccount����
        	Tb_vmc_class tb_vmc_class = new Tb_vmc_class(object2.getString("CLASS_CODE"), object2.getString("CLASS_NAME"),object2.getString("LAST_EDIT_TIME"),"");
        	classDAO.addorupdate(tb_vmc_class);// �޸�
		}
	}
	
	//������Ʒ��Ϣ
	private void updatevmcProduct(String classrst) throws JSONException
	{
		JSONObject jsonObject = new JSONObject(classrst); 
		JSONArray arr1=jsonObject.getJSONArray("ProductList");
		for(int i=0;i<arr1.length();i++)
		{
			JSONObject object2=arr1.getJSONObject(i);
			String product_Class_NO=(object2.getString("product_Class_NO").isEmpty())?"0":object2.getString("product_Class_NO");
			product_Class_NO=product_Class_NO.substring(product_Class_NO.lastIndexOf(',')+1,product_Class_NO.length());
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","������Ʒproduct_NO="+object2.getString("product_NO")
					+"product_Name="+object2.getString("product_Name")+"product_Class_NO="+product_Class_NO,"server.txt");										
			// ����InaccountDAO����
			vmc_productDAO productDAO = new vmc_productDAO(EVServerService.this);
            //����Tb_inaccount����
			Tb_vmc_product tb_vmc_product = new Tb_vmc_product(object2.getString("product_NO"), object2.getString("product_Name"),object2.getString("product_TXT"),Float.parseFloat(object2.getString("market_Price")),
					Float.parseFloat(object2.getString("sales_Price")),0,object2.getString("create_Time"),object2.getString("last_Edit_Time"),"","","",0,0);
			productDAO.addorupdate(tb_vmc_product,product_Class_NO);// �޸�
		}
	}
	
	//��ȡ��ǰ������Ϣ
	private void getcolumnstat()
	{		
		vmc_cabinetDAO cabinetDAO = new vmc_cabinetDAO(EVServerService.this);// ����InaccountDAO����
	    // 1.��ȡ���й��
	    List<Tb_vmc_cabinet> listinfos = cabinetDAO.getScrollData();
	    cabinetID = new String[listinfos.size()];// �����ַ�������ĳ���
	    cabinetType=new int[listinfos.size()];// �����ַ�������ĳ���	    
	    // ����List���ͼ���
	    for (Tb_vmc_cabinet tb_inaccount : listinfos) 
	    {
	        cabinetID[m] = tb_inaccount.getCabID();
	        cabinetType[m]= tb_inaccount.getCabType();
	        ToolClass.Log(ToolClass.INFO,"EV_SERVER","��ȡ���="+cabinetID[m]+"����="+cabinetType[m],"server.txt");
		    m++;// ��ʶ��1
	    }
	    m=0;
	    //2.��ȡ���л�����
	    queryhuodao(Integer.parseInt(cabinetID[m]),cabinetType[m]);
	}
	//��ȡ�������л�����
	private void queryhuodao(int cabinetsetvar,int cabinetTypesetvar)
	{
		//���ӹ�
		if(cabinetTypesetvar==5)
		{
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","APP<<huodao���ӹ����","server.txt");
			EVprotocolAPI.EV_bentoCheck(ToolClass.getBentcom_id(),cabinetsetvar);
		}
		//��ͨ��
		else 
		{
			EVprotocolAPI.getColumn(cabinetsetvar);
		}
	}
	//���»�����Ϣ
	private void updatevmcColumn(String classrst) throws JSONException
	{
		JSONObject jsonObject = new JSONObject(classrst); 
		JSONArray arr1=jsonObject.getJSONArray("PathList");
		for(int i=0;i<arr1.length();i++)
		{
			JSONObject object2=arr1.getJSONObject(i);
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","���»���CABINET_NO="+object2.getString("CABINET_NO")
					+"PATH_NO="+object2.getString("PATH_NO")+"PRODUCT_NO="+object2.getString("PRODUCT_NO")
					+"PATH_COUNT="+object2.getString("PATH_COUNT"),"server.txt");										
//			// ����InaccountDAO����
//			vmc_productDAO productDAO = new vmc_productDAO(EVServerService.this);
//            //����Tb_inaccount����
//			Tb_vmc_product tb_vmc_product = new Tb_vmc_product(object2.getString("product_NO"), object2.getString("product_Name"),object2.getString("product_TXT"),Float.parseFloat(object2.getString("market_Price")),
//					Float.parseFloat(object2.getString("sales_Price")),0,object2.getString("create_Time"),object2.getString("last_Edit_Time"),"","","",0,0);
//			productDAO.addorupdate(tb_vmc_product,product_Class_NO);// �޸�
		}
	}

}
