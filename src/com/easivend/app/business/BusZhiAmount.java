package com.easivend.app.business;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.app.maintain.AddInaccount.COMReceiver;
import com.easivend.common.OrderDetail;
import com.easivend.common.SerializableMap;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.http.EVServerhttp;
import com.easivend.http.Zhifubaohttp;
import com.easivend.model.Tb_vmc_system_parameter;
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
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class BusZhiAmount  extends Activity
{
	private final int SPLASH_DISPLAY_LENGHT = 1500; // �ӳ�1.5��
	//���ȶԻ���
	ProgressDialog dialog= null;
	public static BusZhiAmount BusZhiAmountAct=null;
	private final static int REQUEST_CODE=1;//���������ʶ
	TextView txtbuszhiamountcount=null,txtbuszhiamountAmount=null,txtbuszhiamountbillAmount=null,txtbuszhiamounttime=null,
			txtbuszhiamounttsxx=null;
	ImageButton imgbtnbuszhiamountqxzf=null,imgbtnbuszhiamountqtzf=null;
	float amount=0;//��Ʒ��Ҫ֧�����
	float billmoney=0,coinmoney=0,money=0;//Ͷ�ҽ��
	float RealNote=0,RealCoin=0,RealAmount=0;//�˱ҽ��
	private int recLen = 180; 
	private int queryLen = 0; 
    private TextView txtView; 
    ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
    private int iszhienable=0;//1���ʹ�ָ��,0��û���ʹ�ָ��
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
		//4.ע�������
		comBroadreceiver = LocalBroadcastManager.getInstance(this);
		comreceiver=new COMReceiver();
		IntentFilter comfilter=new IntentFilter();
		comfilter.addAction("android.intent.action.comrec");
		comBroadreceiver.registerReceiver(comreceiver,comfilter);
		//*****************
		//ע��Ͷ�����������
		//*****************
		EVprotocolAPI.setCallBack(new jniInterfaceImp());

		//��ֽ��Ӳ����
    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,1);
    	Intent intent=new Intent();
    	intent.putExtra("EVWhat", COMService.EV_MDB_ENABLE);	
		intent.putExtra("bill", 1);	
		intent.putExtra("coin", 1);	
		intent.putExtra("opt", 1);	
		intent.setAction("android.intent.action.comsend");//action���������ͬ
		comBroadreceiver.sendBroadcast(intent);
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
					case COMService.EV_MDB_ENABLE:	
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
						    	intent3.putExtra("EVWhat", COMService.EV_MDB_C_INFO);	
								intent3.setAction("android.intent.action.comsend");//action���������ͬ
								comBroadreceiver.sendBroadcast(intent3);
							}
							ToolClass.setBill_err(0);
							ToolClass.setCoin_err(0);
						}
						break;
//					case COMService.EV_MDB_B_INFO:
//						break;
//					case COMService.EV_MDB_C_INFO:
//						String acceptor2="";
//						if((Integer)Set.get("acceptor")==3)
//							acceptor2="��������";
//						else if((Integer)Set.get("acceptor")==2)
//							acceptor2="MDB";
//						else if((Integer)Set.get("acceptor")==1)
//							acceptor2="��������";
//						else if((Integer)Set.get("acceptor")==0)
//							acceptor2="��";
//						String dispenser2="";
//						if((Integer)Set.get("dispenser")==2)
//							dispenser2="MDB";
//						else if((Integer)Set.get("dispenser")==1)
//							dispenser2="hopper";
//						else if((Integer)Set.get("dispenser")==0)
//							dispenser2="��";
//						String code2=String.valueOf(Set.get("code"));
//						String sn2=String.valueOf(Set.get("sn"));
//						String model2=String.valueOf(Set.get("model"));
//						String ver2=String.valueOf(Set.get("ver"));
//						int capacity2=(Integer)Set.get("capacity");
//						String str2="Ӳ�ҽ�����:"+acceptor2+"Ӳ��������:"+dispenser2+"����:"+code2
//								+"���к�"+sn2;
//						txtcoinmanagerpar.setText(str2);
//						str2=" �ͺ�:"+model2+"�汾��:"+ver2+"������:"+capacity2;
//						txtcoinmanagerpar2.setText(str2);
//						spincoinmanagercoin.setSelection((Integer)Set.get("acceptor"));
//						
//						
//						str2="";
//						Map<String,Integer> allSet3=new LinkedHashMap<String, Integer>();
//						allSet3.put("ch_r1", Set.get("ch_r1"));
//						allSet3.put("ch_r2", Set.get("ch_r2"));
//						allSet3.put("ch_r3", Set.get("ch_r3"));
//						allSet3.put("ch_r4", Set.get("ch_r4"));
//						allSet3.put("ch_r5", Set.get("ch_r5"));
//						allSet3.put("ch_r6", Set.get("ch_r6"));
//						allSet3.put("ch_r7", Set.get("ch_r7"));
//						allSet3.put("ch_r8", Set.get("ch_r8"));
//						//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+allSet3.toString());
//						double all[]=new double[allSet3.size()];	
//						int i=0;
//						Set<Map.Entry<String,Integer>> allset3=allSet3.entrySet();  //ʵ����
//						Iterator<Map.Entry<String,Integer>> iter3=allset3.iterator();
//					    while(iter3.hasNext())
//					    {
//					        Map.Entry<String,Integer> me=iter3.next();
//					        all[i++]=ToolClass.MoneyRec(me.getValue());
//					        //str+="[ͨ��"+me.getKey() + "]=" + ToolClass.MoneyRec(me.getValue()) + ",";
//					    }
//					    txtcoinmanagercoinin1.setText(String.valueOf(all[0]));
//					    txtcoinmanagercoinin2.setText(String.valueOf(all[1]));
//					    txtcoinmanagercoinin3.setText(String.valueOf(all[2]));
//					    txtcoinmanagercoinin4.setText(String.valueOf(all[3]));
//					    txtcoinmanagercoinin5.setText(String.valueOf(all[4]));
//					    txtcoinmanagercoinin6.setText(String.valueOf(all[5]));
//					    txtcoinmanagercoinin7.setText(String.valueOf(all[6]));
//					    txtcoinmanagercoinin8.setText(String.valueOf(all[7]));
//					    txtcoinmanagercoinin9.setText("0");
//					    txtcoinmanagercoinin10.setText("0");
//					    txtcoinmanagercoinin11.setText("0");
//					    txtcoinmanagercoinin12.setText("0");
//					    txtcoinmanagercoinin13.setText("0");
//					    txtcoinmanagercoinin14.setText("0");
//					    txtcoinmanagercoinin15.setText("0");
//					    txtcoinmanagercoinin16.setText("0");
//					    //����ͨ����ֵ
//					    spinhopper.setSelection((Integer)Set.get("dispenser"));
//					    str2="";
//					    Map<String,Integer> allSet4=new LinkedHashMap<String, Integer>();
//						allSet4.put("ch_d1", Set.get("ch_d1"));
//						allSet4.put("ch_d2", Set.get("ch_d2"));
//						allSet4.put("ch_d3", Set.get("ch_d3"));
//						allSet4.put("ch_d4", Set.get("ch_d4"));
//						allSet4.put("ch_d5", Set.get("ch_d5"));
//						allSet4.put("ch_d6", Set.get("ch_d6"));
//						allSet4.put("ch_d7", Set.get("ch_d7"));
//						allSet4.put("ch_d8", Set.get("ch_d8"));
//					    ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+allSet4.toString(),"log.txt");
//					    double all2[]=new double[allSet4.size()];	
//						i=0;
//						Set<Map.Entry<String,Integer>> allset4=allSet4.entrySet();  //ʵ����
//					    Iterator<Map.Entry<String,Integer>> iter4=allset4.iterator();
//					    while(iter4.hasNext())
//					    {
//					        Map.Entry<String,Integer> me=iter4.next();
//					        all2[i++]=ToolClass.MoneyRec(me.getValue());
//					        //str2+="[ͨ��"+me.getKey() + "]=" + ToolClass.MoneyRec(me.getValue()) + ",";
//					    }
//					    txthopperin1.setText(String.valueOf(all2[0]));
//					    txthopperin2.setText(String.valueOf(all2[1]));
//					    txthopperin3.setText(String.valueOf(all2[2]));
//					    txthopperin4.setText(String.valueOf(all2[3]));
//					    txthopperin5.setText(String.valueOf(all2[4]));
//					    txthopperin6.setText(String.valueOf(all2[5]));
//					    txthopperin7.setText(String.valueOf(all2[6]));
//					    txthopperin8.setText(String.valueOf(all2[7]));
//					    //Heart����
//					    Intent intent4=new Intent();
//				    	intent4.putExtra("EVWhat", COMService.EV_MDB_HEART);
//						intent4.setAction("android.intent.action.comsend");//action���������ͬ
//						comBroadreceiver.sendBroadcast(intent4);
//						devopt=COMService.EV_MDB_HEART;//Heart����
//						break;	
//					case COMService.EV_MDB_HEART://������ѯ
//						Map<String,Object> obj=new LinkedHashMap<String, Object>();
//						obj.putAll(Set);
//						String bill_enable=((Integer)Set.get("bill_enable")==1)?"ʹ��":"����";
//						txtbillmanagerstate.setText(bill_enable);
//						String bill_payback=((Integer)Set.get("bill_payback")==1)?"����":"û����";
//					  	txtbillpayback.setText(bill_payback);
//					  	String bill_err=((Integer)Set.get("bill_err")==0)?"����":"������:"+(Integer)Set.get("bill_err");
//					  	txtbillerr.setText(bill_err);
//					  	double money=ToolClass.MoneyRec((Integer)Set.get("bill_recv"));					  	
//					  	txtbillpayin.setText(String.valueOf(money));
//					  	amount=money;//��ǰֽ��Ͷ��
//					  	money=ToolClass.MoneyRec((Integer)Set.get("bill_remain"));
//					  	txtbillmanagerbillpayamount.setText(String.valueOf(money));
//					  	int bill_errstatus=ToolClass.getvmcStatus(obj,1);
//					  	ToolClass.setBill_err(bill_errstatus);
//					  	
//					  	String coin_enable=((Integer)Set.get("coin_enable")==1)?"ʹ��":"����";
//					  	txtcoinmanagerstate.setText(coin_enable);
//						String coin_payback=((Integer)Set.get("coin_payback")==1)?"����":"û����";
//						txtcoinpayback.setText(coin_payback);
//					  	String coin_err=((Integer)Set.get("coin_err")==0)?"����":"������:"+(Integer)Set.get("coin_err");
//					  	txtcoinerr.setText(coin_err);
//					  	money=ToolClass.MoneyRec((Integer)Set.get("coin_recv"));					  	
//					  	txtcoinpayin.setText(String.valueOf(money));
//					  	amount+=money;//��ǰӲ��Ͷ��
//					  	money=ToolClass.MoneyRec((Integer)Set.get("coin_remain"));
//					  	txtcoinmanagercoininamount.setText(String.valueOf(money));
//					  	int coin_errstatus=ToolClass.getvmcStatus(obj,2);
//					  	ToolClass.setCoin_err(coin_errstatus);
//					  	
//					  	String hopperString=null;
//					  	if(Set.containsKey("hopper1"))
//					  	{
//						  	hopperString="[1]:"+ToolClass.gethopperstats((Integer)Set.get("hopper1"))+"[2]:"+ToolClass.gethopperstats((Integer)Set.get("hopper2"))
//						  				+"[[3]:"+ToolClass.gethopperstats((Integer)Set.get("hopper3"))+"[4]:"+ToolClass.gethopperstats((Integer)Set.get("hopper4"))
//							  			+"[5]:"+ToolClass.gethopperstats((Integer)Set.get("hopper5"))+"[6]:"+ToolClass.gethopperstats((Integer)Set.get("hopper6"))
//							  			+"[7]:"+ToolClass.gethopperstats((Integer)Set.get("hopper7"))+"[8]:"+ToolClass.gethopperstats((Integer)Set.get("hopper8"));
//					  	}
//					  	txthopperincount.setText(hopperString);
//						break;
//					case EVprotocolAPI.EV_MDB_PAYOUT://����
//						money=ToolClass.MoneyRec((Integer)Set.get("bill_changed"));					  	
//						txtpaymoney.setText(String.valueOf(money));	
//						money=ToolClass.MoneyRec((Integer)Set.get("coin_changed"));					  	
//						txtcoinpaymoney.setText(String.valueOf(money));	
//						txthopperpaymoney.setText(String.valueOf(money));	
//						break;
//					case EVprotocolAPI.EV_MDB_HP_PAYOUT://����
//						txthopperpaynum.setText(String.valueOf((Integer)Set.get("changed")));
//						
//						break;	
					default:break;	
				}
				break;			
			}			
		}

	}
	//����һ��ר�Ŵ������ӿڵ�����
	private class jniInterfaceImp implements JNIInterface
	{
		int con=0;
		@Override
		public void jniCallback(Map<String, Object> allSet) {
			float payin_amount=0,reamin_amount=0,payout_amount=0;
			// TODO Auto-generated method stub	
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<mdb�豸���","log.txt");
			Map<String, Object> Set= allSet;
			int jnirst=(Integer)Set.get("EV_TYPE");
			switch (jnirst)
			{
				case EVprotocolAPI.EV_MDB_ENABLE://�������߳�Ͷ�ҽ����Ϣ	
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
							EVprotocolAPI.EV_mdbCoinInfoCheck(ToolClass.getCom_id());
						ToolClass.setBill_err(0);
						ToolClass.setCoin_err(0);
					}										
					break;
				case EVprotocolAPI.EV_MDB_B_INFO:	
					break;
				case EVprotocolAPI.EV_MDB_C_INFO:
					dispenser=(Integer)Set.get("dispenser");
					EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
					break;	
				case EVprotocolAPI.EV_MDB_HEART://������ѯ
					iszhienable=1;					
					String bill_enable="";
					String coin_enable="";
					String hopperString="";
					int bill_err=ToolClass.getvmcStatus(Set,1);
					int coin_err=ToolClass.getvmcStatus(Set,2);
					ToolClass.setBill_err(bill_err);
					ToolClass.setCoin_err(coin_err);
					int hopper1=ToolClass.getvmcStatus(Set,3);
					if(bill_err>0)
						bill_enable="[ֽ����]�޷�ʹ��";
					if(coin_err>0)
						coin_enable="[Ӳ����]�޷�ʹ��";					
					if(hopper1>0)
						hopperString="[������]:"+ToolClass.gethopperstats(hopper1);
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
					  			Intent intent=new Intent();
						    	intent.putExtra("EVWhat", COMService.EV_MDB_ENABLE);	
								intent.putExtra("bill", 1);	
								intent.putExtra("coin", 1);	
								intent.putExtra("opt", 0);	
								intent.setAction("android.intent.action.comsend");//action���������ͬ
								comBroadreceiver.sendBroadcast(intent);	
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
				  				Intent intent=new Intent();
						    	intent.putExtra("EVWhat", COMService.EV_MDB_ENABLE);	
								intent.putExtra("bill", 1);	
								intent.putExtra("coin", 1);	
								intent.putExtra("opt", 0);	
								intent.setAction("android.intent.action.comsend");//action���������ͬ
								comBroadreceiver.sendBroadcast(intent);	
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
				  			timer.shutdown(); 
				  			tochuhuo();
				  		}
				  	}
					break;
				case EVprotocolAPI.EV_MDB_PAYOUT://����
					break;
				case EVprotocolAPI.EV_MDB_PAYBACK://�˱�
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
					//�ر�ֽ��Ӳ����
		  	    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,0);
			    	Intent intent=new Intent();
			    	intent.putExtra("EVWhat", COMService.EV_MDB_ENABLE);	
					intent.putExtra("bill", 1);	
					intent.putExtra("coin", 1);	
					intent.putExtra("opt", 0);	
					intent.setAction("android.intent.action.comsend");//action���������ͬ
					comBroadreceiver.sendBroadcast(intent);	
		  	    	dialog.dismiss();
		  	    	finish();
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
	                    if(queryLen>=1)
	                    {
	                    	queryLen=0;
	                    	EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
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
	                    	Intent intent=new Intent();
	        		    	intent.putExtra("EVWhat", COMService.EV_MDB_ENABLE);	
	        				intent.putExtra("bill", 1);	
	        				intent.putExtra("coin", 1);	
	        				intent.putExtra("opt", 1);	
	        				intent.setAction("android.intent.action.comsend");//action���������ͬ
	        				comBroadreceiver.sendBroadcast(intent);	
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
    	Intent intent = null;// ����Intent����                
    	intent = new Intent(BusZhiAmount.this, BusHuo.class);// ʹ��Accountflag���ڳ�ʼ��Intent
//    	intent.putExtra("out_trade_no", out_trade_no);
//    	intent.putExtra("proID", proID);
//    	intent.putExtra("productID", productID);
//    	intent.putExtra("proType", proType);
//    	intent.putExtra("cabID", cabID);
//    	intent.putExtra("huoID", huoID);
//    	intent.putExtra("prosales", prosales);
//    	intent.putExtra("count", count);
//    	intent.putExtra("reamin_amount", reamin_amount);
//    	intent.putExtra("zhifutype", zhifutype);    	
    	startActivityForResult(intent, REQUEST_CODE);// ��Accountflag
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
		  	    	EVprotocolAPI.EV_mdbCost(ToolClass.getCom_id(),ToolClass.MoneySend(amount));
		  	    	money-=amount;
				}
				//����ʧ��,����Ǯ
				else
				{					
				}
				//2.����Ͷ�ҽ��
  				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�˿�money="+money,"log.txt");
  				txtbuszhiamountbillAmount.setText(String.valueOf(money));
  				//ûʣ������ˣ����˱�
  				if(money==0)
  				{  					
			    	OrderDetail.addLog(BusZhiAmount.this);
					//�ر�ֽ��Ӳ����
		  	    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,0);
			    	Intent intent=new Intent();
			    	intent.putExtra("EVWhat", COMService.EV_MDB_ENABLE);	
					intent.putExtra("bill", 1);	
					intent.putExtra("coin", 1);	
					intent.putExtra("opt", 0);	
					intent.setAction("android.intent.action.comsend");//action���������ͬ
					comBroadreceiver.sendBroadcast(intent);	
		  	    	finish();
  				}
  				//�˱�
  				else 
  				{
  					dialog= ProgressDialog.show(BusZhiAmount.this,"�����˱���","���Ժ�...");
  					EVprotocolAPI.setCallBack(new jniInterfaceImp());
  					//�˱�
  	  	  	    	EVprotocolAPI.EV_mdbPayback(ToolClass.getCom_id(),1,1);
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
  	    	EVprotocolAPI.EV_mdbPayback(ToolClass.getCom_id(),1,1);
  		} 
  		else 
  		{
  			//�ر�ֽ��Ӳ����
  	    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,0);  
  			Intent intent=new Intent();
	    	intent.putExtra("EVWhat", COMService.EV_MDB_ENABLE);	
			intent.putExtra("bill", 1);	
			intent.putExtra("coin", 1);	
			intent.putExtra("opt", 0);	
			intent.setAction("android.intent.action.comsend");//action���������ͬ
			comBroadreceiver.sendBroadcast(intent);	
  			finish();
		}  		
  	}
  	
  	@Override
	protected void onDestroy() {
		//=============
  		//COM�������
  		//=============
  		//5.���ע�������
  		comBroadreceiver.unregisterReceiver(comreceiver);
	    super.onDestroy();		
	}
}


