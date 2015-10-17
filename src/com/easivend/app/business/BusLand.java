package com.easivend.app.business;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.easivend.app.maintain.MaintainActivity;
import com.easivend.common.ToolClass;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.fragment.BusinesslandFragment;
import com.easivend.fragment.BusinesslandFragment.BusFragInteraction;
import com.easivend.fragment.MoviewlandFragment;
import com.easivend.fragment.MoviewlandFragment.MovieFragInteraction;
import com.easivend.http.EVServerhttp;
import com.example.evconsole.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class BusLand extends Activity implements MovieFragInteraction,BusFragInteraction{	
    private MoviewlandFragment moviewlandFragment;
    private BusinesslandFragment businesslandFragment;
    Timer timer = new Timer(true);
    private final int SPLASH_DISPLAY_LENGHT = 5*60; //  5*60�ӳ�5����	
    private int recLen = SPLASH_DISPLAY_LENGHT; 
    private boolean isbus=true;//true��ʾ�ڹ��ҳ�棬false������ҳ��
    //����ҳ��
    Intent intent=null;
    final static int REQUEST_CODE=1; 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ��title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ȫ��
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.busland);		
		//���ú������������Ĳ��ֲ���
		this.setRequestedOrientation(ToolClass.getOrientation());
		//��ʼ��Ĭ��fragment
		initView();
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
		                  ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<recclose=movielandFragment","log.txt");
			    	      switchMovie();
		              }	
	        	  }
	        } 
	    }, 1000, 1000);       // timeTask  
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

        moviewlandFragment = new MoviewlandFragment();
        transaction.replace(R.id.id_content, moviewlandFragment);
        transaction.commit();
    }

	//��������ʵ��Movie�ӿ�,��ת��Business��
	@Override
	public void switchBusiness() {
		// TODO Auto-generated method stub
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<busland=switchBusiness","log.txt");
	    FragmentManager fm = getFragmentManager();
	    // ����Fragment����
	    FragmentTransaction transaction = fm.beginTransaction();
	    if (businesslandFragment == null) 
	    {
	       	businesslandFragment = new BusinesslandFragment();
	    }
	    //�����塢����������ݸ�businesslandFragment
	    //....�����̲��ô�������
	    
	    transaction.replace(R.id.id_content, businesslandFragment);
	    // transaction.addToBackStack();
	    // �����ύ
	    transaction.commit();
	    isbus=false;
	    recLen=SPLASH_DISPLAY_LENGHT;
	}
	
	//��������ʵ��Business�ӿ�,�رձ�activity����
	@Override
	public void finishBusiness() {
		// TODO Auto-generated method stub
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<busland=finishBusiness","log.txt");
		finish();
	}
	//��������ʵ��Business�ӿ�,��ͣ����ʱ��ʱ������ת����Ʒ����ҳ��
	//buslevel����1����Ʒ���2����Ʒ����ҳ�棬3����Ʒ��ϸҳ��
	@Override
	public void gotoBusiness(int buslevel,Map<String, String>str) {
		// TODO Auto-generated method stub
		isbus=true;
	    recLen=SPLASH_DISPLAY_LENGHT;
		//switchMovie();
		switch(buslevel)
		{
			case 1:
				intent = new Intent(BusLand.this, BusgoodsClass.class);// ʹ��Accountflag���ڳ�ʼ��Intent
		    	startActivityForResult(intent,REQUEST_CODE);// ��Accountflag
				break;
			case 2:
				intent = new Intent(BusLand.this, Busgoods.class);// ʹ��Accountflag���ڳ�ʼ��Intent
            	intent.putExtra("proclassID", "");
            	startActivityForResult(intent,REQUEST_CODE);// ��Accountflag
				break;
			case 3:
				intent = new Intent(BusLand.this, BusgoodsSelect.class);// ʹ��Accountflag���ڳ�ʼ��Intent
	        	intent.putExtra("proID", str.get("proID"));
	        	intent.putExtra("productID", str.get("productID"));
	        	intent.putExtra("proImage", str.get("proImage"));
	        	intent.putExtra("prosales", str.get("prosales"));
	        	intent.putExtra("procount", str.get("procount"));
	        	intent.putExtra("proType", str.get("proType"));//1����ͨ����ƷID����,2����ͨ����������
	        	intent.putExtra("cabID", str.get("cabID"));//�������,proType=1ʱ��Ч
	        	intent.putExtra("huoID", str.get("huoID"));//����������,proType=1ʱ��Ч


//	        	OrderDetail.setProID(proID);
//            	OrderDetail.setProductID(productID);
//            	OrderDetail.setProType("2");
//            	OrderDetail.setCabID(cabID);
//            	OrderDetail.setColumnID(huoID);
//            	OrderDetail.setShouldPay(Float.parseFloat(prosales));
//            	OrderDetail.setShouldNo(1);
	        	
	        	startActivityForResult(intent,REQUEST_CODE);// ��Accountflag
				break;
		}
	}
	
	//���ص����ҳ��
	public void switchMovie() {
		// TODO Auto-generated method stub
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<busland=switchMovie","log.txt");
	    FragmentManager fm = getFragmentManager();
	    // ����Fragment����
	    FragmentTransaction transaction = fm.beginTransaction();
	    if (moviewlandFragment == null) 
	    {
        	moviewlandFragment = new MoviewlandFragment();
        }
	    //�����塢����������ݸ�businesslandFragment
	    //....�����̲��ô�������
	    
        // ʹ�õ�ǰFragment�Ĳ������id_content�Ŀؼ�
        transaction.replace(R.id.id_content, moviewlandFragment);	    
	    // transaction.addToBackStack();
	    // �����ύ
	    transaction.commit();
	    recLen=SPLASH_DISPLAY_LENGHT;
	    isbus=true;
	}
	
	//��������ʵ��Business�ӿ�,��ͣ��ʱ��
	@Override
	public void stoptimer() {
		// TODO Auto-generated method stub
		isbus=true;
	    recLen=SPLASH_DISPLAY_LENGHT;
	}
	//��������ʵ��Business�ӿ�,���´򿪶�ʱ��
	@Override
	public void restarttimer() {
		// TODO Auto-generated method stub
		isbus=false;
	    recLen=SPLASH_DISPLAY_LENGHT;
	}
//	// �л�Fragment
//	@SuppressLint("NewApi")
//    public void setonClick(View v) {
//        FragmentManager fm = getFragmentManager();
//        // ����Fragment����
//        FragmentTransaction transaction = fm.beginTransaction();
//        switch (v.getId()) {
//        case R.id.btnweixin:
//            if (moviewlandFragment == null) {
//            	moviewlandFragment = new MoviewlandFragment();
//            }
//            // ʹ�õ�ǰFragment�Ĳ������id_content�Ŀؼ�
//            transaction.replace(R.id.id_content, moviewlandFragment);
//            break;
//        case R.id.btnfriend:
//            if (businesslandFragment == null) {
//            	businesslandFragment = new BusinesslandFragment();
//            }
//            transaction.replace(R.id.id_content, businesslandFragment);
//            break;
//        }
//        // transaction.addToBackStack();
//        // �����ύ
//        transaction.commit();
//    }
			
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
		//�ָ�����ʱ��ʱ��
		//isbus=true;
	    //recLen=SPLASH_DISPLAY_LENGHT;
		switchMovie();
	}
	@Override
	protected void onDestroy() {
		timer.cancel(); 
    	//�˳�ʱ������intent
        Intent intent=new Intent();
        setResult(MaintainActivity.RESULT_CANCELED,intent);
		super.onDestroy();		
	}

	

	

	

	
}
