package com.easivend.app.business;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.easivend.common.OrderDetail;
import com.easivend.common.SerializableMap;
import com.easivend.common.ToolClass;
import com.easivend.evprotocol.EVprotocol;
import com.easivend.view.COMService;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BusZhiAmount  extends Activity
{
	private final int SPLASH_DISPLAY_LENGHT = 500; // �ӳ�1.5��
	//���ȶԻ���
	ProgressDialog dialog= null;
	public static BusZhiAmount BusZhiAmountAct=null;
	private final static int REQUEST_CODE=1;//���������ʶ
	TextView txtbuszhiamountcount=null,txtbuszhiamountAmount=null,txtbuszhiamountbillAmount=null,txtbuszhiamounttime=null,
			txtbuszhiamounttsxx=null;
	ImageButton imgbtnbuszhiamountqxzf=null,imgbtnbuszhiamountqtzf=null;
	ImageView imgbtnbusgoodsback=null;
	float amount=0;//��Ʒ��Ҫ֧�����
	float billmoney=0,coinmoney=0,money=0;//Ͷ�ҽ��
	float RealNote=0,RealCoin=0,RealAmount=0;//�˱ҽ��
	private int recLen = 180; 
	private int queryLen = 0; 
    ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
    private int iszhienable=0;//1���ʹ�ָ��,0��û���ʹ�ָ��,2����Ͷ���Ѿ�����
    private boolean isempcoin=false;//false��δ���͹�ֽ����ָ�true��Ϊȱ�ң��Ѿ����͹�ֽ����ָ��
    private int dispenser=0;//0��,1hopper,2mdb
    private boolean ischuhuo=false;//true�Ѿ��������ˣ������ϱ���־
//	private String proID = null;
//	private String productID = null;
//	private String proType = null;
//	private String cabID = null;
//	private String huoID = null;
//    private String prosales = null;
//    private String count = null;
//    private String reamin_amount = null;
    private String zhifutype = "0";//0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
//    private String id="";
    private String out_trade_no=null;
    private int iszhiamount=0;//1�ɹ�Ͷ��Ǯ,0û�гɹ�Ͷ��Ǯ    
    //COM�������
  	LocalBroadcastManager comBroadreceiver;
  	COMReceiver comreceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ��title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ȫ��
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.buszhiamount);
		BusZhiAmountAct = this;
		//����Ʒҳ����ȡ����ѡ�е���Ʒ
//		Intent intent=getIntent();
//		Bundle bundle=intent.getExtras();
//		proID=bundle.getString("proID");
//		productID=bundle.getString("productID");
//		proType=bundle.getString("proType");
//		cabID=bundle.getString("cabID");
//		huoID=bundle.getString("huoID");
//		prosales=bundle.getString("prosales");
//		count=bundle.getString("count");
//		reamin_amount=bundle.getString("reamin_amount");
		out_trade_no=ToolClass.out_trade_no(BusZhiAmount.this);
		amount=OrderDetail.getShouldPay()*OrderDetail.getShouldNo();
		OrderDetail.setOrdereID(out_trade_no);
    	OrderDetail.setPayType(Integer.parseInt(zhifutype));
		txtbuszhiamountcount= (TextView) findViewById(R.id.txtbuszhiamountcount);
		txtbuszhiamountcount.setText(String.valueOf(OrderDetail.getShouldNo()));
		txtbuszhiamountAmount= (TextView) findViewById(R.id.txtbuszhiamountAmount);
		txtbuszhiamountAmount.setText(String.valueOf(amount));
		txtbuszhiamountbillAmount= (TextView) findViewById(R.id.txtbuszhiamountbillAmount);		
		txtbuszhiamounttime = (TextView) findViewById(R.id.txtbuszhiamounttime);
		txtbuszhiamounttsxx = (TextView) findViewById(R.id.txtbuszhiamounttsxx);
		timer.scheduleWithFixedDelay(task, 1, 1, TimeUnit.SECONDS);       // timeTask 
		imgbtnbuszhiamountqxzf = (ImageButton) findViewById(R.id.imgbtnbuszhiamountqxzf);
		imgbtnbuszhiamountqxzf.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	if(BusgoodsSelect.BusgoodsSelectAct!=null)
					BusgoodsSelect.BusgoodsSelectAct.finish(); 
		    	finishActivity();
		    }
		});
		imgbtnbuszhiamountqtzf = (ImageButton) findViewById(R.id.imgbtnbuszhiamountqtzf);
		imgbtnbuszhiamountqtzf.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	finishActivity();
		    }
		});
		this.imgbtnbusgoodsback=(ImageView)findViewById(R.id.imgbtnbusgoodsback);
		imgbtnbusgoodsback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	if(BusgoodsSelect.BusgoodsSelectAct!=null)
					BusgoodsSelect.BusgoodsSelectAct.finish(); 
		    	finishActivity();
		    }
		});
		//4.ע�������
		comBroadreceiver = LocalBroadcastManager.getInstance(this);
		comreceiver=new COMReceiver();
		IntentFilter comfilter=new IntentFilter();
		comfilter.addAction("android.intent.action.comrec");
		comBroadreceiver.registerReceiver(comreceiver,comfilter);
				
		//��ֽ��Ӳ����
		//��ʱ1s
	    new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {            	
        		//��ֽ��Ӳ����
            	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,1);
            	BillEnable(1);	
            }

		}, SPLASH_DISPLAY_LENGHT);
    	
	}
	//2.����COMReceiver�Ľ������㲥���������շ�����ͬ��������
	public class COMReceiver extends BroadcastReceiver 
	{
		int con=0;
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			// TODO Auto-generated method stub
			Bundle bundle=intent.getExtras();
			int EVWhat=bundle.getInt("EVWhat");
			switch(EVWhat)
			{
			//��������	
			case COMService.EV_OPTMAIN: 
				SerializableMap serializableMap = (SerializableMap) bundle.get("result");
				Map<String, Integer> Set=serializableMap.getMap();
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMBusAmount �ֽ��豸����="+Set,"com.txt");
				int EV_TYPE=Set.get("EV_TYPE");
				switch(EV_TYPE)
				{
					case EVprotocol.EV_MDB_ENABLE:	
						//��ʧ��,�ȴ����´�
						if( ((Integer)Set.get("bill_result")==0)&&((Integer)Set.get("coin_result")==0) )
						{
							txtbuszhiamounttsxx.setText("��ʾ��Ϣ������"+con);
							if((Integer)Set.get("bill_result")==0)
								ToolClass.setBill_err(2);
							if((Integer)Set.get("coin_result")==0)
								ToolClass.setCoin_err(2);
							con++;
						}
						//�򿪳ɹ�
						else
						{
							//��һ�δ򿪲ŷ���coninfo���Ժ�Ͳ��ٲ��������
							if(iszhienable==0)
							{
								//EVprotocolAPI.EV_mdbCoinInfoCheck(ToolClass.getCom_id());
								//Ӳ������ѯ�ӿ�
								Intent intent3=new Intent();
						    	intent3.putExtra("EVWhat", EVprotocol.EV_MDB_C_INFO);	
								intent3.setAction("android.intent.action.comsend");//action���������ͬ
								comBroadreceiver.sendBroadcast(intent3);
							}
							ToolClass.setBill_err(0);
							ToolClass.setCoin_err(0);
						}
						break;
					case EVprotocol.EV_MDB_B_INFO:
						break;
					case EVprotocol.EV_MDB_C_INFO:
						dispenser=(Integer)Set.get("dispenser");
					    //Heart����
					    Intent intent4=new Intent();
				    	intent4.putExtra("EVWhat", EVprotocol.EV_MDB_HEART);
						intent4.setAction("android.intent.action.comsend");//action���������ͬ
						comBroadreceiver.sendBroadcast(intent4);
						break;	
					case EVprotocol.EV_MDB_HEART://������ѯ
						Map<String,Object> obj=new LinkedHashMap<String, Object>();
						obj.putAll(Set);
						iszhienable=1;					
						String bill_enable="";
						String coin_enable="";
						String hopperString="";
						int bill_err=ToolClass.getvmcStatus(obj,1);
						int coin_err=ToolClass.getvmcStatus(obj,2);
						ToolClass.setBill_err(bill_err);
						ToolClass.setCoin_err(coin_err);
						if(bill_err>0)
							bill_enable="[ֽ����]�޷�ʹ��";
						if(coin_err>0)
							coin_enable="[Ӳ����]�޷�ʹ��";
						int hopper1=0;
						if(dispenser==1)//hopper
					  	{
							hopper1=ToolClass.getvmcStatus(obj,3);
							if(hopper1>0)
								hopperString="[������]:"+ToolClass.gethopperstats(hopper1);
					  	}
						else if(dispenser==2)//mdb
					  	{
					  		//��ǰ��ҽ��С��5Ԫ
					  		if(ToolClass.MoneyRec((Integer)Set.get("coin_remain"))<5)
					  		{
					  			hopperString="[������]:ȱ��";
					  		}
					  	}
					  	txtbuszhiamounttsxx.setText("��ʾ��Ϣ��"+bill_enable+coin_enable+hopperString);
					  	billmoney=ToolClass.MoneyRec((Integer)Set.get("bill_recv"));	
					  	coinmoney=ToolClass.MoneyRec((Integer)Set.get("coin_recv"));
					  	money=billmoney+coinmoney;
					  	//���ȱ��,�Ͱ�ֽ��Ӳ�����ر�
					  	if(dispenser==1)//hopper
					  	{
					  		if(hopper1>0)//hopperȱ��
					  		{
						  		if(isempcoin==false)//��һ�ιر�ֽ��Ӳ����
						  		{
						  			//�ر�ֽ��Ӳ����
						  	    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,0);
						  			BillEnable(0);		
						  			isempcoin=true;
						  		}
					  		}
					  	}
					  	else if(dispenser==2)//mdb
					  	{
					  		//��ǰ��ҽ��С��5Ԫ
					  		if(ToolClass.MoneyRec((Integer)Set.get("coin_remain"))<5)
					  		{
					  			if(isempcoin==false)//��һ�ιر�ֽ��Ӳ����
						  		{
						  			//�ر�ֽ��Ӳ����
						  	    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,0);
					  				BillEnable(0);	
						  			isempcoin=true;
						  		}
					  		}
					  	}
					  	
					  	if(money>0)
					  	{					  		
					  		iszhiamount=1;
					  		recLen = 180;//��Ͷ�Һ󵹼�ʱ���ü�����
					  		txtbuszhiamountbillAmount.setText(String.valueOf(money));
					  		OrderDetail.setSmallNote(billmoney);
					  		OrderDetail.setSmallConi(coinmoney);
					  		OrderDetail.setSmallAmount(money);
					  		if(money>=amount)
					  		{
					  			iszhienable=2;
					  			timer.shutdown(); 
					  			tochuhuo();
					  		}
					  	}
						break;
					case EVprotocol.EV_MDB_PAYOUT://����			
						break;	
					case EVprotocol.EV_MDB_PAYBACK://�˱�
						RealNote=ToolClass.MoneyRec((Integer)Set.get("bill_changed"));	
						RealCoin=ToolClass.MoneyRec((Integer)Set.get("coin_changed"));	
						RealAmount=RealNote+RealCoin;						
						OrderDetail.setRealNote(RealNote);
				    	OrderDetail.setRealCoin(RealCoin);
				    	OrderDetail.setRealAmount(RealAmount);
				    	//�˱����
				    	if(RealAmount==money)
				    	{
				    		OrderDetail.setRealStatus(1);//�˿����				    		
				    	}
				    	//Ƿ��
				    	else
				    	{
				    		OrderDetail.setRealStatus(3);//�˿�ʧ��
				    		OrderDetail.setDebtAmount(money-RealAmount);//Ƿ����
				    	}
				    	if(ischuhuo==true)
				    	{
				    		OrderDetail.addLog(BusZhiAmount.this);
				    	}
				    	else
				    	{
				    		OrderDetail.cleardata();
						}	
			  	    	dialog.dismiss();
			  	    	finish();
						break; 	
					default:break;	
				}
				break;			
			}			
		}

	}
		
	//���õ���ʱ��ʱ��
	TimerTask task = new TimerTask() { 
        @Override 
        public void run() { 
  
            runOnUiThread(new Runnable() {      // UI thread 
                @Override 
                public void run() { 
                    recLen--; 
                    txtbuszhiamounttime.setText("����ʱ:"+recLen); 
                    ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<recLen="+recLen,"log.txt");
                    if(recLen <= 0)
                    { 
                    	timer.shutdown(); 
                        finishActivity();
                    } 
                    //���Ͳ�ѯ����ָ��
                    if(iszhienable==1)
                    {
	                    queryLen++;
	                    if(queryLen>=5)
	                    {
	                    	queryLen=0;
	                    	//EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
	                    	//Heart����
						    Intent intent2=new Intent();
					    	intent2.putExtra("EVWhat", EVprotocol.EV_MDB_HEART);
							intent2.setAction("android.intent.action.comsend");//action���������ͬ
							comBroadreceiver.sendBroadcast(intent2);
	                    }
                    }
                    //���ʹ�ֽ��Ӳ����ָ��
                    else if(iszhienable==0)
                    {
                    	queryLen++;
	                    if(queryLen>=10)
	                    {
	                    	queryLen=0;
	                    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,1);
	                    	BillEnable(1);
	                    }
                    }
                } 
            }); 
        } 
    };
    //��������
    private void tochuhuo()
    {        
    	ischuhuo=true;
    	BillEnable(0);    	   
    	//��ʱ
	    new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {   
            	Intent intent = null;// ����Intent����                
            	intent = new Intent(BusZhiAmount.this, BusHuo.class);// ʹ��Accountflag���ڳ�ʼ��Intent
//            	intent.putExtra("out_trade_no", out_trade_no);
//            	intent.putExtra("proID", proID);
//            	intent.putExtra("productID", productID);
//            	intent.putExtra("proType", proType);
//            	intent.putExtra("cabID", cabID);
//            	intent.putExtra("huoID", huoID);
//            	intent.putExtra("prosales", prosales);
//            	intent.putExtra("count", count);
//            	intent.putExtra("reamin_amount", reamin_amount);
//            	intent.putExtra("zhifutype", zhifutype);
            	startActivityForResult(intent, REQUEST_CODE);// ��Accountflag
            }

		}, 2500);    	
    }
    //����BusHuo������Ϣ
  	@Override
  	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  		// TODO Auto-generated method stub
  		if(requestCode==REQUEST_CODE)
  		{
  			if(resultCode==BusZhiAmount.RESULT_CANCELED)
  			{
  				Bundle bundle=data.getExtras();
  				int status=bundle.getInt("status");//�������1�ɹ�,0ʧ��
  				//1.
  				//�����ɹ�,��Ǯ
				if(status==1)
				{
					//��Ǯ
		  	    	//EVprotocolAPI.EV_mdbCost(ToolClass.getCom_id(),ToolClass.MoneySend(amount));
					Intent intent=new Intent();
			    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_COST);	
					intent.putExtra("cost", ToolClass.MoneySend((float)amount));	
					intent.setAction("android.intent.action.comsend");//action���������ͬ
					comBroadreceiver.sendBroadcast(intent);
					money-=amount;
				}
				//����ʧ��,����Ǯ
				else
				{					
				}
				//2.����Ͷ�ҽ��
  				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�˿�money="+money,"log.txt");
  				txtbuszhiamountbillAmount.setText(String.valueOf(money));
  				  				
  				//��������˱�
  				if(money>0)
  				{
  					dialog= ProgressDialog.show(BusZhiAmount.this,"�����˱���","���Ժ�...");
  					new Handler().postDelayed(new Runnable() 
					{
			            @Override
			            public void run() 
			            {            	
			            	//�˱�
		  	  	  	    	//EVprotocolAPI.EV_mdbPayback(ToolClass.getCom_id(),1,1);
		  					Intent intent=new Intent();
		  			    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_PAYBACK);	
		  					intent.putExtra("bill", 1);	
		  					intent.putExtra("coin", 1);	
		  					intent.setAction("android.intent.action.comsend");//action���������ͬ
		  					comBroadreceiver.sendBroadcast(intent);
			            }

					}, 500);  					
				}
  			    //ûʣ������ˣ����˱�
  				else
  				{  					
			    	OrderDetail.addLog(BusZhiAmount.this);			    	
			    	//��ֽ��Ӳ����					
				    new Handler().postDelayed(new Runnable() 
					{
			            @Override
			            public void run() 
			            {            	
			            	finish();
			            }

					}, 500);
  				}
  			}			
  		}
  	}
    //��������
  	private void finishActivity()
  	{
  		timer.shutdown(); 
  		if(iszhiamount==1)
  		{
  			dialog= ProgressDialog.show(BusZhiAmount.this,"�����˱���","���Ժ�...");
  			OrderDetail.setPayStatus(2);//֧��ʧ��
  			//�˱�
  	    	//EVprotocolAPI.EV_mdbPayback(ToolClass.getCom_id(),1,1);
  			Intent intent=new Intent();
	    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_PAYBACK);	
			intent.putExtra("bill", 1);	
			intent.putExtra("coin", 1);	
			intent.setAction("android.intent.action.comsend");//action���������ͬ
			comBroadreceiver.sendBroadcast(intent);
  		} 
  		else 
  		{	
  			finish();
		}  		
  	}
  	
    //1��,0�رչر�ֽ��Ӳ����   
  	private void BillEnable(int opt)
  	{  		 	
		Intent intent=new Intent();
		intent.putExtra("EVWhat", EVprotocol.EV_MDB_ENABLE);	
		intent.putExtra("bill", 1);	
		intent.putExtra("coin", 1);	
		intent.putExtra("opt", opt);	
		intent.setAction("android.intent.action.comsend");//action���������ͬ
		comBroadreceiver.sendBroadcast(intent);	
  	}
  	
  	@Override
	protected void onDestroy() {
		//=============
  		//COM�������
  		//=============
  		//�ر�ֽ��Ӳ����
    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,0);  
  		BillEnable(0);	
  		//5.���ע�������
  		comBroadreceiver.unregisterReceiver(comreceiver);
	    super.onDestroy();		
	}
}


