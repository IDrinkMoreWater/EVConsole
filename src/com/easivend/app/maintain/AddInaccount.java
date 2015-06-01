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
	private Spinner spinbillmanagerbill=null;
	private EditText edtpayout=null;
	private TextView txtbillmanagerpar=null,txtbillmanagerstate=null,txtbillmanagerbillin=null,txtbillmanagerbillincount=null
			,txtbillmanagerbillpay=null,txtbillmanagerbillpaycount=null,txtbillmanagerbillpayamount=null,txtbillpayin=null,
			txtpaymoney=null,txtbillpayback=null,txtbillerr=null;
	private Button btnbillon=null,btnbilloff=null,btnbillquery=null,btnbillset=null,btnbillexit=null,btnbillpayout=null;// ����Button�����˳���
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
    	myTabpay.setIndicator("����������");
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
						{}
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
					case EVprotocolAPI.EV_MDB_HEART://�������߳�Ͷ�ҽ����Ϣ		
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
						break;
					case EVprotocolAPI.EV_MDB_PAYOUT://�������߳���������Ϣ
						money=ToolClass.MoneyRec((Integer)Set.get("bill_changed"));					  	
						txtpaymoney.setText(String.valueOf(money));	
						money=ToolClass.MoneyRec((Integer)Set.get("coin_changed"));					  	
						//txtpaymoney.setText(String.valueOf(money));	
						break;	
				}				
			}
			
		});
  	    //===============
    	//ֽ��������ҳ��
    	//===============
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
		btnbillexit = (Button) findViewById(R.id.btnbillexit);
		btnbillexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		        finish();
		    }
		});
		
	}
	@Override
	protected void onDestroy() {		
		EVprotocolAPI.cashControl(0);//�ر��ձ��豸	
		super.onDestroy();
	}

}
