package com.easivend.evprotocol;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.common.ToolClass;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ExtraCOMThread implements Runnable {

	private Handler mainhand=null,childhand=null;
	public static final int EV_OPTMAIN	= 9;	//�����豸��������
	ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
	private static Map<String,Object> allSet = new LinkedHashMap<String,Object>() ;
	int onInit=0;//0��ʾû�г�ʼ��������ֵ��ʾ���ڳ�ʼ���Ľ׶�
	boolean cmdSend=false;//true���͵�����ȴ�ACKȷ��,���ֵֻ������Ҫ�ظ�ACK������
	int devopt=0;//����״ֵ̬	
	//��������
	int cabinet=0;
	int column=0;
	//�ֽ��豸ʹ�ܽ���
	int bill=0;
	int coin=0;
	int opt=0;
	//�ֽ��豸���		
	int cost_value=0;//�ֽ��豸�ۿ���
	
	int billPay=0;//ֽ���˱ҽ��
	int coinPay=0;//Ӳ���˱ҽ��
	
		
	public ExtraCOMThread(Handler mainhand) {
		this.mainhand=mainhand;		
	}
	public Handler obtainHandler()
	{
		return this.childhand;
	}
	
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		Looper.prepare();//�û��Լ�������࣬�����߳���Ҫ�Լ�׼��loop
		ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraThread start["+Thread.currentThread().getId()+"]","com.txt");
		childhand=new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what)
				{	
				//��ɽ��	
				case COMThread.EV_BENTO_CHECKALLCHILD://���߳̽������̱߳�ɽȫ����ѯ��Ϣ		
					//1.�õ���Ϣ
					JSONObject ev6=null;
					try {
						ev6 = new JSONObject(msg.obj.toString());
						devopt=COMThread.EV_BENTO_CHECKALLCHILD;						
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case COMThread.EV_BENTO_CHECKCHILD://���߳̽������̱߳�ɽ���ѯ��Ϣ	
					//1.�õ���Ϣ
					JSONObject ev7=null;
					try {
						ev7 = new JSONObject(msg.obj.toString());
						devopt=COMThread.EV_BENTO_CHECKCHILD;						
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case EVprotocol.EV_BENTO_OPEN://���߳̽������̸߳��ӿ���
					//1.�õ���Ϣ
					JSONObject ev2=null;
					try {
						ev2 = new JSONObject(msg.obj.toString());
						cabinet=ev2.getInt("cabinet");
						column=ev2.getInt("column");
						devopt=EVprotocol.EV_BENTO_OPEN;						
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;	
				case EVprotocol.EV_MDB_ENABLE://���߳̽������߳��ֽ��豸ʹ�ܽ���					
					//1.�õ���Ϣ
					JSONObject ev=null;
					try {
						ev = new JSONObject(msg.obj.toString());
						bill=ev.getInt("bill");
						coin=ev.getInt("coin");
						opt=ev.getInt("opt");
						devopt=EVprotocol.EV_MDB_ENABLE;						
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace(); 
					}										
					break;
				case EVprotocol.EV_MDB_B_INFO://���߳̽������߳��ֽ��豸
					//���ӿڻص���Ϣ
					allSet.clear();
					allSet.put("EV_TYPE", EVprotocol.EV_MDB_B_INFO);
					allSet.put("acceptor", 0);
					allSet.put("dispenser", 0);
					allSet.put("code", 0);
					allSet.put("sn", 0);
					allSet.put("model", 0);
					allSet.put("ver", 0);
					allSet.put("capacity", 0);
					for(int i=1;i<9;i++)
					{
						allSet.put("ch_r"+i, 0);								
					}
					
					for(int i=1;i<9;i++)
					{
						allSet.put("ch_d"+i, 0);								
					}
					//3.�����̷߳�����Ϣ
	  				Message tomain11=mainhand.obtainMessage();
	  				tomain11.what=EV_OPTMAIN;							
	  				tomain11.obj=allSet;
	  				mainhand.sendMessage(tomain11); // ������Ϣ
					
					break;	
				case EVprotocol.EV_MDB_C_INFO://���߳̽������߳��ֽ��豸
					//���ӿڻص���Ϣ
					allSet.clear();
					allSet.put("EV_TYPE", EVprotocol.EV_MDB_C_INFO);
					allSet.put("acceptor", 0);
					allSet.put("dispenser", 0);
					allSet.put("code", 0);
					allSet.put("sn", 0);
					allSet.put("model", 0);
					allSet.put("ver", 0);
					allSet.put("capacity", 0);
					for(int i=1;i<17;i++)
					{
						allSet.put("ch_r"+i, 0);								
					}
					
					for(int i=1;i<9;i++)
					{
						allSet.put("ch_d"+i, 0);								
					}
					//3.�����̷߳�����Ϣ
	  				Message tomain12=mainhand.obtainMessage();
	  				tomain12.what=EV_OPTMAIN;							
	  				tomain12.obj=allSet;
	  				mainhand.sendMessage(tomain12); // ������Ϣ
					
					break;	
				case EVprotocol.EV_MDB_PAYOUT://MDB�豸�ұ�
					//1.�õ���Ϣ
					JSONObject ev16=null;
					try {
						ev16 = new JSONObject(msg.obj.toString());
						bill=ev16.getInt("bill");
						coin=ev16.getInt("coin");
						billPay=ev16.getInt("billPay");
						coinPay=ev16.getInt("coinPay");	
						devopt=EVprotocol.EV_MDB_PAYOUT;
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}				
					break;	
					//����ҳ��ʹ��	
				case EVprotocol.EV_MDB_HEART://���߳̽������߳��ֽ��豸
					devopt=EVprotocol.EV_MDB_HEART;	
					break;
				case EVprotocol.EV_MDB_COST:					
					//1.�õ���Ϣ
					JSONObject ev18=null;
					try {
						ev18 = new JSONObject(msg.obj.toString());
						cost_value=ev18.getInt("cost");
						devopt=EVprotocol.EV_MDB_COST;						
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;	
				case EVprotocol.EV_MDB_PAYBACK:
					//1.�õ���Ϣ
					JSONObject ev19=null;
					try {
						ev19 = new JSONObject(msg.obj.toString());
						bill=ev19.getInt("bill");
						coin=ev19.getInt("coin");
						devopt=EVprotocol.EV_MDB_PAYBACK;						
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}					
					break;	
				case VboxProtocol.VBOX_PROTOCOL:
					ToolClass.Log(ToolClass.INFO,"EV_COM","COMThread ��ɽ��ر�","com.txt");
					timer.shutdown();
					break;
				}
			}
		};	
		timer.scheduleWithFixedDelay(task, 5000, 100,TimeUnit.MILLISECONDS);       // timeTask
		Looper.loop();//�û��Լ�������࣬�����߳���Ҫ�Լ�׼��loop		 
	}
	
	TimerTask task = new TimerTask() 
	{ 
		int onInit=0;//0��ʾû�г�ʼ��������ֵ��ʾ���ڳ�ʼ���Ľ׶�
		boolean cmdSend=false;//true���͵�����ȴ�ACKȷ��,���ֵֻ������Ҫ�ظ�ACK������
		int statusnum=0;//�ﵽһ��ֵʱ����һ��get_status
		//�ֽ��豸���	
		int coin_remain=0;//Ӳ������ǰ���ҽ��	�Է�Ϊ��λ
		int payback_value=0;//������
		
		int g_holdValue = 0;//��ǰ�ݴ�ֽ�ҽ�� �Է�Ϊ��λ
		int coin_recv=0;//Ӳ������ǰ�ձҽ��	�Է�Ϊ��λ
		int bill_recv=0;//ֽ������ǰ�ձҽ��	�Է�Ϊ��λ
			
		
		/*********************************************************************************************************
		** Function name:     	GetAmountMoney
		** Descriptions:	    Ͷ���ܽ��
		** input parameters:    ��
		** output parameters:   ��
		** Returned value:      ��
		*********************************************************************************************************/
		int GetAmountMoney()
		{	
			return coin_recv + bill_recv + g_holdValue;
		}
		
		int hd_num=0;//��������
		int  decimalPlaces=1;//������λ1��,2��,0Ԫ
		//����decimalPlaces��������
		void setDecimal(int decimal_places)
		{
			if(decimal_places==0)
			{
				 decimalPlaces=1;
			}
		}
		/***********************************************************
		vmc�ڲ��������Է�Ϊ��λ���������ϴ���pcʱ��Ҫת��

		decimalPlaces=1�Խ�Ϊ��λ
			���磬��Ҫ�ϴ���pc����200��,
			200*10=2000
			2000/100=20�ǣ����ϴ�20��
			
		decimalPlaces=2�Է�Ϊ��λ
			���磬��Ҫ�ϴ���pc����200��,
			200*100=20000
			20000/100=200�֣����ϴ�200��	

		decimalPlaces=0��ԪΪ��λ
			���磬��Ҫ�ϴ���pc����200��,
			200=200
			200/100=2Ԫ�����ϴ�2Ԫ	
		***************************************************************/
		int   MoneySend(int sendMoney)
		{
			int tempMoney=0;
			
			//��ʽ2: �ϴ�ScaledValue = ActualValueԪ/(10-DecimalPlaces�η�)
			if(decimalPlaces==1)
			{
				//tempMoney = sendMoney*10;
				//tempMoney = tempMoney/100;
				tempMoney = sendMoney/10;
			}
			else if(decimalPlaces==2)
			{
				tempMoney = sendMoney;
			}
			else if(decimalPlaces==0)
			{
				//tempMoney = sendMoney;
				//tempMoney = tempMoney/100;
				tempMoney = sendMoney/100;
			}
			//����:600��=6Ԫ
			return tempMoney;
		}
		
		/***********************************************************
		vmc�ڲ��������Է�Ϊ��λ�����Խ���pc������������ʱ��Ҫת��

		decimalPlaces=1�Խ�Ϊ��λ
			���磬pc�´���20��,
			20*100=2000
			2000/10=200�֣�������200��
			
		decimalPlaces=2�Է�Ϊ��λ
			���磬pc�´���200��,
			200*100=20000
			20000/100=200�֣�������200��	

		decimalPlaces=0��ԪΪ��λ
			���磬pc�´���2Ԫ,
			2*100=200
			200=200�֣�������200��
		***************************************************************/
		int   MoneyRec(int recMoney)
		{
			int tempMoney;
			
			tempMoney = recMoney;
			//��ʽ1:  ActualValueԪ =����ScaledValue*(10-DecimalPlaces�η�)
			if(decimalPlaces==1)
			{
				//tempMoney = tempMoney*100;
				//tempMoney = tempMoney/10;
				tempMoney = tempMoney*10;
			}
			else if(decimalPlaces==2)
			{
				tempMoney = tempMoney;
			}
			else if(decimalPlaces==0)
			{
				tempMoney = tempMoney*100;
				//tempMoney = tempMoney;
			}
			//����:6Ԫ=600��	
			return tempMoney;
		}
		
        @Override 
        public void run() 
        { 
        	if(ToolClass.getExtraComType()>0)
        	{
	        	//ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraThread Timer["+Thread.currentThread().getId()+"]","com.txt");
	        	String resjson = VboxProtocol.VboxReadMsg(ToolClass.getExtracom_id(),100);
	        	//ToolClass.Log(ToolClass.INFO,"EV_COM","Threadresjson<<"+resjson.toString(),"com.txt");
	        	//2.�������
				try {
					JSONObject jsonObject6 = new JSONObject(resjson); 
					//����keyȡ������
					JSONObject ev_head6 = (JSONObject) jsonObject6.getJSONObject("EV_json");
					int str_evType6 =  ev_head6.getInt("EV_type");
					if(str_evType6==VboxProtocol.VBOX_PROTOCOL)
					{
						int mt= ev_head6.getInt("mt");
						if(mt == VboxProtocol.VBOX_TIMEOUT || mt == VboxProtocol.VBOX_DATA_ERROR)
						{
							//ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraAPI<<ERROR="+mt,"com.txt");
				        }
						else
						{
							//1.����ACK
							int F7=ev_head6.getInt("F7");
							if(F7 == 1 && mt != VboxProtocol.VBOX_POLL){
			                    VboxProtocol.VboxSendAck(ToolClass.getExtracom_id());
			                }
							
							switch(mt)
							{
								case VboxProtocol.VBOX_ACK_RPT:	
									ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraACK<<ACK,cmd="+cmdSend+"devopt="+devopt+"onInit="+onInit,"com.txt");
									if(cmdSend)
									{
										switch(devopt)
										{
											case VboxProtocol.VBOX_HUODAO_IND:
												if(onInit==2)
												{
													devopt=VboxProtocol.VBOX_SALEPRICE_IND;
													cmdSend=false;
													onInit=3;//saleprice_ind�׶�
												}
												break;
											case VboxProtocol.VBOX_SALEPRICE_IND:	
												if(onInit==3)
												{
													devopt=COMThread.EV_BENTO_CHECKCHILD;
													cmdSend=false;
													onInit=4;//get_huodao�׶�
												}
												break;
											case EVprotocol.EV_BENTO_OPEN://���߳̽������̸߳��ӿ���
												//��������ֵ
												devopt=0;
												cmdSend=false;
												break;
											case EVprotocol.EV_MDB_ENABLE://���߳̽������߳��ֽ��豸ʹ�ܽ���	
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadENABLERec<<bill="+bill+"coin="+coin+"opt="+opt,"com.txt");
												//��������ֵ
												devopt=0;
												cmdSend=false;
												//���ӿڻص���Ϣ
												allSet.clear();
												allSet.put("EV_TYPE", EVprotocol.EV_MDB_ENABLE);
												allSet.put("opt", opt);
												allSet.put("bill_result", bill);
												allSet.put("coin_result", coin);												
												//3.�����̷߳�����Ϣ
												Message tomain=mainhand.obtainMessage();
												tomain.what=EV_OPTMAIN;							
												tomain.obj=allSet;
												mainhand.sendMessage(tomain); // ������Ϣ
												break;	
											case EVprotocol.EV_MDB_PAYOUT://MDB�豸�ұ�
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadPAYOUTRec<<coinPay="+coinPay,"com.txt");
												//��������ֵ
												devopt=0;
												cmdSend=false;	
												//���ӿڻص���Ϣ
												allSet.clear();
												allSet.put("EV_TYPE", EVprotocol.EV_MDB_PAYOUT);
												allSet.put("result", 1);
												allSet.put("bill_changed", 0);
												allSet.put("coin_changed", coinPay);
												//3.�����̷߳�����Ϣ
								  				Message tomain16=mainhand.obtainMessage();
								  				tomain16.what=EV_OPTMAIN;							
								  				tomain16.obj=allSet;
								  				mainhand.sendMessage(tomain16); // ������Ϣ
												break;	
											case EVprotocol.EV_MDB_COST://�ۿ�
												//��������ֵ
												devopt=0;
												cmdSend=false;
												break;
											case EVprotocol.EV_MDB_PAYBACK://�˱�
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadPAYBACKRec<<GetAmountMoney="+GetAmountMoney(),"com.txt");
												//��������ֵ
												devopt=0;
												cmdSend=false;
												//���ӿڻص���Ϣ
												allSet.clear();
												allSet.put("EV_TYPE", EVprotocol.EV_MDB_PAYBACK);
												allSet.put("result", 1);
												allSet.put("bill_changed", 0);
												allSet.put("coin_changed", GetAmountMoney());
												//3.�����̷߳�����Ϣ
								  				Message tomain19=mainhand.obtainMessage();
								  				tomain19.what=EV_OPTMAIN;							
								  				tomain19.obj=allSet;
								  				mainhand.sendMessage(tomain19); // ������Ϣ
												break;
										}
									}
									break;
								case VboxProtocol.VBOX_NAK_RPT:	
									ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraNAK<<NAK,cmd="+cmdSend+"devopt="+devopt+"onInit="+onInit,"com.txt");
									if(cmdSend)
									{
										switch(devopt)
										{
											case VboxProtocol.VBOX_HUODAO_IND:
												if(onInit==2)
												{
													devopt=VboxProtocol.VBOX_SALEPRICE_IND;
													cmdSend=false;
													onInit=3;//saleprice_ind�׶�
												}
												break;
											case VboxProtocol.VBOX_SALEPRICE_IND:	
												if(onInit==3)
												{
													devopt=COMThread.EV_BENTO_CHECKCHILD;
													cmdSend=false;
													onInit=4;//get_huodao�׶�
												}
												break;
											case EVprotocol.EV_BENTO_OPEN://���߳̽������̸߳��ӿ���
												//��������ֵ
												devopt=0;
												cmdSend=false;
												break;	
											case EVprotocol.EV_MDB_ENABLE://���߳̽������߳��ֽ��豸ʹ�ܽ���	
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadENABLERec<<bill="+bill+"coin="+coin+"opt="+opt,"com.txt");
												//��������ֵ
												devopt=0;
												cmdSend=false;
												//���ӿڻص���Ϣ
												allSet.clear();
												allSet.put("EV_TYPE", EVprotocol.EV_MDB_ENABLE);
												allSet.put("opt", opt);
												allSet.put("bill_result", bill);
												allSet.put("coin_result", coin);												
												//3.�����̷߳�����Ϣ
												Message tomain=mainhand.obtainMessage();
												tomain.what=EV_OPTMAIN;							
												tomain.obj=allSet;
												mainhand.sendMessage(tomain); // ������Ϣ
												break;	
											case EVprotocol.EV_MDB_PAYOUT://MDB�豸�ұ�
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadPAYOUTRec<<coinPay="+coinPay,"com.txt");
												//��������ֵ
												devopt=0;
												cmdSend=false;	
												//���ӿڻص���Ϣ
												allSet.clear();
												allSet.put("EV_TYPE", EVprotocol.EV_MDB_PAYOUT);
												allSet.put("result", 1);
												allSet.put("bill_changed", 0);
												allSet.put("coin_changed", coinPay);
												//3.�����̷߳�����Ϣ
								  				Message tomain16=mainhand.obtainMessage();
								  				tomain16.what=EV_OPTMAIN;							
								  				tomain16.obj=allSet;
								  				mainhand.sendMessage(tomain16); // ������Ϣ
												break;	
											case EVprotocol.EV_MDB_COST://�ۿ�
												//��������ֵ
												devopt=0;
												cmdSend=false;
												break;
											case EVprotocol.EV_MDB_PAYBACK://�˱�
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadPAYBACKRec<<GetAmountMoney="+GetAmountMoney(),"com.txt");
												//��������ֵ
												devopt=0;
												cmdSend=false;
												//���ӿڻص���Ϣ
												allSet.clear();
												allSet.put("EV_TYPE", EVprotocol.EV_MDB_PAYBACK);
												allSet.put("result", 1);
												allSet.put("bill_changed", 0);
												allSet.put("coin_changed", GetAmountMoney());
												//3.�����̷߳�����Ϣ
								  				Message tomain19=mainhand.obtainMessage();
								  				tomain19.what=EV_OPTMAIN;							
								  				tomain19.obj=allSet;
								  				mainhand.sendMessage(tomain19); // ������Ϣ
												break;
										}
									}
									break;	
								case VboxProtocol.VBOX_POLL:								
									//ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraPOLL<<"+resjson.toString(),"com.txt");
									switch(devopt)
									{
										//׼����ʼ���׶�
										case COMThread.EV_BENTO_CHECKALLCHILD://���߳̽������̱߳�ɽ��ȫ����ѯ��Ϣ
											if(cmdSend==false)
											{
												if(++statusnum>2)
												{
													statusnum=0;
													ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadGetSetup>>","com.txt");
													VboxProtocol.VboxGetSetup(ToolClass.getExtracom_id());
													onInit=1;//setup�׶�
													cmdSend=true;
												}
												else
												{
													ToolClass.Log(ToolClass.INFO,"EV_COM","Threadstatusnumwait="+statusnum,"com.txt");
													if(F7==1)
													{
														VboxProtocol.VboxSendAck(ToolClass.getExtracom_id());
													}
												}
											}
											break;
										case VboxProtocol.VBOX_HUODAO_IND:
											if((onInit==2)&&(cmdSend==false))
											{
												if(++statusnum>2)
												{
													statusnum=0;
													JSONArray arr=new JSONArray();
													for(int i=1;i<(hd_num+1);i++)
													{
														JSONObject obj=new JSONObject();
														obj.put("id", i);
														arr.put(obj);
													}
													JSONObject zhuheobj=new JSONObject();
													zhuheobj.put("port", ToolClass.getExtracom_id());
													zhuheobj.put("sp_id", arr);										
													zhuheobj.put("device", 0);
													zhuheobj.put("EV_type", VboxProtocol.VBOX_PROTOCOL);
													JSONObject reqStr=new JSONObject();
													reqStr.put("EV_json", zhuheobj);
													ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadHuodaoind>>"+reqStr,"com.txt");
													VboxProtocol.VboxHuodaolInd(ToolClass.getExtracom_id(),reqStr.toString());
													cmdSend=true;
												}
												else
												{
													ToolClass.Log(ToolClass.INFO,"EV_COM","Threadstatusnumwait="+statusnum,"com.txt");
													if(F7==1)
													{
														VboxProtocol.VboxSendAck(ToolClass.getExtracom_id());
													}
												}
											}
											break;
										case VboxProtocol.VBOX_SALEPRICE_IND:
											if((onInit==3)&&(cmdSend==false))
											{
												if(++statusnum>2)
												{
													statusnum=0;
													JSONArray arr=new JSONArray();
													for(int i=1;i<(hd_num+1);i++)
													{
														JSONObject obj=new JSONObject();
														obj.put("id", MoneySend(100));//56800
														arr.put(obj);
													}
													JSONObject zhuheobj=new JSONObject();
													zhuheobj.put("port", ToolClass.getExtracom_id());
													zhuheobj.put("sp_id", arr);										
													zhuheobj.put("device", 0);
													zhuheobj.put("EV_type", VboxProtocol.VBOX_PROTOCOL);
													JSONObject reqStr=new JSONObject();
													reqStr.put("EV_json", zhuheobj);
													ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSalepriceind>>"+reqStr,"com.txt");
													VboxProtocol.VboxSalePriceInd(ToolClass.getExtracom_id(),reqStr.toString());
													cmdSend=true;
												}
												else
												{
													ToolClass.Log(ToolClass.INFO,"EV_COM","Threadstatusnumwait="+statusnum,"com.txt");
													if(F7==1)
													{
														VboxProtocol.VboxSendAck(ToolClass.getExtracom_id());
													}
												}	
											}
											break;
										case COMThread.EV_BENTO_CHECKCHILD://���߳̽������̱߳�ɽ���ѯ��Ϣ	
											if(cmdSend==false)
											{
												//��ʼ��
												if(onInit==4)
												{
													if(++statusnum>2)
													{
														statusnum=0;
														ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadGetHuodao>>","com.txt");
														VboxProtocol.VboxGetHuoDao(ToolClass.getExtracom_id(),0);
														cmdSend=true;
													}
													else
													{
														ToolClass.Log(ToolClass.INFO,"EV_COM","Threadstatusnumwait="+statusnum,"com.txt");
														if(F7==1)
														{
															VboxProtocol.VboxSendAck(ToolClass.getExtracom_id());
														}
													}
												}
												//����GetHuodao
												else
												{
													ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadGetHuodao>>","com.txt");
													VboxProtocol.VboxGetHuoDao(ToolClass.getExtracom_id(),0);
													cmdSend=true;
												}
											}
											break;	
										case EVprotocol.EV_BENTO_OPEN://���߳̽������̸߳��ӿ���
											if(cmdSend==false)
											{
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSend0.2=cabinet="+cabinet+"column="+column,"com.txt");
												VboxProtocol.VboxVendoutInd(ToolClass.getExtracom_id(), 0, 2, column, 2, 0);
												cmdSend=true;
											}
											break;
										case EVprotocol.EV_MDB_ENABLE://���߳̽������߳��ֽ��豸ʹ�ܽ���	
											if(cmdSend==false)
											{
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadENABLESend0.2>>bill="+bill+"coin="+coin+"opt="+opt,"com.txt");
												VboxProtocol.VboxControlInd(ToolClass.getExtracom_id(),2,opt);
												cmdSend=true;
											}
											break;
										case EVprotocol.EV_MDB_PAYOUT://MDB�豸�ұ�
											if(cmdSend==false)
											{
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadPAYOUTSend0.2>>bill="+bill+"coin="+coin+"billPay="+billPay+"coinPay="+coinPay,"com.txt");
												VboxProtocol.VboxPayoutInd(ToolClass.getExtracom_id(),0,MoneySend(coinPay),2);
												cmdSend=true;
											}											
											break;
										case EVprotocol.EV_MDB_HEART://������ѯ�ӿ�
											ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadHEARTSend0.2>>","com.txt");
											if(++statusnum>2)
											{
												statusnum=0;
												VboxProtocol.VboxGetStatus(ToolClass.getExtracom_id());
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadGetStatus>>","com.txt");
											}
											//���ӿڻص���Ϣ
											allSet.clear();
											allSet.put("EV_TYPE", EVprotocol.EV_MDB_HEART);
											allSet.put("bill_enable", 1);
											allSet.put("bill_payback", 0);
											allSet.put("bill_err", 0);
											allSet.put("bill_recv", bill_recv+g_holdValue);
											allSet.put("bill_remain", 0);
											allSet.put("coin_enable", 1);
											allSet.put("coin_payback", 0);
											allSet.put("coin_err", 0);
											allSet.put("coin_recv", coin_recv);
											allSet.put("coin_remain", coin_remain);
											allSet.put("hopper1", 0);
											allSet.put("hopper2", 0);
											allSet.put("hopper3", 0);
											allSet.put("hopper4", 0);
											allSet.put("hopper5", 0);
											allSet.put("hopper6", 0);
											allSet.put("hopper7", 0);
											allSet.put("hopper8", 0);
											devopt=0;
											//3.�����̷߳�����Ϣ
							  				Message tomain13=mainhand.obtainMessage();
							  				tomain13.what=EV_OPTMAIN;							
							  				tomain13.obj=allSet;
							  				mainhand.sendMessage(tomain13); // ������Ϣ
											break;
										case EVprotocol.EV_MDB_COST://�ۿ�
											if(cmdSend==false)
											{
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadCOSTSend0.2>>cost="+cost_value,"com.txt");
												VboxProtocol.VboxCostInd(ToolClass.getExtracom_id(),0,MoneySend(cost_value),2);
												cmdSend=true;
											}
											break;	
										case EVprotocol.EV_MDB_PAYBACK://�˱�
											if(cmdSend==false)
											{
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadPAYBACKSend0.2>>bill="+bill+"coin="+coin,"com.txt");
												VboxProtocol.VboxControlInd(ToolClass.getExtracom_id(),6,0);
												cmdSend=true;
											}
											break;
										default:
											if(F7==1)
											{
												VboxProtocol.VboxSendAck(ToolClass.getExtracom_id());
											}
											break;
									}
									break;
								case VboxProtocol.VBOX_VMC_SETUP://����setup����Ϣ
									//��ʼ��1.Get_Setup
									if((onInit==1)&&(cmdSend))
									{
										hd_num=ev_head6.getInt("hd_num");
										int decimal_places=ev_head6.getInt("decimal_places");											
										ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSetupRpt<<"+ev_head6,"com.txt");
										setDecimal(decimal_places);
										devopt=VboxProtocol.VBOX_HUODAO_IND;
										cmdSend=false;
										onInit=2;//huodao_ind�׶�
									}
									break;
								case VboxProtocol.VBOX_PAYIN_RPT://Ͷ����Ϣ
									int dt=ev_head6.getInt("dt");
									int value=ev_head6.getInt("value");
									if(dt==0)
									{
										coin_recv+=MoneyRec(value);
									}
									else if(dt==100)
									{
										g_holdValue=MoneyRec(value);
									}
									else if(dt==101)
									{
										g_holdValue=0;
									}
									else if(dt==1)
									{
										bill_recv+=MoneyRec(value);
										g_holdValue=0;
									}
									ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadPayinRpt<<dt="+dt+"value="+MoneyRec(value)+"GetAmountMoney="+GetAmountMoney(),"com.txt");
									break;	
								case VboxProtocol.VBOX_PAYOUT_RPT://�ұ���Ϣ
									payback_value=MoneyRec(ev_head6.getInt("value")); 
									g_holdValue = 0;//��ǰ�ݴ�ֽ�ҽ�� �Է�Ϊ��λ
									bill_recv=0;//ֽ������ǰ�ձҽ��	�Է�Ϊ��λ
									coin_recv=MoneyRec(ev_head6.getInt("total_value"));//Ӳ������ǰ�ձҽ��	�Է�Ϊ��λ
									ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadPayoutRpt<<payback_value="+payback_value+"GetAmountMoney="+GetAmountMoney(),"com.txt");
									break;
								case VboxProtocol.VBOX_VENDOUT_RPT://�������
									int status=ev_head6.getInt("status");
									ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadVendoutRpt<<column="+column+"status="+status,"com.txt");
									//�����ɹ�
									if(status==0)
									{
										//���ӿڻص���Ϣ
										allSet.clear();
										allSet.put("EV_TYPE", EVprotocol.EV_BENTO_OPEN);
										allSet.put("addr", 1);//���ӵ�ַ
										allSet.put("box", column);//���ӵ�ַ
										allSet.put("result", 1);
									}
									//����ʧ��
									else
									{
										//���ӿڻص���Ϣ
										allSet.clear();
										allSet.put("EV_TYPE", EVprotocol.EV_BENTO_OPEN);
										allSet.put("addr", 0);//���ӵ�ַ
										allSet.put("box", 0);//���ӵ�ַ
										allSet.put("result", 0);
									}
									//3.�����̷߳�����Ϣ
					  				Message tomain2=mainhand.obtainMessage();
					  				tomain2.what=EV_OPTMAIN;							
					  				tomain2.obj=allSet;
					  				mainhand.sendMessage(tomain2); // ������Ϣ
									
									break;
								case VboxProtocol.VBOX_COST_RPT://�ۿ���Ϣ
									cost_value=MoneyRec(ev_head6.getInt("value")); 
									g_holdValue = 0;//��ǰ�ݴ�ֽ�ҽ�� �Է�Ϊ��λ
									bill_recv=0;//ֽ������ǰ�ձҽ��	�Է�Ϊ��λ
									coin_recv=MoneyRec(ev_head6.getInt("total_value"));//Ӳ������ǰ�ձҽ��	�Է�Ϊ��λ
									ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadCostRpt<<cost_value="+cost_value+"GetAmountMoney="+GetAmountMoney(),"com.txt");
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EVprotocol.EV_MDB_COST);
									allSet.put("result", 1);
									allSet.put("bill_recv", bill_recv);
									allSet.put("coin_recv", coin_recv);
									//3.�����̷߳�����Ϣ
					  				Message tomain18=mainhand.obtainMessage();
					  				tomain18.what=EV_OPTMAIN;							
					  				tomain18.obj=allSet;
					  				mainhand.sendMessage(tomain18); // ������Ϣ
									
									break;	
								case VboxProtocol.VBOX_ACTION_RPT://�������ô���
									//���ӿڻص���Ϣ
									allSet.clear();
									int action=ev_head6.getInt("action");									
									//  ����                     ������ʼ                 ���ҿ�ʼ
									if((action==0)||(action==1)||(action==2))
									{										
									}
									else if(action==5)
									{
										int actvalue=ev_head6.getInt("value");
										ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadAction<<ά��ģʽ","com.txt");
										allSet.put("EV_TYPE", COMThread.EV_BUTTONRPT_MAINTAIN);
										allSet.put("btnvalue", actvalue);
										//3.�����̷߳�����Ϣ
						  				Message tomain19=mainhand.obtainMessage();
						  				tomain19.what=COMThread.EV_BUTTONMAIN;							
						  				tomain19.obj=allSet;
						  				mainhand.sendMessage(tomain19); // ������Ϣ
									}
									
									break;
								case VboxProtocol.VBOX_REQUEST://���������ô���
									//ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadRequest<<","com.txt");
									break;	
								case VboxProtocol.VBOX_BUTTON_RPT://������Ϣ
									//���ӿڻص���Ϣ
									allSet.clear();
									int type=ev_head6.getInt("type");									
									if(type==0)
									{
										ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadButtonRpt<<Game","com.txt");
										allSet.put("EV_TYPE", COMThread.EV_BUTTONRPT_GAME);
									}
									else if(type==1)
									{
										int btntype=ev_head6.getInt("type");
										int btnvalue=ev_head6.getInt("value");
										ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadButtonRpt<<Huodaobtntype="+btntype+"btnvalue="+btnvalue,"com.txt");
										allSet.put("EV_TYPE", COMThread.EV_BUTTONRPT_HUODAO);
										allSet.put("btnvalue", btnvalue);
									}
									else if(type==2)
									{
										int btntype=ev_head6.getInt("type");
										int btnvalue=ev_head6.getInt("value");
										ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadButtonRpt<<Spbtntype="+btntype+"btnvalue="+btnvalue,"com.txt");
										allSet.put("EV_TYPE", COMThread.EV_BUTTONRPT_SP);
										allSet.put("btnvalue", btnvalue);
									}
									else if(type==4)
									{
										ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadButtonRpt<<Return","com.txt");
										allSet.put("EV_TYPE", COMThread.EV_BUTTONRPT_RETURN);
									}
									
									//3.�����̷߳�����Ϣ
					  				Message tomain19=mainhand.obtainMessage();
					  				tomain19.what=COMThread.EV_BUTTONMAIN;							
					  				tomain19.obj=allSet;
					  				mainhand.sendMessage(tomain19); // ������Ϣ
									break;
								case VboxProtocol.VBOX_STATUS_RPT://����״̬
									int bv_st=ev_head6.getInt("bv_st");
									int cc_st=ev_head6.getInt("cc_st");
									int vmc_st=ev_head6.getInt("vmc_st");
									int change=ev_head6.getInt("change");
									coin_remain=MoneyRec(change);
									ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadStatusRpt<<bv_st"+bv_st+"cc_st="+cc_st+"vmc_st="+vmc_st+"coin_remain="+coin_remain,"com.txt");
									break;	
								case VboxProtocol.VBOX_HUODAO_RPT://������Ϣ
									ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadHuodao<<"+resjson.toString(),"com.txt");
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", VboxProtocol.VBOX_HUODAO_RPT);
									allSet.put("cool", 0);
									allSet.put("hot", 0);
									allSet.put("light", 0);
									JSONArray arr=ev_head6.getJSONArray("huodao");//����json����
									//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����2:"+arr.toString());
									for(int i=0;i<arr.length();i++)
									{
										JSONObject object2=arr.getJSONObject(i);
										allSet.put(String.valueOf(object2.getInt("no")), object2.getInt("remain"));								
									}											
									//��ʾ�ǵ�һ�γ�ʼ�����
									if(onInit==4)
									{
										//��������ֵ
										devopt=0;
										cmdSend=false;
										onInit=0;
										ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadHuodaoInit<<"+allSet,"com.txt");
										//3.�����̷߳�����Ϣ
										Message tomain=mainhand.obtainMessage();
						  				tomain.what=COMThread.EV_CHECKALLMAIN;							
						  				tomain.obj=allSet;
						  				mainhand.sendMessage(tomain); // ������Ϣ
									}
									else if(cmdSend)
									{
										//��������ֵ
										devopt=0;
										cmdSend=false;										
										ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadHuodaoRpt<<"+allSet,"com.txt");
										//3.�����̷߳�����Ϣ
										Message tomain=mainhand.obtainMessage();
						  				tomain.what=COMThread.EV_CHECKMAIN;							
						  				tomain.obj=allSet;
						  				mainhand.sendMessage(tomain); // ������Ϣ										
									}
									break;								
								case VboxProtocol.VBOX_INFO_RPT:
									int infotype=ev_head6.getInt("type");
									if(infotype==3)//��ǰ���
									{
										int total_value=ev_head6.getInt("total_value");
										ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadInfototal<<total_value="+total_value,"com.txt");
									}									
									break;									
								default:								
									ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadDefault<<"+resjson.toString(),"com.txt");
									break;
							}	
							
						}
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	        	
        	}
        } 
    };

}
