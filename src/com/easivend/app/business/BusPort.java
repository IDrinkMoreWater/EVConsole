package com.easivend.app.business;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.bean.ComBean;
import com.easivend.app.maintain.CahslessTest;
import com.easivend.app.maintain.MaintainActivity;
import com.easivend.app.maintain.PrintTest;
import com.easivend.app.maintain.MaintainActivity.EVServerReceiver;
import com.easivend.common.OrderDetail;
import com.easivend.common.SerializableMap;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_productDAO;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.evprotocol.COMThread;
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
import com.easivend.fragment.BuszhiposFragment;
import com.easivend.fragment.BuszhiposFragment.BuszhiposFragInteraction;
import com.easivend.fragment.BuszhiweiFragment;
import com.easivend.fragment.BuszhiweiFragment.BuszhiweiFragInteraction;
import com.easivend.fragment.MoviewlandFragment.MovieFragInteraction;
import com.easivend.http.EVServerhttp;
import com.easivend.http.Weixinghttp;
import com.easivend.http.Zhifubaohttp;
import com.easivend.model.Tb_vmc_product;
import com.easivend.model.Tb_vmc_system_parameter;
import com.easivend.view.COMService;
import com.easivend.view.EVServerService;
import com.easivend.view.PassWord;
import com.example.evconsole.R;
import com.example.printdemo.MyFunc;
import com.example.printdemo.SerialHelper;
import com.landfone.common.utils.IUserCallback;
import com.landfoneapi.mispos.Display;
import com.landfoneapi.mispos.DisplayType;
import com.landfoneapi.mispos.ErrCode;
import com.landfoneapi.mispos.LfMISPOSApi;
import com.landfoneapi.protocol.pkg.REPLY;
import com.landfoneapi.protocol.pkg._04_GetRecordReply;
import com.printsdk.cmd.PrintCmd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
//Buszhiposҳ��ӿ�
BuszhiposFragInteraction,
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
	private BuszhiposFragment buszhiposFragment;
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
	public static final int BUSZHIPOS=9;//POS֧��ҳ��
	public static final int BUSHUO=8;//����ҳ��
	private int gotoswitch=0;//��ǰ��ת���ĸ�ҳ��
	private int con=0;
	//���ȶԻ���
	ProgressDialog dialog= null;
	private String zhifutype = "0";//֧����ʽ0=�ֽ�1=pos 3=��ά��4=΢֧�� -1=ȡ����,5��������
	private String out_trade_no=null;
	//Server�������
	LocalBroadcastManager localBroadreceiver;
	EVServerReceiver receiver;
	//=================
	//==�ֽ�֧��ҳ�����
	//=================
	private int queryLen = 0; 
	private int billdev=1;//�Ƿ���Ҫ��ֽ����,1��Ҫ
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
    private int ispayoutopt=0;//1���ڽ����˱Ҳ���,0δ�����˱Ҳ���
    //=================
  	//==΢��֧��ҳ�����
  	//=================
    //�߳̽���΢�Ŷ�ά�����
    private ExecutorService weixingthread=null;
    private Handler weixingmainhand=null,weixingchildhand=null;   
    Weixinghttp weixinghttp=null;
    private int iszhiwei=0;//1�ɹ������˶�ά��,0û�гɹ����ɶ�ά�룬2����Ͷ���Ѿ�����
    //=================
  	//==pos֧��ҳ�����
  	//=================
    private LfMISPOSApi mMyApi = new LfMISPOSApi();
    private Handler posmainhand=null;
    private int iszhipos=0;//1�ɹ������˿ۿ�����,0û�з��ͳɹ��ۿ�����2ˢ���ۿ��Ѿ���ɲ��ҽ���㹻
    //�˿����
   	private String rfd_card_no = "";
   	private String rfd_spec_tmp_serial = "";
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
    //=================
    //��ӡ�����
    //=================
  	boolean istitle1,istitle2,isno,issum,isthank,iser,isdate;
	int serialno=0;
	String title1str,title2str,thankstr,erstr;
	SerialControl ComA;                  // ���ڿ���
	static DispQueueThread DispQueue;    // ˢ����ʾ�߳�
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // ���ʻ���־ʱ���ʽ��
	private Handler printmainhand=null;
	private int isPrinter=0;//0û�����ô�ӡ����1�����ô�ӡ����2��ӡ���Լ�ɹ������Դ�ӡ
    
    //=========================
    //activity��fragment�ص����
    //=========================
    /**
     * ����������fragment������,���þ�̬����
     */
    public static BusPortFragInteraction listterner;
    public static BusPortMovieFragInteraction listternermovie;
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
        //�ֽ�ҳ��
        void BusportTsxx(String str);      //��ʾ��Ϣ
        void BusportTbje(String str);      //Ͷ�ҽ��
        //����ҳ��
        void BusportChjg(int sta);      //�������
        //���ֽ�ҳ��
        void BusportSend(String str);      //��ά��
    }
    public interface BusPortMovieFragInteraction
    {
    	//��ʾ������ʾ��Ϣ
        void BusportMovie(int infotype);      //��ʾ������ʾ��Ϣ
        //ˢ�¹��ҳ��
        void BusportAds();      //ˢ�¹���б�
    }
        
    /**
     * ����������fragment������,
     * �����ġ���Fragment�����ص�activity��ʱ������ע��ص���Ϣ
     * @param activity
     */
  	public static void setCallBack(BusPortFragInteraction call){ 
  		listterner = call;
      }
  	public static void setMovieCallBack(BusPortMovieFragInteraction call){ 
  		listternermovie = call;
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
		        	
		        	//=================
	        		//==pos֧��ҳ�����
	        		//=================
		        	else if(gotoswitch==BUSZHIPOS)
		        	{
			        	//���Ͳ�ѯ����ָ��
	                    if(iszhipos==1)
	                    {		                    
	                    }
	                    //���Ͷ�������ָ��
	                    else if(iszhipos==0)
	                    {		                    
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
						if(ispayoutopt==1)
						{
							//��¼��־�˱����
							OrderDetail.setRealStatus(3);//��¼�˱�ʧ��
							OrderDetail.setRealCard(0);//��¼�˱ҽ��
							OrderDetail.setDebtAmount(amount);//Ƿ����
							OrderDetail.addLog(BusPort.this);
							ispayoutopt=0;
							//��������ҳ��
							listterner.BusportTsxx("���׽��:�˿�ʧ��");
							dialog.dismiss();
							zhierDestroy(1);
						}
						break;		
					case Zhifubaohttp.SETPAYOUTMAIN://���߳̽������߳���Ϣ
						if(ispayoutopt==1)
						{
							//��¼��־�˱����
							OrderDetail.setRealStatus(1);//��¼�˱ҳɹ�
							OrderDetail.setRealCard(amount);//��¼�˱ҽ��
							OrderDetail.addLog(BusPort.this);
							ispayoutopt=0;
							//��������ҳ��
							listterner.BusportTsxx("���׽��:�˿�ɹ�");
							dialog.dismiss();
							zhierDestroy(1);
						}
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
						//listterner.BusportTsxx("���׽��:"+msg.obj.toString());
						if(ispayoutopt==1)
						{
							//��¼��־�˱����
							OrderDetail.setRealStatus(3);//��¼�˱�ʧ��
							OrderDetail.setRealCard(0);//��¼�˱ҽ��
							OrderDetail.setDebtAmount(amount);//Ƿ����
							OrderDetail.addLog(BusPort.this);
							ispayoutopt=0;
							//��������ҳ��
							listterner.BusportTsxx("���׽��:�˿�ʧ��");
							dialog.dismiss();
							zhierDestroy(1);
						}
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
						if(ispayoutopt==1)
						{
							//��¼��־�˱����
							OrderDetail.setRealStatus(3);//��¼�˱�ʧ��
							OrderDetail.setRealCard(0);//��¼�˱ҽ��
							OrderDetail.setDebtAmount(amount);//Ƿ����
							OrderDetail.addLog(BusPort.this);
							ispayoutopt=0;
							//��������ҳ��
							listterner.BusportTsxx("���׽��:�˿�ʧ��");
							dialog.dismiss();
							//������
							clearamount();						
							recLen=10;
						}
						break;	
					case Weixinghttp.SETPAYOUTMAIN://���߳̽������߳���Ϣ
						if(ispayoutopt==1)
						{
							//��¼��־�˱����
							OrderDetail.setRealStatus(1);//��¼�˱ҳɹ�
							OrderDetail.setRealCard(amount);//��¼�˱ҽ��
							OrderDetail.addLog(BusPort.this);
							ispayoutopt=0;
							//��������ҳ��
							listterner.BusportTsxx("���׽��:�˿�ɹ�");
							dialog.dismiss();
							//������
							clearamount();						
							recLen=10;
						}
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
						//listterner.BusportTsxx("���׽��:"+msg.obj.toString());
						if(ispayoutopt==1)
						{
							//��¼��־�˱����
							OrderDetail.setRealStatus(3);//��¼�˱�ʧ��
							OrderDetail.setRealCard(0);//��¼�˱ҽ��
							OrderDetail.setDebtAmount(amount);//Ƿ����
							OrderDetail.addLog(BusPort.this);
							ispayoutopt=0;
							//��������ҳ��
							listterner.BusportTsxx("���׽��:�˿�ʧ��");
							dialog.dismiss();
							//������
							clearamount();						
							recLen=10;
						}
						break;		
				}				
			}
			
		};
		//�����û��Լ��������
		weixinghttp=new Weixinghttp(weixingmainhand);
		weixingthread=Executors.newCachedThreadPool();
		
		
		//***********************
		//����pos����
		//***********************
		posmainhand=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub				
				switch (msg.what) 
				{
					case CahslessTest.OPENSUCCESS:
						break;
					case CahslessTest.OPENFAIL:	
						break;
					case CahslessTest.CLOSESUCCESS:
						break;
					case CahslessTest.CLOSEFAIL:	
						break;
					case CahslessTest.COSTSUCCESS:
						listterner.BusportTsxx("��ʾ��Ϣ���������");
						iszhipos=2;
						//��ʱ
					    new Handler().postDelayed(new Runnable() 
						{
				            @Override
				            public void run() 
				            {         
				            	//��������ȡ������Ϣ
								mMyApi.pos_getrecord("000000000000000", "00000000","000000", mIUserCallback);
							}

						}, 300);
						break;
					case CahslessTest.COSTFAIL:	
						listterner.BusportTsxx("��ʾ��Ϣ���ۿ�ʧ��");
						iszhipos=0;
						break;
					case CahslessTest.QUERYSUCCESS:
					case CahslessTest.QUERYFAIL:	
						//listterner.BusportTsxx("������Ϣ��"+SpecInfoField);
						tochuhuo();	
						break;
					case CahslessTest.DELETESUCCESS:
					case CahslessTest.DELETEFAIL:	
						//��ʱ
					    new Handler().postDelayed(new Runnable() 
						{
				            @Override
				            public void run() 
				            {         
				            	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<viewSwitch=BUSPORT","log.txt");
								ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �رն�����","com.txt");
						    	mMyApi.pos_release();
								clearamount();
						    	viewSwitch(BUSPORT, null);
							}

						}, 300);						
						break;						
					case CahslessTest.PAYOUTSUCCESS:
						if(ispayoutopt==1)
						{
							//��¼��־�˱����
							OrderDetail.setRealStatus(1);//��¼�˱ҳɹ�
							OrderDetail.setRealCard(amount);//��¼�˱ҽ��
							OrderDetail.addLog(BusPort.this);
							ispayoutopt=0;
							//��������ҳ��
							listterner.BusportTsxx("���׽��:�˿�ɹ�");
							//��ʱ
						    new Handler().postDelayed(new Runnable() 
							{
					            @Override
					            public void run() 
					            {         
					            	ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �رն�����","com.txt");
					            	dialog.dismiss();
					            	mMyApi.pos_release();
									//������
									clearamount();						
									recLen=10;
								}

							}, 300);							
						}
						break;
					case CahslessTest.PAYOUTFAIL:	
						if(ispayoutopt==1)
						{
							//��¼��־�˱����
							OrderDetail.setRealStatus(3);//��¼�˱�ʧ��
							OrderDetail.setRealCard(0);//��¼�˱ҽ��
							OrderDetail.setDebtAmount(amount);//Ƿ����
							OrderDetail.addLog(BusPort.this);
							ispayoutopt=0;
							//��������ҳ��
							listterner.BusportTsxx("���׽��:�˿�ʧ��");
							//��ʱ
						    new Handler().postDelayed(new Runnable() 
							{
					            @Override
					            public void run() 
					            {         
					            	ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �رն�����","com.txt");
					            	dialog.dismiss();
					            	mMyApi.pos_release();
									//������
									clearamount();						
									recLen=10;
								}

							}, 300);							
						}
						break;		
				}
			}
		};	
		//=================
	    //��ӡ�����
	    //=================
		printmainhand=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub				
				switch (msg.what) 
				{
					case PrintTest.NORMAL:
						ToolClass.Log(ToolClass.INFO,"EV_COM","��ӡ������","com.txt");
						if(isPrinter==1)
							isPrinter=2;
						break;
					case PrintTest.NOPOWER:
						ToolClass.Log(ToolClass.INFO,"EV_COM","��ӡ��δ���ӻ�δ�ϵ�","com.txt");
						break;
					case PrintTest.NOMATCH:
						ToolClass.Log(ToolClass.INFO,"EV_COM","��ӡ���쳣[��ӡ���͵��ÿⲻƥ��]","com.txt");
						if(isPrinter==1)
							isPrinter=2;
						break;
					case PrintTest.HEADOPEN:	
						ToolClass.Log(ToolClass.INFO,"EV_COM","��ӡ����ӡ��ͷ��","com.txt");
						break;
					case PrintTest.CUTTERERR:
						ToolClass.Log(ToolClass.INFO,"EV_COM","��ӡ���е�δ��λ","com.txt");
						break;
					case PrintTest.HEADHEAT:
						ToolClass.Log(ToolClass.INFO,"EV_COM","��ӡ��ͷ����","com.txt");
						break;
					case PrintTest.BLACKMARKERR:
						ToolClass.Log(ToolClass.INFO,"EV_COM","��ӡ���ڱ����","com.txt");
						break;
					case PrintTest.PAPEREXH:	
						ToolClass.Log(ToolClass.INFO,"EV_COM","��ӡ��ֽ��","com.txt");
						break;
					case PrintTest.PAPERWILLEXH://���Ҳ���Ե�����״̬ʹ��	
						ToolClass.Log(ToolClass.INFO,"EV_COM","��ӡ��ֽ����","com.txt");
						if(isPrinter==1)
							isPrinter=2;
						break;
					case PrintTest.UNKNOWERR: 
						ToolClass.Log(ToolClass.INFO,"EV_COM","��ӡ�������쳣="+msg.obj,"com.txt");
						break;
				}
				if(isPrinter==2)
				{
					isPrinter=3;
					ToolClass.Log(ToolClass.INFO,"EV_COM","��ӡƾ֤...","com.txt");
					PrintBankQueue();
				}
			}
		};
		
				
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
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<busland=�˳�����ҳ��","log.txt");
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
		if(ToolClass.checkCLIENT_STATUS_SERVICE())
		{
			viewSwitch(buslevel, str);
		}
	}
	//��������ʵ��Business�ӿ�,����ȡ����
	@Override
	public void quhuoBusiness(String PICKUP_CODE)
	{
		if(ToolClass.checkCLIENT_STATUS_SERVICE())
		{
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<portȡ����="+PICKUP_CODE,"log.txt");
			Intent intent2=new Intent(); 
			intent2.putExtra("EVWhat", EVServerhttp.SETPICKUPCHILD);
			intent2.putExtra("PICKUP_CODE", PICKUP_CODE);
			intent2.setAction("android.intent.action.vmserversend");//action���������ͬ
			localBroadreceiver.sendBroadcast(intent2);
		}
	}
	//��������ʵ��Business�ӿ�,������ʾ��Ϣ
	@Override
	public void tishiInfo(int infotype)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<port��ʾ����="+infotype,"log.txt");
		listternermovie.BusportMovie(infotype);
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
				listternermovie.BusportAds();
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
		//�������Ͷ���Ѿ����������Թ����򲻽����˱Ҳ���
		if(iszhienable==2)
		{
			ToolClass.Log(ToolClass.INFO,"EV_COM","COMBusPort �˱Ұ�ť��Ч","com.txt");
		}
		else if(iszhiamount==1)
  		{
  			dialog= ProgressDialog.show(BusPort.this,"�����˱���","���Ժ�...");
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
  			amountDestroy(0);
		} 	    
	    
	}
	
	//�ر�ҳ��:type=1��ʱ10s�ر�,0�����ر�
	private void amountDestroy(int type)
	{
		//��ʱ�ر�
		if(type==1)
		{
			//������
	    	clearamount();
  			//�ر�ֽ��Ӳ����
  	    	BillEnable(0);
  	    	recLen=10;
		}
		//�����ر�
		else
		{
			//�ر�ֽ��Ӳ����
	    	BillEnable(0);
			clearamount();
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
    	//�������ɨ���Ѿ����������Թ����򲻽����˿����
    	if(iszhier==2)
    	{
    		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<zhier�˱Ұ�ť��Ч","log.txt");
    	}
    	else if(iszhier==1)
			deletezhier();
		else 
		{
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<viewSwitch=BUSPORT","log.txt");
			zhierDestroy(0);
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
  		zhierDestroy(0);
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
  	}
  	
    //�ر�ҳ��:type=1��ʱ10s�ر�,0�����ر�
  	private void zhierDestroy(int type)
  	{
  		//��ʱ�ر�
  		if(type==1)
  		{
  			clearamount();
			recLen=10;			
  		}
  		//�����ر�
  		else
  		{
  			clearamount();
	    	viewSwitch(BUSPORT, null);
  		}
  	}
    
    //=======================
  	//ʵ��Buszhiweiҳ����ؽӿ�
  	//=======================
    //��������ʵ��Buszhiwei�ӿ�,ת����ҳ��
  	@Override
	public void BuszhiweiFinish() {
		// TODO Auto-generated method stub
  		//�������ɨ���Ѿ����������Թ����򲻽����˿����
    	if(iszhiwei==2)
    	{
    		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<zhiwei�˱Ұ�ť��Ч","log.txt");
    	}
    	else if(iszhiwei==1)
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
  	//ʵ��Buszhiposҳ����ؽӿ�
  	//=======================
    //��������ʵ��Buszhipos�ӿ�,ת����ҳ��
  	@Override
	public void BuszhiposFinish() {
		// TODO Auto-generated method stub
  		//�������ɨ���Ѿ����������Թ����򲻽����˿����
    	if(iszhipos==2)
    	{
    		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<zhipos�˱Ұ�ť��Ч","log.txt");
    	}
    	else if(iszhipos==1)
			deletezhipos();
		else 
		{
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<viewSwitch=BUSPORT","log.txt");
			ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �رն�����","com.txt");
	    	mMyApi.pos_release();
			clearamount();
	    	viewSwitch(BUSPORT, null);
		}
	}
    //���ڳ�ʱ�Ľ�������
  	private void timeoutBuszhiposFinish()
  	{
  		BuszhiposFinish();
  	}
  	
    //��������
  	private void deletezhipos()
  	{
  		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<viewSwitch=��������","log.txt");
  		ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ����������ˢ��ǰ��..","com.txt");
    	mMyApi.pos_cancel();
  	}
  	
    //�˿��
  	private void payoutzhipos()
  	{
  		ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �������˿�="+amount,"com.txt");
    	mMyApi.pos_refund(rfd_card_no,ToolClass.MoneySend(amount),rfd_spec_tmp_serial, mIUserCallback);
  	}
  
  	//�ӿڷ���
  	private IUserCallback mIUserCallback = new IUserCallback(){
  		@Override
  		public void onResult(REPLY rst) 
  		{
  			if(rst!=null) 
  			{
  				Message childmsg=posmainhand.obtainMessage();
  				//info(rst.op + ":" + rst.code + "," + rst.code_info);
  				//��������ʶ����LfMISPOSApi�¡�OP_����ͷ�ľ�̬�������磺LfMISPOSApi.OP_INIT��LfMISPOSApi.OP_PURCHASE�ȵ�
  				//�򿪴���
  				if(rst.op.equals(LfMISPOSApi.OP_INIT))
  				{
  					//�����������Ϣ��code��code_info�ķ���/˵������com.landfoneapi.mispos.ErrCode
  					if(rst.code.equals(ErrCode._00.getCode())){//����00������ɹ�
  						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �򿪳ɹ�"+ToolClass.getExtracom(),"com.txt");
  						childmsg.what=CahslessTest.OPENSUCCESS;
  						childmsg.obj="�򿪳ɹ�"+ToolClass.getExtracom();
  					}else{
  						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ��ʧ��"+ToolClass.getExtracom()+",code:"+rst.code+",info:"+rst.code_info,"com.txt");						
  						childmsg.what=CahslessTest.OPENFAIL;
  						childmsg.obj="��ʧ��"+ToolClass.getExtracom()+",code:"+rst.code+",info:"+rst.code_info;
  					}
  				}
  				//�رմ���
  				else if(rst.op.equals(LfMISPOSApi.OP_RELEASE))
  				{
  					if(rst.code.equals(ErrCode._00.getCode())){//����00������ɹ�
  						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �رճɹ�","com.txt");
  						childmsg.what=CahslessTest.CLOSESUCCESS;
  						childmsg.obj="�رճɹ�";
  					}else{
  						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �ر�ʧ��,code:"+rst.code+",info:"+rst.code_info,"com.txt");						
  						childmsg.what=CahslessTest.CLOSEFAIL;
  						childmsg.obj="�ر�ʧ��,code:"+rst.code+",info:"+rst.code_info;
  					}
  				}
  				//�ۿ�
  				else if(rst.op.equals(LfMISPOSApi.OP_PURCHASE))
  				{
  					if(rst.code.equals(ErrCode._00.getCode())){//����00������ɹ�
  						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �ۿ�ɹ�","com.txt");
  						childmsg.what=CahslessTest.COSTSUCCESS;
  						childmsg.obj="�ۿ�ɹ�";
  					}
  					else if(rst.code.equals(ErrCode._XY.getCode())){
  						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �����ɹ�","com.txt");
  						childmsg.what=CahslessTest.DELETESUCCESS;
  						childmsg.obj="�����ɹ�";
  					}
  					else
  					{
  						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �ۿ�ʧ��,code:"+rst.code+",info:"+rst.code_info,"com.txt");
  						childmsg.what=CahslessTest.COSTFAIL;
  						childmsg.obj="�ۿ�ʧ��,code:"+rst.code+",info:"+rst.code_info;
  					}
  				}
  				//�˿�
  				else if(rst.op.equals(LfMISPOSApi.OP_REFUND))
  				{
  					//����00������ɹ�
					if(rst.code.equals(ErrCode._00.getCode()))
					{
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �˿�ɹ�","com.txt");
						childmsg.what=CahslessTest.PAYOUTSUCCESS;
  						childmsg.obj="�˿�ɹ�";
					}else
					{
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �˿�ʧ��,code:"+rst.code+",info:"+rst.code_info,"com.txt");
						childmsg.what=CahslessTest.PAYOUTFAIL;
						childmsg.obj="�˿�ʧ��,code:"+rst.code+",info:"+rst.code_info;
					}
				}
  				//���ؽ��
  				else if(rst.op.equals(LfMISPOSApi.OP_GETRECORD))
  				{
  					//����00������ɹ�
  					if(rst.code.equals(ErrCode._00.getCode()))
  					{
  						String tmp = "����:�ض���Ϣ=";
  						tmp += "[" + ((_04_GetRecordReply) (rst)).getSpecInfoField();//�ض���Ϣ����Ա����Ҫ������
  						/*�ض���Ϣ˵��
  						+��ֵ����(19)
  						+�ն���ˮ��(6)
  						+�ն˱��(8)
  						+���κ�(6)
  						+�̻���(15)
  						+�̻�����(60)
  						+��Ա����(60)
  						+����ʱ��(6)
  						+��������(8)
  						+���׵���(14)
  						+���ѽ��(12)
  						+�˻����(12)
  						+��ʱ������ˮ�ţ�26��
  						���϶��Ƕ��������Ƕ���12λ��ǰ��0����������λ���󲹿ո�

  						* */
  						tmp += "],�̻�����=[" + ((_04_GetRecordReply) (rst)).getMer();//�̻�����
  						tmp += "],�ն˺�=[" + ((_04_GetRecordReply) (rst)).getTmn();//�ն˺�
  						tmp += "],����=[" + ((_04_GetRecordReply) (rst)).getCardNo();//����
  						tmp += "],�������κ�=[" + ((_04_GetRecordReply) (rst)).getTransacionBatchNo();//�������κ�
  						tmp += "],ԭ��������=[" + ((_04_GetRecordReply) (rst)).getTransacionVoucherNo();//ԭ��������
  						tmp += "],�������ں�ʱ��=[" + ((_04_GetRecordReply) (rst)).getTransacionDatetime();//�������ں�ʱ��
  						tmp += "],���׽��=[" + ((_04_GetRecordReply) (rst)).getTransacionAmount();//���׽��
  						tmp +="]";
  						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ��ѯ�ɹ�="+tmp,"com.txt");

  						//�˿������ȡ
						String tmp_spec = ((_04_GetRecordReply) (rst)).getSpecInfoField();
						int tmp_spec_len = tmp_spec!=null?tmp_spec.length():0;
						//����
						//rfd_amt_fen = amount;//ʹ���ϴ�ȫ����Խ���1��
						//���˿�š�
						if(tmp_spec!=null && tmp_spec_len>(2+19)){
							rfd_card_no = (((_04_GetRecordReply) (rst)).getSpecInfoField()).substring(0+2,2+19);
						}
						//����ʱ������ˮ�š�
						if(tmp_spec!=null && tmp_spec_len>26){
							rfd_spec_tmp_serial = (((_04_GetRecordReply) (rst)).getSpecInfoField()).substring((tmp_spec_len-26),tmp_spec_len);
						}else{//ʹ�ÿո�ʱ����ʾ��һ�εġ���ʱ������ˮ�š�
							rfd_spec_tmp_serial = String.format("%1$-26s","");
						}
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �˿����=���"+amount+"����="+rfd_card_no+"��ˮ��="+rfd_spec_tmp_serial,"com.txt");
  						childmsg.what=CahslessTest.QUERYSUCCESS;
  						childmsg.obj=((_04_GetRecordReply) (rst)).getSpecInfoField();
  					}
  					else
  					{
  						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ��ѯʧ��,code:"+rst.code+",info:"+rst.code_info,"com.txt");
  						childmsg.what=CahslessTest.QUERYFAIL;
  						childmsg.obj="";
  					}
  				}
  				posmainhand.sendMessage(childmsg);
  			}
  		}

  		@Override
  		public void onProcess(Display dpl) {//���̺���ʾ��Ϣ
  			if(dpl!=null) {
  				//lcd(dpl.getType() + "\n" + dpl.getMsg());

  				//����ʾ��Ϣ���͡�type��˵������com.landfoneapi.mispos.DisplayType
  				if(dpl.getType().equals(DisplayType._4.getType())){
  					ToolClass.Log(ToolClass.INFO,"EV_COM","COMAPI ͨѶ��ʾ<<"+dpl.getMsg(),"com.txt");
  				}

  			}
  		}
  	};
    //=======================
  	//ʵ��Bushuoҳ����ؽӿ�
  	//=======================
    //��������ʵ��Bushuo�ӿ�,����
    @Override
	public void BushuoChuhuoOpt(int cabinetvar, int huodaoNo,int cabinetTypevar) {
		// TODO Auto-generated method stub
    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<busport��ƷcabID="+cabinetvar+"huoID="+huodaoNo+"cabType="+cabinetTypevar,"log.txt");
    	//2.���������
		float cost=0;
		if(Integer.parseInt(zhifutype)==0)
		{
			cost=amount;
		}
    	ToolClass.Log(ToolClass.INFO,"EV_JNI",
		    	"[APPsend>>]cabinet="+String.valueOf(cabinetvar)
		    	+" column="+huodaoNo
		    	+" cost="+cost
		    	,"log.txt");
		Intent intent = new Intent();
		//4.����ָ��㲥��COMService
		intent.putExtra("EVWhat", COMService.EV_CHUHUOCHILD);	
		intent.putExtra("cabinet", cabinetvar);	
		intent.putExtra("column", huodaoNo);	
		intent.putExtra("cost", ToolClass.MoneySend(cost));
		intent.setAction("android.intent.action.comsend");//action���������ͬ
		comBroadreceiver.sendBroadcast(intent);
    }
    
    //��������ʵ��Bushuo�ӿ�,��������ҳ��
    @Override
	public void BushuoFinish(final int status) {
    	// TODO Auto-generated method stub
    	recLen=SPLASH_DISPLAY_LENGHT;
    	//=======
		//��ӡ�����
		//=======
		// ��ѯ״̬
		if(isPrinter>0)
        {
			//�����ɹ�,��ӡƾ֤
			if(status==1)
			{
				new Handler().postDelayed(new Runnable() 
				{
		            @Override
		            public void run() 
		            {	  
		            	GetPrinterStates(ComA, PrintCmd.GetStatus()); 
		            }

				}, 600);
			}
        }
		
		new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {	  
            	//=============
        		//��ӡ�����
        		//=============
        		if(isPrinter>0)
        		{        			
        			CloseComPort(ComA);// 2.1 �رմ���
        		}
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
        			    	intent.putExtra("EVWhat", EVprotocol.EV_MDB_COST);	
        					intent.putExtra("cost", ToolClass.MoneySend((float)amount));	
        					intent.setAction("android.intent.action.comsend");//action���������ͬ
        					comBroadreceiver.sendBroadcast(intent);					
        				}
        				//����ʧ��,����Ǯ
        				else
        				{	
        					payback();
        				}				
            			break;
            		//posҳ��	
            		case 1:
            			//�����ɹ�,��������
        				if(status==1)
        				{
        					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<pos���˿�","log.txt");
        					OrderDetail.addLog(BusPort.this);	
        					mMyApi.pos_release();
        					clearamount();
        					recLen=10;
        				}
        				//����ʧ��,��Ǯ
        				else
        				{	
        					ispayoutopt=1;
        					ToolClass.Log(ToolClass.INFO,"EV_COM","APP<<pos�˿�amount="+amount,"com.txt");
        					dialog= ProgressDialog.show(BusPort.this,"�����˿���","���Ժ�...");
        					payoutzhipos();//�˿����									
        				}
            			break;	
            		//֧����ҳ��	
            		case 3:
            			//�����ɹ�,��������
        				if(status==1)
        				{
        					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ali���˿�","log.txt");
        					OrderDetail.addLog(BusPort.this);					
        					zhierDestroy(1);
        				}
        				//����ʧ��,��Ǯ
        				else
        				{	
        					ispayoutopt=1;
        					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ali�˿�amount="+amount,"log.txt");					
        					dialog= ProgressDialog.show(BusPort.this,"�����˿���","���Ժ�...");
        					payoutzhier();//�˿����	
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
        					ispayoutopt=1;
        					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<wei�˿�amount="+amount,"log.txt");
        					dialog= ProgressDialog.show(BusPort.this,"�����˿���","���Ժ�...");
        					payoutzhiwei();//�˿����									
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

		}, 1600);
    	
    	
	}
    
    //������ɺ󣬽����˱�����
  	private void payback()
  	{
  		//2.����Ͷ�ҽ��
		ToolClass.Log(ToolClass.INFO,"EV_COM","APP<<�˿�money="+money,"com.txt");
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
	    	OrderDetail.addLog(BusPort.this);
	    	amountDestroy(1);
		}
  	}
  	
    //=======================
  	//ʵ�ִ�ӡ����ؽӿ�
  	//=======================
  //=================
    //��ӡ�����
    //=================
	//��ȡ��ӡ��Ϣ
    private void ReadSharedPreferencesPrinter()
    {
    	//�ļ���˽�е�
    	SharedPreferences  user = getSharedPreferences("print_info",0);
    	//��ȡ
    	//����һ
    	istitle1=user.getBoolean("istitle1",true);
    	if(istitle1==false)
    	{
    		title1str="";
    	}
    	else
    	{
    		title1str=user.getString("title1str", "�����ۻ���");
    	}
		//�����
    	istitle2=user.getBoolean("istitle2",true);
    	if(istitle2==false)
    	{
    		title2str="";
    	}
    	else
    	{
    		title2str=user.getString("title2str", "����ƾ֤");
    	}
		//�������
		isno=user.getBoolean("isno",true);
		serialno=user.getInt("serialno", 1);
		//���ͳ��
		issum=user.getBoolean("issum",true);
		//��л��ʾ
    	isthank=user.getBoolean("isthank",true);
    	if(isthank==false)
    	{
    		thankstr="";
    	}
    	else
    	{
    		thankstr=user.getString("thankstr", "ллʹ�������ۻ���,���ǽ��߳�Ϊ������!");
    	}
		//��ά��
    	iser=user.getBoolean("iser",true);
    	if(iser==false)
    	{
    		erstr="";
    	}
    	else
    	{
    		erstr=user.getString("erstr", "http://www.easivend.com.cn/");
    	}
		//��ǰʱ��
		isdate=user.getBoolean("isdate",true);		
    }
    
  
    //д����ˮ����Ϣ
    private void SaveSharedPreferencesSerialno()
    {
    	//�ļ���˽�е�
		SharedPreferences  user = getSharedPreferences("print_info",0);
		//��Ҫ�ӿڽ��б༭
		SharedPreferences.Editor edit=user.edit();
		//д��
		//�������
		edit.putInt("serialno", serialno);
		//�ύ����
		edit.commit();
    }
    
    // ------------------�򿪴���--------------------
    private void OpenComPort(SerialHelper ComPort) {
		try {
			ComPort.open();
		} catch (SecurityException e) {
			ToolClass.Log(ToolClass.ERROR,"EV_COM","û�ж�/дȨ��","com.txt");
		} catch (IOException e) {
			ToolClass.Log(ToolClass.ERROR,"EV_COM","δ֪����","com.txt");
		} catch (InvalidParameterException e) {
			ToolClass.Log(ToolClass.ERROR,"EV_COM","��������","com.txt");
		}
	}
    
    // ------------------�رմ���--------------------
 	private void CloseComPort(SerialHelper ComPort) {
 		if (ComPort != null) {
 			ComPort.stopSend();
 			ComPort.close();
 		}
 	}
 	
    // -------------------------��ѯ״̬---------------------------
 	private void GetPrinterStates(SerialHelper ComPort, byte[] sOut) {
  		try { 			
 			if (ComPort != null && ComPort.isOpen()) {
 				ComPort.send(sOut);
 				ercheck = true; 				
 				ToolClass.Log(ToolClass.INFO,"EV_COM","��ӡ��״̬��ѯ...","com.txt");
 			} else
 			{
 				ToolClass.Log(ToolClass.ERROR,"EV_COM","��ӡ������δ��","com.txt"); 				
 			}
 		} catch (Exception ex) {
 			ToolClass.Log(ToolClass.ERROR,"EV_COM","��ӡ�����ڴ��쳣="+ex.getMessage(),"com.txt"); 			
 		}
  		
 	}
 	/**
	 * ��ӡ���۵���
	 */
	private void PrintBankQueue() {
		try {
			// СƱ����
			byte[] bValue = new byte[100];
			ComA.send(PrintCmd.SetBold(0));
			ComA.send(PrintCmd.SetAlignment(1));
			ComA.send(PrintCmd.SetSizetext(1, 1));
			//����һ
			if(istitle1)
			{
				ComA.send(PrintCmd.PrintString(title1str+"\n\n", 0));
			}
			//�����
			if(istitle2)
			{
				ComA.send(PrintCmd.PrintString(title2str+"\n\n", 0));
			}
			//�������
			if(isno)
			{
				ComA.send(PrintCmd.SetBold(1));
				ComA.send(PrintCmd.PrintString(String.format("%04d", serialno++)+"\n\n", 0));
			}
			// СƱ��Ҫ����
			CleanPrinter(); // �����棬ȱʡģʽ
			ComA.send(PrintCmd.PrintString(OrderDetail.getProID()+"  ����"+OrderDetail.getShouldPay()+"Ԫ\n", 0));
			ComA.send(PrintCmd.PrintString("_________________________________________\n\n", 0));
			//���ͳ��
			if(issum)
			{
				ComA.send(PrintCmd.PrintString("�ܼ�:"+OrderDetail.getShouldPay()*OrderDetail.getShouldNo()+"Ԫ\n", 0));
			}
			//��л��ʾ
			if(isthank)
			{
				ComA.send(PrintCmd.PrintString(thankstr+"\n", 0));
			}
			// ��ά��
			if(iser)
			{
				ComA.send(PrintCmd.PrintFeedline(2));    
				PrintQRCode();  // ��ά���ӡ                          
				ComA.send(PrintCmd.PrintFeedline(2));   
			}
			//��ǰʱ��
			if(isdate)
			{
				ComA.send(PrintCmd.SetAlignment(2));
				ComA.send(PrintCmd.PrintString(sdf.format(new Date()).toString() + "\n\n", 1));
			}
			// ��ֽ4��,����ֽ,������
			PrintFeedCutpaper(4);  
			SaveSharedPreferencesSerialno();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ��ӡ��ά��
	private void PrintQRCode() throws IOException {
		byte[] bValue = new byte[50];
		bValue = PrintCmd.PrintQrcode(erstr, 25, 7, 1);
		ComA.send(bValue);
	}
	
	// ��ֽ���У�����ֽ��������
	private void PrintFeedCutpaper(int iLine) throws IOException{
		ComA.send(PrintCmd.PrintFeedline(iLine));
		ComA.send(PrintCmd.PrintCutpaper(0));
		ComA.send(PrintCmd.SetClean());
	}
	// �����棬ȱʡģʽ
	private void CleanPrinter(){
		ComA.send(PrintCmd.SetClean());
	}
    
	// -------------------------ˢ����ʾ�߳�---------------------------
	private class DispQueueThread extends Thread {
		private Queue<ComBean> QueueList = new LinkedList<ComBean>();
		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				final ComBean ComData;
				while ((ComData = QueueList.poll()) != null) {
					runOnUiThread(new Runnable() {
						public void run() {
							DispRecData(ComData);
						}
					});
					try {
						Thread.sleep(200);// ��ʾ���ܸߵĻ������԰Ѵ���ֵ��С��
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		public synchronized void AddQueue(ComBean ComData) {
			QueueList.add(ComData);
		}
	}
	
	// ------------------------��ʾ��������----------------------------
	 /*
	  * 0 ��ӡ������ ��1 ��ӡ��δ���ӻ�δ�ϵ硢2 ��ӡ���͵��ÿⲻƥ�� 
	  * 3 ��ӡͷ�� ��4 �е�δ��λ ��5 ��ӡͷ���� ��6 �ڱ���� ��7 ֽ�� ��8 ֽ����
	  */
	private void DispRecData(ComBean ComRecData) {
		Message childmsg=printmainhand.obtainMessage();
		StringBuilder sMsg = new StringBuilder();
		try {
			sMsg.append(MyFunc.ByteArrToHex(ComRecData.bRec));
			int iState = PrintCmd.CheckStatus(ComRecData.bRec); // ���״̬
			ToolClass.Log(ToolClass.INFO,"EV_COM","����״̬��" + iState + "======="
					+ ComRecData.bRec[0],"com.txt");
			switch (iState) {
			case 0:
				sMsg.append("����");                 // ����
				ercheck = true;
				childmsg.what=PrintTest.NORMAL;
				break;
			case 1:
				sMsg.append("δ���ӻ�δ�ϵ�");//δ���ӻ�δ�ϵ�
				ercheck = true;
				childmsg.what=PrintTest.NOPOWER;
				break;
			case 2:
				sMsg.append("�쳣[��ӡ���͵��ÿⲻƥ��]");               //�쳣[��ӡ���͵��ÿⲻƥ��]
				ercheck = false;
				childmsg.what=PrintTest.NOMATCH;
				break;
			case 3:
				sMsg.append("��ӡ��ͷ��");        //��ӡ��ͷ��
				ercheck = true;
				childmsg.what=PrintTest.HEADOPEN;
				break;
			case 4:
				sMsg.append("�е�δ��λ");         //�е�δ��λ
				ercheck = true;
				childmsg.what=PrintTest.CUTTERERR;
				break;
			case 5:
				sMsg.append("��ӡͷ����");    // ��ӡͷ����
				ercheck = true;
				childmsg.what=PrintTest.HEADHEAT;
				break;
			case 6:
				sMsg.append("�ڱ����");         // �ڱ����
				ercheck = true;
				childmsg.what=PrintTest.BLACKMARKERR;
				break;
			case 7:
				sMsg.append("ֽ��");               //ֽ��
				ercheck = true;
				childmsg.what=PrintTest.PAPEREXH;
				break;
			case 8:
				sMsg.append("ֽ����");           //ֽ����
				ercheck = true;
				childmsg.what=PrintTest.PAPERWILLEXH;
				break;
			default:
				break;
			}
			childmsg.obj=sMsg.toString();
		} catch (Exception ex) {
			childmsg.what=PrintTest.UNKNOWERR;
			childmsg.obj=ex.getMessage();
		}
		printmainhand.sendMessage(childmsg);
	}
    // -------------------------�ײ㴮�ڿ�����---------------------------
    private static class SerialControl extends SerialHelper {
		public SerialControl() {
		}
		private static SerialControl single = null;
		// ��̬��������
		public static SerialControl getInstance() {
			if (single == null) {
				single = new SerialControl();
			}
			return single;
		}
		@Override
		protected void onDataReceived(final ComBean ComRecData) {
			DispQueue.AddQueue(ComRecData);// �̶߳�ʱˢ����ʾ(�Ƽ�)
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
    	//=================
	    //��ӡ�����
	    //=================
    	vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(BusPort.this);// ����InaccountDAO����
	    // �õ��豸ID��
    	Tb_vmc_system_parameter tb_inaccount = parameterDAO.find();
    	if(tb_inaccount!=null)
    	{
    		isPrinter=tb_inaccount.getIsNet();
    	}
    	ToolClass.Log(ToolClass.INFO,"EV_COM","isPrinter=" + isPrinter,"com.txt");
        if(isPrinter>0)
        {
        	ToolClass.Log(ToolClass.INFO,"EV_COM","�򿪴�ӡ��","com.txt");
        	ReadSharedPreferencesPrinter();
	    	ComA = SerialControl.getInstance();
	        ComA.setbLoopData(PrintCmd.GetStatus());
	        DispQueue = new DispQueueThread();
			DispQueue.start();
			//�򿪴���
			ComA.setPort(ToolClass.getPrintcom());            // 1.1 �趨����
			ComA.setBaudRate("9600");// 1.2 �趨������
			OpenComPort(ComA); // 1.3 �򿪴���			
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
  	    billdev=1;//�Ƿ���Ҫ��ֽ����,1��Ҫ
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
  		//posҳ��
  		iszhipos=0;//1�ɹ������˿ۿ�����,0û�з��ͳɹ��ۿ�����2ˢ���ۿ��Ѿ���ɲ��ҽ���㹻
  		ercheck=false;//true���ڶ�ά����̲߳����У����Ժ�falseû�ж�ά����̲߳���
  	    //��ӡ��ҳ��
  		isPrinter=0;//0û�����ô�ӡ����1�����ô�ӡ����2��ӡ���Լ�ɹ������Դ�ӡ
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
				//Heart����
			    Intent intent2=new Intent();
		    	intent2.putExtra("EVWhat", EVprotocol.EV_MDB_HEART);
				intent2.setAction("android.intent.action.comsend");//action���������ͬ
				comBroadreceiver.sendBroadcast(intent2);
				
				amount=OrderDetail.getShouldPay()*OrderDetail.getShouldNo(); 
				zhifutype="0";
				out_trade_no=ToolClass.out_trade_no(BusPort.this);// ����InaccountDAO����;
				//�л�ҳ��
				if (buszhiamountFragment == null) {
					buszhiamountFragment = new BuszhiamountFragment();
	            }
	            // ʹ�õ�ǰFragment�Ĳ������id_content�Ŀؼ�
	            transaction.replace(R.id.id_content, buszhiamountFragment);
	            //��ʱ
			    new Handler().postDelayed(new Runnable() 
				{
		            @Override
		            public void run() 
		            {   
		            	//��ֽ����
						BillEnable(1);
		            }

				}, 1500);
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
			case BUSZHIPOS://pos֧��
				isbus=false;
				amount=OrderDetail.getShouldPay()*OrderDetail.getShouldNo();
				zhifutype="1";				
				out_trade_no=ToolClass.out_trade_no(BusPort.this);// ����InaccountDAO����;
				if (buszhiposFragment == null) {
					buszhiposFragment = new BuszhiposFragment();
	            }
	            // ʹ�õ�ǰFragment�Ĳ������id_content�Ŀؼ�
	            transaction.replace(R.id.id_content, buszhiposFragment);
	            ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �򿪶�����"+ToolClass.getExtracom(),"com.txt");
	            //�򿪴���
	            //ip���˿ڡ����ڡ������ʱ���׼ȷ
				mMyApi.pos_init("121.40.30.62", 18080
						,ToolClass.getExtracom(), "9600", mIUserCallback);
				//��ʱ
			    new Handler().postDelayed(new Runnable() 
				{
		            @Override
		            public void run() 
		            {   
		            	ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �������ۿ�="+amount,"com.txt");
		            	listterner.BusportTsxx("��ʾ��Ϣ����ˢ��");
						mMyApi.pos_purchase(ToolClass.MoneySend(amount), mIUserCallback);	
				    	iszhipos=1;
					}

				}, 500);	           
				break;	
			case BUSHUO://����ҳ��	
				recLen=10*60;
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
			case COMThread.EV_OPTMAIN: 
				SerializableMap serializableMap = (SerializableMap) bundle.get("result");
				Map<String, Integer> Set=serializableMap.getMap();
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMBusPort �ۺ��豸����="+Set,"com.txt");
				int EV_TYPE=Set.get("EV_TYPE");
				switch(EV_TYPE)
				{
					case EVprotocol.EV_MDB_ENABLE:	
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
								billdev=0;
							}
							//ֽ��������
							else if( ((Integer)Set.get("bill_result")==0)&&((Integer)Set.get("coin_result")==1) )
							{
								listterner.BusportTsxx("��ʾ��Ϣ��[ֽ����]�޷�ʹ��");
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
								ToolClass.setBill_err(2);
								ToolClass.setCoin_err(0);
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
						iszhienable=1;
						break;	
					case EVprotocol.EV_MDB_HEART://������ѯ
						Map<String,Object> obj=new LinkedHashMap<String, Object>();
						obj.putAll(Set);
						String bill_enable="";
						String coin_enable="";
						String hopperString="";
						int bill_err=ToolClass.getvmcStatus(obj,1);
						int coin_err=ToolClass.getvmcStatus(obj,2);	
						//����ֽ��������ʱ��������ֽ������
						if(bill_err>0)
						{
							billdev=0;
						}
						//ֽ����ҳ��
						if(iszhienable==1)
						{								
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
					case EVprotocol.EV_MDB_COST://�ۿ�����
						float cost=ToolClass.MoneyRec((Integer)Set.get("cost"));	
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMBusPort �ۿ�="+cost,"com.txt");
						money-=amount;//�ۿ�
						payback();								
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
				    		OrderDetail.addLog(BusPort.this);
				    	}
				    	else
				    	{
				    		OrderDetail.cleardata();
						}
				    	dialog.dismiss();
						if(gotoswitch==BUSZHIAMOUNT)
			  	    	{
			  	    		amountDestroy(0);	
			  	    	}
			  	    	else
			  	    	{
			  	    		amountDestroy(1);
			  	    	}
						break; 
					//�ǳ�������	
					case EVprotocol.EV_BENTO_OPEN://���ӹ���� 					
					case EVprotocol.EV_COLUMN_OPEN://�������
						status=Set.get("result");//�������
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
		intent.putExtra("EVWhat", EVprotocol.EV_MDB_ENABLE);	
		intent.putExtra("bill", billdev);	
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
