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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.easivend.evprotocol.EVprotocol;
import com.easivend.http.EVServerhttp;
import com.easivend.view.COMService;
import com.easivend.view.DogService;
import com.easivend.view.EVServerService;
import com.easivend.weixing.WeiConfigAPI;
import com.easivend.alipay.AlipayConfigAPI;
import com.easivend.app.business.BusLand;
import com.easivend.app.business.BusPort;
import com.easivend.common.PictureAdapter;
import com.easivend.common.SerializableMap;
import com.easivend.common.ToolClass;
import com.example.evconsole.R;


import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MaintainActivity extends Activity
{
	TextView txtcom=null,txtbentcom=null,txtcolumncom=null;
	private GridView gvInfo;// ����GridView����
	//��תҳ��Ի���
	private ProgressDialog barmaintain= null;
	//���ȶԻ���
	ProgressDialog dialog= null;
	// �����ַ������飬�洢ϵͳ����
    private String[] titles = new String[] { "��Ʒ����", "��������", "��������", "������Ϣ", "������־", "��������", "����ҳ��", "�˳�" };
    // ����int���飬�洢���ܶ�Ӧ��ͼ��
    private int[] images = new int[] { R.drawable.addoutaccount, R.drawable.addinaccount, R.drawable.outaccountinfo, R.drawable.showinfo,
            R.drawable.inaccountinfo, R.drawable.sysset, R.drawable.accountflag, R.drawable.exit };
    String com=null,bentcom=null,columncom=null,server="";
    final static int REQUEST_CODE=1;   
    //��ȡ������Ϣ
   Map<String,Integer> huoSet=new HashMap<String,Integer>();
    //Dog�������
    int isallopen=1;//�Ƿ񱣳ֳ���һֱ��,1һֱ��,0�رպ󲻴�
	private final int SPLASH_DISPLAY_LENGHT = 5000; // �ӳ�5��
	//LocalBroadcastManager dogBroadreceiver;
	//Server�������
	LocalBroadcastManager localBroadreceiver;
	EVServerReceiver receiver;
	private boolean issale=false;//true�Ƿ��Ѿ��Զ��򿪹�����ҳ���ˣ�����򿪹����Ͳ��ٴ���
	Map<String, String> vmcmap;
	//COM�������
	LocalBroadcastManager comBroadreceiver;
	COMReceiver comreceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintain);	
		//ȡ����Ļ�ĳ��Ϳ����бȽ����ú������ı���
		Display display = getWindowManager().getDefaultDisplay();  
		int width = display.getWidth();  
		int height = display.getHeight();  
		if (width > height) {  
			ToolClass.setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//��Ϊ����
		} else { 
			ToolClass.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//��Ϊ����
			//ToolClass.setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//��Ϊ����
		}
		//���ú������������Ĳ��ֲ���
		this.setRequestedOrientation(ToolClass.getOrientation());
				
		dialog= ProgressDialog.show(MaintainActivity.this,"ͬ��������","���Ժ�...");
				
		//==========
		//Dog�������
		//==========
		//��������
		startService(new Intent(this,DogService.class));
		//dogBroadreceiver.getInstance(this);
		//��ʱ5s
	    new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {      
            	//����ָ��㲥��DogService
        		Intent intent=new Intent();
        		intent.putExtra("isallopen", isallopen);
        		intent.setAction("android.intent.action.dogserversend");//action���������ͬ
        		//dogBroadreceiver.sendBroadcast(intent); 
        		sendBroadcast(intent); 
            }

		}, SPLASH_DISPLAY_LENGHT);
		
		
		
		//=============
		//Server�������
		//=============
		//3.��������
		startService(new Intent(MaintainActivity.this,EVServerService.class));
		//4.ע�������
		localBroadreceiver = LocalBroadcastManager.getInstance(this);
		receiver=new EVServerReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("android.intent.action.vmserverrec");
		localBroadreceiver.registerReceiver(receiver,filter);
		vmcmap = ToolClass.getvmc_no(MaintainActivity.this);
		
		//=============
		//COM�������
		//=============
		//3.��������
		startService(new Intent(MaintainActivity.this,COMService.class));
		//4.ע�������
		comBroadreceiver = LocalBroadcastManager.getInstance(this);
		comreceiver=new COMReceiver();
		IntentFilter comfilter=new IntentFilter();
		comfilter.addAction("android.intent.action.comrec");
		comBroadreceiver.registerReceiver(comreceiver,comfilter);
		//��ʱ3s
	    new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {      
    			//�򿪸��ӹ񴮿�
    			if(ToolClass.getBentcom().equals("")==false)
    			{
    				ToolClass.Log(ToolClass.INFO,"EV_COM","bentcomSend="+ToolClass.getBentcom(),"com.txt");
    				EVprotocol.EVPortRelease(ToolClass.getBentcom());
    				String bentcom = EVprotocol.EVPortRegister(ToolClass.getBentcom());
    				ToolClass.Log(ToolClass.INFO,"EV_COM","bentcom="+bentcom,"com.txt");
    				ToolClass.setBentcom_id(ToolClass.Resetportid(bentcom));
    			}
    			//�򿪵��ɹ񴮿�
    			if(ToolClass.getColumncom().equals("")==false)
    			{
    				ToolClass.Log(ToolClass.INFO,"EV_COM","columncomSend="+ToolClass.getColumncom(),"com.txt");
    				EVprotocol.EVPortRelease(ToolClass.getColumncom());
    				String columncom = EVprotocol.EVPortRegister(ToolClass.getColumncom());
    				ToolClass.Log(ToolClass.INFO,"EV_COM","columncom="+columncom,"com.txt");
    				ToolClass.setColumncom_id(ToolClass.Resetportid(columncom));
    			}
    			//���ֽ��豸����
    			if(ToolClass.getCom().equals("")==false) 
    			{
    				ToolClass.Log(ToolClass.INFO,"EV_COM","comSend="+ToolClass.getCom(),"com.txt");
    				EVprotocol.EVPortRelease(ToolClass.getCom());
    				String com = EVprotocol.EVPortRegister(ToolClass.getCom());
    				ToolClass.Log(ToolClass.INFO,"EV_COM","com="+com,"com.txt");
    				ToolClass.setCom_id(ToolClass.Resetportid(com));
    			}
            	//7.����ָ��㲥��COMService
        		Intent intent=new Intent();
        		intent.putExtra("EVWhat", COMService.EV_CHECKALLCHILD);	
        		intent.setAction("android.intent.action.comsend");//action���������ͬ
        		comBroadreceiver.sendBroadcast(intent);
            }

		}, 1000);
				
		//================
		//�������ú�ע�����
		//================
		txtcom=(TextView)super.findViewById(R.id.txtcom);
		txtbentcom=(TextView)super.findViewById(R.id.txtbentcom);
		txtcolumncom=(TextView)super.findViewById(R.id.txtcolumncom);
		ToolClass.SetDir();	
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<log·��:"+ToolClass.getEV_DIR()+File.separator+"logs","log.txt");			
		//�������ļ���ȡ����
		Map<String, String> list=ToolClass.ReadConfigFile();
		if(list!=null)
		{
	        com = list.get("com");
	        bentcom = list.get("bentcom");
	        columncom = list.get("columncom");
	        server = list.get("server");//���÷�����·��
	        AlipayConfigAPI.SetAliConfig(list);//���ð����˺�
	        WeiConfigAPI.SetWeiConfig(list);//����΢���˺�
	        if(list.containsKey("isallopen"))//�����Ƿ�һֱ�򿪳���
	        {
	        	isallopen=Integer.parseInt(list.get("isallopen"));		        	
	        }
	        ToolClass.setCom(com);
	        ToolClass.setBentcom(bentcom);
	        ToolClass.setColumncom(columncom);	

		}
		else
		{
			dialog.dismiss();
		}
		
		//����΢��֤��
		ToolClass.setWeiCertFile();
		//��������ˮӡͼƬ		
		Bitmap mark = BitmapFactory.decodeResource(this.getResources(), R.drawable.ysq);  
		ToolClass.setMark(mark);
		//����goc
		ToolClass.setGoc(MaintainActivity.this);	
		//================
		//�Ź������
		//================		
		gvInfo = (GridView) findViewById(R.id.gvInfo);// ��ȡ�����ļ��е�gvInfo���
        PictureAdapter adapter = new PictureAdapter(titles, images, this);// ����pictureAdapter����
        gvInfo.setAdapter(adapter);// ΪGridView��������Դ
        gvInfo.setOnItemClickListener(new OnItemClickListener() {// ΪGridView��������¼�
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = null;// ����Intent����
                switch (arg2) {
                case 0:
                	barmaintain= ProgressDialog.show(MaintainActivity.this,"����Ʒ����","���Ժ�...");
                	intent = new Intent(MaintainActivity.this, GoodsManager.class);// ʹ��GoodsManager���ڳ�ʼ��Intent
                	startActivityForResult(intent,REQUEST_CODE);// ��GoodsManager
                    break;
                case 1:
                	barmaintain= ProgressDialog.show(MaintainActivity.this,"�򿪻�������","���Ժ�...");
                    intent = new Intent(MaintainActivity.this, HuodaoTest.class);// ʹ��HuodaoTest���ڳ�ʼ��Intent
                    startActivityForResult(intent,REQUEST_CODE);// ��HuodaoTest
                    break;
                case 2:
                	barmaintain= ProgressDialog.show(MaintainActivity.this,"�򿪲�������","���Ժ�...");
                	intent = new Intent(MaintainActivity.this, ParamManager.class);// ʹ��ParamManager���ڳ�ʼ��Intent
                    startActivityForResult(intent,REQUEST_CODE);// ��ParamManager                    
                    break;    
                case 3:
                	barmaintain= ProgressDialog.show(MaintainActivity.this,"�򿪶�����־��ѯ","���Ժ�...");
                	intent = new Intent(MaintainActivity.this, Order.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                	startActivityForResult(intent,REQUEST_CODE);
                    break;                
                case 4:
                	barmaintain= ProgressDialog.show(MaintainActivity.this,"�򿪲�����־��ѯ","���Ժ�...");
                	intent = new Intent(MaintainActivity.this, LogOpt.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                	startActivityForResult(intent,REQUEST_CODE);
                    break;
                case 5:
                	barmaintain= ProgressDialog.show(MaintainActivity.this,"�򿪱�������","���Ժ�...");
                	intent = new Intent(MaintainActivity.this, Login.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                	startActivityForResult(intent,REQUEST_CODE);// ��Accountflag
                    break;
                case 6:
                	barmaintain= ProgressDialog.show(MaintainActivity.this,"�򿪽���ҳ��","���Ժ�...");
    				//����
    				if(ToolClass.getOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    				{
    					intent = new Intent(MaintainActivity.this, BusLand.class);// ʹ��Accountflag���ڳ�ʼ��Intent
    				}
    				//����
    				else
    				{
    					intent = new Intent(MaintainActivity.this, BusPort.class);// ʹ��Accountflag���ڳ�ʼ��Intent
    				}                	
                    startActivityForResult(intent,REQUEST_CODE);// ��Accountflag
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
				barmaintain.dismiss();
			}	
			else if(resultCode==MaintainActivity.RESULT_OK)
			{	
				barmaintain.dismiss();
				//�������ļ���ȡ����
				Map<String, String> list=ToolClass.ReadConfigFile();
				if(list!=null)
				{
			        if(list.containsKey("isallopen"))
			        {
			        	isallopen=Integer.parseInt(list.get("isallopen"));	
			        	//����ָ��㲥��DogService
		        		Intent intent=new Intent();
		        		intent.putExtra("isallopen", isallopen);
		        		intent.setAction("android.intent.action.dogserversend");//action���������ͬ
		        		//dogBroadreceiver.sendBroadcast(intent);
		        		sendBroadcast(intent); 
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
				dialog.dismiss();				
	    		break;
			case EVServerhttp.SETFAILMAIN:
				Log.i("EV_JNI","activity=ʧ�ܣ��������");
				if(dialog.isShowing())
					dialog.dismiss();
	    		break;	
			}
			if(issale==false)
			{
				issale=true;
				//ǩ����ɣ��Զ������ۻ�����
				barmaintain= ProgressDialog.show(MaintainActivity.this,"�򿪽���ҳ��","���Ժ�...");
				Intent intbus;
				//����
				if(ToolClass.getOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
				{
					intbus = new Intent(MaintainActivity.this, BusLand.class);// ʹ��Accountflag���ڳ�ʼ��Intent
				}
				//����
				else
				{
					intbus = new Intent(MaintainActivity.this, BusPort.class);// ʹ��Accountflag���ڳ�ʼ��Intent
				}
				startActivityForResult(intbus,REQUEST_CODE);// ��Accountflag
			}
		}

	}
	
	//=============
	//COM�������
	//=============	
	//2.����COMReceiver�Ľ������㲥���������շ�����ͬ��������
	public class COMReceiver extends BroadcastReceiver 
	{

		@Override
		public void onReceive(Context context, Intent intent) 
		{
			// TODO Auto-generated method stub
			Bundle bundle=intent.getExtras();
			int EVWhat=bundle.getInt("EVWhat");
			switch(EVWhat)
			{
			case COMService.EV_CHECKALLMAIN:
				//ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ������ѯȫ��","com.txt");
				SerializableMap serializableMap = (SerializableMap) bundle.get("result");
				Map<String, Integer> Set=serializableMap.getMap();
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ������ѯȫ��="+Set,"com.txt");	
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<vmserversend","log.txt");
		    	//*******
				//������ͬ��
				//*******
				Intent intent2=new Intent();
				intent2.putExtra("EVWhat", EVServerhttp.SETCHILD);
				intent2.putExtra("vmc_no", vmcmap.get("vmc_no"));
				intent2.putExtra("vmc_auth_code", vmcmap.get("vmc_auth_code"));
				//��������
		        final SerializableMap myMap=new SerializableMap();
		        myMap.setMap(Set);//��map������ӵ���װ��myMap<span></span>��
		        Bundle bundle2=new Bundle();
		        bundle2.putSerializable("huoSet", myMap);
		        intent2.putExtras(bundle2);
				intent2.setAction("android.intent.action.vmserversend");//action���������ͬ
				localBroadreceiver.sendBroadcast(intent2);  
	    		break;				
			}			
		}

	}
	
	@Override
	protected void onDestroy() {		
		//=============
		//Server�������
		//=============
		//5.���ע�������
		localBroadreceiver.unregisterReceiver(receiver);
		//6.��������
		stopService(new Intent(MaintainActivity.this, EVServerService.class));
		//=============
		//COM�������
		//=============
		//5.���ע�������
		comBroadreceiver.unregisterReceiver(comreceiver);
		//6.��������
		stopService(new Intent(MaintainActivity.this, COMService.class));
		// TODO Auto-generated method stub
		super.onDestroy();		
	}
}


