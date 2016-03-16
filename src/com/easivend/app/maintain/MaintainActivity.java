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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.easivend.dao.vmc_cabinetDAO;
import com.easivend.evprotocol.EVprotocol;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.http.EVServerhttp;
import com.easivend.model.Tb_vmc_cabinet;
import com.easivend.view.DogService;
import com.easivend.view.EVServerService;
import com.easivend.weixing.WeiConfigAPI;
import com.easivend.alipay.AlipayConfigAPI;
import com.easivend.app.business.BusLand;
import com.easivend.app.business.BusPort;
import com.easivend.common.PictureAdapter;
import com.easivend.common.ProPictureAdapter;
import com.easivend.common.SerializableMap;
import com.easivend.common.ToolClass;
import com.example.evconsole.R;


import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Display;
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
    //EVprotocolAPI ev=null;
    int comopen=0,bentopen=0,columnopen=0;//1�������ڴ򿪣�0����û�д�,-1���豸����ʧ��,2���ڴ��豸���    
    boolean columnchk=false;//true�����Ѿ����˻����Լ���
    String com=null,bentcom=null,columncom=null,server="";
    final static int REQUEST_CODE=1;   
    //��ȡ������Ϣ
    private String[] cabinetID = null;//���������������
    private int[] cabinetType=null;//��������
    private int huom = 0;// ����һ����ʼ��ʶ
    Map<String,Integer> huoSet=new HashMap<String,Integer>();
    //Dog�������
    int isallopen=1;//�Ƿ񱣳ֳ���һֱ��,1һֱ��,0�رպ󲻴�
	private final int SPLASH_DISPLAY_LENGHT = 5000; // �ӳ�5��
	//Server�������
	EVServerReceiver receiver;
	private int issuc=0;//0׼�����ڳ�ʼ����1���Կ�ʼǩ����2ǩ���ɹ�	
	private boolean issale=false;//true�Ƿ��Ѿ��Զ��򿪹�����ҳ���ˣ�����򿪹����Ͳ��ٴ���
	Map<String, String> vmcmap;
	ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
	
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
		
		
		//ע�ᴮ�ڼ�����
		EVprotocolAPI.setCallBack(new jniInterfaceImp());
		dialog= ProgressDialog.show(MaintainActivity.this,"ͬ��������","���Ժ�...");
				
		//==========
		//Dog�������
		//==========
		//��������
		startService(new Intent(this,DogService.class));
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
        		sendBroadcast(intent); 
            }

		}, SPLASH_DISPLAY_LENGHT);
		
		
		
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
		vmcmap = ToolClass.getvmc_no(MaintainActivity.this);		
		timer.scheduleWithFixedDelay(new Runnable() { 
	        @Override 
	        public void run() { 
	  
	            runOnUiThread(new Runnable() {      // UI thread 
	                @Override 
	                public void run() { 
	                	if(issuc==2) 
	                	{
	                		//�Ȳ�ѯ�豸��Ϣ�����ϱ���������
	                		EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
						}
	                } 
	            }); 
	        } 
	    }, 5, 1*60, TimeUnit.SECONDS);       // timeTask 10*60
		
		//================
		//�������ú�ע�����
		//================
		txtcom=(TextView)super.findViewById(R.id.txtcom);
		txtbentcom=(TextView)super.findViewById(R.id.txtbentcom);
		txtcolumncom=(TextView)super.findViewById(R.id.txtcolumncom);
		ToolClass.SetDir();		
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
			
	        //��������
	        EVprotocolAPI.vmcEVStart();//��������
	        //�ֽ�ģ��
			if(com.equals("")==true)
			{
				txtcom.setText(com+"[�ֽ�ģ��]����δ����");		
			}
			else 
			{
				//�����񴮿�		
				comopen = EVprotocolAPI.EV_portRegister(com);
				if(comopen == 1)
				{
					txtcom.setText(com+"[�ֽ�ģ��]��������׼������");			
				}
				else
				{
					txtcom.setText(com+"[�ֽ�ģ��]���ڴ�ʧ��");
				}	
			}
			//���ӹ�
			if(bentcom.equals("")==true)
			{
				txtbentcom.setText(bentcom+"[���ӹ�]����δ����");		
			}
			else
			{
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
			//����
			if(columncom.equals("")==true)
			{
				txtcolumncom.setText(columncom+"[����]����δ����");		
			}
			else
			{
				//�򿪸��ӹ�
				columnopen = EVprotocolAPI.EV_portRegister(columncom);
				if(columnopen == 1)
				{
					txtcolumncom.setText(columncom+"[����]��������׼������");			
				}
				else
				{
					txtcolumncom.setText(columncom+"[����]���ڴ�ʧ��");
				}
			}
			
			if((comopen!=1)&&(bentopen!=1)&&(columnopen!=1))
			{
				dialog.dismiss();
			}	
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
	
	//����һ��ר�Ŵ������ӿڵ�����
	private class jniInterfaceImp implements JNIInterface
	{
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
					//�ֽ�ģ���ʼ�����
					if(Set.get("port_com").equals(com))
					{
						ToolClass.setCom_id((Integer)Set.get("port_id"));
						if((Integer)Set.get("port_id")>=0)
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�ֽ�ģ���������","log.txt");	
							txtcom.setText(com+"[�ֽ�ģ��]�������");
							comopen=2;
						}
						else
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�ֽ�ģ������ʧ��","log.txt");	
							txtcom.setText(bentcom+"[�ֽ�ģ��]����ʧ��");	
							comopen=-1;
						}						
					}
					//���ӹ�׼������
					else if(Set.get("port_com").equals(bentcom))
					{
						ToolClass.setBentcom_id((Integer)Set.get("port_id"));
						//��ʼ��������Ϣ
						if((Integer)Set.get("port_id")>=0)
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<���ӹ�����...","log.txt");	
							txtbentcom.setText(bentcom+"[���ӹ�]�������");	
							getcolumnstat();
							break;
						}
						else
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<���ӹ�����ʧ��","log.txt");	
							txtbentcom.setText(bentcom+"[���ӹ�]����ʧ��");
							bentopen=-1;														
						}
					}
					//����׼������
					else if(Set.get("port_com").equals(columncom))
					{
						ToolClass.setColumncom_id((Integer)Set.get("port_id"));
						//��ʼ��������Ϣ
						if((Integer)Set.get("port_id")>=0)
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��������...","log.txt");	
							txtcolumncom.setText(columncom+"[����]�������");
							getcolumnstat();
							break;	
						}
						else
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��������ʧ��","log.txt");	
							txtcolumncom.setText(columncom+"[����]����ʧ��");
							columnopen=-1;							
						}
					}
					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ǩ��1","log.txt");	
					//�ж������󣬿��Կ�ʼǩ������							
					onInit();
					break;
				//�����ѯ	
				case EVprotocolAPI.EV_COLUMN_CHECK:	
					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<���ػ���״̬:","log.txt");	
					String tempno2=null;
				
					//�������
			        Set<Entry<String, Object>> allmap=Set.entrySet();  //ʵ����
			        Iterator<Entry<String, Object>> iter=allmap.iterator();
			        while(iter.hasNext())
			        {
			            Entry<String, Object> me=iter.next();
			            if(
			               (me.getKey().equals("EV_TYPE")!=true)
			            )   
			            {
			            	if(Integer.parseInt(me.getKey())<10)
			    				tempno2="0"+me.getKey();
			    			else 
			    				tempno2=me.getKey();
			            	
			            	huoSet.put(cabinetID[huom]+tempno2,(Integer)me.getValue());
			            }
			        } 
			        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+huoSet.size()+"����״̬:"+huoSet.toString(),"log.txt");	
			        huom++;
			        if(huom<cabinetID.length)
			        {
			        	//2.��ȡ���л�����
			    	    queryhuodao(Integer.parseInt(cabinetID[huom]),cabinetType[huom]);
			        }
			        else
			        {
			        	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<���ӹ������������","log.txt");	
			        	columnopen=2;
			        	bentopen=2;
			        	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ǩ��2","log.txt");	
						//���Կ�ʼǩ������
			        	onInit();
					}
					break;
				//���ӹ��ѯ	
				case EVprotocolAPI.EV_BENTO_CHECK:
					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<���ػ���״̬:","log.txt");	
					String tempno=null;
				
					//�������
			        Set<Entry<String, Object>> allmap2=Set.entrySet();  //ʵ����
			        Iterator<Entry<String, Object>> iter2=allmap2.iterator();
			        while(iter2.hasNext())
			        {
			            Entry<String, Object> me=iter2.next();
			            if(
			               (me.getKey().equals("EV_TYPE")!=true)&&(me.getKey().equals("cool")!=true)
			               &&(me.getKey().equals("hot")!=true)&&(me.getKey().equals("light")!=true)
			            )   
			            {
			            	if(Integer.parseInt(me.getKey())<10)
			    				tempno="0"+me.getKey();
			    			else 
			    				tempno=me.getKey();
			            	
			            	huoSet.put(cabinetID[huom]+tempno,(Integer)me.getValue());
			            }
			        } 
			        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+huoSet.size()+"����״̬:"+huoSet.toString(),"log.txt");	
			        huom++;
			        if(huom<cabinetID.length)
			        {
			        	//2.��ȡ���л�����
			    	    queryhuodao(Integer.parseInt(cabinetID[huom]),cabinetType[huom]);
			        }
			        else
			        {
			        	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<���ӹ������������","log.txt");	
			        	columnopen=2;
			        	bentopen=2;
			        	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ǩ��3","log.txt");	
						//���Կ�ʼǩ������
			        	onInit();
					}
					break;	
				//�ֽ��豸״̬��ѯ
				case EVprotocolAPI.EV_MDB_HEART://������ѯ
					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�ֽ��豸״̬:","log.txt");	
					
					int bill_err=ToolClass.getvmcStatus(Set,1);
					int coin_err=ToolClass.getvmcStatus(Set,2);
					//�ϱ���������
					Intent intent=new Intent();
    				intent.putExtra("EVWhat", EVServerhttp.SETDEVSTATUCHILD);
    				intent.putExtra("bill_err", bill_err);
    				intent.putExtra("coin_err", coin_err);
    				intent.setAction("android.intent.action.vmserversend");//action���������ͬ
    				sendBroadcast(intent); 
					break; 	
			}
		}
	}
	
	private void onInit()
	{	
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<comopen="+comopen+"bentopen="+bentopen+"columnopen="+columnopen,"log.txt");	
		if((comopen!=1)&&(bentopen!=1)&&(columnopen!=1)) 
		{
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<vmserversend","log.txt");
	    	Intent intent=new Intent();
			intent.putExtra("EVWhat", EVServerhttp.SETCHILD);
			intent.putExtra("vmc_no", vmcmap.get("vmc_no"));
			intent.putExtra("vmc_auth_code", vmcmap.get("vmc_auth_code"));
			//��������
	        final SerializableMap myMap=new SerializableMap();
	        myMap.setMap(huoSet);//��map������ӵ���װ��myMap<span></span>��
	        Bundle bundle=new Bundle();
	        bundle.putSerializable("huoSet", myMap);
	        intent.putExtras(bundle);
			intent.setAction("android.intent.action.vmserversend");//action���������ͬ
			sendBroadcast(intent);  
		}
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
		        		sendBroadcast(intent);
			        }
				}
			}
		}
		//ע�ᴮ�ڼ�����
		EVprotocolAPI.setCallBack(new jniInterfaceImp());	
	}
	
	//=============
	//�������
	//=============
	//��ȡ��ǰ������Ϣ
	private void getcolumnstat()
	{	
		if(columnchk==false)
		{
			columnchk=true;
			vmc_cabinetDAO cabinetDAO = new vmc_cabinetDAO(MaintainActivity.this);// ����InaccountDAO����
		    // 1.��ȡ���й��
		    List<Tb_vmc_cabinet> listinfos = cabinetDAO.getScrollData();
		    cabinetID = new String[listinfos.size()];// �����ַ�������ĳ���
		    cabinetType=new int[listinfos.size()];// �����ַ�������ĳ���	    
		    // ����List���ͼ���
		    for (Tb_vmc_cabinet tb_inaccount : listinfos) 
		    {
		        cabinetID[huom] = tb_inaccount.getCabID();
		        cabinetType[huom]= tb_inaccount.getCabType();
		        ToolClass.Log(ToolClass.INFO,"EV_JNI","��ȡ���="+cabinetID[huom]+"����="+cabinetType[huom],"log.txt");
			    huom++;// ��ʶ��1
		    }
		    huom=0;
		    if(listinfos.size()>0)
		    {
			    //2.��ȡ���л�����
			    queryhuodao(Integer.parseInt(cabinetID[huom]),cabinetType[huom]);
		    }
		    else 
		    {
		    	bentopen=-1;
		    	columnopen=-1;
		    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<���ӹ���������ʧ��","log.txt");	
		    	txtbentcom.setText(bentcom+"[���ӹ�]����ʧ��");
		    	txtcolumncom.setText(columncom+"[����]����ʧ��");
		    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ǩ��4","log.txt");	
		    	//���Կ�ʼǩ������
		    	onInit();
			}
		}
	}
	
	//��ȡ�������л�����
	private void queryhuodao(int cabinetsetvar,int cabinetTypesetvar)
	{
		//���ӹ�
		if(cabinetTypesetvar==5)
		{
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<huodao���ӹ��ѯ","log.txt");
			EVprotocolAPI.EV_bentoCheck(ToolClass.getBentcom_id(),cabinetsetvar);
		}
		//��ͨ��
		else 
		{
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<huodao��ͨ���ѯ","log.txt");
			EVprotocolAPI.getColumn(ToolClass.getColumncom_id(),1,1);
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
				issuc=2;	
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
	
	@Override
	protected void onDestroy() {
		EVprotocolAPI.vmcEVStop();//�رռ���
		//�رմ���
		if(comopen!=0)	
			EVprotocolAPI.EV_portRelease(ToolClass.getCom_id());
		if(bentopen!=0)
			EVprotocolAPI.EV_portRelease(ToolClass.getBentcom_id());
		if(columnopen!=0)
			EVprotocolAPI.EV_portRelease(ToolClass.getColumncom_id());
		EVprotocolAPI.vmcEVStop();//�رռ���		
		timer.shutdown(); //�رն�ʱ��
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


