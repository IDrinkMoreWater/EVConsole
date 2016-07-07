package com.easivend.evprotocol;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
	Timer timer = new Timer(); 
	private static Map<String,Object> allSet = new LinkedHashMap<String,Object>() ;
	boolean onInit=false;
	boolean cmdSend=false;//true���͵�����ȴ�ACKȷ��
	int devopt=0;//����״ֵ̬	
	int statusnum=0;//20����һ��get_status
	//�ֽ��豸ʹ�ܽ���
	int bill=0;
	int coin=0;
	int opt=0;
	//�ֽ��豸���	
	int coin_remain=0;//Ӳ������ǰ���ҽ��	�Է�Ϊ��λ
	int payback_value=0;//������
	
	int g_holdValue = 0;//��ǰ�ݴ�ֽ�ҽ�� �Է�Ϊ��λ
	int coin_recv=0;//Ӳ������ǰ�ձҽ��	�Է�Ϊ��λ
	int bill_recv=0;//ֽ������ǰ�ձҽ��	�Է�Ϊ��λ
	
	int cost_value=0;//�ֽ��豸�ۿ���
	
	int billPay=0;//ֽ���˱ҽ��
	int coinPay=0;//Ӳ���˱ҽ��
	
	
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
				//���ӹ�
				case 0x01://���߳̽������̸߳��Ӳ�ѯ��Ϣ
					ToolClass.Log(ToolClass.INFO,"EV_COM","COMExtraThread="+msg.obj,"com.txt");	
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
					int billPay16=0;
					int coinPay16=0;
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
				}
			}
		};	
		//Init();
		timer.scheduleAtFixedRate(task, 5000, 100);       // timeTask
		Looper.loop();//�û��Լ�������࣬�����߳���Ҫ�Լ�׼��loop		 
	}
	
	private void Init()
	{
		if(ToolClass.getExtraComType()>0)
    	{
        	//ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraThread Timer["+Thread.currentThread().getId()+"]","com.txt");
        	String resjson = VboxProtocol.VboxReadMsg(ToolClass.getExtracom_id(),100);
        	
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
						ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraAPI<<"+resjson.toString(),"com.txt");
						int F7=ev_head6.getInt("F7");
						if(mt==VboxProtocol.VBOX_POLL)
						{
							//1.��ȡsetup
							VboxProtocol.VboxGetSetup(ToolClass.getExtracom_id());
							String resjson1 = VboxProtocol.VboxReadMsg(ToolClass.getExtracom_id(),100);
							ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraAPI<<"+resjson1.toString(),"com.txt");
							//2.��ȡgethuodao
							VboxProtocol.VboxGetHuoDao(ToolClass.getExtracom_id(),0);
							resjson1 = VboxProtocol.VboxReadMsg(ToolClass.getExtracom_id(),100);
							ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraAPI<<"+resjson1.toString(),"com.txt");
							
							onInit=true;
						}
					}
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	        	
    	}
	}
	TimerTask task = new TimerTask() 
	{ 
        @Override 
        public void run() 
        { 
        	if(ToolClass.getExtraComType()>0)
        	{
	        	//ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraThread Timer["+Thread.currentThread().getId()+"]","com.txt");
	        	String resjson = VboxProtocol.VboxReadMsg(ToolClass.getExtracom_id(),100);
	        	
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
									ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraACK<<ACK","com.txt");
									if(cmdSend)
									{
										switch(devopt)
										{
											case EVprotocol.EV_MDB_ENABLE://���߳̽������߳��ֽ��豸ʹ�ܽ���	
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadExtraRec=bill="+bill+"coin="+coin+"opt="+opt,"com.txt");
												//���ӿڻص���Ϣ
												allSet.clear();
												allSet.put("EV_TYPE", EVprotocol.EV_MDB_ENABLE);
												allSet.put("opt", opt);
												allSet.put("bill_result", bill);
												allSet.put("coin_result", coin);
												//��������ֵ
												devopt=0;
												cmdSend=false;
												//3.�����̷߳�����Ϣ
												Message tomain=mainhand.obtainMessage();
												tomain.what=EV_OPTMAIN;							
												tomain.obj=allSet;
												mainhand.sendMessage(tomain); // ������Ϣ
												break;	
											case EVprotocol.EV_MDB_PAYOUT://MDB�˱�
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
										}
									}
									break;
								case VboxProtocol.VBOX_NAK_RPT:	
									ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraNAK<<NAK","com.txt");
									if(cmdSend)
									{
										switch(devopt)
										{
											case EVprotocol.EV_MDB_ENABLE://���߳̽������߳��ֽ��豸ʹ�ܽ���	
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadExtraRec=bill="+bill+"coin="+coin+"opt="+opt,"com.txt");
												//���ӿڻص���Ϣ
												allSet.clear();
												allSet.put("EV_TYPE", EVprotocol.EV_MDB_ENABLE);
												allSet.put("opt", opt);
												allSet.put("bill_result", 0);
												allSet.put("coin_result", 0);
												//��������ֵ
												devopt=0;
												cmdSend=false;	
												//3.�����̷߳�����Ϣ
												Message tomain=mainhand.obtainMessage();
												tomain.what=EV_OPTMAIN;							
												tomain.obj=allSet;
												mainhand.sendMessage(tomain); // ������Ϣ
												break;
											case EVprotocol.EV_MDB_PAYOUT://MDB�˱�
												//��������ֵ
												devopt=0;
												cmdSend=false;	
												//���ӿڻص���Ϣ
												allSet.clear();
												allSet.put("EV_TYPE", EVprotocol.EV_MDB_PAYOUT);
												allSet.put("result", 1);
												allSet.put("bill_changed", 0);
												allSet.put("coin_changed", 0);
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
										}
									}
									break;	
								case VboxProtocol.VBOX_POLL:								
									//ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraPOLL<<"+resjson.toString(),"com.txt");
									switch(devopt)
									{
										case EVprotocol.EV_MDB_ENABLE://���߳̽������߳��ֽ��豸ʹ�ܽ���	
											if(cmdSend==false)
											{
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadExtraSend0.2=bill="+bill+"coin="+coin+"opt="+opt,"com.txt");
												VboxProtocol.VboxControlInd(ToolClass.getExtracom_id(),2,opt);
												cmdSend=true;
											}
											break;
										case EVprotocol.EV_MDB_PAYOUT://MDB�˱�
											if(cmdSend==false)
											{
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSend0.2=bill="+bill+"coin="+coin+"billPay="+billPay+"coinPay="+coinPay,"com.txt");
												VboxProtocol.VboxPayoutInd(ToolClass.getExtracom_id(),0,coinPay/10,2);
												cmdSend=true;
											}											
											break;
										case EVprotocol.EV_MDB_HEART://������ѯ�ӿ�
											ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadExtraHeart","com.txt");
											if(++statusnum>2)
											{
												statusnum=0;
												VboxProtocol.VboxGetStatus(ToolClass.getExtracom_id());
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadExtraGetStatus","com.txt");
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
												ToolClass.Log(ToolClass.INFO,"EV_COM","ThreadSend0.2=cost="+cost_value,"com.txt");
												VboxProtocol.VboxCostInd(ToolClass.getExtracom_id(),0,cost_value/10,2);
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
									int hd_num=ev_head6.getInt("hd_num");
									ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraSetupRpt<<hd_num="+hd_num,"com.txt");
									break;
								case VboxProtocol.VBOX_PAYIN_RPT://Ͷ����Ϣ
									int dt=ev_head6.getInt("dt");
									int value=ev_head6.getInt("value");
									if(dt==0)
									{
										coin_recv+=value*10;
									}
									else if(dt==100)
									{
										g_holdValue=value*10;
									}
									else if(dt==101)
									{
										g_holdValue=0;
									}
									else if(dt==1)
									{
										bill_recv+=value*10;
										g_holdValue=0;
									}
									ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraPayinRpt<<dt="+dt+"value="+(value*10)+"GetAmountMoney="+GetAmountMoney(),"com.txt");
									break;	
								case VboxProtocol.VBOX_PAYOUT_RPT://�ұ���Ϣ
									payback_value=ev_head6.getInt("value")*10; 
									g_holdValue = 0;//��ǰ�ݴ�ֽ�ҽ�� �Է�Ϊ��λ
									bill_recv=0;//ֽ������ǰ�ձҽ��	�Է�Ϊ��λ
									coin_recv=ev_head6.getInt("total_value")*10; ;//Ӳ������ǰ�ձҽ��	�Է�Ϊ��λ
									ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraPayoutRpt<<payback_value="+payback_value+"GetAmountMoney="+GetAmountMoney(),"com.txt");
									break;
								case VboxProtocol.VBOX_COST_RPT://�ۿ���Ϣ
									cost_value=ev_head6.getInt("value")*10; 
									g_holdValue = 0;//��ǰ�ݴ�ֽ�ҽ�� �Է�Ϊ��λ
									bill_recv=0;//ֽ������ǰ�ձҽ��	�Է�Ϊ��λ
									coin_recv=ev_head6.getInt("total_value")*10; ;//Ӳ������ǰ�ձҽ��	�Է�Ϊ��λ
									ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraCostRpt<<cost_value="+cost_value+"GetAmountMoney="+GetAmountMoney(),"com.txt");
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
									break;	
								case VboxProtocol.VBOX_STATUS_RPT://����״̬
									int bv_st=ev_head6.getInt("bv_st");
									int cc_st=ev_head6.getInt("cc_st");
									int vmc_st=ev_head6.getInt("vmc_st");
									int change=ev_head6.getInt("change");
									coin_remain=change*10;
									ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraStatusRpt<<bv_st"+bv_st+"cc_st="+cc_st+"vmc_st="+vmc_st+"coin_remain="+coin_remain,"com.txt");
									break;	
								case VboxProtocol.VBOX_HUODAO_RPT://������Ϣ
									int[] huodao=new int[21];
									huodao[0]=ev_head6.getInt("huodao1");
									huodao[1]=ev_head6.getInt("huodao2");
									huodao[2]=ev_head6.getInt("huodao3");
									huodao[3]=ev_head6.getInt("huodao4");
									huodao[4]=ev_head6.getInt("huodao5");
									huodao[5]=ev_head6.getInt("huodao6");
									huodao[6]=ev_head6.getInt("huodao7");
									huodao[7]=ev_head6.getInt("huodao8");
									huodao[8]=ev_head6.getInt("huodao9");
									huodao[9]=ev_head6.getInt("huodao10");
									huodao[10]=ev_head6.getInt("huodao11");
									huodao[11]=ev_head6.getInt("huodao12");
									huodao[12]=ev_head6.getInt("huodao13");
									huodao[13]=ev_head6.getInt("huodao14");
									huodao[14]=ev_head6.getInt("huodao15");
									huodao[15]=ev_head6.getInt("huodao16");
									huodao[16]=ev_head6.getInt("huodao17");
									huodao[17]=ev_head6.getInt("huodao18");
									huodao[18]=ev_head6.getInt("huodao19");
									huodao[19]=ev_head6.getInt("huodao20");
									huodao[20]=ev_head6.getInt("huodao21");
									ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraHuodaoRpt<<"+huodao,"com.txt");
									break;								
								case VboxProtocol.VBOX_INFO_RPT:
									int infotype=ev_head6.getInt("type");
									if(infotype==3)//��ǰ���
									{
										int total_value=ev_head6.getInt("total_value");
										ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraInfototal<<total_value="+total_value,"com.txt");
									}									
									break;									
								default:								
									ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraDefault<<"+resjson.toString(),"com.txt");
									break;
							}	
							
//							
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
