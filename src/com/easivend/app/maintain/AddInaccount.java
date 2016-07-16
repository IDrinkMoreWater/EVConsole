/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           AddInaccount.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        ֽ����Ӳ��������ҳ��          
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.app.maintain;


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.easivend.view.COMService;
import com.easivend.common.SerializableMap;
import com.easivend.common.ToolClass;
import com.easivend.evprotocol.COMThread;
import com.easivend.evprotocol.EVprotocol;
import com.example.evconsole.R;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class AddInaccount extends TabActivity
{
	private TabHost mytabhost = null;
	private int[] layres=new int[]{R.id.tab_billmanager,R.id.tab_coinmanager,R.id.tab_payoutmanager};//��Ƕ�����ļ���id
	private double amount=0;//��Ͷ�ҽ��
	//ֽ����
	private Spinner spinbillmanagerbill=null;
	private String [] billStringArray; 
	private ArrayAdapter<String> billAdapter ;
	private EditText edtpayout=null;
	private TextView txtbillmanagerpar=null,txtbillmanagerpar2=null,txtbillmanagerstate=null,txtbillmanagerbillin=null,txtbillmanagerbillin2=null,txtbillmanagerbillincount=null
			,txtbillmanagerbillpay=null,txtbillmanagerbillpay2=null,txtbillmanagerbillpaycount=null,txtbillmanagerbillpayamount=null,txtbillpayin=null,
			txtpaymoney=null,txtbillpayback=null,txtbillerr=null;
	private Button btnbillon=null,btnbilloff=null,btnbillquery=null,btnbillset=null,btnbillexit=null,btnbillpayout=null;// ����Button�����˳���
	//Ӳ����
	private Spinner spincoinmanagercoin=null;
	private String [] coinStringArray; 
	private ArrayAdapter<String> coinAdapter ;
	private TextView txtcoinmanagerpar=null,txtcoinmanagerpar2=null,txtcoinmanagerstate=null,txtcoinpayback=null,txtcoinerr=null,
			txtcoinmanagercoinincount=null,txtcoinmanagercoininamount=null,txtcoinpayin=null,txtcoinpaymoney=null;
	private EditText txtcoinmanagercoinin1=null,txtcoinmanagercoinin2=null,txtcoinmanagercoinin3=null,txtcoinmanagercoinin4=null,
			txtcoinmanagercoinin5=null,txtcoinmanagercoinin6=null,txtcoinmanagercoinin7=null,txtcoinmanagercoinin8=null,
			txtcoinmanagercoinin9=null,txtcoinmanagercoinin10=null,txtcoinmanagercoinin11=null,txtcoinmanagercoinin12=null,
			txtcoinmanagercoinin13=null,txtcoinmanagercoinin14=null,txtcoinmanagercoinin15=null,txtcoinmanagercoinin16=null,
			edtcoinpayout=null;		
	private Button btncoinon=null,btncoinoff=null,btncoinquery=null,btncoinset=null,btncoinpayout=null,btncoinexit=null;
	//hopper������
	private Spinner spinhopper=null;
	private String [] hopperStringArray; 
	private ArrayAdapter<String> hopperAdapter ;
	private TextView txthopperincount=null,txthopperpaymoney=null,txthopperpaynum=null;
	private EditText txthopperin1=null,txthopperin2=null,txthopperin3=null,txthopperin4=null,
			txthopperin5=null,txthopperin6=null,txthopperin7=null,txthopperin8=null,edthopperpayout=null,
			edthopperpayno=null,edthopperpaynum=null;		
	private Button btnhopperquery=null,btnhopperpay=null,btnhopperpaymoney=null,btnhopperset=null,btnhopperexit=null,
			btnhopperpaynum=null;
	private int devopt=0;//��������	
	private boolean cashopt=false;//true��ʾ�д�ֽ��������Ӳ����
	private boolean ercheck=false;//true�����˱Ҳ����У����Ժ�falseû���˱ҵ��̲߳���
	private int queryLen = 0; //��ѯʱ��
	ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
	//COM�������
	LocalBroadcastManager comBroadreceiver;
	COMReceiver comreceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.addinaccount);// ���ò����ļ�
		this.mytabhost = super.getTabHost();//ȡ��TabHost����
        LayoutInflater.from(this).inflate(R.layout.addinaccount, this.mytabhost.getTabContentView(),true);
        //����Tab�����
        TabSpec myTabbill=this.mytabhost.newTabSpec("tab0");
        myTabbill.setIndicator("ֽ��������");
        myTabbill.setContent(this.layres[0]);
    	this.mytabhost.addTab(myTabbill); 
    	
    	TabSpec myTabcoin=this.mytabhost.newTabSpec("tab1");
    	myTabcoin.setIndicator("Ӳ��������");
    	myTabcoin.setContent(this.layres[1]);
    	this.mytabhost.addTab(myTabcoin); 
    	
    	TabSpec myTabpay=this.mytabhost.newTabSpec("tab2");
    	myTabpay.setIndicator("Hopper����������");
    	myTabpay.setContent(this.layres[2]);
    	this.mytabhost.addTab(myTabpay); 
    	timer.scheduleWithFixedDelay(task, 1, 1, TimeUnit.SECONDS);       // timeTask 
    	
    	//4.ע�������
		comBroadreceiver = LocalBroadcastManager.getInstance(this);
		comreceiver=new COMReceiver();
		IntentFilter comfilter=new IntentFilter();
		comfilter.addAction("android.intent.action.comrec");
		comBroadreceiver.registerReceiver(comreceiver,comfilter);
  	    //===============
    	//ֽ��������ҳ��
    	//===============
  	    spinbillmanagerbill = (Spinner) findViewById(R.id.spinbillmanagerbill);
  	    billStringArray=getResources().getStringArray(R.array.bill_label);
  	    //ʹ���Զ����ArrayAdapter
        billAdapter = new ArrayAdapter<String>(this,R.layout.viewspinner,billStringArray);
        spinbillmanagerbill.setAdapter(billAdapter);// ΪListView�б���������Դ
	  	txtbillmanagerpar = (TextView) findViewById(R.id.txtbillmanagerpar);
	  	txtbillmanagerpar2 = (TextView) findViewById(R.id.txtbillmanagerpar2);
	  	txtbillmanagerstate = (TextView) findViewById(R.id.txtbillmanagerstate);
	  	txtbillpayback = (TextView) findViewById(R.id.txtbillpayback);
	  	txtbillerr = (TextView) findViewById(R.id.txtbillerr);
	  	txtbillmanagerbillin = (TextView) findViewById(R.id.txtbillmanagerbillin);
	  	txtbillmanagerbillin2 = (TextView) findViewById(R.id.txtbillmanagerbillin2);
	  	txtbillmanagerbillincount = (TextView) findViewById(R.id.txtbillmanagerbillincount);
	  	txtbillmanagerbillpay = (TextView) findViewById(R.id.txtbillmanagerbillpay);
	  	txtbillmanagerbillpay2 = (TextView) findViewById(R.id.txtbillmanagerbillpay2);
	  	txtbillmanagerbillpaycount = (TextView) findViewById(R.id.txtbillmanagerbillpaycount);
	  	txtbillmanagerbillpayamount = (TextView) findViewById(R.id.txtbillmanagerbillpayamount);
	  	txtbillpayin = (TextView) findViewById(R.id.txtbillpayin);
	  	txtpaymoney = (TextView) findViewById(R.id.txtpaymoney);
		edtpayout = (EditText) findViewById(R.id.edtpayout);
		btnbillpayout = (Button) findViewById(R.id.btnbillpayout);
		btnbillpayout.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//EVprotocolAPI.EV_mdbPayout(ToolClass.getCom_id(),1,0,ToolClass.MoneySend(Float.parseFloat(edtpayout.getText().toString())),0);
		    	Intent intent=new Intent();
		    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_PAYOUT);	
				intent.putExtra("bill", 1);	
				intent.putExtra("coin", 0);	
				intent.putExtra("billPay", ToolClass.MoneySend(Float.parseFloat(edtpayout.getText().toString())));	
				intent.putExtra("coinPay", 0);	
				intent.setAction("android.intent.action.comsend");//action���������ͬ
				comBroadreceiver.sendBroadcast(intent);
				devopt=EVprotocol.EV_MDB_PAYOUT;
				ercheck=true;
		    }
		});
		btnbillon = (Button) findViewById(R.id.btnbillon);
		btnbillon.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,0,1);
		    	Intent intent=new Intent();
		    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_ENABLE);	
				intent.putExtra("bill", 1);	
				intent.putExtra("coin", 0);	
				intent.putExtra("opt", 1);	
				intent.setAction("android.intent.action.comsend");//action���������ͬ
				comBroadreceiver.sendBroadcast(intent);
				devopt=1;//����ֽ����
		    }
		});
		btnbilloff = (Button) findViewById(R.id.btnbilloff);
		btnbilloff.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,0,0);
		    	Intent intent=new Intent();
		    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_ENABLE);	
				intent.putExtra("bill", 1);	
				intent.putExtra("coin", 0);	
				intent.putExtra("opt", 0);	
				intent.setAction("android.intent.action.comsend");//action���������ͬ
				comBroadreceiver.sendBroadcast(intent);
				devopt=1;//����ֽ����
		    }
		});
		btnbillquery = (Button) findViewById(R.id.btnbillquery);
		btnbillquery.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
		    	Intent intent2=new Intent();
		    	intent2.putExtra("EVWhat", EVprotocol.EV_MDB_HEART);
				intent2.setAction("android.intent.action.comsend");//action���������ͬ
				comBroadreceiver.sendBroadcast(intent2);
				devopt=EVprotocol.EV_MDB_HEART;//Heart����
		    }
		});
		btnbillset = (Button) findViewById(R.id.btnbillset);
		btnbillset.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
//		    	//��������Ի���
//		    	Dialog alert=new AlertDialog.Builder(AddInaccount.this)
//		    		.setTitle("�Ի���")//����
//		    		.setMessage("��ȷ��Ҫ����ֽ������")//��ʾ�Ի����е�����
//		    		.setIcon(R.drawable.ic_launcher)//����logo
//		    		.setPositiveButton("����", new DialogInterface.OnClickListener()//�˳���ť���������ü����¼�
//		    			{				
//			    				@Override
//			    				public void onClick(DialogInterface dialog, int which) 
//			    				{
//			    					// TODO Auto-generated method stub	
//			    					int billtype=0;
//			    			    	if(spinbillmanagerbill.getSelectedItemPosition()==1)
//			    			    		billtype=2;
//			    					else if(spinbillmanagerbill.getSelectedItemPosition()==0)
//			    						billtype=0;
//			    			    	//EVprotocolAPI.EV_mdbBillConfig(ToolClass.getCom_id(),billtype);	
//			    			    	Intent intent2=new Intent();
//			    			    	intent2.putExtra("EVWhat", COMService.EV_MDB_B_CON);
//			    			    	intent2.putExtra("billtype", billtype);
//			    			    	intent2.setAction("android.intent.action.comsend");//action���������ͬ
//			    					comBroadreceiver.sendBroadcast(intent2);
//			    					devopt=COMService.EV_MDB_B_CON;
//			    			    	// ������Ϣ��ʾ
//						            Toast.makeText(AddInaccount.this, "����ֽ�����ɹ���", Toast.LENGTH_SHORT).show();
//						     }
//		    		      }
//		    			)		    		        
//	    		        .setNegativeButton("ȡ��", new DialogInterface.OnClickListener()//ȡ����ť���������ü����¼�
//	    		        	{			
//	    						@Override
//	    						public void onClick(DialogInterface dialog, int which) 
//	    						{
//	    							// TODO Auto-generated method stub				
//	    						}
//	    		        	}
//	    		        )
//	    		        .create();//����һ���Ի���
//	    		        alert.show();//��ʾ�Ի���	
		    	// TODO: handle exception
				ToolClass.failToast("��������δ������");
		    }
		});	
		btnbillexit = (Button) findViewById(R.id.btnbillexit2);
		btnbillexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	finishActivity();
		    }
		});
		//===============
    	//Ӳ��������ҳ��
    	//===============
		spincoinmanagercoin = (Spinner) findViewById(R.id.spincoinmanagercoin);
		coinStringArray=getResources().getStringArray(R.array.coin_label);
  	    //ʹ���Զ����ArrayAdapter
		coinAdapter = new ArrayAdapter<String>(this,R.layout.viewspinner,coinStringArray);
        spincoinmanagercoin.setAdapter(coinAdapter);// ΪListView�б���������Դ
	  	txtcoinmanagerpar = (TextView) findViewById(R.id.txtcoinmanagerpar);
	  	txtcoinmanagerpar2 = (TextView) findViewById(R.id.txtcoinmanagerpar2);
	  	txtcoinmanagerstate = (TextView) findViewById(R.id.txtcoinmanagerstate);
	  	txtcoinpayback = (TextView) findViewById(R.id.txtcoinpayback);
	  	txtcoinerr = (TextView) findViewById(R.id.txtcoinerr);
	  	txtcoinmanagercoinincount = (TextView) findViewById(R.id.txtcoinmanagercoinincount);
	  	txtcoinmanagercoininamount = (TextView) findViewById(R.id.txtcoinmanagercoininamount);
	  	txtcoinpayin = (TextView) findViewById(R.id.txtcoinpayin);
	  	txtcoinpaymoney = (TextView) findViewById(R.id.txtcoinpaymoney);
	  	txtcoinmanagercoinin1 = (EditText) findViewById(R.id.txtcoinmanagercoinin1);
	  	txtcoinmanagercoinin2 = (EditText) findViewById(R.id.txtcoinmanagercoinin2);
	  	txtcoinmanagercoinin3 = (EditText) findViewById(R.id.txtcoinmanagercoinin3);
	  	txtcoinmanagercoinin4 = (EditText) findViewById(R.id.txtcoinmanagercoinin4);
	  	txtcoinmanagercoinin5 = (EditText) findViewById(R.id.txtcoinmanagercoinin5);
	  	txtcoinmanagercoinin6 = (EditText) findViewById(R.id.txtcoinmanagercoinin6);
	  	txtcoinmanagercoinin7 = (EditText) findViewById(R.id.txtcoinmanagercoinin7);
	  	txtcoinmanagercoinin8 = (EditText) findViewById(R.id.txtcoinmanagercoinin8);
	  	txtcoinmanagercoinin9 = (EditText) findViewById(R.id.txtcoinmanagercoinin9);
	  	txtcoinmanagercoinin10 = (EditText) findViewById(R.id.txtcoinmanagercoinin10);
	  	txtcoinmanagercoinin11 = (EditText) findViewById(R.id.txtcoinmanagercoinin11);
	  	txtcoinmanagercoinin12 = (EditText) findViewById(R.id.txtcoinmanagercoinin12);
	  	txtcoinmanagercoinin13 = (EditText) findViewById(R.id.txtcoinmanagercoinin13);
	  	txtcoinmanagercoinin14 = (EditText) findViewById(R.id.txtcoinmanagercoinin14);
	  	txtcoinmanagercoinin15 = (EditText) findViewById(R.id.txtcoinmanagercoinin15);
	  	txtcoinmanagercoinin16 = (EditText) findViewById(R.id.txtcoinmanagercoinin16);
	  	edtcoinpayout = (EditText) findViewById(R.id.edtcoinpayout);
	  	btncoinon = (Button) findViewById(R.id.btncoinon);
	  	btncoinon.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),0,1,1);
		    	Intent intent=new Intent();
		    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_ENABLE);	
				intent.putExtra("bill", 0);	
				intent.putExtra("coin", 1);	
				intent.putExtra("opt", 1);	
				intent.setAction("android.intent.action.comsend");//action���������ͬ
				comBroadreceiver.sendBroadcast(intent);
				devopt=2;//����Ӳ����
		    }
		});
	  	btncoinoff = (Button) findViewById(R.id.btncoinoff);
	  	btncoinoff.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),0,1,0);
		    	Intent intent=new Intent();
		    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_ENABLE);	
				intent.putExtra("bill", 0);	
				intent.putExtra("coin", 1);	
				intent.putExtra("opt", 0);	
				intent.setAction("android.intent.action.comsend");//action���������ͬ
				comBroadreceiver.sendBroadcast(intent);
				devopt=2;//����Ӳ����
		    }
		});
	  	btncoinquery = (Button) findViewById(R.id.btncoinquery);
	  	btncoinquery.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
		    	Intent intent2=new Intent();
		    	intent2.putExtra("EVWhat", EVprotocol.EV_MDB_HEART);
				intent2.setAction("android.intent.action.comsend");//action���������ͬ
				comBroadreceiver.sendBroadcast(intent2);
				devopt=EVprotocol.EV_MDB_HEART;//Heart����
		    }
		});
	  	btncoinpayout = (Button) findViewById(R.id.btncoinpayout);
	  	btncoinpayout.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//EVprotocolAPI.EV_mdbPayout(ToolClass.getCom_id(),0,1,0,ToolClass.MoneySend(Float.parseFloat(edtcoinpayout.getText().toString())));
		    	Intent intent=new Intent();
		    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_PAYOUT);	
				intent.putExtra("bill", 0);	
				intent.putExtra("coin", 1);	
				intent.putExtra("billPay", 0);	
				intent.putExtra("coinPay", ToolClass.MoneySend(Float.parseFloat(edtcoinpayout.getText().toString())));	
				intent.setAction("android.intent.action.comsend");//action���������ͬ
				comBroadreceiver.sendBroadcast(intent);
				devopt=EVprotocol.EV_MDB_PAYOUT;
				ercheck=true;
		    }
		});
	  	btncoinset = (Button) findViewById(R.id.btncoinset);
	  	btncoinset.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//CoinConfig();
		    	// TODO: handle exception
				ToolClass.failToast("��������δ������");
		    }
		});
	  	btncoinexit = (Button) findViewById(R.id.btncoinexit);
	  	btncoinexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	finishActivity();
		    }
		});
	    //===============
    	//hopper����ҳ��
    	//===============
	  	spinhopper = (Spinner) findViewById(R.id.spinhopper);
	  	hopperStringArray=getResources().getStringArray(R.array.payout_label);
  	    //ʹ���Զ����ArrayAdapter
	  	hopperAdapter = new ArrayAdapter<String>(this,R.layout.viewspinner,hopperStringArray);
	  	spinhopper.setAdapter(hopperAdapter);// ΪListView�б���������Դ
	  	txthopperincount = (TextView) findViewById(R.id.txthopperincount);
	  	txthopperpaymoney = (TextView) findViewById(R.id.txthopperpaymoney);
	    txthopperpaynum = (TextView) findViewById(R.id.txthopperpaynum);
	  	txthopperin1 = (EditText) findViewById(R.id.txthopperin1);
	  	txthopperin2 = (EditText) findViewById(R.id.txthopperin2);
	  	txthopperin3 = (EditText) findViewById(R.id.txthopperin3);
	  	txthopperin4 = (EditText) findViewById(R.id.txthopperin4);
	  	txthopperin5 = (EditText) findViewById(R.id.txthopperin5);
	  	txthopperin6 = (EditText) findViewById(R.id.txthopperin6);
	  	txthopperin7 = (EditText) findViewById(R.id.txthopperin7);
	  	txthopperin8 = (EditText) findViewById(R.id.txthopperin8);	  	
	  	edthopperpayout = (EditText) findViewById(R.id.edthopperpayout);
	  	edthopperpayno = (EditText) findViewById(R.id.edthopperpayno);
	  	edthopperpaynum = (EditText) findViewById(R.id.edthopperpaynum);
	  	btnhopperquery = (Button) findViewById(R.id.btnhopperquery);
	  	btnhopperquery.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//EVprotocolAPI.EV_mdbCoinInfoCheck(ToolClass.getCom_id());
		    	//EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
		    	Intent intent2=new Intent();
		    	intent2.putExtra("EVWhat", EVprotocol.EV_MDB_HEART);
				intent2.setAction("android.intent.action.comsend");//action���������ͬ
				comBroadreceiver.sendBroadcast(intent2);
				devopt=EVprotocol.EV_MDB_HEART;//Heart����
		    }
		});
	  	btnhopperpay = (Button) findViewById(R.id.btnhopperpay);
	  	btnhopperpay.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//EVprotocolAPI.EV_mdbPayout(ToolClass.getCom_id(),0,1,0,ToolClass.MoneySend(Float.parseFloat(edthopperpayout.getText().toString())));
		    	Intent intent=new Intent();
		    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_PAYOUT);	
		    	intent.putExtra("bill", 0);	
		    	intent.putExtra("coin", 1);	
		    	intent.putExtra("billPay", 0);	
		    	intent.putExtra("coinPay", ToolClass.MoneySend(Float.parseFloat(edthopperpayout.getText().toString())));	
		    	intent.setAction("android.intent.action.comsend");//action���������ͬ
		    	comBroadreceiver.sendBroadcast(intent);
		    	devopt=EVprotocol.EV_MDB_PAYOUT;
		    	ercheck=true;
		    }
		});
	  	btnhopperpaymoney = (Button) findViewById(R.id.btnhopperpaymoney);
	  	btnhopperpaymoney.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//EVprotocolAPI.EV_mdbPayout(ToolClass.getCom_id(),0,1,0,ToolClass.MoneySend(Float.parseFloat(edthopperpayout.getText().toString())));
		    	Intent intent=new Intent();
		    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_PAYOUT);	
		    	intent.putExtra("bill", 0);	
		    	intent.putExtra("coin", 1);	
		    	intent.putExtra("billPay", 0);	
		    	intent.putExtra("coinPay", ToolClass.MoneySend(Float.parseFloat(edthopperpayout.getText().toString())));	
		    	intent.setAction("android.intent.action.comsend");//action���������ͬ
		    	comBroadreceiver.sendBroadcast(intent);
		    	devopt=EVprotocol.EV_MDB_PAYOUT;
		    	ercheck=true;
		    }
		});
	  	btnhopperpaynum = (Button) findViewById(R.id.btnhopperpaynum);
	  	btnhopperpaynum.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//EVprotocolAPI.EV_mdbHopperPayout(ToolClass.getCom_id(),Integer.parseInt(edthopperpayno.getText().toString()),Integer.parseInt(edthopperpaynum.getText().toString()));
		    	Intent intent=new Intent();
		    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_HP_PAYOUT);	
		    	intent.putExtra("no", Integer.parseInt(edthopperpayno.getText().toString()));	
		    	intent.putExtra("nums", Integer.parseInt(edthopperpaynum.getText().toString()));	
		    	intent.setAction("android.intent.action.comsend");//action���������ͬ
		    	comBroadreceiver.sendBroadcast(intent);
		    	devopt=EVprotocol.EV_MDB_HP_PAYOUT;
		    	ercheck=true;
		    }
		});
	  	btnhopperset = (Button) findViewById(R.id.btnhopperset);
	  	btnhopperset.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//CoinConfig();
		    	// TODO: handle exception
				ToolClass.failToast("��������δ������");
		    }
		});
	  	btnhopperexit = (Button) findViewById(R.id.btnhopperexit);
	  	btnhopperexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	finishActivity();
		    }
		});
	}
	
	//���õ���ʱ��ʱ��
	TimerTask task = new TimerTask() { 
        @Override 
        public void run() { 
  
            runOnUiThread(new Runnable() {      // UI thread 
                @Override 
                public void run() { 
                    //���Ͳ�ѯ����ָ��
                    if(cashopt)
                    {
                    	//û�����˱��У��ſ��Բ�ѯ
                    	if(ercheck==false)
                    	{
		                    queryLen++;
		                    if(queryLen>=5)
		                    {
		                    	queryLen=0;
		                    	//EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
		        		    	Intent intent2=new Intent();
		        		    	intent2.putExtra("EVWhat", EVprotocol.EV_MDB_HEART);
		        				intent2.setAction("android.intent.action.comsend");//action���������ͬ
		        				comBroadreceiver.sendBroadcast(intent2);
		        				devopt=EVprotocol.EV_MDB_HEART;//Heart����
		                    }
                    	}
                    }                    
                } 
            }); 
        } 
    };
	
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
			//��������	
			case COMThread.EV_OPTMAIN: 
				SerializableMap serializableMap = (SerializableMap) bundle.get("result");
				Map<String, Integer> Set=serializableMap.getMap();
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �ֽ��豸����="+Set,"com.txt");
				int EV_TYPE=Set.get("EV_TYPE");
				switch(EV_TYPE)
				{
					case EVprotocol.EV_MDB_ENABLE:
						if(devopt==1)//����ֽ����
						{
							//ֽ�������ɹ�
							if((Integer)Set.get("bill_result")>0)
							{
								ToolClass.setBill_err(0);
	//							EVprotocolAPI.EV_mdbBillInfoCheck(ToolClass.getCom_id());
	//							EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
								//ֽ������ѯ�ӿ�
								Intent intent2=new Intent();
						    	intent2.putExtra("EVWhat", EVprotocol.EV_MDB_B_INFO);	
								intent2.setAction("android.intent.action.comsend");//action���������ͬ
								comBroadreceiver.sendBroadcast(intent2);
								cashopt=true;
							}
							else
							{
								ToolClass.setBill_err(2);
								cashopt=false;
							}
						}
						
						if(devopt==2)//����Ӳ����
						{
							//Ӳ�������ɹ�
							if((Integer)Set.get("coin_result")>0)
							{
								ToolClass.setCoin_err(0);
	//							EVprotocolAPI.EV_mdbCoinInfoCheck(ToolClass.getCom_id());
	//							EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
								//Ӳ������ѯ�ӿ�
								Intent intent3=new Intent();
						    	intent3.putExtra("EVWhat", EVprotocol.EV_MDB_C_INFO);	
								intent3.setAction("android.intent.action.comsend");//action���������ͬ
								comBroadreceiver.sendBroadcast(intent3);
								cashopt=true;
							}
							else
							{
								ToolClass.setCoin_err(2);
								cashopt=false;
							}
						}
						break;
					case EVprotocol.EV_MDB_B_INFO:							 
						String acceptor=((Integer)Set.get("acceptor")==2)?"MDB":"��";
						String dispenser=((Integer)Set.get("dispenser")==2)?"MDB":"��";
						String code=String.valueOf(Set.get("code"));
						String sn=String.valueOf(Set.get("sn"));
						String model=String.valueOf( Set.get("model"));
						String ver=String.valueOf(Set.get("ver"));
						int capacity=(Integer)Set.get("capacity");
						String str="ֽ�ҽ�����:"+acceptor+"ֽ��������:"+dispenser+"����:"+code
								+"���к�"+sn;
						txtbillmanagerpar.setText(str);
						str=" �ͺ�:"+model+"�汾��:"+ver+"������:"+capacity;
						txtbillmanagerpar2.setText(str);
						if((Integer)Set.get("acceptor")==2)
							spinbillmanagerbill.setSelection((Integer)Set.get("acceptor")-1);
						else if((Integer)Set.get("acceptor")==0)
							spinbillmanagerbill.setSelection(0);	
						
						Map<String,Integer> allSet1=new LinkedHashMap<String, Integer>();
						allSet1.put("ch_r1", Set.get("ch_r1"));
						allSet1.put("ch_r2", Set.get("ch_r2"));
						allSet1.put("ch_r3", Set.get("ch_r3"));
						allSet1.put("ch_r4", Set.get("ch_r4"));
						allSet1.put("ch_r5", Set.get("ch_r5"));
						allSet1.put("ch_r6", Set.get("ch_r6"));
						allSet1.put("ch_r7", Set.get("ch_r7"));
						allSet1.put("ch_r8", Set.get("ch_r8"));
						String allb1[]=new String[allSet1.size()];	
						int bi=0;
						Set<Map.Entry<String,Integer>> allset=allSet1.entrySet();  //ʵ����
					    Iterator<Map.Entry<String,Integer>> iter=allset.iterator();
					    while(iter.hasNext())
					    {
					        Map.Entry<String,Integer> me=iter.next();
					        //str+="["+me.getKey() + "]" + ToolClass.MoneyRec(me.getValue()) + ",";
					        allb1[bi++]="["+me.getKey() + "]" + ToolClass.MoneyRec(me.getValue());
					    }
					    String bstr1="",bstr2="";
					    for(bi=0;bi<8;bi++)
					    {
					    	if(bi<4)
					    		bstr1+=allb1[bi];
					    	else
					    		bstr2+=allb1[bi];							
					    }
					    txtbillmanagerbillin.setText(bstr1);
					    txtbillmanagerbillin2.setText(bstr2);
					  
					    Map<String,Integer> allSet2=new LinkedHashMap<String, Integer>();
						allSet2.put("ch_d1", Set.get("ch_d1"));
						allSet2.put("ch_d2", Set.get("ch_d2"));
						allSet2.put("ch_d3", Set.get("ch_d3"));
						allSet2.put("ch_d4", Set.get("ch_d4"));
						allSet2.put("ch_d5", Set.get("ch_d5"));
						allSet2.put("ch_d6", Set.get("ch_d6"));
						allSet2.put("ch_d7", Set.get("ch_d7"));
						allSet2.put("ch_d8", Set.get("ch_d8"));
					    String allb2[]=new String[allSet2.size()];	
					    bi=0;
						Set<Map.Entry<String,Integer>> allset2=allSet2.entrySet();  //ʵ����
					    Iterator<Map.Entry<String,Integer>> iter2=allset2.iterator();
					    while(iter2.hasNext())
					    {
					        Map.Entry<String,Integer> me=iter2.next();
					        //str+="[ͨ��"+me.getKey() + "]=" + ToolClass.MoneyRec(me.getValue()) + ",";
					        allb2[bi++]="["+me.getKey() + "]" + ToolClass.MoneyRec(me.getValue());
					    }
					    bstr1="";
					    bstr2="";
					    for(bi=0;bi<8;bi++)
					    {
					    	if(bi<4)
					    		bstr1+=allb2[bi];
					    	else
					    		bstr2+=allb2[bi];							
					    }
					    txtbillmanagerbillpay.setText(bstr1);
					    txtbillmanagerbillpay2.setText(bstr2);
					    //Heart����
					    Intent intent2=new Intent();
				    	intent2.putExtra("EVWhat", EVprotocol.EV_MDB_HEART);
						intent2.setAction("android.intent.action.comsend");//action���������ͬ
						comBroadreceiver.sendBroadcast(intent2);
						devopt=EVprotocol.EV_MDB_HEART;//Heart����
						break;
					case EVprotocol.EV_MDB_C_INFO:
						String acceptor2="";
						if((Integer)Set.get("acceptor")==3)
							acceptor2="��������";
						else if((Integer)Set.get("acceptor")==2)
							acceptor2="MDB";
						else if((Integer)Set.get("acceptor")==1)
							acceptor2="��������";
						else if((Integer)Set.get("acceptor")==0)
							acceptor2="��";
						String dispenser2="";
						if((Integer)Set.get("dispenser")==2)
							dispenser2="MDB";
						else if((Integer)Set.get("dispenser")==1)
							dispenser2="hopper";
						else if((Integer)Set.get("dispenser")==0)
							dispenser2="��";
						String code2=String.valueOf(Set.get("code"));
						String sn2=String.valueOf(Set.get("sn"));
						String model2=String.valueOf(Set.get("model"));
						String ver2=String.valueOf(Set.get("ver"));
						int capacity2=(Integer)Set.get("capacity");
						String str2="Ӳ�ҽ�����:"+acceptor2+"Ӳ��������:"+dispenser2+"����:"+code2
								+"���к�"+sn2;
						txtcoinmanagerpar.setText(str2);
						str2=" �ͺ�:"+model2+"�汾��:"+ver2+"������:"+capacity2;
						txtcoinmanagerpar2.setText(str2);
						spincoinmanagercoin.setSelection((Integer)Set.get("acceptor"));
						
						
						str2="";
						Map<String,Integer> allSet3=new LinkedHashMap<String, Integer>();
						allSet3.put("ch_r1", Set.get("ch_r1"));
						allSet3.put("ch_r2", Set.get("ch_r2"));
						allSet3.put("ch_r3", Set.get("ch_r3"));
						allSet3.put("ch_r4", Set.get("ch_r4"));
						allSet3.put("ch_r5", Set.get("ch_r5"));
						allSet3.put("ch_r6", Set.get("ch_r6"));
						allSet3.put("ch_r7", Set.get("ch_r7"));
						allSet3.put("ch_r8", Set.get("ch_r8"));
						//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+allSet3.toString());
						double all[]=new double[allSet3.size()];	
						int i=0;
						Set<Map.Entry<String,Integer>> allset3=allSet3.entrySet();  //ʵ����
						Iterator<Map.Entry<String,Integer>> iter3=allset3.iterator();
					    while(iter3.hasNext())
					    {
					        Map.Entry<String,Integer> me=iter3.next();
					        all[i++]=ToolClass.MoneyRec(me.getValue());
					        //str+="[ͨ��"+me.getKey() + "]=" + ToolClass.MoneyRec(me.getValue()) + ",";
					    }
					    txtcoinmanagercoinin1.setText(String.valueOf(all[0]));
					    txtcoinmanagercoinin2.setText(String.valueOf(all[1]));
					    txtcoinmanagercoinin3.setText(String.valueOf(all[2]));
					    txtcoinmanagercoinin4.setText(String.valueOf(all[3]));
					    txtcoinmanagercoinin5.setText(String.valueOf(all[4]));
					    txtcoinmanagercoinin6.setText(String.valueOf(all[5]));
					    txtcoinmanagercoinin7.setText(String.valueOf(all[6]));
					    txtcoinmanagercoinin8.setText(String.valueOf(all[7]));
					    txtcoinmanagercoinin9.setText("0");
					    txtcoinmanagercoinin10.setText("0");
					    txtcoinmanagercoinin11.setText("0");
					    txtcoinmanagercoinin12.setText("0");
					    txtcoinmanagercoinin13.setText("0");
					    txtcoinmanagercoinin14.setText("0");
					    txtcoinmanagercoinin15.setText("0");
					    txtcoinmanagercoinin16.setText("0");
					    //����ͨ����ֵ
					    spinhopper.setSelection((Integer)Set.get("dispenser"));
					    str2="";
					    Map<String,Integer> allSet4=new LinkedHashMap<String, Integer>();
						allSet4.put("ch_d1", Set.get("ch_d1"));
						allSet4.put("ch_d2", Set.get("ch_d2"));
						allSet4.put("ch_d3", Set.get("ch_d3"));
						allSet4.put("ch_d4", Set.get("ch_d4"));
						allSet4.put("ch_d5", Set.get("ch_d5"));
						allSet4.put("ch_d6", Set.get("ch_d6"));
						allSet4.put("ch_d7", Set.get("ch_d7"));
						allSet4.put("ch_d8", Set.get("ch_d8"));
					    ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+allSet4.toString(),"log.txt");
					    double all2[]=new double[allSet4.size()];	
						i=0;
						Set<Map.Entry<String,Integer>> allset4=allSet4.entrySet();  //ʵ����
					    Iterator<Map.Entry<String,Integer>> iter4=allset4.iterator();
					    while(iter4.hasNext())
					    {
					        Map.Entry<String,Integer> me=iter4.next();
					        all2[i++]=ToolClass.MoneyRec(me.getValue());
					        //str2+="[ͨ��"+me.getKey() + "]=" + ToolClass.MoneyRec(me.getValue()) + ",";
					    }
					    txthopperin1.setText(String.valueOf(all2[0]));
					    txthopperin2.setText(String.valueOf(all2[1]));
					    txthopperin3.setText(String.valueOf(all2[2]));
					    txthopperin4.setText(String.valueOf(all2[3]));
					    txthopperin5.setText(String.valueOf(all2[4]));
					    txthopperin6.setText(String.valueOf(all2[5]));
					    txthopperin7.setText(String.valueOf(all2[6]));
					    txthopperin8.setText(String.valueOf(all2[7]));
					    //Heart����
					    Intent intent4=new Intent();
				    	intent4.putExtra("EVWhat", EVprotocol.EV_MDB_HEART);
						intent4.setAction("android.intent.action.comsend");//action���������ͬ
						comBroadreceiver.sendBroadcast(intent4);
						devopt=EVprotocol.EV_MDB_HEART;//Heart����
						break;	
					case EVprotocol.EV_MDB_HEART://������ѯ
						Map<String,Object> obj=new LinkedHashMap<String, Object>();
						obj.putAll(Set);
						String bill_enable=((Integer)Set.get("bill_enable")==1)?"ʹ��":"����";
						txtbillmanagerstate.setText(bill_enable);
						String bill_payback=((Integer)Set.get("bill_payback")==1)?"����":"û����";
					  	txtbillpayback.setText(bill_payback);
					  	String bill_err=((Integer)Set.get("bill_err")==0)?"����":"������:"+(Integer)Set.get("bill_err");
					  	txtbillerr.setText(bill_err);
					  	double money=ToolClass.MoneyRec((Integer)Set.get("bill_recv"));					  	
					  	txtbillpayin.setText(String.valueOf(money));
					  	amount=money;//��ǰֽ��Ͷ��
					  	money=ToolClass.MoneyRec((Integer)Set.get("bill_remain"));
					  	txtbillmanagerbillpayamount.setText(String.valueOf(money));
					  	int bill_errstatus=ToolClass.getvmcStatus(obj,1);
					  	ToolClass.setBill_err(bill_errstatus);
					  	
					  	String coin_enable=((Integer)Set.get("coin_enable")==1)?"ʹ��":"����";
					  	txtcoinmanagerstate.setText(coin_enable);
						String coin_payback=((Integer)Set.get("coin_payback")==1)?"����":"û����";
						txtcoinpayback.setText(coin_payback);
					  	String coin_err=((Integer)Set.get("coin_err")==0)?"����":"������:"+(Integer)Set.get("coin_err");
					  	txtcoinerr.setText(coin_err);
					  	money=ToolClass.MoneyRec((Integer)Set.get("coin_recv"));					  	
					  	txtcoinpayin.setText(String.valueOf(money));
					  	amount+=money;//��ǰӲ��Ͷ��
					  	money=ToolClass.MoneyRec((Integer)Set.get("coin_remain"));
					  	txtcoinmanagercoininamount.setText(String.valueOf(money));
					  	int coin_errstatus=ToolClass.getvmcStatus(obj,2);
					  	ToolClass.setCoin_err(coin_errstatus);
					  	
					  	String hopperString=null;
					  	if(Set.containsKey("hopper1"))
					  	{
						  	hopperString="[1]:"+ToolClass.gethopperstats((Integer)Set.get("hopper1"))+"[2]:"+ToolClass.gethopperstats((Integer)Set.get("hopper2"))
						  				+"[[3]:"+ToolClass.gethopperstats((Integer)Set.get("hopper3"))+"[4]:"+ToolClass.gethopperstats((Integer)Set.get("hopper4"))
							  			+"[5]:"+ToolClass.gethopperstats((Integer)Set.get("hopper5"))+"[6]:"+ToolClass.gethopperstats((Integer)Set.get("hopper6"))
							  			+"[7]:"+ToolClass.gethopperstats((Integer)Set.get("hopper7"))+"[8]:"+ToolClass.gethopperstats((Integer)Set.get("hopper8"));
					  	}
					  	txthopperincount.setText(hopperString);
						break;
					case EVprotocol.EV_MDB_PAYOUT://����
						money=ToolClass.MoneyRec((Integer)Set.get("bill_changed"));					  	
						txtpaymoney.setText(String.valueOf(money));	
						money=ToolClass.MoneyRec((Integer)Set.get("coin_changed"));					  	
						txtcoinpaymoney.setText(String.valueOf(money));	
						txthopperpaymoney.setText(String.valueOf(money));
						ercheck=false;
						break;
					case EVprotocol.EV_MDB_HP_PAYOUT://����
						txthopperpaynum.setText(String.valueOf((Integer)Set.get("changed")));
						ercheck=false;
						break;
					case EVprotocol.EV_MDB_COST://��Ǯ
						float cost=ToolClass.MoneyRec((Integer)Set.get("cost"));	
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �ۿ�="+cost,"com.txt");
						
						//����Ѿ������ֽ��豸����ر���
						if(cashopt)
						{
							Intent intent5=new Intent();
					    	intent5.putExtra("EVWhat", EVprotocol.EV_MDB_ENABLE);	
							intent5.putExtra("bill", 1);	
							intent5.putExtra("coin", 1);	
							intent5.putExtra("opt", 0);	
							intent5.setAction("android.intent.action.comsend");//action���������ͬ
							comBroadreceiver.sendBroadcast(intent5);
							cashopt=false;
						}
						finish();
						break;
					default:break;	
				}
				break;			
			}			
		}

	}
	
	private void CoinConfig()
	{
		//��������Ի���
    	Dialog alert=new AlertDialog.Builder(AddInaccount.this)
    		.setTitle("�Ի���")//����
    		.setMessage("��ȷ��Ҫ����Ӳ��������������")//��ʾ�Ի����е�����
    		.setIcon(R.drawable.ic_launcher)//����logo
    		.setPositiveButton("����", new DialogInterface.OnClickListener()//�˳���ť���������ü����¼�
    			{				
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) 
	    				{
	    					// TODO Auto-generated method stub	
	    					Map<String, Integer>ch_r=new LinkedHashMap<String, Integer>();
	    			    	Map<String, Integer>ch_d=new LinkedHashMap<String, Integer>();
	    			    	ch_r.put("1", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin1.getText().toString())));
	    			    	ch_r.put("2", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin2.getText().toString())));
	    			    	ch_r.put("3", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin3.getText().toString())));
	    			    	ch_r.put("4", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin4.getText().toString())));
	    			    	ch_r.put("5", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin5.getText().toString())));
	    			    	ch_r.put("6", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin6.getText().toString())));
	    			    	ch_r.put("7", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin7.getText().toString())));
	    			    	ch_r.put("8", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin8.getText().toString())));
	    			    	ch_r.put("9", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin9.getText().toString())));
	    			    	ch_r.put("10", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin10.getText().toString())));
	    			    	ch_r.put("11", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin11.getText().toString())));
	    			    	ch_r.put("12", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin12.getText().toString())));
	    			    	ch_r.put("13", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin13.getText().toString())));
	    			    	ch_r.put("14", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin14.getText().toString())));
	    			    	ch_r.put("15", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin15.getText().toString())));
	    			    	ch_r.put("16", ToolClass.MoneySend(Float.parseFloat(txtcoinmanagercoinin16.getText().toString())));
	    			    	
	    			    	ch_d.put("1", ToolClass.MoneySend(Float.parseFloat(txthopperin1.getText().toString())));
	    			    	ch_d.put("2", ToolClass.MoneySend(Float.parseFloat(txthopperin2.getText().toString())));
	    			    	ch_d.put("3", ToolClass.MoneySend(Float.parseFloat(txthopperin3.getText().toString())));
	    			    	ch_d.put("4", ToolClass.MoneySend(Float.parseFloat(txthopperin4.getText().toString())));
	    			    	ch_d.put("5", ToolClass.MoneySend(Float.parseFloat(txthopperin5.getText().toString())));
	    			    	ch_d.put("6", ToolClass.MoneySend(Float.parseFloat(txthopperin6.getText().toString())));
	    			    	ch_d.put("7", ToolClass.MoneySend(Float.parseFloat(txthopperin7.getText().toString())));
	    			    	ch_d.put("8", ToolClass.MoneySend(Float.parseFloat(txthopperin8.getText().toString())));
	    			    	ch_d.put("9", 0);
	    			    	ch_d.put("10", 0);
	    			    	ch_d.put("11", 0);
	    			    	ch_d.put("12", 0);
	    			    	ch_d.put("13", 0);
	    			    	ch_d.put("14", 0);
	    			    	ch_d.put("15", 0);
	    			    	ch_d.put("16", 0);
	    			    	//EVprotocolAPI.EV_mdbCoinConfig(ToolClass.getCom_id(),spincoinmanagercoin.getSelectedItemPosition(),spinhopper.getSelectedItemPosition(),ch_r,ch_d);
	    			    	//ToolClass.Log(ToolClass.INFO,"EV_COM","cointype="+spincoinmanagercoin.getSelectedItemPosition()+"hoppertype="+spinhopper.getSelectedItemPosition(),"com.txt");
	    			    	Intent intent2=new Intent();
	    			    	intent2.putExtra("EVWhat", EVprotocol.EV_MDB_C_CON);
	    			    	intent2.putExtra("acceptor", spincoinmanagercoin.getSelectedItemPosition());
	    			    	intent2.putExtra("dispenser", spinhopper.getSelectedItemPosition());
	    			    	//��������
					        SerializableMap myMap=new SerializableMap();
					        myMap.setMap(ch_r);//��map������ӵ���װ��myMap<span></span>��
					        Bundle bundle=new Bundle();
					        bundle.putSerializable("ch_r", myMap);
					        intent2.putExtras(bundle);
					        //��������
					        SerializableMap myMap2=new SerializableMap();
					        myMap2.setMap(ch_d);//��map������ӵ���װ��myMap<span></span>��
					        Bundle bundle2=new Bundle();
					        bundle2.putSerializable("ch_d", myMap2);
					        intent2.putExtras(bundle2);
	    			    	intent2.setAction("android.intent.action.comsend");//action���������ͬ
	    					comBroadreceiver.sendBroadcast(intent2);
	    					devopt=EVprotocol.EV_MDB_B_CON;
	    			    	// ������Ϣ��ʾ
				            Toast.makeText(AddInaccount.this, "����Ӳ�����������ɹ���", Toast.LENGTH_SHORT).show();
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
	//�ر�ҳ��
	private void finishActivity()
	{
		//��Ǯ
	    //EVprotocolAPI.EV_mdbCost(ToolClass.getCom_id(),ToolClass.MoneySend((float)amount));
		timer.shutdown(); 
		if(amount>0)
		{
			Intent intent=new Intent();
	    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_COST);	
			intent.putExtra("cost", ToolClass.MoneySend((float)amount));	
			intent.setAction("android.intent.action.comsend");//action���������ͬ
			comBroadreceiver.sendBroadcast(intent);
			devopt=EVprotocol.EV_MDB_COST;
		}
		else
		{
			//����Ѿ������ֽ��豸����ر���
			if(cashopt)
			{
				Intent intent=new Intent();
		    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_ENABLE);	
				intent.putExtra("bill", 1);	
				intent.putExtra("coin", 1);	
				intent.putExtra("opt", 0);	
				intent.setAction("android.intent.action.comsend");//action���������ͬ
				comBroadreceiver.sendBroadcast(intent);
				cashopt=false;
			}
			finish();
		}
	}
	@Override
	protected void onDestroy() {		
		//=============
  		//COM�������
  		//=============
  		//5.���ע�������
  		comBroadreceiver.unregisterReceiver(comreceiver);
	    super.onDestroy();		
	}
	
}
