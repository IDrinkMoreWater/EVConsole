package com.easivend.app.business;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.easivend.app.maintain.MaintainActivity;
import com.easivend.common.ToolClass;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.fragment.BusgoodsFragment;
import com.easivend.fragment.BusgoodsFragment.BusgoodsFragInteraction;
import com.easivend.fragment.BusgoodsclassFragment;
import com.easivend.fragment.BusgoodsclassFragment.BusgoodsclassFragInteraction;
import com.easivend.fragment.BusgoodsselectFragment;
import com.easivend.fragment.BusgoodsselectFragment.BusgoodsselectFragInteraction;
import com.easivend.fragment.BusinesslandFragment;
import com.easivend.fragment.BusinessportFragment;
import com.easivend.fragment.BusinessportFragment.BusportFragInteraction;
import com.easivend.fragment.MoviewlandFragment;
import com.easivend.fragment.MoviewlandFragment.MovieFragInteraction;
import com.easivend.http.EVServerhttp;
import com.example.evconsole.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class BusPort extends Activity implements 
//businessҳ��ӿ�
MovieFragInteraction,BusportFragInteraction,
//busgoodsclassҳ��ӿ�
BusgoodsclassFragInteraction,
//busgoodsҳ��ӿ�
BusgoodsFragInteraction,
//Busgoodsselectҳ��ӿ�
BusgoodsselectFragInteraction
{
	private BusinessportFragment businessportFragment;
	private BusgoodsclassFragment busgoodsclassFragment;
	private BusgoodsFragment busgoodsFragment;
	private BusgoodsselectFragment busgoodsselectFragment;
	//����ҳ��
    Intent intent=null;
    final static int REQUEST_CODE=1; 
    public static final int BUSPORT=1;//��ҳ��
    public static final int BUSGOODSCLASS=2;//��Ʒ���ҳ��
	public static final int BUSGOODS=3;//��Ʒ����ҳ��
	public static final int BUSGOODSSELECT=4;//��Ʒ��ϸҳ��
	Timer timer = new Timer(true);
    private final int SPLASH_DISPLAY_LENGHT = 5*60; //  5*60�ӳ�5����	
    private int recLen = SPLASH_DISPLAY_LENGHT; 
    private boolean isbus=true;//true��ʾ�ڹ��ҳ�棬false������ҳ��
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ��title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ȫ��
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.busport);		
		//���ú������������Ĳ��ֲ���
		this.setRequestedOrientation(ToolClass.getOrientation());
		//ע�ᴮ�ڼ�����
		EVprotocolAPI.setCallBack(new jniInterfaceImp());
		timer.schedule(new TimerTask() { 
	        @Override 
	        public void run() { 
	        	  if(isbus==false)
	        	  {
		        	  //ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<recLen="+recLen,"log.txt");
		        	  recLen--; 		    	      
		        	  if(recLen == 0)
		              { 
		                  ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<recclose=BusinessportFragment","log.txt");
		                  viewSwitch(BUSPORT,null);		                  
		              }	
	        	  }
	        } 
	    }, 1000, 1000);       // timeTask 
		//��ʼ��Ĭ��fragment
		initView();
	}
	
	//��ʼ��Ĭ��fragment
	public void initView() {        
        // ����Ĭ�ϵ�Fragment
        setDefaultFragment();
    }
	
	// ����Ĭ�ϵ�Fragment
	@SuppressLint("NewApi")
    private void setDefaultFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        businessportFragment = new BusinessportFragment();
        transaction.replace(R.id.id_content, businessportFragment);
        transaction.commit();
    }
	
	//=======================
	//ʵ��Businessҳ����ؽӿ�
	//=======================
	@Override
	public void switchBusiness() {
		// TODO Auto-generated method stub		
	}

	//��������ʵ��Business�ӿ�,�رձ�activity����
	@Override
	public void finishBusiness() {
		// TODO Auto-generated method stub
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<busland=finishBusiness","log.txt");
		finish();
	}
	//��������ʵ��Business�ӿ�,ת����Ʒ����ҳ��
	//buslevel��ת����ҳ��
	@Override
	public void gotoBusiness(int buslevel, Map<String, String> str)
	{
		viewSwitch(buslevel, str);
	}
	
	//=======================
	//ʵ��BusgoodsClassҳ����ؽӿ�
	//=======================
	//��������ʵ��Busgoodsclass�ӿ�,ת����Ʒ����ҳ��
	@Override
	public void BusgoodsclassSwitch(Map<String, String> str) {
		// TODO Auto-generated method stub
		viewSwitch(BUSGOODS, str);
	}

	//��������ʵ��Busgoodsclass�ӿ�,ת����ҳ��
	@Override
	public void BusgoodsclassFinish() {
		// TODO Auto-generated method stub
		viewSwitch(BUSPORT, null);
	}
	
	
	//=======================
	//ʵ��Busgoodsҳ����ؽӿ�
	//=======================
	//��������ʵ��Busgoods�ӿ�,ת����Ʒ��ϸҳ��
	@Override
	public void BusgoodsSwitch(Map<String, String> str) {
		// TODO Auto-generated method stub
		viewSwitch(BUSGOODSSELECT, str);
	}
	//��������ʵ��Busgoodsclass�ӿ�,ת����ҳ��
	@Override
	public void BusgoodsFinish() {
		// TODO Auto-generated method stub
		viewSwitch(BUSPORT, null);
	}
	
	//=======================
	//ʵ��Busgoodsselectҳ����ؽӿ�
	//=======================
	//��������ʵ��Busgoodsselect�ӿ�,ת����Ʒ��ϸҳ��
	@Override
	public void BusgoodsselectSwitch(Map<String, String> str) {
		// TODO Auto-generated method stub
		
	}
	//��������ʵ��Busgoodsselect�ӿ�,ת����ҳ��
	@Override
	public void BusgoodsselectFinish() {
		// TODO Auto-generated method stub
		viewSwitch(BUSPORT, null);
	}
	
	
	//ȫ�������л�view�ĺ���
	public void viewSwitch(int buslevel, Map<String, String> str)
	{
		recLen=SPLASH_DISPLAY_LENGHT;
		Bundle data = new Bundle();
		FragmentManager fm = getFragmentManager();
        // ����Fragment����
        FragmentTransaction transaction = fm.beginTransaction();
		// TODO Auto-generated method stub
		switch(buslevel)
		{
			case BUSPORT://��ҳ��
				isbus=true;
				if (businessportFragment == null) {
					businessportFragment = new BusinessportFragment();
	            }
	            // ʹ�õ�ǰFragment�Ĳ������id_content�Ŀؼ�
	            transaction.replace(R.id.id_content, businessportFragment);
				break;
			case BUSGOODSCLASS://��Ʒ���
				isbus=false;
				if (busgoodsclassFragment == null) {
					busgoodsclassFragment = new BusgoodsclassFragment();
	            }
	            // ʹ�õ�ǰFragment�Ĳ������id_content�Ŀؼ�
	            transaction.replace(R.id.id_content, busgoodsclassFragment);
				break;
			case BUSGOODS:
				isbus=false;
//				intent = new Intent(BusPort.this, Busgoods.class);// ʹ��Accountflag���ڳ�ʼ��Intent
//            	intent.putExtra("proclassID", "");
//            	startActivityForResult(intent,REQUEST_CODE);// ��Accountflag
				if (busgoodsFragment == null) {
					busgoodsFragment = new BusgoodsFragment();
	            }
				//�����塢����������ݸ�friendfragment
				//��������
		        data.clear();
		        data.putString("proclassID", str.get("proclassID"));
		        busgoodsFragment.setArguments(data);
	            // ʹ�õ�ǰFragment�Ĳ������id_content�Ŀؼ�
	            transaction.replace(R.id.id_content, busgoodsFragment);
				break;
			case BUSGOODSSELECT:
				isbus=false;
//				intent = new Intent(BusPort.this, BusgoodsSelect.class);// ʹ��Accountflag���ڳ�ʼ��Intent
//	        	intent.putExtra("proID", str.get("proID"));
//	        	intent.putExtra("productID", str.get("productID"));
//	        	intent.putExtra("proImage", str.get("proImage"));
//	        	intent.putExtra("prosales", str.get("prosales"));
//	        	intent.putExtra("procount", str.get("procount"));
//	        	intent.putExtra("proType", str.get("proType"));//1����ͨ����ƷID����,2����ͨ����������
//	        	intent.putExtra("cabID", str.get("cabID"));//�������,proType=1ʱ��Ч
//	        	intent.putExtra("huoID", str.get("huoID"));//����������,proType=1ʱ��Ч


//	        	OrderDetail.setProID(proID);
//            	OrderDetail.setProductID(productID);
//            	OrderDetail.setProType("2");
//            	OrderDetail.setCabID(cabID);
//            	OrderDetail.setColumnID(huoID);
//            	OrderDetail.setShouldPay(Float.parseFloat(prosales));
//            	OrderDetail.setShouldNo(1);
	        	
//	        	startActivityForResult(intent,REQUEST_CODE);// ��Accountflag
				if (busgoodsselectFragment == null) {
					busgoodsselectFragment = new BusgoodsselectFragment();
	            }
				//�����塢����������ݸ�friendfragment
				//��������
				data.clear();
		        data.putString("proID", str.get("proID"));
		        data.putString("productID", str.get("productID"));
		        data.putString("proImage", str.get("proImage"));
		        data.putString("prosales", str.get("prosales"));
		        data.putString("procount", str.get("procount"));
		        data.putString("proType", str.get("proType"));//1����ͨ����ƷID����,2����ͨ����������
		        data.putString("cabID", str.get("cabID"));//�������,proType=1ʱ��Ч
		        data.putString("huoID", str.get("huoID"));//����������,proType=1ʱ��Ч
	        	busgoodsselectFragment.setArguments(data);
	            // ʹ�õ�ǰFragment�Ĳ������id_content�Ŀؼ�
	            transaction.replace(R.id.id_content, busgoodsselectFragment);
				break;
		}
		// transaction.addToBackStack();
        // �����ύ
        transaction.commit();
	}
		
	
	//����һ��ר�Ŵ������ӿڵ�����
	private class jniInterfaceImp implements JNIInterface
	{
		@Override
		public void jniCallback(Map<String, Object> allSet) {
			// TODO Auto-generated method stub
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<business������","log.txt");	
			Map<String, Object> Set= allSet;
			int jnirst=(Integer) Set.get("EV_TYPE");
			//txtcom.setText(String.valueOf(jnirst));
			switch (jnirst)
			{
				//�ֽ��豸״̬��ѯ
				case EVprotocolAPI.EV_MDB_HEART://������ѯ
					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�ֽ��豸״̬:","log.txt");	
					int bill_err=ToolClass.getvmcStatus(Set,1);
					int coin_err=ToolClass.getvmcStatus(Set,2);
					//�ϱ���������
					Intent intent=new Intent();
    				intent.putExtra("EVWhat", EVServerhttp.SETDEVSTATUCHILD);
    				intent.putExtra("bill_err", bill_err);
    				intent.putExtra("coin_err", coin_err);
    				intent.setAction("android.intent.action.vmserversend");//action���������ͬ
    				sendBroadcast(intent); 
					break; 	
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{		
    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<businessJNI","log.txt");
		//ע�ᴮ�ڼ�����
		EVprotocolAPI.setCallBack(new jniInterfaceImp());		
	}
	@Override
	protected void onDestroy() {
		//�˳�ʱ������intent
        Intent intent=new Intent();
        setResult(MaintainActivity.RESULT_CANCELED,intent);
		super.onDestroy();		
	}

	

	

	

	

}
