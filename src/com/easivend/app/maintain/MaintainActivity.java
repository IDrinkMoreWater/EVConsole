/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           MaintainActivity.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        ά���˵���ҳ��          
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.app.maintain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.easivend.evprotocol.EVprotocol;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.http.EVServerhttp;
import com.easivend.view.DogService;
import com.easivend.view.DogService.LocalBinder;
import com.easivend.view.EVServerService;
import com.easivend.weixing.WeiConfigAPI;
import com.easivend.alipay.AlipayConfigAPI;
import com.easivend.app.business.Business;
import com.easivend.common.PictureAdapter;
import com.easivend.common.ProPictureAdapter;
import com.easivend.common.ToolClass;
import com.example.evconsole.R;


import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.R.string;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MaintainActivity extends Activity
{
	TextView txtcom=null,txtbentcom=null;
	private GridView gvInfo;// ����GridView����
	private ProgressBar barmaintain=null;
	// �����ַ������飬�洢ϵͳ����
    private String[] titles = new String[] { "��Ʒ����", "��������", "��������", "������Ϣ", "������־", "��������", "����ҳ��", "�˳�" };
    // ����int���飬�洢���ܶ�Ӧ��ͼ��
    private int[] images = new int[] { R.drawable.addoutaccount, R.drawable.addinaccount, R.drawable.outaccountinfo, R.drawable.showinfo,
            R.drawable.inaccountinfo, R.drawable.sysset, R.drawable.accountflag, R.drawable.exit };
    //EVprotocolAPI ev=null;
    int comopen=0,bentopen=0;//1�����Ѿ��򿪣�0����û�д�    
    String com=null,bentcom=null,server="";
    final static int REQUEST_CODE=1;   
    //Dog�������
    DogService localService;
	boolean bound=false;
	int isallopen=1;//�Ƿ񱣳ֳ���һֱ��,1һֱ��,0�رպ󲻴�
	private final int SPLASH_DISPLAY_LENGHT = 3000; // �ӳ�3��
	//Server�������
	EVServerReceiver receiver;
	boolean issuc=false;
	//�󶨵Ľӿ�
	private ServiceConnection conn=new ServiceConnection()
	{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//Log.i("currenttime","service onBindSUC="+service.getInterfaceDescriptor());
			LocalBinder binder=(LocalBinder) service;
			localService = binder.getService();
			bound=true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			//Log.i("currenttime","service onBindFail");
			bound=false;
		}

		
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintain);	
		//ע�ᴮ�ڼ�����
		EVprotocolAPI.setCallBack(new JNIInterface() {
			
			@Override
			public void jniCallback(Map<String, Object> allSet) {
				// TODO Auto-generated method stub
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<main������","log.txt");	
				Map<String, Object> Set= allSet;
				int jnirst=(Integer) Set.get("EV_TYPE");
				//txtcom.setText(String.valueOf(jnirst));
				switch (jnirst)
				{
					case EVprotocolAPI.EV_REGISTER://�������߳���Ϣ
						//�����ʼ�����
						if(Set.get("port_com").equals(com))
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�����ʼ�����","log.txt");	
							txtcom.setText(com+"[����]��ʼ�����");		
							ToolClass.setCom_id((Integer)Set.get("port_id"));
						}
						else if(Set.get("port_com").equals(bentcom))
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<���ӹ��ʼ�����","log.txt");	
							txtbentcom.setText(bentcom+"[���ӹ�]��ʼ�����");		
							ToolClass.setBentcom_id((Integer)Set.get("port_id"));
						}
										
						break;
				}
			}
		}); 
		
		//==========
		//Dog�������
		//==========
		//��ʱ3s
	    new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {      
        		localService.setAllopen(isallopen);
            }

		}, SPLASH_DISPLAY_LENGHT);
		//��������
		startService(new Intent(this,DogService.class));
		//�󶨷���
		Intent intent=new Intent(this,DogService.class);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
		bound=true;
		//=============
		//Server�������
		//=============
		//3.��������
		startService(new Intent(MaintainActivity.this,EVServerService.class));
		//4.ע�������
		receiver=new EVServerReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("android.intent.action.vmserverrec");
		this.registerReceiver(receiver,filter);
		//7.����ָ��㲥��EVServerService
		final Map<String, String> vmcmap = ToolClass.getvmc_no(MaintainActivity.this);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() { 
	        @Override 
	        public void run() { 
	  
	            runOnUiThread(new Runnable() {      // UI thread 
	                @Override 
	                public void run() { 
	                	if(issuc==false)
	                	{
		                	Intent intent=new Intent();
		    				intent.putExtra("EVWhat", EVServerhttp.SETCHILD);
		    				intent.putExtra("vmc_no", vmcmap.get("vmc_no"));
		    				intent.putExtra("vmc_auth_code", vmcmap.get("vmc_auth_code"));
		    				intent.setAction("android.intent.action.vmserversend");//action���������ͬ
		    				sendBroadcast(intent);  
	                	}
	                } 
	            }); 
	        } 
	    }, 3*1000, 60*1000);       // timeTask 
		
		//================
		//�������ú�ע�����
		//================
		txtcom=(TextView)super.findViewById(R.id.txtcom);
		txtbentcom=(TextView)super.findViewById(R.id.txtbentcom);
		
		//�������ļ���ȡ����
		Map<String, String> list=ToolClass.ReadConfigFile();
		if(list!=null)
		{
	        com = list.get("com");
	        bentcom = list.get("bentcom");
	        server = list.get("server");
	        AlipayConfigAPI.SetAliConfig(list);//���ð����˺�
	        WeiConfigAPI.SetWeiConfig(list);//����΢���˺�
	        if(list.containsKey("isallopen"))
	        {
	        	isallopen=Integer.parseInt(list.get("isallopen"));	        	
	        }
			txtcom.setText(com+"��������׼������");	
			EVprotocolAPI.vmcEVStart();//��������
			//�����񴮿�		
			comopen = EVprotocolAPI.EV_portRegister(com);
			if(comopen == 1)
			{
				txtcom.setText(com+"[����]��������׼������");			
			}
			else
			{
				txtcom.setText(com+"[����]���ڴ�ʧ��");
			}			
			txtbentcom.setText(bentcom+"[���ӹ�]����׼������");	
			//�򿪸��ӹ�
			bentopen = EVprotocolAPI.EV_portRegister(bentcom);
			if(bentopen == 1)
			{
				txtbentcom.setText(bentcom+"[���ӹ�]��������׼������");			
			}
			else
			{
				txtbentcom.setText(bentcom+"[���ӹ�]���ڴ�ʧ��");
			}
		}
				
		
		//================
		//�Ź������
		//================		
		barmaintain= (ProgressBar) findViewById(R.id.barmaintain);
		gvInfo = (GridView) findViewById(R.id.gvInfo);// ��ȡ�����ļ��е�gvInfo���
        PictureAdapter adapter = new PictureAdapter(titles, images, this);// ����pictureAdapter����
        gvInfo.setAdapter(adapter);// ΪGridView��������Դ
        gvInfo.setOnItemClickListener(new OnItemClickListener() {// ΪGridView��������¼�
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = null;// ����Intent����
                switch (arg2) {
                case 0:
                	barmaintain.setVisibility(View.VISIBLE);                	
                	intent = new Intent(MaintainActivity.this, GoodsManager.class);// ʹ��GoodsManager���ڳ�ʼ��Intent
                	startActivityForResult(intent,REQUEST_CODE);// ��GoodsManager
                    break;
                case 1:
                	barmaintain.setVisibility(View.VISIBLE); 
                    intent = new Intent(MaintainActivity.this, HuodaoTest.class);// ʹ��HuodaoTest���ڳ�ʼ��Intent
                    startActivityForResult(intent,REQUEST_CODE);// ��HuodaoTest
                    break;
                case 2:
                	barmaintain.setVisibility(View.VISIBLE);
                	intent = new Intent(MaintainActivity.this, ParamManager.class);// ʹ��ParamManager���ڳ�ʼ��Intent
                    startActivityForResult(intent,REQUEST_CODE);// ��ParamManager                    
                    break;    
                case 3:
                	intent = new Intent(MaintainActivity.this, Order.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                    startActivity(intent);// ��Accountflag
                    break;                
                case 4:
                	intent = new Intent(MaintainActivity.this, LogOpt.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                    startActivity(intent);// ��Accountflag
                    break;
                case 5:
                	intent = new Intent(MaintainActivity.this, Login.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                	startActivityForResult(intent,REQUEST_CODE);// ��Accountflag
                    break;
                case 6:
                    intent = new Intent(MaintainActivity.this, Business.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                    startActivity(intent);// ��Accountflag
                    break;
                case 7:
                    finish();// �رյ�ǰActivity
                }
            }
        });

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO Auto-generated method stub
		if(requestCode==REQUEST_CODE)
		{
			if(resultCode==MaintainActivity.RESULT_CANCELED)
			{				
				barmaintain.setVisibility(View.GONE);
			}	
			else if(resultCode==MaintainActivity.RESULT_OK)
			{				
				//�������ļ���ȡ����
				Map<String, String> list=ToolClass.ReadConfigFile();
				if(list!=null)
				{
			        if(list.containsKey("isallopen"))
			        {
			        	isallopen=Integer.parseInt(list.get("isallopen"));	
			        	localService.setAllopen(isallopen);
			        }
				}
			}
		}
	}
	
	//=============
	//Server�������
	//=============
	//2.����EVServerReceiver�Ľ������㲥���������շ�����ͬ��������
	public class EVServerReceiver extends BroadcastReceiver 
	{

		@Override
		public void onReceive(Context context, Intent intent) 
		{
			// TODO Auto-generated method stub
			Bundle bundle=intent.getExtras();
			int EVWhat=bundle.getInt("EVWhat");
			switch(EVWhat)
			{
			case EVServerhttp.SETMAIN:
				Log.i("EV_JNI","activity=ǩ���ɹ�");
				issuc=true;
	    		break;
			case EVServerhttp.SETFAILMAIN:
				Log.i("EV_JNI","activity=ǩ��ʧ��");
				//issuc=false;
	    		break;	
			}	
		}

	}
	
	@Override
	protected void onDestroy() {
		EVprotocolAPI.vmcEVStop();//�رռ���
		//�رմ���
		if(comopen>0)	
			EVprotocolAPI.EV_portRelease(ToolClass.getCom_id());
		if(bentopen>0)
			EVprotocolAPI.EV_portRelease(ToolClass.getBentcom_id());
		EVprotocolAPI.vmcEVStop();//�رռ���
		//=============
		//Server�������
		//=============
		//5.���ע�������
		MaintainActivity.this.unregisterReceiver(receiver);
		//6.��������
		stopService(new Intent(MaintainActivity.this, EVServerService.class));
		// TODO Auto-generated method stub
		super.onDestroy();		
	}
}


