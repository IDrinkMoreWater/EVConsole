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
	int bill=0;
	int coin=0;
	int opt=0;
	
	
	
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
//										
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
								case VboxProtocol.VBOX_ACTION_RPT://�������ô���
									break;	
								case VboxProtocol.VBOX_STATUS_RPT://����״̬
									int bv_st=ev_head6.getInt("bv_st");
									int cc_st=ev_head6.getInt("cc_st");
									int vmc_st=ev_head6.getInt("vmc_st");
									int change=ev_head6.getInt("change");
									ToolClass.Log(ToolClass.INFO,"EV_COM","ExtraStatusRpt<<bv_st"+bv_st+"cc_st="+cc_st+"vmc_st="+vmc_st+"change="+change,"com.txt");
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
