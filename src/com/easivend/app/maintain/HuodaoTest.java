/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           HuodaoTest.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        ��������ҳ��          
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.app.maintain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.easivend.dao.vmc_cabinetDAO;
import com.easivend.dao.vmc_classDAO;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.http.EVServerhttp;
import com.easivend.model.Tb_vmc_cabinet;
import com.easivend.model.Tb_vmc_class;
import com.easivend.model.Tb_vmc_column;
import com.easivend.common.HuoPictureAdapter;
import com.easivend.common.ProPictureAdapter;
import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_CabinetAdapter;
import com.easivend.common.Vmc_ClassAdapter;
import com.easivend.common.Vmc_HuoAdapter;
import com.easivend.common.Vmc_ProductAdapter;
import com.example.evconsole.R;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SlidingDrawer;
import android.widget.Switch;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class HuodaoTest extends TabActivity 
{
	private TabHost mytabhost = null;
	private ProgressBar barhuomanager=null;
	private int[] layres=new int[]{R.id.tab_huodaomanager,R.id.tab_huodaotest,R.id.tab_huodaoset};//��Ƕ�����ļ���id
	private TextView txthuosetrst=null;
	private int con=1;//��ѯ���Ӵ���
	private int ishuoquery=0;//�Ƿ����ڲ�ѯ1,���ڲ�ѯ,0��ѯ���
	Timer timer = new Timer(); 
	private Button btnhuosetadd=null,btnhuosetdel=null,btnhuosetbu=null,btnhuosetexit=null;
	private Spinner spinhuosetCab=null,spinhuotestCab=null,spinhuopeiCab=null;
	private String[] cabinetID=null;//���������������
	private int[] cabinetType = null;//�����������������
	private int cabinetsetvar=0,cabinetTypesetvar=0;
	Map<String, Integer> huoSet= new LinkedHashMap<String,Integer>();
	private int huonum=0;//�����������
	private int huonno=0;//ѭ�������ڼ���������
	// ��������б�
	Vmc_HuoAdapter huoAdapter=null;
	GridView gvhuodao=null;
	private final static int REQUEST_CODE=1;//���������ʶ
	
	private int device=0;//�������		
	private int status=0;//�������
	private int hdid=0;//����id
	private int cool=0;//�Ƿ�֧������ 	 	1:֧�� 0:��֧��
	private int hot=0;//�Ƿ�֧�ּ���  		1:֧�� 0:��֧��
	private int light=0;//�Ƿ�֧������  	1:֧�� 0:��֧��
	private TextView txthuorst=null,txthuotestrst=null;
	private Button btnhuochu=null,btnhuochuall=null;// ����Button���󡰳�����
	private Button btnhuocancel=null;// ����Button�������á�
	private Button btnhuoexit=null;// ����Button�����˳���
	private EditText edtcolumn=null;
	private TextView txtlight=null,txtcold=null,txthot=null;
	private Switch switchlight = null,switcold = null,switchhot = null;
	private int cabinetvar=0,cabinetTypevar=0;
	//��������ҳ��
	private int cabinetpeivar=0,cabinetTypepeivar=0;
	private Switch btnhuosetc1=null,btnhuosetc2=null,btnhuosetc3=null,btnhuosetc4=null,
			btnhuosetc5=null,btnhuosetc6=null,btnhuosetc7=null,btnhuosetc8=null,
			btnhuoset11=null,btnhuoset12=null,btnhuoset13=null,btnhuoset14=null,btnhuoset15=null,
			btnhuoset16=null,btnhuoset17=null,btnhuoset18=null,btnhuoset19=null,btnhuoset110=null,
			btnhuoset21=null,btnhuoset22=null,btnhuoset23=null,btnhuoset24=null,btnhuoset25=null,
			btnhuoset26=null,btnhuoset27=null,btnhuoset28=null,btnhuoset29=null,btnhuoset210=null,
			btnhuoset31=null,btnhuoset32=null,btnhuoset33=null,btnhuoset34=null,btnhuoset35=null,
			btnhuoset36=null,btnhuoset37=null,btnhuoset38=null,btnhuoset39=null,btnhuoset310=null,
			btnhuoset41=null,btnhuoset42=null,btnhuoset43=null,btnhuoset44=null,btnhuoset45=null,
			btnhuoset46=null,btnhuoset47=null,btnhuoset48=null,btnhuoset49=null,btnhuoset410=null,
			btnhuoset51=null,btnhuoset52=null,btnhuoset53=null,btnhuoset54=null,btnhuoset55=null,
			btnhuoset56=null,btnhuoset57=null,btnhuoset58=null,btnhuoset59=null,btnhuoset510=null,
			btnhuoset61=null,btnhuoset62=null,btnhuoset63=null,btnhuoset64=null,btnhuoset65=null,
			btnhuoset66=null,btnhuoset67=null,btnhuoset68=null,btnhuoset69=null,btnhuoset610=null,
			btnhuoset71=null,btnhuoset72=null,btnhuoset73=null,btnhuoset74=null,btnhuoset75=null,
			btnhuoset76=null,btnhuoset77=null,btnhuoset78=null,btnhuoset79=null,btnhuoset710=null,
			btnhuoset81=null,btnhuoset82=null,btnhuoset83=null,btnhuoset84=null,btnhuoset85=null,
			btnhuoset86=null,btnhuoset87=null,btnhuoset88=null,btnhuoset89=null,btnhuoset810=null;
	private Button btnhuosetsethuo=null,btnhuosetclose=null;							
			
	private Handler myhHandler=null;
	//EVprotocolAPI ev=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.huodao);// ���ò����ļ�
		//���ú������������Ĳ��ֲ���
		this.setRequestedOrientation(ToolClass.getOrientation());
		this.mytabhost = super.getTabHost();//ȡ��TabHost����
        LayoutInflater.from(this).inflate(R.layout.huodao, this.mytabhost.getTabContentView(),true);
        //����Tab�����
        TabSpec myTabhuodaomana=this.mytabhost.newTabSpec("tab0");
        myTabhuodaomana.setIndicator("��������");
        myTabhuodaomana.setContent(this.layres[0]);
    	this.mytabhost.addTab(myTabhuodaomana); 
    	
    	TabSpec myTabhuodaotest=this.mytabhost.newTabSpec("tab1");
    	myTabhuodaotest.setIndicator("��������");
    	myTabhuodaotest.setContent(this.layres[1]);
    	this.mytabhost.addTab(myTabhuodaotest);
    	
    	TabSpec myTabhuodaoset=this.mytabhost.newTabSpec("tab2");
    	myTabhuodaoset.setIndicator("��������");
    	myTabhuodaoset.setContent(this.layres[2]);
    	this.mytabhost.addTab(myTabhuodaoset); 
    	
    	//ע�����������
  	    EVprotocolAPI.setCallBack(new JNIInterface() {
			
			@Override
			public void jniCallback(Map<String, Object> allSet) {
				// TODO Auto-generated method stub
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<huodao�������","log.txt");
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
						ishuoquery=0;
						String tempno=null;
						
						cool=(Integer)Set.get("cool");
						hot=(Integer)Set.get("hot");
						light=(Integer)Set.get("light");
						ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����cool:"+cool+",hot="+hot+",light="+light,"log.txt");
						if(light>0)
						{
							txtlight.setText("֧��");
							switchlight.setEnabled(true);
							
						}
						else
						{
							txtlight.setText("��֧��");
							switchlight.setEnabled(false);
						}
						if(cool>0)
						{
							txtcold.setText("֧��");
							switcold.setEnabled(true);
						}
						else
						{
							txtcold.setText("��֧��");
							switcold.setEnabled(false);
						}
						if(hot>0)
						{
							txthot.setText("֧��");
							switchhot.setEnabled(true);
						}
						else
						{
							txthot.setText("��֧��");
							switchhot.setEnabled(false);
						}
						
						huoSet.clear();
						//�������
				        Set<Entry<String, Object>> allmap=Set.entrySet();  //ʵ����
				        Iterator<Entry<String, Object>> iter=allmap.iterator();
				        while(iter.hasNext())
				        {
				            Entry<String, Object> me=iter.next();
				            if(
				               (me.getKey().equals("EV_TYPE")!=true)&&(me.getKey().equals("cool")!=true)
				               &&(me.getKey().equals("hot")!=true)&&(me.getKey().equals("light")!=true)
				            )   
				            {
				            	if(Integer.parseInt(me.getKey())<10)
				    				tempno="0"+me.getKey();
				    			else 
				    				tempno=me.getKey();
				            	
				            	huoSet.put(tempno, (Integer)me.getValue());
				            }
				        } 
				        huonum=huoSet.size();
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+huonum+"����״̬:"+huoSet.toString(),"log.txt");	
						showhuodao();						
						break;	
					case EVprotocolAPI.EV_BENTO_OPEN://���ӹ����
						device=(Integer)allSet.get("addr");//�������						
						hdid=(Integer)allSet.get("box");//����id
						status=(Integer)allSet.get("result");//�������
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�������"+"device=["+device+"],hdid=["+hdid+"],status=["+status+"]","log.txt");	
						
						txthuorst.setText("device=["+device+"],hdid=["+hdid+"],status=["+status+"]");
						sethuorst(status);
						//ѭ����������������
						if((huonno>0)&&(huonno<huonum))
						{							
							huonno++;
							//���ӹ�
							if(cabinetTypevar==5)
							{
								ToolClass.Log(ToolClass.INFO,"EV_JNI",
								    	"[APPsend>>]cabinet="+String.valueOf(cabinetvar)
								    	+" cabType="+String.valueOf(cabinetTypevar)
								    	+" column="+huonno		    	
								    	,"log.txt");	 
								EVprotocolAPI.EV_bentoOpen(ToolClass.getBentcom_id(),cabinetvar,huonno);						
							}
						}
						else if(huonno>=huonum)
						{
							huonno=0;
						}
						break;	
					case EVprotocolAPI.EV_BENTO_LIGHT://���ӹ񿪵�
						device=(Integer)allSet.get("addr");//���						
						int opt=(Integer)allSet.get("opt");//����id
						status=(Integer)allSet.get("result");//���
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����������"+"device=["+device+"],opt=["+opt+"],status=["+status+"]","log.txt");	
						txthuorst.setText("device=["+device+"],opt=["+opt+"],status=["+status+"]");						
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
		}); 
    	//===============
    	//��������ҳ��
    	//===============
  	    timer.schedule(task, 3*1000, 3*1000);       // timeTask 
  	    txthuosetrst=(TextView)findViewById(R.id.txthuosetrst);
  	    barhuomanager= (ProgressBar) findViewById(R.id.barhuomanager);
  	    huoAdapter=new Vmc_HuoAdapter();
  	    this.gvhuodao=(GridView) findViewById(R.id.gvhuodao); 
    	spinhuosetCab= (Spinner) findViewById(R.id.spinhuosetCab); 
    	spinhuotestCab= (Spinner) findViewById(R.id.spinhuotestCab); 
    	spinhuopeiCab= (Spinner) findViewById(R.id.spinhuopeiCab); 
    	//��ʾ����Ϣ
    	showabinet();
    	this.spinhuosetCab.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//ֻ���й�ŵ�ʱ�򣬲�������ع��ڻ�����Ϣ
				if(cabinetID!=null)
				{
					barhuomanager.setVisibility(View.VISIBLE); 
					cabinetsetvar=Integer.parseInt(cabinetID[arg2]); 
					cabinetTypesetvar=cabinetType[arg2]; 
					queryhuodao();					
				}				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	//�޸Ļ���ӻ�����Ӧ��Ʒ
    	gvhuodao.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub cabinetID[0],
				String huo[]=huoAdapter.getHuoID();
				String huoID = huo[arg2];// ��¼������Ϣ               
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����ID="+cabinetsetvar+huoID+"status="+huoSet.get(huoID),"log.txt");
				Intent intent = new Intent();
		    	intent.setClass(HuodaoTest.this, HuodaoSet.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
                intent.putExtra("huoID", huoID);
                intent.putExtra("cabID", String.valueOf(cabinetsetvar));
                intent.putExtra("huoStatus", String.valueOf(huoSet.get(huoID)));
		    	startActivityForResult(intent, REQUEST_CODE);// ��AddInaccount	
			}// ΪGridView��������¼�
    		
    	});
    	//��ӹ�
    	btnhuosetadd = (Button) findViewById(R.id.btnhuosetadd);
    	btnhuosetadd.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	cabinetAdd();
		    }
		});
    	//ɾ����
    	btnhuosetdel = (Button) findViewById(R.id.btnhuosetdel);
    	btnhuosetdel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	cabinetDel();
		    }
		});
    	//���񲹻�
    	btnhuosetbu = (Button) findViewById(R.id.btnhuosetbu);
    	btnhuosetbu.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	cabinetbuhuo();
		    }
		});
    	btnhuosetexit = (Button) findViewById(R.id.btnhuosetexit);
    	btnhuosetexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	timer.cancel();
		    	finish();
		    }
		});    	
    	
    	//��̬���ÿؼ��߶�
    	//
    	DisplayMetrics  dm = new DisplayMetrics();  
        //ȡ�ô�������  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        //���ڵĿ��  
        int screenWidth = dm.widthPixels;          
        //���ڸ߶�  
        int screenHeight = dm.heightPixels;      
        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ļ"+screenWidth
				+"],["+screenHeight+"]","log.txt");	
		
    	LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) gvhuodao.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
    	linearParams.height =  screenHeight-500;// ���ؼ��ĸ�ǿ�����75����
    	gvhuodao.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
  	   
    	//===============
    	//��������ҳ��
    	//===============
		
    	//spinhuoCab= (Spinner) findViewById(R.id.spinhuoCab); 
		txthuorst=(TextView)findViewById(R.id.txthuorst);
		txthuotestrst=(TextView)findViewById(R.id.txthuotestrst);
		btnhuochu = (Button) findViewById(R.id.btnhuochu);
		btnhuochuall = (Button) findViewById(R.id.btnhuochuall);
		btnhuocancel = (Button) findViewById(R.id.btnhuocancel);
		btnhuoexit = (Button) findViewById(R.id.btnhuoexit);
		edtcolumn = (EditText) findViewById(R.id.edtcolumn);
		txtlight = (TextView) findViewById(R.id.txtlight);		
		txtcold = (TextView) findViewById(R.id.txtcold);
		txthot = (TextView) findViewById(R.id.txthot);	
		switchlight = (Switch)findViewById(R.id.switchlight);
		switchlight.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					//���ӹ�
					if(cabinetTypevar==5)
					{
						EVprotocolAPI.EV_bentoLight(ToolClass.getBentcom_id(),cabinetvar,1);						
					}
					//��ͨ��
					else 
					{
						//rst=EVprotocolAPI.trade(cabinetvar,Integer.parseInt(edtcolumn.getText().toString()),typevar,
				    	//		ToolClass.MoneySend(price));	
					}
				}
				else 
				{
					//���ӹ�
					if(cabinetTypevar==5)
					{
						EVprotocolAPI.EV_bentoLight(ToolClass.getBentcom_id(),cabinetvar,0);						
					}
					//��ͨ��
					else 
					{
						//rst=EVprotocolAPI.trade(cabinetvar,Integer.parseInt(edtcolumn.getText().toString()),typevar,
				    	//		ToolClass.MoneySend(price));	
					}
					
				}
			}  
			
			
		}); 
		
		switcold = (Switch)findViewById(R.id.switcold);
		switcold.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
			}  
			
			
		});
		switchhot = (Switch)findViewById(R.id.switchhot);
		switchhot.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
			}  
			
			
		});
		this.spinhuotestCab.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//ֻ���й�ŵ�ʱ�򣬲�������ع��ڻ�����Ϣ
				if(cabinetID!=null)
				{
					cabinetvar=Integer.parseInt(cabinetID[arg2]); 
					cabinetTypevar=cabinetType[arg2]; 
				}				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		btnhuochu.setOnClickListener(new OnClickListener() {// Ϊ������ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {		    	  
		    	ToolClass.Log(ToolClass.INFO,"EV_JNI",
		    	"[APPsend>>]cabinet="+String.valueOf(cabinetvar)
		    	+" cabType="+String.valueOf(cabinetTypevar)
		    	+" column="+String.valueOf(Integer.parseInt(edtcolumn.getText().toString())),"log.txt"		    	
		    	);
		    	if (edtcolumn.getText().toString().isEmpty()!=true)	
		    	{
		    		float price=0;
		    		int typevar=0;
		    		if(price>0)
		    			typevar=0;
		    		else 
		    			typevar=2;
		    		int rst=0;
		    		//���ӹ�
					if(cabinetTypevar==5)
					{
						EVprotocolAPI.EV_bentoOpen(ToolClass.getBentcom_id(),cabinetvar,Integer.parseInt(edtcolumn.getText().toString()));						
					}
					//��ͨ��
					else 
					{
						rst=EVprotocolAPI.trade(cabinetvar,Integer.parseInt(edtcolumn.getText().toString()),typevar,
				    			ToolClass.MoneySend(price));	
					}
			    	   	
		    	}
		    }
		});
		btnhuochuall.setOnClickListener(new OnClickListener() {// Ϊ������ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {		    	  
		    	   		    		
	    		int rst=0;
	    		//���ӹ�
				if(cabinetTypevar==5)
				{
					huonno=1;
					ToolClass.Log(ToolClass.INFO,"EV_JNI",
					    	"[APPsend>>]cabinet="+String.valueOf(cabinetvar)
					    	+" cabType="+String.valueOf(cabinetTypevar)
					    	+" column="+huonno,"log.txt"		    	
					    	);	 
					EVprotocolAPI.EV_bentoOpen(ToolClass.getBentcom_id(),cabinetvar,huonno);						
				}
				//��ͨ��
				else 
				{
//					rst=EVprotocolAPI.trade(cabinetvar,Integer.parseInt(edtcolumn.getText().toString()),typevar,
//			    			ToolClass.MoneySend(price));	
				}
		    }
		});
		btnhuocancel.setOnClickListener(new OnClickListener() {// Ϊ���ð�ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	edtcolumn.setText("");// ���ý���ı���Ϊ��		    	      
		    }
		});
		btnhuoexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	timer.cancel();
		    	finish();
		    }
		});
		
		//===============
    	//��������ҳ��
    	//===============		
		btnhuosetc1 = (Switch) findViewById(R.id.btnhuosetc1);
		btnhuosetc2 = (Switch) findViewById(R.id.btnhuosetc2);
		btnhuosetc3 = (Switch) findViewById(R.id.btnhuosetc3);
		btnhuosetc4 = (Switch) findViewById(R.id.btnhuosetc4);
		btnhuosetc5 = (Switch) findViewById(R.id.btnhuosetc5);
		btnhuosetc6 = (Switch) findViewById(R.id.btnhuosetc6);
		btnhuosetc7 = (Switch) findViewById(R.id.btnhuosetc7);
		btnhuosetc8 = (Switch) findViewById(R.id.btnhuosetc8);		
		btnhuoset11 = (Switch) findViewById(R.id.btnhuoset11);
		btnhuoset12 = (Switch) findViewById(R.id.btnhuoset12);
		btnhuoset13 = (Switch) findViewById(R.id.btnhuoset13);
		btnhuoset14 = (Switch) findViewById(R.id.btnhuoset14);
		btnhuoset15 = (Switch) findViewById(R.id.btnhuoset15);
		btnhuoset16 = (Switch) findViewById(R.id.btnhuoset16);
		btnhuoset17 = (Switch) findViewById(R.id.btnhuoset17);
		btnhuoset18 = (Switch) findViewById(R.id.btnhuoset18);
		btnhuoset19 = (Switch) findViewById(R.id.btnhuoset19);
		btnhuoset110 = (Switch) findViewById(R.id.btnhuoset110);
		btnhuoset21 = (Switch) findViewById(R.id.btnhuoset21);
		btnhuoset22 = (Switch) findViewById(R.id.btnhuoset22);
		btnhuoset23 = (Switch) findViewById(R.id.btnhuoset23);
		btnhuoset24 = (Switch) findViewById(R.id.btnhuoset24);
		btnhuoset25 = (Switch) findViewById(R.id.btnhuoset25);
		btnhuoset26 = (Switch) findViewById(R.id.btnhuoset26);
		btnhuoset27 = (Switch) findViewById(R.id.btnhuoset27);
		btnhuoset28 = (Switch) findViewById(R.id.btnhuoset28);
		btnhuoset29 = (Switch) findViewById(R.id.btnhuoset29);
		btnhuoset210 = (Switch) findViewById(R.id.btnhuoset210);
		btnhuoset31 = (Switch) findViewById(R.id.btnhuoset31);
		btnhuoset32 = (Switch) findViewById(R.id.btnhuoset32);
		btnhuoset33 = (Switch) findViewById(R.id.btnhuoset33);
		btnhuoset34 = (Switch) findViewById(R.id.btnhuoset34);
		btnhuoset35 = (Switch) findViewById(R.id.btnhuoset35);
		btnhuoset36 = (Switch) findViewById(R.id.btnhuoset36);
		btnhuoset37 = (Switch) findViewById(R.id.btnhuoset37);
		btnhuoset38 = (Switch) findViewById(R.id.btnhuoset38);
		btnhuoset39 = (Switch) findViewById(R.id.btnhuoset39);
		btnhuoset310 = (Switch) findViewById(R.id.btnhuoset310);
		btnhuoset41 = (Switch) findViewById(R.id.btnhuoset41);
		btnhuoset42 = (Switch) findViewById(R.id.btnhuoset42);
		btnhuoset43 = (Switch) findViewById(R.id.btnhuoset43);
		btnhuoset44 = (Switch) findViewById(R.id.btnhuoset44);
		btnhuoset45 = (Switch) findViewById(R.id.btnhuoset45);
		btnhuoset46 = (Switch) findViewById(R.id.btnhuoset46);
		btnhuoset47 = (Switch) findViewById(R.id.btnhuoset47);
		btnhuoset48 = (Switch) findViewById(R.id.btnhuoset48);
		btnhuoset49 = (Switch) findViewById(R.id.btnhuoset49);
		btnhuoset410 = (Switch) findViewById(R.id.btnhuoset410);
		btnhuoset51 = (Switch) findViewById(R.id.btnhuoset51);
		btnhuoset52 = (Switch) findViewById(R.id.btnhuoset52);
		btnhuoset53 = (Switch) findViewById(R.id.btnhuoset53);
		btnhuoset54 = (Switch) findViewById(R.id.btnhuoset54);
		btnhuoset55 = (Switch) findViewById(R.id.btnhuoset55);
		btnhuoset56 = (Switch) findViewById(R.id.btnhuoset56);
		btnhuoset57 = (Switch) findViewById(R.id.btnhuoset57);
		btnhuoset58 = (Switch) findViewById(R.id.btnhuoset58);
		btnhuoset59 = (Switch) findViewById(R.id.btnhuoset59);
		btnhuoset510 = (Switch) findViewById(R.id.btnhuoset510);
		btnhuoset61 = (Switch) findViewById(R.id.btnhuoset61);
		btnhuoset62 = (Switch) findViewById(R.id.btnhuoset62);
		btnhuoset63 = (Switch) findViewById(R.id.btnhuoset63);
		btnhuoset64 = (Switch) findViewById(R.id.btnhuoset64);
		btnhuoset65 = (Switch) findViewById(R.id.btnhuoset65);
		btnhuoset66 = (Switch) findViewById(R.id.btnhuoset66);
		btnhuoset67 = (Switch) findViewById(R.id.btnhuoset67);
		btnhuoset68 = (Switch) findViewById(R.id.btnhuoset68);
		btnhuoset69 = (Switch) findViewById(R.id.btnhuoset69);
		btnhuoset610 = (Switch) findViewById(R.id.btnhuoset610);
		btnhuoset71 = (Switch) findViewById(R.id.btnhuoset71);
		btnhuoset72 = (Switch) findViewById(R.id.btnhuoset72);
		btnhuoset73 = (Switch) findViewById(R.id.btnhuoset73);
		btnhuoset74 = (Switch) findViewById(R.id.btnhuoset74);
		btnhuoset75 = (Switch) findViewById(R.id.btnhuoset75);
		btnhuoset76 = (Switch) findViewById(R.id.btnhuoset76);
		btnhuoset77 = (Switch) findViewById(R.id.btnhuoset77);
		btnhuoset78 = (Switch) findViewById(R.id.btnhuoset78);
		btnhuoset79 = (Switch) findViewById(R.id.btnhuoset79);
		btnhuoset710 = (Switch) findViewById(R.id.btnhuoset710);
		btnhuoset81 = (Switch) findViewById(R.id.btnhuoset81);
		btnhuoset82 = (Switch) findViewById(R.id.btnhuoset82);
		btnhuoset83 = (Switch) findViewById(R.id.btnhuoset83);
		btnhuoset84 = (Switch) findViewById(R.id.btnhuoset84);
		btnhuoset85 = (Switch) findViewById(R.id.btnhuoset85);
		btnhuoset86 = (Switch) findViewById(R.id.btnhuoset86);
		btnhuoset87 = (Switch) findViewById(R.id.btnhuoset87);
		btnhuoset88 = (Switch) findViewById(R.id.btnhuoset88);
		btnhuoset89 = (Switch) findViewById(R.id.btnhuoset89);
		btnhuoset810 = (Switch) findViewById(R.id.btnhuoset810);
		
		btnhuosetc1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btnhuoset11.setChecked(isChecked);
				btnhuoset12.setChecked(isChecked);
				btnhuoset13.setChecked(isChecked);
				btnhuoset14.setChecked(isChecked);
				btnhuoset15.setChecked(isChecked);
				btnhuoset16.setChecked(isChecked);
				btnhuoset17.setChecked(isChecked);
				btnhuoset18.setChecked(isChecked);
				btnhuoset19.setChecked(isChecked);
				btnhuoset110.setChecked(isChecked);
			} 
        });
		btnhuosetc2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btnhuoset21.setChecked(isChecked);
				btnhuoset22.setChecked(isChecked);
				btnhuoset23.setChecked(isChecked);
				btnhuoset24.setChecked(isChecked);
				btnhuoset25.setChecked(isChecked);
				btnhuoset26.setChecked(isChecked);
				btnhuoset27.setChecked(isChecked);
				btnhuoset28.setChecked(isChecked);
				btnhuoset29.setChecked(isChecked);
				btnhuoset210.setChecked(isChecked);
			} 
        });
		btnhuosetc3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btnhuoset31.setChecked(isChecked);
				btnhuoset32.setChecked(isChecked);
				btnhuoset33.setChecked(isChecked);
				btnhuoset34.setChecked(isChecked);
				btnhuoset35.setChecked(isChecked);
				btnhuoset36.setChecked(isChecked);
				btnhuoset37.setChecked(isChecked);
				btnhuoset38.setChecked(isChecked);
				btnhuoset39.setChecked(isChecked);
				btnhuoset310.setChecked(isChecked);
			} 
        });
		btnhuosetc4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btnhuoset41.setChecked(isChecked);
				btnhuoset42.setChecked(isChecked);
				btnhuoset43.setChecked(isChecked);
				btnhuoset44.setChecked(isChecked);
				btnhuoset45.setChecked(isChecked);
				btnhuoset46.setChecked(isChecked);
				btnhuoset47.setChecked(isChecked);
				btnhuoset48.setChecked(isChecked);
				btnhuoset49.setChecked(isChecked);
				btnhuoset410.setChecked(isChecked);
			} 
        });
		btnhuosetc5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btnhuoset51.setChecked(isChecked);
				btnhuoset52.setChecked(isChecked);
				btnhuoset53.setChecked(isChecked);
				btnhuoset54.setChecked(isChecked);
				btnhuoset55.setChecked(isChecked);
				btnhuoset56.setChecked(isChecked);
				btnhuoset57.setChecked(isChecked);
				btnhuoset58.setChecked(isChecked);
				btnhuoset59.setChecked(isChecked);
				btnhuoset510.setChecked(isChecked);
			} 
        });
		btnhuosetc6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btnhuoset61.setChecked(isChecked);
				btnhuoset62.setChecked(isChecked);
				btnhuoset63.setChecked(isChecked);
				btnhuoset64.setChecked(isChecked);
				btnhuoset65.setChecked(isChecked);
				btnhuoset66.setChecked(isChecked);
				btnhuoset67.setChecked(isChecked);
				btnhuoset68.setChecked(isChecked);
				btnhuoset69.setChecked(isChecked);
				btnhuoset610.setChecked(isChecked);
			} 
        });
		btnhuosetc7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btnhuoset71.setChecked(isChecked);
				btnhuoset72.setChecked(isChecked);
				btnhuoset73.setChecked(isChecked);
				btnhuoset74.setChecked(isChecked);
				btnhuoset75.setChecked(isChecked);
				btnhuoset76.setChecked(isChecked);
				btnhuoset77.setChecked(isChecked);
				btnhuoset78.setChecked(isChecked);
				btnhuoset79.setChecked(isChecked);
				btnhuoset710.setChecked(isChecked);
			} 
        });
		btnhuosetc8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btnhuoset81.setChecked(isChecked);
				btnhuoset82.setChecked(isChecked);
				btnhuoset83.setChecked(isChecked);
				btnhuoset84.setChecked(isChecked);
				btnhuoset85.setChecked(isChecked);
				btnhuoset86.setChecked(isChecked);
				btnhuoset87.setChecked(isChecked);
				btnhuoset88.setChecked(isChecked);
				btnhuoset89.setChecked(isChecked);
				btnhuoset810.setChecked(isChecked);
			} 
        });
		
		this.spinhuopeiCab.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//ֻ���й�ŵ�ʱ�򣬲�������ع��ڻ�����Ϣ
				if(cabinetID!=null)
				{
					cabinetpeivar=Integer.parseInt(cabinetID[arg2]); 
					cabinetTypepeivar=cabinetType[arg2]; 
				}				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	//===============
	//��������ҳ��
	//===============
	//���õ���ʱ��ʱ��
	TimerTask task = new TimerTask() { 
        @Override 
        public void run() { 
  
            runOnUiThread(new Runnable() {      // UI thread 
                @Override 
                public void run() { 
                    if(ishuoquery==1)
                    {
                    	queryhuodao();
                    }
//                    //��ѯ�Ѿ����
//                    else if(ishuoquery==0)
//                    {
//                    	timer.cancel(); //ȡ������ʱ�����Ͳ����ټ�������
//                    }
                } 
            }); 
        } 
    };
	private void queryhuodao()
	{
		txthuosetrst.setText("��ѯ����:"+(con++));		
		ishuoquery=1;
		//���ӹ�
		if(cabinetTypesetvar==5)
		{
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<huodao���ӹ����","log.txt");
			EVprotocolAPI.EV_bentoCheck(ToolClass.getBentcom_id(),cabinetsetvar);
		}
		//��ͨ��
		else 
		{
			EVprotocolAPI.getColumn(cabinetsetvar);
		}
	}
	//����GoodsProSet������Ϣ
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==REQUEST_CODE)
		{
			if(resultCode==HuodaoTest.RESULT_OK)
			{
				barhuomanager.setVisibility(View.VISIBLE);  
				showhuodao();
			}			
		}
	}
	
	@Override
	protected void onDestroy() {
    	//�˳�ʱ������intent
        Intent intent=new Intent();
        setResult(MaintainActivity.RESULT_CANCELED,intent);
		super.onDestroy();		
	}
	
	//��ӹ��
	private void cabinetAdd()
	{
		final String[] values=null;
		View myview=null;    
		String [] mStringArray; 
		ArrayAdapter<String> mAdapter ;
		
		// TODO Auto-generated method stub
		LayoutInflater factory = LayoutInflater.from(HuodaoTest.this);
		myview=factory.inflate(R.layout.selectcabinet, null);
		final EditText dialogcab=(EditText) myview.findViewById(R.id.dialogcab);
		final Spinner dialogspincabtype=(Spinner) myview.findViewById(R.id.dialogspincabtype);
		mStringArray=getResources().getStringArray(R.array.cabinet_Type);
  	    //ʹ���Զ����ArrayAdapter
	  	mAdapter = new ArrayAdapter<String>(this,R.layout.viewspinner,mStringArray);
	  	dialogspincabtype.setAdapter(mAdapter);// ΪListView�б���������Դ
		
		Dialog dialog = new AlertDialog.Builder(HuodaoTest.this)
		.setTitle("����")
		.setPositiveButton("����", new DialogInterface.OnClickListener() 	
		{
				
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub	
				
				//Toast.makeText(HuodaoTest.this, "��ֵ="+dialogspincabtype.getSelectedItemId(), Toast.LENGTH_LONG).show();
				String no = dialogcab.getText().toString();
		    	int type = (int)dialogspincabtype.getSelectedItemId()+1;
		    	if ((no.isEmpty()!=true)&&(type!=0))
		    	{
		    		addabinet(no,type);
		    	}
		    	else
		        {
		            Toast.makeText(HuodaoTest.this, "����������ź����ͣ�", Toast.LENGTH_SHORT).show();
		        }
			}
		})
		.setNegativeButton("ȡ��",  new DialogInterface.OnClickListener()//ȡ����ť���������ü����¼�
    	{			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub				
			}
    	})
		.setView(myview)//���ｫ�Ի��򲼾��ļ����뵽�Ի�����
		.create();
		dialog.show();	
	}
	//�����ݿ���ӹ����ͱ��ֵ
	private void addabinet(String no,int type)
	{
		try 
		{
			// ����InaccountDAO����
			vmc_cabinetDAO cabinetDAO = new vmc_cabinetDAO(HuodaoTest.this);
            // ����Tb_inaccount����
        	Tb_vmc_cabinet tb_vmc_cabinet = new Tb_vmc_cabinet(no,type);
        	cabinetDAO.add(tb_vmc_cabinet);// ���������Ϣ
        	showabinet();//��ʾ����Ϣ
        	ToolClass.addOptLog(HuodaoTest.this,0,"��ӹ�:"+no);
        	// ������Ϣ��ʾ
            Toast.makeText(HuodaoTest.this, "������������ӳɹ���", Toast.LENGTH_SHORT).show();
            
		} catch (Exception e)
		{
			// TODO: handle exception
			Toast.makeText(HuodaoTest.this, "�������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
		}
	}
	//��ʾȫ������Ϣ
	private void showabinet()
	{
		ArrayAdapter<String> arrayAdapter = null;// ����ArrayAdapter���� 
		Vmc_CabinetAdapter vmc_cabAdapter=new Vmc_CabinetAdapter();
	    String[] strInfos = vmc_cabAdapter.showSpinInfo(HuodaoTest.this);	    
	    // ʹ���ַ��������ʼ��ArrayAdapter����
	    arrayAdapter = new ArrayAdapter<String>(this,R.layout.viewspinner, strInfos);
	    spinhuosetCab.setAdapter(arrayAdapter);// Ϊspin�б���������Դ
	    spinhuotestCab.setAdapter(arrayAdapter);// Ϊspin�б���������Դ
	    spinhuopeiCab.setAdapter(arrayAdapter);// Ϊspin�б���������Դ
	    cabinetID=vmc_cabAdapter.getCabinetID();    
	    cabinetType=vmc_cabAdapter.getCabinetType();
	    //ֻ���й�ŵ�ʱ�򣬲�������ع��ڻ�����Ϣ
//		if(cabinetID!=null)
//		{
//		    cabinetsetvar=Integer.parseInt(cabinetID[0]); 
//		    cabinetTypesetvar=cabinetType[0]; 
//		}
	}
	//���뱾��ȫ��������Ϣ
	private void showhuodao()
	{		 
		huoAdapter.showProInfo(HuodaoTest.this, "", huoSet,String.valueOf(cabinetsetvar));
		
		HuoPictureAdapter adapter = new HuoPictureAdapter(String.valueOf(cabinetsetvar),huoAdapter.getHuoID(),huoAdapter.getHuoproID(),huoAdapter.getHuoRemain(),huoAdapter.getHuolasttime(), huoAdapter.getProImage(),HuodaoTest.this);// ����pictureAdapter����
		gvhuodao.setAdapter(adapter);// ΪGridView��������Դ		 
		barhuomanager.setVisibility(View.GONE);  
	}
	//ɾ������Լ����ڻ���ȫ����Ϣ
	private void cabinetDel()
	{
		//��������Ի���
    	Dialog alert=new AlertDialog.Builder(HuodaoTest.this)
    		.setTitle("�Ի���")//����
    		.setMessage("��ȷ��Ҫɾ���ù���")//��ʾ�Ի����е�����
    		.setIcon(R.drawable.ic_launcher)//����logo
    		.setPositiveButton("ɾ��", new DialogInterface.OnClickListener()//�˳���ť���������ü����¼�
    			{				
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) 
	    				{
	    					// TODO Auto-generated method stub	
	    					// ����InaccountDAO����
	    					vmc_columnDAO columnDAO = new vmc_columnDAO(HuodaoTest.this);
				            columnDAO.deteleCab(String.valueOf(cabinetsetvar));// ɾ���ù������Ϣ
				            
				            vmc_cabinetDAO cabinetDAO = new vmc_cabinetDAO(HuodaoTest.this);
				            cabinetDAO.detele(String.valueOf(cabinetsetvar));// ɾ���ù���Ϣ
				            ToolClass.addOptLog(HuodaoTest.this,2,"ɾ����:"+cabinetsetvar);
	    					// ������Ϣ��ʾ
				            Toast.makeText(HuodaoTest.this, "��ɾ���ɹ���", Toast.LENGTH_SHORT).show();						            
				            finish();
	    				}
    		      }
    			)		    		        
		        .setNegativeButton("ȡ��", new DialogInterface.OnClickListener()//ȡ����ť���������ü����¼�
		        	{			
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							// TODO Auto-generated method stub				
						}
		        	}
		        )
		        .create();//����һ���Ի���
		        alert.show();//��ʾ�Ի���
	}
	
	//�����������
	private void cabinetbuhuo()
	{
		//��������Ի���
    	Dialog alert=new AlertDialog.Builder(HuodaoTest.this)
    		.setTitle("�Ի���")//����
    		.setMessage("��ȷ��Ҫ�������������")//��ʾ�Ի����е�����
    		.setIcon(R.drawable.ic_launcher)//����logo
    		.setPositiveButton("����", new DialogInterface.OnClickListener()//�˳���ť���������ü����¼�
    			{				
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) 
	    				{
	    					// TODO Auto-generated method stub	
	    					barhuomanager.setVisibility(View.VISIBLE);
	    					// ����InaccountDAO����
	    					vmc_columnDAO columnDAO = new vmc_columnDAO(HuodaoTest.this);
				            columnDAO.buhuoCab(String.valueOf(cabinetsetvar));
				            showhuodao();
				            //=============
			    			//Server�������
			    			//=============
			    			//7.����ָ��㲥��EVServerService
			    			Intent intent2=new Intent();
		    				intent2.putExtra("EVWhat", EVServerhttp.SETHUODAOSTATUCHILD);
		    				intent2.setAction("android.intent.action.vmserversend");//action���������ͬ
		    				sendBroadcast(intent2);
	    					// ������Ϣ��ʾ
				            Toast.makeText(HuodaoTest.this, "�����ɹ���", Toast.LENGTH_SHORT).show();	
	    				}
    		      }
    			)		    		        
		        .setNegativeButton("ȡ��", new DialogInterface.OnClickListener()//ȡ����ť���������ü����¼�
		        	{			
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							// TODO Auto-generated method stub				
						}
		        	}
		        )
		        .create();//����һ���Ի���
		        alert.show();//��ʾ�Ի���
	}
	
	//===============
	//��������ҳ��
	//===============
	private void sethuorst(int status)
	{
		switch(status)
		{			
			case 0:
				txthuotestrst.setText("����ʧ��");
				break;
			case 1:
				txthuotestrst.setText("�����ɹ�");
				break;		
		}
	}
}
