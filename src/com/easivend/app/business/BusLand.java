package com.easivend.app.business;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.easivend.app.business.BusPort.EVServerReceiver;
import com.easivend.app.maintain.MaintainActivity;
import com.easivend.common.OrderDetail;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_productDAO;
import com.easivend.fragment.BusinesslandFragment;
import com.easivend.fragment.BusinesslandFragment.BusFragInteraction;
import com.easivend.fragment.MoviewlandFragment;
import com.easivend.fragment.MoviewlandFragment.MovieFragInteraction;
import com.easivend.http.EVServerhttp;
import com.easivend.model.Tb_vmc_product;
import com.easivend.view.PassWord;
import com.example.evconsole.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BusLand extends Activity implements MovieFragInteraction,BusFragInteraction{	
    private MoviewlandFragment moviewlandFragment;
    private BusinesslandFragment businesslandFragment;
    ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
    private final int SPLASH_DISPLAY_LENGHT = 5*60; //  5*60�ӳ�5����	
    private int recLen = SPLASH_DISPLAY_LENGHT; 
    private boolean isbus=true;//true��ʾ�ڹ��ҳ�棬false������ҳ��
    //����ҳ��
    Intent intent=null;
    final static int REQUEST_CODE=1; 
    final static int PWD_CODE=2;
    //Server�������
  	LocalBroadcastManager localBroadreceiver;
  	EVServerReceiver receiver;
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
		//=============
		//Server�������
		//=============
		//4.ע�������
		localBroadreceiver = LocalBroadcastManager.getInstance(this);
		receiver=new EVServerReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("android.intent.action.vmserverrec");
		localBroadreceiver.registerReceiver(receiver,filter);
		//��ʼ��Ĭ��fragment
		initView();		
		timer.scheduleWithFixedDelay(new Runnable() { 
	        @Override 
	        public void run() { 
	        	//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<portthread="+Thread.currentThread().getId(),"log.txt"); 
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
	    }, 1, 1, TimeUnit.SECONDS);       // timeTask  
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
	    //�򿪶�ʱ��
	    isbus=false;
	    recLen=SPLASH_DISPLAY_LENGHT;
	}
	
	//��������ʵ��Business�ӿ�,�������
	@Override
	public void finishBusiness() {
		// TODO Auto-generated method stub
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<busland=�˳�","log.txt");
//    	Intent intent = new Intent();
//    	intent.setClass(BusLand.this, PassWord.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
//        startActivityForResult(intent, PWD_CODE);
		//��ʱ0.5s
	    new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {      
            	finish(); 
            }

		}, 500);		
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
			case 4:
				intent = new Intent(BusLand.this, BusHuo.class);// ʹ��Accountflag���ڳ�ʼ��Intent
            	startActivityForResult(intent,REQUEST_CODE);// ��Accountflag
				break;	
		}
	}
	//��������ʵ��Business�ӿ�,����ȡ����
	@Override
	public void quhuoBusiness(String PICKUP_CODE)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<landȡ����="+PICKUP_CODE,"log.txt");
		Intent intent2=new Intent(); 
		intent2.putExtra("EVWhat", EVServerhttp.SETPICKUPCHILD);
		intent2.putExtra("PICKUP_CODE", PICKUP_CODE);
		intent2.setAction("android.intent.action.vmserversend");//action���������ͬ
		localBroadreceiver.sendBroadcast(intent2);
	}
	
	//=============
	//Server�������
	//=============	
	//2.����EVServerReceiver�Ľ������㲥���������շ�����ͬ��������
	public class EVServerReceiver extends BroadcastReceiver 
	{

		@Override
		public void onReceive(Context context, Intent intent) 
		{
			// TODO Auto-generated method stub
			Bundle bundle=intent.getExtras();
			int EVWhat=bundle.getInt("EVWhat");
			switch(EVWhat)
			{
			case EVServerhttp.SETPICKUPMAIN:
				String PRODUCT_NO=bundle.getString("PRODUCT_NO");
				String out_trade_no=bundle.getString("out_trade_no");
				ToolClass.Log(ToolClass.INFO,"EV_JNI","BusPort=ȡ����ɹ�PRODUCT_NO="+PRODUCT_NO+"out_trade_no="+out_trade_no,"log.txt");					
				// ����InaccountDAO�������ڴ����ݿ�����ȡ���ݵ�Tb_vmc_product����
		 	    vmc_productDAO productdao = new vmc_productDAO(context);
		 	    Tb_vmc_product tb_vmc_product=productdao.find(PRODUCT_NO);
		 	    //���浽���������
		 	    //��������Ϣ
		 	    OrderDetail.setProID(tb_vmc_product.getProductID()+"-"+tb_vmc_product.getProductName());		 	    
		 	    OrderDetail.setProType("1");
		 	    OrderDetail.setOrdereID(out_trade_no);
		 	    //����֧���� 
				OrderDetail.setPayType(-1);
		 	    OrderDetail.setShouldPay(tb_vmc_product.getSalesPrice());
		 	    OrderDetail.setShouldNo(1);
		 	    OrderDetail.setCabID("");
		 		OrderDetail.setColumnID("");
		 	    //������ϸ��Ϣ��   
		 	    OrderDetail.setProductID(PRODUCT_NO);
		 	    gotoBusiness(4,null);
				break;
			case EVServerhttp.SETERRFAILPICKUPMAIN:
				ToolClass.Log(ToolClass.INFO,"EV_JNI","BusPort=ȡ����ʧ��","log.txt");
				// ������Ϣ��ʾ
				Toast myToast=Toast.makeText(context, "��Ǹ��ȡ������Ч,����ϵ����Ա��", Toast.LENGTH_LONG);
				myToast.setGravity(Gravity.CENTER, 0, 0);
				LinearLayout toastView = (LinearLayout) myToast.getView();
				ImageView imageCodeProject = new ImageView(getApplicationContext());
				imageCodeProject.setImageResource(R.drawable.search);
				toastView.addView(imageCodeProject, 0);
				myToast.show();
	    		break;	
			case EVServerhttp.SETADVRESETMAIN:
				ToolClass.Log(ToolClass.INFO,"EV_JNI","BusPort=ˢ�¹��","log.txt");
				//listterner.BusportAds();
				break;
			}			
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
		
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{		
    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<businessJNI,requestCode="+requestCode+"resultCode="+resultCode,"log.txt");		
		if((requestCode==REQUEST_CODE)&&(resultCode==0x03))
		{
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ȡ����ҳ��","log.txt");
			OrderDetail.addLog(BusLand.this);	
			switchMovie();
		}		
		else 
		{
			switchMovie();	
		}
	}
	@Override
	protected void onDestroy() {
		timer.shutdown();
		//=============
		//Server�������
		//=============
		//5.���ע�������
		localBroadreceiver.unregisterReceiver(receiver);
    	//�˳�ʱ������intent
        Intent intent=new Intent();
        setResult(MaintainActivity.RESULT_CANCELED,intent);
		super.onDestroy();		
	}

	

	

	

	
}
