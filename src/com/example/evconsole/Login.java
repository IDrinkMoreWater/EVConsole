package com.example.evconsole;


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
    private Button btnlogin, btnclose;// ��������Button����
    
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
	}
	
}
