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
import java.util.Map;

import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.common.ToolClass;
import com.example.evconsole.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddInaccount extends Activity
{
	private EditText edtpayout=null;
	private TextView txtpayin=null,txtreamin=null,txtpaymoney=null;
	private Button btnpayout=null,btnbillexit=null,btnpaymoney=null;// ����Button�����˳���
	private Handler myhHandler=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addinaccount);// ���ò����ļ�
		//ע��Ͷ�����������
  	    EVprotocolAPI.setCallBack(new JNIInterface() 
		{
			
			@Override
			public void jniCallback(Map<String, Integer> allSet) {
				float payin_amount=0,reamin_amount=0,payout_amount=0;
				// TODO Auto-generated method stub	
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<payinout���");
				Map<String, Integer> Set= allSet;
				int jnirst=Set.get("EV_TYPE");
				switch (jnirst)
				{
					case EVprotocolAPI.EV_PAYIN_RPT://�������߳�Ͷ�ҽ����Ϣ						
						payin_amount=ToolClass.MoneyRec((Integer) allSet.get("payin_amount"));
						reamin_amount=ToolClass.MoneyRec((Integer) allSet.get("reamin_amount"));
						ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<Ͷ��:"+payin_amount
								+"�ܹ�:"+reamin_amount);							
						txtpayin.setText(String.valueOf(payin_amount));
						txtreamin.setText(String.valueOf(reamin_amount));
						break;
					case EVprotocolAPI.EV_PAYOUT_RPT://�������߳���������Ϣ
						payout_amount=ToolClass.MoneyRec((Integer) allSet.get("payout_amount"));
						reamin_amount=ToolClass.MoneyRec((Integer) allSet.get("reamin_amount"));
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<������"+String.valueOf(payout_amount));							
						txtpaymoney.setText(String.valueOf(payout_amount));
						break;	
				}				
			}
			
		});
		
  	    txtpayin = (TextView) findViewById(R.id.txtpayin);
  	    txtreamin = (TextView) findViewById(R.id.txtreamin);
		edtpayout = (EditText) findViewById(R.id.edtpayout);
		txtpaymoney = (TextView) findViewById(R.id.txtpaymoney);
		EVprotocolAPI.cashControl(1);//���ձ��豸	
		//ȫ���˱�
		btnpayout = (Button) findViewById(R.id.btnpayout);
		btnpayout.setOnClickListener(new OnClickListener() {// Ϊ�˱Ұ�ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	ToolClass.Log(ToolClass.INFO,"EV_JNI","[APPsend>>]back");
		    	EVprotocolAPI.payback();
		    	txtpayin.setText("0");
		    	txtreamin.setText("0");
		    }
		});
		//����
		btnpaymoney= (Button) findViewById(R.id.btnpaymoney);
		btnpaymoney.setOnClickListener(new OnClickListener() {// Ϊ�˱Ұ�ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	ToolClass.Log(ToolClass.INFO,"EV_JNI","[APPsend>>]"+edtpayout.getText().toString());
		    	EVprotocolAPI.payout(ToolClass.MoneySend(Float.parseFloat(edtpayout.getText().toString())));
		    	txtpayin.setText("0");
		    	txtreamin.setText("0");
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
