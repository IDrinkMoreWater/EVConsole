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

import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_cabinetDAO;
import com.easivend.dao.vmc_orderDAO;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity 
{
	private EditText txtlogin,txtbent,txtcolumn,txtserver,txtip;// ����EditText����
	private TextView tvip=null,tvVersion=null;
    private Button btnlogin, btnclose,btnGaoji,btnDel;// ��������Button����
    private Switch switchallopen;
    String com =null;
    String bentcom =null;
    String columncom =null;
    String server =null,sercom=null,serip=null;
    int isallopen=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);// ���ò����ļ�
		//���ú������������Ĳ��ֲ���
		this.setRequestedOrientation(ToolClass.getOrientation());		
		switchallopen = (Switch)findViewById(R.id.switchallopen); //��ȡ���ؼ�  
        txtlogin = (EditText) findViewById(R.id.txtLogin);// ��ȡ���ں��ı���
        txtbent = (EditText) findViewById(R.id.txtbent);// ��ȡ���ں��ı���
        txtcolumn = (EditText) findViewById(R.id.txtcolumn);// ��ȡ���ں��ı���
        txtserver = (EditText) findViewById(R.id.txtserver);// ��ȡ����˹�˾��ַ�ı���
        txtip = (EditText) findViewById(R.id.txtip);// ��ȡ�����ip��ַ�ı���
        tvip = (TextView) findViewById(R.id.tvip);
        tvVersion = (TextView) findViewById(R.id.tvVersion);//�����汾��
        btnDel = (Button) findViewById(R.id.btnDel);// ��ȡ���ȫ����Ʒ������Ϣ
        tvip.setVisibility(View.GONE);
        txtip.setVisibility(View.GONE);
        btnDel.setVisibility(View.GONE);
        tvVersion.setText("�汾�ţ�"+ToolClass.getVersion());
        Map<String, String> list=ToolClass.ReadConfigFile();
        if(list!=null)
        {
	        com = list.get("com");
	        bentcom = list.get("bentcom");
	        columncom = list.get("columncom");
	        if(list.containsKey("server"))
	        {
	        	server = list.get("server");
	        	sercom=server.substring(server.lastIndexOf('/')+1,server.length());
	        	serip=server.substring(0,server.lastIndexOf('/')+1);
	        }	        
	        if(list.containsKey("isallopen"))
	        {
	        	isallopen=Integer.parseInt(list.get("isallopen"));
	        }
        }
        txtlogin.setText(com);
        txtbent.setText(bentcom);
        txtcolumn.setText(columncom);
        txtserver.setText(sercom);
        txtip.setText(serip);
        switchallopen.setChecked((isallopen==1)?true:false);
        btnlogin = (Button) findViewById(R.id.btnLogin);// ��ȡ�޸İ�ť
        btnclose = (Button) findViewById(R.id.btnClose);// ��ȡȡ����ť
        btnGaoji = (Button) findViewById(R.id.btnGaoji);// ��ȡ�߼���ť
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
    	        columncom = txtcolumn.getText().toString(); 
    	        isallopen= (switchallopen.isChecked()==true)?1:0;
    	        serip = txtip.getText().toString();
    	        sercom = txtserver.getText().toString(); 
    	        server=serip+sercom;
            	ToolClass.WriteConfigFile(com, bentcom,columncom,server,String.valueOf(isallopen));            	
            	ToolClass.addOptLog(Login.this,1,"�޸Ĵ���:");
	            // ������Ϣ��ʾ
	            Toast.makeText(Login.this, "���޸Ĵ��ڡ��ɹ���", Toast.LENGTH_SHORT).show();
	            //�˳�ʱ������intent
	            Intent intent=new Intent();
	            setResult(MaintainActivity.RESULT_OK,intent);
            	finish();// �˳���ǰ����           
            }
        });
        btnGaoji.setOnClickListener(new OnClickListener() {// Ϊȡ����ť���ü����¼�
            @Override
            public void onClick(View arg0) {
                if(txtip.getVisibility()==View.GONE)
                {
                	tvip.setVisibility(View.VISIBLE);
                    txtip.setVisibility(View.VISIBLE);
                    btnDel.setVisibility(View.VISIBLE);
                }
                else
                {
                	tvip.setVisibility(View.GONE);
                    txtip.setVisibility(View.GONE);
                    btnDel.setVisibility(View.GONE);
				}
            }
        });
        btnDel.setOnClickListener(new OnClickListener() {// Ϊȡ����ť���ü����¼�
            @Override
            public void onClick(View arg0) {
            	//��������Ի���
            	Dialog alert=new AlertDialog.Builder(Login.this)
            		.setTitle("�Ի���")//����
            		.setMessage("��ȷ��Ҫɾ��ȫ����Ʒ������Ϣ��")//��ʾ�Ի����е�����
            		.setIcon(R.drawable.ic_launcher)//����logo
            		.setPositiveButton("ɾ��", new DialogInterface.OnClickListener()//�˳���ť���������ü����¼�
            			{				
        	    				@Override
        	    				public void onClick(DialogInterface dialog, int which) 
        	    				{
        	    					// TODO Auto-generated method stub	
        	    					// ����InaccountDAO����
        	    					vmc_cabinetDAO cabinetDAO = new vmc_cabinetDAO(Login.this);
        				            cabinetDAO.deteleall();// ɾ��
        				            //ɾ�����׼�¼
        				            vmc_orderDAO orderDAO = new vmc_orderDAO(Login.this);
        				            orderDAO.deteleall();
        				            ToolClass.addOptLog(Login.this,2,"ɾ��ȫ����Ʒ������Ϣ");
        	    					// ������Ϣ��ʾ
        				            Toast.makeText(Login.this, "ɾ���ɹ���", Toast.LENGTH_SHORT).show();						            
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
        });
	}
	
	@Override
	protected void onDestroy() {
    	//�˳�ʱ������intent
        Intent intent=new Intent();
        setResult(MaintainActivity.RESULT_CANCELED,intent);
		super.onDestroy();		
	}
	
}
