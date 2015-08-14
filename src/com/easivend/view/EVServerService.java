package com.easivend.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.easivend.app.maintain.HuodaoSet;
import com.easivend.app.maintain.Order;
import com.easivend.common.SerializableMap;
import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_OrderAdapter;
import com.easivend.dao.vmc_cabinetDAO;
import com.easivend.dao.vmc_classDAO;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.dao.vmc_orderDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.http.EVServerhttp;
import com.easivend.model.Tb_vmc_cabinet;
import com.easivend.model.Tb_vmc_class;
import com.easivend.model.Tb_vmc_column;
import com.easivend.model.Tb_vmc_order_pay;
import com.easivend.model.Tb_vmc_product;
import com.example.evconsole.R;

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
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class EVServerService extends Service {
	private final int SPLASH_DISPLAY_LENGHT = 3000; // �ӳ�3��
	private Thread thread=null;
    private Handler mainhand=null,childhand=null;  
    private String vmc_no=null;
    private String vmc_auth_code=null;
    private int tokenno=0;
    EVServerhttp serverhttp=null;
    ActivityReceiver receiver;
    Map<String,Integer> huoSet=null;
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
			//ǩ��
			case EVServerhttp.SETCHILD:
				vmc_no=bundle.getString("vmc_no");
				vmc_auth_code=bundle.getString("vmc_auth_code");
				SerializableMap serializableMap = (SerializableMap) bundle.get("huoSet");
				huoSet=serializableMap.getMap();
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","receiver:vmc_no="+vmc_no+"vmc_auth_code="+vmc_auth_code
						+"huoSet="+huoSet.toString(),"server.txt");
				
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
    		//�豸״̬�ϱ�	
			case EVServerhttp.SETDEVSTATUCHILD:
				int bill_err=bundle.getInt("bill_err");
				int coin_err=bundle.getInt("coin_err");
    			ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service �ϱ��豸bill_err="+bill_err
						+" coin_err="+coin_err,"server.txt");				
    			//
	        	childhand=serverhttp.obtainHandler();
	    		Message childmsg3=childhand.obtainMessage();
	    		childmsg3.what=EVServerhttp.SETDEVSTATUCHILD;
	    		JSONObject ev3=null;
	    		try {
	    			ev3=new JSONObject();
	    			ev3.put("bill_err", bill_err);
	    			ev3.put("coin_err", coin_err);	    			  			
	    			ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send0.1="+ev3.toString(),"server.txt");
	    		} catch (JSONException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		childmsg3.obj=ev3;
	    		childhand.sendMessage(childmsg3);
    			break;	
			}			
		}

	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service create","server.txt");
		super.onCreate();
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
					//ǩ��
					case EVServerhttp.SETMAIN://���߳̽������߳���Ϣǩ�����
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ǩ���ɹ�","server.txt");
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
							boolean shprst=updatevmcProduct(msg.obj.toString());
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
							updatevmcColumn(msg.obj.toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//��ʼ����
						//���ظ�activity�㲥,��ʼ�����
						intent=new Intent();
						intent.putExtra("EVWhat", EVServerhttp.SETMAIN);
						intent.setAction("android.intent.action.vmserverrec");//action���������ͬ
						sendBroadcast(intent);
						
						break;
					//��ȡ�豸��Ϣ	
					case EVServerhttp.SETERRFAILDEVSTATUMAIN://���߳̽������߳���Ϣ��ȡ�豸��Ϣʧ��
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ��ȡ�豸��Ϣʧ�ܣ�ԭ��="+msg.obj.toString(),"server.txt");
						break;
					case EVServerhttp.SETDEVSTATUMAIN://���߳̽������߳���Ϣ��ȡ�豸��Ϣ
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ��ȡ�豸��Ϣ�ɹ�","server.txt");
						//ͬ��������������������߳���
		            	childhand=serverhttp.obtainHandler();
		        		Message childheartmsg=childhand.obtainMessage();
		        		childheartmsg.what=EVServerhttp.SETHEARTCHILD;
		        		childhand.sendMessage(childheartmsg);
						
						break;
					//��ȡ������Ϣ	
					case EVServerhttp.SETERRFAILHEARTMAIN://���߳̽������߳���Ϣ��ȡ������Ϣʧ��
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ��ȡ������Ϣʧ�ܣ�ԭ��="+msg.obj.toString(),"server.txt");
						break;
					case EVServerhttp.SETHEARTMAIN://���߳̽������߳���Ϣ��ȡ������Ϣ
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service ��ȡ������Ϣ�ɹ�","server.txt");
						vmc_orderDAO orderDAO = new vmc_orderDAO(EVServerService.this);
						//ͬ���������ͽ��׼�¼������߳���
		            	childhand=serverhttp.obtainHandler();
		        		Message childheartmsg2=childhand.obtainMessage();
		        		childheartmsg2.what=EVServerhttp.SETRECORDCHILD;
		        		childheartmsg2.obj=grid();
		        		childhand.sendMessage(childheartmsg2);						
						break;
					//��ȡ�ϱ����׼�¼����	
					case EVServerhttp.SETERRFAILRECORDMAIN://���߳̽������߳���Ϣ�ϱ����׼�¼ʧ��
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service �ϱ����׼�¼ʧ��","server.txt");
						break;
					case EVServerhttp.SETRECORDMAIN://���߳̽������߳���Ϣ�ϱ����׼�¼
						//�޸������ϱ�״̬Ϊ���ϱ�
						updategrid(msg.obj.toString());
						
						//ͬ���ġ����ͻ����ϴ�������߳���
						childhand=serverhttp.obtainHandler();
		        		Message childheartmsg3=childhand.obtainMessage();
		        		childheartmsg3.what=EVServerhttp.SETHUODAOSTATUCHILD;
		        		childheartmsg3.obj=columngrid();
		        		childhand.sendMessage(childheartmsg3);
						break;	
					//��ȡ�ϱ�������Ϣ����	
					case EVServerhttp.SETERRFAILHUODAOSTATUMAIN://���߳̽������߳��ϱ�������Ϣʧ��
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service �ϱ�������Ϣʧ��","server.txt");
						break;
					case EVServerhttp.SETHUODAOSTATUMAIN://���߳̽������߳��ϱ�������Ϣ
						//�޸������ϱ�״̬Ϊ���ϱ�
						updatecolumns(msg.obj.toString());
						//���¸���token��ֵ
						if(tokenno>=480)
						{
							//������յ�������,����ǩ��������߳���
							//��ʼ��һ:����ǩ��ָ��
				        	childhand=serverhttp.obtainHandler();
				    		Message childheartmsg4=childhand.obtainMessage();
				    		childheartmsg4.what=EVServerhttp.SETCHECKCHILD;
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
				    		childheartmsg4.obj=ev;
				    		childhand.sendMessage(childheartmsg4);
				    		tokenno=0;
						}
						else
						{
							tokenno++;
						}
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
	private boolean updatevmcProduct(String classrst) throws JSONException
	{
		JSONObject jsonObject = new JSONObject(classrst); 
		JSONArray arr1=jsonObject.getJSONArray("ProductList");
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","������Ʒ="+arr1.length()+"txt="+classrst,"server.txt");
		
		for(int i=0;i<arr1.length();i++)
		{
			JSONObject object2=arr1.getJSONObject(i);
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","������Ʒ="+i+"txt="+object2.toString(),"server.txt");
			
			String product_Class_NO=(object2.getString("product_Class_NO").isEmpty())?"0":object2.getString("product_Class_NO");
			product_Class_NO=product_Class_NO.substring(product_Class_NO.lastIndexOf(',')+1,product_Class_NO.length());
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","2������Ʒ"+i+"txt=product_NO="+object2.getString("product_NO")
					+"product_Name="+object2.getString("product_Name")+"product_Class_NO="+product_Class_NO
					+"AttImg="+object2.getString("AttImg"),"server.txt");										
			// ����InaccountDAO����
			vmc_productDAO productDAO = new vmc_productDAO(EVServerService.this);
            //����Tb_inaccount����
			Tb_vmc_product tb_vmc_product = new Tb_vmc_product(object2.getString("product_NO"), object2.getString("product_Name"),object2.getString("product_TXT"),Float.parseFloat(object2.getString("market_Price")),
					Float.parseFloat(object2.getString("sales_Price")),0,object2.getString("create_Time"),object2.getString("last_Edit_Time"),object2.getString("AttImg"),"","",0,0);
			productDAO.addorupdate(tb_vmc_product,product_Class_NO);// �޸�
		}
		return true;
	}
		
	//���»�����Ϣ
	private void updatevmcColumn(String classrst) throws JSONException
	{
		JSONObject jsonObject = new JSONObject(classrst); 
		JSONArray arr1=jsonObject.getJSONArray("PathList");
		for(int i=0;i<arr1.length();i++)
		{
			JSONObject object2=arr1.getJSONObject(i);
			int PATH_ID=object2.getInt("PATH_ID");
			int CABINET_NO=Integer.parseInt(object2.getString("CABINET_NO"));
			int PATH_NO=Integer.parseInt(object2.getString("PATH_NO"));
			String PATH_NOSTR=(PATH_NO<10)?("0"+String.valueOf(PATH_NO)):String.valueOf(PATH_NO);
			int PATH_REMAINING=Integer.parseInt(object2.getString("PATH_REMAINING"));
			int status=0;
			int j=0;
			//�������
	        Set<Map.Entry<String,Integer>> allset=huoSet.entrySet();  //ʵ����
	        Iterator<Map.Entry<String,Integer>> iter=allset.iterator();
	        while(iter.hasNext())
	        {
	            Map.Entry<String,Integer> me=iter.next();
				String huo=me.getKey();
				int cabid=Integer.parseInt(huo.substring(0, 1));
				int huoid=Integer.parseInt(huo.substring(1, huo.length()));
				status=me.getValue();				
			    if((CABINET_NO==cabid)&&(PATH_NO==huoid))
			    {
			    	j=1;
			    	break;
			    }
				//ToolClass.Log(ToolClass.INFO,"EV_SERVER","huo="+cabid+","+huoid+"sta="+me.getValue(),"server.txt");
	        } 			

	        //���Ը��»���
			if(j==1)
			{		
				status=updatehuodaostatus(status,PATH_REMAINING);
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","���»���PATH_ID="+PATH_ID+"CABINET_NO="+object2.getString("CABINET_NO")
						+"PATH_NO="+PATH_NOSTR+"PRODUCT_NO="+object2.getString("PRODUCT_NO")
						+"PATH_COUNT="+object2.getString("PATH_COUNT"),"server.txt");	
				// ����InaccountDAO����
    			vmc_columnDAO columnDAO = new vmc_columnDAO(EVServerService.this);
	            //����Tb_inaccount����
    			Tb_vmc_column tb_vmc_column = new Tb_vmc_column(object2.getString("CABINET_NO"), PATH_NOSTR,object2.getString("PRODUCT_NO"),
    					Integer.parseInt(object2.getString("PATH_COUNT")),Integer.parseInt(object2.getString("PATH_REMAINING")),
    					status,"",PATH_ID,0);    			
    			columnDAO.addorupdateforserver(tb_vmc_column);// �����Ʒ��Ϣ
			}
			//���»���ʧ��
			else
			{
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","���»���ʧ��CABINET_NO="+object2.getString("CABINET_NO")
						+"PATH_NO="+PATH_NOSTR+"PRODUCT_NO="+object2.getString("PRODUCT_NO")
						+"PATH_COUNT="+object2.getString("PATH_COUNT"),"server.txt");										
			}			
		}
	}
	
	//���»���״̬��Ϣ
	//huostate�ӻ������ϵõ��Ļ���״̬��upremain�ӷ�������صĻ����������
	private int updatehuodaostatus(int huostate,int upremain)
	{
		int huostatus=0;
		if(huostate==0)//��������
			huostatus=2;
		else if(huostate==1)//��������
		{
			if(upremain>0)
	    		huostatus=1;
	    	else                             //ȱ��
	    		huostatus=3;	
		}
		return huostatus;
	}
	
	//��ȡ��Ҫ�ϱ��ı���
	private JSONArray grid()
	{		
		//��֧������
		String[] ordereID;// ����ID[pk]
		String[] payType;// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
		String[] payStatus;// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
		String[] RealStatus;// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
		String[] smallNote;// ֽ�ҽ��
		String[] smallConi;// Ӳ�ҽ��
		String[] smallAmount;// �ֽ�Ͷ����
		String[] smallCard;// ���ֽ�֧�����
		String[] shouldPay;// ��Ʒ�ܽ��
		String[] shouldNo;// ��Ʒ������
		String[] realNote;// ֽ���˱ҽ��
		String[] realCoin;// Ӳ���˱ҽ��
		String[] realAmount;// �ֽ��˱ҽ��
		String[] debtAmount;// Ƿ����
		String[] realCard;// ���ֽ��˱ҽ��
		String[] payTime;//֧��ʱ��
			//��ϸ֧������
		String[] productID;//��Ʒid
		String[] cabID;//�����
		String[] columnID;//������
		    //��Ʒ��Ϣ
		String[] productName;// ��Ʒȫ��
		String[] salesPrice;// �Żݼ�,�硱20.00��
		
		//�������Ͷ�����Ϣ
	    //��֧������
		int[] payTypevalue;// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
	    int[] payStatusvalue;// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
	    int[] RealStatusvalue;// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
	  	double[] smallNotevalue;// ֽ�ҽ��
		double[] smallConivalue;// Ӳ�ҽ��
		double[] smallAmountvalue;// �ֽ�Ͷ����
		double[] smallCardvalue;// ���ֽ�֧�����
		double[] shouldPayvalue;// ��Ʒ�ܽ��
		double[] shouldNovalue;// ��Ʒ������
		double[] realNotevalue;// ֽ���˱ҽ��
		double[] realCoinvalue;// Ӳ���˱ҽ��
		double[] realAmountvalue;// �ֽ��˱ҽ��
		double[] debtAmountvalue;// Ƿ����
		double[] realCardvalue;// ���ֽ��˱ҽ��
	  	//��Ʒ��Ϣ
		double[] salesPricevalue;// �Żݼ�,�硱20.00��
		int ourdercount=0;//��¼������
		JSONArray array=new JSONArray();
		
		
		Vmc_OrderAdapter vmc_OrderAdapter=new Vmc_OrderAdapter();
		vmc_OrderAdapter.grid(EVServerService.this);
		//��֧������
		ordereID = vmc_OrderAdapter.getOrdereID();// ����ID[pk]
		payType = vmc_OrderAdapter.getPayType();// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
		payStatus = vmc_OrderAdapter.getPayStatus();// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
		RealStatus = vmc_OrderAdapter.getRealStatus();// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
		smallNote = vmc_OrderAdapter.getSmallNote();// ֽ�ҽ��
		smallConi = vmc_OrderAdapter.getSmallConi();// Ӳ�ҽ��
		smallAmount = vmc_OrderAdapter.getSmallAmount();// �ֽ�Ͷ����
		smallCard = vmc_OrderAdapter.getSmallCard();// ���ֽ�֧�����
		shouldPay = vmc_OrderAdapter.getShouldPay();// ��Ʒ�ܽ��
		shouldNo = vmc_OrderAdapter.getShouldNo();// ��Ʒ������
		realNote = vmc_OrderAdapter.getRealNote();// ֽ���˱ҽ��
		realCoin = vmc_OrderAdapter.getRealCoin();// Ӳ���˱ҽ��
		realAmount = vmc_OrderAdapter.getRealAmount();// �ֽ��˱ҽ��
		debtAmount = vmc_OrderAdapter.getDebtAmount();// Ƿ����
		realCard = vmc_OrderAdapter.getRealCard();// ���ֽ��˱ҽ��
		payTime = vmc_OrderAdapter.getPayTime();//֧��ʱ��
		//��ϸ֧������
		productID = vmc_OrderAdapter.getProductID();//��Ʒid
		cabID = vmc_OrderAdapter.getCabID();//�����
	    columnID = vmc_OrderAdapter.getColumnID();//������
	    //��Ʒ��Ϣ
	    productName = vmc_OrderAdapter.getProductName();// ��Ʒȫ��
	    salesPrice = vmc_OrderAdapter.getSalesPrice();// �Żݼ�,�硱20.00��
	    
	    //�������Ͷ�����Ϣ
	    payTypevalue= vmc_OrderAdapter.getPayTypevalue();// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
		payStatusvalue = vmc_OrderAdapter.getPayStatusvalue();// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
		RealStatusvalue = vmc_OrderAdapter.getRealStatusvalue();// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
	    smallNotevalue= vmc_OrderAdapter.getSmallNotevalue();// ֽ�ҽ��
	    smallConivalue= vmc_OrderAdapter.getSmallConivalue();// Ӳ�ҽ��
	    smallAmountvalue= vmc_OrderAdapter.getSmallAmountvalue();// �ֽ�Ͷ����
	    smallCardvalue= vmc_OrderAdapter.getSmallCardvalue();// ���ֽ�֧�����
	    shouldPayvalue= vmc_OrderAdapter.getShouldPayvalue();// ��Ʒ�ܽ��
	    shouldNovalue= vmc_OrderAdapter.getShouldNovalue();// ��Ʒ������
	    realNotevalue= vmc_OrderAdapter.getRealNotevalue();// ֽ���˱ҽ��
	    realCoinvalue= vmc_OrderAdapter.getRealCoinvalue();// Ӳ���˱ҽ��
	    realAmountvalue= vmc_OrderAdapter.getRealAmountvalue();// �ֽ��˱ҽ��
	    debtAmountvalue= vmc_OrderAdapter.getDebtAmountvalue();// Ƿ����
	    realCardvalue= vmc_OrderAdapter.getRealCardvalue();// ���ֽ��˱ҽ��
	    //��Ʒ��Ϣ
	    salesPricevalue= vmc_OrderAdapter.getSalesPricevalue();// �Żݼ�,�硱20.00��
	    ourdercount=vmc_OrderAdapter.getCount();
	    
	    int orderStatus=0;//1δ֧��,2�����ɹ�,3����δ���
	    int payStatue=0;//0δ����,1���ڸ���,2�������,3����ʧ��
	    int payTyp=0;//0�ֽ�,1֧��������,2����,3֧������ά��,4΢��
	    double RefundAmount=0;//�˿���
	    int actualQuantity=0;//ʵ�ʳ���,1�ɹ�,0ʧ��
	    try {
	    	for(int x=0;x<vmc_OrderAdapter.getCount();x++)	
			{
		    	// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
		    	if(payStatusvalue[x]==2||payStatusvalue[x]==3)
				{
		    		orderStatus=1;
		    		payStatue=3;
		    		actualQuantity=0;
				}
				else if(payStatusvalue[x]==0)
				{
					orderStatus=2;
					payStatue=2;
					actualQuantity=1;
				}
				else if(payStatusvalue[x]==1)
				{
					orderStatus=3;
					payStatue=2;
					actualQuantity=0;
				}
		    	// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
		    	if(payTypevalue[x]==0)
				{
		    		payTyp=0;
				}
				else if(payTypevalue[x]==1)
				{
					payTyp=2;
				}
				else if(payTypevalue[x]==2)
				{
					payTyp=1;
				}
				else if(payTypevalue[x]==3)
				{
					payTyp=3;
				}
				else if(payTypevalue[x]==4)
				{
					payTyp=4;
				}
		    	RefundAmount=realAmountvalue[x]+realCardvalue[x];
		    	//ToolClass.Log(ToolClass.INFO,"EV_SERVER","����payStatus="+payStatusvalue[x]+"payType="+payTypevalue[x],"server.txt");
				
		    	ToolClass.Log(ToolClass.INFO,"EV_SERVER","����orderNo="+ordereID[x]+"orderTime="+getStrtime(payTime[x])+"orderStatus="+orderStatus+"payStatus="
				+payStatue+"payType="+payTyp+"shouldPay="+shouldPay[x]+"RefundAmount="+RefundAmount+"productNo="+productID[x]+"quantity="+1+
				"actualQuantity="+actualQuantity+"customerPrice="+salesPrice[x]+"productName="+productName[x],"server.txt");			
		    	JSONObject object=new JSONObject();
		    	object.put("orderNo", ordereID[x]);
		    	object.put("orderTime", getStrtime(payTime[x]));
		    	object.put("orderStatus", orderStatus);
		    	object.put("payStatus", payStatue);
		    	object.put("payType", payTyp);
		    	object.put("shouldPay", shouldPay[x]);
		    	object.put("RefundAmount", RefundAmount);
		    	object.put("productNo", productID[x]);		    	
		    	object.put("quantity", 1);
		    	object.put("actualQuantity", actualQuantity);
		    	object.put("customerPrice", salesPrice[x]);
		    	object.put("productName", productName[x]);	
		    	
		    	array.put(object);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	    return array;
	}
	private String getStrtime(String orderTime)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date =null;
		try {
			date = df.parse(orderTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd"); //��ȷ������ 
		SimpleDateFormat tempTime = new SimpleDateFormat("HH:mm:ss"); //��ȷ������ 
        String datetime = tempDate.format(date).toString()+"T"
        		+tempTime.format(date).toString(); 
		return datetime;
	}
	
	//�޸������ϱ�״̬Ϊ���ϱ�
	private void updategrid(String str)
	{	
		// ����InaccountDAO����
  		vmc_orderDAO orderDAO = new vmc_orderDAO(EVServerService.this);
  		
		JSONArray json=null;
		try {
			json=new JSONArray(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service �ϱ����׼�¼�ɹ�="+json.toString(),"server.txt");
		for(int i=0;i<json.length();i++)
		{
			JSONObject object2=null;
			String orderno=null;
			try {
				object2 = json.getJSONObject(i);
				orderno=object2.getString("orderno");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","�ϱ����׼�¼="+orderno
					,"server.txt");
			orderDAO.update(orderno);
		}		  	
	}
	
	//��ȡ��Ҫ�ϱ��Ļ���
	private JSONArray columngrid()
	{				
		int ourdercount=0;//��¼������
		JSONArray array=new JSONArray();
		
		// ����InaccountDAO����
		vmc_columnDAO columnDAO = new vmc_columnDAO(EVServerService.this);
		List<Tb_vmc_column> listinfos=columnDAO.getScrollPay();
		String[] pathIDs= new String[listinfos.size()];
		String[] cabinetNumbers= new String[listinfos.size()];
		String[] pathNames= new String[listinfos.size()];
		String[] productIDs= new String[listinfos.size()];
		String[] productNums= new String[listinfos.size()];
		String[] pathCounts= new String[listinfos.size()];
		String[] pathStatuss= new String[listinfos.size()];
		String[] pathRemainings= new String[listinfos.size()];
		String[] lastedittimes= new String[listinfos.size()];
		String[] isdisables= new String[listinfos.size()];
		String[] isupload= new String[listinfos.size()];
		int x=0;
		try {
			// ����List���ͼ���
	  	    for (Tb_vmc_column tb_inaccount : listinfos) 
	  	    {
	  	    	//��֧������
	  	    	pathIDs[x]= String.valueOf(tb_inaccount.getPath_id());
	  	    	cabinetNumbers[x]= tb_inaccount.getCabineID();
	  	    	String PATH_NOSTR=String.valueOf(Integer.parseInt(tb_inaccount.getColumnID()));
	  			pathNames[x]= PATH_NOSTR;
	  			productIDs[x]= tb_inaccount.getCabineID();
	  			productNums[x]= tb_inaccount.getProductID();
	  			pathCounts[x]= String.valueOf(tb_inaccount.getPathCount());
	  			pathStatuss[x]= String.valueOf(tb_inaccount.getColumnStatus());
	  			pathRemainings[x]= String.valueOf(tb_inaccount.getPathRemain());
	  			lastedittimes[x]= tb_inaccount.getLasttime();
	  			isdisables[x]= "0";
	  			isupload[x] = String.valueOf(tb_inaccount.getIsupload());
	  			ToolClass.Log(ToolClass.INFO,"EV_SERVER","�贫����pathID="+pathIDs[x]+"cabinetNumber="+cabinetNumbers[x]+"pathName="+pathNames[x]+"productID="
	  					+productIDs[x]+"productNum="+productNums[x]+"pathCount="+pathCounts[x]+"pathStatus="+pathStatuss[x]+"pathRemaining="+pathRemainings[x]+"lastedittime="+getStrtime(lastedittimes[x])+
	  					"isdisable="+isdisables[x]+"isupload="+isupload[x],"server.txt");			
		    	JSONObject object=new JSONObject();
		    	object.put("pathID", pathIDs[x]);
		    	object.put("cabinetNumber", cabinetNumbers[x]);
		    	object.put("pathName", pathNames[x]);	    	
		    	object.put("productID", productIDs[x]);
		    	object.put("productNum", productNums[x]);
		    	object.put("pathCount", pathCounts[x]);
		    	object.put("pathStatus", pathStatuss[x]);
		    	object.put("pathRemaining", pathRemainings[x]);
		    	object.put("lastedittime", getStrtime(lastedittimes[x]));		    	
		    	object.put("isdisable", isdisables[x]);
		    	object.put("isupload", isupload[x]);
		    	array.put(object);
	  			x++;  			
	  	    }		
		} catch (Exception e) {
			// TODO: handle exception
		}		
	    return array;
	}
	
	//�޸Ļ����ϱ�״̬Ϊ���ϱ�
	private void updatecolumns(String str)
	{	
		// ����InaccountDAO����
  		vmc_columnDAO columnDAO = new vmc_columnDAO(EVServerService.this);
  		
		JSONArray json=null;
		try {
			json=new JSONArray(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","Service �ϱ������ɹ�="+json.toString(),"server.txt");
		for(int i=0;i<json.length();i++)
		{
			JSONObject object2=null;
			String cabinetNumber=null;
			String pathName=null;
			try {
				object2 = json.getJSONObject(i);
				cabinetNumber=object2.getString("cabinetNumber");
				int PATH_NO=Integer.parseInt(object2.getString("pathName"));
				String PATH_NOSTR=(PATH_NO<10)?("0"+String.valueOf(PATH_NO)):String.valueOf(PATH_NO);
				pathName=PATH_NOSTR;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","�ϱ�����cabinetNumber="+cabinetNumber
					+"pathName="+pathName,"server.txt");
			columnDAO.updatecol(cabinetNumber,pathName);
		}		  	
	}

}
