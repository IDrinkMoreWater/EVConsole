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

package com.easivend.app.maintain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.easivend.dao.vmc_cabinetDAO;
import com.easivend.dao.vmc_classDAO;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.model.Tb_vmc_cabinet;
import com.easivend.model.Tb_vmc_class;
import com.easivend.model.Tb_vmc_column;
import com.easivend.common.HuoPictureAdapter;
import com.easivend.common.ProPictureAdapter;
import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_CabinetAdapter;
import com.easivend.common.Vmc_ClassAdapter;
import com.easivend.common.Vmc_HuoAdapter;
import com.easivend.common.Vmc_ProductAdapter;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SlidingDrawer;
import android.widget.Switch;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class HuodaoTest extends TabActivity 
{
	private TabHost mytabhost = null;
	private ProgressBar barhuomanager=null;
	private int[] layres=new int[]{R.id.tab_huodaomanager,R.id.tab_huodaotest};//��Ƕ�����ļ���id
	private TextView txthuosetrst=null;
	private int con=1;//��ѯ���Ӵ���
	private int ishuoquery=0;//�Ƿ����ڲ�ѯ1,���ڲ�ѯ,0��ѯ���
	Timer timer = new Timer(); 
	private Button btnhuosetadd=null,btnhuosetdel=null,btnhuosetbu=null,btnhuosetexit=null;
	private Spinner spinhuosetCab=null,spinhuotestCab=null;
	private String[] cabinetID=null;//���������������
	private int[] cabinetType = null;//�����������������
	private int cabinetsetvar=0,cabinetTypesetvar=0;
	Map<String, Integer> huoSet= new LinkedHashMap<String,Integer>();
	private int huonum=0;//�����������
	private int huonno=0;//ѭ�������ڼ���������
	// ��������б�
	Vmc_HuoAdapter huoAdapter=null;
	GridView gvhuodao=null;
	private final static int REQUEST_CODE=1;//���������ʶ
	
	private int device=0;//�������		
	private int status=0;//�������
	private int hdid=0;//����id
	private int cool=0;//�Ƿ�֧������ 	 	1:֧�� 0:��֧��
	private int hot=0;//�Ƿ�֧�ּ���  		1:֧�� 0:��֧��
	private int light=0;//�Ƿ�֧������  	1:֧�� 0:��֧��
	private TextView txthuorst=null,txthuotestrst=null;
	private Button btnhuochu=null,btnhuochuall=null;// ����Button���󡰳�����
	private Button btnhuocancel=null;// ����Button�������á�
	private Button btnhuoexit=null;// ����Button�����˳���
	private EditText edtcolumn=null;
	private TextView txtlight=null,txtcold=null,txthot=null;
	private Switch switchlight = null,switcold = null,switchhot = null;
	private int cabinetvar=0,cabinetTypevar=0;
	private Handler myhHandler=null;
	//EVprotocolAPI ev=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.huodao);// ���ò����ļ�
		this.mytabhost = super.getTabHost();//ȡ��TabHost����
        LayoutInflater.from(this).inflate(R.layout.huodao, this.mytabhost.getTabContentView(),true);
        //����Tab�����
        TabSpec myTabhuodaomana=this.mytabhost.newTabSpec("tab0");
        myTabhuodaomana.setIndicator("��������");
        myTabhuodaomana.setContent(this.layres[0]);
    	this.mytabhost.addTab(myTabhuodaomana); 
    	
    	TabSpec myTabhuodaotest=this.mytabhost.newTabSpec("tab1");
    	myTabhuodaotest.setIndicator("��������");
    	myTabhuodaotest.setContent(this.layres[1]);
    	this.mytabhost.addTab(myTabhuodaotest); 
    	
    	//ע�����������
  	    EVprotocolAPI.setCallBack(new JNIInterface() {
			
			@Override
			public void jniCallback(Map<String, Object> allSet) {
				// TODO Auto-generated method stub
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<huodao�������");
				Map<String, Object> Set= allSet;
				int jnirst=(Integer)Set.get("EV_TYPE");
				switch (jnirst)
				{
					case EVprotocolAPI.EV_TRADE_RPT://�������߳���Ϣ
//						device=allSet.get("device");//�������
//						status=allSet.get("status");//�������
//						hdid=allSet.get("hdid");//����id
//						hdtype=allSet.get("type");//��������
//						cost=ToolClass.MoneyRec(allSet.get("cost"));//��Ǯ
//						totalvalue=ToolClass.MoneyRec(allSet.get("totalvalue"));//ʣ����
//						huodao=allSet.get("huodao");//ʣ��������
//						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�������"+"device=["+device+"],status=["+status+"],hdid=["+hdid+"],type=["+hdtype+"],cost=["
//								+cost+"],totalvalue=["+totalvalue+"],huodao=["+huodao+"]");	
//						
//						txthuorst.setText("device=["+device+"],status=["+status+"],hdid=["+hdid+"],type=["+hdtype+"],cost=["
//								+cost+"],totalvalue=["+totalvalue+"],huodao=["+huodao+"]");
//						sethuorst(status);
						break;
					case EVprotocolAPI.EV_COLUMN_RPT://�������߳���Ϣ
//						huoSet.clear();
//						//�������
//				        Set<Entry<String, Integer>> allmap=Set.entrySet();  //ʵ����
//				        Iterator<Entry<String, Integer>> iter=allmap.iterator();
//				        while(iter.hasNext())
//				        {
//				            Entry<String, Integer> me=iter.next();
//				            if(me.getKey().equals("EV_TYPE")!=true)
//				            	huoSet.put(me.getKey(), me.getValue());
//				        } 
//						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����״̬:"+huoSet.toString());	
//						showhuodao();						
						break;
					case EVprotocolAPI.EV_BENTO_CHECK://���ӹ��ѯ
						ishuoquery=0;
						String tempno=null;
						
						cool=(Integer)Set.get("cool");
						hot=(Integer)Set.get("hot");
						light=(Integer)Set.get("light");
						ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����cool:"+cool+",hot="+hot+",light="+light);
						if(light>0)
						{
							txtlight.setText("֧��");
							switchlight.setEnabled(true);
							
						}
						else
						{
							txtlight.setText("��֧��");
							switchlight.setEnabled(false);
						}
						if(cool>0)
						{
							txtcold.setText("֧��");
							switcold.setEnabled(true);
						}
						else
						{
							txtcold.setText("��֧��");
							switcold.setEnabled(false);
						}
						if(hot>0)
						{
							txthot.setText("֧��");
							switchhot.setEnabled(true);
						}
						else
						{
							txthot.setText("��֧��");
							switchhot.setEnabled(false);
						}
						
						huoSet.clear();
						//�������
				        Set<Entry<String, Object>> allmap=Set.entrySet();  //ʵ����
				        Iterator<Entry<String, Object>> iter=allmap.iterator();
				        while(iter.hasNext())
				        {
				            Entry<String, Object> me=iter.next();
				            if(
				               (me.getKey().equals("EV_TYPE")!=true)&&(me.getKey().equals("cool")!=true)
				               &&(me.getKey().equals("hot")!=true)&&(me.getKey().equals("light")!=true)
				            )   
				            {
				            	if(Integer.parseInt(me.getKey())<10)
				    				tempno="0"+me.getKey();
				    			else 
				    				tempno=me.getKey();
				            	
				            	huoSet.put(tempno, (Integer)me.getValue());
				            }
				        } 
				        huonum=huoSet.size();
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+huonum+"����״̬:"+huoSet.toString());	
						showhuodao();						
						break;	
					case EVprotocolAPI.EV_BENTO_OPEN://���ӹ����
						device=(Integer)allSet.get("addr");//�������						
						hdid=(Integer)allSet.get("box");//����id
						status=(Integer)allSet.get("result");//�������
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�������"+"device=["+device+"],hdid=["+hdid+"],status=["+status+"]");	
						
						txthuorst.setText("device=["+device+"],hdid=["+hdid+"],status=["+status+"]");
						sethuorst(status);
						//ѭ����������������
						if((huonno>0)&&(huonno<huonum))
						{							
							huonno++;
							//���ӹ�
							if(cabinetTypevar==5)
							{
								ToolClass.Log(ToolClass.INFO,"EV_JNI",
								    	"[APPsend>>]cabinet="+String.valueOf(cabinetvar)
								    	+" cabType="+String.valueOf(cabinetTypevar)
								    	+" column="+huonno		    	
								    	);	 
								EVprotocolAPI.EV_bentoOpen(ToolClass.getBentcom_id(),cabinetvar,huonno);						
							}
						}
						else if(huonno>=huonum)
						{
							huonno=0;
						}
						break;	
					case EVprotocolAPI.EV_BENTO_LIGHT://���ӹ񿪵�
						device=(Integer)allSet.get("addr");//���						
						int opt=(Integer)allSet.get("opt");//����id
						status=(Integer)allSet.get("result");//���
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����������"+"device=["+device+"],opt=["+opt+"],status=["+status+"]");	
						txthuorst.setText("device=["+device+"],opt=["+opt+"],status=["+status+"]");						
						break;	
				}
			}
		}); 
    	//===============
    	//��������ҳ��
    	//===============
  	    timer.schedule(task, 3*1000, 3*1000);       // timeTask 
  	    txthuosetrst=(TextView)findViewById(R.id.txthuosetrst);
  	    barhuomanager= (ProgressBar) findViewById(R.id.barhuomanager);
  	    huoAdapter=new Vmc_HuoAdapter();
  	    this.gvhuodao=(GridView) findViewById(R.id.gvhuodao); 
    	spinhuosetCab= (Spinner) findViewById(R.id.spinhuosetCab); 
    	spinhuotestCab= (Spinner) findViewById(R.id.spinhuotestCab); 
    	//��ʾ����Ϣ
    	showabinet();
    	this.spinhuosetCab.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//ֻ���й�ŵ�ʱ�򣬲�������ع��ڻ�����Ϣ
				if(cabinetID!=null)
				{
					barhuomanager.setVisibility(View.VISIBLE); 
					cabinetsetvar=Integer.parseInt(cabinetID[arg2]); 
					cabinetTypesetvar=cabinetType[arg2]; 
					queryhuodao();					
				}				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	//�޸Ļ���ӻ�����Ӧ��Ʒ
    	gvhuodao.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub cabinetID[0],
				String huo[]=huoAdapter.getHuoID();
				String huoID = huo[arg2];// ��¼������Ϣ               
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����ID="+cabinetsetvar+huoID+"status="+huoSet.get(huoID));
				Intent intent = new Intent();
		    	intent.setClass(HuodaoTest.this, HuodaoSet.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
                intent.putExtra("huoID", huoID);
                intent.putExtra("cabID", String.valueOf(cabinetsetvar));
                intent.putExtra("huoStatus", String.valueOf(huoSet.get(huoID)));
		    	startActivityForResult(intent, REQUEST_CODE);// ��AddInaccount	
			}// ΪGridView��������¼�
    		
    	});
    	//��ӹ�
    	btnhuosetadd = (Button) findViewById(R.id.btnhuosetadd);
    	btnhuosetadd.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	cabinetAdd();
		    }
		});
    	//ɾ����
    	btnhuosetdel = (Button) findViewById(R.id.btnhuosetdel);
    	btnhuosetdel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	cabinetDel();
		    }
		});
    	//���񲹻�
    	btnhuosetbu = (Button) findViewById(R.id.btnhuosetbu);
    	btnhuosetbu.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	cabinetbuhuo();
		    }
		});
    	btnhuosetexit = (Button) findViewById(R.id.btnhuosetexit);
    	btnhuosetexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	timer.cancel();
		    	finish();
		    }
		});    	
    	
    	//��̬���ÿؼ��߶�
    	//
    	DisplayMetrics  dm = new DisplayMetrics();  
        //ȡ�ô�������  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        //���ڵĿ��  
        int screenWidth = dm.widthPixels;          
        //���ڸ߶�  
        int screenHeight = dm.heightPixels;      
        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ļ"+screenWidth
				+"],["+screenHeight+"]");	
		
    	LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) gvhuodao.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
    	linearParams.height =  screenHeight-500;// ���ؼ��ĸ�ǿ�����75����
    	gvhuodao.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
  	   
    	//===============
    	//��������ҳ��
    	//===============
		
    	//spinhuoCab= (Spinner) findViewById(R.id.spinhuoCab); 
		txthuorst=(TextView)findViewById(R.id.txthuorst);
		txthuotestrst=(TextView)findViewById(R.id.txthuotestrst);
		btnhuochu = (Button) findViewById(R.id.btnhuochu);
		btnhuochuall = (Button) findViewById(R.id.btnhuochuall);
		btnhuocancel = (Button) findViewById(R.id.btnhuocancel);
		btnhuoexit = (Button) findViewById(R.id.btnhuoexit);
		edtcolumn = (EditText) findViewById(R.id.edtcolumn);
		txtlight = (TextView) findViewById(R.id.txtlight);		
		txtcold = (TextView) findViewById(R.id.txtcold);
		txthot = (TextView) findViewById(R.id.txthot);	
		switchlight = (Switch)findViewById(R.id.switchlight);
		switchlight.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					//���ӹ�
					if(cabinetTypevar==5)
					{
						EVprotocolAPI.EV_bentoLight(ToolClass.getBentcom_id(),cabinetvar,1);						
					}
					//��ͨ��
					else 
					{
						//rst=EVprotocolAPI.trade(cabinetvar,Integer.parseInt(edtcolumn.getText().toString()),typevar,
				    	//		ToolClass.MoneySend(price));	
					}
				}
				else 
				{
					//���ӹ�
					if(cabinetTypevar==5)
					{
						EVprotocolAPI.EV_bentoLight(ToolClass.getBentcom_id(),cabinetvar,0);						
					}
					//��ͨ��
					else 
					{
						//rst=EVprotocolAPI.trade(cabinetvar,Integer.parseInt(edtcolumn.getText().toString()),typevar,
				    	//		ToolClass.MoneySend(price));	
					}
					
				}
			}  
			
			
		}); 
		
		switcold = (Switch)findViewById(R.id.switcold);
		switcold.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
			}  
			
			
		});
		switchhot = (Switch)findViewById(R.id.switchhot);
		switchhot.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
			}  
			
			
		});
		this.spinhuotestCab.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//ֻ���й�ŵ�ʱ�򣬲�������ع��ڻ�����Ϣ
				if(cabinetID!=null)
				{
					cabinetvar=Integer.parseInt(cabinetID[arg2]); 
					cabinetTypevar=cabinetType[arg2]; 
				}				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		btnhuochu.setOnClickListener(new OnClickListener() {// Ϊ������ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {		    	  
		    	ToolClass.Log(ToolClass.INFO,"EV_JNI",
		    	"[APPsend>>]cabinet="+String.valueOf(cabinetvar)
		    	+" cabType="+String.valueOf(cabinetTypevar)
		    	+" column="+String.valueOf(Integer.parseInt(edtcolumn.getText().toString()))		    	
		    	);
		    	if (edtcolumn.getText().toString().isEmpty()!=true)	
		    	{
		    		float price=0;
		    		int typevar=0;
		    		if(price>0)
		    			typevar=0;
		    		else 
		    			typevar=2;
		    		int rst=0;
		    		//���ӹ�
					if(cabinetTypevar==5)
					{
						EVprotocolAPI.EV_bentoOpen(ToolClass.getBentcom_id(),cabinetvar,Integer.parseInt(edtcolumn.getText().toString()));						
					}
					//��ͨ��
					else 
					{
						rst=EVprotocolAPI.trade(cabinetvar,Integer.parseInt(edtcolumn.getText().toString()),typevar,
				    			ToolClass.MoneySend(price));	
					}
			    	   	
		    	}
		    }
		});
		btnhuochuall.setOnClickListener(new OnClickListener() {// Ϊ������ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {		    	  
		    	   		    		
	    		int rst=0;
	    		//���ӹ�
				if(cabinetTypevar==5)
				{
					huonno=1;
					ToolClass.Log(ToolClass.INFO,"EV_JNI",
					    	"[APPsend>>]cabinet="+String.valueOf(cabinetvar)
					    	+" cabType="+String.valueOf(cabinetTypevar)
					    	+" column="+huonno		    	
					    	);	 
					EVprotocolAPI.EV_bentoOpen(ToolClass.getBentcom_id(),cabinetvar,huonno);						
				}
				//��ͨ��
				else 
				{
//					rst=EVprotocolAPI.trade(cabinetvar,Integer.parseInt(edtcolumn.getText().toString()),typevar,
//			    			ToolClass.MoneySend(price));	
				}
		    }
		});
		btnhuocancel.setOnClickListener(new OnClickListener() {// Ϊ���ð�ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	edtcolumn.setText("");// ���ý���ı���Ϊ��		    	      
		    }
		});
		btnhuoexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	timer.cancel();
		    	finish();
		    }
		});
	}
	//===============
	//��������ҳ��
	//===============
	//���õ���ʱ��ʱ��
	TimerTask task = new TimerTask() { 
        @Override 
        public void run() { 
  
            runOnUiThread(new Runnable() {      // UI thread 
                @Override 
                public void run() { 
                    if(ishuoquery==1)
                    {
                    	queryhuodao();
                    }
//                    //��ѯ�Ѿ����
//                    else if(ishuoquery==0)
//                    {
//                    	timer.cancel(); //ȡ������ʱ�����Ͳ����ټ�������
//                    }
                } 
            }); 
        } 
    };
	private void queryhuodao()
	{
		txthuosetrst.setText("��ѯ����:"+(con++));		
		ishuoquery=1;
		//���ӹ�
		if(cabinetTypesetvar==5)
		{
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<huodao���ӹ����");
			EVprotocolAPI.EV_bentoCheck(ToolClass.getBentcom_id(),cabinetsetvar);
		}
		//��ͨ��
		else 
		{
			EVprotocolAPI.getColumn(cabinetsetvar);
		}
	}
	//����GoodsProSet������Ϣ
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==REQUEST_CODE)
		{
			if(resultCode==HuodaoTest.RESULT_OK)
			{
				barhuomanager.setVisibility(View.VISIBLE);  
				showhuodao();
			}			
		}
	}
	
	@Override
	protected void onDestroy() {
    	//�˳�ʱ������intent
        Intent intent=new Intent();
        setResult(MaintainActivity.RESULT_CANCELED,intent);
		super.onDestroy();		
	}
	
	//��ӹ��
	private void cabinetAdd()
	{
		final String[] values=null;
		View myview=null;    
		String [] mStringArray; 
		ArrayAdapter<String> mAdapter ;
		
		// TODO Auto-generated method stub
		LayoutInflater factory = LayoutInflater.from(HuodaoTest.this);
		myview=factory.inflate(R.layout.selectcabinet, null);
		final EditText dialogcab=(EditText) myview.findViewById(R.id.dialogcab);
		final Spinner dialogspincabtype=(Spinner) myview.findViewById(R.id.dialogspincabtype);
		mStringArray=getResources().getStringArray(R.array.cabinet_Type);
  	    //ʹ���Զ����ArrayAdapter
	  	mAdapter = new ArrayAdapter<String>(this,R.layout.viewspinner,mStringArray);
	  	dialogspincabtype.setAdapter(mAdapter);// ΪListView�б���������Դ
		
		Dialog dialog = new AlertDialog.Builder(HuodaoTest.this)
		.setTitle("����")
		.setPositiveButton("����", new DialogInterface.OnClickListener() 	
		{
				
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub	
				
				//Toast.makeText(HuodaoTest.this, "��ֵ="+dialogspincabtype.getSelectedItemId(), Toast.LENGTH_LONG).show();
				String no = dialogcab.getText().toString();
		    	int type = (int)dialogspincabtype.getSelectedItemId()+1;
		    	if ((no.isEmpty()!=true)&&(type!=0))
		    	{
		    		addabinet(no,type);
		    	}
		    	else
		        {
		            Toast.makeText(HuodaoTest.this, "����������ź����ͣ�", Toast.LENGTH_SHORT).show();
		        }
			}
		})
		.setNegativeButton("ȡ��",  new DialogInterface.OnClickListener()//ȡ����ť���������ü����¼�
    	{			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub				
			}
    	})
		.setView(myview)//���ｫ�Ի��򲼾��ļ����뵽�Ի�����
		.create();
		dialog.show();	
	}
	//�����ݿ���ӹ����ͱ��ֵ
	private void addabinet(String no,int type)
	{
		try 
		{
			// ����InaccountDAO����
			vmc_cabinetDAO cabinetDAO = new vmc_cabinetDAO(HuodaoTest.this);
            // ����Tb_inaccount����
        	Tb_vmc_cabinet tb_vmc_cabinet = new Tb_vmc_cabinet(no,type);
        	cabinetDAO.add(tb_vmc_cabinet);// ���������Ϣ
        	showabinet();//��ʾ����Ϣ
        	ToolClass.addOptLog(HuodaoTest.this,0,"��ӹ�:"+no);
        	// ������Ϣ��ʾ
            Toast.makeText(HuodaoTest.this, "������������ӳɹ���", Toast.LENGTH_SHORT).show();
            
		} catch (Exception e)
		{
			// TODO: handle exception
			Toast.makeText(HuodaoTest.this, "�������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
		}
	}
	//��ʾȫ������Ϣ
	private void showabinet()
	{
		ArrayAdapter<String> arrayAdapter = null;// ����ArrayAdapter���� 
		Vmc_CabinetAdapter vmc_cabAdapter=new Vmc_CabinetAdapter();
	    String[] strInfos = vmc_cabAdapter.showSpinInfo(HuodaoTest.this);	    
	    // ʹ���ַ��������ʼ��ArrayAdapter����
	    arrayAdapter = new ArrayAdapter<String>(this,R.layout.viewspinner, strInfos);
	    spinhuosetCab.setAdapter(arrayAdapter);// Ϊspin�б���������Դ
	    spinhuotestCab.setAdapter(arrayAdapter);// Ϊspin�б���������Դ
	    cabinetID=vmc_cabAdapter.getCabinetID();    
	    cabinetType=vmc_cabAdapter.getCabinetType();
	    //ֻ���й�ŵ�ʱ�򣬲�������ع��ڻ�����Ϣ
//		if(cabinetID!=null)
//		{
//		    cabinetsetvar=Integer.parseInt(cabinetID[0]); 
//		    cabinetTypesetvar=cabinetType[0]; 
//		}
	}
	//���뱾��ȫ��������Ϣ
	private void showhuodao()
	{		 
		huoAdapter.showProInfo(HuodaoTest.this, "", huoSet,String.valueOf(cabinetsetvar));
		
		HuoPictureAdapter adapter = new HuoPictureAdapter(String.valueOf(cabinetsetvar),huoAdapter.getHuoID(),huoAdapter.getHuoproID(),huoAdapter.getHuoRemain(),huoAdapter.getHuolasttime(), huoAdapter.getProImage(),HuodaoTest.this);// ����pictureAdapter����
		gvhuodao.setAdapter(adapter);// ΪGridView��������Դ		 
		barhuomanager.setVisibility(View.GONE);  
	}
	//ɾ������Լ����ڻ���ȫ����Ϣ
	private void cabinetDel()
	{
		//��������Ի���
    	Dialog alert=new AlertDialog.Builder(HuodaoTest.this)
    		.setTitle("�Ի���")//����
    		.setMessage("��ȷ��Ҫɾ���ù���")//��ʾ�Ի����е�����
    		.setIcon(R.drawable.ic_launcher)//����logo
    		.setPositiveButton("ɾ��", new DialogInterface.OnClickListener()//�˳���ť���������ü����¼�
    			{				
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) 
	    				{
	    					// TODO Auto-generated method stub	
	    					// ����InaccountDAO����
	    					vmc_columnDAO columnDAO = new vmc_columnDAO(HuodaoTest.this);
				            columnDAO.deteleCab(String.valueOf(cabinetsetvar));// ɾ���ù������Ϣ
				            
				            vmc_cabinetDAO cabinetDAO = new vmc_cabinetDAO(HuodaoTest.this);
				            cabinetDAO.detele(String.valueOf(cabinetsetvar));// ɾ���ù���Ϣ
				            ToolClass.addOptLog(HuodaoTest.this,2,"ɾ����:"+cabinetsetvar);
	    					// ������Ϣ��ʾ
				            Toast.makeText(HuodaoTest.this, "��ɾ���ɹ���", Toast.LENGTH_SHORT).show();						            
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
	
	//�����������
	private void cabinetbuhuo()
	{
		//��������Ի���
    	Dialog alert=new AlertDialog.Builder(HuodaoTest.this)
    		.setTitle("�Ի���")//����
    		.setMessage("��ȷ��Ҫ�������������")//��ʾ�Ի����е�����
    		.setIcon(R.drawable.ic_launcher)//����logo
    		.setPositiveButton("����", new DialogInterface.OnClickListener()//�˳���ť���������ü����¼�
    			{				
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) 
	    				{
	    					// TODO Auto-generated method stub	
	    					barhuomanager.setVisibility(View.VISIBLE);
	    					// ����InaccountDAO����
	    					vmc_columnDAO columnDAO = new vmc_columnDAO(HuodaoTest.this);
				            columnDAO.buhuoCab(String.valueOf(cabinetsetvar));
				            showhuodao();
	    					// ������Ϣ��ʾ
				            Toast.makeText(HuodaoTest.this, "�����ɹ���", Toast.LENGTH_SHORT).show();	
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
	
	//===============
	//��������ҳ��
	//===============
	private void sethuorst(int status)
	{
		switch(status)
		{			
			case 0:
				txthuotestrst.setText("����ʧ��");
				break;
			case 1:
				txthuotestrst.setText("�����ɹ�");
				break;		
		}
	}
}
