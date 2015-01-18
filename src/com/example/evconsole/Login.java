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


package com.example.evconsole;


import com.example.business.Business;

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
	private EditText txtlogin;// ����EditText����
    private Button btnlogin, btnclose,btnbusiness;// ��������Button����
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);// ���ò����ļ�

        txtlogin = (EditText) findViewById(R.id.txtLogin);// ��ȡ���ں��ı���
        btnlogin = (Button) findViewById(R.id.btnLogin);// ��ȡ��¼��ť
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
                Intent intent = new Intent(Login.this, MaintainActivity.class);// ����Intent����
                intent.putExtra("comport", txtlogin.getText().toString());
                startActivity(intent);// ������Activity               
            }
        });
        btnbusiness = (Button) findViewById(R.id.btnBusiness);// ��ȡ���װ�ť
        btnbusiness.setOnClickListener(new OnClickListener() {// Ϊ���װ�ť���ü����¼�
            @Override
            public void onClick(View arg0) 
            {
                Intent intent = new Intent(Login.this, Business.class);// ����Intent����
                intent.putExtra("comport", txtlogin.getText().toString());
                startActivity(intent);// ������Activity               
            }
        });
	}
	
}
