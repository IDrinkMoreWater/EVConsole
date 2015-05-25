/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           Login.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        ��½ѡ�񴮿ں�ҳ��          
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/


package com.easivend.app.maintain;


import java.util.Map;

import com.easivend.alipay.AlipayConfigAPI;
import com.easivend.app.business.Business;
import com.easivend.common.ToolClass;
import com.easivend.weixing.WeiConfigAPI;
import com.example.evconsole.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity 
{
	private EditText txtlogin,txtbent;// ����EditText����
    private Button btnlogin, btnclose;// ��������Button����
    String com =null;
    String bentcom =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);// ���ò����ļ�

        txtlogin = (EditText) findViewById(R.id.txtLogin);// ��ȡ���ں��ı���
        txtbent = (EditText) findViewById(R.id.txtbent);// ��ȡ���ں��ı���
        Map<String, String> list=ToolClass.ReadConfigFile();
        if(list!=null)
        {
	        com = list.get("com");
	        bentcom = list.get("bentcom");
        }
        txtlogin.setText(com);
        txtbent.setText(bentcom);
        btnlogin = (Button) findViewById(R.id.btnLogin);// ��ȡ�޸İ�ť
        btnclose = (Button) findViewById(R.id.btnClose);// ��ȡȡ����ť
        btnclose.setOnClickListener(new OnClickListener() {// Ϊȡ����ť���ü����¼�
            @Override
            public void onClick(View arg0) {
                finish();// �˳���ǰ����
            }
        });
        btnlogin.setOnClickListener(new OnClickListener() {// Ϊ��¼��ť���ü����¼�
            @Override
            public void onClick(View arg0)
            {
            	com = txtlogin.getText().toString();
    	        bentcom = txtbent.getText().toString();    	        
            	ToolClass.WriteConfigFile(com, bentcom);
            	ToolClass.addOptLog(Login.this,1,"�޸Ĵ���:");
	            // ������Ϣ��ʾ
	            Toast.makeText(Login.this, "���޸Ĵ��ڡ��ɹ���", Toast.LENGTH_SHORT).show();
            	finish();// �˳���ǰ����           
            }
        });        
	}
	
}
