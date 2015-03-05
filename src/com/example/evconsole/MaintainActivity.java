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

package com.example.evconsole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.easivend.evprotocol.EVprotocol;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.common.PictureAdapter;
import com.easivend.common.ToolClass;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MaintainActivity extends Activity
{
	TextView txtcom=null;
	private GridView gvInfo;// ����GridView����
	// �����ַ������飬�洢ϵͳ����
    private String[] titles = new String[] { "�ֽ��豸����", "��������", "�����Ӳ���", "��Ʒ����", "Ԥ���ӿ�", "��������", "Ԥ���ӿ�", "�˳�" };
    // ����int���飬�洢���ܶ�Ӧ��ͼ��
    private int[] images = new int[] { R.drawable.addoutaccount, R.drawable.addinaccount, R.drawable.outaccountinfo, R.drawable.inaccountinfo,
            R.drawable.showinfo, R.drawable.sysset, R.drawable.accountflag, R.drawable.exit };
    //EVprotocolAPI ev=null;
    int comopen=0;//1�����Ѿ��򿪣�0����û�д�
    String str=null;
    //private Handler myhHandler=null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintain);	
		//ע�ᴮ�ڼ�����
		EVprotocolAPI.setCallBack(new JNIInterface() {
			
			@Override
			public void jniCallback(Map<String, Integer> allSet) {
				// TODO Auto-generated method stub
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<main������");	
				Map<String, Integer> Set= allSet;
				int jnirst=Set.get("EV_TYPE");
				txtcom.setText(String.valueOf(jnirst));
				switch (jnirst)
				{
					case EVprotocolAPI.EV_ONLINE://�������߳���Ϣ
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ʼ�����");	
						txtcom.setText(str+"��ʼ�����");
						break;
				}
			}
		}); 
		
		
		Intent intent=getIntent();
		str=intent.getStringExtra("comport");
		txtcom=(TextView)super.findViewById(R.id.txtcom);
		txtcom.setText("����׼������"+str);	
		
		//�򿪴���		
		//ev=new EVprotocolAPI();
		comopen = EVprotocolAPI.vmcStart(str);
		if(comopen == 1)
		{
			txtcom.setText(str+"���ڴ򿪳ɹ�");			
		}
		else
			txtcom.setText(str+"���ڴ�ʧ��");
		
				
		
		gvInfo = (GridView) findViewById(R.id.gvInfo);// ��ȡ�����ļ��е�gvInfo���
        PictureAdapter adapter = new PictureAdapter(titles, images, this);// ����pictureAdapter����
        gvInfo.setAdapter(adapter);// ΪGridView��������Դ
        gvInfo.setOnItemClickListener(new OnItemClickListener() {// ΪGridView��������¼�
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = null;// ����Intent����
                switch (arg2) {
                case 0:
                    intent = new Intent(MaintainActivity.this, AddInaccount.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
                    startActivity(intent);// ��AddInaccount
                    break;
                case 1:
                    intent = new Intent(MaintainActivity.this, HuodaoTest.class);// ʹ��HuodaoTest���ڳ�ʼ��Intent
                    startActivity(intent);// ��HuodaoTest
                    break;
                case 2:
                    //intent = new Intent(MainActivity.this, Inaccountinfo.class);// ʹ��Inaccountinfo���ڳ�ʼ��Intent
                    //startActivity(intent);// ��Inaccountinfo
                    break;    
                case 3:
                    intent = new Intent(MaintainActivity.this, GoodsManager.class);// ʹ��GoodsManager���ڳ�ʼ��Intent
                    startActivity(intent);// ��GoodsManager
                    break;                
                case 4:
                    //intent = new Intent(MainActivity.this, Showinfo.class);// ʹ��Showinfo���ڳ�ʼ��Intent
                    //startActivity(intent);// ��Showinfo
                    break;
                case 5:
                	intent = new Intent(MaintainActivity.this, ParamManager.class);// ʹ��ParamManager���ڳ�ʼ��Intent
                    startActivity(intent);// ��ParamManager
                    break;
                case 6:
                    //intent = new Intent(MainActivity.this, Accountflag.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                    //startActivity(intent);// ��Accountflag
                    break;
                case 7:
                    finish();// �رյ�ǰActivity
                }
            }
        });

	}
	@Override
	protected void onDestroy() {
		//�رմ���
		if(comopen>0)	
			EVprotocolAPI.vmcStop();
		// TODO Auto-generated method stub
		super.onDestroy();		
	}

	
	

}


