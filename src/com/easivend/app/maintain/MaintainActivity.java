/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           MaintainActivity.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        ά���˵���ҳ��          
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.app.maintain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.easivend.evprotocol.EVprotocol;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.weixing.WeiConfigAPI;
import com.easivend.alipay.AlipayConfigAPI;
import com.easivend.app.business.Business;
import com.easivend.common.PictureAdapter;
import com.easivend.common.ProPictureAdapter;
import com.easivend.common.ToolClass;
import com.example.evconsole.R;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MaintainActivity extends Activity
{
	TextView txtcom=null,txtbentcom=null;
	private GridView gvInfo;// ����GridView����
	private ProgressBar barmaintain=null;
	// �����ַ������飬�洢ϵͳ����
    private String[] titles = new String[] { "��Ʒ����", "��������", "��������", "������Ϣ", "������־", "��������", "����ҳ��", "�˳�" };
    // ����int���飬�洢���ܶ�Ӧ��ͼ��
    private int[] images = new int[] { R.drawable.addoutaccount, R.drawable.addinaccount, R.drawable.outaccountinfo, R.drawable.showinfo,
            R.drawable.inaccountinfo, R.drawable.sysset, R.drawable.accountflag, R.drawable.exit };
    //EVprotocolAPI ev=null;
    int comopen=0,bentopen=0;//1�����Ѿ��򿪣�0����û�д�    
    String com=null,bentcom=null;
    final static int REQUEST_CODE=1;
    //private Handler myhHandler=null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintain);	
		//ע�ᴮ�ڼ�����
		EVprotocolAPI.setCallBack(new JNIInterface() {
			
			@Override
			public void jniCallback(Map<String, Object> allSet) {
				// TODO Auto-generated method stub
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<main������");	
				Map<String, Object> Set= allSet;
				int jnirst=(Integer) Set.get("EV_TYPE");
				//txtcom.setText(String.valueOf(jnirst));
				switch (jnirst)
				{
					case EVprotocolAPI.EV_REGISTER://�������߳���Ϣ
						//�����ʼ�����
						if(Set.get("port_com").equals(com))
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�����ʼ�����");	
							txtcom.setText(com+"[����]��ʼ�����");		
							ToolClass.setCom_id((Integer)Set.get("port_id"));
						}
						else if(Set.get("port_com").equals(bentcom))
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<���ӹ��ʼ�����");	
							txtbentcom.setText(bentcom+"[���ӹ�]��ʼ�����");		
							ToolClass.setBentcom_id((Integer)Set.get("port_id"));
						}
										
						break;
				}
			}
		}); 
		txtcom=(TextView)super.findViewById(R.id.txtcom);
		txtbentcom=(TextView)super.findViewById(R.id.txtbentcom);
		
		//�������ļ���ȡ����
		Map<String, String> list=ToolClass.ReadConfigFile();
		if(list!=null)
		{
	        com = list.get("com");
	        bentcom = list.get("bentcom");
	        AlipayConfigAPI.SetAliConfig(list);//���ð����˺�
	        WeiConfigAPI.SetWeiConfig(list);//����΢���˺�	        
			txtcom.setText(com+"��������׼������");	
			EVprotocolAPI.vmcEVStart();//��������
//			//�򿪴���		
//			comopen = EVprotocolAPI.vmcStart(com);
//			if(comopen == 1)
//			{
//				txtcom.setText(com+"[����]���ڴ򿪳ɹ�");			
//			}
//			else
//			{
//				txtcom.setText(Comb+"[����]���ڴ�ʧ��");
//			}			
			txtbentcom.setText(bentcom+"[���ӹ�]����׼������");	
			//�򿪸��ӹ�
			bentopen = EVprotocolAPI.bentoRegister(bentcom);
			if(bentopen == 1)
			{
				txtbentcom.setText(bentcom+"[���ӹ�]��������׼������");			
			}
			else
			{
				txtbentcom.setText(bentcom+"[���ӹ�]���ڴ�ʧ��");
			}
		}
				
		
				
		barmaintain= (ProgressBar) findViewById(R.id.barmaintain);
		gvInfo = (GridView) findViewById(R.id.gvInfo);// ��ȡ�����ļ��е�gvInfo���
        PictureAdapter adapter = new PictureAdapter(titles, images, this);// ����pictureAdapter����
        gvInfo.setAdapter(adapter);// ΪGridView��������Դ
        gvInfo.setOnItemClickListener(new OnItemClickListener() {// ΪGridView��������¼�
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = null;// ����Intent����
                switch (arg2) {
                case 0:
                	barmaintain.setVisibility(View.VISIBLE);                	
                	intent = new Intent(MaintainActivity.this, GoodsManager.class);// ʹ��GoodsManager���ڳ�ʼ��Intent
                	startActivityForResult(intent,REQUEST_CODE);// ��GoodsManager
                    break;
                case 1:
                	barmaintain.setVisibility(View.VISIBLE); 
                    intent = new Intent(MaintainActivity.this, HuodaoTest.class);// ʹ��HuodaoTest���ڳ�ʼ��Intent
                    startActivityForResult(intent,REQUEST_CODE);// ��HuodaoTest
                    break;
                case 2:
                	barmaintain.setVisibility(View.VISIBLE);
                	intent = new Intent(MaintainActivity.this, ParamManager.class);// ʹ��ParamManager���ڳ�ʼ��Intent
                    startActivityForResult(intent,REQUEST_CODE);// ��ParamManager                    
                    break;    
                case 3:
                	intent = new Intent(MaintainActivity.this, Order.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                    startActivity(intent);// ��Accountflag
                    break;                
                case 4:
                	intent = new Intent(MaintainActivity.this, LogOpt.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                    startActivity(intent);// ��Accountflag
                    break;
                case 5:
                	intent = new Intent(MaintainActivity.this, Login.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                    startActivity(intent);// ��Accountflag
                    break;
                case 6:
                    intent = new Intent(MaintainActivity.this, Business.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                    startActivity(intent);// ��Accountflag
                    break;
                case 7:
                    finish();// �رյ�ǰActivity
                }
            }
        });

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO Auto-generated method stub
		if(requestCode==REQUEST_CODE)
		{
			if(resultCode==GoodsManager.RESULT_CANCELED)
			{				
				barmaintain.setVisibility(View.GONE);
			}			
		}
	}
	
	@Override
	protected void onDestroy() {
		EVprotocolAPI.vmcEVStop();//�رռ���
		//�رմ���
		//if(comopen>0)	
			//EVprotocolAPI.vmcStop();
		if(bentopen>0)
			EVprotocolAPI.bentoRelease(ToolClass.getBentcom_id());
		EVprotocolAPI.vmcEVStop();//�رռ���
		// TODO Auto-generated method stub
		super.onDestroy();		
	}
}


