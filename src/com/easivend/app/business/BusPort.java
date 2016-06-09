package com.easivend.app.business;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.app.maintain.MaintainActivity;
import com.easivend.app.maintain.MaintainActivity.EVServerReceiver;
import com.easivend.common.OrderDetail;
import com.easivend.common.SerializableMap;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_productDAO;
import com.easivend.evprotocol.EVprotocol;
import com.easivend.fragment.BusgoodsFragment;
import com.easivend.fragment.BusgoodsFragment.BusgoodsFragInteraction;
import com.easivend.fragment.BusgoodsclassFragment;
import com.easivend.fragment.BusgoodsclassFragment.BusgoodsclassFragInteraction;
import com.easivend.fragment.BusgoodsselectFragment;
import com.easivend.fragment.BusgoodsselectFragment.BusgoodsselectFragInteraction;
import com.easivend.fragment.BushuoFragment;
import com.easivend.fragment.BushuoFragment.BushuoFragInteraction;
import com.easivend.fragment.BusinessportFragment;
import com.easivend.fragment.BusinessportFragment.BusportFragInteraction;
import com.easivend.fragment.BuszhiamountFragment;
import com.easivend.fragment.BuszhiamountFragment.BuszhiamountFragInteraction;
import com.easivend.fragment.BuszhierFragment;
import com.easivend.fragment.BuszhierFragment.BuszhierFragInteraction;
import com.easivend.fragment.BuszhiweiFragment;
import com.easivend.fragment.BuszhiweiFragment.BuszhiweiFragInteraction;
import com.easivend.fragment.MoviewlandFragment.MovieFragInteraction;
import com.easivend.http.EVServerhttp;
import com.easivend.http.Weixinghttp;
import com.easivend.http.Zhifubaohttp;
import com.easivend.model.Tb_vmc_product;
import com.easivend.view.COMService;
import com.easivend.view.EVServerService;
import com.easivend.view.PassWord;
import com.example.evconsole.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BusPort extends Activity implements 
//businessҳ��ӿ�
MovieFragInteraction,BusportFragInteraction,
//busgoodsclassҳ��ӿ�
BusgoodsclassFragInteraction,
//busgoodsҳ��ӿ�
BusgoodsFragInteraction,
//Busgoodsselectҳ��ӿ�
BusgoodsselectFragInteraction,
//Buszhiamountҳ��ӿ�
BuszhiamountFragInteraction,
//Buszhierҳ��ӿ�
BuszhierFragInteraction,
//Buszhiweiҳ��ӿ�
BuszhiweiFragInteraction,
//Bushuoҳ��ӿ�
BushuoFragInteraction
{
	private BusinessportFragment businessportFragment;
	private BusgoodsclassFragment busgoodsclassFragment;
	private BusgoodsFragment busgoodsFragment;
	private BusgoodsselectFragment busgoodsselectFragment;
	private BuszhiamountFragment buszhiamountFragment;
	private BuszhierFragment buszhierFragment;
	private BuszhiweiFragment buszhiweiFragment;
	private BushuoFragment bushuoFragment;
	//����ҳ��
    Intent intent=null;
    //final static int REQUEST_CODE=1; 
    final static int PWD_CODE=2; 
    public static final int BUSPORT=1;//��ҳ��
    public static final int BUSGOODSCLASS=2;//��Ʒ���ҳ��
	public static final int BUSGOODS=3;//��Ʒ����ҳ��
	public static final int BUSGOODSSELECT=4;//��Ʒ��ϸҳ��
	public static final int BUSZHIAMOUNT=5;//�ֽ�֧��ҳ��
	public static final int BUSZHIER=6;//֧����֧��ҳ��
	public static final int BUSZHIWEI=7;//΢��֧��ҳ��
	public static final int BUSHUO=8;//����ҳ��
	private int gotoswitch=0;//��ǰ��ת���ĸ�ҳ��
	private int con=0;
	//���ȶԻ���
	ProgressDialog dialog= null;
	private String zhifutype = "0";//0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��,-1ȡ����
	private String out_trade_no=null;
	//Server�������
	LocalBroadcastManager localBroadreceiver;
	EVServerReceiver receiver;
	//=================
	//==�ֽ�֧��ҳ�����
	//=================
	private int queryLen = 0; 
    private int iszhienable=0;//1���ʹ�ָ��,0��û���ʹ�ָ�2����Ͷ���Ѿ�����
    private boolean isempcoin=false;//false��δ���͹�ֽ����ָ�true��Ϊȱ�ң��Ѿ����͹�ֽ����ָ��
    private int dispenser=0;//0��,1hopper,2mdb
	float billmoney=0,coinmoney=0,money=0;//Ͷ�ҽ��
	float amount=0;//��Ʒ��Ҫ֧�����
	private int iszhiamount=0;//1�ɹ�Ͷ��Ǯ,0û�гɹ�Ͷ��Ǯ
    private boolean ischuhuo=false;//true�Ѿ��������ˣ������ϱ���־
	float RealNote=0,RealCoin=0,RealAmount=0;//�˱ҽ��	
	//=================
	//==֧����֧��ҳ�����
	//=================
	//�߳̽���֧������ά�����
    private ExecutorService zhifubaothread=null;
    private Handler zhifubaomainhand=null,zhifubaochildhand=null;
    Zhifubaohttp zhifubaohttp=null;
    private int iszhier=0;//1�ɹ������˶�ά��,0û�гɹ����ɶ�ά�룬2���ν����Ѿ�����
    private boolean ercheck=false;//true���ڶ�ά����̲߳����У����Ժ�falseû�ж�ά����̲߳���
    //=================
  	//==΢��֧��ҳ�����
  	//=================
    //�߳̽���΢�Ŷ�ά�����
    private ExecutorService weixingthread=null;
    private Handler weixingmainhand=null,weixingchildhand=null;   
    Weixinghttp weixinghttp=null;
    private int iszhiwei=0;//1�ɹ������˶�ά��,0û�гɹ����ɶ�ά�룬2����Ͷ���Ѿ�����
    //=================
	//==����ҳ�����
	//=================
	private int status=0;//�������	
	ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
    private final int SPLASH_DISPLAY_LENGHT = 5*60; //  5*60�ӳ�5����	
    private int recLen = SPLASH_DISPLAY_LENGHT; 
    private boolean isbus=true;//true��ʾ�ڹ��ҳ�棬false������ҳ��
    //=================
    //COM�������
    //=================
  	LocalBroadcastManager comBroadreceiver;
  	COMReceiver comreceiver;
    
    //=========================
    //activity��fragment�ص����
    //=========================
    /**
     * ����������fragment������,���þ�̬����
     */
    public static BusPortFragInteraction listterner;
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//        if(activity instanceof BuszhiamountFragInteraction)
//        {
//            listterner = (BuszhiamountFragInteraction)activity;
//        }
//        else{
//            throw new IllegalArgumentException("activity must implements BuszhiamountFragInteraction");
//        }
//
//    }
    /**
     * ����������fragment������,
     * ����һ������������fragment����ʵ�ֵĽӿ�
     */
    public interface BusPortFragInteraction
    {
        /**
         * Activity ��Fragment����ָ�����������Ը�������������
         * @param str
         */
    	//��Ƶҳ��
        void BusportMovie();      //��Ƶҳ���ֲ�
        //ˢ�¹��ҳ��
        void BusportAds();      //ˢ�¹���б�
        //�ֽ�ҳ��
        void BusportTsxx(String str);      //��ʾ��Ϣ
        void BusportTbje(String str);      //Ͷ�ҽ��
        //����ҳ��
        void BusportChjg(int sta);      //�������
        //���ֽ�ҳ��
        void BusportSend(String str);      //��ά��
    }
        
    /**
     * ����������fragment������,
     * �����ġ���Fragment�����ص�activity��ʱ������ע��ص���Ϣ
     * @param activity
     */
  	public static void setCallBack(BusPortFragInteraction call){ 
  		listterner = call;
      } 
  	
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
		//=============
		//Server�������
		//=============
		//4.ע�������
		localBroadreceiver = LocalBroadcastManager.getInstance(this);
		receiver=new EVServerReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("android.intent.action.vmserverrec");
		localBroadreceiver.registerReceiver(receiver,filter);
		//=============
		//COM�������
		//=============
		//4.ע�������
		comBroadreceiver = LocalBroadcastManager.getInstance(this);
		comreceiver=new COMReceiver();
		IntentFilter comfilter=new IntentFilter();
		comfilter.addAction("android.intent.action.comrec");
		comBroadreceiver.registerReceiver(comreceiver,comfilter);
		timer.scheduleWithFixedDelay(new Runnable() { 
	        @Override 
	        public void run() { 
	        	  //ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<portthread="+Thread.currentThread().getId(),"log.txt");
	        	  if(isbus==false)
	        	  {
		        	  ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<recLen="+recLen,"log.txt");
		        	  recLen--; 
		        	  //�ص���ҳ
		        	  if(recLen == 0)
		              { 
		                  ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<recclose=BusinessportFragment","log.txt");
		                  //=================
			        	  //==�ֽ�֧��ҳ�����
			        	  //=================
				          if(gotoswitch==BUSZHIAMOUNT)
				          {
				        	  BuszhiamountFinish();
				          }
				          //=================
			        	  //==֧����֧��ҳ�����
			        	  //=================
				          else if(gotoswitch==BUSZHIER)
				          {
				        	  timeoutBuszhierFinish();
				          }
				          //=================
			        	  //==΢��֧��ҳ�����
			        	  //=================
				          else if(gotoswitch==BUSZHIWEI)
				          {
				        	  timeoutBuszhiweiFinish();
				          }
				          else
				          {
				        	  viewSwitch(BUSPORT,null);	  
						  }				          
		                  	                   
		              }	
		        	//=================
	        		//==�ֽ�֧��ҳ�����
	        		//=================
		        	if(gotoswitch==BUSZHIAMOUNT)
		        	{
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
						    	intent2.putExtra("EVWhat", COMService.EV_MDB_HEART);
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
		        		        	
		        	
		        	//=================
	        		//==֧����֧��ҳ�����
	        		//=================
		        	else if(gotoswitch==BUSZHIER)
		        	{
			        	//���Ͳ�ѯ����ָ��
	                    if(iszhier==1)
	                    {
		                    queryLen++;
		                    if(queryLen>=4)
		                    {
		                    	queryLen=0;
		                    	queryzhier();
		                    }
	                    }
	                    //���Ͷ�������ָ��
	                    else if(iszhier==0)
	                    {
		                    queryLen++;
		                    if(queryLen>=10)
		                    {
		                    	queryLen=0;
		                    	//���Ͷ���
		                		sendzhier();
		                    }
	                    }
		        	}
		        	
		        	//=================
	        		//==΢��֧��ҳ�����
	        		//=================
		        	else if(gotoswitch==BUSZHIWEI)
		        	{
			        	//���Ͳ�ѯ����ָ��
	                    if(iszhiwei==1)
	                    {
		                    queryLen++;
		                    if(queryLen>=4)
		                    {
		                    	queryLen=0;
		                    	queryzhiwei();
		                    }
	                    }
	                    //���Ͷ�������ָ��
	                    else if(iszhiwei==0)
	                    {
		                    queryLen++;
		                    if(queryLen>=10)
		                    {
		                    	queryLen=0;
		                    	//���Ͷ���
		                		sendzhiwei();
		                    }
	                    }
		        	}
	        	  }
	        } 
	    }, 1, 1, TimeUnit.SECONDS);       // timeTask 
		//��ʼ��Ĭ��fragment
		initView();
		//***********************
		//�߳̽���֧������ά�����
		//***********************
		zhifubaomainhand=new Handler()
		{			
			@Override
			public void handleMessage(Message msg) {
				//barzhifubaotest.setVisibility(View.GONE);
				ercheck=false;
				// TODO Auto-generated method stub				
				switch (msg.what)
				{
					case Zhifubaohttp.SETMAIN://���߳̽������߳���Ϣ
						listterner.BusportSend(msg.obj.toString());
						iszhier=1;
						break;
					case Zhifubaohttp.SETFAILNETCHILD://���߳̽������߳���Ϣ
						listterner.BusportTsxx("���׽��:����"+msg.obj.toString()+con);
						con++;
						break;		
					case Zhifubaohttp.SETPAYOUTMAIN://���߳̽������߳���Ϣ
//						listterner.BusportTsxx("���׽��:�˿�ɹ�");
//						dialog.dismiss();
//						//������
//						clearamount();						
//						recLen=10;						
						break;
					case Zhifubaohttp.SETDELETEMAIN://���߳̽������߳���Ϣ
//						listterner.BusportTsxx("���׽��:�����ɹ�");
//						clearamount();
//				    	viewSwitch(BUSPORT, null);
						break;	
					case Zhifubaohttp.SETQUERYMAINSUCC://���׳ɹ�
						listterner.BusportTsxx("���׽��:���׳ɹ�");
						iszhier=2;
//						//reamin_amount=String.valueOf(amount);
						tochuhuo();
						break;
					case Zhifubaohttp.SETQUERYMAIN://���߳̽������߳���Ϣ
					case Zhifubaohttp.SETFAILPROCHILD://���߳̽������߳���Ϣ
					case Zhifubaohttp.SETFAILBUSCHILD://���߳̽������߳���Ϣ	
					case Zhifubaohttp.SETFAILQUERYPROCHILD://���߳̽������߳���Ϣ
					case Zhifubaohttp.SETFAILQUERYBUSCHILD://���߳̽������߳���Ϣ	
					case Zhifubaohttp.SETFAILPAYOUTPROCHILD://���߳̽������߳���Ϣ		
					case Zhifubaohttp.SETFAILPAYOUTBUSCHILD://���߳̽������߳���Ϣ
					case Zhifubaohttp.SETFAILDELETEPROCHILD://���߳̽������߳���Ϣ		
					case Zhifubaohttp.SETFAILDELETEBUSCHILD://���߳̽������߳���Ϣ						
						listterner.BusportTsxx("���׽��:"+msg.obj.toString());
						break;	
				}				
			}
			
		};
		//�����û��Լ��������
		zhifubaohttp=new Zhifubaohttp(zhifubaomainhand);
		zhifubaothread=Executors.newCachedThreadPool();
		
		
		//***********************
		//�߳̽���΢�Ŷ�ά�����
		//***********************
		weixingmainhand=new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				//barweixingtest.setVisibility(View.GONE);
				ercheck=false;
				// TODO Auto-generated method stub				
				switch (msg.what)
				{
					case Weixinghttp.SETMAIN://���߳̽������߳���Ϣ
						listterner.BusportSend(msg.obj.toString());
						iszhiwei=1;
						break;
					case Weixinghttp.SETFAILNETCHILD://���߳̽������߳���Ϣ
						listterner.BusportTsxx("���׽��:����"+msg.obj.toString()+con);
						con++;						
						break;	
					case Weixinghttp.SETPAYOUTMAIN://���߳̽������߳���Ϣ
//						listterner.BusportTsxx("���׽��:�˿�ɹ�");
//						dialog.dismiss();
//						//������
//						clearamount();						
//						recLen=10;	
						break;
					case Weixinghttp.SETDELETEMAIN://���߳̽������߳���Ϣ
//						listterner.BusportTsxx("���׽��:�����ɹ�");
//						clearamount();
//				    	viewSwitch(BUSPORT, null);
						break;	
					case Weixinghttp.SETQUERYMAINSUCC://���߳̽������߳���Ϣ		
						listterner.BusportTsxx("���׽��:���׳ɹ�");
						//reamin_amount=String.valueOf(amount);
						iszhiwei=2;
						tochuhuo();
						break;
					case Weixinghttp.SETFAILPROCHILD://���߳̽������߳���Ϣ
					case Weixinghttp.SETFAILBUSCHILD://���߳̽������߳���Ϣ	
					case Weixinghttp.SETFAILQUERYPROCHILD://���߳̽������߳���Ϣ
					case Weixinghttp.SETFAILQUERYBUSCHILD://���߳̽������߳���Ϣ		
					case Weixinghttp.SETQUERYMAIN://���߳̽������߳���Ϣ	
					case Weixinghttp.SETFAILPAYOUTPROCHILD://���߳̽������߳���Ϣ		
					case Weixinghttp.SETFAILPAYOUTBUSCHILD://���߳̽������߳���Ϣ
					case Weixinghttp.SETFAILDELETEPROCHILD://���߳̽������߳���Ϣ		
					case Weixinghttp.SETFAILDELETEBUSCHILD://���߳̽������߳���Ϣ
						listterner.BusportTsxx("���׽��:"+msg.obj.toString());
						break;		
				}				
			}
			
		};
		//�����û��Լ��������
		weixinghttp=new Weixinghttp(weixingmainhand);
		weixingthread=Executors.newCachedThreadPool();
		
				
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

	//��������ʵ��Business�ӿ�,�������
	@Override
	public void finishBusiness() {
		// TODO Auto-generated method stub
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<busland=�˳�","log.txt");
//    	Intent intent = new Intent();
//    	intent.setClass(BusPort.this, PassWord.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
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
	//��������ʵ��Business�ӿ�,ת����Ʒ����ҳ��
	//buslevel��ת����ҳ��
	@Override
	public void gotoBusiness(int buslevel, Map<String, String> str)
	{
		viewSwitch(buslevel, str);
	}
	//��������ʵ��Business�ӿ�,����ȡ����
	@Override
	public void quhuoBusiness(String PICKUP_CODE)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<portȡ����="+PICKUP_CODE,"log.txt");
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
				out_trade_no=bundle.getString("out_trade_no");
				ToolClass.Log(ToolClass.INFO,"EV_JNI","BusPort=ȡ����ɹ�PRODUCT_NO="+PRODUCT_NO+"out_trade_no="+out_trade_no,"log.txt");					
				// ����InaccountDAO�������ڴ����ݿ�����ȡ���ݵ�Tb_vmc_product����
		 	    vmc_productDAO productdao = new vmc_productDAO(context);
		 	    Tb_vmc_product tb_vmc_product=productdao.find(PRODUCT_NO);
		 	    //���浽���������
		 	    //��������Ϣ
		 	    OrderDetail.setProID(tb_vmc_product.getProductID()+"-"+tb_vmc_product.getProductName());		 	    
		 	    OrderDetail.setProType("1");
		 	    //����֧���� 
		 	    zhifutype="-1";
		 	    OrderDetail.setShouldPay(tb_vmc_product.getSalesPrice());
		 	    OrderDetail.setShouldNo(1);
		 	    OrderDetail.setCabID("");
		 		OrderDetail.setColumnID("");
		 	    //������ϸ��Ϣ��   
		 	    OrderDetail.setProductID(PRODUCT_NO);
		 	    tochuhuo();
				break;
			case EVServerhttp.SETERRFAILPICKUPMAIN:
				ToolClass.Log(ToolClass.INFO,"EV_JNI","BusPort=ȡ����ʧ��","log.txt");
				// ������Ϣ��ʾ
				ToolClass.failToast("��Ǹ��ȡ������Ч,����ϵ����Ա��");
	    		break;	
			case EVServerhttp.SETADVRESETMAIN:
				ToolClass.Log(ToolClass.INFO,"EV_JNI","BusPort=ˢ�¹��","log.txt");
				listterner.BusportAds();
				break;
			}			
		}

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
	public void BusgoodsselectSwitch(int buslevel) {
		// TODO Auto-generated method stub		
		viewSwitch(buslevel, null);
	}
	//��������ʵ��Busgoodsselect�ӿ�,ת����ҳ��
	@Override
	public void BusgoodsselectFinish() {
		// TODO Auto-generated method stub
		viewSwitch(BUSPORT, null);
	}
	
	//=======================
	//ʵ��Buszhiamountҳ����ؽӿ�
	//=======================
	//��������ʵ��Buszhiamount�ӿ�,ת����ҳ��
	@Override
	public void BuszhiamountFinish() {
		// TODO Auto-generated method stub	
		if(iszhiamount==1)
  		{
  			dialog= ProgressDialog.show(BusPort.this,"�����˱���","���Ժ�...");
  			OrderDetail.setPayStatus(2);//֧��ʧ��
  			//�˱�
  	    	//EVprotocolAPI.EV_mdbPayback(ToolClass.getCom_id(),1,1);
  			Intent intent=new Intent();
	    	intent.putExtra("EVWhat", COMService.EV_MDB_PAYBACK);	
			intent.putExtra("bill", 1);	
			intent.putExtra("coin", 1);	
			intent.setAction("android.intent.action.comsend");//action���������ͬ
			comBroadreceiver.sendBroadcast(intent);
  		} 
  		else 
  		{
  			clearamount();
  			//�ر�ֽ��Ӳ����
  	    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,0);   
  			BillEnable(0);
  			viewSwitch(BUSPORT, null);
		} 	    
	    
	}
	
	
    
    //=======================
  	//ʵ��Buszhierҳ����ؽӿ�
  	//=======================
    //��������ʵ��Buszhier�ӿ�,ת����ҳ��
    @Override
	public void BuszhierFinish() {
		// TODO Auto-generated method stub
    	if(iszhier>0)
			deletezhier();
		else 
		{
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<viewSwitch=BUSPORT","log.txt");
	    	clearamount();
	    	viewSwitch(BUSPORT, null);
		}
	}
    //���ڳ�ʱ�Ľ�������
  	private void timeoutBuszhierFinish()
  	{
  		BuszhierFinish();
  	}
    
    //���Ͷ���
  	private void sendzhier()
  	{	
  		if(ercheckopt())
  		{
	      	// ����Ϣ���͵����߳���
	      	zhifubaochildhand=zhifubaohttp.obtainHandler();
	  		Message childmsg=zhifubaochildhand.obtainMessage();
	  		childmsg.what=Zhifubaohttp.SETCHILD;
	  		JSONObject ev=null;
	  		try {
	  			ev=new JSONObject();
	  			out_trade_no=ToolClass.out_trade_no(BusPort.this);// ����InaccountDAO����;
	  	        ev.put("out_trade_no", out_trade_no);
	  			ev.put("total_fee", String.valueOf(amount));
	  			Log.i("EV_JNI","Send0.1="+ev.toString());
	  		} catch (JSONException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  		childmsg.obj=ev;
	  		zhifubaochildhand.sendMessage(childmsg);
  		}
  	}
    //��ѯ����
  	private void queryzhier()
  	{
  		if(ercheckopt())
  		{
	  		// ����Ϣ���͵����߳���
	  		zhifubaochildhand=zhifubaohttp.obtainHandler();
	  		Message childmsg=zhifubaochildhand.obtainMessage();
	  		childmsg.what=Zhifubaohttp.SETQUERYCHILD;
	  		JSONObject ev=null;
	  		try {
	  			ev=new JSONObject();
	  			ev.put("out_trade_no", out_trade_no);		
	  			//ev.put("out_trade_no", "000120150301113215800");	
	  			Log.i("EV_JNI","Send0.1="+ev.toString());
	  		} catch (JSONException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  		childmsg.obj=ev;
	  		zhifubaochildhand.sendMessage(childmsg);
  		}
  	}
  	//��������
  	private void deletezhier()
  	{
  		//if(ercheckopt())
  		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<viewSwitch=��������","log.txt");
  		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ercheck="+ercheck,"log.txt");
  		{
	  		// ����Ϣ���͵����߳���
	  		zhifubaochildhand=zhifubaohttp.obtainHandler();
	  		Message childmsg=zhifubaochildhand.obtainMessage();
	  		childmsg.what=Zhifubaohttp.SETDELETECHILD;
	  		JSONObject ev=null;
	  		try {
	  			ev=new JSONObject();
	  			ev.put("out_trade_no", out_trade_no);		
	  			//ev.put("out_trade_no", "000120150301092857698");	
	  			Log.i("EV_JNI","Send0.1="+ev.toString());
	  		} catch (JSONException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  		childmsg.obj=ev;
	  		zhifubaochildhand.sendMessage(childmsg);
  		}
  		clearamount();
    	viewSwitch(BUSPORT, null);
  	}
    //�˿�
  	private void payoutzhier()
  	{
  		//if(ercheckopt())
  		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ercheck="+ercheck,"log.txt");
  		{
	  		// ����Ϣ���͵����߳���
	  		zhifubaochildhand=zhifubaohttp.obtainHandler();
	  		Message childmsg=zhifubaochildhand.obtainMessage();
	  		childmsg.what=Zhifubaohttp.SETPAYOUTCHILD;
	  		JSONObject ev=null;
	  		try {
	  			ev=new JSONObject();
	  			ev.put("out_trade_no", out_trade_no);		
	  			ev.put("refund_amount", String.valueOf(amount));
	  			ev.put("out_request_no", ToolClass.out_trade_no(BusPort.this));
	  			Log.i("EV_JNI","Send0.1="+ev.toString());
	  		} catch (JSONException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  		childmsg.obj=ev;
	  		zhifubaochildhand.sendMessage(childmsg);
  		}
  		dialog.dismiss();
		//������
		clearamount();						
		recLen=10;
  	}
    
    //=======================
  	//ʵ��Buszhiweiҳ����ؽӿ�
  	//=======================
    //��������ʵ��Buszhiwei�ӿ�,ת����ҳ��
  	@Override
	public void BuszhiweiFinish() {
		// TODO Auto-generated method stub
  		if(iszhiwei>0)
			deletezhiwei();
		else 
		{
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<viewSwitch=BUSPORT","log.txt");
	    	clearamount();
	    	viewSwitch(BUSPORT, null);
		}
	}
    //���ڳ�ʱ�Ľ�������
  	private void timeoutBuszhiweiFinish()
  	{
  		BuszhiweiFinish();
  	}
    //���Ͷ���
  	private void sendzhiwei()
  	{	
  		if(ercheckopt())
  		{
	      	// ����Ϣ���͵����߳���
	      	weixingchildhand=weixinghttp.obtainHandler();
	  		Message childmsg=weixingchildhand.obtainMessage();
	  		childmsg.what=Weixinghttp.SETCHILD;
	  		JSONObject ev=null;
	  		try {
	  			ev=new JSONObject();
	  			out_trade_no=ToolClass.out_trade_no(BusPort.this);
	  	        ev.put("out_trade_no", out_trade_no);
	  			ev.put("total_fee", String.valueOf(amount));
	  			Log.i("EV_JNI","Send0.1="+ev.toString());
	  		} catch (JSONException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  		childmsg.obj=ev;
	  		weixingchildhand.sendMessage(childmsg);
  		}
  	}
  	//��ѯ����
  	private void queryzhiwei()
  	{
  		if(ercheckopt())
  		{
	  		// ����Ϣ���͵����߳���
	  		weixingchildhand=weixinghttp.obtainHandler();
	  		Message childmsg=weixingchildhand.obtainMessage();
	  		childmsg.what=Weixinghttp.SETQUERYCHILD;
	  		JSONObject ev=null;
	  		try {
	  			ev=new JSONObject();
	  			ev.put("out_trade_no", out_trade_no);		
	  			//ev.put("out_trade_no", "000120150301113215800");	
	  			Log.i("EV_JNI","Send0.1="+ev.toString());
	  		} catch (JSONException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  		childmsg.obj=ev;
	  		weixingchildhand.sendMessage(childmsg);
  		}
  	}
  	//�˿��
  	private void payoutzhiwei()
  	{
  		//if(ercheckopt())
  		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ercheck="+ercheck,"log.txt");
  		{
	  		// ����Ϣ���͵����߳���
	  		weixingchildhand=weixinghttp.obtainHandler();
	  		Message childmsg=weixingchildhand.obtainMessage();
	  		childmsg.what=Weixinghttp.SETPAYOUTCHILD;
	  		JSONObject ev=null;
	  		try {
	  			ev=new JSONObject();
	  			ev.put("out_trade_no", out_trade_no);		
	  			ev.put("total_fee", String.valueOf(amount));
	  			ev.put("refund_fee", String.valueOf(amount));
	  			ev.put("out_refund_no", ToolClass.out_trade_no(BusPort.this));
	  			Log.i("EV_JNI","Send0.1="+ev.toString());
	  		} catch (JSONException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  		childmsg.obj=ev;
	  		weixingchildhand.sendMessage(childmsg);
  		}
  		dialog.dismiss();
		//������
		clearamount();						
		recLen=10;
  	}
  	
	//��������
  	private void deletezhiwei()
  	{
  		//if(ercheckopt())
  		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<viewSwitch=��������","log.txt");
  		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ercheck="+ercheck,"log.txt");
  		{
	  		// ����Ϣ���͵����߳���
	  		weixingchildhand=weixinghttp.obtainHandler();
	  		Message childmsg=weixingchildhand.obtainMessage();
	  		childmsg.what=Weixinghttp.SETDELETECHILD;
	  		JSONObject ev=null;
	  		try {
	  			ev=new JSONObject();
	  			ev.put("out_trade_no", out_trade_no);		
	  			//ev.put("out_trade_no", "000120150301092857698");	
	  			Log.i("EV_JNI","Send0.1="+ev.toString());
	  		} catch (JSONException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  		childmsg.obj=ev;
	  		weixingchildhand.sendMessage(childmsg);
  		}
  		clearamount();
    	viewSwitch(BUSPORT, null);
  	}
    
    //=======================
  	//ʵ��Bushuoҳ����ؽӿ�
  	//=======================
    //��������ʵ��Bushuo�ӿ�,����
    @Override
	public void BushuoChuhuoOpt(int cabinetvar, int huodaoNo,int cabinetTypevar) {
		// TODO Auto-generated method stub
    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<busport��ƷcabID="+cabinetvar+"huoID="+huodaoNo+"cabType="+cabinetTypevar,"log.txt");
		dialog= ProgressDialog.show(BusPort.this,"���ڳ�����","���Ժ�...");
		ToolClass.Log(ToolClass.INFO,"EV_JNI",
		    	"[APPsend>>]cabinet="+String.valueOf(cabinetvar)
		    	+" column="+huodaoNo		    	
		    	,"log.txt");
		Intent intent = new Intent();
		//4.����ָ��㲥��COMService
		intent.putExtra("EVWhat", COMService.EV_CHUHUOCHILD);	
		intent.putExtra("cabinet", cabinetvar);	
		intent.putExtra("column", huodaoNo);	
		intent.setAction("android.intent.action.comsend");//action���������ͬ
		comBroadreceiver.sendBroadcast(intent);
    }
    
    //��������ʵ��Bushuo�ӿ�,��������ҳ��
    @Override
	public void BushuoFinish(int status) {
    	// TODO Auto-generated method stub
    	switch(OrderDetail.getPayType())
    	{
    		//�ֽ�ҳ��
    		case 0:
    			//viewSwitch(BUSZHIAMOUNT, null);
    			//1.
  				//�����ɹ�,��Ǯ
				if(status==1)
				{
					//��Ǯ
		  	    	//EVprotocolAPI.EV_mdbCost(ToolClass.getCom_id(),ToolClass.MoneySend(amount));
					Intent intent=new Intent();
			    	intent.putExtra("EVWhat", COMService.EV_MDB_COST);	
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
  				
  			    //��������˱�
  				if(money>0)
  				{
  					dialog= ProgressDialog.show(BusPort.this,"�����˱���,���"+money,"���Ժ�...");
  					new Handler().postDelayed(new Runnable() 
					{
			            @Override
			            public void run() 
			            {            	
			            	//�˱�
			    	    	//EVprotocolAPI.EV_mdbPayback(ToolClass.getCom_id(),1,1);
		  					Intent intent=new Intent();
		  			    	intent.putExtra("EVWhat", COMService.EV_MDB_PAYBACK);	
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
			    	OrderDetail.addLog(BusPort.this);
			    	clearamount();
		  			new Handler().postDelayed(new Runnable() 
					{
			            @Override
			            public void run() 
			            {            	
			            	//�ر�ֽ��Ӳ����
				  	    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,0); 
					    	BillEnable(0);					    	
			            }

					}, 500); 
		  	    	recLen=10;
  				}
    			break;
    		//֧����ҳ��	
    		case 3:
    			//�����ɹ�,��������
				if(status==1)
				{
					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ali���˿�","log.txt");
					OrderDetail.addLog(BusPort.this);					
					clearamount();
					recLen=10;
				}
				//����ʧ��,��Ǯ
				else
				{	
					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ali�˿�amount="+amount,"log.txt");
					dialog= ProgressDialog.show(BusPort.this,"�����˿���","���Ժ�...");
					payoutzhier();//�˿����
					OrderDetail.setRealStatus(1);//��¼�˱ҳɹ�
					OrderDetail.setRealCard(amount);//��¼�˱ҽ��
					OrderDetail.addLog(BusPort.this);					
				}
    			break;
    		//΢��ҳ��	
    		case 4:
    			//�����ɹ�,��������
				if(status==1)
				{
					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<wei���˿�","log.txt");
					OrderDetail.addLog(BusPort.this);					
					clearamount();
					recLen=10;
				}
				//����ʧ��,��Ǯ
				else
				{	
					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<wei�˿�amount="+amount,"log.txt");
					dialog= ProgressDialog.show(BusPort.this,"�����˿���","���Ժ�...");
					payoutzhiwei();//�˿����
					OrderDetail.setRealStatus(1);//��¼�˱ҳɹ�
					OrderDetail.setRealCard(amount);//��¼�˱ҽ��
					OrderDetail.addLog(BusPort.this);					
				}
    			break;	
    		//ȡ����ҳ��		
    		case -1:
    			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ȡ����ҳ��","log.txt");
				OrderDetail.addLog(BusPort.this);					
				clearamount();
				recLen=10;
    			break;
    	}
    	
	}
    
    
    //=======================
  	//ʵ����ػ��������ӿ�
  	//=======================
    //��������
    private void tochuhuo()
    {        
    	ischuhuo=true;
//    	Intent intent = null;// ����Intent����                
//    	intent = new Intent(BusZhiAmount.this, BusHuo.class);// ʹ��Accountflag���ڳ�ʼ��Intent
////    	intent.putExtra("out_trade_no", out_trade_no);
////    	intent.putExtra("proID", proID);
////    	intent.putExtra("productID", productID);
////    	intent.putExtra("proType", proType);
////    	intent.putExtra("cabID", cabID);
////    	intent.putExtra("huoID", huoID);
////    	intent.putExtra("prosales", prosales);
////    	intent.putExtra("count", count);
////    	intent.putExtra("reamin_amount", reamin_amount);
////    	intent.putExtra("zhifutype", zhifutype);    	
//    	startActivityForResult(intent, REQUEST_CODE);// ��Accountflag
    	OrderDetail.setOrdereID(out_trade_no);
    	OrderDetail.setPayType(Integer.parseInt(zhifutype));
    	if(gotoswitch==BUSZHIAMOUNT)
    	{    	
        	BillEnable(0);	
    	}
    	else if(gotoswitch==BUSZHIER)
    	{
        	OrderDetail.setSmallCard(amount);
    	}
    	else if(gotoswitch==BUSZHIWEI)
    	{
    		OrderDetail.setSmallCard(amount);
    	}
    	//��ʱ
	    new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {   
            	viewSwitch(BUSHUO, null);
            }

		}, 2500);    	
    }
    //�����
  	public void clearamount()
  	{
  		//�ֽ�ҳ��
  	    con = 0;
  	    queryLen = 0; 
  	    iszhienable=0;//1���ʹ�ָ��,0��û���ʹ�ָ��
  	    isempcoin=false;//false��δ���͹�ֽ����ָ�true��Ϊȱ�ң��Ѿ����͹�ֽ����ָ��
  	    dispenser=0;//0��,1hopper,2mdb
  		billmoney=0;coinmoney=0;money=0;//Ͷ�ҽ��
  		amount=0;//��Ʒ��Ҫ֧�����
  		iszhiamount=0;//1�ɹ�Ͷ��Ǯ,0û�гɹ�Ͷ��Ǯ
  		RealNote=0;
  		RealCoin=0;
  		RealAmount=0;//�˱ҽ��
  		ischuhuo=false;//true�Ѿ��������ˣ������ϱ���־
  		//֧����ҳ��
  		iszhier=0;//1�ɹ������˶�ά��,0û�гɹ����ɶ�ά��
  	    //΢��ҳ��
  		iszhiwei=0;//1�ɹ������˶�ά��,0û�гɹ����ɶ�ά��
  		ercheck=false;//true���ڶ�ά����̲߳����У����Ժ�falseû�ж�ά����̲߳���
  	}
  	
  	//�ж��Ƿ��ڶ�ά����̲߳�����,true��ʾ���Բ�����,false���ܲ���
  	private boolean ercheckopt()
  	{
  		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ercheck="+ercheck,"log.txt");
  		if(ercheck==false)
  		{
  			ercheck=true;
  			return true;
  		}
  		else
  		{
  			return false;
		}
  	}
	
	//ȫ�������л�view�ĺ���
	public void viewSwitch(int buslevel, Map<String, String> str)
	{
		recLen=SPLASH_DISPLAY_LENGHT;
		gotoswitch=buslevel;
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
			case BUSZHIAMOUNT://�ֽ�֧��
				isbus=false;
				//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,1);
				BillEnable(1);
				amount=OrderDetail.getShouldPay()*OrderDetail.getShouldNo(); 
				zhifutype="0";
				out_trade_no=ToolClass.out_trade_no(BusPort.this);// ����InaccountDAO����;
				//�л�ҳ��
				if (buszhiamountFragment == null) {
					buszhiamountFragment = new BuszhiamountFragment();
	            }
	            // ʹ�õ�ǰFragment�Ĳ������id_content�Ŀؼ�
	            transaction.replace(R.id.id_content, buszhiamountFragment);
				break;	
			case BUSZHIER://֧����֧��
				isbus=false;
				zhifutype="3";
				amount=OrderDetail.getShouldPay()*OrderDetail.getShouldNo();
				if (buszhierFragment == null) {
					buszhierFragment = new BuszhierFragment();
	            }
				// ʹ�õ�ǰFragment�Ĳ������id_content�Ŀؼ�
	            transaction.replace(R.id.id_content, buszhierFragment);
				//�½�һ���̲߳�����
				zhifubaothread.execute(zhifubaohttp);
				//��ʱ
			    new Handler().postDelayed(new Runnable() 
				{
		            @Override
		            public void run() 
		            {   
			            //���Ͷ���
		        		sendzhier();
		            }

				}, 1500);		            
				break;
			case BUSZHIWEI://΢��֧��
				isbus=false;
				zhifutype="4";
				amount=OrderDetail.getShouldPay()*OrderDetail.getShouldNo();
				if (buszhiweiFragment == null) {
					buszhiweiFragment = new BuszhiweiFragment();
	            }
	            // ʹ�õ�ǰFragment�Ĳ������id_content�Ŀؼ�
	            transaction.replace(R.id.id_content, buszhiweiFragment);
	            //�½�һ���̲߳�����
	            weixingthread.execute(weixinghttp);
				//��ʱ
			    new Handler().postDelayed(new Runnable() 
				{
		            @Override
		            public void run() 
		            {   
		            	//���Ͷ���
		        		sendzhiwei();
		            }

				}, 1500);	           
				break;	
			case BUSHUO://����ҳ��	
				isbus=false;
				if (bushuoFragment == null) {
					bushuoFragment = new BushuoFragment();
	            }
	            // ʹ�õ�ǰFragment�Ĳ������id_content�Ŀؼ�
	            transaction.replace(R.id.id_content, bushuoFragment);
				break;
		}
		// transaction.addToBackStack();
        // �����ύ
        transaction.commit();
	}
		
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{		
    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<businessJNI","log.txt");		
	}
	//2.����COMReceiver�Ľ������㲥���������շ�����ͬ��������
	public class COMReceiver extends BroadcastReceiver 
	{

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
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMBusPort �ۺ��豸����="+Set,"com.txt");
				int EV_TYPE=Set.get("EV_TYPE");
				switch(EV_TYPE)
				{
					case COMService.EV_MDB_ENABLE:	
						//��
						if((Integer)Set.get("opt")==1)
						{
							//��ʧ��,�ȴ����´�
							if( ((Integer)Set.get("bill_result")==0)&&((Integer)Set.get("coin_result")==0) )
							{
								listterner.BusportTsxx("��ʾ��Ϣ������"+con);
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
						}												
						break;
					case COMService.EV_MDB_B_INFO:
						break;
					case COMService.EV_MDB_C_INFO:
						dispenser=(Integer)Set.get("dispenser");						
					    //Heart����
					    Intent intent4=new Intent();
				    	intent4.putExtra("EVWhat", COMService.EV_MDB_HEART);
						intent4.setAction("android.intent.action.comsend");//action���������ͬ
						comBroadreceiver.sendBroadcast(intent4);
						iszhienable=1;
						break;	
					case COMService.EV_MDB_HEART://������ѯ
						Map<String,Object> obj=new LinkedHashMap<String, Object>();
						obj.putAll(Set);
						//ֽ����ҳ��
						if(iszhienable==1)
						{
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
							listterner.BusportTsxx("��ʾ��Ϣ��"+bill_enable+coin_enable+hopperString);
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
						  		recLen = 180;
						  		listterner.BusportTbje(String.valueOf(money));
						  		OrderDetail.setSmallNote(billmoney);
						  		OrderDetail.setSmallConi(coinmoney);
						  		OrderDetail.setSmallAmount(money);
						  		if(money>=amount)
						  		{
						  			iszhienable=2;
						  			tochuhuo();
						  		}
						  	}
						}						
						break;
					case COMService.EV_MDB_PAYOUT://����			
						break;	
					case COMService.EV_MDB_PAYBACK://�˱�
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
				    		OrderDetail.addLog(BusPort.this);
				    	}
				    	else
				    	{
				    		OrderDetail.cleardata();
						}
				    	dialog.dismiss();
						//������
				    	clearamount();
			  			//�ر�ֽ��Ӳ����
			  	    	//EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,0); 
				    	BillEnable(0);
			  	    	if(gotoswitch==BUSZHIAMOUNT)
			  	    	{
			  	    		viewSwitch(BUSPORT, null);	
			  	    	}
			  	    	else
			  	    	{
			  	    		recLen=10;
			  	    	}
						break; 
					//�ǳ�������	
					case EVprotocol.EV_BENTO_OPEN://���ӹ���� 					
					case EVprotocol.EV_COLUMN_OPEN://�������
						status=Set.get("result");//�������
						dialog.dismiss();
						listterner.BusportChjg(status);
					default:break;	
				}
				break;				
			}
		}

	}
	//1��,0�رչر�ֽ��Ӳ����   
  	private void BillEnable(int opt)
  	{  		 	
		Intent intent=new Intent();
		intent.putExtra("EVWhat", COMService.EV_MDB_ENABLE);	
		intent.putExtra("bill", 1);	
		intent.putExtra("coin", 1);	
		intent.putExtra("opt", opt);	
		intent.setAction("android.intent.action.comsend");//action���������ͬ
		comBroadreceiver.sendBroadcast(intent);	
  	}
	@Override
	protected void onDestroy() {
		timer.shutdown(); 
		//=============
		//Server�������
		//=============
		//5.���ע�������
		localBroadreceiver.unregisterReceiver(receiver);
		//=============
		//COM�������
		//=============
		//5.���ע�������
		comBroadreceiver.unregisterReceiver(comreceiver);	
		//�˳�ʱ������intent
        Intent intent=new Intent();
        setResult(MaintainActivity.RESULT_CANCELED,intent);
		super.onDestroy();		
	}

	
	

	

	

	

	

	

	

	

	

}
