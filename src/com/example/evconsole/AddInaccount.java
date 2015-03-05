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

package com.example.evconsole;


import java.util.HashMap;
import java.util.Map;

import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.common.ToolClass;

import android.app.Activity;
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
	private TextView txtbillin=null,txtcoinin=null,txtpaymoney=null;
	private Button btnpayout=null,btnbillexit=null;// ����Button�����˳���
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
				float amount=0;
				// TODO Auto-generated method stub	
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<payinout���");
				Map<String, Integer> Set= allSet;
				int jnirst=Set.get("EV_TYPE");
				switch (jnirst)
				{
					case EVprotocolAPI.EV_PAYIN_RPT://�������߳�Ͷ�ҽ����Ϣ						
						amount=ToolClass.MoneyRec((Integer) allSet.get("amount"));
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<Ͷ�ҽ��"+String.valueOf(amount));							
						txtbillin.setText(String.valueOf(amount));
						break;
					case EVprotocolAPI.EV_PAYOUT_RPT://�������߳���������Ϣ
						amount=ToolClass.MoneyRec((Integer) allSet.get("amount"));
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<������"+String.valueOf(amount));							
						txtpaymoney.setText(String.valueOf(amount));
						break;	
				}				
			}
			
		});
		
		txtbillin = (TextView) findViewById(R.id.txtbillin);
		txtcoinin = (TextView) findViewById(R.id.txtcoinin);
		edtpayout = (EditText) findViewById(R.id.edtpayout);
		txtpaymoney = (TextView) findViewById(R.id.txtpaymoney);
		btnpayout = (Button) findViewById(R.id.btnpayout);
		btnpayout.setOnClickListener(new OnClickListener() {// Ϊ�˱Ұ�ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	ToolClass.Log(ToolClass.INFO,"EV_JNI","[APPsend>>]"+edtpayout.getText().toString());
		    	EVprotocolAPI.payout(ToolClass.MoneySend(Float.parseFloat(edtpayout.getText().toString())));
		    	txtbillin.setText("0");
		    	txtcoinin.setText("0");
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

}
