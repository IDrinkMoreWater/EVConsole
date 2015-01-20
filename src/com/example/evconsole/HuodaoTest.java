/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           HuodaoTest.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        ��������ҳ��          
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

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class HuodaoTest extends Activity 
{
	private int device=0;//�������		
	private int status=0;//�������
	private int hdid=0;//����id
	private int hdtype=0;//��������
	private int cost=0;//��Ǯ
	private int totalvalue=0;//ʣ����
	private int huodao=0;//ʣ��������
	private TextView txthuorst=null;
	private Button btnhuochu=null;// ����Button���󡰳�����
	private Button btnhuocancel=null;// ����Button�������á�
	private Button btnhuoexit=null;// ����Button�����˳���
	private EditText edtcolumn=null,edtprice=null;
	private RadioGroup cabinet=null, type=null;
	private RadioButton mainhuodao=null,fuhuodao=null,moneychu=null,pcchu=null;
	private int cabinetvar=0,typevar=0;
	private Handler myhHandler=null;
	//EVprotocolAPI ev=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.huodao);// ���ò����ļ�
		myhHandler=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub				
				switch (msg.what)
				{
					case EVprotocolAPI.EV_TRADE_RPT://�������߳���Ϣ
						Log.i("EV_JNI","�������");	
						Map<String,Integer> allSet = new HashMap<String,Integer>();
						allSet=(Map<String, Integer>) msg.obj;
						device=allSet.get("cabinet");//�������
						status=allSet.get("result");//�������
						hdid=allSet.get("column");//����id
						hdtype=allSet.get("type");//��������
						cost=allSet.get("cost");//��Ǯ
						totalvalue=allSet.get("remainAmount");//ʣ����
						huodao=allSet.get("remainCount");//ʣ��������
						txthuorst.setText("device=["+device+"],status=["+status+"],hdid=["+hdid+"],type=["+hdtype+"],cost=["
								+cost/100+"],totalvalue=["+totalvalue/100+"],huodao=["+huodao+"]");
						break;
				}				
			}
			
		};
		EVprotocolAPI.setHandler(myhHandler);
		txthuorst=(TextView)findViewById(R.id.txthuorst);
		btnhuochu = (Button) findViewById(R.id.btnhuochu);
		btnhuocancel = (Button) findViewById(R.id.btnhuocancel);
		btnhuoexit = (Button) findViewById(R.id.btnhuoexit);
		edtcolumn = (EditText) findViewById(R.id.edtcolumn);
		edtprice = (EditText) findViewById(R.id.edtprice);
		//�õ��Ǹ������
		this.cabinet = (RadioGroup) super.findViewById(R.id.cabinet);
	    this.mainhuodao = (RadioButton) super.findViewById(R.id.mainhuodao);
	    this.fuhuodao = (RadioButton) super.findViewById(R.id.fuhuodao);		
	    this.cabinet.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// �������
				if(mainhuodao.getId()==checkedId)
				{
					cabinetvar=1;
				}
				//�������
				else if(fuhuodao.getId()==checkedId)
				{
					cabinetvar=2;
				}
			}
		});
	    //�õ�������ʽ
  		this.type = (RadioGroup) super.findViewById(R.id.type);
  	    this.moneychu = (RadioButton) super.findViewById(R.id.moneychu);
  	    this.pcchu = (RadioButton) super.findViewById(R.id.pcchu);		
  	    this.type.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
  			@Override
  			public void onCheckedChanged(RadioGroup group, int checkedId) {
  				// �ֽ����
  				if(moneychu.getId()==checkedId)
  				{
  					typevar=0;
  				}
  				//PC����
  				else if(pcchu.getId()==checkedId)
  				{
  					typevar=2;
  				}
  			}
  		});
		
		btnhuochu.setOnClickListener(new OnClickListener() {// Ϊ������ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {		    	  
//		    	Log.i("EV_JNI",
//		    	"[send]cabinet="+String.valueOf(cabinetvar)
//		    	+" column="+String.valueOf(Integer.parseInt(edtcolumn.getText().toString()))
//		    	+" type="+String.valueOf(typevar)
//		    	+" price="+String.valueOf(Float.parseFloat(edtprice.getText().toString())*100)
//		    	);
		    	EVprotocolAPI.trade(cabinetvar,Integer.parseInt(edtcolumn.getText().toString()),typevar,
		    			Float.parseFloat(edtprice.getText().toString()));		    	
		    	//txthuorst.setText(EVprotocolAPI.gethuodaorst());
		    }
		});
		btnhuocancel.setOnClickListener(new OnClickListener() {// Ϊ���ð�ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	edtcolumn.setText("");// ���ý���ı���Ϊ��
		    	edtprice.setText("");// ����ʱ���ı���Ϊ��		        
		    }
		});
		btnhuoexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		        finish();
		    }
		});
	}
	
}
