package com.easivend.app.business;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.bean.ComBean;
import com.easivend.app.maintain.PrintTest;
import com.easivend.common.OrderDetail;
import com.easivend.common.SerializableMap;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.evprotocol.COMThread;
import com.easivend.evprotocol.EVprotocol;
import com.easivend.model.Tb_vmc_system_parameter;
import com.easivend.view.COMService;
import com.example.evconsole.R;
import com.example.printdemo.MyFunc;
import com.example.printdemo.SerialHelper;
import com.printsdk.cmd.PrintCmd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class BusHuo extends Activity 
{
	private final int SPLASH_DISPLAY_LENGHT = 500; // �ӳ�2��		
	private String proID = null;
	private String productID = null;
	private String proType = null;
	private String cabID = null;
	private String huoID = null;
    private float prosales = 0;
    private int count = 0;
    private int zhifutype = 0;//0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
    private TextView txtbushuoname = null;
    private ImageView ivbushuoquhuo=null;
    private int tempx=0;
    private int cabinetvar=0,huodaoNo=0,cabinetTypevar=0;
    private vmc_columnDAO columnDAO =null; 
    //�������
    private int status=0;//�������	
    //=================
    //COM�������
    //=================
  	LocalBroadcastManager comBroadreceiver;
  	COMReceiver comreceiver;
    //=================
    //��ӡ�����
    //=================
  	boolean istitle1,istitle2,isno,issum,isthank,iser,isdate,isdocter;
	int serialno=0;//��ˮ��
	String title1str,title2str,thankstr,erstr;
	SerialControl ComA;                  // ���ڿ���
	static DispQueueThread DispQueue;    // ˢ����ʾ�߳�
	private boolean ercheck=false;//true���ڴ�ӡ���Ĳ����У����Ժ�falseû�д�ӡ���Ĳ���
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // ���ʻ���־ʱ���ʽ��
	SimpleDateFormat sdfdoc = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss"); // ҩ��СƱ��
	private Handler printmainhand=null;
	private int isPrinter=0;//0û�����ô�ӡ����1�����ô�ӡ��
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ��title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ȫ��
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.bushuo);	
		//ɾ��ǰ���activity
		if(BusgoodsClass.BusgoodsClassAct!=null)
			BusgoodsClass.BusgoodsClassAct.finish(); 
		if(Busgoods.BusgoodsAct!=null)
			Busgoods.BusgoodsAct.finish(); 
		if(BusgoodsSelect.BusgoodsSelectAct!=null)
			BusgoodsSelect.BusgoodsSelectAct.finish(); 
		
		
		//4.ע�������
		comBroadreceiver = LocalBroadcastManager.getInstance(this);
		comreceiver=new COMReceiver();
		IntentFilter comfilter=new IntentFilter();
		comfilter.addAction("android.intent.action.comrec");
		comBroadreceiver.registerReceiver(comreceiver,comfilter);
		 
  	    
		//����Ʒҳ����ȡ����ѡ�е���Ʒ
//		Intent intent=getIntent();
//		Bundle bundle=intent.getExtras();
		proID=OrderDetail.getProID();
		productID=OrderDetail.getProductID();
		proType=OrderDetail.getProType();
		cabID=OrderDetail.getCabID();
		huoID=OrderDetail.getColumnID();
		prosales=OrderDetail.getShouldPay();//��Ʒ����
		count=OrderDetail.getShouldNo();//����
		zhifutype=OrderDetail.getPayType();
		txtbushuoname=(TextView)findViewById(R.id.txtbushuoname);
				
  	    ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷorderID="+OrderDetail.getOrdereID()+"proID="+proID+" productID="
				+productID+" proType="
				+proType+" cabID="+cabID+" huoID="+huoID+" prosales="+prosales+" count="
				+count+" zhifutype="+zhifutype,"log.txt");		
  	    this.ivbushuoquhuo =(ImageView) super.findViewById(R.id.ivbushuoquhuo);  	    
  	    Bitmap bitmap=ToolClass.ReadAdshuoFile();
	    if(bitmap!=null)
	    {
	    	this.ivbushuoquhuo.setImageBitmap(bitmap);// ����ͼ��Ķ�����ֵ
	    }
	    else
	    {
	    	ivbushuoquhuo.setImageResource(R.drawable.chuwaitland);
	    }
		//****
		//����
		//****
		chuhuoopt(tempx);
		
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
						ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo��ӡ������","com.txt");
						if(isPrinter==1)
							isPrinter=2;
						break;
					case PrintTest.NOPOWER:
						ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo��ӡ��δ���ӻ�δ�ϵ�","com.txt");
						break;
					case PrintTest.NOMATCH:
						ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo��ӡ���쳣[��ӡ���͵��ÿⲻƥ��]","com.txt");
						if(isPrinter==1)
							isPrinter=2;
						break;
					case PrintTest.HEADOPEN:	
						ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo��ӡ����ӡ��ͷ��","com.txt");
						break;
					case PrintTest.CUTTERERR:
						ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo��ӡ���е�δ��λ","com.txt");
						break;
					case PrintTest.HEADHEAT:
						ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo��ӡ��ͷ����","com.txt");
						break;
					case PrintTest.BLACKMARKERR:
						ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo��ӡ���ڱ����","com.txt");
						break;
					case PrintTest.PAPEREXH:	
						ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo��ӡ��ֽ��","com.txt");
						break;
					case PrintTest.PAPERWILLEXH://���Ҳ���Ե�����״̬ʹ��	
						ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo��ӡ��ֽ����","com.txt");
						if(isPrinter==1)
							isPrinter=2;
						break;
					case PrintTest.UNKNOWERR: 
						ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo��ӡ�������쳣="+msg.obj,"com.txt");
						break;
				}				
			}
		};
		vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(BusHuo.this);// ����InaccountDAO����
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
	}
			
	//����,����ֵ0ʧ��,1����ָ��ɹ����ȴ����ؽ��,2�������
	private void chuhuoopt(int huox)
	{
		
		// ����InaccountDAO�������ڴ����ݿ�����ȡ���ݵ�Tb_vmc_column����
 	    columnDAO = new vmc_columnDAO(this);
 	    //txtbushuoname.setText(proID+"["+prosales+"]"+"->���ڳ���,���Ժ�...");
		//1.�������������
		//����Ʒid����
		if(proType.equals("1")==true)
		{
	 	    // ��ȡ����������Ϣ�����洢��Map������
			List<String> alllist = columnDAO.getproductColumn(productID);
			cabinetvar=Integer.parseInt(alllist.get(0));
			huodaoNo=Integer.parseInt(alllist.get(1));
			cabinetTypevar=Integer.parseInt(alllist.get(2));
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷcabID="+cabinetvar+"huoID="+huodaoNo+"cabType="+cabinetTypevar,"log.txt"); 
		}
		//������id����
		else if(proType.equals("2")==true)
		{
	 	    // ��ȡ����������Ϣ�����洢��Map������
			String alllist = columnDAO.getcolumnType(cabID);
			cabinetvar=Integer.parseInt(cabID);
			huodaoNo=Integer.parseInt(huoID);
			cabinetTypevar=Integer.parseInt(alllist);
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷcabID="+cabinetvar+"huoID="+huodaoNo+"cabType="+cabinetTypevar,"log.txt"); 
		}
		
		
		new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {	  
            	//2.���������
        		float cost=0;
        		if(zhifutype==0)
        		{
        			cost=prosales;
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

		}, SPLASH_DISPLAY_LENGHT);
	}
	//�޸Ĵ������
	private void chuhuoupdate(int cabinetvar,int huodaoNo)
	{
		String cab=null,huo=null;
		cab=String.valueOf(cabinetvar);
		//����id=1��9����Ϊ01��09
        if(huodaoNo<10)
        {
        	huo="0"+String.valueOf(huodaoNo);
        }
        else
        {
        	huo=String.valueOf(huodaoNo);
        }	
        //�۳��������
		columnDAO.update(cab,huo);		
	}
	
	//��¼��־�������type=1�����ɹ���0����ʧ��
	private void chuhuoLog(int type)
	{
		OrderDetail.setYujiHuo(1);
		OrderDetail.setCabID(String.valueOf(cabinetvar));
		OrderDetail.setColumnID(String.valueOf(huodaoNo));
		if(type==1)//�����ɹ�
		{
			OrderDetail.setPayStatus(0);
			OrderDetail.setRealHuo(1);
			OrderDetail.setHuoStatus(0);
		}
		else//����ʧ��
		{
			OrderDetail.setPayStatus(1);
			OrderDetail.setRealHuo(0);
			OrderDetail.setHuoStatus(1);
		}
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
				SerializableMap serializableMap2 = (SerializableMap) bundle.get("result");
				Map<String, Integer> Set2=serializableMap2.getMap();
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMBusHuo ��������="+Set2,"com.txt");
				int EV_TYPE=Set2.get("EV_TYPE");
				if((EV_TYPE==EVprotocol.EV_BENTO_OPEN)||(EV_TYPE==EVprotocol.EV_COLUMN_OPEN))
				{
					status=Set2.get("result");//�������
					ToolClass.Log(ToolClass.INFO,"EV_COM","APP<<BusHuo�������"+"device=["+cabinetvar+"],hdid=["+huodaoNo+"],status=["+status+"]","com.txt");	
					
					//������������ģ��ſ۳��������
					if(OrderDetail.getPayType()!=5)
					{
						//1.���³������
						//�۳��������
						chuhuoupdate(cabinetvar,huodaoNo);
					}					
					//�����ɹ�
					if(status==1)
					{
						txtbushuoname.setText(proID+"["+prosales+"]"+"->������ɣ��뵽"+cabinetvar+"��"+huodaoNo+"����ȡ��Ʒ!");
						txtbushuoname.setTextColor(android.graphics.Color.BLUE);
						chuhuoLog(1);//��¼��־
						ivbushuoquhuo.setImageResource(R.drawable.chusuccessland);
						//=======
						//��ӡ�����
						//=======
						//��ӡ
						if(isPrinter>0)
				        {
							new Handler().postDelayed(new Runnable() 
							{
					            @Override
					            public void run() 
					            {	 					            	
									ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo��ӡƾ֤...","com.txt");
									if(isdocter)
							    	{
							    		PrintDocter();                             // ��ӡСƱ
							    	}
							    	else
							    	{
							    		PrintBankQueue();                             // ��ӡСƱ
							    	}									
					            }

							}, 600);
							
				        }
					}
					else
					{
						txtbushuoname.setText(proID+"["+prosales+"]"+"->"+cabinetvar+"��"+huodaoNo+"��������ʧ�ܣ���û�б��ۿ�!");
						txtbushuoname.setTextColor(android.graphics.Color.RED);
						chuhuoLog(0);//��¼��־
						ivbushuoquhuo.setImageResource(R.drawable.chufailland);
					}
										
											
					//3.�˻�����ҳ��
		 	    	new Handler().postDelayed(new Runnable() 
					{
	                    @Override
	                    public void run() 
	                    {	   
	                    	//�˳�ʱ������intent
	        	            Intent intentrec=new Intent();
	        	            intentrec.putExtra("status", status);//�������
	                    	if(zhifutype==0)//�ֽ�֧��
	                    	{           
	                    		intentrec.putExtra("cabinetvar", cabinetvar);//�������	                    		
	            	            BusHuo.this.setResult(BusZhiAmount.RESULT_CANCELED,intentrec);                    	            
	                		}
	                    	else if(zhifutype==1)//����
	                    	{
	                    		BusHuo.this.setResult(BusZhipos.RESULT_CANCELED,intentrec);                    	            
	                		}
	                    	else if(zhifutype==3)//֧������ά��
	                    	{
	                    		BusHuo.this.setResult(BusZhier.RESULT_CANCELED,intentrec);                    	            
	                		}
	                    	else if(zhifutype==4)//΢��ɨ��
	                    	{                        			
	                    		BusHuo.this.setResult(BusZhiwei.RESULT_CANCELED,intentrec);                    	            
	                		}
	                    	else if(zhifutype==5)//�����
	                    	{                        			
	                    		BusHuo.this.setResult(BusZhitihuo.RESULT_CANCELED,intentrec);                    	            
	                		}
	                    	else if(zhifutype==-1)//ȡ����
	                    	{                        			
	                    		BusHuo.this.setResult(0x03,intentrec);                    	            
	                		}
	                    	finish();	
	                    }
	
					}, 2000);
					break;
				}
			//��ť����
			case COMThread.EV_BUTTONMAIN:
				SerializableMap serializableMap = (SerializableMap) bundle.get("result");
				Map<String, Integer> Set=serializableMap.getMap();
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMBusHuo ��������="+Set,"com.txt");
				break;
			}			
		}

	}
	
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
		//ҩ��СƱ
		isdocter=user.getBoolean("isdocter",true);
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
			ComA.send(PrintCmd.PrintString(OrderDetail.getProID()+"  ����"+prosales+"Ԫ\n", 0));
			ComA.send(PrintCmd.PrintString("_________________________________________\n\n", 0));
			//���ͳ��
			if(issum)
			{
				ComA.send(PrintCmd.PrintString("�ܼ�:"+prosales*count+"Ԫ\n", 0));
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
	/**
	 * ��ӡҩ��СƱ
	 */
	private void PrintDocter() {
		try {
			// СƱ����
			ComA.send(PrintCmd.SetAlignment(1));
			ComA.send(PrintCmd.PrintString("ҩ��СƱ\n", 0));
			CleanPrinter(); // �����棬ȱʡģʽ	
			//�������
			if(isno)
			{
				ComA.send(PrintCmd.PrintString("�վݺ�:                                  "+String.format("%06d", 385017+(serialno++))+"\n", 0));
			}					
			//��ǰʱ��
			if(isdate)
			{
				ComA.send(PrintCmd.PrintString(sdfdoc.format(new Date()).toString() + "\n\n", 1));
			}
			ComA.send(PrintCmd.PrintString("����      Ʒ�����     �۸�*����     С��\n", 0));
			ComA.send(PrintCmd.PrintString("____________________________________________\n", 0));
			//ComA.send(PrintCmd.PrintString("160705  ��̵��߽���0.32g*10��*3��  57*4  228\n", 0));
			ComA.send(PrintCmd.PrintString(OrderDetail.getProID()+"   "+prosales+"*"+count+"   "+prosales+"\n", 0));
			ComA.send(PrintCmd.PrintString("____________________________________________\n", 0));
			ComA.send(PrintCmd.PrintString("�ϼ�(�����):                         "+prosales*count+"\n", 0));
			ComA.send(PrintCmd.PrintString("Ӧ�տ�:                               "+prosales*count+"\n", 0));
			//0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
			if(zhifutype==0)
			{
				ComA.send(PrintCmd.PrintString("ʵ�տ�:                              "+OrderDetail.getSmallAmount()+"\n", 0));
				ComA.send(PrintCmd.PrintString("____________________________________________\n", 0));
				ComA.send(PrintCmd.PrintString("����:                                        \n", 0));
				ComA.send(PrintCmd.PrintString(" ������֧��:                      0.00\n", 0));
				ComA.send(PrintCmd.PrintString("____________________________________________\n", 0));
				ComA.send(PrintCmd.PrintString("�һ�(�����):                      "+(OrderDetail.getSmallAmount()-prosales)+"\n\n", 0));
			}
			else if(zhifutype==1)
			{
				ComA.send(PrintCmd.PrintString("ʵ�տ�:                              "+prosales*count+"\n", 0));
				ComA.send(PrintCmd.PrintString("____________________________________________\n", 0));
				ComA.send(PrintCmd.PrintString("����:                                        \n", 0));
				ComA.send(PrintCmd.PrintString(" ������֧��:                      "+prosales*count+"\n", 0));
				ComA.send(PrintCmd.PrintString("____________________________________________\n", 0));
				ComA.send(PrintCmd.PrintString("�һ�(�����):                      0.00\n\n", 0));
			}
			else if(zhifutype==3)
			{
				ComA.send(PrintCmd.PrintString("ʵ�տ�:                              "+prosales*count+"\n", 0));
				ComA.send(PrintCmd.PrintString("____________________________________________\n", 0));
				ComA.send(PrintCmd.PrintString("����:                                        \n", 0));
				ComA.send(PrintCmd.PrintString(" ֧����֧��:                      "+prosales*count+"\n", 0));
				ComA.send(PrintCmd.PrintString("____________________________________________\n", 0));
				ComA.send(PrintCmd.PrintString("�һ�(�����):                      0.00\n\n", 0));
			}
			else if(zhifutype==4)
			{
				ComA.send(PrintCmd.PrintString("ʵ�տ�:                              "+prosales*count+"\n", 0));
				ComA.send(PrintCmd.PrintString("____________________________________________\n", 0));
				ComA.send(PrintCmd.PrintString("����:                                        \n", 0));
				ComA.send(PrintCmd.PrintString("  ΢��֧��:                      "+prosales*count+"\n", 0));
				ComA.send(PrintCmd.PrintString("____________________________________________\n", 0));
				ComA.send(PrintCmd.PrintString("�һ�(�����):                      0.00\n\n", 0));
			}
			ComA.send(PrintCmd.PrintString("��������СƱ�Ա��ۺ����лл����ҩƷ��������Ʒ������������ˡ���˻�\n", 0));			
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
			ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo����״̬��" + iState + "======="
					+ ComRecData.bRec[0],"com.txt");
			switch (iState) {
			case 0:
				ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo>>����","com.txt");
				sMsg.append("����");                 // ����
				ercheck = true;
				childmsg.what=PrintTest.NORMAL;
				break;
			case 1:
				ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo>>δ���ӻ�δ�ϵ�","com.txt");
				sMsg.append("δ���ӻ�δ�ϵ�");//δ���ӻ�δ�ϵ�
				ercheck = true;
				childmsg.what=PrintTest.NOPOWER;
				break;
			case 2:
				ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo>>�쳣[��ӡ���͵��ÿⲻƥ��]","com.txt");
				sMsg.append("�쳣[��ӡ���͵��ÿⲻƥ��]");               //�쳣[��ӡ���͵��ÿⲻƥ��]
				ercheck = false;
				childmsg.what=PrintTest.NOMATCH;
				break;
			case 3:
				ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo>>��ӡ��ͷ��","com.txt");
				sMsg.append("��ӡ��ͷ��");        //��ӡ��ͷ��
				ercheck = true;
				childmsg.what=PrintTest.HEADOPEN;
				break;
			case 4:
				ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo>>�е�δ��λ","com.txt");
				sMsg.append("�е�δ��λ");         //�е�δ��λ
				ercheck = true;
				childmsg.what=PrintTest.CUTTERERR;
				break;
			case 5:
				ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo>>��ӡͷ����","com.txt");
				sMsg.append("��ӡͷ����");    // ��ӡͷ����
				ercheck = true;
				childmsg.what=PrintTest.HEADHEAT;
				break;
			case 6:
				ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo>>�ڱ����","com.txt");
				sMsg.append("�ڱ����");         // �ڱ����
				ercheck = true;
				childmsg.what=PrintTest.BLACKMARKERR;
				break;
			case 7:
				ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo>>ֽ��","com.txt");
				sMsg.append("ֽ��");               //ֽ��
				ercheck = true;
				childmsg.what=PrintTest.PAPEREXH;
				break;
			case 8:
				ToolClass.Log(ToolClass.INFO,"EV_COM","bushuo>>ֽ����","com.txt");
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
	
	@Override
	protected void onDestroy() {
		//=============
		//��ӡ�����
		//=============
		if(isPrinter>0)
		{
			CloseComPort(ComA);// 2.1 �رմ���
		}
		//=============
		//COM�������
		//=============
		//5.���ע�������
		comBroadreceiver.unregisterReceiver(comreceiver);	
		super.onDestroy();		
	}
}
