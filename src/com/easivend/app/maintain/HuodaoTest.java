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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.easivend.dao.vmc_cabinetDAO;
import com.easivend.dao.vmc_classDAO;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.model.Tb_vmc_cabinet;
import com.easivend.model.Tb_vmc_class;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SlidingDrawer;
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
	private int[] layres=new int[]{R.id.tab_huodaomanager,R.id.tab_huodaotest};//��Ƕ�����ļ���id
	private Button btnhuosetadd=null,btnhuosetdel=null,btnhuosetexit=null;
	private Spinner spinhuosetCab=null;
	private String[] cabinetID=null;
	Map<String, Integer> huoSet= new TreeMap<String,Integer>();
	// ��������б�
	Vmc_HuoAdapter huoAdapter=null;
	GridView gvhuodao=null;
	private final static int REQUEST_CODE=1;//���������ʶ
	
	private int device=0;//�������		
	private int status=0;//�������
	private int hdid=0;//����id
	private int hdtype=0;//��������
	private float cost=0;//��Ǯ
	private float totalvalue=0;//ʣ����
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
			public void jniCallback(Map<String, Integer> allSet) {
				// TODO Auto-generated method stub
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<huodao�������");
				Map<String, Integer> Set= allSet;
				int jnirst=Set.get("EV_TYPE");
				switch (jnirst)
				{
					case EVprotocolAPI.EV_TRADE_RPT://�������߳���Ϣ
						device=allSet.get("device");//�������
						status=allSet.get("status");//�������
						hdid=allSet.get("hdid");//����id
						hdtype=allSet.get("type");//��������
						cost=ToolClass.MoneyRec(allSet.get("cost"));//��Ǯ
						totalvalue=ToolClass.MoneyRec(allSet.get("totalvalue"));//ʣ����
						huodao=allSet.get("huodao");//ʣ��������
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�������"+"device=["+device+"],status=["+status+"],hdid=["+hdid+"],type=["+hdtype+"],cost=["
								+cost+"],totalvalue=["+totalvalue+"],huodao=["+huodao+"]");	
						
						txthuorst.setText("device=["+device+"],status=["+status+"],hdid=["+hdid+"],type=["+hdtype+"],cost=["
								+cost+"],totalvalue=["+totalvalue+"],huodao=["+huodao+"]");
						break;
					case EVprotocolAPI.EV_COLUMN_RPT://�������߳���Ϣ
						huoSet.clear();
						//�������
				        Set<Entry<String, Integer>> allmap=Set.entrySet();  //ʵ����
				        Iterator<Entry<String, Integer>> iter=allmap.iterator();
				        while(iter.hasNext())
				        {
				            Entry<String, Integer> me=iter.next();
				            if(me.getKey().equals("EV_TYPE")!=true)
				            	huoSet.put(me.getKey(), me.getValue());
				        } 
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����״̬:"+huoSet.toString());	
						showhuodao();						
						break;
				}
			}
		}); 
    	//===============
    	//��������ҳ��
    	//===============
  	    
  	    huoAdapter=new Vmc_HuoAdapter();
  	    this.gvhuodao=(GridView) findViewById(R.id.gvhuodao); 
    	spinhuosetCab= (Spinner) findViewById(R.id.spinhuosetCab); 
    	//��ʾ����Ϣ
    	showabinet();
    	this.spinhuosetCab.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				EVprotocolAPI.getColumn(Integer.parseInt(cabinetID[arg2]));
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
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����ID="+cabinetID[0]+huoID+"status="+huoSet.get(huoID));
				Intent intent = new Intent();
		    	intent.setClass(HuodaoTest.this, HuodaoSet.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
                intent.putExtra("huoID", huoID);
                intent.putExtra("cabID", cabinetID[0]);
                intent.putExtra("huoStatus", String.valueOf(huoSet.get(huoID)));
		    	startActivityForResult(intent, REQUEST_CODE);// ��AddInaccount	
			}// ΪGridView��������¼�
    		
    	});
    	btnhuosetadd = (Button) findViewById(R.id.btnhuosetadd);
    	btnhuosetadd.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	cabinetAdd();
		    }
		});
    	btnhuosetdel = (Button) findViewById(R.id.btnhuosetdel);
    	btnhuosetexit = (Button) findViewById(R.id.btnhuosetexit);
    	btnhuosetexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
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
		    	ToolClass.Log(ToolClass.INFO,"EV_JNI",
		    	"[APPsend>>]cabinet="+String.valueOf(cabinetvar)
		    	+" column="+String.valueOf(Integer.parseInt(edtcolumn.getText().toString()))
		    	+" type="+String.valueOf(typevar)
		    	+" price="+edtprice.getText().toString()
		    	);
		    	int rst=EVprotocolAPI.trade(cabinetvar,Integer.parseInt(edtcolumn.getText().toString()),typevar,
		    			ToolClass.MoneySend(Float.parseFloat(edtprice.getText().toString())));	   	
		    	
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
	//===============
	//��������ҳ��
	//===============
	//����GoodsProSet������Ϣ
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==REQUEST_CODE)
		{
			if(resultCode==HuodaoTest.RESULT_OK)
			{
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
		// TODO Auto-generated method stub
		LayoutInflater factory = LayoutInflater.from(HuodaoTest.this);
		myview=factory.inflate(R.layout.selectcabinet, null);
		final EditText dialogcab=(EditText) myview.findViewById(R.id.dialogcab);
		final Spinner dialogspincabtype=(Spinner) myview.findViewById(R.id.dialogspincabtype);
		
		
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
        	Tb_vmc_cabinet tb_vmc_cabinet = new Tb_vmc_cabinet(no, type);
        	cabinetDAO.add(tb_vmc_cabinet);// ���������Ϣ
        	showabinet();//��ʾ����Ϣ
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
	    arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strInfos);
	    spinhuosetCab.setAdapter(arrayAdapter);// ΪListView�б���������Դ
	    cabinetID=vmc_cabAdapter.getCabinetID();    	
	}
	//���뱾��ȫ��������Ϣ
	private void showhuodao()
	{
		huoAdapter.showProInfo(HuodaoTest.this, "", huoSet,cabinetID[0]);
		HuoPictureAdapter adapter = new HuoPictureAdapter(cabinetID[0],huoAdapter.getHuoID(),huoAdapter.getHuoproID(),huoAdapter.getHuoRemain(),huoAdapter.getHuolasttime(), huoAdapter.getProImage(),HuodaoTest.this);// ����pictureAdapter����
		gvhuodao.setAdapter(adapter);// ΪGridView��������Դ
	}
		
}
