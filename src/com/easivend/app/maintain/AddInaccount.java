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


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.common.ToolClass;
import com.example.evconsole.R;

import android.R.integer;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class AddInaccount extends TabActivity
{
	private TabHost mytabhost = null;
	private int[] layres=new int[]{R.id.tab_billmanager,R.id.tab_coinmanager,R.id.tab_payoutmanager};//��Ƕ�����ļ���id
	//ֽ����
	private Spinner spinbillmanagerbill=null;
	private String [] billStringArray; 
	private ArrayAdapter<String> billAdapter ;
	private EditText edtpayout=null;
	private TextView txtbillmanagerpar=null,txtbillmanagerstate=null,txtbillmanagerbillin=null,txtbillmanagerbillincount=null
			,txtbillmanagerbillpay=null,txtbillmanagerbillpaycount=null,txtbillmanagerbillpayamount=null,txtbillpayin=null,
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
	private TextView txthopperincount=null,txthopperpaymoney=null;
	private EditText txthopperin1=null,txthopperin2=null,txthopperin3=null,txthopperin4=null,
			txthopperin5=null,txthopperin6=null,txthopperin7=null,txthopperin8=null,edthopperpayout=null;		
	private Button btnhopperquery=null,btnhopperpay=null,btnhopperexit=null;
	private Handler myhHandler=null;	
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
    	
		//ע��Ͷ�����������
  	    EVprotocolAPI.setCallBack(new JNIInterface() 
		{
			
			@Override
			public void jniCallback(Map<String, Object> allSet) {
				float payin_amount=0,reamin_amount=0,payout_amount=0;
				// TODO Auto-generated method stub	
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<mdb�豸���");
				Map<String, Object> Set= allSet;
				int jnirst=(Integer)Set.get("EV_TYPE");
				switch (jnirst)
				{
					case EVprotocolAPI.EV_MDB_ENABLE://�������߳�Ͷ�ҽ����Ϣ	
						//ֽ�������ɹ�
						if((Integer)Set.get("bill_result")>0)
						{
							EVprotocolAPI.EV_mdbBillInfoCheck(ToolClass.getCom_id());
							EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
						}
						//Ӳ�������ɹ�
						if((Integer)Set.get("coin_result")>0)
						{
							EVprotocolAPI.EV_mdbCoinInfoCheck(ToolClass.getCom_id());
							EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
						}
						break;
					case EVprotocolAPI.EV_MDB_B_INFO:							 
						String acceptor=((Integer)Set.get("acceptor")==2)?"MDB":"��";
						String dispenser=((Integer)Set.get("dispenser")==2)?"MDB":"��";
						String code=(String) Set.get("code");
						String sn=(String) Set.get("sn");
						String model=(String) Set.get("model");
						String ver=(String) Set.get("ver");
						int capacity=(Integer)Set.get("capacity");
						String str="ֽ�ҽ�����:"+acceptor+"ֽ��������:"+dispenser+"����:"+code
								+"���к�"+sn+" �ͺ�:"+model+"�汾��:"+ver+"������:"+capacity;
						txtbillmanagerpar.setText(str);
						if((Integer)Set.get("acceptor")==2)
							spinbillmanagerbill.setSelection((Integer)Set.get("acceptor")-1);
						else if((Integer)Set.get("acceptor")==0)
							spinbillmanagerbill.setSelection(0);	
						str="";
						Map<String,Integer> allSet1=(Map<String, Integer>) Set.get("ch_r");
						Set<Map.Entry<String,Integer>> allset=allSet1.entrySet();  //ʵ����
					    Iterator<Map.Entry<String,Integer>> iter=allset.iterator();
					    while(iter.hasNext())
					    {
					        Map.Entry<String,Integer> me=iter.next();
					        str+="[ͨ��"+me.getKey() + "]=" + ToolClass.MoneyRec(me.getValue()) + ",";
					    }
					    txtbillmanagerbillin.setText(str);
					    str="";
					    Map<String,Integer> allSet2=(Map<String, Integer>) Set.get("ch_d");
						Set<Map.Entry<String,Integer>> allset2=allSet2.entrySet();  //ʵ����
					    Iterator<Map.Entry<String,Integer>> iter2=allset2.iterator();
					    while(iter2.hasNext())
					    {
					        Map.Entry<String,Integer> me=iter2.next();
					        str+="[ͨ��"+me.getKey() + "]=" + ToolClass.MoneyRec(me.getValue()) + ",";
					    }
					    txtbillmanagerbillpay.setText(str);
						break;
					case EVprotocolAPI.EV_MDB_C_INFO:
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
						String code2=(String) Set.get("code");
						String sn2=(String) Set.get("sn");
						String model2=(String) Set.get("model");
						String ver2=(String) Set.get("ver");
						int capacity2=(Integer)Set.get("capacity");
						String str2="Ӳ�ҽ�����:"+acceptor2+"Ӳ��������:"+dispenser2+"����:"+code2
								+"���к�"+sn2;
						txtcoinmanagerpar.setText(str2);
						str2=" �ͺ�:"+model2+"�汾��:"+ver2+"������:"+capacity2;
						txtcoinmanagerpar2.setText(str2);
						spincoinmanagercoin.setSelection((Integer)Set.get("acceptor"));
						
						
						str2="";
						Map<String, Integer>  allSet3=(Map<String, Integer>) Set.get("ch_r");
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
					    txtcoinmanagercoinin9.setText(String.valueOf(all[8]));
					    txtcoinmanagercoinin10.setText(String.valueOf(all[9]));
					    txtcoinmanagercoinin11.setText(String.valueOf(all[10]));
					    txtcoinmanagercoinin12.setText(String.valueOf(all[11]));
					    txtcoinmanagercoinin13.setText(String.valueOf(all[12]));
					    txtcoinmanagercoinin14.setText(String.valueOf(all[13]));
					    txtcoinmanagercoinin15.setText(String.valueOf(all[14]));
					    txtcoinmanagercoinin16.setText(String.valueOf(all[15]));
					    //����ͨ����ֵ
					    spinhopper.setSelection((Integer)Set.get("dispenser"));
					    str2="";
					    Map<String, Integer> allSet4=(Map<String, Integer>) Set.get("ch_d");
					    ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+allSet4.toString());
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
						break;	
					case EVprotocolAPI.EV_MDB_HEART://������ѯ
						String bill_enable=((Integer)Set.get("bill_enable")==1)?"ʹ��":"����";
						txtbillmanagerstate.setText(bill_enable);
						String bill_payback=((Integer)Set.get("bill_payback")==1)?"����":"û����";
					  	txtbillpayback.setText(bill_payback);
					  	String bill_err=((Integer)Set.get("bill_err")==0)?"����":"������:"+(Integer)Set.get("bill_err");
					  	txtbillerr.setText(bill_err);
					  	double money=ToolClass.MoneyRec((Integer)Set.get("bill_recv"));					  	
					  	txtbillpayin.setText(String.valueOf(money));					  	
					  	money=ToolClass.MoneyRec((Integer)Set.get("bill_remain"));
					  	txtbillmanagerbillpayamount.setText(String.valueOf(money));
					  	
					  	String coin_enable=((Integer)Set.get("coin_enable")==1)?"ʹ��":"����";
					  	txtcoinmanagerstate.setText(coin_enable);
						String coin_payback=((Integer)Set.get("coin_payback")==1)?"����":"û����";
						txtcoinpayback.setText(coin_payback);
					  	String coin_err=((Integer)Set.get("coin_err")==0)?"����":"������:"+(Integer)Set.get("coin_err");
					  	txtcoinerr.setText(coin_err);
					  	money=ToolClass.MoneyRec((Integer)Set.get("coin_recv"));					  	
					  	txtcoinpayin.setText(String.valueOf(money));					  	
					  	money=ToolClass.MoneyRec((Integer)Set.get("coin_remain"));
					  	txtcoinmanagercoininamount.setText(String.valueOf(money));
						break;
					case EVprotocolAPI.EV_MDB_PAYOUT://����
						money=ToolClass.MoneyRec((Integer)Set.get("bill_changed"));					  	
						txtpaymoney.setText(String.valueOf(money));	
						money=ToolClass.MoneyRec((Integer)Set.get("coin_changed"));					  	
						txtcoinpaymoney.setText(String.valueOf(money));	
						txthopperpaymoney.setText(String.valueOf(money));	
						break;	
				}				
			}
			
		});
  	    //===============
    	//ֽ��������ҳ��
    	//===============
  	    spinbillmanagerbill = (Spinner) findViewById(R.id.spinbillmanagerbill);
  	    billStringArray=getResources().getStringArray(R.array.bill_label);
  	    //ʹ���Զ����ArrayAdapter
        billAdapter = new ArrayAdapter<String>(this,R.layout.viewspinner,billStringArray);
        spinbillmanagerbill.setAdapter(billAdapter);// ΪListView�б���������Դ
	  	txtbillmanagerpar = (TextView) findViewById(R.id.txtbillmanagerpar);
	  	txtbillmanagerstate = (TextView) findViewById(R.id.txtbillmanagerstate);
	  	txtbillpayback = (TextView) findViewById(R.id.txtbillpayback);
	  	txtbillerr = (TextView) findViewById(R.id.txtbillerr);
	  	txtbillmanagerbillin = (TextView) findViewById(R.id.txtbillmanagerbillin);
	  	txtbillmanagerbillincount = (TextView) findViewById(R.id.txtbillmanagerbillincount);
	  	txtbillmanagerbillpay = (TextView) findViewById(R.id.txtbillmanagerbillpay);
	  	txtbillmanagerbillpaycount = (TextView) findViewById(R.id.txtbillmanagerbillpaycount);
	  	txtbillmanagerbillpayamount = (TextView) findViewById(R.id.txtbillmanagerbillpayamount);
	  	txtbillpayin = (TextView) findViewById(R.id.txtbillpayin);
	  	txtpaymoney = (TextView) findViewById(R.id.txtpaymoney);
		edtpayout = (EditText) findViewById(R.id.edtpayout);
		btnbillpayout = (Button) findViewById(R.id.btnbillpayout);
		btnbillpayout.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	EVprotocolAPI.EV_mdbPayout(ToolClass.getCom_id(),1,0,ToolClass.MoneySend(Float.parseFloat(edtpayout.getText().toString())),0);
		    }
		});
		btnbillon = (Button) findViewById(R.id.btnbillon);
		btnbillon.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,0,1);
		    }
		});
		btnbilloff = (Button) findViewById(R.id.btnbilloff);
		btnbilloff.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,0,0);
		    }
		});
		btnbillquery = (Button) findViewById(R.id.btnbillquery);
		btnbillquery.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
		    }
		});
		btnbillset = (Button) findViewById(R.id.btnbillset);
		btnbillset.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        //finish();
		    }
		});	
		btnbillexit = (Button) findViewById(R.id.btnbillexit2);
		btnbillexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		        finish();
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
		    	EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),0,1,1);
		    }
		});
	  	btncoinoff = (Button) findViewById(R.id.btncoinoff);
	  	btncoinoff.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),0,1,0);
		    }
		});
	  	btncoinquery = (Button) findViewById(R.id.btncoinquery);
	  	btncoinquery.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
		    }
		});
	  	btncoinpayout = (Button) findViewById(R.id.btncoinpayout);
	  	btncoinpayout.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	EVprotocolAPI.EV_mdbPayout(ToolClass.getCom_id(),0,1,0,ToolClass.MoneySend(Float.parseFloat(edtcoinpayout.getText().toString())));
		    }
		});
	  	btncoinset = (Button) findViewById(R.id.btncoinset);
	  	btncoinset.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//EVprotocolAPI.EV_mdbPayout(ToolClass.getCom_id(),1,0,ToolClass.MoneySend(Float.parseFloat(edtpayout.getText().toString())),0);
		    }
		});
	  	btncoinexit = (Button) findViewById(R.id.btncoinexit);
	  	btncoinexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		        finish();
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
	  	txthopperin1 = (EditText) findViewById(R.id.txthopperin1);
	  	txthopperin2 = (EditText) findViewById(R.id.txthopperin2);
	  	txthopperin3 = (EditText) findViewById(R.id.txthopperin3);
	  	txthopperin4 = (EditText) findViewById(R.id.txthopperin4);
	  	txthopperin5 = (EditText) findViewById(R.id.txthopperin5);
	  	txthopperin6 = (EditText) findViewById(R.id.txthopperin6);
	  	txthopperin7 = (EditText) findViewById(R.id.txthopperin7);
	  	txthopperin8 = (EditText) findViewById(R.id.txthopperin8);
	  	edthopperpayout = (EditText) findViewById(R.id.edthopperpayout);
	  	btnhopperquery = (Button) findViewById(R.id.btnhopperquery);
	  	btnhopperquery.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	EVprotocolAPI.EV_mdbCoinInfoCheck(ToolClass.getCom_id());
		    }
		});
	  	btnhopperpay = (Button) findViewById(R.id.btnhopperpay);
	  	btnhopperpay.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	EVprotocolAPI.EV_mdbPayout(ToolClass.getCom_id(),0,1,0,ToolClass.MoneySend(Float.parseFloat(edthopperpayout.getText().toString())));
		    }
		});
	  	btnhopperexit = (Button) findViewById(R.id.btnhopperexit);
	  	btnhopperexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		        finish();
		    }
		});
	}
	

}
